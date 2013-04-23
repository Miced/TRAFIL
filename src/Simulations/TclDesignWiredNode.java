/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Drakoulelis
 */
public class TclDesignWiredNode extends TclDesignNode {

    private Rectangle2D.Double rectangle;

    public TclDesignWiredNode(int nodeID) {
	name = "n" + nodeID;
	properties.getNodeNameField().setText(name);
    }

    public void setRectangle(Rectangle2D.Double rectangle) {
	this.rectangle = rectangle;
    }

    public Rectangle2D.Double getRectangle() {
	return rectangle;
    }

    public void paintSquare(Graphics2D g2d) {
	g2d.setPaint(Color.RED);
	g2d.fill(rectangle);
	g2d.setPaint(borderColor);
	g2d.draw(rectangle);
    }
}
