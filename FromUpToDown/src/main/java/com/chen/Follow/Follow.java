package com.chen.Follow;

import java.util.ArrayList;

public class Follow {
    private char NTerminal;  //非终结符
    private ArrayList<Character> arrayList; //非终结符的follow集

    public Follow() {
        arrayList = new ArrayList<Character>();
    }

    public ArrayList<Character> getArrayList() {
        return arrayList;
    }

    public char getNTerminal() {
        return NTerminal;
    }

    public void setNTerminal(char NTerminal) {
        this.NTerminal = NTerminal;
    }

    //往NTerminal的follow里面添加新的元素
    public void add(char a){
        arrayList.add(a);
    }

    //判断是否含有某一个字符
    public boolean IsOfFollow(char a){
        return arrayList.contains(a);
    }

    //判断NTerminal的follow集是否为空
    public boolean IsEmpty(){
        return arrayList.isEmpty();
    }

    @Override
    public String toString() {
        return "Follow{" +
                "NTerminal=" + NTerminal +
                ", arrayList=" + arrayList +
                '}';
    }

}
