/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.database.SensorDAO;
import com.smartcampus.database.SensorReadingDAO;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;
    private SensorReadingDAO readingDAO = new SensorReadingDAO();
    private SensorDAO sensorDAO = new SensorDAO();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReading() {
        return readingDAO.getReadingsForSensor(sensorId);
    }
    
    @POST
    public Response addReading(SensorReading newReading){
        Sensor parentSensor = sensorDAO.getSensor(sensorId);
        if (parentSensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Sensor not found. Cannot add reading.\"}")
                           .build();
        }
        
        SensorReading createdReading = readingDAO.addReading(sensorId, newReading);
        
        // Update sensor's currentValue
        parentSensor.setCurrentValue(createdReading.getValue());
        sensorDAO.updateSensor(sensorId, parentSensor); 

        // Return success response
        return Response.created(URI.create("/api/v1/sensors/" + sensorId + "/readings/" + createdReading.getId()))
                       .entity(createdReading)
                       .build();
    }
}
