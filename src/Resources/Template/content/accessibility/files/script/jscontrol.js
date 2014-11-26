var currentslide = -1; //Slide corrente
var currenttime = 0; //Secondo corrente


//ADD LISTNERS TO THE PLAYERREADY METHOD
function addListeners()
{
	player.addControllerListener("PLAY", "playStateTracker");
	playlist = player.getPlaylist();
    if(playlist.length > 0)
    {
        player.addModelListener('TIME', 'timeTracker');
    }
    else
    {
       setTimeout("addListeners();", 100);
    }
};

// gets the pause/play state of the player
function playStateTracker(obj) {
	playerState = obj.state;
};

//CHECK TIME
function timeTracker(obj)
{
	currenttime = obj.position;
	checkposition();
};

//CHECK WHEN STOP
function checkposition()
{
	if(currentslide >= 0)
	{
		endtime = chapters[2][currentslide];
		if(currenttime>=endtime)
		{
			player.sendEvent("PLAY", false);			
			if(!(settings["looping"]))			
			{
				jumper(currentslide,false);
			}
			else
			{
				jumper((currentslide+1),true);
			}
		}
	}
	else
	{
		jumper(0);
	}
};

// control play pause function
function togglePausePlay() {
	player.sendEvent("PLAY", !playerState);
}


// jump to a specific point (in secs)
//Please set true firstcall only if you call the slide the first time. Otherwise you get a loop after the slide end.
function jumper(slidenumber,firstcall) {
	//Set new video end time
	currentslide = slidenumber;
	startsec = chapters[1][slidenumber];
	//PLAY BEFORE SEEK
	player.sendEvent("PLAY",true);
	//Seeking to the start position
	player.sendEvent("SEEK", startsec);
	//PAUSE to enhance usability if autoplay is false
	if((!(settings['autoplay']))||(!firstcall))
	{
		player.sendEvent("PLAY",false);
	}
	
	getInfo();//Provide information about selected slide
	imgProvider(slidenumber); //Provide image and alternative text
	selectView(settings['viewmode']); //Set previously selected viewmode
	if(slidenumber !=0)
	{
	window.location.href="#infocurrent";  //Move the cursor to the lesson's title
	}
};

var player;
var playerState;	

//WHAT'S to DO when player is ready?
function playerReady(thePlayer) {
	player = document.getElementById('playID');
	addListeners();
	playerState = player.getConfig();
};

//LINK CREATION FUNCTION
function genchapters() {
var i = 0;
var retval = "";
for(i = 0; i<chapters[0].length; i++)
{
   retval = retval + "<tr><td><a href=\"javascript:jumper(" + i +",true);\">" + chapters[0][i] + "</td></tr>";
}
  return retval;
};

//GO TO NEXT SLIDE
function nextSlide() {
	if(currentslide+1<chapters[1].length)
	{
		jumper(currentslide+1);
	}
	else
	{
		if(!(settings['english']))
		{
		alert("Spiacente, questa era l'ultima slide");
		}
		else
		{
		alert("Sorry! That was the Last slide");
		}
	}
};

//GO TO PREVIOUS SLIDE
function prevSlide() {
	if(currentslide-1>=0)
	{
		jumper(currentslide-1);
	}
	else
	{
		if(!(settings['english']))
		{
		alert("Spiacente, questa era la prima slide");
		}
		else
		{
		alert("Sorry! That was the First slide");
		}
	}
};

//FORWARD
function forward()
{
	seconds = 15;
	if(currenttime+seconds >= chapters[2][currentslide])
	{
		seconds = chapters[2][currentslide] - currenttime;
	}
	//PLAY BEFORE SEEK
	player.sendEvent("PLAY",true);
	//Seeking to the start position
	player.sendEvent("SEEK", currenttime+seconds);
	//PAUSE to enhance usability
	player.sendEvent("PLAY",true);
}

//REWIND
function rewind()
{
	seconds = 20;
	if(currenttime-seconds<0)
	{
		seconds = currenttime-chapters[1][currentslide];
	}
	//PLAY BEFORE SEEK
	player.sendEvent("PLAY",true);
	//Seeking to the start position
	player.sendEvent("SEEK", currenttime-seconds);
	//PAUSE to enhance usability
	player.sendEvent("PLAY",true);
}

function getInfo()
{
	if(currentslide>=0)
	{
		document.getElementById('infocurrent').innerHTML = ("<h2>Slide "+ (currentslide + 1) + ": "+chapters[0][currentslide]+"</h2>");
	}
}

//Provide current image and alternative text insertion string
function imgProvider(slidenum)
{
	slideref = chapters[3][slidenum]
	if(settings["textonly"])
	{
		if(!(settings['english']))
		{
		document.getElementById('slideshow').innerHTML =  ("Inizio descrizione slide. \n"+imgs[1][slideref]+"Fine descrizione slide.");
	}
		else
		{
		document.getElementById('slideshow').innerHTML =  ("Start slide description. \n"+imgs[1][slideref]+"End slide description");
		}
	}
	else
	{
		if(!(settings['english']))
		{
		document.getElementById('slideshow').innerHTML =  ("<p align=\"center\"><img height=\"330\" id=\"slideimg\" src=\""+imgs[0][slideref]+"\" alt=\"Inizio descrizione slide. "+imgs[2][slideref]+". Fine descrizione slide.\"/></p>");
		}
		else
		{
			document.getElementById('slideshow').innerHTML =  ("<p align=\"center\"><img height=\"330\" id=\"slideimg\" src=\""+imgs[0][slideref]+"\" alt=\"Start slide description. "+imgs[2][slideref]+". End slide description.\"/></p>");
		}
	}
}

/*Provide multiple view selection:
Parameter "mode":
1. SplitView
2. SlideView
3. VideoView
*/
function selectView(mode)
{
	var slideshowid = slideshowid = document.getElementById('slideshow');

	if(mode==1) //SplitView
	{
		slideshowid.style.display = ''; //Slide show became visible
		document.getElementById("slideimg").height="330";
		document.getElementById("playID").height="330";
		document.getElementById("playID").width="412,5";
		settings['viewmode'] = 1;
	}
	else if(mode==2) // SlideView
	{
		slideshowid.style.display = ''; //Slide show became visible
		document.getElementById("slideimg").height="600";
		//Player became invisible
		player.height=1;
		player.width=1;
		settings['viewmode'] = 2;
	}
	else if(mode==3) //VideoView
	{
		slideshowid.style.display="none"; //SlideShow hidding
		//Big Video
		document.getElementById("playID").height="700";
		document.getElementById("playID").width="875";
		settings['viewmode'] = 3;
	}
}

function cycleView()
{
	var newview = settings["viewmode"]+1;
	if(settings['viewmode'] == 3)
	{
		newview = 1;
	}
	selectView(newview);
}

//Get streamer.php URL
function getStreamURL()
{
	return window.location.toString().replace(/accessibility.*/g,"streamer.php");
}

//SUPPORT FUNCTION
function getObj(name) {
	if (document.getElementById) {
		return document.getElementById(name);
	} else if (document.all) {
		return document.all[name];
	} else if (document.layers) {
		return document.layers[name];
	}
};
