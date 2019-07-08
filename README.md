# openhab2-binding-husqvarnaam


openHAB2 binding for a husqvarna automower AM 220 AC/230ACX.

## Supported Things

The automower types 220 AC /230 ACX do not have a WLAN-Module out-of-the-box (see References).

## Discovery

This binding does not support discovery of the thing.

## Thing configuration

In the things folder, create a file called husqvarna.things (or any other name) and configure your AM inside.

The binding can control AMs through the local network (ipAm/ipAmUnsupported thing type).

Configuration of ipAm/ipAmUnsupported:

* address: the hostname/ipAddress of the AM on the local network. (mandatory)
* tcpPort: the port number to use to connect to the AM. (mandatory)
* pollingIntervalInSec: the interval in seconds the state of the AM is checked for updates (optional, default is 60)
* expertMode: mode to lookup special values like charge time, battery voltage (optional, default is false)

Example:

```
husqvarnaam:ipAm:am230 [ address="192.168.1.25", tcpPort="10001" ]
```

## Channels

<table>
    <tr><td><b>Channel</b></td><td><b>Channel Group</b></td><td><b>Item Type</b></td><td><b>Description</b></td></tr>
    <tr><td>currentState</td><td>displayInformation</td><td>Number</td><td>Displays the current state of the AM</td></tr>
    <tr><td>mowTime</td><td>displayInformation</td><td>Number:Time</td><td>Displays the current mow time in [min] of the AM</td></tr>
    <tr><td>chargeTime</td><td>displayInformation</td><td>Number:Time</td><td>Displays the current charge time in [min] of the AM</td></tr>
    <tr><td>operationTime</td><td>displayInformation</td><td>Number:Time</td><td>Displays the total operations time in [h] of the AM</td></tr>
    <tr><td>currentDateTime</td><td>displayInformation</td><td>DateTime</td><td>Displays the current Date and Time of the AM</td></tr>
    <tr><td>mode</td><td>displayInformation</td><td>String</td><td>Operation Mode for AM. Possible values are:
    <br/>
    0=MAN<br/>
	1=AUTO<br/>
	3=HOME<br/>
	4=DEMO<br/>
    </td></tr>
    <tr><td>Latest Update Time</td><td>displayInformation</td><td>DateTime</td><td>Latest time of update</td></tr>
    <tr><td>Expert mode</td><td>displayInformation</td><td>Switch</td><td>Is expert mode active</td></tr>   
    <tr><td>timerActiveMode</td><td>timer</td><td>Switch</td><td>Timer Active/Inactive for AM</td></tr>
    <tr><td>timerActiveWeekdays</td><td>timer</td><td>Number</td><td>Timer of day Active/Inactive for AM (This is a bitmask)</td></tr>
    <tr><td>timer1Start</td><td>timer</td><td>DateTime</td><td>1. Timer in hh:mm for AM</td></tr>
    <tr><td>timer1Stop</td><td>timer</td><td>DateTime</td><td>1. Timer in hh:mm for AM</td></tr>
    <tr><td>timer2Start</td><td>timer</td><td>DateTime</td><td>2. Timer in hh:mm for AM</td></tr>
    <tr><td>timer2Stop</td><td>timer</td><td>DateTime</td><td>2. Timer in hh:mm for AM</td></tr>
    <tr><td>weTimer1Start</td><td>timer</td><td>DateTime</td><td>1. Weekend Timer in hh:mm for AM</td></tr>
    <tr><td>weTimer1Stop</td><td>timer</td><td>DateTime</td><td>1. Weekend Timer in hh:mm for AM</td></tr>
    <tr><td>weTimer2Start</td><td>timer</td><td>DateTime</td><td>2. Weekend Timer in hh:mm for AM</td></tr>
    <tr><td>weTimer2Stop</td><td>timer</td><td>DateTime</td><td>2. Weekend Timer in hh:mm for AM</td></tr>
    <tr><td>rectangleModeState</td><td>details</td><td>Switch</td><td>Displays rectangle mode state of the AM ?</td></tr>
    <tr><td>rectangleModePercent</td><td>details</td><td>Number:Dimensionless</td><td>Displays rectangle mode percent [%] of the AM</td></tr>
    <tr><td>rectangleModeReference</td><td>details</td><td>String</td><td>Displays rectangle mode Reference of the AM ?</td></tr>
    <tr><td>batteryCapacityUsedMaH</td><td>details</td><td>Number:Energy</td><td>Displays battery capacity used [mAh] of the AM</td></tr>
    <tr><td>batteryCurrentMa</td><td>details</td><td>Number:ElectricCurrent</td><td>Displays battery current [mA] of the AM</td></tr>
    <tr><td>batteryCapacityMaH</td><td>details</td><td>Number:Energy</td><td>Displays battery capacity [mAh] of the AM</td></tr>
    <tr><td>batteryCapacitySearchStartMaH</td><td>details</td><td>String</td><td>Displays battery capacity Search Start [mAh] of the AM</td></tr>
    <tr><td>batteryVoltage</td><td>details</td><td>Number:ElectricPotential</td><td>Displays battery Voltage [V] of the AM</td></tr>
    <tr><td>batteryTemperature</td><td>details</td><td>Number:Temperature</td><td>Displays battery Temperature [°C]of the AM</td></tr>
    <tr><td>batteryTemperatureCharge</td><td>details</td><td>Number:Temperature</td><td>Displays battery Temperature Charge of the AM</td></tr>
    <tr><td>batteryLatestChargeMin</td><td>details</td><td>Number:Time</td><td>Displays battery latest Charge [m] of the AM</td></tr>
    <tr><td>batteryNextTemperatureMeasurementSec</td><td>details</td><td>String</td><td>Displays battery next temperature [s] of the AM ?</td></tr>
    <tr><td>velocityMotor</td><td>details</td><td>Number</td><td>Displays velocity of motor [?] of the AM ?</td></tr>
    <tr><td>velocityLeft</td><td>details</td><td>Number:Speed</td><td>Displays velocity of left wheel [?] of the AM</td></tr>
    <tr><td>velocityRight</td><td>details</td><td>Number:Speed</td><td>Displays velocity of right wheel [?] of the AM</td></tr>
    <tr><td>firmwareVersion</td><td>details</td><td>String</td><td>Displays firmware version of the AM ?</td></tr>
    <tr><td>languageFileVersion</td><td>details</td><td>String</td><td>Displays language file version of the AM ?</td></tr>
</table>
Mode of the AM and the Timers can be set, all other information is readonly. All information in channel group "details" is experimental,
due to the lack of an official specification of the husqvarna protocol the interpretation of the values is sometimes unclear. Most information is
extracted from [1].

Timer of day: each day of week is represented as a special bit in a 7 bit value
<table>
    <tr><td><b>Day of week</b></td><td><b>Value</b></td><td><b>bit value</b></td></tr>
    <tr><td>Monday</td><td>1</td><td>1</td></tr>
    <tr><td>Tuesday</td><td>2</td><td>10</td></tr>
    <tr><td>Wednesday</td><td>4</td><td>100</td></tr>
    <tr><td>Thursday</td><td>8</td><td>1000</td></tr>
    <tr><td>Friday</td><td>16</td><td>10000</td></tr>
    <tr><td>Saturday</td><td>32</td><td>100000</td></tr>
    <tr><td>Sunday</td><td>64</td><td>1000000</td></tr>
</table>
e.g. the value 27 (0011011) means the timer is active on Monday, Tuesday, Thursday, Friday

## Logging

To look at the communication between openHAB and the Am in more detail choose "DEBUG" for the logical view and "TRACE" for the byte values of each command.
In karaf set e.g.

```
log:set DEBUG org.openhab.binding.husqvarnaam
```

## Full example

* demo.things:

```
husqvarnaam:ipAm:am230 [ address="192.168.1.25", tcpPort="10001", pollingIntervalInSec=60, expertMode=false ]
```

* demo.items:

```
Group gHusqvarna <am> (All)
Group gBasics <none> (gHusqvarna)
Group gTimers <time-on> (gHusqvarna)
Group gDetails <none> (gHusqvarna)

Group gTimer1 "Timer 1" <time-on> (gTimers)
Group gTimer2 "Timer 2" <time-on>  (gTimers)
Group gweTimer1 "Woe Timer 1" <time-on>  (gTimers)
Group gweTimer2 "Woe Timer 2" <time-on>  (gTimers)
Group gWeekdays "Wochentage" <none> (gTimers)

/* Husqvarna AM Items */
/* Basic information */
String am230StateInformation	"Aktueller Status [MAP(husqvarnaStatusCode_de.map):%s]"			<am>		(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#currentState" }
Number:Time am230MowTimeInformation	"Mähzeit [%.1f %unit%]"	<time>	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#mowTime" }
Number:Time am230ChargeTimeInformation	"Ladezeit [%.1f %unit%]"	<time>	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#chargeTime" }
Number:Time am230OperationTimeInformation	"Gesamtbetriebszeit [%.1f %unit%]"	<time>	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#operationTime" }
DateTime am230CurrentMowerDateTime	"Aktuelles Datum am AM [%1$tA, %1$td.%1$tm.%1$tY %1$tT]"	<calendar>	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#currentDateTime" }
String am230ModeSet	"Modus"	<none>	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#mode"}
Switch am230ExpertMode  "Expertenmodus" <none> 	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#expertMode"}
DateTime am230LatestUpdateTime  "Letzte Aktualisierung [%1$tA, %1$td.%1$tm.%1$tY %1$tT]" <time> 	(gBasics)	{ channel="husqvarnaam:ipAm:am230:displayInformation#latestUpdateTime"}

/* details (expert mode) */
String am230rectangleModeState "Rectangle mode state [%s]"		<none>	(gDetails) {channel="husqvarnaam:ipAm:am230:details#rectangleModeState" }
Number:Dimensionless am230rectangleModePercent "Rectangle mode [%.1f %unit%]"		<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#rectangleModePercent" }
String am230rectangleModeReference "Rectangle mode Reference [%s]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#rectangleModeReference" }
Number:Energy am230batteryCapacityUsedMaH "Battery capacity used [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryCapacityUsedMaH" }
Number:ElectricCurrent am230batteryCurrentMa "Battery current [%.1f %unit%]"			<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryCurrentMa" }
Number:Energy am230batteryCapacityMaH "Battery capacity [%.1f %unit%]"		<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryCapacityMaH" }
Number:Energy am230batteryCapacitySearchStartMaH "Battery capacity Search start [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryCapacitySearchStartMaH" }
Number:ElectricPotential am230batteryVoltage "Battery voltage [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryVoltage" }
Number:Temperature am230batteryTemperature "Battery temperature [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryTemperature" }
Number:Temperature am230batteryTemperatureCharge "Battery temperature charged [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryTemperatureCharge" }
Number:Time am230batteryLatestChargeMin "Battery latest charge [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryLatestChargeMin" }
String am230batteryNextTemperatureMeasurementSec "Next Battery temperature measurement[%s sec]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#batteryNextTemperatureMeasurementSec" }
Number am230bladeVelocityMotor "Blade velocity motor [%s rpm]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#velocityMotor" }
Number:Speed am230velocityLeft "Velocity left wheel [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#velocityLeft" }
Number:Speed am230velocityRight "Velocity right wheel [%.1f %unit%]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#velocityRight" }
String am230firmwareVersion "Firmware Version [%s]"		<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#firmwareVersion" }
String am230languageFileVersion "Language file Version [%s]"	<none>	(gDetails)  {channel="husqvarnaam:ipAm:am230:details#languageFileVersion" }

/* Timers: the mapping between channel(DateTime) and GUI-Items must be done in "rules" */ 
DateTime am230Timer1Start "Timer1 Start [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#timer1Start"}
Number am230Timer1StartUi "Start" <clock>	(gTimer1)

DateTime am230Timer1Stop "Timer1 Stop [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#timer1Stop"}
Number am230Timer1StopUi "Stop" <clock>	(gTimer1)

DateTime am230Timer2Start "Timer2 Start [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#timer2Start"}
Number am230Timer2StartUi "Start" <clock>	(gTimer2)

DateTime am230Timer2Stop "Timer2 Stop [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#timer2Stop"}
Number am230Timer2StopUi "Stop" <clock>	(gTimer2)

DateTime am230WeTimer1Start "WeTimer1 Start [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#weTimer1Start"}
Number am230WeTimer1StartUi "Start" <clock>	(gweTimer1)

DateTime am230WeTimer1Stop "WeTimer1 Stop [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#weTimer1Stop"}
Number am230WeTimer1StopUi "Stop" <clock>	(gweTimer1)

DateTime am230WeTimer2Start "WeTimer2 Start [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#weTimer2Start"}
Number am230WeTimer2StartUi "Start" <clock>	(gweTimer2)

DateTime am230WeTimer2Stop "WeTimer2 Stop [%1$tR]" <time> { channel="husqvarnaam:ipAm:am230:timer#weTimer2Stop"}
Number am230WeTimer2StopUi "Stop" <clock>	(gweTimer2)

Switch am230TimerActive "Timer active" 	(gTimers) {channel="husqvarnaam:ipAm:am230:timer#timerActiveMode" }

Switch am230timerMonday	"Montag" (gWeekdays)
Switch am230timerTuesday "Dienstag" (gWeekdays)
Switch am230timerWednesday	"Mittwoch" (gWeekdays)
Switch am230timerThursday	"Donnerstag" (gWeekdays)
Switch am230timerFriday	"Freitag" (gWeekdays)
Switch am230timerSaturday	"Samstag" (gWeekdays)
Switch am230timerSunday	"Sonntag" (gWeekdays)
Switch am230AllDays	"Alle Tage" (gWeekdays)
Number am230TimerWeekdays "Timers (bitmask) [%d]" {channel="husqvarnaam:ipAm:am230:timer#timerActiveWeekdays" }
```

A timer value is defined as DateTime by the channel and as Number for Hour and Minute separately for display in the UI. Therefore
there are two rules to update channel value and ui values in 


The AM returns its state information (channel 'displayInformation#currentState') as number. Use a map file 'husqvarnaStatusCode.map' to decode like

```
6=Left wheel motor blocked
12=No signal
16=Out of boundary
18=Low battery voltage
26=Charging station blocked
34=Automower lifted
52=No contact to charging station
54=Pin timeout
1000=Leaving charging station
1002=Mowing
1006=Starting blades
1008=Blades started
1012=Signal: Start mowing
1014=Charging
1016=Waiting in charging station
1024=Entering charging station
1036=Square mode
1038=Stuck
1040=Collision
1042=Searching
1044=Stop
1048=Docking
1050=Leaving charging station
1052=Error
1056=Waiting (Mode Man/Home)
1058=Following Boundary
1060=Signal found
1062=Stuck
1064=Searching
1070=Following guide wire
1072=Following boundary wire
-=unknown (-)
NULL=unknown (NULL)
```

* demo.rules

```
import java.util.concurrent.locks.ReentrantLock
import java.time.ZonedDateTime
import java.util.TimeZone
import java.util.Calendar

val ReentrantLock weekDaysLock = new ReentrantLock()

val int MONDAY=1
val int TUESDAY=2
val int WEDNESDAY=4
val int THURSDAY=8
val int FRIDAY=16
val int SATURDAY=32
val int SUNDAY=64
val long LOCK_TIMEOUT=50L

val org.eclipse.xtext.xbase.lib.Functions$Function2<NumberItem,Integer,OnOffType> convertOnOff = [
	NumberItem bitMask,
	Integer bitSet |
	var b1 = (bitMask.state as DecimalType).intValue
	var b2 = bitSet.intValue
	if (b1.bitwiseAnd(b2)==b2) {
		return OnOffType.ON
	} else {
		return OnOffType.OFF
	}
]

val org.eclipse.xtext.xbase.lib.Functions$Function2<DateTimeItem,NumberItem,Void> timerUI = [
	DateTimeItem timerItem,
	NumberItem guiTimerItem |
	if (timerItem.state != NULL){
		var ZonedDateTime dateTime = (timerItem.state as DateTimeType).zonedDateTime;
		guiTimerItem.postUpdate(new DecimalType(dateTime.hour * 60 + dateTime.minute));
		logInfo("husqvarnaam.rules","Updating AM timer -> UI "+guiTimerItem)
	}
]

val org.eclipse.xtext.xbase.lib.Functions$Function2<NumberItem,DateTimeItem,Void> timerLogic = [
	NumberItem guiTimerItem,
	DateTimeItem timerItem |
	if( (guiTimerItem.state != NULL) && (guiTimerItem.state != UNDEF) ){
	  var Calendar calendar =Calendar.getInstance()
	  calendar.clear()
	  // Copy the Alarm-Time from the UI to local variable
	  var minutesOfDay = (guiTimerItem.state as DecimalType).intValue
	  // Calendar rolls over to be forwarded to backend
	  calendar.set(Calendar.MINUTE, minutesOfDay)
	  timerItem.sendCommand(new DateTimeType(ZonedDateTime.ofInstant(calendar.toInstant(), TimeZone.getDefault().toZoneId())
				  .withFixedOffsetZone()))
	  logInfo("husqvarnaam.rules","Updating UI timer -> AM "+timerItem)
	}
]

rule "Initialization AM Timer"
 when 
   System started
 then
    // initialize time gui elements
	am230Time1StartUi.postUpdate(UnDefType.UNDEF)
	am230Time1StopUi.postUpdate(UnDefType.UNDEF)
	am230Time2StartUi.postUpdate(UnDefType.UNDEF)
	am230Time2StopUi.postUpdate(UnDefType.UNDEF)
	am230WeTime1StartUi.postUpdate(UnDefType.UNDEF)
	am230WeTime1StopUi.postUpdate(UnDefType.UNDEF)
	am230WeTime2StartUi.postUpdate(UnDefType.UNDEF)
	am230WeTime2StopUi.postUpdate(UnDefType.UNDEF)
 end

rule "uiTimer1Start"
when
	Item am230Timer1StartUi received command
then
	timerLogic.apply(am230Timer1StartUi,am230Timer1Start)
end

rule "updateTimer1Start"
when
	Item am230Timer1Start changed
then
	timerUI.apply(am230Timer1Start,am230Timer1StartUi)
end

rule "uiTimer1Stop"
when
	Item am230Timer1StopUi received command
then
	timerLogic.apply(am230Timer1StopUi,am230Timer1Stop)
end

rule "updateTimer1Stop"
when
	Item am230Timer1Stop changed
then
	timerUI.apply(am230Timer1Stop,am230Timer1StopUi)
end

rule "uiTimer2Start"
when
	Item am230Timer2StartUi received command
then
	timerLogic.apply(am230Timer2StartUi,am230Timer2Start)
end

rule "updateTimer2Start"
when
	Item am230Timer2Start changed
then
	timerUI.apply(am230Timer2Start,am230Timer2StartUi)
end

rule "uiTimer2Stop"
when
	Item am230Timer2StopUi received command
then
	timerLogic.apply(am230Timer2StopUi,am230Timer2Stop)
end

rule "updateTimer2Stop"
when
	Item am230Timer2Stop changed
then
	timerUI.apply(am230Timer2Stop,am230Timer2StopUi)
end

rule "uiWeTimer1Start"
when
	Item am230WeTimer1StartUi received command
then
	timerLogic.apply(am230WeTimer1StartUi,am230WeTimer1Start)
end

rule "updateWeTimer1Start"
when 
	Item am230WeTimer1Start changed
then
	timerUI.apply(am230WeTimer1Start,am230WeTimer1StartUi)
end

rule "uiWeTimer1Stop"
when 
	Item am230WeTimer1StopUi received command
then
	timerLogic.apply(am230WeTimer1StopUi,am230WeTimer1Stop)
end

rule "updateWeTimer1Stop"
when 
	Item am230WeTimer1Stop changed
then
	timerUI.apply(am230WeTimer1Stop,am230WeTimer1StopUi)
end

rule "uiWeTimer2Start"
when
	Item am230WeTimer2StartUi received command
then
	timerLogic.apply(am230WeTimer2StartUi,am230WeTimer2Start)
end

rule "updateWeTimer2Start"
when 
	Item am230WeTimer2Start changed
then
	timerUI.apply(am230WeTimer2Start,am230WeTimer2StartUi)
end

rule "uiWeTimer2Stop"
when
	Item am230WeTimer2StopUi received command
then
	timerLogic.apply(am230WeTimer2StopUi,am230WeTimer2Stop)
end

rule "updateWeTimer2Stop"
when 
	Item am230WeTimer2Stop changed
then
	timerUI.apply(am230WeTimer2Stop,am230WeTimer2StopUi)
end

// updates timer weekdays bitmask in UI, if changed on AM
rule "updateTimerWeekdays"
when
	Item am230TimerWeekdays changed
then
	try{
		weekDaysLock.lock()
		logInfo("husqvarnaam.rules","Weekday Timers changed on AM, active on "+(am230TimerWeekdays.state as DecimalType).intValue)
		am230timerMonday.postUpdate(convertOnOff.apply(am230TimerWeekdays,MONDAY))
		am230timerTuesday.postUpdate(convertOnOff.apply(am230TimerWeekdays,TUESDAY))
		am230timerWednesday.postUpdate(convertOnOff.apply(am230TimerWeekdays,WEDNESDAY))
		am230timerThursday.postUpdate(convertOnOff.apply(am230TimerWeekdays,THURSDAY))
		am230timerFriday.postUpdate(convertOnOff.apply(am230TimerWeekdays,FRIDAY))
		am230timerSaturday.postUpdate(convertOnOff.apply(am230TimerWeekdays,SATURDAY))
		am230timerSunday.postUpdate(convertOnOff.apply(am230TimerWeekdays,SUNDAY))
	} finally  {
     // release the lock - we are ready to process the next event
		weekDaysLock.unlock()
  }
end

// updates timer weekdays bitmask in AM, if changed in UI
rule "uiTimerWeekDays"
when
	Item am230timerMonday received command or
	Item am230timerTuesday  received command or
	Item am230timerWednesday received command or
	Item am230timerThursday received command or
	Item am230timerFriday received command or
	Item am230timerSaturday received command or
	Item am230timerSunday received command
then
  // If the UI to change the Alarm time is clicked several times the code below
  // is subject to race conditions. Therefore we make sure that all events 
  // are processed one after the other.
  try {
	weekDaysLock.lock()
	var int allDays = 0
    // Create bitmask
	var mo=am230timerMonday.state as OnOffType
	var tu=am230timerTuesday.state as OnOffType
	var we=am230timerWednesday.state as OnOffType
	var th=am230timerThursday.state as OnOffType
	var fr=am230timerFriday.state as OnOffType
	var sa=am230timerSaturday.state as OnOffType
	var su=am230timerSunday.state as OnOffType
	
	logInfo("husqvarnaam.rules","Timer active Monday "+mo)

    	if(mo == OnOffType.ON){
		allDays= allDays.bitwiseOr(MONDAY)
    	}
    	if(tu == OnOffType.ON){
		allDays= allDays.bitwiseOr(TUESDAY)
    	}
    	if(we == OnOffType.ON){
		allDays= allDays.bitwiseOr(WEDNESDAY)
    	}
    	if(th== OnOffType.ON){
		allDays= allDays.bitwiseOr(THURSDAY)
    	}
    	if(fr == OnOffType.ON){
		allDays= allDays.bitwiseOr(FRIDAY)
    	}
    	if(sa == OnOffType.ON){
		allDays= allDays.bitwiseOr(SATURDAY)
    	}
    	if(su == OnOffType.ON){
		allDays= allDays.bitwiseOr(SUNDAY)
    	}
	logInfo("husqvarnaam.rules","Timer active on "+allDays)
	am230TimerWeekdays.sendCommand(new DecimalType(allDays))
   } finally  {
     // release the lock - we are ready to process the next event
	weekDaysLock.unlock()
  }
end

// Switch am230AllDays	"Allday" in UI, propagate changes to individual switch item
rule "uiTimerAllWeekDays"
when
	Item am230AllDays received command
then
  // If the UI to change the Alarm time is clicked several times the code below
  // is subject to race conditions. Therefore we make sure that all events 
  // are processed one after the other.
  try {
	weekDaysLock.lock()
    	var all=am230AllDays.state as OnOffType
    	if(all == OnOffType.ON){
	    am230timerMonday.postUpdate(OnOffType.ON)
	    am230timerTuesday.postUpdate(OnOffType.ON)
	    am230timerWednesday.postUpdate(OnOffType.ON)
	    am230timerThursday.postUpdate(OnOffType.ON)
	    am230timerFriday.postUpdate(OnOffType.ON)
	    am230timerSaturday.postUpdate(OnOffType.ON)
	    am230timerSunday.postUpdate(OnOffType.ON)
    	} else {
	    am230timerMonday.postUpdate(OnOffType.OFF)
	    am230timerTuesday.postUpdate(OnOffType.OFF)
	    am230timerWednesday.postUpdate(OnOffType.OFF)
	    am230timerThursday.postUpdate(OnOffType.OFF)
	    am230timerFriday.postUpdate(OnOffType.OFF)
	    am230timerSaturday.postUpdate(OnOffType.OFF)
	    am230timerSunday.postUpdate(OnOffType.OFF)
    	}
   } finally  {
     // release the lock - we are ready to process the next event
     	weekDaysLock.unlock()
  }
end
```

* demo.sitemap:

```
sitemap demo label="Husqvarna Menu"
{
    Frame label="Husqvarna Automower" icon="am" {
	Text item=am230StateInformation icon="am"
	Text item=am230MowTimeInformation
	Text item=am230ChargeTimeInformation
	Text item=am230OperationTimeInformation
	Text item=am230CurrentMowerDateTime
	Switch item=am230ModeSet mappings=[0="MAN", 1="AUTO", 3="HOME", 4="DEMO"]
		
	Text label="Husqvarna AM Timer" icon="time" {
		Frame {
			Switch item=am230TimerActive
		}
		Frame label="Wochentage" visibility=[am230TimerActive==ON]{
			Switch item=am230timerMonday
			Switch item=am230timerTuesday
			Switch item=am230timerWednesday
			Switch item=am230timerThursday
			Switch item=am230timerFriday
			Switch item=am230timerSaturday
			Switch item=am230timerSunday
			Switch item=am230AllDays
		}
		Frame visibility=[am230TimerActive==ON] {
			Text label="Wochen (Mo-Do) Timer" icon="time-on"{
				Frame label="Timer 1" {
					Setpoint item=am230Timer1StartUi label="Start [JS(NumberToClock.js):%s]" minValue=0 maxValue=720 step=15
					Setpoint item=am230Timer1StopUi label="Stop [JS(NumberToClock.js):%s]" minValue=0 maxValue=720 step=15
				}
				Frame label="Timer 2" {
					Setpoint item=am230Timer2StartUi label="Start [JS(NumberToClock.js):%s]" minValue=720 maxValue=1425 step=15
					Setpoint item=am230Timer2StopUi label="Stop [JS(NumberToClock.js):%s]" minValue=720 maxValue=1425 step=15
				}
			}
			Text label="Wochenende (Fr-So) Timer" icon="time-on" {
				Frame label="Timer 1" {
					Setpoint item=am230WeTimer1StartUi label="Start [JS(NumberToClock.js):%s]" minValue=0 maxValue=720 step=15
					Setpoint item=am230WeTimer1StopUi label="Stop [JS(NumberToClock.js):%s]" minValue=0 maxValue=720 step=15
				}
				Frame label="Timer 2" {
					Setpoint item=am230WeTimer2StartUi label="Start [JS(NumberToClock.js):%s]" minValue=720 maxValue=1425 step=15
					Setpoint item=am230WeTimer2StopUi label="Stop [JS(NumberToClock.js):%s]" minValue=720 maxValue=1425 step=15
				}
			}

		}
	}
	Text label="Husqvarna AM Details" {
		Text item=am230rectangleModeState
		Text item=am230rectangleModePercent
		Text item=am230rectangleModeReference
		Text item=am230batteryCapacityUsedMaH
		Text item=am230batteryCurrentMa
		Text item=am230batteryCapacityMaH
		Text item=am230batteryCapacitySearchStartMaH
		Text item=am230batteryVoltage
		Text item=am230batteryTemperature
		Text item=am230batteryTemperatureCharge
		Text item=am230batteryLatestChargeMin
		Text item=am230batteryNextTemperatureMeasurementSec
		Text item=am230bladeVelocityMotor
		Text item=am230velocityLeft
		Text item=am230velocityRight
		Text item=am230firmwareVersion
		Text item=am230languageFileVersion
	}
    }
}
```


## References

\[1\] An open source project is https://homematic-forum.de/forum/viewtopic.php?f=31&t=7295

\[2\] A commercial provider is http://www.pritschet.eu/WIFI-Control/
