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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

    // adds comments to datastore 
    String comment = request.getParameter("comments");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    if(comment != null && !comment.equals("")) {
      Entity taskEntity = new Entity("Task");
      taskEntity.setProperty("comments", comment);
      taskEntity.setProperty("email", userEmail);
      datastore.put(taskEntity);
    }
    String commentNumString = request.getParameter("numComments");
    int commentNum = numComments(commentNumString); 
    
    // takes query and puts all data into an arraylist.
    ArrayList<String> commentList = new ArrayList<String>();
    Query query = new Query("Task");
    PreparedQuery results = datastore.prepare(query);

    // prints data as requested by the amount of comments requested
    int i = 0;
    response.setContentType("application/json;");
    for (Entity entity : results.asIterable()) {
      if(i < commentNum || commentNum > commentList.size()) {
        String commentEntity = (String) entity.getProperty("comments");
        String email = (String) entity.getProperty("email");
        String fullcomment = email + ": " + commentEntity;
        commentList.add(fullcomment);
        comment = messageGson(commentList.get(i));
        response.getWriter().println(fullcomment);
        i++;
      } else {
        break;
      }
    }
   } else {
      String urlToRedirectToAfterUserLogsIn = "/";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
      response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a> first.</p>");
  }
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // sends user input to doGet
    doGet(request,response);
    response.sendRedirect("/index.html");
  }
  
  // JSON messages to string  
  private static String messageGson(String message ) {
    Gson gson = new Gson();
    String json = gson.toJson(message);
    return json;
  }

  // converts the number to an int
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
