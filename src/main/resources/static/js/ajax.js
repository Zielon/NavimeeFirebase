var logs = {};

function start(){
    document.getElementsByClassName("loader")[0].style.display = "block";
    setInterval(function(){ loadDoc();}, 500)
}

function reset(){}

function loadDoc() {
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
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
                td.style.backgroundColor = 'Lavender';
            else if(log.type === "ADDITION")
                td.style.backgroundColor = 'LightYellow';
            else if(log.type === "RETRIEVAL")
                td.style.backgroundColor = 'LightSteelBlue';
            else if(log.type === "DELETION")
                td.style.backgroundColor = 'IndianRed';
            else if(log.type === "EXCEPTION"){
                td.style.backgroundColor = 'Crimson';
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