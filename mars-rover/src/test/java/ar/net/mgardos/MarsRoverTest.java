package ar.net.mgardos;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The Mars Rover is expected to move forward (f), backward (b) and turn left (l) and right (r), accepting more than
 * one move. When an invalid command is encountered, the rover should stop moving and notify.
 */
public class MarsRoverTest {
    @Test
    public void test01() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("");

        assertTrue(marsRover.isAt(1, 2, "N"));
    }

    @Test
    public void test02() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("f");

        assertTrue(marsRover.isAt(1, 3, "N"));
    }

    @Test
    public void test03() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("b");

        assertTrue(marsRover.isAt(1, 1, "N"));
    }

    @Test
    public void test04() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("r");

        assertFalse(marsRover.isAt(1, 2, "N"));
        assertTrue(marsRover.isAt(1, 2, "E"));
    }

    @Test
    public void test05() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("l");

        assertFalse(marsRover.isAt(1, 2, "N"));
        assertTrue(marsRover.isAt(1, 2, "W"));
    }

    @Test
    public void test06() {
        MarsRover marsRover = new MarsRover(1, 2, "E");
        marsRover.process("f");

        assertFalse(marsRover.isAt(1, 2, "E"));
        assertTrue(marsRover.isAt(2, 2, "E"));
    }

    @Test
    public void test07() {
        MarsRover marsRover = new MarsRover(1, 2, "E");
        marsRover.process("b");

        assertFalse(marsRover.isAt(1, 2, "E"));
        assertTrue(marsRover.isAt(0, 2, "E"));
    }

    @Test
    public void test08() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("ffbl");

        assertFalse(marsRover.isAt(1, 2, "N"));
        assertTrue(marsRover.isAt(1, 3, "W"));
    }

    @Test
    public void test09() {
        MarsRover marsRover = new MarsRover(1, 2, "E");
        marsRover.process("l");

        assertFalse(marsRover.isAt(1, 2, "E"));
        assertTrue(marsRover.isAt(1, 2, "N"));
    }

    @Test
    public void test10() {
        MarsRover marsRover = new MarsRover(1, 2, "E");
        marsRover.process("r");

        assertFalse(marsRover.isAt(1, 2, "E"));
        assertTrue(marsRover.isAt(1, 2, "S"));
    }

    @Test
    public void test11() {
        MarsRover marsRover = new MarsRover(1, 2, "S");
        marsRover.process("ffbllrrr");

        assertFalse(marsRover.isAt(1, 2, "S"));
        assertTrue(marsRover.isAt(1, 1, "W"));
    }

    @Test
    public void test12() {
        MarsRover marsRover = new MarsRover(1, 2, "W");
        marsRover.process("ffbllrrr");

        assertFalse(marsRover.isAt(1, 2, "W"));
        assertTrue(marsRover.isAt(0, 2, "N"));
    }

    @Test
    public void test13() {
        MarsRover marsRover = new MarsRover(1, 2, "N");
        marsRover.process("flf");

        assertFalse(marsRover.isAt(1, 2, "N"));
        assertTrue(marsRover.isAt(0, 3, "W"));
    }
}
