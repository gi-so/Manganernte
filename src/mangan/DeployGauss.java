//Debugmode missing

package mangan;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * Deploys an amount of robots in random positions following a Gaussian distribution ,
 * using a predefined Mean and Variance 
 *
 */

public class DeployGauss implements DeploymentStrategy {
	public void deployRobots (int amount, int distance)
	  {
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
	    DeployGauss gaussian = new DeployGauss();
	    
	    double MEAN = 250.0f;
	    double VARIANCE = 80.0f + distance;
	        
	    for (int idx = 1; idx <= amount; ++idx){
	    	double gx = gaussian.getGaussian(MEAN, VARIANCE);
	    	double gy = gaussian.getGaussian(MEAN, VARIANCE);
	    	System.out.println("Generated x: \t" +gx +"\ty: \t" +gy);
	    	int x = (int)gx;
	    	int y = (int)gy;
			try {
				Simulator.createRobot(x, y);
				coordinates.add(new Coordinates(x, y));
				System.out.println("Info: Deployed robot at x:" + x + " y: " + y + ".");
			} catch (RobotHasNoNeighborException ex) {
				idx--;
				System.out.println("Error: " + ex.getMessage());
			}
	    }
	  }
	  private Random fRandom = new Random();
	  public double getGaussian(double aMean, double aVariance){
	    return aMean + fRandom.nextGaussian() * aVariance;
	  }
	} 


