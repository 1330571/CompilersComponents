package dev.compiler;

import java.io.Reader;
import java.util.Stack;

public class ReConvert {
    private final String ReExpr;
    private int state;

    public ReConvert(String reExpr) {
        ReExpr = reExpr;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < reExpr.length(); ++i) {
            if (reExpr.charAt(i) != ' ')
                str.append(reExpr.charAt(i));
        }
        reExpr = str.toString();
        try {
            if (checkValidity(reExpr)) {
                state = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 咋处理？
        }
    }

    /**
     * 检查条件： 1.不允许出现英文字母、*、|、(、) 之外的其他字符 2.()括号要匹配 3.不允许 || |* ** 这四种情况出现
     *
     * @param str 待检查的正规式
     */
    private boolean checkValidity(String str) throws Exception {

        // 检查是否有非要求字符输入
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!isValidateChar(c)) {
                throw new Exception("输入非法字符");
            }
        }

        // 检查括号匹配、是否有|| ** |* 出现
        int count = 0;// 记录（ 、）数量
        for (int i = 0; i < str.length() - 1; i++) {
            char c1 = str.charAt(i);
            char c2 = str.charAt(i + 1);
            if (c1 == '(') {
                count++;
            } else if (c1 == ')') {
                count--;
            }
            if (c1 == '|' && (c2 == '*' || c2 == '|')) {
                throw new Exception("|后不能有|或*");
            } else if (c1 == '*' && c2 == '*') {
                throw new Exception("*后不能有*");
            }
        }
        if (str.charAt(str.length() - 1) == ')') {
            count--;
        }
        if (count != 0) {
            throw new Exception("括号不匹配");
        } else {
            return true;
        }
    }

    private boolean isValidateChar(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        } else return c == '|' || c == '*' || c == '(' || c == ')';
    }

    /**
     * 正规式转自动机
     */
    public NFA parseToFA() {
        // TODO Check if the String validity like `(()*`
        Stack<Character> sym = new Stack<>();
        Stack<NFA> NFAs = new Stack<>();
        NFA result = null;

        for (int i = 0; i < ReExpr.length(); i++) {
            char c = ReExpr.charAt(i);
            int type = getType(c);
            if (c == '(') {
                sym.push(c);
            } else if (type == 0) {
                result = makeNFA_S(c);
                NFAs.push(result);

                if (NFAs.size() >= 2) {
                    if (sym.size() > 0 && sym.peek() == '|') {  //考虑单个的闭包
                        System.out.println("aaaa"+ReExpr.charAt(i));
                        if(i+1<ReExpr.length()&&ReExpr.charAt(i+1)=='*'){  //考虑到 形如 ab|a* 这种情况，需要我们向后考虑
                            continue;
                        }
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
                    System.out.println("dddddddd"+NFAs.size());
                    if (NFAs.size() >= 2) {
                        System.out.println("5555555555555555555555555555");
                        if (sym.size() > 0 && sym.peek() == '|' && (i+1>=ReExpr.length()||ReExpr.charAt(i+1)!='*')) {  //考虑单个的闭包
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
                } else if (c == '|') {
                    sym.push(c);
                }
            } else if (c == ')') {
                sym.pop();
                if (NFAs.size() >= 2) {
                    if (sym.size() > 0 && sym.peek() == '|') {
                        NFA t1 = NFAs.pop();
                        NFA t2 = NFAs.pop();
                        result = makeNFA_OR(t1, t2);
                        NFAs.push(result);
                    } else if (ReExpr.charAt(i - 1) != '(') {
                        NFA t2 = NFAs.pop();
                        NFA t1 = NFAs.pop();
                        result = makeNFA_AND(t1, t2);
                        NFAs.push(result);
                    }
                }
            }
        }
        assert result != null;
        System.out.println("============"+result.getStart().getName());
        System.out.println("============"+result.getEnd().getName());
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
    private NFA makeNFA_OR(NFA right, NFA left) {
        int flag = 0;
        for (Edge edge : right.getStart().getNxt()) {
            System.out.println("[edge]:" + edge);
            for (char c : edge.getAllTransitions().toCharArray()) {
                if (edge.to.equals(right.getEnd())) {
                    left.getStart().addNextNode(left.getEnd(), edge.id, c); // right只有一条边相连
                    flag = 1;
                } else {
                    left.getStart().addNextNode(edge.to, edge.id, c); // right有多条边相连
                }
            }
        }

        for (Edge edge : right.getEnd().getPrev()) {
            if (flag == 1)
                break;
            for (char c : edge.getAllTransitions().toCharArray()) {
                if (edge.to.equals(right.getStart()))
                    left.getStart().addNextNode(left.getEnd(), edge.id, c);
                else {
                    edge.to.addNextNode(left.getEnd(), edge.id, c);
                    for (Edge edge1 : edge.to.getNxt()) {
                        edge.to.reNxtEdge(edge1);
                    }
                }
            }
        }

        return new NFA(left.getStart(), left.getEnd());
    }

    /**
     * 建立一个自动机，把一个自动机通过闭包关系连接起来
     *
     * @param op 自动机
     */
    private NFA makeNFA_CL(NFA op) {
        Node node = new Node(String.valueOf(++state));// 新建一个点，用做闭包的基点
        node.addNextNode(op.getStart(), 'Ɛ'); // 基点空转移到自动机op的初始节点
        op.getEnd().addNextNode(node, 'Ɛ'); // 自动机op的终止节点空转移到基点
        return new NFA(node, node);
    }

    /**
     * 建立一个自动机，把两个自动机通过与关系连接起来
     *
     * @param left  自动机1
     * @param right 自动机2
     */

    private NFA makeNFA_AND(NFA left, NFA right) {
        for (Edge edge : right.getStart().getNxt()) {
            for (char c : edge.getAllTransitions().toCharArray()) {
                left.getEnd().addNextNode(edge.to, edge.id, c);
            }
        }
        return new NFA(left.getStart(), right.getEnd());
    }
}
