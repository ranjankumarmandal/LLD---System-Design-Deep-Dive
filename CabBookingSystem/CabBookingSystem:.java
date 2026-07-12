import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

enum CabType {
    MINI,
    SEDAN,
    SUV
}

enum DriverStatus {
    AVAILABLE,
    ON_TRIP
}

enum TripStatus {
    REQUESTED,
    ASSIGNED,
    STARTED,
    COMPLETED,
    CANCELLED
}

class Location {
    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Location other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}

class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class Rider extends User {
    public Rider(int id, String name) {
        super(id, name);
    }
}

class Driver extends User {
    private DriverStatus status;

    public Driver(int id, String name) {
        super(id, name);
        this.status = DriverStatus.AVAILABLE;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }
}

class Cab {
    private int id;
    private CabType type;
    private Driver driver;
    private Location location;

    public Cab(int id, CabType type, Driver driver, Location location) {
        this.id = id;
        this.type = type;
        this.driver = driver;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public CabType getType() {
        return type;
    }

    public Driver getDriver() {
        return driver;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

class Trip {
    private int id;
    private Rider rider;
    private Cab cab;
    private Location pickup;
    private Location destination;
    private TripStatus status;

    public Trip(int id, Rider rider, Cab cab, Location pickup, Location destination) {
        this.id = id;
        this.rider = rider;
        this.cab = cab;
        this.pickup = pickup;
        this.destination = destination;
        this.status = TripStatus.ASSIGNED;
    }

    public int getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Cab getCab() {
        return cab;
    }

    public Location getPickup() {
        return pickup;
    }

    public Location getDestination() {
        return destination;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void startTrip() {
        status = TripStatus.STARTED;
    }

    public void completeTrip() {
        status = TripStatus.COMPLETED;
        cab.getDriver().setStatus(DriverStatus.AVAILABLE);
        cab.setLocation(destination);
    }

    public void cancelTrip() {
        status = TripStatus.CANCELLED;
        cab.getDriver().setStatus(DriverStatus.AVAILABLE);
    }
}

class CabRepository {
    private List<Cab> cabs = new ArrayList<>();

    public void addCab(Cab cab) {
        cabs.add(cab);
    }

    public Cab findNearestAvailableCab(Location pickup, CabType type) {
        Cab result = null;
        double minDistance = Double.MAX_VALUE;

        for (Cab cab : cabs) {
            if (cab.getType() == type &&
                    cab.getDriver().getStatus() == DriverStatus.AVAILABLE) {

                double distance = cab.getLocation().distance(pickup);

                if (distance < minDistance) {
                    minDistance = distance;
                    result = cab;
                }
            }
        }

        return result;
    }
}

class TripRepository {
    private Map<Integer, Trip> trips = new HashMap<>();

    public void saveTrip(Trip trip) {
        trips.put(trip.getId(), trip);
    }

    public Trip getTrip(int id) {
        return trips.get(id);
    }
}

class BookingService {
    private CabRepository cabRepository;
    private TripRepository tripRepository;
    private AtomicInteger tripIdGenerator = new AtomicInteger(1);

    public BookingService(CabRepository cabRepository, TripRepository tripRepository) {
        this.cabRepository = cabRepository;
        this.tripRepository = tripRepository;
    }

    public Trip bookCab(Rider rider, Location pickup, Location destination, CabType type) {
        Cab cab = cabRepository.findNearestAvailableCab(pickup, type);

        if (cab == null) {
            return null;
        }

        cab.getDriver().setStatus(DriverStatus.ON_TRIP);

        Trip trip = new Trip(
                tripIdGenerator.getAndIncrement(),
                rider,
                cab,
                pickup,
                destination
        );

        tripRepository.saveTrip(trip);
        return trip;
    }

    public void startTrip(int tripId) {
        Trip trip = tripRepository.getTrip(tripId);
        if (trip != null) {
            trip.startTrip();
        }
    }

    public void completeTrip(int tripId) {
        Trip trip = tripRepository.getTrip(tripId);
        if (trip != null) {
            trip.completeTrip();
        }
    }

    public void cancelTrip(int tripId) {
        Trip trip = tripRepository.getTrip(tripId);
        if (trip != null) {
            trip.cancelTrip();
        }
    }
}

public class CabBookingSystem {
    public static void main(String[] args) {
        CabRepository cabRepository = new CabRepository();
        TripRepository tripRepository = new TripRepository();
        BookingService bookingService = new BookingService(cabRepository, tripRepository);

        Driver driver1 = new Driver(1, "John");
        Driver driver2 = new Driver(2, "David");

        cabRepository.addCab(new Cab(1, CabType.MINI, driver1, new Location(2, 3)));
        cabRepository.addCab(new Cab(2, CabType.SEDAN, driver2, new Location(5, 5)));

        Rider rider = new Rider(1, "Alice");

        Trip trip = bookingService.bookCab(
                rider,
                new Location(1, 1),
                new Location(10, 10),
                CabType.MINI
        );

        if (trip != null) {
            bookingService.startTrip(trip.getId());
            bookingService.completeTrip(trip.getId());
        }
    }
}