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

import com.punyal.blackhole.core.data.EventDataBase;
import com.punyal.blackhole.core.data.IncomingDataBase;
import com.punyal.blackhole.core.net.EndPoint;
import com.punyal.blackhole.tentacle.Ticket;
import java.net.InetAddress;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mserver {
    private final EndPoint endPoint;
    private final LWM2Mlist devicesList;
    
    public LWM2Mserver(Ticket myTicket, IncomingDataBase incomingDB, EventDataBase eventDB, InetAddress address, int port) {
        endPoint = new EndPoint(address, port);
        devicesList = new LWM2Mlist(myTicket, incomingDB, eventDB);
    }
    
    public LWM2Mserver(Ticket myTicket, IncomingDataBase incomingDB, EventDataBase eventDB, String host, int port) {
        endPoint = new EndPoint(host, port);
        devicesList = new LWM2Mlist(myTicket, incomingDB, eventDB);
    }
    
    public void start() {
        System.out.println("LWM2Mserver: Starting...");
        startSniffer();
    }
    
    private void startSniffer() {
        LWM2Msniffer lwm2mSnifer = new LWM2Msniffer(endPoint, devicesList);
        lwm2mSnifer.start();
    }
    
    public LWM2Mlist getDevices() {
        return devicesList;
    }
    
}
