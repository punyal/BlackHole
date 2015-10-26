/**
 * rockbolt.js
 *
 *  @author: Pablo Puñal Pereira <pablo.punal@ltu.se>
 */

function sendAlarm() {
  window.alert("ALARM");
  $('#RockBolt-281').remove();
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
      $('#rockBoltsConnected').text(data.connected_devices);

      console.log("success"+JSON.stringify(data));
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

  setTimeout(function() {
    $('#listRockBolts').append('<th>RockBolt-282</th><td>fdfd:0:0:0:180b:70d7:fdb4:9fbc</td><td><a href="#" class="btnSmall">Select</a></td><td><a href="#" class="btnTiny" onclick="sendAlarm();">HERE</a> <a href="#" class="btnTiny">OFF</a></td></tr>');


  }, 2000);
});
