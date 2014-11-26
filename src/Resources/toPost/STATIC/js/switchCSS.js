
var style = "default";       // title del css di default
var cookiename = "Layout";   // nome del cookie
var days = 30;               // durata in giorni del cookie

function switchStyle(s) {
  if (!document.getElementsByTagName) return;
  var el = document.getElementsByTagName("link");
  for (var i = 0; i < el.length; i++ ) {
    if (el[i].getAttribute("rel").indexOf("style") != -1 && el[i].getAttribute("title")) {
      el[i].disabled = true;
      if (el[i].getAttribute("title") == s) el[i].disabled = false;
    }
  }
}

function loadStyle() {
  var c = getStyleCookie();
  if (c && c != style) {
    switchStyle(c);
    style = c;
  }
}

function setStyle(s) {
  if (s != style) {
    switchStyle(s);
    style = s;
    setStyleCookie();     
  }
}

window.onload = loadStyle;   // carica lo style swichter all' onload


// Funzioni di manipolazione dei cookie
function setCookie(name, value, expdays) {
  var now = new Date();
  var exp = new Date(now.getTime() + (1000*60*60*24*expdays));
  document.cookie = name + "=" + escape(value) + ";" +
                    "expires=" + exp.toGMTString() + ";" +
                    "path=/";
}

function delCookie(name) {   // fa scadere il cookie
  var now = new Date();
  var exp = new Date(now.getTime() - 1);
  document.cookie = name + "=;" +
                    "expires=" + exp.toGMTString() + ";" + 
                    "path=/";
}

function getCookie(name) {   // restituisce il valore del cookie
  var cname = name + "=";
  var dc = document.cookie;
  if (dc.length > 0) {
    var start = dc.indexOf(cname);
    if (start != -1) {
      start += cname.length;
      var stop = dc.indexOf(";", start);
      if (stop == -1) stop = dc.length;
      return unescape(dc.substring(start,stop));
    }
  }
  return null;
}

function setStyleCookie() {
  setCookie(cookiename, style, days);
}

function getStyleCookie() {
  return getCookie(cookiename);
}

function delStyleCookie() {
  delCookie(cookiename);
}

// Stylesheet per Netscape 4
// necessita di un css a parte
if(document.layers)
  document.writeln("<link rel='stylesheet' type='text/css' href='/nn4.css' />");
