package mangan;

import java.awt.Point;

/**
 * Own class created to handle all the information of every single robot. A set of operators allow us to add coordinates or
 * velocity, substract, get the components, etc.
 */
public class Coordinates extends Point {

	/**
	 * x value of the Coordinates.
	 */
	private double x;
	
	/**
	 * y value of the Coordinates.
	 */
	private double y;
	
	/**
	 * x values of the velocity of these Coordinates.
	 */
	private double Vx;

	/**
	 * y values of the velocity of these Coordinates.
	 */
	private double Vy;

	/**
	 * Constructor needs a position.
	 * @param x
	 * @param y
	 */
	public Coordinates(double x, double y) {
		this.x = x;
		this.y = y;
	}

    /** 
     * hashcode is overwritten, because we might store n times the same location in a map.
     * @return the System.identityHashCode() as a more or less unique value.
     */
	@Override
	public int hashCode() {
		int result = System.identityHashCode(this);
		return result;
 	}

	/**
	 * Overwritten to make sure every object is more or less unique.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinates other = (Coordinates) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/**
	 * (non-Javadoc)
	 * @see java.awt.Point#getX()
	 */
	public double getX() {
		return x;
	}

	/**
	 * (non-Javadoc)
	 * @see java.awt.Point#getY()
	 */
	public double getY() {
		return y;
	}

	/**
	 * Getter for velocity x of this coordinate object.
	 * @return the velocity x as double.
	 */
	public double getVx() {
		return Vx;
	}

	/**
	 * Getter for velocity y of this coordinate object.
	 * @return the velocity y as double.
	 */
	public double getVy() {
		return Vy;
	}
	
	/**
	 * Setter for location x value.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Setter for location y value.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Setter for objects with coordinates.
	 * @param setCord is a Coordinates object from which we take the x and y values.
	 */
	public void setCoords(Coordinates setCord){
		this.x = setCord.getX();
		this.y = setCord.getY();
	}
	
	/**
	 * To add two Coordinates objects to each other.
	 * @param addition
	 * @return the sum of both Coordinates.
	 */
	public Coordinates add(Coordinates addition){
		this.x = this.x + addition.getX();
		this.y = this.y + addition.getY();
		return this;
	}
	
	/**
	 * Setter for the velocity x and y.
	 * @param velocity other Coordinates object.
	 * @return new object with applied values.
	 */
	public Coordinates setVelocity (Coordinates velocity){
		this.Vx = velocity.getVx();
		this.Vy = velocity.getVy();
		return this;
	}

	/**
	 * Adder for the velocity x and y.
	 * @param velocity other Coordinates object.
	 * @return new object with applied values.
	 */
	public Coordinates addVelocity (Coordinates velocity){
		this.Vx += velocity.getVx();
		this.Vy += velocity.getVy();
		return this;
	}
	
	
	/**
	 * To subtract two Coordinates from each other.
	 * @param substraction other Coordinates object.
	 * @return new object with applied values.
	 */
	public Coordinates sub(Coordinates substraction){
		this.x = this.x - substraction.getX();
		this.y = this.y - substraction.getY();
		return this;
	}
	
	/**
	 * To multiply two Coordinates by each other.
	 * @param product other Coordinates object.
	 * @return new object with applied values.
	 */
	public Coordinates multiply (double product){
		this.x = this.x * product;
		this.y = this.y * product;
		return this;
	}
	
	/**
	 * To divide two Coordinates with each other.
	 * @param divisor other Coordinates object.
	 * @return new object with applied values.
	 */
	public Coordinates divide (int divisor){
		if (divisor != 0) {
			if (this.x != 0.0){
				this.x = this.x / divisor;
			}
			else {
				this.x = 0.0;
			}
			if (this.y != 0.0){
				this.y = this.y / divisor;
			} else {
				this.y = 0.0;
			}
		}
		return this;
	}
}
