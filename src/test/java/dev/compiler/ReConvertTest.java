package dev.compiler;

import org.junit.jupiter.api.Test;

class ReConvertTest {

    @Test
    public void test1() {
        String s = "abc";
        ReConvert reConvert = new ReConvert(s);
        NFA nfa = reConvert.parseToFA();
        nfa.printNFA();
    }
    @Test
    public void test2() {
        String s = "(abc)*";
        ReConvert reConvert = new ReConvert(s);
        NFA nfa = reConvert.parseToFA();
        System.out.println("hello");
    }
    @Test
    public void test3() {
        String s = "(a|b)|c";
        ReConvert reConvert = new ReConvert(s);
        NFA nfa = reConvert.parseToFA();
        Matcher matcher = new Matcher(nfa.getStart());
        matcher.matchStr("a");
    }

}