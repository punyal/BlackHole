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
package com.punyal.blackhole.core.data;

import static com.punyal.blackhole.constants.ConstantsSystem.DATA_BASE_MAX_SIZE;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class EventDataBase {
    private final List<EventData> list;
    private String criticalMessage;
    
    public EventDataBase() {
        list = new ArrayList<>();
        criticalMessage = "";
    }
    
    synchronized public void setCriticalMessage(String message) {
        criticalMessage = message;
    }
    
    synchronized public String getCriticalMessage() {
        return criticalMessage;
    }
    
    synchronized public void addEvent(String data) {
        if (list.size() >= DATA_BASE_MAX_SIZE) list.remove(0);
        list.add(new EventData(System.currentTimeMillis(), data));
    }
    
    public List<EventData> getEventsFrom(long timestamp) {
        List<EventData> filtered = new ArrayList<>();
        for (EventData event: list) {
            if (event.timestamp > timestamp) filtered.add(event);
        }
        return filtered;
    }
    
}
