/* JavaScripts for QuickChat */

var host = 'localhost:8080';

var $message = $('#message');
var $history = $('#history');

register();

// Registers to the server
function register() {
  var websocket = new WebSocket('ws://' + host + '/register');
  websocket.onmessage = function(evt) {
      var msg = jQuery.parseJSON(evt.data);
      addMessage(msg);
    };
}

// Adds a received message to the chat history
// msg: the message to add
function addMessage(msg) {
  var time = new Date();
  var t = ['<span class="time">', time.toLocaleTimeString(), '</span>'].join('');
  var author = ["<span class=\"author\">", msg.author, "</span>: "].join('');
                
  $history.append('<li>' + t + author + msg.msg +'</li>');
  $('#history li:last')[0].scrollIntoView();
}

// Sends the current message to the server
function sendToServer() {
  var msg = $.trim($message.val());
  if(msg) {
    var author = $.trim($('#name').val() || 'anonymous');
    $.post("/msg", {msg: msg, author: author}, function (resp) {
      $message.val('');
    });
  }
}

// Send button click handler
$("#send").on("click", function() {
    sendToServer();
});

// Send message to server, when enter is pressed
$message.focus().keyup(function (e) {
  if(e.which === 13) { // enter
    sendToServer();
  }
});