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
import static com.punyal.blackhole.core.net.web.MIMEtype.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class WebHandler extends AbstractHandler{
    private final LWM2Mlist devicesList;
    
    public WebHandler(LWM2Mlist devicesList) {
        this.devicesList = devicesList;
    }
    
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        
        // Check incoming target
        WebFile webFile = new WebFile(target);
        
        // Check specific cases:
        if (webFile.getExtension().isEmpty()) { // No extension
            if (webFile.getFileName().isEmpty()) {
                if (webFile.getFolder().equals("/")) { // Index
                    webFile.setTarget("/index.html");
                    fileResponse(webFile, baseRequest, request, response);
                }
                else fileNotFound(baseRequest, request, response);
            } else { // AJAX jQuery handlers...
                // listOfRockBolts
                listOfRockBolts(baseRequest, request, response);
                
            }
        } else {
            fileResponse(webFile, baseRequest, request, response);
        }
        
        System.out.println(webFile.toString());
    }
    
    public void fileResponse(WebFile webFile,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
       
        
        // Read the files and send the response
        InputStream is = WebHandler.class.getResourceAsStream("/pages"+webFile.getFileRoute());
        if (is == null) {
            fileNotFound(baseRequest, request, response);
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder data = new StringBuilder();
            while ((line = reader.readLine()) != null) data.append(line);
            response.setContentType(MIMEtype.getMIME(webFile.getExtension()));
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println(data.toString());
        }
        
        
    }
    
    
    public void fileNotFound(
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        // Read the files and send the response
        InputStream is = WebHandler.class.getResourceAsStream("/pages/404.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder data = new StringBuilder();
        while ((line = reader.readLine()) != null) data.append(line);
        response.setContentType(MIMEtype.getMIME(HTML_MIME_TYPE));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(data.toString());
    }
    
    
    public void listOfRockBolts(
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        JSONObject json = new JSONObject();
        json.put("time_date", (new Date(System.currentTimeMillis())).toString());
        json.put("connected_devices", ""+devicesList.size());
        
        response.setContentType(MIMEtype.getMIME(JSON_MIME_TYPE));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(json.toJSONString());
    }
    
}
