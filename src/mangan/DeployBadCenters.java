package mangan;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Deploys an amount of robots throwing two gaussian distributions randomly spread on the map, 
 * but forcing the center of the gaussian functions to be quite distant, so we can test
 * the behavior of the robots in two irregular groups. Each gaussian has different and random size, 
 * variance, mean, and number of robots.
 * 
 */
public class DeployBadCenters implements DeploymentStrategy {

	/**
	 * Deploy robots in a way where some sort of groups are created to check the usage of such a deployment.
	 * 
	 * @param amount the amount of robots to create
	 * @param distance the distance how far a robot can see
	 */
	public void deployRobots(int amount, int distance)
	    {
	  		List<Coordinates> coordinates = new ArrayList<Coordinates>();
	        DeployGauss gaussian = new DeployGauss();
	        
	        // INITIAL VALUES, MEANS AND VARIANCES
	        double VARIANCEa = (Math.random() * 50) + 30;
	        double VARIANCEb = (Math.random() * 50) + 20;
	        
	        double MEANax = (Math.random() * 75) + 150;
	        double MEANay = (Math.random() * 75) + 150;
	        
	        double MEANbx = (Math.random() * 75) + 250 + distance;
	        double MEANby = (Math.random() * 75) + 250 + distance;
	        
	        double percent = ((Math.random() * 60) + 20)/100;
	        int newamount = (int)(percent*amount);

	        // 1st GAUSSIAN
	        //Generate Coordinates
	        for (int idx = 1; idx <= newamount; ++idx){
	        	double gx = gaussian.getGaussian(MEANax, VARIANCEa);
	        	double gy = gaussian.getGaussian(MEANay, VARIANCEa);
	  	  	if(Simulator.debug()) {
	  	  		System.out.println("Generated x: \t" +gx +"\ty: \t" +gy);
	  	  	}
	        int x = (int)gx;
	        int y = (int)gy;
	  	  	try {
	  	  		//Create Robot at coordinates
	  	  		Simulator.createRobot(x, y);
	  	  		coordinates.add(new Coordinates(x, y));
	  	  		if(Simulator.debug()) {
	  	  			System.out.println("Info: Deployed robot at x:" + x + " y: " + y + ".");
	  	  		}
	  	  	} catch (RobotHasNoNeighborException ex) {
	  	  		if(Simulator.debug()) {
	  	  			System.out.println("Error: " + ex.getMessage());
	  	  		}
	  	  	  }
	        }

	        // 2nd GAUSSIAN
	        //Generate Coordinates
	        for (int idx = 1; idx <= (amount-newamount); ++idx){
	        	double gx = gaussian.getGaussian(MEANbx, VARIANCEb);
	        	double gy = gaussian.getGaussian(MEANby, VARIANCEb);
	  	  	if(Simulator.debug()) {
	  	  		System.out.println("Generated x: \t" +gx +"\ty: \t" +gy);
	  	  	}
	        int x = (int)gx;
	        int y = (int)gy;
	  	  	try {
	  	  		//Create Robot at Coordinates
	  	  		Simulator.createRobot(x, y);
	  	  		coordinates.add(new Coordinates(x, y));
	  	  		if(Simulator.debug()) {
	  	  			System.out.println("Info: Deployed robot at x:" + x + " y: " + y + ".");
	  	  		}
	  	  	} catch (RobotHasNoNeighborException ex) {
	  	  		if(Simulator.debug()) {
	  	  			System.out.println("Error: " + ex.getMessage());
	  	  		}
	  	  	}
        }    
	}
} 