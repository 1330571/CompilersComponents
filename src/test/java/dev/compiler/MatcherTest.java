package dev.compiler;

import static org.junit.jupiter.api.Assertions.*;

class MatcherTest {

    @org.junit.jupiter.api.Test
    //测试能否匹配正确的字符
    void testMatchStr() {
        //构造一个接受含有01的自动机
        // A --0--> B --1--> C
        // B接受0 返回B，接受1 前往C
        // C接受0 返回C，接受1 返回C
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        A.addNextNode(B,"0",'0');
        A.addNextNode(A,"0",'1');
        System.out.println(A);
        B.addNextNode(C,"0",'1');
        B.addNextNode(B,"0",'0');

        C.addNextNode(C,"0",'0');
        C.addNextNode(C,"0",'1');

        A.setState(0);
        C.setState(2);

        Matcher matcher=new Matcher(A);
        System.out.println("测试111000 输出应当为False");
        assertFalse(matcher.matchStr("111000"));

        System.out.println("测试01111 输出应当为True");
        assertTrue(matcher.matchStr("01111"));

    }
}
