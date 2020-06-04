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
    // Converts message to JSON string
    String comment = messageGson(messages(request));
    response.setContentType("application/json;");
    response.getWriter().println(comment);
  }

    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
 //   String comment = messageGson(messages(request));
   // response.setContentType("application/json;");
    //response.getWriter().println(comment);
        doGet(request,response);

    response.sendRedirect("/index.html");

  }

    // Takes user comments and adds them to list
    private ArrayList<String> messages(HttpServletRequest request) {
      String comment = request.getParameter("comments");
      ArrayList<String> message = new ArrayList<String>();
       if(comment == null) {
           return message;
       }
          message.add(comment);
       
      return message;
  }

  // JSON messages to string  
  private static String messageGson(ArrayList<String>  message ) {
    Gson gson = new Gson();
    String json = gson.toJson(message);
    return json;
  }
}
