/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.database;

import com.smartcampus.models.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author HP
 */
public class SensorReadingDAO {
    // Fetch all readings for a specific sensor
   public List<SensorReading> getReadingsForSensor(String sensorId) {
        return MockDatabase.getSensorReadings().getOrDefault(sensorId, new ArrayList<>());
    }
   
   public SensorReading addReading(String sensorId, SensorReading newReading){
       // Generate a unique ID
        newReading.setId(UUID.randomUUID().toString());
        
        // If the timestamp is empty
        if (newReading.getTimestamp() == 0) {
            newReading.setTimestamp(System.currentTimeMillis());
        }
        
        // Fetch the history, add the new reading, and save it back
        List<SensorReading> history = getReadingsForSensor(sensorId);
        history.add(newReading);
        MockDatabase.getSensorReadings().put(sensorId, history);
        
        return newReading;
   }
}
