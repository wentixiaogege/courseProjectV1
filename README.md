dropwizard-jdbi-template
========================

Project template for a REST service exposed through dropwizard 

This contains,


which works together out-of-the-box.

Usage
=====

To start the server,

- create the distributable by running "mvn clean install"
- move to target folder and run "java -jar dropwizard-jdbi-template-1.0-SNAPSHOT.jar server ../config.yaml"

The server will start at port 8080. You can use UserResourceClient to interact with it. You can also get into admin interface by going to http://localhost:8081

//add user
curl -i -X PUT -H "Content-Type: application/json" -d '{"id":1,"name":"jack","city":"San Jose"}' http://localhost:8080/user

//get user
curl -i -X GET -H "Content-Type: application/json"  http://localhost:8080/user/1

