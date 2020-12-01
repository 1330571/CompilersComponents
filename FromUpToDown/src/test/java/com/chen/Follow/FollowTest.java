package com.chen.Follow;

import com.chen.First.First;
import com.chen.First.GetFirst;
import com.chen.production.ALanguage;
import org.junit.jupiter.api.Test;

import java.util.List;

class FollowTest {
    @Test
    public void testFollow() {
        String s1 = "A->bAa";
        String s2 = "B->dBc";
        String[] s = {s1, s2};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALanguage.printLanguage(aLanguage);
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        GetFollow getFollow = new GetFollow();
        List<Follow> follows = getFollow.getFollows(aLanguage, firsts);
        for (Follow follow : follows) {
            System.out.println(follow.toString());
        }
    }

    @Test
    public void testFollow2() {
        String s1 = "A->aBC";
        String s2 = "B->b";
        String s22 = "B->$";
        String s3 = "C->c";
        String s33 = "C->$";
        String[] s = {s1, s2, s22, s3, s33};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALanguage.printLanguage(aLanguage);
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        GetFollow getFollow = new GetFollow();
        List<Follow> follows = getFollow.getFollows(aLanguage, firsts);
        for (Follow follow : follows) {
            System.out.println(follow.toString());
        }
    }


    @Test
    public void testFollow3() {
        String s1 = "A->BCD";
        String s2 = "B->a";
        String s22 = "C->c|$";
        String s33 = "D->d|$";
        String[] s = {s1, s2, s22, s33};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALanguage.printLanguage(aLanguage);
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        GetFollow getFollow = new GetFollow();
        List<Follow> follows = getFollow.getFollows(aLanguage, firsts);
        for (Follow follow : follows) {
            System.out.println(follow.toString());
        }
    }
}