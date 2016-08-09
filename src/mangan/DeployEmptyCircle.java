package mangan;

import java.util.ArrayList;
import java.util.List;

public class DeployEmptyCircle implements DeploymentStrategy {	

	public void deployRobots (int amount, int BaseDist)
	{
		BaseDist = 20;		//Why set to 20 when given as parameter?
		int level = 10;
		
		System.out.println("Robot Number = \t" +amount +"\tDistance = \t" +BaseDist);
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
		
		// DEPLOY CENTER ROBOT IN RANDOM PLACE
		int CenterX = (int)(Math.random() * 1000);
		int CenterY = (int)(Math.random() * 1000);
		int x = CenterX;
		int y = CenterY;

				
		// Deploy the rest of the robots around the center ones
		while(amount > coordinates.size()) {
			
			// (int) always rounds down to calculate the maximum amount of robots to be deployed in the next ring
			int RobNum = (int)(2 * Math.PI * level); 
			System.out.println("RobNum first" +RobNum +amount);
			//Generate Number of robots per circle
			if (RobNum > (amount - coordinates.size())) RobNum = (amount - coordinates.size()); 
			double IncAlpha = 360 / RobNum;
			int Distance = BaseDist * level;
			System.out.println("RobNum second" +RobNum +"\tLevel: \t" +level +"\tIncAlpha: \t" +IncAlpha);
			
			// Deploys robots always completing the circle by distributing robots equally
			for(double alpha=0.0; alpha<360; alpha = alpha + IncAlpha)
			{
				x = CenterX + (int)(Distance * Math.cos(Math.toRadians(alpha)));
				y = CenterY + (int)(Distance * Math.sin(Math.toRadians(alpha)));
				System.out.println("x: \t" +(x - CenterX) +"\ty: \t" +(y - CenterY) +"\talpha: \t" +alpha);
				
				try {
					Simulator.createRobot(x, y);
					coordinates.add(new Coordinates(x, y));
					System.out.println("Info: Deployed robot at x:" + x + " y: " + y + ".");
				} catch (RobotHasNoNeighborException ex) {
					System.out.println("Error: " + ex.getMessage());
				}
			}
			level++;
		}
	}

}
