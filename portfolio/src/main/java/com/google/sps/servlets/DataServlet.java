// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.ArrayList;



/**
* Creating a servlet that stores comments as a JSON list and use JavaScript that builds UI from
* that data. 
**/
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // adds comments to datastore 
    String comment = request.getParameter("comments");
    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comments", comment);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    // takes query and puts all data into an arraylist.
    ArrayList<String> commentList = new ArrayList<String>();
    Query query = new Query("Task");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String commentEntity = (String) entity.getProperty("comments");
      commentList.add(commentEntity);
    }


    String commentNumString = request.getParameter("numComments");
    System.out.println(commentNumString);
    int commentNum = numComments(commentNumString); 

    // Converts message to JSON string
    response.setContentType("application/json;");
    for(int i = 0; i < commentNum; i++) {
       comment = messageGson(commentList.get(i));
       response.getWriter().println(comment);
    }
  }

    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // sends user input to doGet
    doGet(request,response);
    response.sendRedirect("/index.html");
  }

    // Takes user comments and adds them to list
    private String messages(String comment) {
      return comment;
  }

  // JSON messages to string  
  private static String messageGson(String  message ) {
    Gson gson = new Gson();
    String json = gson.toJson(message);
    return json;
  }

  private int numComments(String number) {
      int commentNumber;
      try {
        commentNumber = Integer.parseInt(number);
       } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + number);
      return -1;
    }

    return commentNumber;
    
  }
}
