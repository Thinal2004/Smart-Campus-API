/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.database;

import java.util.HashMap;
import java.util.Map;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;

/**
 *
 * @author HP
 */
public class MockDatabase {
    // Static maps to simulate database tables holding data in memory
    private static Map<String, Room> rooms = new HashMap<>();
    private static Map<String, Sensor> sensors = new HashMap<>();

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }
}
