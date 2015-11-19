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
import com.punyal.blackhole.core.net.lwm2m.LWM2Mlist;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Alarmer extends Thread {
    private final AlarmCollector alarmRMS;
    private final AlarmCollector alarmStrain;
    private final Multicaster multicaster;
    
    public Alarmer(LWM2Mlist devicesList) {
        alarmRMS = new AlarmCollector();
        alarmStrain = new AlarmCollector();
        multicaster = new Multicaster(devicesList);
        setDaemon(true);
    }
    
    public void startThread() {
        start();
    }
    
    public void newAlarm(String resource, String name, int alarmLevel, long timestamp) {
        switch(resource) {
            case COAP_RESOURCE_STRAIN:
                alarmStrain.add(name, alarmLevel, timestamp);
                break;
            case COAP_RESOURCE_RMS:
                alarmRMS.add(name, alarmLevel, timestamp);
                break;
            default:break;
        }
        
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                
                if (alarmRMS.isTimeout()) {
                    System.out.print("RMS alarm level"+alarmRMS.getAlarmLevel()+" except to [");
                    for (String name: alarmRMS.getNames())
                        System.out.print(name+" ");
                    System.out.print("]\n");
                    multicaster.newMulticaster(alarmRMS.getNames(), COAP_RESOURCE_RMS, alarmRMS.getAlarmLevel());
                    alarmRMS.clear();
                }
                
                if (alarmStrain.isTimeout()) {
                    System.out.print("RMS alarm level"+alarmStrain.getAlarmLevel()+" except to [");
                    for (String name: alarmStrain.getNames())
                        System.out.print(name+" ");
                    System.out.print("]\n");
                    multicaster.newMulticaster(alarmStrain.getNames(), COAP_RESOURCE_STRAIN, alarmStrain.getAlarmLevel());
                    alarmStrain.clear();
                }
                
                try {
                    Thread.sleep(100); // Sleep 1s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        } finally {
            System.out.println("Killing Alarm!");
        }
    }
}
