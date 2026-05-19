import java.util.*;

enum Direction {
    UP,
    DOWN,
    IDLE
}

class Floor {
    private int number;

    public Floor(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

class Request {
    private Floor sourceFloor;
    private Floor destinationFloor;
    private Direction direction;

    public Request(Floor sourceFloor, Floor destinationFloor) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = destinationFloor.getNumber() > sourceFloor.getNumber()
                ? Direction.UP
                : Direction.DOWN;
    }

    public Floor getSourceFloor() {
        return sourceFloor;
    }

    public Floor getDestinationFloor() {
        return destinationFloor;
    }

    public Direction getDirection() {
        return direction;
    }
}

class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private Queue<Request> requests;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.requests = new LinkedList<>();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void addRequest(Request request) {
        requests.offer(request);
    }

    public boolean isIdle() {
        return requests.isEmpty();
    }

    public void processRequests() {
        while (!requests.isEmpty()) {
            Request request = requests.poll();

            moveToFloor(request.getSourceFloor().getNumber());
            moveToFloor(request.getDestinationFloor().getNumber());
        }

        direction = Direction.IDLE;
    }

    private void moveToFloor(int destination) {
        if (destination > currentFloor) {
            direction = Direction.UP;

            while (currentFloor < destination) {
                currentFloor++;
                System.out.println("Elevator " + id + " at floor " + currentFloor);
            }
        } else if (destination < currentFloor) {
            direction = Direction.DOWN;

            while (currentFloor > destination) {
                currentFloor--;
                System.out.println("Elevator " + id + " at floor " + currentFloor);
            }
        }
    }
}

class ElevatorSystem {
    private List<Elevator> elevators;

    public ElevatorSystem(int elevatorCount) {
        elevators = new ArrayList<>();

        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator(i + 1));
        }
    }

    public void handleRequest(Request request) {
        Elevator elevator = findBestElevator(request);
        elevator.addRequest(request);
    }

    private Elevator findBestElevator(Request request) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(
                    elevator.getCurrentFloor() - request.getSourceFloor().getNumber()
            );

            if (distance < minDistance) {
                minDistance = distance;
                bestElevator = elevator;
            }
        }

        return bestElevator;
    }

    public void processAllRequests() {
        for (Elevator elevator : elevators) {
            elevator.processRequests();
        }
    }
}

class User {
    private int id;

    public User(int id) {
        this.id = id;
    }

    public Request createRequest(int source, int destination) {
        return new Request(new Floor(source), new Floor(destination));
    }
}