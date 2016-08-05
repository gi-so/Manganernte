//JUnit-Testing Tutorial
//http://www.vogella.com/tutorials/JUnit/article.html

package manganTest;

import mangan.RobotHasNoNeighborException;
import mangan.Simulator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.After;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class RobotTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@After
	public void cleanIndex() {
		Simulator.clearRobots();
	}
	
	@Test
	public void throwsNothing() {
	}

	@Test
	public void throwsRobotHasNoNeighborException() throws RobotHasNoNeighborException {
		thrown.expect(RobotHasNoNeighborException.class);
		thrown.expectMessage("No neighbors near");
		Simulator.createRobot(0,0);
		Simulator.createRobot(1000,0);
	}
	
	@Test
	public void throwsNoRobotHasNoNeighborException() throws RobotHasNoNeighborException {
		try {
			Simulator.createRobot(0,0);
			Simulator.createRobot(1,0);
			Simulator.createRobot(0,1);
			Simulator.createRobot(1,1);
		} catch(RobotHasNoNeighborException ex) {
			fail("Robot definitely has a neighbor. Fix your code!");
		}
	}
	
	@Test
	public void robotsOnTheSameField() throws RobotHasNoNeighborException {
		try {
			Simulator.createRobot(0,0);
			Simulator.createRobot(0,0);
		} catch(RobotHasNoNeighborException ex) {
			fail("Two robots on the same field don't find each other.");
		}

	}
	
	@Test
	public void robotCountIsCorrect() throws RobotHasNoNeighborException {
		for(int i = 0; i < 1000; i++) {
			Simulator.createRobot(0,0);
		}
		assertEquals(1000, Simulator.getRobotCount(), 0.0);
	}
}
