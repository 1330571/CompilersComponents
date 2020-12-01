package com.chen;

import com.chen.First.First;
import com.chen.First.GetFirst;
import com.chen.production.ALanguage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstTest {
    //最基本的测试类型，A应该能推出a
    @Test
    public void testFirst() {
        String s1 = "A->aB";
        String s2 = "B->aA";
        String[] s = {s1, s2};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        for (First first : firsts) {
            if (first.getSymbol() >= 'A' && first.getSymbol() <= 'Z')
                System.out.println(first.toString());
        }
        assertEquals(firsts.get(0).getArrayList(), new ArrayList<Character>(Collections.singletonList('a')));
        assertEquals(firsts.get(1).getArrayList(), new ArrayList<Character>(Collections.singletonList('a')));
    }

    //此时A应该能够同时推出B和C的first集，所以应该为{$,b,c}
    @Test
    public void testFirst2() {
        String s1 = "A->$";
        String s11 = "A->B";
        String s12 = "A->C";
        String s2 = "B->b";
        String s3 = "C->c";
        String[] s = {s1, s2, s11, s12, s3};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        for (First first : firsts) {
            if (first.getSymbol() >= 'A' && first.getSymbol() <= 'Z')
                System.out.println(first.toString());
        }
        assertEquals(firsts.get(0).getArrayList(), new ArrayList<Character>(Arrays.asList('$', 'b', 'c')));
    }

    //此时考虑的是A所能推出的非终结符同时全部都有概率推出空集，所以所有的first集要考虑都加入进来，但此时C不可以推出
    //空集，所以A只能推出b,c
    @Test
    public void testFirst3() {
        String s1 = "A->BC";
        String s2 = "B->b";
        String s22 = "B->$";
        String s3 = "C->c";
//        String s33 = "C->$";
        String[] s = {s1, s2, s22, s3};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        for (First first : firsts) {
            if (first.getSymbol() >= 'A' && first.getSymbol() <= 'Z')
                System.out.println(first.toString());
        }
        assertEquals(firsts.get(0).getArrayList(), new ArrayList<Character>(Arrays.asList('b', 'c')));
    }

    //此时A->BC，B和C都可以推空，所以相比上一个例子还可以推出空
    @Test
    public void testFirst4() {
        String s1 = "A->BC";
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
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        for (First first : firsts) {
            if (first.getSymbol() >= 'A' && first.getSymbol() <= 'Z')
                System.out.println(first.toString());
        }
        assertEquals(firsts.get(0).getArrayList(), new ArrayList<Character>(Arrays.asList('b', 'c', '$')));
    }

    @Test
    public void testFirst5() {
        String s1 = "A->AbC";
        String s2 = "C->Cc";
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
        for (First first : firsts) {
            if (first.getSymbol() >= 'A' && first.getSymbol() <= 'Z')
                System.out.println(first.toString());
        }
    }

    @Test
    public void testFirst6() {
        String s1 = "A->aB";
        String s2 = "A->aC";
        String s3 = "B->b";
        String s4 = "C->c";
        String[] s = {s1, s2, s3, s4};
        ALanguage aLanguage = new ALanguage();
        try {
            aLanguage.createALanguage(s, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ALanguage.printLanguage(aLanguage);
        GetFirst getFirst = new GetFirst();
        List<First> firsts = getFirst.getFirsts(aLanguage);
        for (First first : firsts) {
            if (first.getSymbol() >= 'A' && first.getSymbol() <= 'Z')
                System.out.println(first.toString());
        }
    }
}

