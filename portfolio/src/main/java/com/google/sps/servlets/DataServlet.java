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
/**
*
**/
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;



import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
   // Converts message to JSON string
   String message = messageGson(messages());
    response.setContentType("application/json;");
   response.getWriter().println(message);

  }
    // Will have messages that are presented on the screen
    public String messages() {
    ArrayList<String> message = new ArrayList<String>();
      message.add("This is my first time using Json");
      message.add("I wonder if a guy named Jason made Json");
      message.add("I love pie");
    return message.get(0);
  }

  private String messageGson(String message) {
    Gson gson = new Gson();
    String json = gson.toJson(message);
    return json;
  }
}
