import java.time.LocalDateTime;
import java.util.*;

enum VehicleType {
    BIKE,
    CAR,
    TRUCK
}

enum SpotType {
    BIKE,
    CAR,
    TRUCK
}

enum TicketStatus {
    ACTIVE,
    PAID
}

abstract class Vehicle {
    private final String licenseNumber;
    private final VehicleType type;

    protected Vehicle(String licenseNumber, VehicleType type) {
        this.licenseNumber = licenseNumber;
        this.type = type;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public VehicleType getType() {
        return type;
    }
}

class Bike extends Vehicle {
    public Bike(String licenseNumber) {
        super(licenseNumber, VehicleType.BIKE);
    }
}

class Car extends Vehicle {
    public Car(String licenseNumber) {
        super(licenseNumber, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    public Truck(String licenseNumber) {
        super(licenseNumber, VehicleType.TRUCK);
    }
}

class ParkingSpot {
    private final String id;
    private final SpotType type;
    private Vehicle vehicle;

    public ParkingSpot(String id, SpotType type) {
        this.id = id;
        this.type = type;
    }

    public synchronized boolean parkVehicle(Vehicle vehicle) {
        if (isOccupied()) {
            return false;
        }

        if (!canFitVehicle(vehicle)) {
            return false;
        }

        this.vehicle = vehicle;
        return true;
    }

    public synchronized void removeVehicle() {
        this.vehicle = null;
    }

    public boolean isOccupied() {
        return vehicle != null;
    }

    public boolean canFitVehicle(Vehicle vehicle) {
        return switch (vehicle.getType()) {
            case BIKE -> type == SpotType.BIKE;
            case CAR -> type == SpotType.CAR;
            case TRUCK -> type == SpotType.TRUCK;
        };
    }

    public String getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}

class Floor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public Optional<ParkingSpot> getAvailableSpot(Vehicle vehicle) {
        return spots.stream()
                .filter(spot -> !spot.isOccupied())
                .filter(spot -> spot.canFitVehicle(vehicle))
                .findFirst();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }
}

class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus status;

    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }

    public void closeTicket() {
        this.exitTime = LocalDateTime.now();
        this.status = TicketStatus.PAID;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public TicketStatus getStatus() {
        return status;
    }
}

class Entrance {
    private final String id;

    public Entrance(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class Exit {
    private final String id;

    public Exit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

class ParkingLot {
    private final String name;
    private final List<Floor> floors;
    private final Map<String, ParkingTicket> activeTickets;

    public ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.activeTickets = new HashMap<>();
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
    }

    public synchronized ParkingTicket parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors) {
            Optional<ParkingSpot> optionalSpot = floor.getAvailableSpot(vehicle);

            if (optionalSpot.isPresent()) {
                ParkingSpot spot = optionalSpot.get();
                boolean parked = spot.parkVehicle(vehicle);

                if (parked) {
                    String ticketId = UUID.randomUUID().toString();
                    ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot);
                    activeTickets.put(ticketId, ticket);
                    return ticket;
                }
            }
        }

        throw new RuntimeException("Parking lot is full");
    }

    public synchronized void exitVehicle(String ticketId) {
        ParkingTicket ticket = activeTickets.get(ticketId);

        if (ticket == null) {
            throw new RuntimeException("Invalid ticket");
        }

        ticket.getSpot().removeVehicle();
        ticket.closeTicket();
        activeTickets.remove(ticketId);
    }

    public void displayAvailability() {
        for (Floor floor : floors) {
            System.out.println("Floor: " + floor.getFloorNumber());

            for (ParkingSpot spot : floor.getSpots()) {
                System.out.println(
                        "SpotId=" + spot.getId() +
                                ", Type=" + spot.getType() +
                                ", Occupied=" + spot.isOccupied()
                );
            }
        }
    }

    public String getName() {
        return name;
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot("City Mall");

        Floor floor1 = new Floor(1);
        floor1.addSpot(new ParkingSpot("F1-S1", SpotType.BIKE));
        floor1.addSpot(new ParkingSpot("F1-S2", SpotType.CAR));

        Floor floor2 = new Floor(2);
        floor2.addSpot(new ParkingSpot("F2-S1", SpotType.TRUCK));
        floor2.addSpot(new ParkingSpot("F2-S2", SpotType.CAR));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        Vehicle car = new Car("KA01AB1234");
        Vehicle bike = new Bike("KA02XY9999");

        ParkingTicket carTicket = parkingLot.parkVehicle(car);
        ParkingTicket bikeTicket = parkingLot.parkVehicle(bike);

        parkingLot.displayAvailability();

        parkingLot.exitVehicle(carTicket.getTicketId());

        System.out.println("After Exit:");

        parkingLot.displayAvailability();
    }
}