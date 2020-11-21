package com.chen.First;

import java.util.ArrayList;

public class First {
    private char Symbol;  //文法符号
    private ArrayList<Character> arrayList; //文法符号的first集

    public First() {
        arrayList = new ArrayList<Character>();
    }

    public ArrayList<Character> getArrayList() {
        return arrayList;
    }

    public char getSymbol() {
        return Symbol;
    }

    public void setSymbol(char Symbol) {
        this.Symbol = Symbol;
    }

    //往NTerminal的first里面添加新的元素
    public void add(char a){
        arrayList.add(a);
    }

    //判断是否含有某一个字符
    public boolean IsOfFirst(char a){
        return arrayList.contains(a);
    }

    //判断NTerminal的first集是否为空
    public boolean IsEmpty(){
        return arrayList.isEmpty();
    }

    @Override
    public String toString() {
        return "First{" +
                "Symbol=" + Symbol +
                ", arrayList=" + arrayList +
                '}';
    }
}
