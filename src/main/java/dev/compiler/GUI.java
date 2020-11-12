package dev.compiler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public class GUI extends JFrame {

    public GUI(Node node) {
        this.add(new FAPanel(node));
        this.setTitle("FA GUI INTERFACE");
        this.setSize(888, 1288);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}

class FAPanel extends JPanel {
    final Node node;
    private Node highlightNode = null;
    final int fontSize = 24;
    final int X = 50;
    final int Y = 750;
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

    private void setRandomColor(Graphics g) {
        Random random = new Random();
        g.setColor(new Color(random.nextInt(144) + 100, random.nextInt(144) + 90, random.nextInt(100) + 156));
    }

    private void setStart(Graphics g) {
        g.setColor(new Color(255, 100, 20));
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
            g.drawLine(x1, y1, (int) (x1 - L * Math.cos(Math.PI / 12 - theta)), (int) (y1 + L * Math.sin(Math.PI / 12 - theta)));
            theta += Math.PI / 12;
            g.drawLine(x1, y1, (int) (x1 - L * Math.cos(theta)), (int) (y1 - L * Math.sin(theta)));
        }
    }

    public void drawMatchingAnimation() {
        //TODO Code Here
    }

    private void drawArrow2(Graphics g, int x1, int y1, int x2, int y2) {
        double l1 = Math.abs(x1 - x2);
        double l2 = Math.abs(y1 - y2);
        double l3 = Math.sqrt(l1 * l1 + l2 * l2);
        double theta = Math.asin(l2 / l3);
        if (y1 <= y2) {
            theta -= Math.PI / 12;
            g.drawLine(x2, y2, (int) (x2 + L * Math.cos(theta)), (int) (y2 - L * Math.sin(theta)));
            theta += Math.PI / 6;
            g.drawLine(x2, y2, (int) (x2 + L * Math.cos(theta)), (int) (y2 - L * Math.sin(theta)));
        } else {
            g.drawLine(x2, y2, (int) (x2 + L * Math.cos(Math.PI / 12 - theta)), (int) (y2 - L * Math.sin(Math.PI / 12 - theta)));
            theta += Math.PI / 12;
            g.drawLine(x2, y2, (int) (x2 + L * Math.cos(theta)), (int) (y2 + L * Math.sin(theta)));
        }
    }

    private double dis(Pos a, Pos b) {
        return Math.sqrt((a.centralX - b.centralX) * (a.centralX - b.centralX)
                + (a.centralY - b.centralY) * (a.centralY - b.centralY));
    }

    private boolean checkLineCover(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        if ((double) (x2 - x1) / (double) (y2 - y1) - (double) (x4 - x3) / (double) (y4 - y3) < 1e-3) {

        }
        return false;
    }

    private void drawText(Graphics g, ArrayList<Pos> arrayList, char[] arr, int off, int len, int x, int y, Line line) {
        Random random = new Random();
        Pos _pos = new Pos(x, y);
        double innerX = x, innerY = y;
        while (true) {
            boolean notCover = false;
            for (Pos pos : arrayList) {
                if (dis(pos, new Pos((int) innerX, (int) innerY)) < 15.0) {
                    notCover = true;
                    break;
                }
            }
            if (!notCover) break;
            if (line != null) {
                int rnd = random.nextInt(2);
                if (rnd == 0) rnd = 4;
                else rnd = -4;
                innerX += (double) rnd * line.xPer;
                innerY += (double) rnd * line.yPer;
            } else {
                break; //TODO Some strategy ?
            }
        }
        arrayList.add(new Pos((int) innerX, (int) innerY));
//        g.drawChars(arr, off, arr.length, (int) innerX, (int) innerY);
        StringBuilder stringBuilder = new StringBuilder();
        for (char ch : arr) stringBuilder.append(ch);
        g.setFont(new Font(null, Font.PLAIN, fontSize));
        g.drawString(stringBuilder.toString(), (int) innerX, (int) innerY);
    }

    public void animationFrame() {
        //TODO ADD CODE HERE

    }

    private void setHighlightNode(Node node) {
        //TODO ADD CODE HERE
        highlightNode = node;
    }

    @Override
    public void paint(Graphics g) {
        //FIXME Text Size Won't Work
        g.setFont(new Font(null, Font.PLAIN, fontSize));
        int x = X, y = Y;
        super.paint(g);
        HashMap<String, Pos> nodePosMap = new HashMap<>();
        ArrayList<Node> nodeArrayList = new ArrayList<>();
        HashSet<Node> toPaint = new HashSet<>();
        BFSMgr bfsMgr = new BFSMgr(node);

        ArrayList<Pos> lineMediumPoint = new ArrayList<>();

        setBlack(g);
//        g.drawOval(x, y, 50, 50);
//        g.drawChars(node.getName().toCharArray(), 0, node.getName().toCharArray().length, x + 25, y + 25);
//        nodePosMap.put(node.getName(), new Pos(x, y));
//        bfsMgr.getNextLayer();
        int cnt = 0, xCnt = 0;
        while (true) {
            cnt = 0;
//            y = 800 - xCnt * 99;
            y = Y;
            if (xCnt > 13) {
                y = Y / 3;
            }
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
                y -= 175;
                ++cnt;
            }
            x += 85;
            ++xCnt;
            if (xCnt == 14) {
                x = 100;
            }
        }
        //Note FUNCTION HERE
        for (Node _node : nodeArrayList) {
            for (Edge edge : _node.getPrev()) {
                x = nodePosMap.get(_node.getName()).centralX;
                y = nodePosMap.get(_node.getName()).centralY;
//				lineMediumPoint.add(new Pos(x,y));
                Pos pos = nodePosMap.get(edge.to.getName());
//                setBlue(g);
                setRandomColor(g);
                if (pos == null) continue;
                if (x == pos.centralX && y == pos.centralY) {
//                        g.drawLine(x, y, pos.centralX, pos.centralY);
                    int expected_x = x - 25, expected_y = y - 25;

                    g.drawArc(x - 25, y - 25, 50, 50, 0, 268);
                    char[] arr = edge.getAllTransitions().toCharArray();
                    drawText(g, lineMediumPoint, arr, 0, arr.length, (x + pos.centralX) / 2 - 20, (y + pos.centralY) / 2 - 20, null);
                } else {
                    if (x > pos.centralX) {
                        x += 7;
                        y += 7;
                        g.drawLine(x + 25, y + 25, pos.centralX + 20 + 7, pos.centralY + 25 + 7);
//                        g.drawOval(pos.centralX + 20 + 7, pos.centralY + 25 + 7, 5, 5);
                        Line line = new Line(x + 25, y + 25, pos.centralX + 20 + 7, pos.centralY + 25 + 7);
                        drawArrow(g, x + 25, y + 25, pos.centralX + 20 + 7, pos.centralY + 25 + 7);
//                    drawArrow2(g, x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
                        char[] arr = edge.getAllTransitions().toCharArray();
//                        setBlack(g);
//                        g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 + 23, (y + pos.centralY) / 2 + 23);
                        drawText(g, lineMediumPoint, arr, 0, arr.length, (x + pos.centralX) / 2 + 23, (y + pos.centralY) / 2 + 23, line);
                    } else {
                        //FIXME Multiple lines cover each other
                        x -= 7;
                        y -= 7;
                        setRandomColor(g);
                        g.drawLine(x + 25, y + 25, pos.centralX + 20 - 7, pos.centralY + 25 - 7);
                        Line line = new Line(x + 25, y + 25, pos.centralX + 20 - 7, pos.centralY + 25 - 7);
//                        drawArrow(g, x + 25, y + 25, pos.centralX + 20, pos.centralY + 25);
                        drawArrow2(g, pos.centralX + 20 - 7, pos.centralY + 25 - 7, x + 25, y + 25);
                        char[] arr = edge.getAllTransitions().toCharArray();
//                        setCharColor(g);
//                        g.drawChars(arr, 0, arr.length, (x + pos.centralX) / 2 + 22, (y + pos.centralY) / 2 + 22);
                        drawText(g, lineMediumPoint, arr, 0, arr.length, (x + pos.centralX) / 2 + 22, (y + pos.centralY) / 2 + 22, line);
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

class Line {
    int x1, x2, y1, y2;
    double xPer, yPer;

    public Line(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        double l1 = Math.abs(y1 - y2);
        double l2 = Math.abs(x1 - x2);

        double l3 = Math.sqrt(l1 * l1 + l2 * l2);
        double theta = Math.asin(l1 / l3);

        yPer = (y1 < y2 ? 1 : -1) * Math.sin(theta);

        xPer = (x1 < x2 ? 1 : -1) * Math.cos(theta);
        if (x1 == x2) xPer = 0;
        if (y1 == y2) yPer = 0;

        System.out.println(xPer + "   " + yPer);

    }

}