package dev.compiler;

public class Matcher {
    final Node node;

    public Matcher(Node node) {
        this.node = node;
    }

    public boolean matchStr(String patternStr) {
        boolean accepted;
        if (accepted=_matchStr(patternStr))
            System.out.println(" ！！！接受！！！");
        else
            System.out.println(" ！！！拒绝！！！");
        return accepted;
    }

    private boolean _matchStr(String patternStr) {
        System.out.println("————————START TO MATCH————————");
        System.out.print("于" + node.getName() + "开始");
        Node currentNode = node;
        for (int i = 0; i < patternStr.length(); ++i) {
            currentNode = currentNode.walkNext(patternStr.charAt(i));
            if (currentNode == null)
                return false;
            System.out.print(" --> " + currentNode.getName());
        }
        return currentNode.getState() == 2; //终结结点
    }
}
