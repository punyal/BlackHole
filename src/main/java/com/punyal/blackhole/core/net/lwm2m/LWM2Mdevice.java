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
import static com.punyal.blackhole.constants.ConstantsSystem.DATA_BASE_MAX_SIZE;
import com.punyal.blackhole.core.data.EventDataBase;
import com.punyal.blackhole.core.data.IncomingData;
import com.punyal.blackhole.core.data.IncomingDataBase;
import com.punyal.blackhole.core.data.RMSdata;
import com.punyal.blackhole.core.data.StrainData;
import com.punyal.blackhole.core.net.EndPoint;
import com.punyal.blackhole.core.net.rockbolt.RockboltClient;
import com.punyal.blackhole.core.net.web.Torch;
import com.punyal.blackhole.tentacle.Ticket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class LWM2Mdevice {
    private final Ticket myTicket;
    private final EventDataBase eventDB;
    private final EndPoint endPoint;
    private final String name;
    private String id;
    private long lastUpdate;
    private boolean connected;
    private RockboltClient rockboltClient;
    private IncomingDataBase incomingDB;
    // int alarmTotal;
    //private int messagesTotal;
    private int battery;
    private int messagesIn;
    private int messagesOut;
    private int alarmsVibration;
    private int alarmsStrain;
    
    // microDB
    private final List<StrainData> strainData;
    private final List<RMSdata> rmsData;
    
    
    
    public LWM2Mdevice(Ticket myTicket, EventDataBase eventDB, EndPoint endPoint, String name, String id) {
        this.myTicket = myTicket;
        this.eventDB = eventDB;
        this.endPoint = endPoint;
        this.name = name;
        this.id = id;
        lastUpdate = System.currentTimeMillis();
        connected = false;
        incomingDB = null;
        //alarmTotal = 0;
        //messagesTotal = 0;
        battery = 0;
        messagesIn = 0;
        messagesOut = 0;
        alarmsVibration = 0;
        alarmsStrain = 0;
        
        strainData = new ArrayList<>();
        rmsData = new ArrayList<>();
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
    
    public boolean isAlive() {
        return connected;
    }
    
    public void setAlive(IncomingDataBase incomingDB) {
        if (!connected) {
            connected = true;
            eventDB.addEvent(name+" [Online]");
            //run here a new thread!!
            if ( (System.currentTimeMillis()-lastUpdate)>LWM2M_TIMEOUT ) {
                System.out.println(name+" [resurrected]");
            } else {
                System.out.println(name+" [alive]");
            }
            rockboltClient = new RockboltClient(myTicket, this);
            rockboltClient.startThread();
            this.incomingDB = incomingDB;
        }
        lastUpdate = System.currentTimeMillis();
    }
    
    public void setDead() {
        eventDB.addEvent(name+" [Offline]");
        connected = false;
        rockboltClient.stopThread();
    }
    
    public void incomingData(String resource, String response) {
        if (isAlive()) lastUpdate = System.currentTimeMillis();
        if (incomingDB != null) {
            incomingDB.addData(new IncomingData(name, resource, response));
        }
    }
    
    public void torch(boolean mode) {
        Torch torchThread = new Torch(myTicket, this, mode);
        torchThread.startThread();
    }
    
    /*public void increaseAlarmTotal(){
        alarmTotal++;
    }
    
    public int getAlarmTotal() {
        return alarmTotal;
    }
    
    public void increaseMessagesTotal(){
        messagesTotal++;
    }
    
    public int getMessagesTotal() {
        return messagesTotal;
    }
    */
    
    public int getBatteryLevel() {
        return battery;
    }
    
    public void setBatteryLevel(int battery) {
        this.battery = battery;
    }
    
    public int getMessageIn() {
        return messagesIn;
    }
    
    public void increaseMessageIn() {
        messagesIn++;
    }
    public int getMessageOut() {
        return messagesOut;
    }
    
    public void increaseMessageOut() {
        messagesOut++;
    }
    
    public int getAlarmsVibration() {
        return alarmsVibration;
    }
    
    public void increaseAlarmsVibration() {
        alarmsVibration++;
    }
    
    public int getAlarmsStrain() {
        return alarmsStrain;
    }
    
    public void increaseAlarmsStrain() {
        alarmsStrain++;
    }
    
    synchronized public void addVibrationData(float x, float y, float z) {
        if (!isAlive()) return;
        if (rmsData.size() >= DATA_BASE_MAX_SIZE) rmsData.remove(0);
        rmsData.add(new RMSdata(null, System.currentTimeMillis(), 0, x, y, z));
    }
    
    synchronized public void addStrainData(int strain) {
        if (!isAlive()) return;
        if (strainData.size() >= DATA_BASE_MAX_SIZE) strainData.remove(0);
        strainData.add(new StrainData(null, System.currentTimeMillis(), 0, strain));
    }
    
    public List<RMSdata> getVibrationData() {
        return rmsData;
    }
    
    public List<StrainData> getStrainData() {
        return strainData;
    }
}
