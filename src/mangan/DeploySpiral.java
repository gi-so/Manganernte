package mangan;

import java.awt.Point;
import java.util.HashMap;

/**
 * 
 * Deploys an amount of robots following a archimedean spiral pattern with 4
 * circles. The length of the spiral is divided by the number of robots. In each
 * step a robot in the subrange between 2 steps is deployed.
 * 
 */

public class DeploySpiral implements DeploymentStrategy {

	public void deployRobots(int n, int distance) {
		int rounds = 4;
		int range = 500;
		HashMap<String, Point> coordinates = new HashMap<String, Point>();
		String key;
		// Calculate Const a based on range
		double a = range / ((rounds * 4 * Math.PI)); // exact a
		a = (range - a * 2) / ((rounds * 2) * 2 * Math.PI); // to be in range
															// subtract a on
															// each side and
															// calculate again
		int subrange = (int) a;
		// Calculate size of steps
		double totaldegree = rounds * 2 * Math.PI;
		double step = totaldegree / n; // Step in rad
		double phi = 0.0;
		Point temp = new Point(0, 0);
		Point p = new Point(0, 0);
		int tempx;
		int tempy;
		int c = 0; // Created points
		while (c < n) {
			// Create random point in subrange
			p.x = (int) (Math.random() * subrange);
			p.y = (int) (Math.random() * subrange);
			// Create Spiral Points
			tempx = (int) (a * phi * Math.cos(phi));
			tempy = (int) (a * phi * Math.sin(phi));
			// Get Point on Spiral and and correct the origin
			p.x += tempx + (range / 2);
			p.y += tempy + (range / 2);
			key = String.format("%d/%d", p.x, p.y);
			// As long as the point is already in the coordinates create a
			// new one
			while (coordinates.containsKey(key)) {
				p.x = (int) (Math.random() * subrange);
				p.y = (int) (Math.random() * subrange);
				p.x += tempx + (range / 2);
				p.y += tempy + (range / 2);
				key = String.format("%d/%d", p.x, p.y);
			}
			// If key is unique append key and point to coordinates
			coordinates.put(key, p);
			// Create Robot
			try {
				Simulator.createRobot(p.x, p.y);
				System.out.println("Info: Deployed robot at x:" + p.x + " y: " + p.y + ".");
			} catch (RobotHasNoNeighborException ex) {
				System.out.println("Error: " + ex.getMessage());
			}
			// Increase angel and created points
			phi += step;
			c++;
		}
	}
}
