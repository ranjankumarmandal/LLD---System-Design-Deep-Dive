import java.util.*;

enum SignalState {
    RED,
    YELLOW,
    GREEN
}

enum PedestrianState {
    WALK,
    DONT_WALK
}

enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
}

class Timer {
    private final int duration;

    public Timer(int duration) {
        this.duration = duration;
    }

    public void waitForNextCycle() {
        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class TrafficSignal {
    private SignalState state = SignalState.RED;

    public void setState(SignalState state) {
        this.state = state;
    }

    public SignalState getState() {
        return state;
    }
}

class PedestrianSignal {
    private PedestrianState state = PedestrianState.DONT_WALK;

    public void setState(PedestrianState state) {
        this.state = state;
    }

    public PedestrianState getState() {
        return state;
    }
}

class Road {
    private final Direction direction;
    private final TrafficSignal vehicleSignal;
    private final PedestrianSignal pedestrianSignal;

    public Road(Direction direction) {
        this.direction = direction;
        this.vehicleSignal = new TrafficSignal();
        this.pedestrianSignal = new PedestrianSignal();
    }

    public Direction getDirection() {
        return direction;
    }

    public TrafficSignal getVehicleSignal() {
        return vehicleSignal;
    }

    public PedestrianSignal getPedestrianSignal() {
        return pedestrianSignal;
    }
}

class Intersection {
    private final Map<Direction, Road> roads = new EnumMap<>(Direction.class);

    public void addRoad(Road road) {
        roads.put(road.getDirection(), road);
    }

    public Road getRoad(Direction direction) {
        return roads.get(direction);
    }

    public Collection<Road> getRoads() {
        return roads.values();
    }
}

class Controller {
    private final Intersection intersection;
    private final Timer greenTimer = new Timer(30);
    private final Timer yellowTimer = new Timer(5);

    public Controller(Intersection intersection) {
        this.intersection = intersection;
    }

    public void start() {
        while (true) {
            allowNorthSouth();
            allowEastWest();
        }
    }

    private void allowNorthSouth() {
        setVehicle(Direction.NORTH, SignalState.GREEN);
        setVehicle(Direction.SOUTH, SignalState.GREEN);
        setVehicle(Direction.EAST, SignalState.RED);
        setVehicle(Direction.WEST, SignalState.RED);

        setPedestrian(Direction.NORTH, PedestrianState.DONT_WALK);
        setPedestrian(Direction.SOUTH, PedestrianState.DONT_WALK);
        setPedestrian(Direction.EAST, PedestrianState.WALK);
        setPedestrian(Direction.WEST, PedestrianState.WALK);

        printStatus();
        greenTimer.waitForNextCycle();

        setVehicle(Direction.NORTH, SignalState.YELLOW);
        setVehicle(Direction.SOUTH, SignalState.YELLOW);

        printStatus();
        yellowTimer.waitForNextCycle();

        setVehicle(Direction.NORTH, SignalState.RED);
        setVehicle(Direction.SOUTH, SignalState.RED);
    }

    private void allowEastWest() {
        setVehicle(Direction.EAST, SignalState.GREEN);
        setVehicle(Direction.WEST, SignalState.GREEN);
        setVehicle(Direction.NORTH, SignalState.RED);
        setVehicle(Direction.SOUTH, SignalState.RED);

        setPedestrian(Direction.NORTH, PedestrianState.WALK);
        setPedestrian(Direction.SOUTH, PedestrianState.WALK);
        setPedestrian(Direction.EAST, PedestrianState.DONT_WALK);
        setPedestrian(Direction.WEST, PedestrianState.DONT_WALK);

        printStatus();
        greenTimer.waitForNextCycle();

        setVehicle(Direction.EAST, SignalState.YELLOW);
        setVehicle(Direction.WEST, SignalState.YELLOW);

        printStatus();
        yellowTimer.waitForNextCycle();

        setVehicle(Direction.EAST, SignalState.RED);
        setVehicle(Direction.WEST, SignalState.RED);
    }

    private void setVehicle(Direction direction, SignalState state) {
        intersection.getRoad(direction).getVehicleSignal().setState(state);
    }

    private void setPedestrian(Direction direction, PedestrianState state) {
        intersection.getRoad(direction).getPedestrianSignal().setState(state);
    }

    private void printStatus() {
        System.out.println("---------------");
        for (Road road : intersection.getRoads()) {
            System.out.println(
                    road.getDirection() +
                            " Vehicle: " + road.getVehicleSignal().getState() +
                            " Pedestrian: " + road.getPedestrianSignal().getState()
            );
        }
    }
}

public class TrafficSignalSystem {
    public static void main(String[] args) {
        Intersection intersection = new Intersection();

        intersection.addRoad(new Road(Direction.NORTH));
        intersection.addRoad(new Road(Direction.SOUTH));
        intersection.addRoad(new Road(Direction.EAST));
        intersection.addRoad(new Road(Direction.WEST));

        Controller controller = new Controller(intersection);
        controller.start();
    }
}