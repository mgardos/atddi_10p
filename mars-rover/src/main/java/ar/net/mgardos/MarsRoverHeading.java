package ar.net.mgardos;

public interface MarsRoverHeading {
    void rotateLeft(MarsRover marsRover);

    void rotateRight(MarsRover marsRover);

    int moveBackward(MarsRover marsRover);

    int moveForward(MarsRover marsRover);
}
