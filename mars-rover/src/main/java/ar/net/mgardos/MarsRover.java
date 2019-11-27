package ar.net.mgardos;

public class MarsRover {
    private int x;
    private int y;
    private String heading;
    private MarsRoverHeading headingNorth;
    private MarsRoverHeading headingWest;
    private MarsRoverHeadingSouth headingSouth;
    private MarsRoverHeadingEast headingEast;

    public MarsRover(int x, int y, String heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        headingNorth = new MarsRoverHeadingNorth();
        headingWest = new MarsRoverHeadingWest();
        headingSouth = new MarsRoverHeadingSouth();
        headingEast = new MarsRoverHeadingEast();
    }

    public boolean isAt(int x, int y, String heading) {
        return this.x == x && this.y == y && this.heading.equals(heading);
    }

    public void process(String commands) {
        commands.chars().forEach(command -> processCommand(Character.toString(command)));
    }

    public void processCommand(String string) {
        if (heading.equals("E")) {
            if (string.equals("f")) {
                headingEast.moveForward(this);
            }
            if (string.equals("b")) {
                moveWest();
            }
            if (string.equals("l")) {
                pointToNorth();
            }
            if (string.equals("r")) {
                pointToSouth();
            }
        } else if (heading.equals("S")) {
            if (string.equals("b")) {
                headingSouth.moveBackward(this);
            }
            if (string.equals("f")) {
                headingSouth.moveForward(this);
            }
            if (string.equals("l")) {
                headingSouth.rotateLeft(this);
            }
            if (string.equals("r")) {
                headingSouth.rotateRight(this);
            }
        } else if (heading.equals("W")) {
            if (string.equals("b")) {
                headingWest.moveBackward(this);
            }
            if (string.equals("f")) {
                headingWest.moveForward(this);
            }
            if (string.equals("l")) {
                headingWest.rotateLeft(this);
            }
            if (string.equals("r")) {
                headingWest.rotateRight(this);
            }
        } else if (heading.equals("N")) {
            if (string.equals("f")) {
                headingNorth.moveForward(this);
            }
            if (string.equals("b")) {
                headingNorth.moveBackward(this);
            }
            if (string.equals("r")) {
                headingNorth.rotateRight(this);
            }
            if (string.equals("l")) {
                headingNorth.rotateLeft(this);
            }
        }
    }

    public void pointToWest() {
        heading = "W";
    }

    public void pointToEast() {
        heading = "E";
    }

    public int moveSouth() {
        return y--;
    }

    public int moveNorth() {
        return y++;
    }

    public void pointToSouth() {
        heading = "S";
    }

    public void pointToNorth() {
        heading = "N";
    }

    public int moveWest() {
        return x--;
    }

    public int moveEast() {
        return x++;
    }
}
