var logs = {};
var intervalId = null;
var isResetting = false;

function start(){
    document.getElementsByClassName("loader")[0].style.display = "block";
    intervalId = setInterval(function(){ loadDoc();}, 500)
}

function reset(){
      clearInterval(intervalId);
      logs = {};
      isResetting = true;
      document.getElementById("logs").innerHTML = "";
      document.getElementsByClassName("loader")[0].style.display = "block";
      document.getElementById("startButton").disabled = true;
      document.getElementById("resetButton").disabled = true;
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                isResetting = false;
                document.getElementsByClassName("loader")[0].style.display = "none";
                document.getElementById("startButton").disabled = false;
                 document.getElementById("resetButton").disabled = false;
                document.getElementById("logs").innerHTML = "";
            }
      };
      xhttp.open("POST", "api/logs/delete", true);
      xhttp.send();}

function loadDoc() {
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200 && !isResetting) {
                document.getElementsByClassName("loader")[0].style.display = "none";
                addLogs(JSON.parse(this.responseText));
            }
      };
      xhttp.open("GET", "api/logs/all", true);
      xhttp.send();
};

function addLogs(newLogs){

    var arrayLogs = Object.keys(logs).map(key => { return logs[key]; });
    var lastLog = "";
    newLogs.filter(newLog => arrayLogs.every(log => newLog.id != log.id)).forEach(log => {
        lastLog = log.id;
        var tr = document.createElement('tr');
        tr.setAttribute("id", lastLog)
        Object.keys(log).forEach(key => {

            var td = document.createElement('td');
            var div = document.createElement('div');

            // Background colors based on a type.
            if(log.type === "TASK")
                tr.style.backgroundColor = 'Lavender';
            else if(log.type === "ADDITION")
                tr.style.backgroundColor = 'LightYellow';
            else if(log.type === "RETRIEVAL")
                tr.style.backgroundColor = 'LightSteelBlue';
            else if(log.type === "DELETION")
                tr.style.backgroundColor = 'IndianRed';
            else if(log.type === "EXCEPTION"){
                tr.style.backgroundColor = 'Crimson';
                if(key === "reference")
                    div.className = "exception";
            }

            div.appendChild(document.createTextNode(key === "time" ? new Date(log[key]).toISOString() : !log[key] ? " - " : log[key]))
            td.appendChild(div);
            tr.appendChild(td);
        });
        document.getElementById("logs").appendChild(tr);
    });

    if(lastLog != "")
        $(`#${lastLog}`).eq(-1).attr("tabindex",-1).focus();

    newLogs.forEach(log => {
        logs[log.id] = log;
    });
};