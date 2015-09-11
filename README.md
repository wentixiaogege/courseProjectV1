Course Project Server Part 
========================

Project template for a REST service exposed through dropwizard 

This contains:
        dropwizard -.0.8.1 for setting up the server 
        
        JDBI for connect with mysql 
        
        AngularJS Jquery bootstrap for front page design 

which works together out-of-the-box.


DropWizard :
  Dropwizard pulls together stable, mature libraries from the Java ecosystem into a simple, light-weight package that lets you focus on getting things done. read more on http://www.dropwizard.io/


Summary:
------------------------

This project is mainly about how to use RESTful web service in the IOT project ,basically It is how to set up a running system with both a device get the real data and a  server store and display the data. I will talk in detail about how to set up the running system first, and then I will talk about how the hardware and software 's functions and how they work together. which works together out-of-the-box.

The System Architecture:
------------------------

              
            1st Level       Web Browser 
     
                              |     |
            2nd Level       RESTful Server
                             |        |
                            |          |
            3rd Level  End Device   End Device


setting up the system 
------------------------
Then I am going to talk about how to make the system work , be sure you have downloaded all the code  !! and import them in the eclipse projects , by the way I am using eclipse for setting up this system not in the raspberry but in my local PC .If you really want to programming in the raspberry pi ,you can do it ,but I am sure it is very slow .you can use my projects also ,cause you still can use maven in the raspberry pi. I am going to lead you guys from both hardware and software side.

Hardware:
===============

1. the raspberry OS:
	The first is you need to install and configure the system in the raspberry pi ,which using as SD card as its memeory and storage ,I am 		not going to talk about how to install the r aspberry OS here you can find more tutorials here: https://www.raspberrypi.org
2.The connection of the DHT11 temp sensor and the device 
	2.1 connect temp sensor
	you need three wires to get data from the DHT11 sensor, which is power/ground/data .below is my connection:
                              
![alt tag](https://github.com/wentixiaogege/CourseProjectEndDevice/blob/master/readme_img/temp_connection.png)                                                          













I mainly get the inspiration from this website: http://hirt.se/blog/?p=493









   2.2 connect the relay device
       relay something is just like relay a led device ,you only need to wires to relay a device which is state and ground: connection like below:
             
![alt tag](https://github.com/wentixiaogege/CourseProjectEndDevice/blob/master/readme_img/relay_connection.png)   










 


  
3.the xbee connection :
  	 for the xbee connection you may need using XCTU for confire the xbee :
		http://www.libelium.com/development/waspmote/	documentation/x-ctu-tutorial/
   	 for how to configure the xbee different mode learn form here:
 		http://www.arduino-hacks.com/xbee-api-mode/
	  	below is my configure: 
	  	
3.1. for end device:

![alt tag](https://github.com/wentixiaogege/CourseProjectEndDevice/blob/master/readme_img/xbee_end.png)   















3.2. for server device:
	   

![alt tag](https://github.com/wentixiaogege/CourseProjectEndDevice/blob/master/readme_img/xbee_server.png)
















after you plugin the xbee device into your system use command below:
             ls /dev/tty
	you will get a device named 
          “ttyUSB*” * means numbers:

 
Software:
===============

After the hardware part is done! You need to install some libraries and configure to make the system runing.

1.install DHT-11 driver :(optional in server part)
	download code from here:   https://github.com/adafruit/Adafruit-Raspberry-Pi- Python-Code/tree/master/Adafruit_DHT_Driver 
	cd into this folder and then using the command  : make 
	you will get a runnable file Adafruit_DHT;put it into you PATH environment.
 
2.install jdk8
	download  form here http://www.oracle.com/technetwork/java/javase/downloads/jdk8-arm-downloads-2187472.html
	unzip it into somewhere I installed it in /opt/java 
	cd /opt/java/yourjdkversion/
	sudo update-alternatives --install "/usr/bin/java" "java" "/opt/java/yourjdkversion/java" 1
	sudo update-alternatives --set java /opt/java/yourjdkversion/bin/java

   open /etc/profile add those lines in the bottom:


   export JAVA_HOME=/opt/ yourjdkversion
   export JRE_HOME=$JAVA_HOME/jre 
   export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib 
   export PATH=$JAVA_HOME/bin:$PATH 


  Now you should be able to execute java

3.install java serial tools
 	You can install RXTX like this:
		$ sudo apt-get install librxtx-java


4.set up the server part 
   	Be sure you have download maven2 .if you haven't run below command in the  command line:
                sudo apt-get install maven2
                
   Be sure you have download 
        CourseProject : https://github.com/wentixiaogege/CourseProject   ;
		CourseProjectV1: https://github.com/wentixiaogege/CourseProjectV1;
	    import those projects into your eclipse you will see all the avaibleable code,make sure there is no error show up.

![alt tag](https://github.com/wentixiaogege/CourseProjectEndDevice/blob/master/readme_img/eclipse_server_init.png)      



   cd to the CourseProject folder and then run below:
		mvn clean install 
   cd to the CourseProjectV1 folder and then run below :
		mvn clean install 
   everything should went well and then you will get a XXXX.jar package like below:
   
   
![alt tag](https://github.com/wentixiaogege/CourseProjectEndDevice/blob/master/readme_img/eclipse_server_compiled.png)   

copy and paste both config.yml and this XXXX.jar package into your raspberry OS system anywhere you want and run the below command:


sudo java -Djava.library.path=/usr/lib/jni/ -jar yourpackagename.jar server config.yml



you are set!!!!!!






API designs:
============

	list all devices		v1/devices/	GET		{devices:[{id:1, name:tmp006, status:0},{id:2, name:tmp007, status:0},....]}
	get a device			v1/devices/{id}	GET		{id:1, name:tmp006, status:0}
	update a device			v1/devices/{id}	POST	{id:1, name:tmp006, status:0}	{result:true}
	add a device			v1/devices/	PUT	{id:2, name:tmp007, status:1}	{result:true}
	remove a device			v1/devices/{id}	DELETE		{result:true}
	list period data of the device	v1/devices/{id}/period/temp	GET	{intervals:3600,starttime:2015-7-2 12:12:12,endtime:2015-7-4 :12:12:12}	[{"id":112,"deviceId":1,"data":16,"timestamp":1436836733000},...,{"id":4402,"deviceId":1,"data":20.957447,"timestamp":1436903871000}]
	list all  data of device	v1/devices/{id}/all/temp	GET		[{"id":112,"deviceId":1,"data":16,"timestamp":1436836733000},...,{"id":4402,"deviceId":1,"data":20.957447,"timestamp":1436903871000}]
	relay a device	v1/relay/{id}/{relayState}	POST		{result:true}


Usage
=====

To start the server,

- create the distributable by running "mvn clean install"
- move to target folder and run "java -jar CourseProjectV1-0.0.1-SNAPSHOT.jar server ../config.yml"

The server will start at http://localsever:8080. You can also get into admin interface by going to http://localhost:8081/admin/.

//add device
curl -i -X PUT -H "Content-Type: application/json" -d '{"id":11,"name":"test Device","status":0,"dataType":"lightding"}' http://localhost:8080/devices

//get device
curl -i -X GET -H "Content-Type: application/json"  http://localhost:8080/devices/11

//get device Data 
curl -i -X GET -H "Content-Type: application/json"  http://localhost:8080/devices/1/all/temp

//get period device data
//using json 
curl -i -X POST -H "Content-Type: application/json" -d '{"intervals":3600,"starttime":"2015-07-13 18:18:04","endtime":"2015-07-13 23:31:15"}' http://localhost:8080/devices/1/peroid/temp

curl -i -X GET -H "Content-Type: application/json" -d '{"glossary":{"intervals":3600,"starttime":"2015-07-13 18:18:04","status":0,"endtime":"2015-07-13 23:31:15"}}' http://localhost:8080/devices/1/peroid/temp
//using url pathparam

//delete device

curl -i -X DELETE -H "Content-Type: application/json"  http://localhost:8080/service/devices/11

//relay the device
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/service/devices/1/1


Usage of the System:
---------------------

next, I will tell in detail about how to change or modify functions in  the server part.
For how to modify or change function in the end device part goto :
	https://github.com/wentixiaogege/CourseProjectEndDevice
 
mainly for the server part you should know that we are using a website framework dropwizard for better understanding click here:
	http://www.dropwizard.io/
basically we have the file structure below:
![alt tag](https://github.com/wentixiaogege/courseProjectV1/blob/master/readme_img/filestructure.png)

 you will get a better understanding of the structure from here 		:http://www.dropwizard.io/getting-started.html
next what I am talking based on that you know basic things about dropwizard.and you have made my system running well. If you are experienced ,don't care about what I said above.

2.0 configure the database and logging system
      for now we already know that every dropwizard app has a configuration file XXXX.yaml/yml ,you can see mine as a referrence :(yourprojectfolder/config.yml):
		      
		sundial:
		  thread-pool-size: 5
		  shutdown-on-unload: true
		  wait-on-shutdown: false
		  start-delay-seconds: 0
		  start-scheduler-on-load: true
		  global-lock-on-load: false
		  annotated-jobs-package-name: edu.itu.course.dropwizard.jobs
		
		# Database settings.
		database:
		
		  # the name of your JDBC driver
		  driverClass: com.mysql.jdbc.Driver
		
		  # the username
		  user: root
		
		  # the password
		  password: 355itu11
		
		  # the JDBC URL
		  url: jdbc:mysql://localhost:3306/courseProjectv1
		
		  # any properties specific to your JDBC driver:
		  properties:
		    charSet: UTF-8
		    hibernate.dialect: org.hibernate.dialect.MySQL5Dialect    
		
		  # the maximum amount of time to wait on an empty pool before throwing an exception
		  maxWaitForConnection: 1s
		
		  # the SQL query to run when validating a connection's liveness
		  validationQuery: "/* MyApplication Health Check */ SELECT 1"
		
		  # the minimum number of connections to keep open
		  minSize: 8
		
		  # the maximum number of connections to keep open
		  maxSize: 32
		
		  # whether or not idle connections should be validated
		  checkConnectionWhileIdle: false
		
		  checkConnectionOnBorrow: true
		
		 # the amount of time to sleep between runs of the idle connection validation, abandoned cleaner and idle pool resizing
		  evictionInterval: 10s
		
		 # the minimum amount of time an connection must sit idle in the pool before it is eligible for eviction
		  minIdleTime: 1 minute
		
		# use the simple server factory if you only want to run on a single port
		#server:
		#  type: simple
		#  connector:
		#    type: http
		#    port: 8080
		
		server:
		#  softNofileLimit: 1000
		#  hardNofileLimit: 1000
		  rootPath: '/service/*'
		  applicationContextPath: /
		  applicationConnectors:
		    - type: http
		      port: 8080
		  adminContextPath: /admin
		  adminConnectors:
		    - type: http
		      port: 8081
		#  Uncomment the following if you want https support
		#    - type: https
		#      port: 8443
		#      keyStorePath: example.keystore
		#      keyStorePassword: example
		#      validateCerts: false
		
		# this requires the npn-boot library on the JVM's boot classpath
		#    - type: spdy3
		#      port: 8445
		#      keyStorePath: example.keystore
		#      keyStorePassword: example
		#      validateCerts: false
		  
		
		# Uncomment the following if you want to enable https support for admin work
		#    - type: https
		#      port: 8444
		#      keyStorePath: example.keystore
		#      keyStorePassword: example
		#      validateCerts: false
		
		logging:
		
		  # Permit DEBUG, INFO, WARN and ERROR messages to be logged by appenders.
		  level: INFO
		
		  appenders:
		    # Log INFO to stderr
		    - type: console
		      threshold: INFO
		      target: stderr
		      logFormat: "%-5level [%thread][%logger{0}]: %message%n"
		
		    # Log info, warnings and errors to our apps' main log.
		    # Rolled over daily and retained for 5 days.
		    - type: file
		      threshold: INFO
		      currentLogFilename: ./logs/example.log
		      archivedLogFilenamePattern: ./logs/myapp-%d.log.gz
		      archivedFileCount: 5
		
		    # Log debug messages, info, warnings and errors to our apps' debug log.
		    # Rolled over hourly and retained for 6 hours
		    - type: file
		      threshold: DEBUG
		      currentLogFilename: ./logs/debug.log
		      archivedLogFilenamePattern: ./logs/debug-%d{yyyy-MM-dd-hh}.log.gz
		      archivedFileCount: 6
		      
if there is something wrong here :you better go to the official website :
	http://www.dropwizard.io/manual/configuration.html 
for help, I have got many help there.

2.1 change the frequency of getting the data
  I am using jobs for getting the temperature data from end device. 
Location is :
 	 yourprojectfolder/src/main/java/edu.itu.course.dropwizard.jobs.MyXbeeJob.java
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/myxbeejob.png)

I am using crontrigger for control the frequency . The format of this trigger is below:
Cron expressions are comprised of 6 required fields and one optional field separated by white space. The fields respectively are described as follows:
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/crontrigger.png)

For better understanding check here:
	http://quartz-scheduler.org/api/2.2.0/org/quartz/CronExpression.html
	
2.2 getting humidity data other than temperature
if you have changed you end device code into getting temperature data, cause in our device table:
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/device_table.png)

we can use either temperature or humidity. You may be need to add a new device here for getting the humidity data. Remember change the dataType into 'Humidity'; below scripts will help you :

 insert into device (id, name, status,dataType) values (2, 'gethumiditydevice', 0, 'Humidity');

notes: remember change your end device part device data into 2 also .
Go to your-end-device-project-folder/src/main/resources/xbee.properties :

![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/changexbeeproperty.png)


change from 1 to 2. There are maybe still something you need to do for how to display the humidity data in to browser .
Remember here:
	you need to change the TEMP into  HUMIDITY for sure:
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/changefront.png)

2.3 adding new APIs:
all our api file located in :
   yourprojectfolder/src/main/java/edu.itu.course.dropwizard.api;
   
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/apis.png)

or you can goto yourprojectfolder/src/main/resources/Documentation for API used.xlsx

you can check here for how a representation class working :
http://www.dropwizard.io/getting-started.html#creating-a-representation-class


here is a working flow of the API:

1.add the API in the representation class
2.implement the function in the 
    edu.itu.course.dropwizard.resources.DeviceResourceImpl
3.testing 

you can check in the DeviceResourceImpl class for learning how to implement the functions.


2.4 modify html/jsp pages :

on man! You can keep watching the tutorial to here, It means you must have a better understanding how this system works without knowing how the front page shows.

Here is the basic idea for the front end:
   I am using jquery + angularJS +google visulization.
If you are experienced in the front pages , you are all finished ! I believe you will know how to change the front page base your experience.
If you are not experienced in the front pages, you better learn some htmls+jquery+angularJS tutorials for basic understanding. I am not going to talk about the working mechanism of the front pages, cause there are so many things to talk
Next I will talk about basically how my front pages works:
 all the front page locates:
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/webpages.png)

 and configure the folder into the application:
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/configurewebpages.png)

for now the dropwizard will know all your web pages 

and the chart-1.1.js is mainly for communicating between server and the pages.
Working flow:
     1.in the showdata.jsp click confirm:
     2. in the  chart-1.1.js catch the click go to the confirm function
     3. inside the confirm function:
      	3.1 check the data and other things before submit to server
	    3.2 do the request for getting the data or relay the device
	    3.3 get the data and then draw the chart .

For showing the data I am using goolge visualization,learn how to use it from here:
 https://developers.google.com/chart/interactive/docs/reference

This is the basic working flow:
![alt tag](https://github.com/wentixiaogege/CourseProjectV1/blob/master/readme_img/basicwebpagesworking.png)


All right ! So till now you should have a basic working flow for how the front page works here.I am not plan to tell the query + angularJS+google visualization in deep in this tutorial.

Thanks !
-----------------------------------------
any questions:mail wentixiaogege@gmail.com 
	 