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

class ElevatorSystem {
    List<Elevator> elevators;

    public ElevatorSystem(int numElevators, int numFloors) {
        elevators = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator(i, 0));
        }
    }

    public void handleExternalRequest(int floor, Direction direction) {
        Elevator best = null;
        int minDistance = Integer.MAX_VALUE;
        for (Elevator e : elevators) {
            int distance = Math.abs(e.currentFloor - floor);
            if (e.direction == Direction.IDLE ||
                    (e.direction == direction && ((direction == Direction.UP && e.currentFloor < floor)
                            || (direction == Direction.DOWN && e.currentFloor > floor)))) {
                if (distance < minDistance) {
                    best = e;
                    minDistance = distance;
                }
            }
        }
        if (best == null) best = elevators.get(0);
        best.addTargetFloor(floor);
        System.out.println("Assigned external request at floor " + floor + " to elevator " + best.id);
    }

    public void handleInternalRequest(int elevatorId, int floor) {
        elevators.get(elevatorId).addTargetFloor(floor);
        System.out.println("Elevator " + elevatorId + " got internal request to floor " + floor);
    }

    public void step() {
        for (Elevator e : elevators) {
            e.step();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ElevatorSystem es = new ElevatorSystem(2, 10);
        es.handleExternalRequest(5, Direction.UP);
        es.handleExternalRequest(3, Direction.DOWN);
        es.handleExternalRequest(7, Direction.UP);
        es.handleInternalRequest(0, 9);

        for (int i = 0; i < 15; i++) {
            es.step();
        }
    }
}