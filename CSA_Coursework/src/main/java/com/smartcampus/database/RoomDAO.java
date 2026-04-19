/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.database;

import com.smartcampus.models.Room;
import java.util.Collection;

/**
 *
 * @author HP
 */
public class RoomDAO {
    public Collection<Room> getAllRooms(){
        return MockDatabase.getRooms().values();
    }
    
    public Room getRoom(String id){
        return MockDatabase.getRooms().get(id);   
    }
    
    public Room addRoom(Room room){
        MockDatabase.getRooms().put(room.getId(), room);
        return room;
    }
    
    public void deleteRoom(String roomId) {
        MockDatabase.getRooms().remove(roomId);
    }
}


