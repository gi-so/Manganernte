package mangan;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  Deploys an amount of robots following a filled squared pattern. The first robot is one of the four central points
 *  of the square, which grows in a spiral shape by adding the following robots by 1,1,2,2,3,3,4,4,... in each direction
 *  (1 right, 1 up, 2 left, 2 down, 3 right, 3 up, 4 left, and so on). 
 *
 */
public class DeploySquare implements DeploymentStrategy {
	
	public void deployRobots(int amount, int Distance) {
		System.out.println("I'm alive!");
		
		long start = System.currentTimeMillis();
		int iterations = 0;
		List<Coordinates> coordinates = new ArrayList<Coordinates>();

		System.out.println("Robot Number = \t" +amount +"\tDistance" +Distance);
		int SideIncrease = 1;
		int Increased = 0;		
		int Direction = 1; // Right = 1; Up = 2; Left = 3; Down = 4;
		
		// First Robot
		//why fixed position?
		int x = 200;
		int y = 200;
		int lastx = 200;
		int lasty = 200;

		try {
			Simulator.createRobot(x, y);
			coordinates.add(new Coordinates(x, y));
			System.out.println("Info: Deployed robot at x:" + x + " y: " + y + ".");
		} catch (RobotHasNoNeighborException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		
		// changes direction every certain robots deployed
		for (int rob=1; rob<amount; rob++)	
		{ 
			switch(Direction) // 
			{
			case 1: 
					x = lastx + Distance;
					y = lasty;
					if (Increased + 1 < SideIncrease) Increased++;
					else 
					{
						Direction = 2;
						Increased = 0;
					}			
				break;
				
			case 2: 
					x = lastx;
					y = lasty + Distance;
					if (Increased + 1 < SideIncrease) Increased++;
					else 
					{
						Direction = 3;
						Increased = 0;
						SideIncrease++;
					}
				break;
				
			case 3: 
				x = lastx - Distance;
				y = lasty;
				if (Increased + 1 < SideIncrease) Increased++;
				else 
				{
					Direction = 4;
					Increased = 0;
				}
			break;
				
			case 4: 
				x = lastx;
				y = lasty- Distance;
				if (Increased + 1 < SideIncrease) Increased++;
				else 
				{
					Direction = 1;
					Increased = 0;
					SideIncrease++;
				}
			break;
				
			} // end switch
			
			try {
				Simulator.createRobot(x, y);
				coordinates.add(new Coordinates(x, y));
				System.out.println("Info: Deployed robot at x:" + x + " y: " + y + ".");
			} catch (RobotHasNoNeighborException ex) {
				System.out.println("Error: " + ex.getMessage());
			}
			lastx = x;
			lasty = y;
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
	
	
	

