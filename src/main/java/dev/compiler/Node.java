package dev.compiler;

import java.util.ArrayList;


public class Node {
    ArrayList<Edge> nxt = null;
    ArrayList<Edge> prev = null;
    private int state; //0 开始 1 普通 2终结

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    private String name = "No Name"; //结点名字 输出信息用

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Node(ArrayList<Edge> nxt, ArrayList<Edge> prev) {
        this.nxt = nxt;
        this.prev = prev;
    }

    public ArrayList<Edge> getNxt() {
        return nxt;
    }

    public ArrayList<Edge> getPrev() {
        return prev;
    }

    public Node(String name) {
        this.name = name;
        nxt = new ArrayList<>();
        prev = new ArrayList<>();
    }

    public void addNextEdge(Edge edge) {
        nxt.add(edge);
    }

    public void addPrevEdge(Edge edge) {
        prev.add(edge);
    }

    /**
     * 添加一个结点，自动管理多个转移函数的情况
     *
     * @param node       下一个点
     * @param id         边id
     * @param transition 转移函数
     */
    public void addNextNode(Node node, String id, char transition) {
        node.addPrevNode(this, id, transition);
        for (Edge edge : nxt) {
            if (edge.to == node) {
                edge.addTransition(transition);
                return;
            }
        }
        nxt.add(new Edge(node, id, transition));
    }

    private void addPrevNode(Node node, String id, char transition) {
        for (Edge edge : prev) {
            if (edge.to == node) {
                edge.addTransition(transition);
                return;
            }
        }
        prev.add(new Edge(node, id, transition));
    }

    @Override
    public String toString() {
        return "Node{" +
                "nxt=" + nxt +
                ", prev=" + prev +
                '}';
    }

    /**
     * 往下走
     *
     * @param transition 转移字符
     * @return 走到哪
     */
    public Node walkNext(char transition) {
        for (Edge edge : nxt) {
            Node node = edge.consumeChar(transition);
            if (node != null)
                return node;
        }
        return null;
    }
}
