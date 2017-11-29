var logs = {};

function start(){
    setInterval(function(){ loadDoc();}, 500)
}

function reset(){

}

function loadDoc() {
      var xhttp = new XMLHttpRequest();
      xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
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
            td.appendChild(document.createTextNode(log[key]))
            tr.appendChild(td)
        });
        document.getElementById("logs").appendChild(tr);
    });

    newLogs.forEach(log =>{
        logs[log.id] = log;
    });
};