# vehicle_charging_station
vehicle charging station management system

This is a RESTful Microservice for an “electric vehicle charging station management system” application.
Companies have stations. Companies have children that have stations, and those belong to the parent company initially.

- endpoint: http://localhost:8080/
- swagger: http://localhost:8080/swagger-ui/index.html

# Prerequisites

JDK 1.8
Spring Boot
Maven 4.0.0
PostgreSql

# Data
Data is stored in PostgreSql database                                    
Tables:                                                   
1. Station (id, name, latitude, longitude, company_id)
2. Company (id, parent_company_id, name)

# Libraries used
Hibernate Validation API                          
Lombok                         
JSON Library

# External tools
Postman

# Package structure
ws: sub packages : controller , dto , exception                         
core: sub packages : model, service: serviceImpl                         
infrastructure: subpackages : entity, repositories                        
visibility:                         
ws -> core -> infrastructure                         
object  conversation: model mapper

# Features
- CRUD operations for both company and station and more
- Logger
- Custom exceptions
- Unit tests

# instructions

use Postman

##########################################

- create station

POST http://localhost:8080/station
JSON body:
{
"name": "station_name",
"latitude": 21,
"longitude": 21,
"companyId":1
}

##########################################

- update station by id

PUT http://localhost:8080/station/{id}
JSON body:
{
"name": "station_name",
"latitude": 21,
"longitude": 21,
"companyId":2
}

##########################################

- get all stations

GET http://localhost:8080/station

##########################################

- get station by id 

GET http://localhost:8080/station/{id}

##########################################

- get station within the radius of n kilometers from a point (latitude, longitude) ordered by distance

GET http://localhost:8080/station/withinRadius?radius=4&longitude=2&latitude=2

##########################################

- get station and all child stations given a company id

GET http://localhost:8080/station/withChildStations/{id}

##########################################

- delete station by id 

DELETE http://localhost:8080/station/{id}

##########################################

- create company

POST http://localhost:8080/company
JSON body:
{
"name": "company_name",
"parentCompany": 21
}

##########################################

- update company by id

PUT http://localhost:8080/company/{id}
JSON body:
{
"name": "company_name",
"parentCompany": 22
}

##########################################

- get all companies

GET http://localhost:8080/company

##########################################

- get company by id

GET http://localhost:8080/company/{id}

##########################################

- delete company by id

DELETE http://localhost:8080/company/{id}

