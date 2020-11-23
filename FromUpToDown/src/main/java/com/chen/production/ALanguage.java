package com.chen.production;

import java.util.ArrayList;
import java.util.List;

public class ALanguage {
    private ArrayList<Character> NotTerminal;  //所有的非终结符
    private ArrayList<Character> Terminal; //所有的终结符
    private List<Language> list;   //终结符和非终结符的文法

    public ALanguage() {
        NotTerminal = new ArrayList<Character>();
        Terminal = new ArrayList<Character>();
        list = new ArrayList<Language>();
    }

    /**
     * @param s 将用户输入的String类型文法进行转化
     */
    public void createALanguage(String s[]) throws Exception {
        //将String类型的输入转化为文法
        for (int i = 0; i < s.length; i++) {
            Language language = new Language();
            language.S2L(s[i]);  //将String类型的文法进行转化
            list.add(language);

            //找到文法中所有的非终结符
            if (NotTerminal.contains(language.getNTerminal()) == false) {
                NotTerminal.add(language.getNTerminal());
            }
            //找寻文法中的所有终结符
            for (int i1 = 0; i1 < language.getMatch().length; i1++) {
                Character c = language.getMatch()[i1];
                if ((c < 'A' || c > 'Z') && Terminal.contains(c) == false) {
                    if (c == '$') {
                        continue;
                    }
                    //扫描文法中的每一个match 如果Terminal中不存在就加入Terminal中
                    Terminal.add(language.getMatch()[i1]);
                }
            }
        }
    }

    public List<Language> getList() {
        return list;
    }

    public ArrayList<Character> getNotTerminal() {
        return NotTerminal;
    }

    public ArrayList<Character> getTerminal() {
        return Terminal;
    }

    /**
     * 从所有文法中，找出所有非终结符nt的文法
     * @param nt 非终结符
     * */
    public List<Language> getLanguage(Character nt) {
        List<Language> ntLanguage = new ArrayList<>();
        Language language = null;
        for (int i = 0; i < list.size(); i++) {
            language = list.get(i);
            if (language.getNTerminal() == nt) {
                ntLanguage.add(language);
            }
        }
        return ntLanguage;
    }
}
