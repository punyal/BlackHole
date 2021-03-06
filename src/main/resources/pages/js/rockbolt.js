/**
 * rockbolt.js
 *
 *  @author: Pablo Puñal Pereira <pablo.punal@ltu.se>
 */

var graphClient = null;
var vibrationData = new vis.DataSet();
var vibrationChart;
var strainData = new vis.DataSet();
var strainChart;

function getGraph(name) {
    graphClient = name;
    vibrationData.remove(vibrationData.getIds());
    strainData.remove(strainData.getIds());

}

var timeLineModeAutomatic = 1;
function autoUpdate(mode) {
    $('#timeLineUpdateMode').empty();
    if (mode) {
        $('#timeLineUpdateMode').append('<a class="btn update-easy-pie-chart" onclick="autoUpdate(0);"><i class="icon-repeat"></i> Automatic</a>');
    } else {
        $('#timeLineUpdateMode').append('<a class="btn update-easy-pie-chart" onclick="autoUpdate(1);"><i class="icon-hand-up"></i> Manual</a>');
    }
    timeLineModeAutomatic = mode;
    
}


function bootVibrationStrainChart() {
    var vibrationContainer = document.getElementById('vis_vibration');
    var strainContainer = document.getElementById('vis_strain');
    var vibrationOptions = {
        legend: true,
        dataAxis: {
            left: {
                range: {min:0, max:10}
            }
        }
    };
    var vibrationGroups = new vis.DataSet();
    vibrationGroups.add({
    id: 0,
    content: 'X',
    options: {
    }});
    vibrationGroups.add({
    id: 1,
    content: 'Y',
    options: {
    }});
    vibrationGroups.add({
    id: 2,
    content: 'Z',
    options: {
    }});

    var strainGroups = new vis.DataSet();
    strainGroups.add({
        id: 0,
        content: 'strain',
        options: {}
    });
    
    
    vibrationChart = new vis.Graph2d(vibrationContainer, vibrationData, vibrationGroups, vibrationOptions);
    var strainOptions = {
        dataAxis: {
            left: {
                range: {min:0, max:1000}
            }
        }
    };
    strainChart = new vis.Graph2d(strainContainer, strainData, strainGroups, strainOptions);
    var actualTime = Date.now();
    var prevTime = new Date();
    prevTime.setMinutes(prevTime.getMinutes() - 4);
    vibrationChart.setWindow(prevTime, actualTime);
    strainChart.setWindow(prevTime, actualTime);
}

function getVibrationStrainData() {
    vibrationChart.moveTo(Date.now());
    strainChart.moveTo(Date.now());
    if (graphClient) {
    var jSonReq = {"device": graphClient};
    $.ajax({
       url: "getVibrationStrainData",
       type: 'post',
       dataType: 'json',
       data: jSonReq,
        success: function (data) {
            
            vibrationData.remove(vibrationData.getIds());
            strainData.remove(strainData.getIds());
            
            for (var i=0; i<data.vibration.length ;i++) {
                vibrationData.add([{x: new Date(data.vibration[i].time) , y: data.vibration[i].X, group: 0}]);
                vibrationData.add([{x: new Date(data.vibration[i].time) , y: data.vibration[i].Y, group: 1}]);
                vibrationData.add([{x: new Date(data.vibration[i].time) , y: data.vibration[i].Z, group: 2}]);
            }
            
            for (var i=0; i<data.strain.length ;i++) {
                strainData.add([{x: new Date(data.strain[i].time) , y: data.strain[i].strain, group: 0}]);
            }
        },
        error: function (data, status, er) {
          console.log("error: "+data+" status: "+status+" er:"+er);
        }
       
    });
    } else {
        //console.log("No valid client for Charts");
    }
}



var timeLineData = new vis.DataSet();
var timeline;
function bootTimeLine() {
    
    //timeLineData.on('*', function (event, properties, senderId) {
    //    console.log('event', event, properties);
    //});
    
    var container = document.getElementById('vis_timeline');
    
    // Configuration for the Timeline
    var options = {
        autoResize: true
      };

    // Create a Timeline
    timeline = new vis.Timeline(container, timeLineData, options);
    var actualTime = Date.now();
    var prevTime = new Date();
    
    prevTime.setMinutes(prevTime.getMinutes() - 1);
    
    timeline.setWindow(prevTime, actualTime);
  
}

var lastTime = 0;

function getTimeLine() {
    if (timeLineModeAutomatic) timeline.moveTo(Date.now());
    
    /*
    timeLineData.add([
      {content: 'eventX', start: Date.now()}
    ]);*/
    var jSonReq = {"lastTime": lastTime};
    $.ajax({
       url: "getTimeLine",
       type: 'post',
       dataType: 'json',
       data: jSonReq,
        success: function (data) {
            lastTime = data.actualTime;
            for (var i=0; i<data.events.length ;i++) {
                timeLineData.add([{content: data.events[i].data, start: new Date(data.events[i].time)}]);
            }
        },
        error: function (data, status, er) {
          console.log("error: "+data+" status: "+status+" er:"+er);
        }
       
    });
    
    
}


function torch(name, mode) {
    console.log("Torch for "+name+" mode "+mode);
    var jSonReq = {"torchdata": name+"("+mode+")"};
    $.ajax({
        url: "torch",
        type: 'post',
        dataType: 'json',
        data: jSonReq
    });
}

var criticalMessage = "";

function getServerInfo() {
    /**
     * Incomming data expected:
     *  {
     *      "version": 0.2,
     *      "dateTime": "Y-M-d h:m:s z",
     *      "devicesConnected": 4,
     *      "totalAlarms": 1,
     *      "totalMessages": 123,
     *      "criticalAlertMessage": "RockBolt-204 broken!!"
     *  } 
     */
  $.ajax({
    url: "getServerInfo",
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json',
    mimeType: 'application/json',

    success: function (data) {
      $('#version').text(data.version);
      $('#dateTime').text(data.dateTime);
      $('#devicesConnected').text(data.devicesConnected);
      $('#totalAlarms').text(data.totalAlarms);
      $('#totalMessages').text(data.totalMessages);
      $('#criticalAlertMessage').text(data.criticalAlertMessage);
        if(criticalMessage.localeCompare(data.criticalAlertMessage)) {
            criticalMessage = data.criticalAlertMessage;
            window.alert(criticalMessage);
        }
    },
    error: function (data, status, er) {
      console.log("error: "+data+" status: "+status+" er:"+er);
    }
  });
}

function newRB(name, address, battery, in_com, out_com, vibration, strain, status, lastCon) {
    $('#RockBoltsList').append(
                '<tr id="'+name+'">'+
                    '<td>'+name+'</td>'+
                    '<td id="'+name+'Address">'+address+'</td>'+
                    '<td id="'+name+'Battery">'+battery+'</td>'+
                    '<td id="'+name+'In">'+in_com+'</td>'+
                    '<td id="'+name+'Out">'+out_com+'</td>'+
                    '<td id="'+name+'Vibration">'+vibration+'</td>'+
                    '<td id="'+name+'Strain">'+strain+'</td>'+
                    '<td>'+
                        '<button class="btn btn-success" onclick="torch(\''+name+'\',1);"><i class="icon-eye-open"></i> ON</button>'+
                        '<button class="btn btn-danger" onclick="torch(\''+name+'\',0);"><i class="icon-eye-close"></i> OFF</button>'+
                    '</td>'+
                    '<td>'+
                    '<button class="btn btn-primary" onclick="getGraph(\''+name+'\');"><i class="icon-ok"></i> Select</button>'+
                    '</td>'+
                    '<td id="'+name+'Status">'+status+'</td>'+
                    '<td id="'+name+'LastCon">'+lastCon+'</td>'+
                '</tr>');
}

function updateRB(name, address, battery, in_com, out_com, vibration, strain, status, lastCon) {    
    if ($('#'+name+'Address').text().localeCompare(address) !== 0) $('#'+name+'Address').text(address);
    if ($('#'+name+'Battery').text().localeCompare(battery) !== 0) $('#'+name+'Battery').text(battery);
    if ($('#'+name+'In').text().localeCompare(in_com) !== 0) $('#'+name+'In').text(in_com);
    if ($('#'+name+'Out').text().localeCompare(out_com) !== 0) $('#'+name+'Out').text(out_com);
    if ($('#'+name+'Vibration').text().localeCompare(vibration) !== 0) $('#'+name+'Vibration').text(vibration);
    if ($('#'+name+'Strain').text().localeCompare(strain) !== 0) $('#'+name+'Strain').text(strain);
    if ($('#'+name+'LastCon').text().localeCompare(lastCon) !== 0) $('#'+name+'LastCon').text(lastCon);
    if ($('#'+name+'Status').text().localeCompare(status) !== 0) {
        $('#'+name).empty();
        if (status.localeCompare("Offline")) {
            console.log(name+" changes to Online");
            $('#'+name).append('<td>'+name+'</td>'+
                    '<td id="'+name+'Address">'+address+'</td>'+
                    '<td id="'+name+'Battery">'+battery+'</td>'+
                    '<td id="'+name+'In">'+in_com+'</td>'+
                    '<td id="'+name+'Out">'+out_com+'</td>'+
                    '<td id="'+name+'Vibration">'+vibration+'</td>'+
                    '<td id="'+name+'Strain">'+strain+'</td>'+
                    '<td>'+
                        '<button class="btn btn-success" onclick="torch(\''+name+'\',1);"><i class="icon-eye-open"></i> ON</button>'+
                        '<button class="btn btn-danger" onclick="torch(\''+name+'\',0);"><i class="icon-eye-close"></i> OFF</button>'+
                    '</td>'+
                    '<td>'+
                    '<button class="btn btn-primary" onclick="getGraph(\''+name+'\');"><i class="icon-ok"></i> Select</button>'+
                    '</td>'+
                    '<td id="'+name+'Status">'+status+'</td>'+
                    '<td id="'+name+'LastCon">'+lastCon+'</td>');
        } else {
            console.log(name+" changes to Offline");
            $('#'+name).append('<td>'+name+'</td>'+
                    '<td id="'+name+'Address">'+address+'</td>'+
                    '<td id="'+name+'Battery">'+battery+'</td>'+
                    '<td id="'+name+'In">'+in_com+'</td>'+
                    '<td id="'+name+'Out">'+out_com+'</td>'+
                    '<td id="'+name+'Vibration">'+vibration+'</td>'+
                    '<td id="'+name+'Strain">'+strain+'</td>'+
                    '<td></td>'+
                    '<td></td>'+
                    '<td id="'+name+'Status">'+status+'</td>'+
                    '<td id="'+name+'LastCon">'+lastCon+'</td>');
        }
        
        
    }
}

function getRockBoltsList() {
    /**
     * Incomming data expected:
     *  {
     *      "devices": [
     *          {
     *              "name": "\"RockBolt-240\"",
     *              "address": "fdfd:0:0:0:5c0c:7122:c2df:4a58",
     *              "battery": "12%",
     *              "in_com": 123,
     *              "out_com": 23,
     *              "vibration": 34,
     *              "strain": 21,
     *              "status": "Online",
     *              "last_con": "2015-11-21 13:59:02 CET"
     *          }
     *      ]
     *  }
     */
  $.ajax({
    url: "getRockBoltsList",
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json',
    mimeType: 'application/json',

    success: function (data) {
        for (var i=0; i<data.devices.length ;i++) {
            if ($('#'+data.devices[i].name).length < 1)
                newRB(data.devices[i].name,
                      data.devices[i].address,
                      data.devices[i].battery,
                      data.devices[i].in_com,
                      data.devices[i].out_com,
                      data.devices[i].vibration,
                      data.devices[i].strain,
                      data.devices[i].status,
                      data.devices[i].last_con);
            else updateRB(data.devices[i].name,
                          data.devices[i].address,
                          data.devices[i].battery,
                          data.devices[i].in_com,
                          data.devices[i].out_com,
                          data.devices[i].vibration,
                          data.devices[i].strain,
                          data.devices[i].status,
                          data.devices[i].last_con);
        }
        
    },
    error: function (data, status, er) {
      console.log("error: "+data+" status: "+status+" er:"+er);
    }
  });
}


$(document).ready(function() {
  setInterval(function() {
    getServerInfo();
  }, 1000);
  
  setInterval(function() {
    getRockBoltsList();
  }, 1000);
  
  bootTimeLine();
  setInterval(function() {
    getTimeLine();
  }, 1000);
  
  
  bootVibrationStrainChart();
  setInterval(function() {
    getVibrationStrainData();
  }, 1000);
});
