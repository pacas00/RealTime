RealTime
-----------

For Minecraft 1.7.10

<a href='http://jenkins.petercashel.net/job/RealTime/'><img src='http://jenkins.petercashel.net/buildStatus/icon?job=RealTime'></a>





----------
 Config
----------

    B:EnforceSync=false
	
	//Enables RealTime
    B:RealTimeEnabled=false
	
	//TimeZone
    S:RealTime_TimeZone_Name=Australian Eastern Standard Time (Queensland)    # Set's to your Time zone by default
    # Supports 'GMT-4' time zone format as well as 'Australia/Brisbane' and 'Australian Eastern Standard Time (Queensland)'
	# See TZ column in the following page if unsure. https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
	
	//Weather (WIP)
	S:WeatherAPIKEY=
	# Put your Free Local Weather API key here to enable the weatherMan block.
    
	
    S:WeatherLocation=
    # Set your desired weather location. Valid Formats Are
    • City Name
    • City Name, State (US only)
    • City Name, State, Country
    • City Name, Country
    • IP Address
    • UK or Canada Postal Code or US Zipcode
    • Latitude and longitude in in decimal degrees (XX.XXX,XX.XXX)
	
--------------------------------------------------------------------
<a href="http://www.worldweatheronline.com/free-weather-feed.aspx" title="Get a Free local weather content provider" target="_blank">Free Local Weather API key from World Weather Online</a>

Powered by <a href="http://www.worldweatheronline.com/" title="Free local weather content provider" target="_blank">World Weather Online</a>
