/**
 * rockbolt.js
 *
 *  @author: Pablo Puñal Pereira <pablo.punal@ltu.se>
 */

var TotalConnected = 0;

function sendAlarm() {
  window.alert("ALARM");
  $('#RockBolt-281').remove();
}

function getGraph(name) {
    window.alert("getGraph of "+name);
}

function torch(name, mode) {
    console.log("Torch ON for "+name+" mode "+mode);
    var jSonReq = {"torchdata": name+"("+mode+")"};
    $.ajax({
    url: "torch",
    type: 'post',
    dataType: 'json',
    data: jSonReq
  });
}


function newRockbolt(name, address, messages, alarms) {
    TotalConnected++;
    $('#listRockBolts').append('<tr id="'+name+'"><th>'+name+'</th><td id="'+name+'address">'+address+'</td><td id="'+name+'messages">'+messages+'</td><td id="'+name+'alarms">'+alarms+'</td><td><a href="" class="btnSmall" onclick="getGraph(\''+name+'\');">Select</a></td><td><a href="#" class="btnTiny" onclick="torch(\''+name+'\',1);">ON</a> <a href="#" class="btnTiny" onclick="torch(\''+name+'\',0);">OFF</a></td></tr>');
}

function updateRockbolt(name, address, messages, alarms) {
    TotalConnected++;
    $('#'+name+'address').text(address);
    $('#'+name+'messages').text(messages);
    $('#'+name+'alarms').text(alarms);
}

function getListOfRockBolts() {
  $.ajax({
    url: "listOfRockBolts",
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json',
    mimeType: 'application/json',

    success: function (data) {
      $('#timeDate').text(data.time_date);
      $('#rockBoltsConnected').text(data.devices.length);
      
      if (TotalConnected > data.devices.length) {
          $('#listRockBolts').text("");
          TotalConnected = 0;
      }
      
      for (var i=0; i<data.devices.length;i++) {
          if($('#'+data.devices[i].name).length < 1) newRockbolt(data.devices[i].name,data.devices[i].address, data.devices[i].messages, data.devices[i].alarms);
          else updateRockbolt(data.devices[i].name,data.devices[i].address, data.devices[i].messages, data.devices[i].alarms);
      }
    },
    error: function (data, status, er) {
      console.log("error: "+data+" status: "+status+" er:"+er);
    }
  });
}

$(document).ready(function() {
  setInterval(function() {
    getListOfRockBolts();
  }, 1000);

  /*setTimeout(function() {
    $('#listRockBolts').append('<th>RockBolt-282</th><td>fdfd:0:0:0:180b:70d7:fdb4:9fbc</td><td><a href="#" class="btnSmall">Select</a></td><td><a href="#" class="btnTiny" onclick="sendAlarm();">HERE</a> <a href="#" class="btnTiny">OFF</a></td></tr>');

  }, 2000);*/
});
