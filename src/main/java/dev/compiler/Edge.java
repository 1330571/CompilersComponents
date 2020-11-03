package dev.compiler;

import java.util.ArrayList;
import java.util.Random;


public class Edge {
    final Node to;
    final String id;
    final String name;
    ArrayList<Character> transitions = new ArrayList<>();

    /**
     * 构造
     * @param to 前往哪个节点
     * @param id 标志这个节点的ID
     */
    public Edge(Node to, String id) {
        this.to = to;
        this.id = id;
        name = Integer.toString(new Random().nextInt(66666));
    }

    public Edge(Node to, String id, char transition) {
        this.to = to;
        this.id = id;
        name = Integer.toString(new Random().nextInt(66666));
        transitions.add(transition);
    }

    public Edge(Node to, String id, String name) {
        this.to = to;
        this.id = id;
        this.name = name;
    }

    /**
     * 添加转移函数
     *
     * @param transition 转移字符
     */
    public void addTransition(char transition) {
        transitions.add(transition);
    }

    /**
     * 尝试转移
     *
     * @param feedChar 喂入的字符
     * @return 走到的结点，null代表哪里都去不了
     */
    public Node consumeChar(char feedChar) {
        for (char ch : transitions) {
            if (feedChar == ch) {
                return to;
            }
        }
        return null; //没有匹配 dead state
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("前往" + to.getName() + '\n');
        for (char ch : transitions)
            stringBuilder.append(" " + ch + " ");
        return stringBuilder.toString();
    }

    public String getAllTransitions(){
        StringBuilder stringBuilder = new StringBuilder();
        for(char ch:transitions) {
            stringBuilder.append(ch);
            stringBuilder.append('|');
        }
        return stringBuilder.substring(0,stringBuilder.length() - 1).toString();
    }
}

