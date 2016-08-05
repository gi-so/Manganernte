package mangan;

/**
 * Class for saving an comparing the results, especially for batch mode.
 */
public class Result {
	/**
	 * total collected mangan.
	 */
	private double Mangan;

	/**
	 * total distance traveled by the robots.
	 */
	private double Distance;

	/**
	 * name of the file where all stats of this result are saved.
	 */
	private String File;

	/**
	 * deployment strategy used for this run
	 */
	private String Strategy;

	/**
	 * number of robots
	 */
	private int Robots;

	/**
	 * viewingrange of the robots
	 */
	private int View;

	/**
	 * number of iteration of this run, or time needed for simulation
	 */
	private int Iterations;

	/**
	 * mangan collected per iteration
	 */
	private double ManganPerIteration;

	private double DistancePerIteration;

	/**
	 * Constructor of the result class. It should be only possible to create a
	 * result if all values are known.
	 * 
	 * @param mangan
	 * @param distance
	 * @param file
	 * @param strategy
	 * @param robots
	 * @param view
	 * @param iterations
	 */
	public Result(double mangan, double distance, String file, String strategy, int robots, int view, int iterations) {
		super();
		Mangan = mangan;
		Distance = distance;
		File = file;
		Strategy = strategy;
		Robots = robots;
		View = view;
		Iterations = iterations;
		ManganPerIteration = mangan / iterations;
		DistancePerIteration = distance / iterations;
	}

	/**
	 * Get the Distance per Iteration value
	 * 
	 * @return Distance per Iteration
	 */
	public double getDistancePerIteration() {
		return DistancePerIteration;
	}

	/**
	 * Get the Mangan per Iteration value
	 * 
	 * @return Mangan per Iteration
	 */
	public double getManganPerIteration() {
		return ManganPerIteration;
	}

	/**
	 * Get the Strategy used in this run
	 * 
	 * @return Strategy used in this run
	 */
	public String getStrategy() {
		return Strategy;
	}

	/**
	 * Get the total collected Mangan of this run
	 * 
	 * @return total collected Mangan of this run
	 */
	public double getMangan() {
		return Mangan;
	}

	/**
	 * Get the total distance traveled by the robots of this run
	 * 
	 * @return total distance traveled by the robots of this run
	 */
	public double getDistance() {
		return Distance;
	}

	/**
	 * Get the filename, where all stats of this run are saved to
	 * 
	 * @return filename
	 */
	public String getFile() {
		return File;
	}

	/**
	 * Get the number of robots involved in this run
	 * 
	 * @return number of robots
	 */
	public int getRobots() {
		return Robots;
	}

	/**
	 * Get the viewingrange of the robots involved in this run
	 * 
	 * @return viewingrange of the robots
	 */
	public int getView() {
		return View;
	}

	/**
	 * Get the number of iteration of this run, used as time needed for the
	 * simulation
	 * 
	 * @return number of iterations
	 */
	public int getIterations() {
		return Iterations;
	}

	/**
	 * Compares this result to another result and returns the better one, total
	 * collected Mangan per iteration used as indicator
	 * 
	 * @param a,
	 *            result to compare with
	 * @return the better result
	 */
	public Result getBest(Result a) {
		if (a.getManganPerIteration() > this.getManganPerIteration()) {
			return a;
		} else {
			return this;
		}

	}

	/**
	 * Compares this result to another result and returns the faster one,
	 * iterations used as indicator
	 * 
	 * @param a,
	 *            result to compare with
	 * @return the faster result
	 */
	public Result getFastest(Result a) {
		if (this.getIterations() > a.getIterations()) {
			return a;
		} else {
			return this;
		}

	}

	/**
	 * Compares this result to another result and returns the result with more
	 * total collected mangan
	 * 
	 * @param a,
	 *            result to compare with
	 * @return the result with more total collected mangan
	 */
	public Result getMostColl(Result a) {
		if (this.getMangan() < a.getMangan()) {
			return a;
		} else {
			return this;
		}

	}

	/**
	 * Compares this result to another result and returns the better one,
	 * greatest Movement per iteration used as indicator
	 * 
	 * @param a,
	 *            result to compare with
	 * @return the better result
	 */
	public Result getMovest(Result a) {
		if (a.getDistancePerIteration() > this.getDistancePerIteration()) {
			return a;
		} else {
			return this;
		}

	}

}
