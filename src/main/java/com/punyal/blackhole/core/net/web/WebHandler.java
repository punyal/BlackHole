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

import static com.punyal.blackhole.constants.ConstantsNet.*;
import static com.punyal.blackhole.constants.ConstantsSystem.BH_VERSION;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mdevice;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mlist;
import static com.punyal.blackhole.core.net.web.MIMEtype.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONArray;
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
                    //fileResponse(webFile, baseRequest, request, response);
                }
                else fileNotFound(baseRequest, request, response);
            } else { // AJAX jQuery handlers...
                // listOfRockBolts
                switch (webFile.getFileName()) {
                    case "getServerInfo":
                        getServerInfo(baseRequest, request, response);
                        break;
                    case "getRockBoltsList":
                        getRockBoltsList(baseRequest, request, response);
                        break;
                    case "torch":
                        torch(baseRequest, request, response);
                        break;
                }
                
            }
        } 
        //System.out.println(target);
        //System.out.println(webFile.toString());
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
            
                while ((line = reader.readLine()) != null){
                    data.append(line);
                    data.append(System.getProperty("line.separator"));
                }
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
        while ((line = reader.readLine()) != null) data.append(line+"\n");
        response.setContentType(MIMEtype.getMIME(HTML_MIME_TYPE));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(data.toString());
    }
    
    
    public void getServerInfo(
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        /**
         * Outgoing data expected:
         *  {
         *      "version": 0.2,
         *      "dateTime": "Y-M-d h:m:s z",
         *      "devicesConnected": 4,
         *      "totalAlarms": 1,
         *      "totalMessages": 123,
         *      "criticalAlertMessage": "RockBolt-204 broken!!"
         *  } 
         */
        JSONObject json = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d HH:mm:ss z");
        
        json.put("version", BH_VERSION);
        json.put("dateTime", sdf.format(new Date(System.currentTimeMillis())));
        json.put("devicesConnected", devicesList.getNumberOnlineDevices());
        json.put("totalAlarms", devicesList.getTotalAlarms());
        json.put("totalMessages", devicesList.getTotalMessages());
        json.put("criticalAlertMessage", "");
        
        
        response.setContentType(MIMEtype.getMIME(JSON_MIME_TYPE));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(json.toJSONString());
    }
    
    
    public void getRockBoltsList(
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        /**
         *  Outgoing data expected:
         *  {
         *      "devices": [
         *          {
         *              "name": "\"RockBolt-240\"",
         *              "address": "fdfd:0:0:0:5c0c:7122:c2df:4a58",
         *              "battery": "12%",
         *              "in_com": 123,
         *              "out_com": 23,
         *              "vibration": 34,
         *              "strain": 21
         *          }
         *      ]
         *  }
         */
        
        JSONObject json = new JSONObject();
        JSONArray list = new JSONArray();
        JSONObject tmp;
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d HH:mm:ss z");
        
        //System.out.println("Connected devices: "+devicesList.getDevices().size());
        
        for (LWM2Mdevice device: devicesList.getDevices()) {
            tmp = new JSONObject();
            tmp.put(RB_NAME, device.getName());
            tmp.put(RB_ADDRESS, device.getEndPoint().getAddress());
            tmp.put(RB_BATTERY, device.getBatteryLevel());
            tmp.put(RB_IN, device.getMessageIn());
            tmp.put(RB_OUT, device.getMessageOut());
            tmp.put(RB_VIBRATION, device.getAlarmsVibration());
            tmp.put(RB_STRAIN, device.getAlarmsStrain());
            tmp.put(RB_STATUS, (device.isAlive())?"Online":"Offline");
            tmp.put(RB_LAST_CONNECTION, sdf.format(new Date(device.getLastUpdate())));
            list.add(tmp);
        }
        
        json.put("devices", list);
        
        response.setContentType(MIMEtype.getMIME(JSON_MIME_TYPE));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(json.toJSONString());
    }
    
    public void torch(
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        
        //System.out.println("------------------");
        String torchData = request.getParameter("torchdata");
        String name = torchData.substring(0, torchData.lastIndexOf("("));
        int mode = Integer.parseInt(torchData.substring(torchData.lastIndexOf("(")+1, torchData.lastIndexOf(")")));
        
        LWM2Mdevice device = devicesList.getDeviceByName(name);
        if(device != null) device.torch((mode != 0));
        
        //System.out.println("------------------");
        response.setContentType(MIMEtype.getMIME(HTML_MIME_TYPE));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("");
    }
    
}
