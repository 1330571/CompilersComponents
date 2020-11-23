package com.chen.FAT;

import com.chen.First.First;
import com.chen.First.GetFirst;
import com.chen.Follow.Follow;
import com.chen.Follow.GetFollow;
import com.chen.production.ALanguage;
import com.chen.production.Language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*预测分析表*/
public class FAT {
    private ALanguage aLanguage;//文法
    private GetFirst getFirst = new GetFirst();//First集
    private GetFollow getFollow = new GetFollow();//Follow集
    private Map<Character, First> firsts = new HashMap<>();
    private Map<Character, Follow> follows = new HashMap<>();
    private Map<Character, Map<Character, Language>> table = new HashMap<>();//预测分析表

    /**
     * @param aLanguage 这里要求输入一个已经解析好的文法
     */
    public FAT(ALanguage aLanguage) {
        this.aLanguage = aLanguage;
        //获取该文法的First和Follow集
        for (First first : getFirst.getFirsts(aLanguage)) {
            firsts.put(first.getSymbol(), first);
        }
        for (Follow follow : getFollow.getFollows(aLanguage, getFirst.getFirsts())) {
            follows.put(follow.getNTerminal(), follow);
        }
    }

    private Language getLanguageByTerminal(List<Language> ntLanguage, Character t) {
        for (Language La : ntLanguage) {
            if (getFirst.getOneLanguageFirst(La).contains(t)) {
                return La;
            }
        }
        return null;
    }

    public void createTable() {
        List<Character> terminals = aLanguage.getTerminal();
        terminals.add('#');
        for (Character nt : aLanguage.getNotTerminal()) {
            Boolean flag = false;
            Map<Character, Language> row = new HashMap<>();
            First first = firsts.get(nt);
            Follow follow = null;
            if (first.IsOfFirst('$')) {
                flag = true;
                follow = follows.get(nt);
            }
            for (Character t : terminals) {
                if (!first.IsOfFirst(t)) {
                    if (flag && follow.IsOfFollow(t)) {
                        char[] cc = {'$'};
                        Language emptyLanguage = new Language(nt,cc);
                        row.put(t, emptyLanguage);
                    } else {
                        row.put(t, null);
                    }
                } else {
                    List<Language> ntLanguage = aLanguage.getLanguage(nt);
                    row.put(t, getLanguageByTerminal(ntLanguage, t));
                }
            }
            table.put(nt, row);
        }
    }

    public String tableToString() {
        List<Character> terminals = aLanguage.getTerminal();
        List<Character> nTerminals = aLanguage.getNotTerminal();
        String s = "";
        for (Character nt : nTerminals) {
            s += (nt + ": ");
            for (Character t : terminals) {
                s += "{" + t + ",";
                Language language = table.get(nt).get(t);
                if (language == null) {
                    s += "err},";
                } else {
                    s += (nt+"->"+Arrays.toString(language.getMatch()) + "},");
                }
            }
            s += "\n";
        }
        return s;
    }
}
