/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Thinal Kulathunga
 */

@ApplicationPath("/api/v1") // Base URI
public class SmartCampusApplication extends Application {
    // JAX-RS automatically scan the project and register any classes annotated with @Path or @Provider.
    
}
