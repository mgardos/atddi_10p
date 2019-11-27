package ar.net.mgardos;

public class MarsRoverHeadingNorth implements MarsRoverHeading {
    @Override
    public void rotateLeft(final MarsRover marsRover) {
        marsRover.pointToWest();
    }

    @Override
    public void rotateRight(final MarsRover marsRover) {
        marsRover.pointToEast();
    }

    @Override
    public int moveBackward(final MarsRover marsRover) {
        return marsRover.moveSouth();
    }

    @Override
    public int moveForward(final MarsRover marsRover) {
        return marsRover.moveNorth();
    }
}
