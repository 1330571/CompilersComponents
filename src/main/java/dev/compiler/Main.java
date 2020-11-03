package dev.compiler;

public class Main {
    public static void main(String[] args) {
        //构造一个接受含有01的自动机
        // A --0--> B --1--> C
        // B接受0 返回B，接受1 前往C
        // C接受0 返回C，接受1 返回C
        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        A.addNextNode(B, "0", '0');
        A.addNextNode(A, "0", '1');
        System.out.println(A);
        B.addNextNode(C, "0", '1');
        B.addNextNode(B, "0", '0');

        C.addNextNode(C, "0", '0');
        C.addNextNode(C, "0", '1');

        A.setState(0);
        C.setState(2);

        //Ok to comment
        C.addNextNode(D,"0",'0');
        C.addNextNode(E,"0",'0');
        E.addNextNode(A,"0",'1');
        D.addNextNode(B,"0",'0');
        D.addNextNode(F,'0');
        D.addNextNode(G,'0');
        G.addNextNode(B,'0');
        //
        Matcher matcher = new Matcher(A);
        new GUI(A);
    }
}
