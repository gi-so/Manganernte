package mangan;
import java.util.List;


/**
 * 
 * Implements the full logic of each robot. It only keeps the information that every robot can know: 
 * relative position of other robots, and own speed, direction, etc.
 * Also keeps distance traveled and mangan collected by the robot, as well as max speed.
 * joinDistance and escapeDistance are two variable parameters which empower alg1 and alg2
 * at big and short distances respectively. 
 * As the robots only move after all of them have calculated their new position, we differentiate the 
 * method think (algs calculation) and the method moving (applying future position).
 * All the calculations are done by relative positions of the neighbors, provided by the Simulator.getNeighbors method
 *  
 */
public class Robot {
	/**
	 * Future coordinates after a move is done.
	 */
	Coordinates future = new Coordinates(0.0, 0.0);

	/**
	 * Old coordinates after a move is done.
	 */
	Coordinates past = new Coordinates(0.0, 0.0);
	
	/**
	 * 10 dm/s = 1m/s maximum speed to fit the requirements
	 */
	double speedLimit = 10.0;
	
	/**
	 * Total distance traveled by the robot
	 */
	double totalDistance = 0.0;	
	
	/**
	 * Total mangan collected by the robot
	 */
	double totalMangan = 0.0;	
	
	/**
	 * Alg1 arbitrary distance for empowering joining algorithm
	 */
	int joinDistance = 190; 	
	
	/**
	 * Alg2 arbitrary distance for empowering spreading algorithm
	 */	
	int escapeDistance = 80; 	
	
	/**
	 * To decide the threshold at which speed a move is counted as "moving".
	 */
	double MovThreshold = 9;
	
	/**
	 * Are we moving yet? If so, this is true. Else it's false.
	 */
	boolean Move;
	
	/**
	 * Getter for the total traveled distance.
	 * @return double value of the distance.
	 */
	public double getTotalDistance() {
		return totalDistance;
	}

	/**
	 * Getter for the total amount of collected mangan.
	 * @return double value of the collected mangan.
	 */
	public double getTotalMangan() {
		return totalMangan;
	}

	/**
	 * Getter for the join distance which defines the joininess of algorithm one.
	 * @return the distance of joininess.
	 */
	public int getJoinDistance() {
		return joinDistance;
	}

	/**
	 * Setter for the join distance which defines the joininess of algorithm one.
	 * @param joinDistance
	 */
	public void setJoinDistance(int joinDistance) {
		this.joinDistance = joinDistance;
	}

	/**
	 * Getter for the escape distance which defines the escapiness of algorithm two.
	 * @return the distance of escapiness
	 */
	public int getEscapeDistance() {
		return escapeDistance;
	}

	/**
	 * Setter for the escape distance which defines the escapiness of algorithm two.
	 * @param escapeDistance
	 */
	public void setEscapeDistance(int escapeDistance) {
		this.escapeDistance = escapeDistance;
	}

	/**
	 * Getter for the moving status. Are we still running?
	 * @return true, if we are moving. False if not.
	 */
	public boolean getMove() {
		return Move;
	}
	
	/**
	 * Think gets the relative coordinates of the neighbors and calculates three ways of future movement following PSO 
	 * algorithms of cohesion (alg1), separation (alg2) and alignment (alg3) explained in each of them.
	 * A weight for each algorithm is given by the Simulator.
	 * 
	 * @param neighbors a List<> of all the neighbor robots.
	 * @param weight1 strength of algorithm one.
	 * @param weight2 strength of algorithm two.
	 * @param weight3 strength of algorithm three.
	 * @return the new Coordinate object for the future position.
	 */
	public Coordinates think (List<Coordinates> neighbors, double weight1, double weight2, double weight3){
		
		// Components given for each algorithm
		Coordinates v1 = alg1(neighbors);
		Coordinates v2 = alg2(neighbors);
		Coordinates v3 = alg3(neighbors);
		// Weighting algorithms and to the final x,y components 
		double x = v1.getX()*weight1 + v2.getX()*weight2 + v3.getX()*weight3;
		double y = v1.getY()*weight1 + v2.getY()*weight2 + v3.getY()*weight3;
		future.setX(x);
		future.setY(y);
		
		// Absolute speed limitation to 1 m/s (10 units as we work in decimeters)
		double speed = Math.sqrt (Math.pow(future.getX(),2) + Math.pow(future.getY(), 2));
		if (speed > speedLimit){
			if (future.getX() != 0.0) {
				future.setX( speedLimit*  (future.getX() / speed));
			}
			if (future.getY() != 0.0) {
				future.setY( speedLimit*  (future.getY() / speed));
			}
		}
		speed = Math.sqrt (Math.pow(future.getX(),2) + Math.pow(future.getY(), 2));
		
		// Checking that the robot moves more than a given threshold in order to avoid the robots to get stuck 
		// without representative moving
		if (speed > MovThreshold){ 
			Move = true;
		}
		else { 
			Move = false;		
		}
		// The next movement gets stored in future as it is not going to be applied until Simulator calls move method 
		future.setVelocity(future);
		return future;
	} // end think


	/**
	 * alg1 calculates the 'centre of mass' of all the visible robots near the moving robot.
	 * 
	 * Citation from http://www.kfish.org/boids/pseudocode.html
	 * Assume we have N boids, called b1, b2, ..., bN. Also, the position of a boid b is denoted b.position. Then the 'centre of mass' c of all N boids is given by: 
	 * c = (b1.position + b2.position + ... + bN.position) / N
	 * 
	 * @param neighbors a List<> of Coordinates for all the neighbors.
	 * @return Coordinates as velocity to the 'centre of mass'.
	 */
	private Coordinates alg1 (List<Coordinates> neighbors){
		Coordinates center = new Coordinates(0.0 , 0.0);	

		// Center of mass calculation
		for (int i=0; i < neighbors.size(); i++){
			center.add(neighbors.get(i));	
		}
		center.divide(neighbors.size()); // average

		// Helps the robot move faster if center of mass is far away (empirical arbitrary decision)
		double dist = Math.abs(Math.sqrt(Math.pow(center.getX(), 2) + Math.pow(center.getY(), 2)));		
		double ratio = 1 + Math.pow((dist/joinDistance), 3);
		center.multiply(ratio);
		
		return center;
	}

	/**
	 * alg2 makes the robot keep a minimum distance with close robots.
	 * @param neighbors a List<> of Coordinates for all the neighbors.
	 * @return Coordinates as velocity away from the robots.
	 */
	private Coordinates alg2 (List<Coordinates> neighbors){
		Coordinates v2 = new Coordinates(0.0 , 0.0);

			int closeRobots = 0;
		 	for (int i=0; i < neighbors.size(); i++){
				double dist = Math.abs(Math.sqrt(Math.pow(neighbors.get(i).getX(), 2) + Math.pow(neighbors.get(i).getY(), 2)));
				if (dist < escapeDistance){
					closeRobots++;
					// helps very close robots to escape faster (empirical arbitrary decision)
					v2.sub(neighbors.get(i).multiply(Math.pow( (escapeDistance/dist), 2)));
				}
		}
		v2.divide(closeRobots); // average
		return v2;
	}

	/**
	 * ALG3 makes the robot adapt its speed components to the behaviour of the neighbors.
	 * Uses the velocity data in the Coordinates class to calculate the average as if in ALG1 but in speed instead of position 
	 * @param neighbors
	 * @return the velocity after algorithm three.
	 */
	private Coordinates alg3 (List<Coordinates> neighbors){
		Coordinates v3 = new Coordinates(0.0 , 0.0);
		for (int i=0; i < neighbors.size(); i++){
			v3.addVelocity(neighbors.get(i));
		}
		v3.divide(neighbors.size());
		
		return v3;
	}

	/**
	 * The next movement of the robot is set from future to past, it returns the future to the simulator, and 
	 * keeps track of the total distance traveled by the robot.
	 * @return the future position of the robot.
	 */
	public Coordinates move(){

		past.setX(future.getX());
		past.setY(future.getY());
		
		double distance = Math.abs(Math.sqrt(Math.pow(future.getX(), 2) + Math.pow(future.getY(), 2)));
		totalDistance += distance;

		return future;
	} 

	// 
	/**
	 * Keeps track of the amount of mangan collected by the robot.
	 * @param mangan amount of total mangan.
	 */
	public void addMangan(double mangan){
		totalMangan += mangan;
	}
}
