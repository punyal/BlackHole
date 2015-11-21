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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mlist {
    private final IncomingDataBase incomingDB;
    private final EventDataBase eventDB;
    private final List<LWM2Mdevice> list;

    public LWM2Mlist(IncomingDataBase incomingDB, EventDataBase eventDB) {
        this.eventDB = eventDB;
        this.incomingDB = incomingDB;
        list = new ArrayList<>();
    }
    
    public int size() {
        return list.size();
    }
    
    public LWM2Mdevice getDeviceByName(String name) {
        LWM2Mdevice device = null;
        for (LWM2Mdevice dev : list)
            if (dev.getName().equals(name)) 
                device = dev;
        return device;
    }
    
    public void addDevice(String name, String id, String host, int port) {
        LWM2Mdevice device = new LWM2Mdevice(eventDB, new EndPoint(host, port), name, id);
        device.setAlive(incomingDB);
        list.add(device);
        
        //System.out.println("New device added: "+name);
    }
    
    public List<LWM2Mdevice> getDevices() {
        return list;
    }
    
    public int getNumberOnlineDevices() {
        int dev = 0;
        for (LWM2Mdevice device: list)
            if (device.isAlive()) dev++;
        return dev;
    }
    
    public int getTotalAlarms() {
        int alarms = 0;
        for (LWM2Mdevice device: list)
            alarms += device.getAlarmsStrain() + device.getAlarmsVibration();
        return alarms;
    }
    
    public int getTotalMessages() {
        int messages = 0;
        for (LWM2Mdevice device: list)
            messages += device.getMessageIn()+ device.getMessageOut();
        return messages;
    }
        
} 
