import java.util.*;

enum Direction {
    UP, DOWN, IDLE
}

enum ElevatorState {
    MOVING, IDLE, STOPPED
}

class Request {
    int floor;
    Direction direction;

    public Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }
}