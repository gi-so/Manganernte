package mangan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;

import processing.core.*;
import controlP5.*;

/**
 * The main Visualization which is based on Processing
 * (http://www.processing.org/) for the main animation part and based on a
 * Processing library called ControlP5
 * (http://www.sojamo.de/libraries/controlP5/) for the menu part.
 * 
 * @version 1.8
 */
public class Visualisation extends PApplet {
	/**
	 * ControlP5 instance.
	 */
	ControlP5 cp5;

	/**
	 * ControlP5 radio button which represents the deployment methods.
	 */
	RadioButton menuRadioButton;

	/**
	 * ControlP5 text field which represents the label for the total amount of
	 * mangan.
	 */
	Textlabel totalMangan;

	/**
	 * ControlP5 text field which represents the label for the total distance
	 * traveld by all robots.
	 */
	Textlabel totalDistance;

	/**
	 * ControlP5 text field which represents the label for the total amount of
	 * robots currently deployed.
	 */
	Textlabel totalRobots;

	/**
	 * Placeholder to make ControlP5 Textfield use a string value.
	 */
	private String Robots;

	/**
	 * Placeholder to access ControlP5 Textfield itself.
	 */
	private Textfield RobotsTF;
	
	/**
	 * Placeholder to make ControlP5 Textfield use a string value.
	 */
	private String Speed;
	
	/**
	 * Placeholder to make ControlP5 Textfield use a String value.
	 */
	private Textfield SpeedTF;
	
	/**
	 * Placeholder to make ControlP5 Textfield use a string value.
	 */
	private String ViewingRange;

	/**
	 * Placeholder to access ControlP5 Textfield itself.
	 */
	private Textfield ViewingRangeTF;

	/**
	 * Placeholder to access ControlP5 Textfield itself.
	 */
	private Textfield Weight1TF;

	/**
	 * Value for the weight of algorithm one.
	 */
	private float weight1;

	/**
	 * Minimal value for the weight of algorithm one.
	 */
	private float weight1Start = 1 / 100;

	/**
	 * Maximal value for the weight of algorithm one.
	 */
	private float weight1End = 3;

	/**
	 * Placeholder to access ControlP5 Textfield itself.
	 */
	private Textfield Weight2TF;

	/**
	 * Value for the weight of algorithm two.
	 */
	private float weight2;

	/**
	 * Minimal value for the weight of algorithm one.
	 */
	private float weight2Start = 1 / 100;

	/**
	 * Maximal value for the weight of algorithm one.
	 */
	private float weight2End = 3;

	/**
	 * Placeholder to access ControlP5 Textfield itself.
	 */
	private Textfield Weight3TF;

	/**
	 * Value for the weight of algorithm three.
	 */
	private float weight3;

	/**
	 * Minimal value for the weight of algorithm one.
	 */
	private float weight3Start = 1 / 100;

	/**
	 * Maximal value for the weight of algorithm one.
	 */
	private float weight3End = 3;

	/**
	 * Line spacing for the grid to be drawn as the playground.
	 */
	private int linespacing = 10;

	/**
	 * Initial amount of robots for the ControlP5 slider.
	 */
	private int amountOfRobots = 30;

	/**
	 * Initial distance value for the ControlP5 slider.
	 */
	private int distanceBetweenRobots = 50;

	/**
	 * A boolean to check if the user wants to run in manual mode.
	 */
	private boolean manual = false;

	/**
	 * A boolean to check if the user wants to run batch mode.
	 */
	private boolean all = false;

	/**
	 * A boolean to check if the visualization should draw the movement of the
	 * robots and also trigger the movement of the simulator.
	 */
	private boolean start = false;

	/**
	 * A boolean helper for batch mode.
	 */
	private boolean start2 = false;

	/**
	 * A list for the coordinates if we have an input file.
	 */
	private static List<Coordinates> robotCoordinatesFromFile = new ArrayList<Coordinates>();

	/**
	 * A listing of all drawn robots, similar to the HashMaps in the simulator
	 * to keep track of the order of the robots.
	 */
	private HashMap<Integer, VisualRobot> robots = new HashMap<Integer, VisualRobot>();

	/**
	 * A Processing font object to store the desired font.
	 */
	PFont f;

	/**
	 * The size of the menu entries on the x axis.
	 */
	int menuWidth = 80;

	/**
	 * The height of the menu entries on the y axis.
	 */
	int menuHeight = 20;

	/**
	 * The size of the sidebar menu. Not initiated with the values because the
	 * "width" isn't available at this time of the initiation.
	 */
	int menuBorder;

	/**
	 * The position of the menu Elements of the sidebar menu. Not initiated with
	 * the values because the "width" isn't available at this time of the
	 * initiation.
	 */
	int menuElementPos;

	/**
	 * variable for iterating over all modes
	 */
	int mode = 0;

	/**
	 * Main application which reads files, if inserted from the command-line.
	 * After the reading, we instantiate the processing object.
	 * 
	 * @param args
	 *            Command-line arguments.
	 */
	public static void main(String args[]) {
		if (args.length != 0) {
			try {
				getValuesFromFile(args[0]);
			} catch (IOException e) {
				System.out.println("Cannot read file: " + args[0]);
				System.out.println("Please provide a proper file as described in the requirements.");
			}
		}
		PApplet.main("mangan.Visualisation");
	}

	/**
	 * Processing configuration before running the main scheduler.
	 */
	public void setup() {
		// Frames per second.
		frameRate(5);
		// Size in pixel.
		size(displayWidth, displayHeight);
		// Get an instance of ControlP5 as reference.
		cp5 = new ControlP5(this);
		// Now we can give a value to menuBoarder.
		menuBorder = width - menuWidth;
		menuElementPos = menuBorder - 10;
		// Menu consists of only ControlP5 configurations, so we only have to
		// run it once.
		drawMenu();
		// We are manual at the beginning.
		deployAlgorithm(7);
		// If there are coordinates from a file available.
		if (robotCoordinatesFromFile.size() > 0) {
			// Create a robot for every coordinate from the file.
			for (Coordinates initialRobots : robotCoordinatesFromFile) {
				createRobot((int) initialRobots.getX(), (int) initialRobots.getY());
			}
			// Enable manual input mode.
			start = true;
		}
	}

	/**
	 * Processings draw() runs forevery and renders all the GUI.
	 */
	public void draw() {
		// Background is white.
		background(255);
		// For a better distance feeling, we have a grid on the ground.
		drawCoordinateSystem();
		// If someone clicked "START", we actually have to start moving the
		// robots.
		if (start) {
			// Here and ...
			moveRobots();
			// ... in the simulator.
			Simulator.move();
		}
		// If the simulation is over, we should clear the screen.
		if (Simulator.getRobotCount() == 0) {
			start = false;
		}
		// If the simulator decides to stop, we stop also, or the simulation is
		// over
		if (Simulator.getGatheringMode()) {
			if (!Simulator.getFarDistance()) {
				start = false;
				if (all && mode < 7 && start2) {
					System.out.println("Mode: " + mode);
					Simulator.setDeployAlgorithm(mode); // set the deploy
														// Algorithm to mode
					clearRobots(0); // to clean up after each iteration
					start(0); // to deploy robots first time
					mode++;
					if (mode >= 7)
						start2 = false; // for clean restart of batch mode
				}
			}
		}

		// We draw the robots all the time.
		drawRobots();
		// And also update the statistics needed for the informaticup.
		drawStats();

		// System.out.println(start);

		// If the user wants to place a robot under his cursor, then he should
		// be
		// able to do so.
		if (mousePressed && mouseX < menuBorder - 10) {
			createRobot(mouseX, mouseY);
		}
	}

	/**
	 * Draws a connection between robots that can see each other.
	 * 
	 * @throws RobotHasNoNeighborException
	 */
	void drawConnections() throws RobotHasNoNeighborException {
		// We only need this, if we have more than one robot.
		if (robots.size() > 1) {
			// Loop over every robot we already draw
			int i = 0;
			List<Coordinates> unredunder = new ArrayList<Coordinates>();
			for (Entry<Integer, VisualRobot> rob : robots.entrySet()) {
				// x, y for convenience.
				float x = rob.getValue().x;
				float y = rob.getValue().y;
				// A temporary coordinate for the getNeighbors call.
				Coordinates robCoordinates = new Coordinates(x, y);
				// Get all neighbors of the current robot
				List<Coordinates> neighbors = Simulator.getNeighbors(robCoordinates, rob.getKey());

				// This brings some redundancy but works quite well for not more
				// than 100 robots.
				for (Coordinates coords : neighbors) {
					int occurrences = Collections.frequency(unredunder, coords);
					if (occurrences < neighbors.size()) {
						stroke(0, 240, 20);
						line(x, y, x + (float) coords.getX(), y + (float) coords.getY());
						unredunder.add(new Coordinates(x + (float) coords.getX(), y + (float) coords.getY()));
						i++;
					}
				}
			}
			println(i);
		}
	}

	/**
	 * Draws the coordinate system in the GUI with a width/height represented by
	 * "linespacing". We leave out some space to not interfere with the menu
	 * (menuBorder-20).
	 */
	void drawCoordinateSystem() {
		// We draw the menu background here, because drawMenu() should, for
		// convenience reasons,
		// only draw ControlP5 objects.
		fill(30);
		rect(menuBorder - 20, 0, width, height);

		// Create a grid as coordinate system with a bright grey as color.
		stroke(120);
		for (int x = 0; x <= menuBorder - 20; x += linespacing) {
			fill(180);
			line(x, 0, x, height);
		}
		for (int y = 0; y <= height; y += linespacing) {
			fill(180);
			line(0, y, menuBorder - 20, y);
		}
	}

	/**
	 * Draws the statistics which are relevant for the informaticup. We
	 * manipulate the values by shortening them to two decimal places.
	 */
	public void drawStats() {
		String tempTotalMangan = String.format("%.2f KG Mangan", Simulator.getTotalMangan());
		String tempTotalDistance = String.format("%.2f m Distance", Simulator.getTotalDistance());

		totalMangan.setText(tempTotalMangan);
		totalDistance.setText(tempTotalDistance);
		totalRobots.setText(Simulator.getRobotCount() + " Robots");
	}

	/**
	 * Draws every robot by calling it's display() method as long as we have at
	 * least one robot.
	 */
	public void drawRobots() {
		if (1 <= robots.size()) {
			for (Entry<Integer, VisualRobot> rob : robots.entrySet()) {
				rob.getValue().display();
			}
		}
	}

	/**
	 * Draws all the robots from the simulator objects as soon as we push the
	 * start button.
	 */
	public void drawRobotsFirstTime() {
		robots.clear();
		List<Coordinates> robotlist = Simulator.getRobotsLocations();
		for (int i = 0; robotlist.size() > i; i++) {
			float x = (float) robotlist.get(i).getX();
			float y = (float) robotlist.get(i).getY();
			VisualRobot robby = new VisualRobot(this, x, y, distanceBetweenRobots);
			robby.display();
			robots.put(i, robby);
		}
	}

	/**
	 * Creates a robot in the simulator and also in the local robots HashMap to
	 * later draw them.
	 * 
	 * @param x
	 *            is the x position.
	 * @param y
	 *            is the y position.
	 */
	public void createRobot(int x, int y) {
		try {
			Simulator.createRobot(x, y);
			robots.put(robots.size(), new VisualRobot(this, x, y, distanceBetweenRobots));
		} catch (RobotHasNoNeighborException e) {
			if (Simulator.debug()) {
				System.out.println("createRobot() in Visualization: " + e.getMessage());
			}
		}

	}

	/**
	 * Move every displayed robot to the position, the simulator calculated.
	 */
	public void moveRobots() {
		if (1 <= robots.size()) {
			List<Coordinates> robotlist = Simulator.getRobotsLocations();
			for (int i = 0; robotlist.size() > i; i++) {
				float x = (float) robotlist.get(i).getX();
				float y = (float) robotlist.get(i).getY();
				robots.get(i).move(x, y);
			}
		}
	}

	/**
	 * All menu entries are drawn here via ControlP5. ControlP5 is the reason,
	 * why we have to call it only once.
	 */

	void drawMenu() {
		totalMangan = cp5.addTextlabel("totalMangan").setText(Simulator.getTotalMangan() + "KG Mangan")
				.setPosition(menuElementPos, 15);
		totalDistance = cp5.addTextlabel("totalDistance").setText(Simulator.getTotalDistance() + "m Distance")
				.setPosition(menuElementPos, 30);
		totalRobots = cp5.addTextlabel("totalRobots").setText(Simulator.getRobotCount() + "Robots")
				.setPosition(menuElementPos, 45);

		cp5.addButton("calculate_weights").setPosition(menuElementPos, height - 450).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

		Weight1TF = cp5.addTextfield("weight1").setPosition(menuElementPos, height - 425).setSize(menuWidth, menuHeight)
				.setDefaultValue(weight1Start);
		cp5.getController("weight1").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM).setPaddingX(0);

		Weight2TF = cp5.addTextfield("weight2").setPosition(menuElementPos, height - 400).setSize(menuWidth, menuHeight)
				.setDefaultValue(weight2Start);
		cp5.getController("weight2").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM).setPaddingX(0);

		Weight3TF = cp5.addTextfield("weight3").setPosition(menuElementPos, height - 375).setSize(menuWidth, menuHeight)
				.setDefaultValue(weight3Start);
		cp5.getController("weight3").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM).setPaddingX(0);

		cp5.addButton("gather").setPosition(menuElementPos, height - 350).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

		menuRadioButton = cp5.addRadioButton("deployAlgorithm").setPosition(menuElementPos, height - 325)
				.setSize(10, 10).setColorForeground(color(120)).setColorActive(color(255)).setColorLabel(color(255))
				.setItemsPerRow(1).setSpacingColumn(5).addItem("Random", 0).addItem("Square", 1).addItem("Circle", 2).addItem("Empty Circle", 3)
				.addItem("Gauss", 4).addItem("Bad Centers", 5).addItem("Spiral", 6).addItem("angular Spiral", 7)
				.addItem("Manual", 8).addItem("All", 9).activate(1);
		RobotsTF = cp5.addTextfield("Robots").setPosition(menuElementPos, height - 195).setSize(menuWidth, menuHeight)
				// .setRange(2,100)
				.setDefaultValue(50);
				
		ViewingRangeTF = cp5.addTextfield("ViewingRange").setPosition(menuElementPos, height - 170)
				.setSize(menuWidth, menuHeight)
				// .setRange(10,400)
				.setDefaultValue(50);
		cp5.getController("ViewingRange").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM).setPaddingX(0);

		cp5.getController("Robots").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM).setPaddingX(0);
		
				
		cp5.addButton("start").setPosition(menuElementPos, height - 125).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

		cp5.addButton("pause").setPosition(menuElementPos, height - 100).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

		cp5.addButton("save").setPosition(menuElementPos, height - 75).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

		cp5.addButton("clearRobots").setPosition(menuElementPos, height - 50).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

		cp5.addButton("exit").setPosition(menuElementPos, height - 25).setSize(menuWidth, menuHeight)
				.setColorForeground(color(120)).setColorActive(color(255));

	}

	/**
	 * Calculate the best weights of a lot of iterations.
	 * 
	 * @param a
	 *            unused id value given to it from ControlP5 object.
	 */
	public void calculate_weights(int a) {
		getTFtext();
		Simulator.deployRobots(amountOfRobots, distanceBetweenRobots);
		drawRobotsFirstTime();
		start = true;
		Simulator.FindBestParameters();
	}

	/**
	 * Set weight for algorithm 1.
	 * 
	 * @param a
	 *            unused id value given to it from ControlP5 object.
	 */
	public void weight1(String a) {
		try {
			weight1 = Float.parseFloat(a);
			if (weight1 > weight1End)
				weight1 = weight1End;
			if (weight1 < weight1Start)
				weight1 = weight1Start;
		} catch (Exception e) {
			weight1 = weight1Start;
		}
		// TODO: Implement this.
	}

	/**
	 * Set weight for algorithm 2.
	 * 
	 * @param a
	 *            unused id value given to it from ControlP5 object.
	 */
	public void weight2(String a) {
		try {
			weight2 = Float.parseFloat(a);
			if (weight2 > weight2End)
				weight2 = weight2End;
			if (weight2 < weight2Start)
				weight2 = weight2Start;
		} catch (Exception e) {
			weight2 = weight2Start;
		}
		// TODO: Implement this.
	}

	/**
	 * Set weight for algorithm 3.
	 * 
	 * @param a
	 *            unused id value given to it from ControlP5 object.
	 */
	public void weight3(String a) {
		try {
			weight3 = Float.parseFloat(a);
			if (weight3 > weight3End)
				weight3 = weight3End;
			if (weight3 < weight3Start)
				weight3 = weight3Start;
		} catch (Exception e) {
			weight3 = weight3Start;
		}
		// TODO: Implement this.
	}

	/**
	 * Tell simulator to gather all robots. Since this is triggered manually via
	 * a ControlP5 button, this can cause situations where the robots start to
	 * cluster in different groups.
	 * 
	 * @param a
	 *            unused id value given to it from ControlP5 object.
	 */
	public void gather(int a) {
		getTFtext();
		Simulator.gather();
	}

	/**
	 * Radiobutton calls this method to set the deployment algorithm. If one
	 * clicks one "MANUAL", manual gets set to true to prevent newly created
	 * robots at a click on start.
	 * 
	 * @param a
	 *            the value of the used radiobutton.
	 */
	public void deployAlgorithm(int a) {
		if (a == 8) {
			manual = true;
			all = false;
			Simulator.setDeployAlgorithm(a); // Only to set the string for
												// saving to file
		} else if (a == 9) {
			manual = false;
			all = true;
			// Prepare the first iteration with the first mode
			Simulator.setDeployAlgorithm(0);
			mode = 1;
			System.out.println("deploy set to all=true");
		} else {
			manual = false;
			all = false;
			Simulator.setDeployAlgorithm(a);

		}
	}

	/**
	 * Start the visualisation of the simulation. If we aren't on manual, we use
	 * the simulator to create robots in the defined deployment algorithm. Then
	 * we use drawRobotsFirstTime() to get all robots from the simulator and
	 * draw them to the GUI.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void start(int a) {
		clearRobots(0);
		// get current values of all text fields
		getTFtext();
		// get current value of the radiobutton, except in batch mode
		if (!start2)
			deployAlgorithm((int) menuRadioButton.getValue());
		if (!manual) {
			Simulator.deployRobots(amountOfRobots, distanceBetweenRobots);
			drawRobotsFirstTime();
			start = true;
			if (all)
				start2 = true; // if batch mode is selected
		} else {
			start = true;
		}
	}

	/**
	 * Stops the drawing of new robots and clears all robots from the simulator
	 * and also the local robots HashMap.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void clearRobots(int a) {
		start = false;
		Simulator.clearRobots();
		robots.clear();
	}

	/**
	 * Set the amount of robots, value between 2 and 100.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void Robots(String a) {
		try {
			amountOfRobots = Integer.parseInt(a);
			if (amountOfRobots > 100)
				amountOfRobots = 100;
			if (amountOfRobots < 2)
				amountOfRobots = 2;
		} catch (Exception e) {
			amountOfRobots = 50;
		}

	}

	/**
	 * Set the viewing range of robots.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void ViewingRange(String a) {
		try {
			distanceBetweenRobots = Integer.parseInt(a);
			if (distanceBetweenRobots > 400)
				distanceBetweenRobots = 400;
			if (distanceBetweenRobots < 10)
				distanceBetweenRobots = 10;
		} catch (Exception e) {
			distanceBetweenRobots = 50;
		}
	}

	/**
	 * Pause the simulation.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void pause(int a) {
		if (start) {
			start = false;
		} else {
			start = true;
		}

	}

	/**
	 * Save current run to file via the simulator.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void save(int a) {
		start = false;
		Simulator.generateFile();
	}

	/**
	 * Exit the application.
	 * 
	 * @param a
	 *            unused id value given by the ControlP5 object.
	 */
	public void exit(int a) {
		System.exit(0);
	}

	/**
	 * ControlP5 controller which gets called at every interaction with the
	 * ControlP5 elements. Isn't in use anymore.
	 * 
	 * @param theEvent
	 *            a unused event handler of ControlP5.
	 * @deprecated jup, we don't need this anymore.
	 */
	public void controlEvent(ControlEvent theEvent) {
	}

	/**
	 * Read values from a file. Only possible if specified from CLI.
	 * 
	 * @param string
	 *            the filename assigned by the CLI.
	 */
	public static void getValuesFromFile(String string) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(string));
		try {
			String line = br.readLine();
			int robotCount = 0;
			String[] robotCoordinates;

			while (line != null) {
				System.out.println(line);
				if (robotCount == 0) {
					robotCount = Integer.parseInt(line);
				} else {
					robotCoordinates = line.split(" ");
					System.out.println(robotCoordinates[0]);
					System.out.println(robotCoordinates[1]);
					Coordinates tempCoordinates = new Coordinates(Integer.parseInt(robotCoordinates[0]),
							Integer.parseInt(robotCoordinates[1]));
					robotCoordinatesFromFile.add(tempCoordinates);
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
	}

	/**
	 * Read values from all Textfields and writes it to the depending variables.
	 * 
	 */
	public void getTFtext() {
		// Get Text of the Robots Textfield if no return was typed
		String temp = RobotsTF.getText();
		Robots(temp);
		// Get Text of the ViewingRange Textfield if no return was typed
		temp = ViewingRangeTF.getText();
		ViewingRange(temp);
		// Get Text of the Weight1 Textfield if no return was typed
		temp = Weight1TF.getText();
		weight1(temp);
		// Get Text of the Weight2 Textfield if no return was typed
		temp = Weight2TF.getText();
		weight2(temp);
		// Get Text of the Weight3 Textfield if no return was typed
		temp = Weight3TF.getText();
		weight3(temp);
	}

}
