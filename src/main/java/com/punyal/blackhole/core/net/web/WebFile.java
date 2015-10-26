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
public class WebFile {
    private final String originalTarget;
    private String folder;
    private String fileName;
    private String extension;
    
    public WebFile(String target) {
        originalTarget = target;
        folder = target.substring(0, target.lastIndexOf("/")+1);
        int end= target.lastIndexOf(".");
        if (end>0) {
            fileName = target.substring(target.lastIndexOf("/")+1, end);
            extension = target.substring(target.lastIndexOf("."));
        } else {
            fileName = target.substring(target.lastIndexOf("/")+1);
            extension = "";
        }
    }
    
    public String getFolder() {
        return folder;
    }
    
    public void setTarget(String target) {
        folder = target.substring(0, target.lastIndexOf("/")+1);
        int end= target.lastIndexOf(".");
        if (end>0) {
            fileName = target.substring(target.lastIndexOf("/")+1, end);
            extension = target.substring(target.lastIndexOf("."));
        } else {
            fileName = target.substring(target.lastIndexOf("/")+1);
            extension = "";
        }
    }
    
    public String getFileName() {
        return fileName;
    }
    
    
    public String getExtension() {
        return extension;
    }
    
    
    public String getFileRoute() {
        return folder+fileName+extension;
    }
    
    public String getOrigianlTarget() {
        return originalTarget;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------------------").append("\n");
        sb.append("Original: ").append(getOrigianlTarget()).append("\n");
        sb.append("folder: ").append(getFolder()).append("\n");
        sb.append("file: ").append(getFileName()).append("\n");
        sb.append("extension: ").append(getExtension()).append("\n");
        return sb.toString();
    }
}
