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

import static com.punyal.blackhole.constants.ConstantsNet.LWM2M_TIMEOUT;
import com.punyal.blackhole.core.data.IncomingData;
import com.punyal.blackhole.core.data.IncomingDataBase;
import com.punyal.blackhole.core.net.EndPoint;
import com.punyal.blackhole.core.net.rockbolt.RockboltClient;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mdevice {
    private final EndPoint endPoint;
    private final String name;
    private String id;
    private long lastUpdate;
    private boolean connected;
    private RockboltClient rockboltClient;
    private IncomingDataBase incomingDB;
    
    public LWM2Mdevice(EndPoint endPoint, String name, String id) {
        this.endPoint = endPoint;
        this.name = name;
        this.id = id;
        lastUpdate = System.currentTimeMillis();
        connected = false;
        incomingDB = null;
    }
    
    public EndPoint getEndPoint() {
        return endPoint;
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public long getLastUpdate() {
        return lastUpdate;
    }
    
    public void updateID(String id) {
        this.id = id;
        setAlive(incomingDB);
    }
    
    public void setAlive(IncomingDataBase incomingDB) {
        if (!connected) {
            connected = true;
            //run here a new thread!!
            if ( (System.currentTimeMillis()-lastUpdate)>LWM2M_TIMEOUT ) {
                System.out.println(name+" [resurrected]");
            } else {
                System.out.println(name+" [alive]");
            }
            rockboltClient = new RockboltClient(this);
            rockboltClient.startThread();
            this.incomingDB = incomingDB;
        }
        lastUpdate = System.currentTimeMillis();
    }
    
    public void setDead() {
        connected = false;
        rockboltClient.stopThread();
    }
    
    public void incomingData(String resource, String response) {
        if (incomingDB != null) {
            incomingDB.addData(new IncomingData(name, resource, response));
        }
    }

}
