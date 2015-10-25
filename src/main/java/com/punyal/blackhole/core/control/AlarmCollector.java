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

import static com.punyal.blackhole.constants.ConstantsSystem.ALARM_GROUP_TIME;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class AlarmCollector {
    private long timestamp;
    private int alarmLevel;
    private List<String> names;
    
    public AlarmCollector() {
        timestamp = 0;
        alarmLevel = 0;
        names = new ArrayList<>();
    }
    
    public void clear() {
        timestamp = 0;
        alarmLevel = 0;
        names = new ArrayList<>();
    }
    
    public void add(String name, int alarmLevel, long timestamp) {
        if (this.timestamp == 0) this.timestamp = timestamp;
        if (alarmLevel > this.alarmLevel) this.alarmLevel = alarmLevel;
        names.add(name);
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public boolean isTimeout() {
        if (timestamp == 0) return false;
        if ((System.currentTimeMillis()-timestamp) > ALARM_GROUP_TIME)
            return true;
        return false;
    }
    
    public int getAlarmLevel() {
        return alarmLevel;
    }
    
    public List<String> getNames() {
        return names;
    }
    
}
