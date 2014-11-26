var isCtrlKey = false;   //Ctrl-key pressed
var isAltKey = false; //Alt-key pressed

//Key relised
document.onkeyup=function(e)
{ 
if(!e)//internet explorer...
{
	e = window.event;
	if((e.keyCode)==17) isCtrlKey=false;
	if((e.keyCode)==18) isAltKey=false;
}
else //standard browser
{ 
	if(e.which == 17) isCtrlKey=false;
	if(e.which == 18) isAltKey=false;
}
}

//Key pressed
document.onkeydown=function(e)
{ 
	if(!e)//IE....
	{
		e = window.event;
		if(e.keyCode == 17) isCtrlKey=true; 
		if(e.keyCode == 18) isAltKey=true; 
		//Se vengono premuti insieme ctrl+alt+e chiama playpausa
		if(e.keyCode == 69 && (isCtrlKey && isAltKey) ) 
		{ 
			togglePausePlay();
			return false;
		}
		if(e.keyCode == 65 && (isCtrlKey && isAltKey)) 
		{ 
			prevSlide();
			return false;
		}
		if(e.keyCode == 83 && (isCtrlKey && isAltKey)) 
		{ 
			nextSlide();
			return false;
		}
		
		if(e.keyCode == 81 && (isCtrlKey && isAltKey)) 
		{ 
			rewind();
			return false;
		}
		
		if(e.keyCode == 87 && (isCtrlKey && isAltKey)) 
		{ 
			forward();
			return false;
		}

		if(e.keyCode == 86 && (isCtrlKey && isAltKey)) 
		{ 
			cycleView();
			return false;
		}
	}
	else //standard browser
	{
		if(e.which == 17) isCtrlKey=true; 
		if(e.which == 18) isAltKey=true; 
		//Se vengono premuti insieme ctrl+alt+e chiama playpausa
		if(e.which == 69 && (isCtrlKey && isAltKey ))
		{ 
			togglePausePlay();
			return false;
		}
		//Se vengono premuti insieme ctrl+alt+a chiama prevSlide
		if(e.which == 65 && (isCtrlKey && isAltKey)) 
		{ 
			prevSlide();
			return false;
		}
		//Se vengono premuti insieme ctrl+alt+s chiama nextSlide
		if(e.which == 83 && (isCtrlKey && isAltKey))
		{ 
			nextSlide();
			return false;
		}
		//Se vengono premuti insieme ctrl+alt+q chiama rewind
		if(e.which == 81 && (isCtrlKey && isAltKey)) 
		{ 
			rewind();
			return false;
		}
		//Se vengono premuti insieme ctrl+alt+w chiama forward
		if(e.which == 87 && (isCtrlKey && isAltKey)) 
		{ 
			forward();
			return false;
		}
		//Modalità di visualizzazione
		if(e.which == 86 && (isCtrlKey && isAltKey ))
		{ 
			cycleView();
			return false;
		}
	}
}