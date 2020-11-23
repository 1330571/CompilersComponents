package com.chen.production;

import com.chen.First.First;

import java.util.Arrays;

public class Language {
    private char NTerminal; //非终结符 （只能为大写字母）
    private char match[]; //非终结符的文法推导

    /**
     *
     * @param language
     * 将用户输入的字符串转化为 Language 存储下来
     * A->B+C
     */
    public void S2L(String language) throws Exception {
        //判断是不是一个合法的文法
        if(language.charAt(0)<'A'||language.charAt(0)>'Z'){  //文法的开头不是大写字母 意味着不是非终结字符
            throw new Exception("输入的文法不是以非终结符开头");
        }
        NTerminal = language.charAt(0);
        //跳过 '-'  和 '>' 存储右边的匹配
        match = language.substring(3).toCharArray();
    }

    public Language(char NTerminal, char[] match) {
        this.NTerminal = NTerminal;
        this.match = match;
    }

    public Language() {
    }

    public char getNTerminal() {
        return NTerminal;
    }

    public char[] getMatch() {
        return match;
    }

    @Override
    public String toString() {
        return "Language{" +
                "NTerminal=" + NTerminal +
                ", match=" + Arrays.toString(match) +
                '}';
    }
}
