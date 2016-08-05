package mangan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * Main class coordinating the evolution of the steps taken by the robots while
 * interacting with them in order to provide the data they are allowed to know
 * by the requirements, in the way they are supposed to. A HashMaps keeps all
 * the info from every robot (what it can know) and another one the coordinates
 * and speed (which it can't know) and manages this data to give it to the
 * robots in the appropiate way. The weights to each moving algorithm can be
 * previously modified and are given to the robots before deployment (as in real
 * life) It tracks the mangan in the whole sea to know how many is collected by
 * each robot (and in total) Countdown is the maximum of iterations (steps,
 * seconds) the robots can be moving around until they change to gathering mode.
 * InitialAttempts is the number iterations any robot is moving faster than a
 * threshold (so they are all stuck) until they automatically switch to
 * gathering mode.
 *
 */
public class Simulator {
	/**
	 * Simulator is a singleton.
	 */
	private static Simulator instance = new Simulator();

	/**
	 * Deployment algorithm is choosen by a strategy pattern.
	 */
	private static DeploymentContext DEPLOYALGORITHM = new DeploymentContext(new DeployRandom());

	/**
	 * String to save the name of the Deployment Strategy to save it to file
	 */
	private static String DeployAlg;

	/**
	 * Hashmaps store Robot objects.
	 */
	private static HashMap<Integer, Robot> robots = new HashMap<Integer, Robot>();

	/**
	 * Hashmaps store Coordinates data (position & speed).
	 */
	private static HashMap<Integer, Coordinates> coordinates = new HashMap<Integer, Coordinates>();

	/**
	 * Temporary HashMap as copy for saving of deployment coordinates.
	 */
	private static HashMap<Integer, Coordinates> saveCoordinates;

	/**
	 * Temporary HashMap as copy for FindBestParameters().
	 */
	private static HashMap<Integer, Robot> tmpRobots;

	/**
	 * Temporary HashMap as copy for FindBestParameters() and saving coordinates
	 * to file.
	 */
	private static HashMap<Integer, Coordinates> tmpCoordinates;

	/**
	 * List to save an compare the results.
	 */
	private static ArrayList<Result> Results = new ArrayList<Result>();

	/**
	 * Mangan ground as 2d array.
	 */
	private static short[][] Mangan = new short[5000][5000]; // WE WILL WORK IN
																// DECIMETERS

	/**
	 * Total collected mangan.
	 */
	private static double TotalMangan = 0;

	/**
	 * Total distance traveled.
	 */
	private static double TotalDistance = 0;

	/**
	 * Radius (or viewing range) of every robot.
	 */
	private static final int RADIUS = 200;

	/**
	 * Save the viewing range of the robots for saving stats to file
	 */
	private static int distance;

	/**
	 * Iterations as a marker for "real-time seconds". One iteration == one
	 * second.
	 */
	private static int Iterations = 0; // Iterations run so far (real-time
										// seconds)

	/**
	 * This gets true if the robots haven't moved in a long time.
	 */
	private static boolean TheyMove; // Controls if they are stuck to activate
										// gathering

	/**
	 * In FindBestParameters indicates no more iterations for that config should
	 * be done
	 */
	private static boolean ChangeWeights;

	/**
	 * If true, robots start to gather.
	 */
	private static boolean GatheringMode = false;

	/**
	 * If true, simulation is done.
	 */
	private static boolean FarDistance = true;

	/**
	 * Attempts (or real-time seconds) till we think that the robots aren't
	 * moving anymore.
	 */
	private static int InitialAttempts = 60;

	/**
	 * The initial weight for algorithm 1.
	 */
	private static double Initialweight1 = 0.8;

	/**
	 * The initial weight for algorithm 2.
	 */
	private static double Initialweight2 = 0.3;

	/**
	 * The initial weight for algorithm 3.
	 */
	private static double Initialweight3 = 1;

	/**
	 * The initial countdown till we change weights
	 */
	private static int Initialcountdown = 5000;

	/**
	 * Assigning the initial weight to the really used weights of algorithm 1.
	 */
	private static double weight1 = Initialweight1;

	/**
	 * Assigning the initial weight to the really used weights of algorithm 2.
	 */
	private static double weight2 = Initialweight2;

	/**
	 * Assigning the initial weight to the really used weights of algorithm 3.
	 */
	private static double weight3 = Initialweight3;

	/**
	 * Assigning the initial countdown to the really used countdown.
	 */
	private static int Countdown = Initialcountdown;

	/**
	 * Assigning the initial attempts to the really used attempts.
	 */
	private static int Attempts = InitialAttempts;

	/**
	 * boolean to enable debug output. true = DEBUG ON; false = DEBUG OFF;
	 */
	private static boolean debug = true;

	/**
	 * Placeholder for the filename to save the stats.
	 */
	private static String fileNameDate;

	/**
	 * Name of the folder where the results should be saved to
	 */
	private static String folderName;

	/**
	 * Returns the current debug state. true = on, false = off. Will enable
	 * debug output at different places!
	 * 
	 * @return boolean which represents the state of debug.
	 */
	public static boolean debug() {
		return debug;
	}

	/**
	 * Clear all robots, the mangan array and all variables, which we need for a
	 * clean run.
	 */
	public static void clearRobots() {

		System.out.println("Total distance travelled by ALL robots " + TotalDistance);
		System.out.println("Total mangan collected by ALL robots: " + TotalMangan);

		robots.clear();
		coordinates.clear();
		Mangan = new short[5000][5000]; // WE WILL WORK IN DECIMETERS
		TotalMangan = 0;
		TotalDistance = 0;
		System.out.println("Cleared all robots!");

		weight1 = Initialweight1;
		weight2 = Initialweight2;
		weight3 = Initialweight3;
		Countdown = Initialcountdown;
		Attempts = InitialAttempts;
		Iterations = 0;
		GatheringMode = false;
		FarDistance = true;
	}

	/**
	 * Private constructor because of the singleton.
	 */
	private Simulator() {
		generateFolder();
	}

	/**
	 * Getter for the singleton instance.
	 */
	public static Simulator getInstance() {
		return instance;
	}

	/**
	 * For a given input, loop over all the possible configurations and
	 * iterations before automatic gathering when all robots are stuck to find
	 * the best parameter values to collect the maximum amount of mangan.
	 */
	public static void FindBestParameters() {

		System.out.println("FINDING BEST PARAMETERS");

		tmpRobots = new HashMap<Integer, Robot>(robots);
		tmpCoordinates = new HashMap<Integer, Coordinates>(coordinates);

		double Bestw1 = 0;
		double Bestw2 = 0;
		double Bestw3 = 0;
		int Bestit = 0;
		double MostMangan = 0;
		double MostManganDist = 0;

		// for (double w1 = 0; w1 <= 3; w1 = w1 + 0.02){
		for (double w1 = 0.1; w1 <= 2; w1 = w1 + 0.1) {

			// for (double w2 = 0; w2 <= 3; w1 = w1 + 0.2){
			for (double w2 = 0; w2 <= 2; w2 = w2 + 0.1) {

				// for (double w3 = 0; w2 <= 10; w1 = w1 + 0.5){
				for (double w3 = 0; w2 <= 2; w3 = w3 + 1) {

					ChangeWeights = false;
					int it = 1; // it will start with one iteration before
								// gathering, then increase one by one
					System.out.println("w1: " + w1 + " w2: " + w2 + " w3: " + w3 + " it: " + it);

					while (ChangeWeights == false) { // switched to true when
														// automatic gathering
														// mode is activated by
														// inactivity
						// Countdown = it;

						// Full simulation from deployment until the end of the
						// gathering process
						// for (int i = 1; i <= Countdown; i++){
						// Simulator.move();
						// }
						// Simulator.gather();

						// They will gather until all of the robots are very
						// close to each other
						while (FarDistance) {
							Simulator.move();
						}

						// Checking if the results were better than previous
						// simulations
						if (TotalMangan > MostMangan) {
							Bestw1 = w1;
							Bestw2 = w2;
							Bestw3 = w3;
							Bestit = it;
							MostMangan = TotalMangan;
							MostManganDist = TotalDistance;
							System.out.println("New Best Mangan: " + MostMangan);
						}
						Simulator.clearRobots();

						// Insert old values to simulator.
						robots.clear();
						robots = new HashMap<Integer, Robot>(tmpRobots);
						coordinates.clear();
						coordinates = new HashMap<Integer, Coordinates>(tmpCoordinates);
						it++;

					} // end while over iterations
				} // end w3
			} // end w2
		} // end w1

		weight1 = Bestw1;
		weight2 = Bestw2;
		weight3 = Bestw3;
		Iterations = Bestit;
	} // end Find Best Parameteres

	/**
	 * Full process to calculate and execute the movement of all robots in a
	 * step, iteration, or real-life second
	 */
	static public void move() {
		// we assume no robot moves until the opposite is proved
		TheyMove = false;
		Iterations++;
		// we assume all robots are close (stop) until the opposite is proved
		FarDistance = false;
		// All the robots receive their neighbours information and just
		// calculate the future movement with the given weights
		for (int i = 1; i <= coordinates.size(); i++) { //
			List<Coordinates> RobotList = new ArrayList<Coordinates>();
			RobotList = getNeighbors(coordinates.get(i), i);
			coordinates.get(i).setVelocity(robots.get(i).think(RobotList, weight1, weight2, weight3));
			// one single robot moving over the threshold is enough
			if (robots.get(i).getMove())
				TheyMove = true;
		}

		// If any robot moved enough we decrease the countdown to gathering
		// until it is activated.
		if (TheyMove == false) {
			if (Simulator.debug()) {
				System.out.println("THEY DIDN'T MOVE: " + Attempts);
			}
			Attempts--;
			if (Attempts == 0) {
				ChangeWeights = true;
				Simulator.gather();
				System.out.println("==== Gathering robots due to inactivity ====");
				// Simulation is over now, save stats
				Simulator.generateFile();
				Simulator.printResultsTXT(Results.size());
				Simulator.printResultsCSV(Results.size());
			}
		}

		// Actually moving the robots so application of the future values
		for (int i = 1; i <= coordinates.size(); i++) {
			Coordinates movement = robots.get(i).move();
			coordinates.get(i).add(movement);

			// Tracking of the total distance traveled by all robots
			double distance = Math.sqrt(Math.pow(movement.getX(), 2) + Math.pow(movement.getY(), 2));
			if (distance > 0) {
				distance = distance / 10;
			}
			TotalDistance += distance;

			// Calculation of the mangan collected by each robot and tracking of
			// the total
			double mangan = Simulator.getMangan(coordinates.get(i));
			robots.get(i).addMangan(mangan);
			TotalMangan += mangan;
		}

		// General Countdown until the gathering mode activation
		Countdown--;
		if (Countdown == 0) {
			Simulator.gather();
		}
	} // End of one iteration of the moving process

	/**
	 * Calculates the mangan around the new position of the robot and collects
	 * it.
	 * 
	 * @param rob
	 *            coordinates of the robot to collect mangan for.
	 * @return the amount of mangan.
	 */
	static public double getMangan(Coordinates rob) {
		double mangan = 0;
		int x = (int) rob.getX();
		int y = (int) rob.getY();

		// Positions occupied by a robot using decimiters (robot is central
		// point)
		int ileftLimit = 5;
		int irightLimit = 5;
		int jupLimit = 5;
		int jdownLimit = 5;

		// Checking that the robot is not in the margins of the ocean/area so it
		// couldn't collect as much mangan
		if (x < ileftLimit)
			ileftLimit = x;
		else if (x > 5000 - irightLimit)
			irightLimit = 5000 - irightLimit;
		if (y < jupLimit)
			jupLimit = y;
		else if (y > 5000 - jdownLimit)
			jdownLimit = 5000 - jdownLimit;

		// For all the 1x1 dcm squares around the 1x1m of the robot, we pick the
		// mangan in a circular shape
		for (int i = x - ileftLimit; i <= (x + irightLimit); i++) {
			for (int j = y - jupLimit; j <= (y + jdownLimit); j++) {
				double dist = Math.pow((x - i), 2) + Math.pow((y - j), 2);
				if (dist < 25) { // Pump radius
					if (Mangan[i][j] != 1) {
						Mangan[i][j] = 1;
						mangan++;
					}
				}
			}
		}
		if (mangan > 0) {
			mangan = mangan / 100;
		}
		return mangan;
	}

	/**
	 * Given one robot, checks which robots are within the RADIUS distance and
	 * keeps the list of coordinates neighbors, with the relative positions of
	 * those respect the robot checked, so the information is able to be sent to
	 * the robot as if it was its own radar.
	 * 
	 * @param rob
	 *            the robots coordinates we want the neighbors for.
	 * @param key
	 *            key of the robot as index value to find the correct one.
	 * @return a list with coordinates of all the neighbors.
	 */
	static public List<Coordinates> getNeighbors(Coordinates rob, int key) {
		// We need to know the coordinates of the current Robot since he cannot
		// tell us
		List<Coordinates> neighbors = new ArrayList<Coordinates>();

		double distX;
		double distY;
		double dist;

		for (int j = 1; j <= coordinates.size(); j++) {

			if (key != j) { // to avoid checking for the robot itself

				distX = (coordinates.get(j).getX() - rob.getX());
				distY = (coordinates.get(j).getY() - rob.getY());
				dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

				// If the robot is close enough to be detected
				if (dist <= RADIUS) {
					Coordinates newNeighbor = new Coordinates(0.0, 0.0);
					newNeighbor.setX(distX);
					newNeighbor.setY(distY);
					neighbors.add(newNeighbor);
					if (GatheringMode) {
						if (dist > 20)
							FarDistance = true;
					}
				}
			}
		}
		return neighbors;
	}

	/**
	 * Set the deployment algorithm by changing the DeploymentContext at runtime
	 * thanks to strategy pattern.
	 * 
	 * @param choice
	 *            an integer to reflect the corresponding deployment algorithm.
	 */
	public static void setDeployAlgorithm(int choice) {
		// TODO: Use reflection for this kind of stuff
		// http://code.google.com/p/reflections/
		switch (choice) {
		case 0:
			DEPLOYALGORITHM = new DeploymentContext(new DeployRandom());
			System.out.println("Deployalgorithm: Random");
			DeployAlg = "random";
			break;
		case 1:
			DEPLOYALGORITHM = new DeploymentContext(new DeploySquare());
			System.out.println("Deployalgorithm: Square");
			DeployAlg = "square";
			break;
		case 2:
			DEPLOYALGORITHM = new DeploymentContext(new DeployCircle());
			System.out.println("Deployalgorithm: Circle");
			DeployAlg = "circle";
			break;
		case 3:
			DEPLOYALGORITHM = new DeploymentContext(new DeployGauss());
			System.out.println("Deployalgorithm: Gauss");
			DeployAlg = "gauss";
			break;
		case 4:
			DEPLOYALGORITHM = new DeploymentContext(new DeployBadCenters());
			System.out.println("Deployalgorithm: Bad Centers");
			DeployAlg = "badcenters";
			break;
		case 5:
			DEPLOYALGORITHM = new DeploymentContext(new DeploySpiral());
			System.out.println("Deployalgorithm: Spiral");
			DeployAlg = "spiral";
			break;
		case 6:
			DEPLOYALGORITHM = new DeploymentContext(new DeployAngularSpiral());
			System.out.println("Deployalgorithm: Angular Spiral");
			DeployAlg = "angularspiral";
			break;
		case 7:
			System.out.println("Deployalgorithm: Manual");
			DeployAlg = "manual";
			break;
		default:
			DEPLOYALGORITHM = new DeploymentContext(new DeployRandom());
			DeployAlg = "random";
			break;
		}
	}

	/**
	 * Create a robot at x, y.
	 * 
	 * @param x
	 *            is the x coordinate of the robot.
	 * @param y
	 *            is the y coordinate of the robot.
	 * @throws RobotHasNoNeighborException
	 *             in case the robot has no neighbors.
	 */
	public static void createRobot(int x, int y) throws RobotHasNoNeighborException {
		Simulator.neighborDiscovery(x, y);
		coordinates.put(coordinates.size() + 1, new Coordinates(x, y));
		robots.put(robots.size() + 1, new Robot());
	}

	/**
	 * Get amount of existing robots.
	 * 
	 * @return integer as amount.
	 */
	public static int getRobotCount() {
		return robots.size();
	}

	/**
	 * Get FarDistance to check if the simulation is over.
	 * 
	 * @return FarDistance as indicator, if the simulation is over.
	 */
	public static boolean getFarDistance() {
		return FarDistance;
	}

	/**
	 * Get GatheringMode to check if the robots are gathering already.
	 * 
	 * @return GatheringMode as indicator, if the robots are gathering.
	 */
	public static boolean getGatheringMode() {
		return GatheringMode;
	}

	/**
	 * Checks a certain position for potential neighbors. Throws exception, if
	 * no robot is found.
	 * 
	 * @param x
	 *            the x value to check.
	 * @param y
	 *            the y value to check.
	 * @return true if robots are available, false if not.
	 * @throws RobotHasNoNeighborException
	 *             in case no robot is near this coordinate.
	 */
	public static boolean neighborDiscovery(int x, int y) throws RobotHasNoNeighborException {
		int srcX = x;
		int srcY = y;

		if (robots.isEmpty()) {
			return true;
		} else {
			for (int i = 1; i <= coordinates.size(); i++) {
				double dstX = coordinates.get(i).getX();
				double dstY = coordinates.get(i).getY();

				if (Math.pow((dstX - srcX), 2) + Math.pow((dstY - srcY), 2) <= Math.pow(RADIUS, 2)) {
					return true;
				}
			}
		}
		throw new RobotHasNoNeighborException("No neighbors near x:" + srcX + " and y:" + srcY + ".");
	}

	/**
	 * Method to deploy robots with the chosen algorithm.
	 * 
	 * @param amount
	 *            of robots to be deployed.
	 * @param distance
	 *            viewing range of the robots.
	 */
	public static void deployRobots(int amount, int distance) {
		DEPLOYALGORITHM.deployRobots(amount, distance);
		saveCoordinates = new HashMap<Integer, Coordinates>(coordinates);
		Simulator.distance = distance;
	}

	/**
	 * Returns all Coordinate objects of the robots currently stored as a List
	 * <Coordinates> object.
	 * 
	 * @return a list with all coordinates currently stored.
	 */
	public static List<Coordinates> getRobotsLocations() {
		List<Coordinates> temp_coords = new ArrayList<Coordinates>();
		for (Entry<Integer, Coordinates> entry : coordinates.entrySet()) {
			temp_coords.add(entry.getValue());
		}
		return temp_coords;
	}

	/**
	 * Generates a Folder with the date of execution in its name to save the
	 * results of this session executed within the constructor of a simulator
	 */
	public static void generateFolder() {
		folderName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		//Genrate OS independent Foldername
		folderName = "results_" + folderName + System.getProperty("file.separator");
		File folder = new File(folderName);
		folder.mkdir();
	}

	/**
	 * Generates a output file with a timestamp in its name, to save all stats
	 * of a simulation run. Adds a Result to the results list
	 */
	public static void generateFile() {
		PrintWriter writer;
		try {
			// Create Filename
			fileNameDate = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			String fileNameDateTemp = fileNameDate + ".txt";
			fileNameDate = folderName + fileNameDate + ".txt";
			writer = new PrintWriter(fileNameDate, "UTF-8");
			// Write config to file
			writer.println("Deploymentstrategy: " + DeployAlg);
			writer.println("Robots: " + robots.size());
			writer.println("Viewingrange: " + distance);
			writer.println();
			// Write results to file
			writer.println("Results: ");
			writer.println(String.format("Iterations: %d", Iterations));
			writer.println(String.format("TotalDistance: %f", TotalDistance));
			writer.println(String.format("TotalMangan: %f", TotalMangan));
			writer.println();
			// Write robots coordinates to file
			writer.println("Robots coordinates:");
			for (Entry<Integer, Coordinates> entry : saveCoordinates.entrySet()) {
				writer.println((int) entry.getValue().getX() + " " + (int) entry.getValue().getY());
			}
			writer.close();
			System.out.println("Saved file to: " + fileNameDate);
			// Add Entry to results List
			Results.add(new Result(TotalMangan, TotalDistance, fileNameDateTemp, DeployAlg, robots.size(), distance,
					Iterations));
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't write file: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding? Something is wrong here: " + e.getMessage());
		}
	}

	/**
	 * creates a table of the last n results and prints them to a TXT file
	 * 
	 * @param n,
	 *            last n results to take into account
	 */
	public static void printResultsTXT(int n) {
		PrintWriter writer;
		Result temp1 = null; // max ManganPerIteration
		Result temp2 = null; // max DistancePerIteration
		// Create filename
		String file = folderName + "results.txt";
		try {
			writer = new PrintWriter(file, "UTF-8");
			// Create Table Head
			writer.println(String.format("%-20s%-20s%-8s%-14s%-12s%-14s%-14s%-22s%-24s", "Filename",
					"Deploymentstrategy", "Robots", "Viewingrange", "Iterations", "Mangan", "Distance",
					"Mangan per Iteration", "Distance per Iteration"));
			// print n last results
			int e = Results.size() - 1;
			for (int i = 0; i < n; i++) {
				writer.print(String.format("%-20s", Results.get(e - i).getFile()));
				writer.print(String.format("%-20s", Results.get(e - i).getStrategy()));
				writer.print(String.format("%6d  ", Results.get(e - i).getRobots()));
				writer.print(String.format("%12d  ", Results.get(e - i).getView()));
				writer.print(String.format("%10d  ", Results.get(e - i).getIterations()));
				writer.print(String.format("%12f  ", Results.get(e - i).getMangan()));
				writer.print(String.format("%12f  ", Results.get(e - i).getDistance()));
				writer.print(String.format("%20f  ", Results.get(e - i).getManganPerIteration()));
				writer.print(String.format("%22f  %n", Results.get(e - i).getDistancePerIteration()));
				// find the best run
				if (temp1 == null) {
					temp1 = Results.get(i);
				} else {
					temp1 = temp1.getBest(Results.get(i));
				}
				// find the most moving run
				if (temp2 == null) {
					temp2 = Results.get(i);
				} else {
					temp2 = temp2.getMovest(Results.get(i));
				}
			}
			if (Results.size() > 1) {
				writer.println();
				// Print best run
				writer.println("Best run:");
				writer.print(String.format("%-20s", temp1.getFile()));
				writer.print(String.format("%-20s", temp1.getStrategy()));
				writer.print(String.format("%6d  ", temp1.getRobots()));
				writer.print(String.format("%12d  ", temp1.getView()));
				writer.print(String.format("%10d  ", temp1.getIterations()));
				writer.print(String.format("%12f  ", temp1.getMangan()));
				writer.print(String.format("%12f  ", temp1.getDistance()));
				writer.print(String.format("%20f  ", temp1.getManganPerIteration()));
				writer.print(String.format("%22f  %n", temp1.getDistancePerIteration()));
				writer.println();
				// Print the most moving run
				writer.println("Most moving of robots:");
				writer.print(String.format("%-20s", temp2.getFile()));
				writer.print(String.format("%-20s", temp2.getStrategy()));
				writer.print(String.format("%6d  ", temp2.getRobots()));
				writer.print(String.format("%12d  ", temp2.getView()));
				writer.print(String.format("%10d  ", temp2.getIterations()));
				writer.print(String.format("%12f  ", temp2.getMangan()));
				writer.print(String.format("%12f  ", temp2.getDistance()));
				writer.print(String.format("%20f  ", temp2.getManganPerIteration()));
				writer.print(String.format("%22f  %n", temp2.getDistancePerIteration()));
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't write file: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding? Something is wrong here: " + e.getMessage());
		}
	}

	/**
	 * creates a table of the last n results and prints them to a CSV file
	 * 
	 * @param n,
	 *            last n results to take into account
	 */
	public static void printResultsCSV(int n) {
		PrintWriter writer;
		Result temp1 = null; // max ManganPerIteration
		Result temp2 = null; // max DistancePerIteration
		// Create filename
		String file = folderName + "results.csv";
		try {
			writer = new PrintWriter(file, "UTF-8");
			// Create Table Head
			writer.println(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s", "Filename", "Deploymentstrategy", "Robots",
					"Viewingrange", "Iterations", "Mangan", "Distance", "Mangan per Iteration",
					"Distance per Iteration"));
			// print n last results
			int e = Results.size() - 1;
			for (int i = 0; i < n; i++) {
				writer.print(String.format("%s;", Results.get(e - i).getFile()));
				writer.print(String.format("%s;", Results.get(e - i).getStrategy()));
				writer.print(String.format("%d;", Results.get(e - i).getRobots()));
				writer.print(String.format("%d;", Results.get(e - i).getView()));
				writer.print(String.format("%d;", Results.get(e - i).getIterations()));
				writer.print(String.format("%f;", Results.get(e - i).getMangan()));
				writer.print(String.format("%f;", Results.get(e - i).getDistance()));
				writer.print(String.format("%f;", Results.get(e - i).getManganPerIteration()));
				writer.print(String.format("%f;%n", Results.get(e - i).getDistancePerIteration()));
				// find the best run
				if (temp1 == null) {
					temp1 = Results.get(i);
				} else {
					temp1 = temp1.getBest(Results.get(i));
				}
				// find the most moving run
				if (temp2 == null) {
					temp2 = Results.get(i);
				} else {
					temp2 = temp2.getMovest(Results.get(i));
				}
			}
			if (Results.size() > 1) {
				writer.println();
				// Print best run
				writer.println("Best run:");
				writer.print(String.format("%s;", temp1.getFile()));
				writer.print(String.format("%s;", temp1.getStrategy()));
				writer.print(String.format("%d;", temp1.getRobots()));
				writer.print(String.format("%d;", temp1.getView()));
				writer.print(String.format("%d;", temp1.getIterations()));
				writer.print(String.format("%f;", temp1.getMangan()));
				writer.print(String.format("%f;", temp1.getDistance()));
				writer.print(String.format("%f;", temp1.getManganPerIteration()));
				writer.print(String.format("%f;%n", temp1.getDistancePerIteration()));
				writer.println();
				// Print the most moving run
				writer.println("Most moving of robots:");
				writer.print(String.format("%s;", temp2.getFile()));
				writer.print(String.format("%s;", temp2.getStrategy()));
				writer.print(String.format("%d;", temp2.getRobots()));
				writer.print(String.format("%d;", temp2.getView()));
				writer.print(String.format("%d;", temp2.getIterations()));
				writer.print(String.format("%f;", temp2.getMangan()));
				writer.print(String.format("%f;", temp2.getDistance()));
				writer.print(String.format("%f;", temp2.getManganPerIteration()));
				writer.print(String.format("%f;%n", temp2.getDistancePerIteration()));
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't write file: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding? Something is wrong here: " + e.getMessage());
		}
	}

	/**
	 * Sets the weights so the robots gather even more to locations where they
	 * can be collected by the imaginary ship again.
	 */
	public static void gather() {
		weight1 = 100.0;
		weight2 = 0.0;
		weight3 = 0.0;
		GatheringMode = true;
		FarDistance = true;
		System.out.println("GATHERING MODE");
	}

	/**
	 * Getter for the total amount of mangan collected till now.
	 * 
	 * @return TotalMangan as double.
	 */
	public static double getTotalMangan() {
		return TotalMangan;
	}

	/**
	 * Getter for the total distance traveled by all robots till now.
	 * 
	 * @return TotalDistance as double.
	 */
	public static double getTotalDistance() {
		return TotalDistance;
	}
}
