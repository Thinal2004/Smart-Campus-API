/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thinal Kulathunga
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        
        // Avoid errors like 404 from safety net
        if (exception instanceof WebApplicationException) {
            WebApplicationException webEx = (WebApplicationException) exception;
            int statusCode = webEx.getResponse().getStatus();
            String errorMessage = webEx.getMessage(); 
            
            return Response.status(statusCode)
                    .type(MediaType.APPLICATION_JSON) 
                    .entity("{\"error\": \"" + errorMessage + "\"}")
                    .build();
        }
        
        // Log the real error in the console
        System.err.println("CRITICAL SERVER ERROR: " + exception.getMessage());
        exception.printStackTrace();
        
        // Send generic 500 error to the client
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\": \"An unexpected internal server error occurred. Please contact support.\"}")
                .build();
    }
}
