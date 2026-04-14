import java.util.*;

enum SpotType {
    SMALL, MEDIUM, LARGE
}

abstract class Vehicle {
    String licensePlate;
    SpotType requiredSpotType;

    public Vehicle(String licensePlate, SpotType requiredSpotType) {
        this.licensePlate = licensePlate;
        this.requiredSpotType = requiredSpotType;
    }
}

class Car extends Vehicle {
    Car(String licensePlate) {
        super(licensePlate, SpotType.MEDIUM);
    }
}

class Bike extends Vehicle {
    Bike(String licensePlate) {
        super(licensePlate, SpotType.SMALL);
    }
}

class Bus extends Vehicle {
    Bus(String licensePlate) {
        super(licensePlate, SpotType.LARGE);
    }
}

class ParkingSpot {
    int spotNumber;
    SpotType type;
    boolean isOccupied;
    Vehicle parkedVehicle;

    ParkingSpot(int spotNumber, SpotType type) {
        this.spotNumber = spotNumber;
        this.type = type;
        this.isOccupied = false;
        this.parkedVehicle = null;
    }

    boolean canFitVehicle(Vehicle v) {
        return !isOccupied && v.requiredSpotType == this.type;
    }

    void park(Vehicle v) {
        this.parkedVehicle = v;
        this.isOccupied = true;
    }

    void vacate() {
        this.parkedVehicle = null;
        this.isOccupied = false;
    }
}

class ParkingFloor {
    int floorNumber;
    List<ParkingSpot> spots;

    ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }
}

class Ticket {
    String ticketId;
    Vehicle vehicle;
    ParkingSpot spot;
    Date entryTime;
    Date exitTime;

    Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = new Date();
    }
}

class ParkingLot {
    List<ParkingFloor> floors;
    Map<String, Ticket> activeTickets;

    ParkingLot(List<ParkingFloor> floors) {
        this.floors = floors;
        this.activeTickets = new HashMap<>();
    }

    Ticket parkVehicle(Vehicle v) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.spots) {
                if (spot.canFitVehicle(v)) {
                    spot.park(v);
                    String ticketId = UUID.randomUUID().toString();
                    Ticket ticket = new Ticket(ticketId, v, spot);
                    activeTickets.put(ticketId, ticket);
                    return ticket;
                }
            }
        }
        return null;
    }

    void unparkVehicle(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket != null) {
            ticket.spot.vacate();
            ticket.exitTime = new Date();
            activeTickets.remove(ticketId);
            long duration = (ticket.exitTime.getTime() - ticket.entryTime.getTime()) / 1000;
            System.out.println("Vehicle parked for " + duration + " seconds. Spot freed!");
        } else {
            System.out.println("Invalid ticket!");
        }
    }
}

class ParkingSpot {
    int spotNumber;
    SpotType type;
    boolean isOccupied;
    Vehicle parkedVehicle;

    ParkingSpot(int spotNumber, SpotType type) {
        this.spotNumber = spotNumber;
        this.type = type;
        this.isOccupied = false;
        this.parkedVehicle = null;
    }

    boolean canFitVehicle(Vehicle v) {
        return !isOccupied && v.requiredSpotType == this.type;
    }

    void park(Vehicle v) {
        this.parkedVehicle = v;
        this.isOccupied = true;
    }

    void vacate() {
        this.parkedVehicle = null;
        this.isOccupied = false;
    }
}

class ParkingFloor {
    int floorNumber;
    List<ParkingSpot> spots;

    ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }
}

class Ticket {
    String ticketId;
    Vehicle vehicle;
    ParkingSpot spot;
    Date entryTime;
    Date exitTime;

    Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = new Date();
    }
}

class ParkingLot {
    List<ParkingFloor> floors;
    Map<String, Ticket> activeTickets;

    ParkingLot(List<ParkingFloor> floors) {
        this.floors = floors;
        this.activeTickets = new HashMap<>();
    }

    Ticket parkVehicle(Vehicle v) {
        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.spots) {
                if (spot.canFitVehicle(v)) {
                    spot.park(v);
                    String ticketId = UUID.randomUUID().toString();
                    Ticket ticket = new Ticket(ticketId, v, spot);
                    activeTickets.put(ticketId, ticket);
                    return ticket;
                }
            }
        }
        return null;
    }

    void unparkVehicle(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket != null) {
            ticket.spot.vacate();
            ticket.exitTime = new Date();
            activeTickets.remove(ticketId);
            long duration = (ticket.exitTime.getTime() - ticket.entryTime.getTime()) / 1000;
            System.out.println("Vehicle parked for " + duration + " seconds. Spot freed!");
        } else {
            System.out.println("Invalid ticket!");
        }
    }
}

