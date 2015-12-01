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
package com.punyal.blackhole.core.net.rockbolt;

import static com.punyal.blackhole.constants.ConstantsNet.*;
import com.punyal.blackhole.core.net.CoapObserver;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mdevice;
import com.punyal.blackhole.tentacle.Ticket;
import com.punyal.blackhole.utils.Parsers;
import org.eclipse.californium.core.CoapResponse;
import org.json.simple.JSONObject;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class RockboltClient extends Thread {
    private final  LWM2Mdevice device;
    private final Ticket myTicket;
    private boolean running;
    private final CoapObserver strainObserver;
    private final CoapObserver rmsObserver;
    private final CoapObserver batteryObserver;
    
    
    public RockboltClient(Ticket myTicket, final LWM2Mdevice device) {
        this.myTicket = myTicket;
        this.device = device;
        this.setDaemon(true);
        running = true;
        strainObserver = new CoapObserver(myTicket, device.getEndPoint(), COAP_RESOURCE_STRAIN) {
            
            @Override
            public void incomingData(CoapResponse response) {
                device.increaseMessageIn();
                if (!response.getResponseText().isEmpty()) {
                    try {
                        device.incomingData(COAP_RESOURCE_STRAIN, response.getResponseText());
                        JSONObject json = Parsers.senml2json(response.getResponseText());
                        device.addStrainData(Integer.parseInt(json.get("strain").toString()));
                    } catch(NullPointerException ex) {}
                }
                    
            }
            
            @Override
            public void error() {
                System.out.println("Error Strain resource on "+device.getName());
            }
        };
        rmsObserver = new CoapObserver(myTicket, device.getEndPoint(), COAP_RESOURCE_RMS) {
            
            @Override
            public void incomingData(CoapResponse response) {
                device.increaseMessageIn();
                if (!response.getResponseText().isEmpty()) {
                    try {
                        device.incomingData(COAP_RESOURCE_RMS, response.getResponseText());
                        JSONObject json = Parsers.senml2json(response.getResponseText());
                        device.addVibrationData(Float.parseFloat(json.get("X").toString()), Float.parseFloat(json.get("Y").toString()), Float.parseFloat(json.get("Z").toString()));
                    } catch(NullPointerException ex) {}
                }
            }
            
            @Override
            public void error() {
                System.out.println("Error RMS resource on "+device.getName());
            }
        };
        
        batteryObserver = new CoapObserver(myTicket, device.getEndPoint(), COAP_RESOURCE_BATTERY) {
            
            @Override
            public void incomingData(CoapResponse response) {
                device.increaseMessageIn();
                if (!response.getResponseText().isEmpty()) {
                    JSONObject json = Parsers.senml2json(response.getResponseText());
                    if (json.get("Vbat") != null) {
                        int battery = Integer.parseInt(json.get("Vbat").toString());
                        device.setBatteryLevel(battery);
                    }
                }
            }
            
            @Override
            public void error() {
                System.out.println("Error RMS resource on "+device.getName());
            }
        };
    }
    
    public void startThread() {
        start();
        //System.out.println("starting observe");
        strainObserver.startObserve();
        rmsObserver.startObserve();
        batteryObserver.startObserve();
    }
    
    public void stopThread() {
        running = false;
        strainObserver.stopObserver();
        rmsObserver.stopObserver();
        batteryObserver.stopObserver();
    }
    
    @Override
    public void run() {
        try {
            while (running) {
                if ( (System.currentTimeMillis()-device.getLastUpdate())>LWM2M_TIMEOUT) { // Disconnection
                    running = false;
                    device.setDead();
                } else {
                    //System.out.println(device.getName() + " alive "+ ((System.currentTimeMillis()-device.getLastUpdate() )/1000));
                }
                
                try {
                    Thread.sleep(1000); // 1 s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        } finally {
            System.out.println(device.getName() + " [dead]");
        }
    }
}
