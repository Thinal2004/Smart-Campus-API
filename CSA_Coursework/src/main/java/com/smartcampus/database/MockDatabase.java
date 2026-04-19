/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.database;

import java.util.HashMap;
import java.util.Map;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.List;

/**
 *
 * @author HP
 */
public class MockDatabase {
    // Static maps to simulate database tables holding data in memory
    private static Map<String, Room> rooms = new HashMap<>();
    private static Map<String, Sensor> sensors = new HashMap<>();
    private static Map<String, List<SensorReading>> sensorReadings = new HashMap<>();
    
    static{
        // Mock rooms
        Room room1 = new Room();
        room1.setId("LIB-301");
        room1.setName("Library Quiet Study");
        room1.setCapacity(50);
        rooms.put(room1.getId(), room1);

        Room room2 = new Room();
        room2.setId("COMP-101");
        room2.setName("Computer Science Lab");
        room2.setCapacity(30);
        rooms.put(room2.getId(), room2);

        // Mock sensors
        Sensor sensor1 = new Sensor();
        sensor1.setId("SENS-001");
        sensor1.setType("TEMP");
        sensor1.setStatus("ACTIVE");
        sensor1.setRoomId("COMP-101"); 
        sensors.put(sensor1.getId(), sensor1);

        Sensor sensor2 = new Sensor();
        sensor2.setId("SENS-002");
        sensor2.setType("CO2");
        sensor2.setStatus("MAINTENANCE");
        sensor2.setRoomId("LIB-301");
        sensors.put(sensor2.getId(), sensor2);
        
        Sensor sensor3 = new Sensor();
        sensor3.setId("SENS-003");
        sensor3.setType("SMOKE");
        sensor3.setStatus("OFFLINE");
        sensor3.setRoomId("COMP-101");
        sensors.put(sensor3.getId(), sensor3);
    }

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }
    
    public static Map<String, List<SensorReading>> getSensorReadings() {
        return sensorReadings;
    }
}
