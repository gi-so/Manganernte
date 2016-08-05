package mangan;

import processing.core.PApplet;

/**
 * This class represents every robot object visualized through Processing.
 * Every robot has a position (float x, y), a diameter and a parent (which
 * represents the connection to the Processing library).
 * 
 * @version 0.4
 */
public class VisualRobot {
	
	/**
	 * The position of the robot in the GUI.
	 */
	float x, y;
	/**
	 * Parent object as connection to the processing library.
	 */
    PApplet parent;
	/**
	 * The diameter of the viewing range of every robot.
	 */
    int diameter;

	/**
	 * Constructor. Only used to initialize the locally needed variables.
	 * 
	 * @param p is the connection to the Processing library.
	 * @param initX the x position.
	 * @param initY the y position.
	 * @param diameter the diameter of the robots viewing range.
	 */
    VisualRobot(PApplet p, float initX, float initY, int diameter) {
        parent = p;
        this.x = (float)initX;
        this.y = (float)initY;
        this.diameter = diameter;
    }
    
    /**
     * If one calls this, the robot object is drawn to the GUI.
     * The visualization consists of a rectangle (the robot) and an ellipse
     * around the robot, visualizing the viewing range.
     */
    public void display() {
    	// Ellipse is transparent
    	parent.noFill();
    	// Ellipse has a border color
    	parent.stroke(240, 20, 0);
    	// Here the ellipse is drawn here.
    	parent.ellipse(x, y, diameter+20/10, diameter+20/10);
    	// The rectangle isn't transparent.
    	parent.fill(0);
    	// The rectangle has a black border.
    	parent.stroke(0);
    	// The rectangle is as big as one rectangle of the background grid.
    	parent.rect(x, y, 10, 10);
    }

    /**
     * The move-method updates the drawing position of the robot.
     * 
     * @param newX the new x value.
     * @param newY the new y value.
     */
    void move(float newX, float newY) {
    	this.x = newX;
    	this.y = newY;
    }
}