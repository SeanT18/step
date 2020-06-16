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

//calls all functions to be loaded when starting the website
function loadAll() {
    userLogin();
    createMap();
}

/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
    const facts =
        ['My favorite animals are sloths', 'I had a phase where I was obssesed with Mayan mythology', 'My favorite anime is one piece', 'I love lasagna'];

    // Pick a random greeting.
    const fact = facts[Math.floor(Math.random() * facts.length)];

    // Add it to the page.
    const factContainer = document.getElementById('fact-container');
    factContainer.innerText = fact;
}

// recieves messages to be presented to the user
function addMessage() {
    var numComment = document.getElementById("numInput").value;
    fetch('/data?numComments=' + numComment).then(response => response.text())
    .then((message) => {
    document.getElementById('message-container').innerHTML = message;
    });
}

function deleteData() {
    const promise = fetch(new Request('/delete-data', {method: 'POST'}));
    promise.then(() => {
    addComments();
    });
}


function userLogin() {
  fetch('/login').then(response => response.text())
  .then((message) => {
    document.getElementById('login-container').innerHTML = message;
  });
}

function createMap() {
  const map = new google.maps.Map(
  document.getElementById('map'),
    {center: {lat: 35.2271, lng: -80.8431}, zoom: 10});

    var features = [
      new google.maps.Marker({
          position: {lat: 35.235661, lng: -80.903183},
          map: map,
          title: 'My High School'
  }),

      new google.maps.Marker({
          position: {lat: 35.190220, lng: -80.921910},
          map: map,
          title: 'This where I had lunch last week'
  }),

       new google.maps.Marker({
          position: {lat: 35.233580, lng: -80.876500},
          map: map,
          title: 'Best place to have chicken sandwiches in Charlotte also conviently 5 mins near me XD'
  }),
];
  // add a listener to each marker when clicked
  for(var i = 0; i < features.length;i++) {
      (function(x) {
        features[i].addListener('click', function() {
            map.setZoom(15);
            map.setCenter(features[x].getPosition());
        });
       })(i)
  }
}
