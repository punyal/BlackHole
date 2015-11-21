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
package com.punyal.blackhole.core.control;

import static com.punyal.blackhole.constants.ConstantsNet.*;
import static com.punyal.blackhole.constants.ConstantsSystem.*;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mdevice;
import java.net.Inet6Address;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Caster extends Thread {
    private final LWM2Mdevice device;
    private final String resource;
    private final int alarmLevel;
    
    public Caster(LWM2Mdevice device, String resource, int alarmLevel) {
        this.device = device;
        this.resource = resource;
        this.alarmLevel = alarmLevel;
    }
    
    public void startThread() {
        start();
    }
    
    @Override
    public void run() {
        try {
            device.increaseMessageOut();
            String uri;
            
            if (device.getEndPoint().getInetAddress() instanceof Inet6Address)
                uri = "coap://["+device.getEndPoint().getAddress()+"]:"+device.getEndPoint().getPort()+COAP_RESOURCE_ROCKBOLT;
            else
                uri = "coap://"+device.getEndPoint().getAddress()+":"+device.getEndPoint().getPort()+COAP_RESOURCE_ROCKBOLT;
            CoapClient coapClient = new CoapClient(uri);
            coapClient.useCONs();
            //System.out.println(uri);
            String alarmMessage="";
            switch (alarmLevel) {
                case 2:
                    if (resource.equals(COAP_RESOURCE_RMS)) alarmMessage = ALARM_MESSAGE_RMS_LEVEL_2;
                    if (resource.equals(COAP_RESOURCE_STRAIN)) alarmMessage = ALARM_MESSAGE_STRAIN_LEVEL_2;
                    break;
                default:
                    if (resource.equals(COAP_RESOURCE_RMS)) alarmMessage = ALARM_MESSAGE_RMS_LEVEL_1;
                    if (resource.equals(COAP_RESOURCE_STRAIN)) alarmMessage = ALARM_MESSAGE_STRAIN_LEVEL_1;
                    break;
            }
            coapClient.post(alarmMessage, MediaTypeRegistry.TEXT_PLAIN);
            //System.out.println("MESSAGE: "+alarmMessage);
            //System.out.println(coapClient.toString());
            
        } finally {
            //System.out.println("Killing Caster...");
        }
    }
    
}
