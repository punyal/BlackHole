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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Cryptonizer {
    public void Cryptonizer() {}
    
    public static String encryptCoAP(String secretKey, String authenticator, String passWord) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b_secretKey = stringToByteArray(secretKey);
            byte[] b_authenticator = hexStringToByteArray(authenticator);
            byte[] b_password = stringToByteArray(passWord);
            
            if(b_authenticator.length != 16)
            throw new IllegalArgumentException("Authenticator with wrong length: "+b_authenticator.length);
            
            int len = 0;
            int tot_len = 0;
            // Check final length to prevent errors
            if(b_password.length%16 != 0) tot_len = 16;
            tot_len += ((int)b_password.length/16)*16;
            
            // Create crypted array
            byte[] crypted = new byte[tot_len];
            
            byte[] b_temp = new byte[b_secretKey.length+b_authenticator.length];
            byte[] c_temp = new byte[16];
            System.arraycopy(b_secretKey, 0, b_temp, 0, b_secretKey.length);
            System.arraycopy(b_authenticator, 0, b_temp, b_secretKey.length, b_authenticator.length);
            b_temp = md.digest(stringToByteArray(ByteArray2Hex(b_temp)));

            while(len < tot_len) {
                // Copy the 16th bytes to XOR
                if((b_password.length - len) < 16) {
                    System.arraycopy(b_password, len, c_temp, 0, b_password.length-len);
                    for(int i=b_password.length-len; i<16; i ++) 
                        c_temp[i] = 0;
                } else System.arraycopy(b_password, len, c_temp, 0, 16);

                for(int i=0; i<16; i++)
                    c_temp[i] = (byte)(0xFF & ((int)c_temp[i]) ^((int)b_temp[i]));

                System.arraycopy(c_temp, 0, crypted, len, 16);
                len += 16;
            }
            return ByteArray2Hex(crypted);
            
        } catch(NoSuchAlgorithmException ex) {
            System.err.println("No Such Arlgorithm Exception "+ ex);
        }
        return null;
        
    }
    
    public static String ByteArray2Hex(byte[] bytes) {
        if(bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(byte b:bytes)
            sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }
    
    public static String ByteArray2String(byte[] bytes) {
        String string;
        try {
            string = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.err.println("ByteArray2String UnsupportedEncodingException "+ ex);
            string = "";
        }
        return string;
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        for(int i = 0; i < len; i+=2)
            data[i/2] = (byte) ((Character.digit(s.charAt(i),16) << 4) +
                    Character.digit(s.charAt(i+1), 16));
        return data;
    }
    
    public static byte[] stringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];
        data = s.getBytes(Charset.forName("UTF-8"));
        return data;
    }
    
    public static String Timestamp2String(long timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S").format(timestamp);
    }
    
    public static String getCoapTicket(String coap) {
        if(coap.contains("Unknown (100)")){
            String temp = coap.substring(coap.indexOf("Unknown (100)"));
            temp = temp.substring(temp.indexOf("0x")+2);
            if (temp.length() < 3 )
                return null;
            temp = temp.split("}")[0];
            temp = temp.split(" ")[0];
            temp = temp.split(",")[0];
            if (temp.length() > 0 )
                return temp;
        }
        return null;
    }
}
