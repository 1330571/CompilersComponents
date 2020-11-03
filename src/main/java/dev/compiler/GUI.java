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
        this.setSize(800, 800);
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
        g.setColor(Color.GREEN);
    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        double l1 = Math.abs(x1 - x2);
        double l2 = Math.abs(y1 - y2);
        double l3 = Math.sqrt(l1 * l1 + l2 * l2);
        double theta = Math.asin(l2 / l3);
        theta += Math.PI / 6;
        g.drawLine(x1, y1, (int) (x1 - L * Math.cos(theta)), (int) (y1 + L * Math.sin(theta)));

        theta -= Math.PI / 3;
        g.drawLine(x1, y1, (int) (x1 - L * Math.cos(theta)), (int) (y1 + L * Math.sin(theta)));
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
        while (true) {
            y = 800 - x;
            toPaint = bfsMgr.getNextLayer();
            if (toPaint.size() == 0) break;
            for (Node node : toPaint) {
                setBlack(g);
                g.drawOval(x, y, 50, 50);
                g.drawChars(node.getName().toCharArray(), 0, node.getName().toCharArray().length, x + 20, y + 20);
                nodePosMap.put(node.getName(), new Pos(x, y));
                nodeArrayList.add(node);
//                for (Edge edge : node.getPrev()) {
//                    Pos pos = nodePosMap.get(edge.to.getName());
//                    setBlue(g);
//                    if (pos == null) continue;
//                    if (x == pos.centralX && y == pos.centralY) {
////                        g.drawLine(x, y, pos.centralX, pos.centralY);
//                        g.drawArc(x - 25, y - 25, 50, 50, 0, 280);
//                        char[] arr = edge.getAllTransitions().toCharArray();
//                        g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 - 20, (y + pos.centralY) / 2 - 20);
//                    } else {
//                        g.drawLine(x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
//                        drawArrow(g, x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
//                        char[] arr = edge.getAllTransitions().toCharArray();
//                        g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 + 15, (y + pos.centralY) / 2 + 15);
//                    }
//                }
                y -= 70;
            }
            x += 70;
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
                    g.drawLine(x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
                    drawArrow(g, x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
                    char[] arr = edge.getAllTransitions().toCharArray();
                    g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 + 15, (y + pos.centralY) / 2 + 15);
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
