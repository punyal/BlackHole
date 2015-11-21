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

import com.punyal.blackhole.core.net.lwm2m.LWM2Mdevice;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mlist;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Multicaster {
    private final LWM2Mlist devicesList;
    
    public Multicaster(LWM2Mlist devicesList) {
        this.devicesList = devicesList;
    }
    
    public void newMulticaster(List<String> exceptDevices, String resource, int alarmLevel) {
        boolean send;
        Caster caster;
        List<LWM2Mdevice> toSendList =new ArrayList<>();
        
        for (LWM2Mdevice device: devicesList.getDevices()) {
            send = true;
            for (String except: exceptDevices) {
                if (except.equals(device.getName()))
                    send = false;
            }
            if (send) toSendList.add(device);
        }
        
        for (LWM2Mdevice device: toSendList) {
            //System.out.println(device.getName());
            caster = new Caster(device, resource, alarmLevel);
            caster.startThread();
        }
    }
}
