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
package com.punyal.blackhole;

import static com.punyal.blackhole.constants.ConstantsNet.*;
import com.punyal.blackhole.core.control.Analyzer;
import com.punyal.blackhole.core.data.IncomingDataBase;
import com.punyal.blackhole.core.data.RMSdataBase;
import com.punyal.blackhole.core.data.StrainDataBase;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mserver;
import com.punyal.blackhole.core.net.web.WebServer;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class BlackHole implements Runnable {
    private final IncomingDataBase incomingDB;
    private final StrainDataBase strainDB;
    private final RMSdataBase rmsDB;
    private final LWM2Mserver lwm2mServer;
    private final Analyzer analyzer;
    private final WebServer webServer;
    
    public BlackHole() {
        incomingDB = new IncomingDataBase();
        strainDB = new StrainDataBase();
        rmsDB = new RMSdataBase();
        lwm2mServer = new LWM2Mserver(incomingDB, LWM2M_SERVER_IP, LWM2M_SERVER_PORT);
        analyzer = new Analyzer(incomingDB, strainDB, rmsDB, lwm2mServer.getDevices());
        analyzer.startThread();
        webServer = new WebServer(lwm2mServer.getDevices());
    }
    
    public void start() {
        System.out.println("BlackHole: Starting...");
        lwm2mServer.start();
        run();
    }

    @Override
    public void run() {
        try {
            while (true) {
                //System.out.println("Devices: "+lwm2mServer.getDevices().size());
                //System.out.println("IncomingDB:"+incomingDB.size()+" StrainDB:"+strainDB.size()+" rmsDB:"+rmsDB.size());
                //rmsDB.printAll();
                try {
                    Thread.sleep(1000); // Sleep 1s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        } finally {
            System.out.println("Sniffer dead!");
        }
    }
    
}
