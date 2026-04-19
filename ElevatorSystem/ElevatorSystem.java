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

class Elevator {
    int id;
    int currentFloor;
    TreeSet<Integer> targetFloors;
    Direction direction;
    ElevatorState state;

    public Elevator(int id, int initialFloor) {
        this.id = id;
        this.currentFloor = initialFloor;
        this.targetFloors = new TreeSet<>();
        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;
    }

    public void addTargetFloor(int floor) {
        targetFloors.add(floor);
        if (floor > currentFloor) direction = Direction.UP;
        else if (floor < currentFloor) direction = Direction.DOWN;
        else direction = Direction.IDLE;
    }

    public void step() {
        if (state == ElevatorState.IDLE && !targetFloors.isEmpty()) {
            state = ElevatorState.MOVING;
        }
        if (state == ElevatorState.MOVING) {
            if (direction == Direction.UP) currentFloor++;
            else if (direction == Direction.DOWN) currentFloor--;

            if (targetFloors.contains(currentFloor)) {
                targetFloors.remove(currentFloor);
                System.out.println("Elevator " + id + " stopping at floor " + currentFloor);
                state = ElevatorState.STOPPED;

                if (targetFloors.isEmpty()) {
                    direction = Direction.IDLE;
                    state = ElevatorState.IDLE;
                } else {
                    int next = targetFloors.first();
                    if (next > currentFloor) direction = Direction.UP;
                    else if (next < currentFloor) direction = Direction.DOWN;
                    else direction = Direction.IDLE;
                    state = ElevatorState.MOVING;
                }
            }
        }
    }
}