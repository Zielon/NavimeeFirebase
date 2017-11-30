var logs = {};

function start(){
    document.getElementsByClassName("loader")[0].style.display = "block";
    setInterval(function(){ loadDoc();}, 500)
}

function reset(){

}

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

    newLogs.filter(newLog => arrayLogs.every(log => newLog.id != log.id)).forEach(log => {
        var tr = document.createElement('tr');
        Object.keys(log).forEach(key => {
            var td = document.createElement('td');
            td.appendChild(document.createTextNode(key === "time" ? new Date(log[key]).toISOString() : log[key]))
            tr.appendChild(td)
        });
        document.getElementById("logs").appendChild(tr);
    });

    newLogs.forEach(log =>{
        logs[log.id] = log;
    });
};