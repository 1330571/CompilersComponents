package com.chen.FAT;

import com.chen.First.First;
import com.chen.First.GetFirst;
import com.chen.Follow.Follow;
import com.chen.Follow.GetFollow;
import com.chen.production.ALanguage;
import com.chen.production.Language;

import java.util.*;

/*预测分析表*/
public class FAT {
    private ALanguage aLanguage;//文法
    private GetFirst getFirst = new GetFirst();//First集
    private GetFollow getFollow = new GetFollow();//Follow集
    private Map<Character, First> firsts = new HashMap<>();
    private Map<Character, Follow> follows = new HashMap<>();
    private Map<Character, Map<Character, Language>> table = new HashMap<>();//预测分析表
    private boolean isCreateTable = false;

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
        //创建预测分析表
        createTable();
    }

    /**
     *  在给定文法中，找出First集中存在终结符t的文法。该函数用于建表时，找出table[A][*]对应的文法
     *  @param ntLanguage 给定文法
     *  @param t 待寻找的终结符
     * */
    private Language getLanguageByTerminal(List<Language> ntLanguage, Character t) {
        //遍历所有文法，若该文法的First中含有t，则返回该文法
        for (Language La : ntLanguage) {
            if (getFirst.getOneLanguageFirst(La).contains(t)) {
                return La;
            }
        }
        return null;
    }

    /**
    *  创建预测分析表
    * */
    private void createTable() {
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
                        Language emptyLanguage = new Language(nt, cc);
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
        isCreateTable = true;
    }

    /**
     * @param s 待预测的字符串
     */
    public boolean predict(String s) {
        if (!isCreateTable) {
            System.out.println("预测分析表还未构建");
            return false;
        }
        Stack<Character> inputStack = new Stack<>();//输入串
        Stack<Character> symbolStack = new Stack<>();//符号栈

        // 1.构建输入串的栈，栈底是#
        inputStack.push('#');
        for (int i = s.length() - 1; i >= 0; i--) {
            inputStack.push(s.charAt(i));
        }

        // 2.符号栈压入#和开始符号
        symbolStack.push('#');
        symbolStack.push(aLanguage.getNotTerminal().get(0));

        // 3.开始预测分析
        while (!symbolStack.empty()) {
            // 3.1 取符号栈，输入串首字符
            Character symbol = symbolStack.peek();
            Character input = inputStack.peek();
            // 3.2 若两个首字符相同，则表明都为终结符，出栈
            if (symbol == input) {
                symbolStack.pop();
                inputStack.pop();
                continue;
            }
            // 3.3 首字符不相同，根据预测分析表找对应的文法
            Language language = table.get(symbol).get(input);
            // 3.4 若文法为null,表示预测失败
            if (language != null) {
                // 3.5 符号栈顶出栈，把文法倒序入栈，空字符不入栈
                char[] match = language.getMatch();
                symbolStack.pop();
                for (int i = match.length - 1; i >= 0; i--) {
                    char c = match[i];
                    if (c != '$') {
                        symbolStack.push(c);
                    }
                }
            } else {
                return false;
            }
        }
        return true;
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
                    s += (nt + "->" + Arrays.toString(language.getMatch()) + "},");
                }
            }
            s += "\n";
        }
        return s;
    }
}
