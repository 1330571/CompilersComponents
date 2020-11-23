package com.chen.First;

import com.chen.production.ALanguage;
import com.chen.production.Language;

import java.util.ArrayList;
import java.util.List;

public class GetFirst {
    private List<First> firsts;  //某一个文法所有非终结符的first集组成的集合
    private ALanguage aLanguage;  //某一个文法

    public List<First> getFirsts() {
        return firsts;
    }

    public GetFirst() {
        //初始化
        firsts = new ArrayList<First>();
    }

    /**
     * 初始化给定的文法 为文法的每一个符号都构造first集
     *
     * @param aLanguage
     */
    private void InitFirsts(ALanguage aLanguage) {
        //遍历给定文法的每一个非终结符 为其构造一个空的first集
        for (Character character : aLanguage.getNotTerminal()) {
            First first = new First();
            first.setSymbol(character);
            firsts.add(first);
        }
        //为文法的每一个终结符构造first集合
        for (Character character : aLanguage.getTerminal()) {
            First first = new First();
            first.setSymbol(character);
            first.add(character);  //终结符的first集元素就是本身
            firsts.add(first);
        }
        this.aLanguage = aLanguage;
    }

    /**
     * @param NT
     * @return 根据所给的非终结符返回其first集
     */
    public First findFirstByNTerminal(Character NT) {
        for (First first : firsts) {
            if (first.getSymbol() == NT) {
                return first;
            }
        }
        return null;
    }

    /**
     * @param NT 非终结符
     * @param T  非终结符first集的一个元素
     *           <p>
     *           将终结符T添加到非终结符NT的first集中
     */
    private boolean InsertFirst(Character NT, Character T) {
        First first = findFirstByNTerminal(NT);
        if (first.IsOfFirst(T) == false) {
            first.add(T);
            return true;
        }
        return false;
    }

    /**
     * 判断所给的符号a是否是终结符
     *
     * @param a 待判断的符号
     * @return true  是终结符
     * false 是非终结符
     */
    public boolean IsNTerminal(Character a) {
        for (Character character : aLanguage.getNotTerminal()) {//遍历文法的每一个非终结符
            if (character == a)
                return true;
        }
        return false;
    }

    /**
     * @param aLanguage 求得给定文法所有符号的first集
     * @return
     */
    public List<First> getFirsts(ALanguage aLanguage) {
        //初始化得到所有符号的first集
        //非终结符的first集初始化之后是空集
        //终结符的first集初始化之后是本身
        InitFirsts(aLanguage);

        //2.扫描一遍所有的产生式 如果其左边的第一个字符是终结符 直接添加到箭头左边非终结符的first集中
        for (Language language : aLanguage.getList()) {
            Character a = language.getMatch()[0];
            if (IsNTerminal(a) == false) {  //是一个终结符
                InsertFirst(language.getNTerminal(), a);
            }
        }

        //3.重复扫描所有的产生式 直到没有新的终结符被添加到任意非终结符的first集中
        //每次扫描判断将右边第一个（非终结符）的first集添加到箭头左边的非终结符的first集里面
        //如果左边第一个（非终结符）的first集存在空集 将下一个紧挨着的非终结符的first集加到 箭头左边的非终结符的first里面
        boolean flag = true;  //作为结束的判断
        while (flag == true) {
            flag = false;

            for (Language language : aLanguage.getList()) {
                int i = 0;
                for (i = 0; i < language.getMatch().length; i++) {

                    Character B = language.getMatch()[i];
                    if (IsNTerminal(B)) {//A->..B.. first(A)=first(A)+first(B)-$
                        First first = findFirstByNTerminal(B);//找到first(B)
                        //将first(B)的所有元素都给first(A)
                        for (Character character : first.getArrayList()) {
                            if (character != '$') {  //空字符不添加进去
                                flag = InsertFirst(language.getNTerminal(), character);
                            }
                        }

                        //B可以推导出'$' 紧跟在B后面的非终结符C的first集也应该加入到first(A)中
                        if (first.IsOfFirst('$') == true) {
                            continue;
                        }
                    }
                    break;
                }

                //对于A->BCD  BCD都可能推出$  则将$加到first(A)中
                if (i == language.getMatch().length) {
                    flag = InsertFirst(language.getNTerminal(), '$');
                }
            }
        }

        return firsts;
    }

    /**
     *  找出该文法的First集，前提条件是所有文法的First集都求完
     *  @param language 给定文法
     * */
    public List<Character> getOneLanguageFirst(Language language) {
        List<Character> languageFirst = new ArrayList<>();
        for (int i = 0; i < language.getMatch().length; i++) {

            Character B = language.getMatch()[i];
            if (IsNTerminal(B)) {//A->..B.. first(A)=first(A)+first(B)-$
                First first = findFirstByNTerminal(B);//找到first(B)
                //将first(B)的所有元素都给first(A)
                for (Character character : first.getArrayList()) {
                    if (character != '$') {  //空字符不添加进去
                        languageFirst.add(character);
                    }
                }
                //B可以推导出'$' 紧跟在B后面的非终结符C的first集也应该加入到first(A)中
                if (first.IsOfFirst('$') == true) {
                    continue;
                }
            } else {
                languageFirst.add(B);
            }
            break;
        }
        return languageFirst;
    }
}
