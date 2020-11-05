package dev.compiler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class GUI extends JFrame {

    public GUI(Node node) {
        this.add(new FAPanel(node));
        this.setTitle("FA GUI INTERFACE");
        this.setSize(888, 888);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}

class FAPanel extends JPanel {
    final Node node;
    final int X = 100;
    final int Y = 700;
    final int L = 20;

    public FAPanel(Node node) {
        super();
        this.node = node;
    }

    private void setBlue(Graphics g) {
        g.setColor(Color.BLUE);
    }

    private void setBlack(Graphics g) {
        g.setColor(Color.BLACK);
    }

    private void setGreen(Graphics g) {
        g.setColor(Color.cyan);
    }

    private void setStart(Graphics g) {
        g.setColor(Color.darkGray);
    }

    private void setCharColor(Graphics g) {
        g.setColor(Color.green);
    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {

        double l1 = Math.abs(x1 - x2);
        double l2 = Math.abs(y1 - y2);
        double l3 = Math.sqrt(l1 * l1 + l2 * l2);
        double theta = Math.asin(l2 / l3);
        if (y1 <= y2) {
            theta += Math.PI / 12;
            g.drawLine(x1, y1, (int) (x1 - L * Math.cos(theta)), (int) (y1 + L * Math.sin(theta)));
            theta -= Math.PI / 6;
            g.drawLine(x1, y1, (int) (x1 - L * Math.cos(theta)), (int) (y1 + L * Math.sin(theta)));
        } else {
            g.drawLine(x1, y1, (int) (x1 - L * Math.cos(Math.PI / 12 - theta)), (int) (y1 + L * Math.sin(Math.PI / 12  - theta)));
            theta += Math.PI / 12;
            g.drawLine(x1, y1, (int) (x1 - L * Math.cos(theta)), (int) (y1 - L * Math.sin(theta)));
        }
    }

    private void drawArrow2(Graphics g, int x1, int y1, int x2, int y2) {
        double l1 = Math.abs(x1 - x2);
        double l2 = Math.abs(y1 - y2);
        double l3 = Math.sqrt(l1 * l1 + l2 * l2);
        double theta = Math.asin(l2 / l3);
        theta -= Math.PI / 12;
        g.drawLine(x2, y2, (int) (x2 + L * Math.cos(theta)), (int) (y2 - L * Math.sin(theta)));

        theta += Math.PI / 6;
        g.drawLine(x2, y2, (int) (x2 + L * Math.cos(theta)), (int) (y2 - L * Math.sin(theta)));
    }

    @Override
    public void paint(Graphics g) {
        int x = X, y = Y;
        super.paint(g);
        HashMap<String, Pos> nodePosMap = new HashMap<>();
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        HashSet<Node> toPaint = new HashSet<>();
        BFSMgr bfsMgr = new BFSMgr(node);
        setBlack(g);
//        g.drawOval(x, y, 50, 50);
//        g.drawChars(node.getName().toCharArray(), 0, node.getName().toCharArray().length, x + 25, y + 25);
//        nodePosMap.put(node.getName(), new Pos(x, y));
//        bfsMgr.getNextLayer();
        int cnt = 0, xCnt = 0;
        while (true) {
            cnt = 0;
//            y = 800 - xCnt * 99;
            y = 800;
            toPaint = bfsMgr.getNextLayer();
            if (toPaint.size() == 0) break;
            for (Node node : toPaint) {
                setBlack(g);
                g.drawChars(node.getName().toCharArray(), 0, node.getName().toCharArray().length, x + 25, y + 25);
                if (node.getState() == 2)
                    g.drawOval(x - 5, y - 5, 60, 60);
                if (node.getState() == 0) {
                    setStart(g);
                }
                g.drawOval(x, y, 50, 50);
                nodePosMap.put(node.getName(), new Pos(x, y));
                nodeArrayList.add(node);
                y -= 60 + (int) (Math.pow(cnt, 1.3)) * 10;
                ++cnt;
            }
            x += 70 + xCnt * 15;
            ++xCnt;
        }
        for (Node _node : nodeArrayList) {
            for (Edge edge : _node.getPrev()) {
                x = nodePosMap.get(_node.getName()).centralX;
                y = nodePosMap.get(_node.getName()).centralY;
                Pos pos = nodePosMap.get(edge.to.getName());
                setBlue(g);
                if (pos == null) continue;
                if (x == pos.centralX && y == pos.centralY) {
//                        g.drawLine(x, y, pos.centralX, pos.centralY);
                    g.drawArc(x - 25, y - 25, 50, 50, 0, 280);
                    char[] arr = edge.getAllTransitions().toCharArray();
                    g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 - 20, (y + pos.centralY) / 2 - 20);
                } else {
                    if (x > pos.centralX) {
                        x += 7;
                        y += 7;
                        g.drawLine(x + 25, y + 25, pos.centralX + 20 + 7, pos.centralY + 25 + 7);
                        drawArrow(g, x + 25, y + 25, pos.centralX + 20 + 7, pos.centralY + 25 + 7);
//                    drawArrow2(g, x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
                        char[] arr = edge.getAllTransitions().toCharArray();
                        setBlack(g);
                        g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 + 23, (y + pos.centralY) / 2 + 23);
                    } else {
                        x -= 7;
                        y -= 7;
                        setGreen(g);
                        g.drawLine(x + 25, y + 25, pos.centralX + 20 - 7, pos.centralY + 25 - 7);
//                        drawArrow(g, x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
                        drawArrow2(g, pos.centralX + 20 - 7, pos.centralY + 25 - 7, x + 25, y + 25);
                        char[] arr = edge.getAllTransitions().toCharArray();
                        setCharColor(g);
                        g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 + 22, (y + pos.centralY) / 2 + 22);
                        setBlack(g);
                    }
                }
            }
        }
    }
}

class BFSMgr {
    Node node;
    HashSet<Node> currentLayer = new HashSet<>();
    HashSet<String> walked = new HashSet<>();
    boolean first = true;

    public HashSet<String> getWalked() {
        return walked;
    }

    public BFSMgr(Node node) {
        this.node = node;
    }

    public HashSet<Node> getNextLayer() {
        if (first) {
            first = false;
            currentLayer.add(node);
            walked.add(node.getName());
            return currentLayer;
        }
        HashSet<Node> tempLayer = new HashSet<>();
        for (Node node : currentLayer) {
            for (Edge edge : node.getNxt()) {
                if (!walked.contains(edge.to.getName())) {
                    tempLayer.add(edge.to);
                    walked.add(edge.to.getName());
                }
            }
        }
        System.out.println(tempLayer);
        currentLayer = tempLayer;
        return currentLayer;
    }
}

class Pos {
    int centralX, centralY;

    public Pos(int centralX, int centralY) {
        this.centralX = centralX;
        this.centralY = centralY;
    }
}
