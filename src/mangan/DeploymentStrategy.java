package mangan;

/**
 * Strategy pattern interface for deployment strategies.
 * 
 * @version 0.1
 */
public interface DeploymentStrategy {
	/**
	 * This has to be implemented by every deployment algorithm.
	 * Deploys amount robots with a distance viewing range.
	 * @param amount Amount of robots to deploy.
	 * @param distance Distance, a single robot can see.
	 */
	public void deployRobots(int amount, int distance);
}