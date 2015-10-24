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
import com.punyal.blackhole.core.data.IncomingData;
import com.punyal.blackhole.core.data.IncomingDataBase;
import com.punyal.blackhole.core.data.RMSdata;
import com.punyal.blackhole.core.data.RMSdataBase;
import com.punyal.blackhole.core.data.StrainData;
import com.punyal.blackhole.core.data.StrainDataBase;
import com.punyal.blackhole.utils.Parsers;
import org.json.simple.JSONObject;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Analyzer extends Thread {
    private final IncomingDataBase incomingDB;
    private final StrainDataBase strainDB;
    private final RMSdataBase rmsDB;
    
    public Analyzer(IncomingDataBase incomingDB, StrainDataBase strainDB, RMSdataBase rmsDB) {
        this.incomingDB = incomingDB;
        this.strainDB = strainDB;
        this.rmsDB = rmsDB;
        this.setDaemon(true);
    }
    
    public void startThread() {
        start();
    }
    
    @Override
    public void run() {
        IncomingData incomingData;
        JSONObject json;
        try {
            while (true) {
                incomingData = incomingDB.getFirst();
                if (incomingData != null) {
                    switch (incomingData.resource) {
                        case COAP_RESOURCE_STRAIN:
                            //System.out.println(incomingData.response);
                            // Parse data
                            json = Parsers.senml2json(incomingData.response);
                            strainDB.addData(new StrainData(
                                    incomingData.name,
                                    incomingData.timestamp,
                                    Integer.parseInt(json.get("alarm").toString()),
                                    Integer.parseInt(json.get("strain").toString())
                            ));
                            break;
                        case COAP_RESOURCE_RMS:
                            //System.out.println(incomingData.response);
                            // Parse data
                            json = Parsers.senml2json(incomingData.response);
                            rmsDB.addData(new RMSdata(
                                    incomingData.name,
                                    incomingData.timestamp,
                                    Integer.parseInt(json.get("a").toString()),
                                    Float.parseFloat(json.get("X").toString()),
                                    Float.parseFloat(json.get("Y").toString()),
                                    Float.parseFloat(json.get("Z").toString())
                            ));
                            break;
                        default:
                            System.out.println("Unknown data");
                            break;
                    }
                }
                
                try {
                    Thread.sleep(100); // Sleep 100ms
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        } finally {
            System.out.println("Killing Analizer");
        }
    }
    
}
