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
package com.punyal.blackhole.core.net.lwm2m;

import static com.punyal.blackhole.constants.ConstantsNet.LWM2M_SERVER_RESOURCE_RD;
import com.punyal.blackhole.core.net.EndPoint;
import java.net.Inet6Address;
import java.util.Iterator;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Msniffer extends Thread {
    private final EndPoint endPoint;
    private final LWM2Mlist devicesList;
    private boolean running;
    
    public LWM2Msniffer(EndPoint endPoint, LWM2Mlist devicesList) {
        this.endPoint = endPoint;
        this.devicesList = devicesList;
        this.setDaemon(true);
        running = true;
    }
    
    public void startThread() {
        this.start();
    }
    
    public void stopThread() {
        running = false;
    }
    
    @Override
    public void run() {
        String uri;
        if (endPoint.getInetAddress() instanceof Inet6Address)
            uri = "coap://["+endPoint.getAddress()+"]:"+endPoint.getPort()+LWM2M_SERVER_RESOURCE_RD;
        else
            uri = "coap://"+endPoint.getAddress()+":"+endPoint.getPort()+LWM2M_SERVER_RESOURCE_RD;
        
        System.out.println("Sniffing uri="+uri);
        CoapClient coapClient = new CoapClient(uri);
        CoapResponse response;
        try {
            while (running) {
                response = coapClient.get();
                
                if (response != null && !response.getResponseText().isEmpty()) {
                    checkConnectedMulles(response.getResponseText());
                }
                
                try {
                    Thread.sleep(2000); // Sleep 2s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        } finally {
            System.out.println("Sniffer dead!");
        }
        
    }
    
    private void checkConnectedMulles(String response) {
        JSONArray jsonArray = (JSONArray) JSONValue.parse(response);
        Iterator i = jsonArray.iterator();
        LWM2Mdevice device;
        while (i.hasNext()) {
            JSONObject json = (JSONObject) i.next();
            String name = json.get("endPoint").toString();
            String id = json.get("registrationId").toString();
            String host = json.get("address").toString().substring(1);
            int port = Integer.parseInt(json.get("port").toString());
            //System.out.println(name+" ("+id+") "+host+":"+port);
            
            device = devicesList.getDeviceByName(name);
            
            if (device == null) // Add new device
                devicesList.addDevice(name, id, host, port);
            else {
                if (!device.getId().equals(id)) {
                    //System.out.println(name+" new id:"+id+ " old id:"+device.getId());
                    //System.out.println("Last update:"+ device.getLastUpdate());
                    device.updateID(id);
                    //System.out.println("Current time:"+ device.getLastUpdate());
                }
            }
        }
    }
}
