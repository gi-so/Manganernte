package mangan;

import java.lang.Exception;

/**
 * Exception thrown in case of, having already a robot in the surface,
 * a deployment is attempted in a position where the robots would not be able to see each other
 */
public class RobotHasNoNeighborException extends Exception {
	/**
	 * Standard exception constructor goes directly to parent.
	 */
	public RobotHasNoNeighborException() { 
	    super(); 
    }
	
	/**
	 * Standard constructor with a message as hint for the exception.
	 * 
	 * @param message describing what the error created.
	 */
    public RobotHasNoNeighborException(String message) { 
    	super(message); 
    }

	/**
	 * Standard constructor with a message and a cause as hint for the exception.
	 * 
	 * @param message describing what the error created.
	 * @param cause describing what the error created.
	 */
    public RobotHasNoNeighborException(String message, Throwable cause) { 
    	super(message, cause); 
    }

	/**
	 * Standard constructor with a cause as hint for the exception.
	 * 
	 * @param cause describing what the error created.
	 */
    public RobotHasNoNeighborException(Throwable cause) { 
    	super(cause); 
    }
}