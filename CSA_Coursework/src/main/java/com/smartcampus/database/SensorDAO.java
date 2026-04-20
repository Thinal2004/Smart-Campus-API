/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.database;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import java.util.Collection;

/**
 *
 * @author HP
 */
public class SensorDAO {

    // Fetch all sensors
    public Collection<Sensor> getAllSensors() {
        return MockDatabase.getSensors().values();
    }

    // Create a new sensor
    public Sensor addSensor(Sensor sensor) {

        MockDatabase.getSensors().put(sensor.getId(), sensor);
        
        // Add sensor to the list in the room
        Room parentRoom = MockDatabase.getRooms().get(sensor.getRoomId());
        if (parentRoom != null) {
            parentRoom.getSensorIds().add(sensor.getId());
        }
        return sensor;  
    }
    
    // Get a sensor by id
    public Sensor getSensor(String sensorId){
        return MockDatabase.getSensors().get(sensorId);
    }
    
    // Update an existing sensor 
    public Sensor updateSensor(String sensorId, Sensor updatedSensor) {
        // Update if the sensor exists
        if (MockDatabase.getSensors().containsKey(sensorId)) {
            updatedSensor.setId(sensorId); 
            MockDatabase.getSensors().put(sensorId, updatedSensor);
            return updatedSensor;
        }
        return null;
    }
}
