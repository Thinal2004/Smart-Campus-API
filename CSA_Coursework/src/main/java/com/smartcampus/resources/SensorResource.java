 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.database.RoomDAO;
import com.smartcampus.database.SensorDAO;
import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Sensor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Thinal Kulathunga
 */
@Path("sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // DAOs for interact with mockdatabase
    private SensorDAO sensorDAO = new SensorDAO();
    private RoomDAO roomDAO = new RoomDAO();

    // Retrieves all sensors
    @GET
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> sensors = sensorDAO.getAllSensors();
        if (type == null) {
            return sensors;
        }

        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : sensors) {
            if (type.equalsIgnoreCase(sensor.getType())) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }

    // Add new sensor
    @POST
    public Response createSensor(Sensor sensor) {
        String roomId = sensor.getRoomId();

        if (roomId == null || roomDAO.getRoom(roomId) == null) {
            throw new LinkedResourceNotFoundException("The specified roomId does not exist.");
        }

        Sensor newSensor = sensorDAO.addSensor(sensor);
        return Response.created(URI.create("/api/v1/sensors/" + newSensor.getId()))
                .entity(newSensor).build();
    }
    
    // Get sensor readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

}
