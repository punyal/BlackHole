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
package com.punyal.blackhole.utils;

import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class Parsers {
    public static JSONObject senml2json(String s) {
        JSONObject json, tmp;
        String data;
        
        json = new JSONObject();
        try {
            tmp = (JSONObject) JSONValue.parse(s);

            // Save base time
            json.put("time", tmp.get("bt"));

            JSONArray slideContent = (JSONArray)tmp.get("e");
            Iterator i = slideContent.iterator();

            while (i.hasNext()) {
                JSONObject slide = (JSONObject) i.next();
                json.put(slide.get("n"), slide.get("v"));
            }
        } catch (NullPointerException e) {}
        return json;
    }
}
