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
