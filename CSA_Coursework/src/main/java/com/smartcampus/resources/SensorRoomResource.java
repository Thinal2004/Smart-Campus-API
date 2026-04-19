/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.database.RoomDAO;
import com.smartcampus.models.Room;
import java.net.URI;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Path("rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    
    RoomDAO roomDAO = new RoomDAO();
    
    @GET
    public Collection<Room> getAllRooms(){
        return roomDAO.getAllRooms();
    }
    
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(room).build();
    }
    
    @POST
    public Response CreateRoom(Room room){
        Room newRoom = roomDAO.addRoom(room);
        return Response.created(URI.create("/api/v1/rooms/" + newRoom.getId()))
                       .entity(newRoom)
                       .build();
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);
        
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Prevent deletion if sensors are attached
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            
            return Response.status(Response.Status.CONFLICT)
                           .entity("{\"error\": \"Cannot delete room: active sensors attached.\"}")
                           .build();
        }

        roomDAO.deleteRoom(roomId);
        return Response.noContent().build();
    }
}
