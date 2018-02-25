# Newse - Backend service for reading news articles

## Starting the application

  `./gradlew run`  

Once the application is started, it can be reached at

  `http://localhost:8080`
  
## Endpoints   
The following are the rest endpoints:

| HTTP Verb        | URL           | Description  | Status Codes |
| ------------- |-------------|:-----| ----|
| `GET` | `http://localhost:8080/article/{id}` | Gets the article corresponding to the supplied article ID | <ul><li>`200 OK` if article exists</li><li>`404 Not Found` if article does not exist</li></ul> |
| `GET` | `http://localhost:8080/articles?keyword={keyword}&author={author}&from={fromDate}&to={toDate}` | <ul><li>Gets the list of articles matching the provided query params</li> <li>gets all articles if no query params are provided, all query params are optional</li> <li>date format is yyyy-MM-dd'T'HH:mm:ss.SSSZ</li></ul>| <ul><li>`200 OK` if article exists</li><li>`404 Not Found` if article does not exist</li></ul> |
| `POST` | `http://localhost:8080/article` | Creates a new article based on the payload contained in the request body and basic auth | <ul><li>`201 Created` if article successfully created</li><li>`401 Unauthorized` If it is not editor</li></ul> |
| `PUT` | `http://localhost:8080/article/{id}` | Updates an existing article with the data contained in the request body | <ul><li>`200 OK` if article successfully updated</li><li>`404 Not Found` if article does not exist</li><li>`401 Unauthorized` If it is not editor</li></ul> |
| `DELETE` | `http://localhost:8080/article/{id}` | Deletes an existing article that corresponds to the supplied article ID | <ul><li>`204 No Content` if article successfully deleted</li><li>`404 Not Found` if article does not exist</li><li>`401 Unauthorized` If it is not editor</li></ul> |

## Some more Gradle commands
    
    To build the application
    `./gradlew build`     
    
    To run all tests
    `./gradlew test`

## Authorization

- Uses basis access authentication to differentiate between an editor and user 
    * Improvement is to use OAuth2.0 based authorization, which is more secured since basic auth is vulnerable because the authorization is set in the header.
- Credentials are stored in properties file, which will be a devops automation generated file in prod.     