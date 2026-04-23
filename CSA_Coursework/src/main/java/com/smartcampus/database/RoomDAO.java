/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.database;

import com.smartcampus.exceptions.RoomNotEmptyException;
import com.smartcampus.models.Room;
import java.util.Collection;

/**
 *
 * @author Thinal Kulathunga
 */
public class RoomDAO {

    public Collection<Room> getAllRooms() {
        return MockDatabase.getRooms().values();
    }

    public Room getRoom(String id) {
        return MockDatabase.getRooms().get(id);
    }

    public Room addRoom(Room room) {
        MockDatabase.getRooms().put(room.getId(), room);
        return room;
    }

    public void deleteRoom(String roomId) {
        Room targetRoom = MockDatabase.getRooms().get(roomId);
        // Check if the room exists
        if (targetRoom != null) {
            // Check for active sensors
            if (!targetRoom.getSensorIds().isEmpty()) {
                throw new RoomNotEmptyException("Cannot delete room: The room is currently occupied by active hardware.");
            }
        }

        // If the list is empty, delete room
        MockDatabase.getRooms().remove(roomId);
    }
}
