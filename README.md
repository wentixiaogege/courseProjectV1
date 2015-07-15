dropwizard-jdbi-template
========================

Project template for a REST service exposed through dropwizard 

This contains,


which works together out-of-the-box.

Usage
=====

To start the server,

- create the distributable by running "mvn clean install"
- move to target folder and run "java -jar CourseProjectV1-0.0.1-SNAPSHOT.jar server ../config.yml"

The server will start at port 8080. You can use UserResourceClient to interact with it. You can also get into admin interface by going to http://localhost:8081

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

curl -i -X DELETE -H "Content-Type: application/json"  http://localhost:8080/devices/11

//relay the device
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/devices/1/1

