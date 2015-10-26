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
package com.punyal.blackhole.core.net.web;

/**
 *
 * @author Pablo Puñal Pereira <pablo.punal@ltu.se>
 */
public class MIMEtype {
    public static final String CSS_EXTENSION = ".css";
    public static final String CSS_MIME_TYPE = "text/css";
    public static final String HTML_EXTENSION = ".html";
    public static final String HTML_MIME_TYPE = "text/html";
    public static final String JAVA_SCRIPT_EXTENSION = ".js";
    public static final String JAVA_SCRIPT_MIME_TYPE = "application/javascript";
    public static final String JSON_EXTENSION = ".json";
    public static final String JSON_MIME_TYPE = "application/json";
    
    // Default
    public static final String DEFAULT_EXTENSION = HTML_EXTENSION;
    public static final String DEFAULT_MIME_TYPE = HTML_MIME_TYPE;
    
    public static String getMIME( String extension) {
        switch (extension.toLowerCase()) {
            case CSS_EXTENSION: return CSS_MIME_TYPE;
            case HTML_EXTENSION: return HTML_MIME_TYPE;
            case JAVA_SCRIPT_EXTENSION: return JAVA_SCRIPT_MIME_TYPE;
            case JSON_EXTENSION: return JSON_MIME_TYPE;
            default: return DEFAULT_MIME_TYPE;
        }
    }
}
