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
package com.punyal.blackhole.constants;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class ConstantsSystem {
    public ConstantsSystem(){}
    
    // DB
    public static final int DATA_BASE_BUFFER_TIME = 30000; // 30 s
    public static final int DATA_BASE_MAX_SIZE = 20;
    
    // COMMUNICATIONS
    public static final int ALARM_GROUP_TIME = 1000; // 1s
    
    // ALARMS
    public static final String ALARM_MESSAGE_RMS_LEVEL_1="LED:3,400,5";
    public static final String ALARM_MESSAGE_RMS_LEVEL_2="LED:3,100,30";
    public static final String TORCH_MESSAGE_ON="TORCH:1";
    public static final String TORCH_MESSAGE_OFF="TORCH:0";
    
}
