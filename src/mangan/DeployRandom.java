package mangan;

import java.util.ArrayList;
import java.util.List;

/**
 * Deploys an amount of robots in random positions within the given sea limits.
 */
public class DeployRandom implements DeploymentStrategy {
	public void deployRobots(int amount, int distance) {
		System.out.println("I'm alive!");
		
		// Measure some stuff
		long start = System.currentTimeMillis();
		int iterations = 0;
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
		
		while(amount > coordinates.size()) {
			int x = (int)(Math.random() * 500);
			int y = (int)(Math.random() * 500);
			
			try {
				Simulator.createRobot(x, y);
				coordinates.add(new Coordinates(x, y));
			} catch (RobotHasNoNeighborException ex) {
				System.out.println("Error: " + ex.getMessage());
			} finally {
				iterations += 1;
			}
		}
		
		// Save the elapsed time
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		float elapsedTimeSec = elapsedTimeMillis/1000F;

		// Print some stats
		System.out.println("Bots placed: " + coordinates.size());
		System.out.println("Runtime in seconds: " + elapsedTimeSec);
		System.out.println("Iterations: " + iterations);
		System.out.print("Coordinates: ");
		for(int i = 0; i < coordinates.size(); i++) {
			Coordinates temp = coordinates.get(i);
			System.out.print("(" + temp.getX() + ", "  + temp.getY() + ") ");
		}
		System.out.print("\n");
	}
}
