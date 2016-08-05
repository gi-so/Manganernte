package mangan;

/**
 * Strategy pattern pseudo class.
 * @version 0.1
 */
public class DeploymentContext {
	/**
	 * Variable to hold the real strategy for deployment.
	 */
	private DeploymentStrategy strategy;
	
	/**
	 * Constructor needs a DeploymentStrategy.
	 */
	public DeploymentContext(DeploymentStrategy strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * Deploy this amount of robots with a viewing range of distance.
	 * 
	 * Every class must implement this.
	 */
	public void deployRobots(int amount, int distance) {
		this.strategy.deployRobots(amount, distance);
	}
}
