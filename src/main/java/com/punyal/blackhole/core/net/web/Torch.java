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

import static com.punyal.blackhole.constants.ConstantsNet.COAP_RESOURCE_ROCKBOLT;
import static com.punyal.blackhole.constants.ConstantsSystem.*;
import com.punyal.blackhole.core.net.EndPoint;
import java.net.Inet6Address;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Torch extends Thread {
    private final EndPoint endPoint;
    private final boolean mode;
    private CoapClient coapClient;
    
    public Torch(EndPoint endPoint, boolean mode) {
        this.endPoint = endPoint;
        this.mode = mode;
        setDaemon(true);
    }
    
    public void startThread() {
        start();
    }
    
    @Override
    public void run() {
        try {
            String uri;
            if (endPoint.getInetAddress() instanceof Inet6Address)
                uri = "coap://["+endPoint.getAddress()+"]:"+endPoint.getPort()+COAP_RESOURCE_ROCKBOLT;
            else
                uri = "coap://"+endPoint.getAddress()+":"+endPoint.getPort()+COAP_RESOURCE_ROCKBOLT;
            coapClient = new CoapClient(uri);
            coapClient.useNONs();
            //System.out.println(coapClient.getURI());
            
            String torchMessage;
            if (mode) torchMessage = TORCH_MESSAGE_ON;
            else torchMessage = TORCH_MESSAGE_OFF;
            //System.out.println("MESSAGE: "+torchMessage);

            coapClient.post(torchMessage, MediaTypeRegistry.TEXT_PLAIN);
            
        } finally {
            //System.out.println("Killing Torch...");
        }
    }
}
