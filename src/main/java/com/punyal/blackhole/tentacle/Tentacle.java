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
package com.punyal.blackhole.tentacle;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Tentacle extends Thread {
    private final Ticket myTicket;
    private final CoapClient coapClient;
    
    public Tentacle() {
        myTicket = new Ticket();
        coapClient = new CoapClient();
        this.setDaemon(true);
    }
    
    public void startThread() {
        start();
    }
    
    public Ticket getMyTicket() {
        return myTicket;
    }
    
    @Override
    public void run() { // Valid Ticket
        CoapResponse response;
        String uri, encoded;
        while (true) {
            if (myTicket.getExpireTime() < System.currentTimeMillis()) {
              // Ask for an authenticator
                uri = "coap://waffle.punyal.com:5682/Authentication";
                coapClient.setURI(uri);
                response = coapClient.get();
                if (response != null) {
                    //System.out.println(response.getResponseText());
                    try {
                        JSONObject json = (JSONObject) JSONValue.parse(response.getResponseText());
                        myTicket.setAuthenticator(json.get("Authenticator").toString());
                    } catch (NullPointerException ex) {
                        myTicket.reset();
                    }
                    
                    // Ask for ticket
                    encoded = "{\"userPass\":\""+Cryptonizer.encryptCoAP("Arrowhead", myTicket.getAuthenticator(), "BlackHole")+"\",\"userName\":\"BlackHole\"}" ;
                    //System.out.println(encoded);
                    
                    response = coapClient.put(encoded, MediaTypeRegistry.APPLICATION_JSON);
                    if (response != null) {
                        //System.out.println(response.getResponseText());
                        try {
                            JSONObject json = (JSONObject) JSONValue.parse(response.getResponseText());
                            myTicket.setTicket(Cryptonizer.hexStringToByteArray(json.get("Ticket").toString()));
                            myTicket.setExpireTime(Long.parseLong(json.get("ExpireTime").toString()));
                            System.out.println("New Ticket: "+Cryptonizer.ByteArray2Hex(myTicket.getTicket()));
                        } catch (NullPointerException ex) {
                            myTicket.reset();
                        }
                        
                    }else {
                        System.out.println("No response");
                    }
                    
                } else {
                    System.out.println("No response");
                }
            }

            try {
                Thread.sleep(1000); // 1 s
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // This should kill it propertly
            }
        }
    }
    
}
