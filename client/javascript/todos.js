// gets todos from the api.
// It adds the values of the various inputs to the requested URl to filter and order the returned todos.
function getFilteredTodos() {
  console.log("Getting todos");

  var url = "/api/todos?";
  if(document.getElementById("owner").value != "") {
    url = url + "&owner=" + getFieldValue("owner");
  }

  var statusValue = document.getElementById("status").value;
  if(statusValue != "") {
    var statusBool = (statusValue.toLowerCase() === "complete");
    url = url + "&status=" + statusBool;
  }

  let checkedCategory = document.querySelector('input[name="category"]:checked').value;

  if (checkedCategory != "") {
    url = url + "&category=" + checkedCategory;
  }
  if(document.getElementById("status").value != "") {
    url = url + "&status=" + getFieldValue("status");
  }
  if(document.getElementById("contains").value != "") {
    url = url + "&contains=" + getFieldValue("contains");
  }
  if(document.getElementById("orderBy").value != "") {
    url = url + "&orderBy=" + getFieldValue("orderBy");
  }
  if(document.getElementById("limit").value != "") {
    url = url + "&limit=" + getFieldValue("limit");
  }

  get(url, function(returned_json){
    document.getElementById("requestUrl").innerHTML = url;
    document.getElementById('jsonDump').innerHTML = syntaxHighlight(JSON.stringify(returned_json, null, 2));
  });
}

//  getTodos() is called when the page loads to populate the table
function getTodos() {
  let limit = document.getElementById('limit').value; // Get the limit value from the input field
  fetch('/api/todos?limit=' + limit)
    .then((response) => {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      // ... existing logic ...
    });
}

