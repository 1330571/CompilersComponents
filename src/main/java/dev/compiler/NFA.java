package dev.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NFA {
    private Node start;//起始点
    private Node end;//终结点

    public NFA(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    /**
     * 打印自动机，注意，有闭包不要打印，会死循环
     *
     */
    public void printNFA() {
        Queue<Node> nodes = new LinkedList<>();
        nodes.offer(start);
        while (!nodes.isEmpty()) {
            Node n = nodes.poll();
            ArrayList<Edge> es = n.getNxt();
            for (Edge e:es) {
                nodes.offer(e.to);
            }
            System.out.println(n.toString());
        }
    }
}
