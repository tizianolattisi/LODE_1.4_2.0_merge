

var estensione = "jpg";

var tempo = "4000";

var cartella;
var num_immagini;

var ws_images;
var ws_load;

var idx;
var num;

var period;
var running;

var extension;

var img;

function oscroller_init() {
	ws_images = new Array();
	ws_load = new Array();
		
	idx = 0;
	num = num_immagini;
	period = tempo;
		
	extension = "." + estensione;

	running = true;

	setTimeout('scroll()',period);
}
		
function scroll(img) {
	if(running) {
		idx++;
		if(idx>=num) {
			idx=0;
		}
		img.src = ws_images[idx].src;
		
		setTimeout("scroll()",period);
	}
}
		
function stop() {
	running = false;
}
		
function show(i) {
	stop();
	idx=i;
	document.images.partenza.src = ws_images[idx].src;
}
		
function run(image,path) {
    img = image;
    loadCartella(path);
    load();
	if(!running) {
		running=true;
		scroll(img);
	}
}
		
function load() {
	for(i=0;i<num;i++) {
		loadImage(i);
	}
}
		
function loadImage(i) {
	ws_images[i] = new Image();
	ws_images[i].src = cartella + i + extension;
}

function loadCartella(path){
    cartella = path;
}