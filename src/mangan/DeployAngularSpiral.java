package mangan;

import java.awt.Point;
import java.util.HashMap;

/**
 * 
 * Deploys an amount of robots following an angular spiral pattern, the robots
 * are equally distributed over the length of the complete spiral
 * 
 */

public class DeployAngularSpiral implements DeploymentStrategy {

	public void deployRobots(int n, int distance) {
		int range = 500;
		HashMap<String, Point> coordinates = new HashMap<String, Point>();
		String key;
		Point center = new Point(range / 2, range / 2);
		Point temp = new Point(); // 0,0 of each subrange
		Point p = new Point();
		char direction = 'n'; // 0= North, 1= West, 2= South, 3=East
		// get longest side of angular spiral
		int l = 0;
		int i = 1;
		while (l < n) {
			l += i;
			if (l < n) {
				l += i;
				if (l < n)
					i++;
			}
		}
		// get size of subrange depending on longest side, +1 to be in range
		int subrange = range / (i + 1);
		// initialize temp point
		temp.x = center.x - (subrange / 2);
		temp.y = center.y - (subrange / 2);
		int j = 0; // total created points
		int level = 1; // length of a side, or number of points per side
		int levelc = 0; // times a level was used
		int c = 0; // created points per level
		// repeat until all points are created
		while (j < n) {
			c = 0;
			// repeat until a side is full and not all points are created
			while (c < level && j < n) {
				c++;
				j++;
				levelc++;
				// Create Point in Subrange
				p.x = (int) (Math.random() * subrange);
				p.y = (int) (Math.random() * subrange);
				p.x += temp.x;
				p.y += temp.y;
				key = String.format("%d/%d", p.x, p.y);
				// As long as the point is already in the coordinates create a
				// new one
				while (coordinates.containsKey(key) || p.x > range || p.x < 0 || p.y > range || p.y < 0) {
					p.x = (int) (Math.random() * subrange);
					p.y = (int) (Math.random() * subrange);
					p.x += temp.x;
					p.y += temp.y;
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
				// set the temp point to next position
				switch (direction) {
				case 'n':
					temp.y += subrange;
					break;
				case 'w':
					temp.x -= subrange;
					break;
				case 's':
					temp.y -= subrange;
					break;
				case 'e':
					temp.x += subrange;
					break;
				default:
					System.out.println("Debug: default");
					break;
				}
			}
			// Change direction if a level is full, set the temp point on the
			// right position, each 2 times increase the level
			if (direction == 'n') {
				direction = 'w';
				temp.x -= (subrange / 2);
				temp.y -= (subrange / 2);
			} else if (direction == 'w') {
				direction = 's';
				temp.x += (subrange / 2);
				temp.y -= (subrange / 2);
			} else if (direction == 's') {
				direction = 'e';
				temp.x += (subrange / 2);
				temp.y += (subrange / 2);
			} else if (direction == 'e') {
				direction = 'n';
				temp.x -= (subrange / 2);
				temp.y += (subrange / 2);

			}
			// if a particular length of a side was 2 times used increase it to
			// get the spiral shape
			if (levelc == 2 * level) {
				level++;
				levelc = 0;
			}
		}
	}
}