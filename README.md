# Reactive Api AddressBook Demo..

## Installation ##

Configure MongoDB connection in the *appliation.yml* file.

```yml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/yapstone
```

Before starting up your application, make sure there is a running MongoDB instance in your local system. 

**NOTE**: If you have not installed it, go to [Mongo download page](ttps://www.mongodb.com/download-center?jmp=nav#community) and get a copy of MongoDB, and install it into your system.

Alternatively, if you are familiar with Docker, it is simple to start a MongoDB instance via Docker Compose file.

```yml
version: '3.3' # specify docker-compose version

# Define the services/containers to be run
services:

      
  mongodb: 
    image: mongo 
    volumes:
      - mongodata:/data/db
    ports:
      - "27017:27017"
    command: --smallfiles --rest


volumes:
  mongodata:  
```

Execute the following command to start a Mongo instance in a Docker container.

```
docker-compose up mongodb
```

When the Mongo service is started, it is ready for bootstraping the application.

```
mvn spring-boot:run
```

## Curl Tests ##

### Create Address Book Entry ###

curl -vvv  -d '{"firstName":"Bob", "lastName":"Builder", "pnoneNumber":"083 439 6070", "address":"Dundalk, Co. Louth", "emailAddress":"bob@yapstone.ie"}' -H "Content-Type: application/json" -X POST 'http://localhost:8080/api/v1/addressbooks'

### Update Address Book Entry ###

curl -vvv  -d '{"firstName":"Bob", "lastName":"Builder 2 ", "pnoneNumber":"083 439 6070", "address":"Dundalk, Co. Louth", "emailAddress":"bob@yapstone.ie"}' -H "Content-Type: application/json" -X PUT 'http://localhost:8080/api/v1/addressbooks/5daf51bda1ff575cbf8273ce'

### Get Address Book Entry by Id ###

curl -vvv  'http://localhost:8080/api/v1/addressbooks/5daf51bda1ff575cbf8273ce'


### Get All Address Book Entries ###

curl -vvv  'http://localhost:8080/api/v1/addressbooks'

### Delete Address Book Entry ###

curl -vvv -X DELETE 'http://localhost:8080/api/v1/addressbooks/5daf51bda1ff575cbf8273ce'



## Mongo Db quick reference guide ##

https://docs.mongodb.com/manual/reference/mongo-shell/

```
> use yapstone
switched to db yapstone
address_books
> db.address_books.find()
{ "_id" : ObjectId("5daf51bda1ff575cbf8273ce"), "firstName" : "Bob", "lastName" : "Builder", "address" : "Dundalk, Co. Louth", "emailAddress" : "bob@yapstone.ie", "_class" : "com.yapstone.demo.domain.AddressBook" }
```





