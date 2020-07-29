# Commands

### Building docker image
to build docker image choose name for image, for example `spring-boot-docker`

`./mvnw install`

`docker build -t spring-boot-demo .`


### Running docker container
to start the app run docker image exposing port 8080:

`docker run -p 8080:8080 spring-boot-demo`
 
## Curl commands

### User endpoint curl commands
`curl -X POST http://localhost:8080/user -H 'content-type: application/json' -d '{"user":"user1", "pwd":"pass1"}'`


`curl -X PATCH http://localhost:8080/user/me -H 'authorization: Basic dXNlcjE6cGFzczE=' -H 'content-type: application/json' -d '{"user":"changedUser1", "pwd":"changedpPass1"}'`


`curl -X GET http://localhost:8080/user/me -H 'authorization: Basic dXNlcjE6cGFzczE='`

### Articles endpoint curl commands

`curl -X GET http://localhost:8080/article/public`


`curl -X POST http://localhost:8080/article/my -H 'authorization: Basic dXNlcjE6cGFzczE=' -H 'content-type: application/json' -d '{"name":"this is name", "text":"this is text"}'`


`curl -X GET http://localhost:8080/article/my -H 'authorization: Basic dXNlcjE6cGFzczE='`


`curl -X PATCH
  http://localhost:8080/article/my/1
  -H 'authorization: Basic dXNlcjE6cGFzczE='
  -H 'content-type: application/json'
  -d '{"name": "changed tex", "text":"like to change my text"}'`
