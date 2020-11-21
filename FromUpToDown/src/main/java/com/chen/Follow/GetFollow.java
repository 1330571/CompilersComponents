package com.chen.Follow;

import com.chen.First.First;
import com.chen.production.ALanguage;
import com.chen.production.Language;

import java.util.ArrayList;
import java.util.List;

public class GetFollow {
    private List<Follow> follows;  //所给文法所有非终结符的follow集 （target）
    private ALanguage aLanguage;   //待求follow集合的文法
    private List<First> firsts;    //所给文法所有字符的first集合

    public GetFollow() {

    }

    /**
     * 根据所给的非终结符找寻他的follow集
     * @param NT 非终结符
     * @return
     */
    private Follow findFollowByNTerminal(Character NT){
        for (Follow follow : follows) {
            if(follow.getNTerminal()==NT){
                return follow;
            }
        }
        return null;
    }

    /**
     * 根据所给的符号找寻他的first集合
     * @param a
     * @return
     */
    private First findFirstBySymbol(Character a){
        for (First first : firsts) {
            if (first.getSymbol()==a){
                return first;
            }
        }
        return null;
    }

    /**
     *
     * @param NT  非终结符
     * @param a   非终结符 新的follow集元素
     * @return  是否添加成功
     *          true  原本follow集中没有 添加成功
     *          falose 原本的follow集已经存在 添加失败
     */
    private boolean InsertFollow(Character NT,Character a){
        Follow follow = findFollowByNTerminal(NT);
        if(follow.IsOfFollow(a)==false){  //非终结符NT的follow集中不包含a 就将a加入到NT的follow集中
            follow.add(a);
            return true;
        }
        return false;
    }

    /**
     *
     * @param aLanguage 文法
     * @param firsts  文法所有符号的first集合
     */
    private void InitFollows(ALanguage aLanguage,List<First> firsts){
        this.aLanguage = aLanguage;
        this.firsts = firsts;
        this.follows = new ArrayList<Follow>();
        //对与文法的每一个非终结符 初始化他们各自的follow集
        for (Character character : aLanguage.getNotTerminal()) {
            Follow follow = new Follow();
            follow.setNTerminal(character);
            follows.add(follow);
        }
    }

    /**
     * 判断所给的符号是不是 非终结符
     * @param a
     * @return
     */
    private boolean IsNTerminal(Character a){
        for (Character character : aLanguage.getNotTerminal()) {
            if (character==a){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param aLanguage  待求follow集的文法
     * @param firsts   该文法所有元素的first集合
     * @return  该文法的follow集
     */
    public List<Follow> getFollows(ALanguage aLanguage, List<First> firsts){
        //初始化文法每一个非终结符的follow集
        InitFollows(aLanguage,firsts);

        //1.将#符号加入到开始符号的follow中
        InsertFollow(aLanguage.getNotTerminal().get(0),'#');

        //2.循环遍历下面的操作直到没有任何新的follow集元素被加到follows集合中
        boolean flag = true;
        while(flag==true){
            flag = false;

            //遍历每一个文法的产生式
            for (Language language : aLanguage.getList()) {

                for(int i = 0; i<language.getMatch().length - 1; i++){
                    Character a = language.getMatch()[i];
                    if(IsNTerminal(a)==true){
                        First firstNext = findFirstBySymbol(language.getMatch()[i + 1]);
                        //是非终结符 将下一个符号的first集元素-'$' 都加到当前非终结符的follow集合中
                        for (Character character : firstNext.getArrayList()) {
                            if(character!='$'){
                                flag = InsertFollow(a,character);
                            }
                        }
                    }
                }

                int j = language.getMatch().length - 1;
                //对每一个产生式从后往前遍历
                for(j = language.getMatch().length - 1 ; j>0;j--){
                    Character a = language.getMatch()[j];
                    if(IsNTerminal(a)==true){
                        Follow followNT = findFollowByNTerminal(language.getNTerminal());
                        //如果产生式最右边的符号是非终结符，就把产生式箭头左边的非终结符的follow集合加到其follow集合中
                        //A->..B  follow(B)=follow(B）+follow(A)
                        for (Character character : followNT.getArrayList()) {
                            InsertFollow(a,character);
                        }
                    }

                    //A->..CB   如果B->$  则继续将follow(A) 加到 follow(C)中
                    if(findFirstBySymbol(a).IsOfFirst('$')){
                        continue;
                    }

                    break;
                }
            }
        }


        return  follows;
    }
}
