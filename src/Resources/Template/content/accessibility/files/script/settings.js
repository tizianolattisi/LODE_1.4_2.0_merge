//Settings array that store user settings. Specified value are default value.
var settings = 
{
	'settingsvisible' : true,
	'english' : false,
	'looping' : false,
	'viewmode' : '1',
	'autoplay' : false,
	'noscroll' : false,
	'textonly' :	false
};

function printsettings()
{
if(settings['english'])
{
	return printsettingsen();
}
else
{
	return printsettingsita();
}
}

//Print settings in italian.
function printsettingsita()
{
return "<form><p><input type=\"checkbox\" value=\"img\" name=\"img\" onClick=\"changetext()\">Visualizza solo testo  al posto dell'immagine della slide (attivare questa opzione se si fa uso di screen reader)</p></form><form><p><input type=\"checkbox\" value=\"autoplay\" name=\"autoplay\" onClick=\"setAutoplay()\">Avvia automaticamente l'audio e il video quando passi ad una nuova slide (sconsigliato per chi fa uso di sintetizzatore vocale)</p></form><form><p><input type=\"checkbox\" value=\"loop\" name=\"loop\" onClick=\"setLooping()\">Al termine dell'ascolto di una slide passa automaticamente alla successiva</p></form><form><p><input type=\"checkbox\" value=\"dragon\" name=\"dragon\" onClick=\"toggleScroll()\">Visualizza elenco slide senza barra di scorrimento (Opzione per utenti DNS)</p></form>";
}

//Print settings in english
function printsettingsen()
{
return "<form><p><input type=\"checkbox\" value=\"img\" name=\"img\" onClick=\"changetext()\">Replace slide image with text description (consider to use this option if you are using a screen reader software)</p></form><form><p><input type=\"checkbox\" value=\"autoplay\" name=\"autoplay\" onClick=\"setAutoplay()\">Start audio/video automatically when you jump to another slide (don't check if you are using a speech synthetizer)</p></form><form><p><input type=\"checkbox\" value=\"loop\" name=\"loop\" onClick=\"setLooping()\">Skip to the next slide when video ends. </p></form><form><p><input type=\"checkbox\" value=\"dragon\" name=\"dragon\" onClick=\"toggleScroll()\">Show slide list without scrollbar (Select this option if you are using DNS)</p></form>";
}


//------------SWICHES-------------------
//Switch setting when user check/uncheck it.

function changetext()
{
	settings["textonly"] = !settings["textonly"];
	jumper(currentslide);
}

function setLooping()
{
	settings["looping"] = !settings["looping"];
}

function setAutoplay()
{
	settings["autoplay"] = !settings["autoplay"];
}

function showhideSettings()
{
	if(settings["settingsvisible"])
	{
		document.getElementById('settings_area').style.display = 'none'; 
	}
	else
	{
		document.getElementById('settings_area').style.display = ''; 
	}
	settings["settingsvisible"] = !settings["settingsvisible"];
	return;
}

function toggleScroll()
{
	if(settings["noscroll"])
	{
		document.getElementById("chapterlist").style.overflow = "auto";
		document.getElementById("chapterlist").style.height ="190px";
	}
	else
	{
		document.getElementById("chapterlist").style.overflow = "visible";
		document.getElementById("chapterlist").style.height ="auto";
	}
	settings["noscroll"] = !settings["noscroll"];
	return;
}
