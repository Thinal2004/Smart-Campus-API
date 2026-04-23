/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.filters;

import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Thinal Kulathunga
 */

@Provider
@PreMatching
public class ApiFilter implements ContainerRequestFilter, ContainerResponseFilter{
    
    private static final Logger LOGGER = Logger.getLogger(ApiFilter.class.getName());

    // Runs before the request reaches Resource methods
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getPath();
        LOGGER.info(">>> INCOMING HTTP REQUEST: [" + method + "] /" + uri);
    }

    // Runs after resource method finishes, right before sending the data to the client
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        LOGGER.info("<<< OUTGOING HTTP RESPONSE: Status Code [" + status + "]");
    }
}
