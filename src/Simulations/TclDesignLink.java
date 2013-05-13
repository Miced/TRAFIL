/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 *
 * @author Drakoulelis
 */
public class TclDesignLink {

    private Color color;
    private Line2D.Double line;
    private TclDesignWiredNode startingNode;
    private TclDesignWiredNode endingNode;

    public TclDesignLink() {
        this.color = new Color(0, 128, 0);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Line2D.Double getLine() {
        return line;
    }

    public void setLine(Line2D.Double line) {
        this.line = line;
    }

    public TclDesignWiredNode getStartingNode() {
        return startingNode;
    }

    public void setStartingNode(TclDesignWiredNode startingNode) {
        this.startingNode = startingNode;
    }

    public TclDesignWiredNode getEndingNode() {
        return endingNode;
    }

    public void setEndingNode(TclDesignWiredNode endingNode) {
        this.endingNode = endingNode;
    }

    public void paintLink(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));	// Make the line thicker
        g2d.draw(line);
    }
}
