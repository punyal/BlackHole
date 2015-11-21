/*
 * The MIT License
 *
 * Copyright 2015 Pablo Puñal Pereira <pablo.punal@ltu.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.punyal.blackhole.core.net.web;

import com.punyal.blackhole.core.net.lwm2m.LWM2Mlist;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class WebServer {
    
    public WebServer(LWM2Mlist devicesList) {        
        Server server = new Server(3000);
        ResourceHandler resource_handler = new ResourceHandler();
        
        String  baseStr  = "/pages";  //... contains: helloWorld.html, login.html, etc. and folder: other/xxx.html
        URL     baseUrl  = WebServer.class.getResource( baseStr ); 
        String  basePath = baseUrl.toExternalForm();
        
        
        resource_handler.setResourceBase(basePath);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{new WebHandler(devicesList),resource_handler,new DefaultHandler()});
        server.setHandler(handlers);
        System.out.println("Starting Server at: " + server.getURI());
        try {
            server.start();
            //server.join();
        } catch (Exception ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
