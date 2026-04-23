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
 * @author Thinal Kulathunga
 */
@Path("rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    // Instantiates the Data Access Object to interact with the mock database
    RoomDAO roomDAO = new RoomDAO();

    // Retrieves a list of all rooms in the system.
    @GET
    public Collection<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    // Retrieves room by ID
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(room).build();
    }

    // Create a new room
    @POST
    public Response createRoom(Room room) {
        Room newRoom = roomDAO.addRoom(room);
        return Response.created(URI.create("/api/v1/rooms/" + newRoom.getId()))
                .entity(newRoom)
                .build();
    }

    // Room decommisioning
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        roomDAO.deleteRoom(roomId);
        return Response.noContent().build();
    }
}
