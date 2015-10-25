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
public class ConstantsNet {
    public ConstantsNet(){}
    
    // CoAP
    public static final int COAP_DEFAULT_PORT = 5683;
    public static final String COAP_RESOURCE_STRAIN = "/3999/0/5500";
    public static final String COAP_RESOURCE_RMS = "/acc/rms";
    public static final String COAP_RESOURCE_ROCKBOLT = "/Rockbolt";
    public static final String COAP_RESOURCE_BOLTFAILURE = "/3200/0/5500";
    
    // LWM2M
    public static final String LWM2M_SERVER_IP = "localhost";
    public static final int LWM2M_SERVER_PORT = COAP_DEFAULT_PORT;
    public static final String LWM2M_SERVER_RESOURCE_RD = "/rd";
    public static final int LWM2M_TIMEOUT = 40000; // 35s
    
}
