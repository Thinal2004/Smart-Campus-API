# Smart Campus RESTful API (JAX-RS)

## Overview of API Design
This project is a RESTful Web API built using Java and the **JAX-RS (Jersey)** framework. It simulates a Smart Campus environment, allowing clients to manage **Rooms**, assign **Sensors** (e.g., Temperature, CO2) to those rooms, and record time-stamped **Readings** for each sensor.

**Key Architectural Features:**
* **Resource-Oriented Architecture:** Strictly adheres to REST principles using standard HTTP methods (`GET`, `POST`, `DELETE`) mapped to logical URIs (`/rooms`, `/sensors`).
* **Sub-Resource Routing:** Implements nested relationships (e.g., `/sensors/{id}/readings`) to logically encapsulate child data.
* **Thread-Safe In-Memory Storage:** Utilizes a Singleton `MockDatabase` powered by `ConcurrentHashMap` to ensure data integrity during simultaneous HTTP requests without relying on external SQL databases.
* **Advanced Error Handling:** Implements custom JAX-RS `ExceptionMapper` classes to provide clear, JSON-formatted responses for 404 (Not Found), 422 (Unprocessable Entity - Dependency Validation), and a 500 (Internal Server Error) Global Safety Net that hides Java stack traces from end-users for enhanced security.
* **API Observability:** Includes custom `ContainerRequestFilter` and `ContainerResponseFilter` to intercept and log all incoming traffic and outgoing status codes.

---

## Build and Launch Instructions

Follow these steps to compile and run the API locally using Apache NetBeans and Tomcat.

### Prerequisites
* **Java Development Kit (JDK):** Version 11 or higher.
* **Apache Tomcat:** Version 9.0 or higher.
* **IDE:** Apache NetBeans (with Maven support).

### Step-by-Step Guide
1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/Thinal2004/Smart-Campus-API.git](https://github.com/Thinal2004/Smart-Campus-API.git)
1. **Open the Project:**
   Launch Apache NetBeans, go to **File > Open Project**, and select the cloned folder.

2. **Clean and Build:**
   Right-click the project folder in the NetBeans Project Explorer and select **Clean and Build**. Maven will download the required JAX-RS dependencies and compile the code.

3. **Deploy to Server:**
   Right-click the project and select **Run**. NetBeans will automatically package the `.war` file, deploy it to your configured Apache Tomcat server, and launch the application.

4. **Verify the Server:**
   The server will start at `http://localhost:8080/SmartCampusAPI`. (Check the NetBeans Tomcat output console to ensure there are no startup errors).

## 💻 Sample API Interactions (cURL Commands)

The following `curl` commands demonstrate successful interactions with the core endpoints of the API. *(Note: Adjust the base URL `http://localhost:8080/SmartCampusAPI/api/v1` to match your local server configuration).*

### 1. View API Discovery Metadata
Returns the root metadata and available resource links.
```bash
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1 \
     -H "Accept: application/json"
```

### 2. Create a New Room
Registers a new room in the system.
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
     -H "Content-Type: application/json" \
     -d '{
           "id": "LAB-404",
           "name": "Advanced Networking Lab",
           "capacity": 35
         }'
```

### 3. Create a Dependent Sensor (Requires Valid Room)
Creates a new sensor and links it to an existing room. (Note: If roomId does not exist, the API returns a custom 422 Unprocessable Entity).
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
     -H "Content-Type: application/json" \
     -d '{
           "id": "SENS-999",
           "type": "CO2",
           "status": "ACTIVE",
           "roomId": "LAB-404"
         }'
```

### 4. Filter Sensors by Type (Query Parameter)
Retrieves a list of sensors, dynamically filtered by the type parameter (Case-Insensitive).
```
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2" \
     -H "Accept: application/json"
```

### 5. Add a Nested Reading to a Sensor (Sub-Resource)
Posts a new data reading to a specific sensor using nested routing.
```
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/SENS-999/readings \
     -H "Content-Type: application/json" \
     -d '{
           "value": 415.5,
           "timestamp": "2026-04-24T10:30:00Z"
         }'
```
## REPORT
### Question: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronise your in-memory data structures (maps/lists) to prevent data loss or race conditions.

The default lifecycle of a JAX-RS resource class is “per-request”. With each incoming HTTP request, a brand-new instance of the resource class (such as SensorRoomResource or SensorResource) is created and destroyed immediately after the associated response is sent to the client. The resource class does not default to being treated as a singleton.
Since the JAX-RS runtime creates a new instance of the resource for each request, it is not possible to store application data in standard instance variables in resource classes. The data would instantly disappear with the next request. Therefore, in order to persist data across multiple independent requests, a separate Data Access (DAO) layer that interacts with the MockDatabase class is utilised. 
To avoid race conditions, ConcurrentHashMap is utilised inside the MockDatabase class to store resource instances. It provides thread safety by allowing concurrent reads and serialising all atomic write operations. This will prevent data corruption caused by multiple clients accessing the same data concurrently from the REST API.



### Question: Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

This provision of “Hypermedia” benefits client developers in various ways. This approach completely decouples the front-end client from the backend's routing structure. The frontend developer no longer has to hardcode endpoint URLs into their applications or reference static documentation for the correct path, but instead dynamically navigate the API using the URIs returned in the response. 
It creates centralisation of business logic and state on the server. For instance, if a sensor enters a maintenance state, the backend will just remove the link to allow new readings. Therefore, the client developer doesn’t need to write conditional logic to see if the action is allowed. The front-end will only render user interface elements based on the presence or absence of links returned by the API



### Question: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

Returning only Room IDs instead of the full list of Room objects reduces the network bandwidth usage because the JSON payload is smaller. This is helpful for users on slow connections. However, if the frontend needs to display a table of rooms with names, capacities, and available sensors, it has to call the list endpoint first (/rooms) and then separate calls for each room id to get data (/rooms/{roomId}). This increases the load on the server from the large number of handshakes required to complete HTTP requests.
In this Smart Campus Application, there could only be a few rooms rather than thousands; returning full Room objects is the more efficient approach.



### Question: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Yes, the DELETE operation is idempotent. Idempotent means it has the same effect on the server state regardless of how many requests are made. 
If a client sends a DELETE request for a valid room ID and no sensors are assigned to that room, the server will delete the existing room and return an HTTP response of 204 No Content to the client. If the client mistakenly sends the same DELETE request again, the server will return a response of 404 Not Found.

Although the HTTP responses between the first and second DELETE requests are different (204 and 404), the state of the server is unchanged after successfully completing the first DELETE operation. The room still does not exist in the server after the initial request. 



### Question: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

The @Consumes(MediaType.APPLICATION_JSON) annotation is used to establish a strict contract about the incoming "Content-Type" header. If the client tries to pass data of another type, such as text/plain or application/xml without JSON, the JAX-RS runtime will intercept the request before it ever gets to the resource method logic. It will reject the request and return an HTTP 415 Unsupported Media Type status.

This feature is important because it prevents the application from trying to parse incompatible data structures, which could potentially cause internal parsing errors or unexpected NullPointerExceptions. Therefore, by enforcing this mismatch at the framework level, the API is ensuring that only valid, expected data formats will be processed by the business logic.



### Question: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?

Filtering using path variables (eg, /sensors/type/CO2) makes the API hierarchy inflexible and complicated. For example, if a developer wanted to filter both by type and status, then using path-based filtering would require a non-standard and complex URL like /sensors/type/CO2/status/ACTIVE. With query parameters, multiple filters can be combined in an ordering of the Developer’s choice (eg, ?type=CO2&status=ACTIVE) without changing the resource URI. 

Tracking query parameters are semantically recognised by browsers, caches, and developers as "Search Criteria," thus maintaining API intuitiveness. For example, In /api/v1/sensors, there is always the complete sensor collection available to return, and the query parameters simply provide a filtered subset of that same collection, rather than pointing to a completely different sub-resource path.



### Question: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?

Instead of creating every nested endpoint inside the SensorResource class, the Sub-Resource Locator Pattern is used to delegate the sub-path (e.g., sensors/{id}/readings/{rid}) to a dedicated sub-resource class (SensorReadingResource). 
Managing cognitive load and maintaining the code base are the major benefits of this model. For a large API with a single controller class that is responsible for many nested paths, it becomes extremely difficult to read, debug, and test.

Additionally, this pattern allows for both reuse and scalability. For example, sub-resource classes can now be instantiated dynamically and even injected with a specific context or piece of information (such as sensorId) provided by its parent (the SensorResource). Therefore, the API's modularity allows many developers to work on their respective parts independently, without risking a merge conflict in an enormous class file. Furthermore, because sensor readings can be tested apart from the logic associated with room management or sensor registration, writing unit tests for the API will become relatively simple. Ultimately, the Sub-Resource Locator pattern ensures the API remains organised rather than cluttered and fragile.



### Question: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

HTTP status code 404 (Not Found) indicates that the requested URI doesn’t exist on the server. In a situation where a client attempts to access a non-existent endpoint (e.g., /api/v1/students), a 404 response is appropriate because the path is incorrect. 

If the requested URI path is perfectly correct (e.g., /api/v1/sensors) and the JSON payload is also syntactically valid/well formatted, but the roomId mentioned in the request body is not available in the database, the issue is not in the location(path), but in the logic. 

The use of the 422 (unprocessable entity) status code is more appropriate because it explicitly communicates to the client that the server understands the correct content type and well-formed syntax of the request, but has been unable to process the instructions sent due to their semantic errors. Using the 422 status code allows clients to distinguish between a broken link (404) and a data validation error (422), enhancing the clarity of the error-handling contract for this API.



### Question: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing internal Java stack traces to external API consumers creates severe security vulnerabilities. Stack traces can specifically expose the framework versions, the internal file system paths on the application server, and the database that the application is connecting to. Attackers can use the information contained within the stack trace to cross-reference known vulnerabilities or develop an attack methodology to exploit the target, such as Path Traversal or SQL injection.  

By implementing a global ExceptionMapper for your API to replace exposed Java stack traces with a generic HTTP response code of 500 (Internal Server Error), the sensitive architectural details of your application are kept private, and the server environment is protected, without impacting the ability of developers to view the actual errors in private server logs.



### Question: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

The use of JAX-RS filters for logging decouples the operational auditing from the core business logic. Manually inserting 'Logger.info()' statements inside each resource method can lead to cluttered code, violating the DRY (Don't Repeat Yourself) principle. Also, it is subject to human errors. If a developer forgets to add a new log statement for an updated endpoint, it creates a gap in observability of the API and provides inconsistent debugging through the system.

Filters intercept requests and responses at the frame level, allowing them to capture essential data even for requests that fail before they reach a resource method, such as 401 Unauthorised or 415 Unsupported Media Type errors. This increases the scalability of the API through one change in the logging format or additional metadata within one class, versus changing all resource classes impacted by the logging changes throughout the application.
