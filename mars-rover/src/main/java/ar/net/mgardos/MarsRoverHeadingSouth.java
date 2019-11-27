package ar.net.mgardos;

public class MarsRoverHeadingSouth implements MarsRoverHeading {
    @Override
    public void rotateRight(MarsRover marsRover) {
        marsRover.pointToWest();
    }

    @Override
    public void rotateLeft(MarsRover marsRover) {
        marsRover.pointToEast();
    }

    @Override
    public int moveBackward(MarsRover marsRover) {
        return marsRover.moveNorth();
    }

    @Override
    public int moveForward(MarsRover marsRover) {
        return marsRover.moveSouth();
    }
}
