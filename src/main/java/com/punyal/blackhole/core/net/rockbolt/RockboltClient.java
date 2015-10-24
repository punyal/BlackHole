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
package com.punyal.blackhole.core.net.rockbolt;

import static com.punyal.blackhole.constants.ConstantsNet.LWM2M_TIMEOUT;
import com.punyal.blackhole.core.net.lwm2m.LWM2Mdevice;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class RockboltClient extends Thread {
    private final  LWM2Mdevice device;
    private boolean running;
    
    
    public RockboltClient(LWM2Mdevice device) {
        this.device = device;
        this.setDaemon(true);
        running = true;
    }
    
    public void startThread() {
        this.start();
    }
    
    public void stopThread() {
        running = false;
    }
    
    @Override
    public void run() {
        try {
            while (running) {
                if ( (System.currentTimeMillis()-device.getLastUpdate())>LWM2M_TIMEOUT) { // Disconnection
                    running = false;
                    device.setDead();
                } else {
                    //System.out.println(device.getName() + " alive "+ ((System.currentTimeMillis()-device.getLastUpdate() )/1000));
                }
                
                try {
                    Thread.sleep(1000); // 1 s
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // This should kill it propertly
                }
            }
        } finally {
            System.out.println(device.getName() + " [dead]");
        }
    }
}
