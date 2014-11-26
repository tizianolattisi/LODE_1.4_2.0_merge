/**
 * jCountr
 * Creates a countdown timer from a jQuery object. 
 *
 * $(<selector>).counter({params});
 *
 * @author agentor@gmx.de
 * @version 1.1
 */
 
jQuery.fn.counter = function(params) {
	var self = this;

	self.display = $(this);
	self.hour = params.hour?params.hour:"0";
	self.min = params.min?params.min:"0";
	self.sec = params.sec?params.sec:"0";
	self.hour_end = params.hour_end?params.hour_end:"0";
	self.min_end = params.min_end?params.min_end:"0";
	self.sec_end = params.sec_end?params.sec_end:"0";
	self.message = params.message?params.message:"FINISHED";
	self.delimiter = params.delimiter?params.delimiter:":";
	self.return_url = params.return_url?params.return_url:"";
	self.interval = params.interval?params.interval*1000:"1000";
	self.iteration = params.iteration?params.iteration:"1";
	self.direction = params.direction?params.direction:"down";
	
	self.timestamp = (self.hour * 60 * 60) + (self.min * 60) + (self.sec*1);
	self.timestamp_end = (self.hour_end * 60 * 60) + (self.min_end * 60) + (self.sec_end*1);
            
    self.onTick = params.onTick?params.onTick:function() { return true; };
    self.onFinish = params.onFinish?params.onFinish:function() { return true; };


	/** update the countdown while running **/
	self._updateCounter = function(params) {
		self.hour = params.hour?params.hour:self.hour;
		self.min = params.min?params.min:self.min;
		self.sec = params.sec?params.sec:self.sec;
		self.hour_end = params.hour_end?params.hour_end:self.hour_end;
		self.min_end = params.min_end?params.min_end:self.min_end;
		self.sec_end = params.sec_end?params.sec_end:self.sec_end;
		self.message = params.message?params.message:self.message;
		self.delimiter = params.delimiter?params.delimiter:self.delimiter;
		self.return_url = params.return_url?params.return_url:self.return_url;
		self.interval = params.interval?params.interval*1000:self.interval;
		self.iteration = params.iteration?params.iteration:self.iteration;
		self.direction = params.direction?params.direction:self.direction;
		
		self.timestamp = (self.hour * 60 * 60) + (self.min * 60) + (self.sec*1);
		self.timestamp_end = (self.hour_end * 60 * 60) + (self.min_end * 60) + (self.sec_end*1);
		return;
		
	};

	/** get a counter from an timestamp **/
	self._getCounterFromTimestamp = function(t) {
		if (t > 0) {
			hours = Math.floor(t / 3600)
			minutes = Math.floor( (t / 3600 - hours) * 60)
	    	seconds = Math.round( ( ( (t / 3600 - hours) * 60) - minutes) * 60)
	    } else {
	        hours = 0;
	        minutes = 0;
	        seconds = 0;
	    }
	    
	    if (seconds == 60)  {
	    	seconds = 0;
	    }
	    
	    if(seconds == 0)  {
	    	if(hours != 0) 	{
	    		minutes = minutes/1 + 1;
	    	}
	    }
	    
	    if (minutes < 10)  {
	    	if (minutes < 0) {
	    		minutes = 0;
	    	}
		    minutes = '0' + minutes;
	    }
	    if (seconds < 10) {
	    	if (seconds < 0) {
	    		seconds = 0;
	    	}
	    	seconds = '0' + seconds;
	    }
	    
	    if (hours < 10) {
	    	if (hours < 0) {
	    		hours = 0;
	    	}
	    	hours = '0' + hours;
	    }
		
		if(hours > 0) {
		    return hours + self.delimiter + minutes + self.delimiter + seconds;
		} else {
			return minutes + self.delimiter + seconds;
		}
	};
       
    /** update the text for the countdown timer **/
    self._tick = function() {

    	if(self.timestamp > 0 && self.timestamp != self.timestamp_end) {
	    	var counter = self._getCounterFromTimestamp(self.timestamp);
	    	
	    	/** show the current time **/
	        self.display.html(counter);
	        
	        if(self.direction == "down") {
		        self.timestamp = self.timestamp*1 - self.iteration*1;
	        } else if(self.direction == "up") {
	        	self.timestamp = self.timestamp*1 + self.iteration*1;
	        } else {
	        	return;
	        }
    	} else {
    		window.clearInterval(self._interval);
            if (!self.onFinish(self.display)) { 
            	return;
            }
            
            /** display the finish message **/
            self.display.html(self.message);
            
            /** if there is a return url, go there **/
            if(self.return_url != ""){
	            window.location.href = self.return_url;
            }
            return;
    	}
    };
    
    
    /** do it **/
    self._tick();
    self._interval = window.setInterval(
        self._tick,
        self.interval
        );

    return this;
};