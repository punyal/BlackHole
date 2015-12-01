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
package com.punyal.blackhole.tentacle;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Ticket {
    private byte ticket[];
    private String authenticator;
    private long expireTime;
    
    public Ticket() {
        ticket = null;
        authenticator = null;
        expireTime = 0;
    }
    
    public void reset() {
        ticket = null;
        authenticator = null;
        expireTime = 0;
    }
    
    public void setTicket(byte[] ticket) {
        this.ticket = ticket;
    }
    
    public byte[] getTicket() {
        return ticket;
    }
    
    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }
    
    public String getAuthenticator() {
        return authenticator;
    }
    
    public void setExpireTime(long expireTime) {
        this.expireTime = System.currentTimeMillis()+expireTime;
    }
    
    public long getExpireTime() {
        return expireTime;
    }
    
}
