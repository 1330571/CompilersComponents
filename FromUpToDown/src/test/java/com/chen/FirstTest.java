package com.chen;

import com.chen.FAT.FAT;
import com.chen.First.First;
import com.chen.First.GetFirst;
import com.chen.Follow.Follow;
import com.chen.Follow.GetFollow;
import com.chen.production.ALanguage;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FirstTest {

    //测试样例是书上的例子
    @Test
    public void test(){
        String s1 = "A->ABC";
        String s2 = "A->c";
        String s3= "C->+BC";
        String s4= "C->$";
        String s5 = "B->DE";
        String s6 = "E->E*DE";
        String s7 = "E->$";
        String s8 = "D->(A)";
        String s9 = "D->i";
        String[] s = {s1,s2,s3,s4,s5,s6,s7,s8,s9};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //First集
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        System.out.println("*******************************************");
        for (First first : firsts) {
            System.out.println(first.toString());
        }

        System.out.println("*******************************************");

        //Follow集
        GetFollow getFollow = new GetFollow();
        List<Follow> follows = getFollow.getFollows(aLanguage, firsts);
        for (Follow follow : follows) {
            System.out.println(follow.toString());
        }

        /*FAT fat = new FAT(aLanguage);
        System.out.println(fat.predict("i*i+i"));*/
        //System.out.println(fat.tableToString());
    }

}
