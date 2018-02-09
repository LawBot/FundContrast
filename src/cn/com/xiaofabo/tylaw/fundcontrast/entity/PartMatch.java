/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 陈光曦
 */
public class PartMatch {

    private Map<Integer, Integer> matchList;
    private List<Integer> deleteList;
    private List<Integer> addList;

    public PartMatch() {
        matchList = new HashMap<>();
        deleteList = new LinkedList<>();
        addList = new LinkedList<>();
    }

    public Map<Integer, Integer> getMatchList() {
        return matchList;
    }

    public List getDeleteList() {
        return deleteList;
    }

    public List getAddList() {
        return addList;
    }
    
    public Integer addToMatchList(int a, int b){
        return matchList.put(a, b);
    }
    
    public boolean addToDeleteList(int a){
        return deleteList.add(a);
    }
    
    public boolean addToAddList(int a){
        return addList.add(a);
    }

}
