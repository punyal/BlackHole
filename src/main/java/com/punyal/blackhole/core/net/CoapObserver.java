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
package com.punyal.blackhole.core.net;

import com.punyal.blackhole.tentacle.Cryptonizer;
import com.punyal.blackhole.tentacle.Ticket;
import java.net.Inet6Address;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public abstract class CoapObserver {
    private final EndPoint endPoint;
    private CoapClient coapClient;
    private final CoapHandler coapHandler;
    private CoapObserveRelation relation;
    private final String resource;
    private final Ticket myTicket;
    
    public CoapObserver(Ticket myTicket, EndPoint endPoint, String resource) {
        this.myTicket = myTicket;
        this.endPoint = endPoint;
        this.resource = resource;
        coapHandler = new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                incomingData(response);
            }
            @Override
            public void onError() {
                error();
            }
        };
    }
    
    public void startObserve() {
        String uri;
        if (endPoint.getInetAddress() instanceof Inet6Address)
            uri = "coap://["+endPoint.getAddress()+"]:"+endPoint.getPort()+resource;
        else
            uri = "coap://"+endPoint.getAddress()+":"+endPoint.getPort()+resource;
        coapClient = new CoapClient(uri);
        coapClient.setTimeout(60000);
        //System.out.println("Starting Observe: "+coapClient.getURI());
        System.out.println("Observing with:"+Cryptonizer.ByteArray2Hex(myTicket.getTicket()));
        relation = coapClient.observe(myTicket.getTicket(),coapHandler);
    }
    
    public void stopObserver() {
        //System.out.println("Stoping Observe: "+coapClient.getURI());
        if (relation!=null) relation.reactiveCancel();
    }
    
    abstract public void incomingData(CoapResponse response);
    
    abstract public void error();
}
