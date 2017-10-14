package org.toyworld.beans.conf;

import java.util.List;

import java.util.Map;




public class componentbean {
    public String modulerootpath;   
    private List<Map<String,String>> protocolmodules;
    private List<Map<String,String>> handlemodules;
    
    public String getmodulerootpath() {
        return modulerootpath;
    }

    public void setmodulerootpath(String modulerootpath) {
        this.modulerootpath = modulerootpath;
    }
    
    public List<Map<String,String>> getprotocolmodules() {
        return protocolmodules;
    }

    public void setprotocolmodules(List<Map<String,String>> protocolmodules) {
        this.protocolmodules = protocolmodules;
    }
    
    public List<Map<String,String>> gethandlemodules() {
        return handlemodules;
    }

    public void sethandlemodules(List<Map<String,String>> handlemodules) {
        this.handlemodules = handlemodules;
    }
    @Override
    public String toString() {
        return modulerootpath+protocolmodules.toString()+handlemodules.toString();
    }
}
