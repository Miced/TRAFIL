/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Drakoulelis
 */
public class TclDesignWirelessNode extends TclDesignNode {

    private Ellipse2D.Double circle;

    public TclDesignWirelessNode(int nodeID) {
	name = "n" + nodeID;
	properties.getNodeNameField().setText(name);
    }

    public Ellipse2D.Double getCircle() {
	return circle;
    }

    public void setCircle(Ellipse2D.Double circle) {
	this.circle = circle;
    }

    public void paintCircle(Graphics2D g2d) {
	g2d.setPaint(Color.BLUE);
	g2d.fill(circle);
	g2d.setPaint(borderColor);
	g2d.draw(circle);
    }
}
