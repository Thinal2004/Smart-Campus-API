/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("api_version", "v1.0");
        metadata.put("contact", "admin@smartcampus.ac.uk");
        metadata.put("description", "Smart Campus Sensor & Room Management API");

        // HATEOAS Links mapping primary collections
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");

        metadata.put("resources", links);

        return Response.ok(metadata).build();
    }
}
