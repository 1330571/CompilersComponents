package dev.compiler;

import java.util.Stack;

public class ReConvert {
    private String ReExpr;
    private int state;

    public ReConvert(String reExpr) {
        ReExpr = reExpr;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < reExpr.length(); ++i) {
            if (reExpr.charAt(i) != ' ') str.append(reExpr.charAt(i));
        }
        reExpr = str.toString();
        state = 0;
    }

    /**
     * 正规式转自动机
     */
    public NFA parseToFA() {
        //TODO Check if the String validity like `(()*`
        Stack<Character> sym = new Stack<>();
        Stack<NFA> NFAs = new Stack<>();
        NFA result = null;

        for (int i = 0; i < ReExpr.length(); i++) {
            Character c = ReExpr.charAt(i);
            int type = getType(c);
            if (c == '(') {
                sym.push(c);
            } else if (type == 0) {
                result = makeNFA_S(c);
                NFAs.push(result);
                if (NFAs.size() >= 2) {
                    if (sym.size() > 0 && sym.peek() == '|') {
                        NFA t1 = NFAs.pop();
                        NFA t2 = NFAs.pop();
                        result = makeNFA_OR(t1, t2);
                        NFAs.push(result);
                    } else if (i > 0 && ReExpr.charAt(i - 1) != '(') {
                        NFA t2 = NFAs.pop();
                        NFA t1 = NFAs.pop();
                        result = makeNFA_AND(t1, t2);
                        NFAs.push(result);
                    }
                }

            } else if (type == 1) {
                if (c == '*') {
                    NFA t = NFAs.pop();
                    result = makeNFA_CL(t);
                    NFAs.push(result);
                } else if (c == '|') {
                    sym.push(c);
                }
            } else if (c == ')') {
                sym.pop();
                if (NFAs.size() >= 2) {
                    NFA t1 = NFAs.pop();
                    NFA t2 = NFAs.pop();
                    result = makeNFA_AND(t1, t2);
                    NFAs.push(result);
                }
            }
        }
        result.getStart().setState(0);
        result.getEnd().setState(2);
        return result;
    }


    /**
     * 判断字符类型，0 表示操作数， 1 表示操作符， 2 表示左右括号
     *
     * @param c 待检查的字符
     */
    private int getType(char c) {
        if (c == '*' || c == '|') {
            return 1;
        } else if (c == '(' || c == ')') {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * 建立一个自动机，状态1通过a进入状态2
     *
     * @param a 转换字符
     */
    private NFA makeNFA_S(char a) {
        Node n1 = new Node(String.valueOf(++state));
        Node n2 = new Node(String.valueOf(++state));
        n1.addNextNode(n2, a);
        return new NFA(n1, n2);
    }

    /**
     * 建立一个自动机，把两个自动机通过或关系连接起来
     *
     * @param left  自动机1
     * @param right 自动机2
     */
    private NFA makeNFA_OR(NFA left, NFA right) {
        Node pre = new Node(String.valueOf(++state));
        Node after = new Node(String.valueOf(++state));
        pre.addNextNode(left.getStart(), '$');
        pre.addNextNode(right.getStart(), '$');
        left.getEnd().addNextNode(after, '$');
        right.getEnd().addNextNode(after, '$');
        return new NFA(pre, after);
    }

    /**
     * 建立一个自动机，把一个自动机通过闭包关系连接起来
     *
     * @param op 自动机
     */
    private NFA makeNFA_CL(NFA op) {
        Node pre = new Node(String.valueOf(++state));
        Node after = new Node(String.valueOf(++state));
        pre.addNextNode(op.getStart(), '$');
        op.getEnd().addNextNode(after, '$');
        op.getEnd().addNextNode(op.getStart(), '$');
        pre.addNextNode(after, '$');
        return new NFA(pre, after);
    }

    /**
     * 建立一个自动机，把两个自动机通过与关系连接起来
     *
     * @param left  自动机1
     * @param right 自动机2
     */
    private NFA makeNFA_AND(NFA left, NFA right) {
        left.getEnd().addNextNode(right.getStart(), '$');
        return new NFA(left.getStart(), right.getEnd());
    }
}
