package ar.net.mgardos;

public class MarsRoverHeadingWest implements MarsRoverHeading {
    @Override
    public void rotateRight(MarsRover marsRover) {
        marsRover.pointToNorth();
    }

    @Override
    public int moveForward(MarsRover marsRover) {
        return marsRover.moveWest();
    }

    @Override
    public void rotateLeft(MarsRover marsRover) {
        marsRover.pointToSouth();
    }

    @Override
    public int moveBackward(MarsRover marsRover) {
        return marsRover.moveEast();
    }
}
