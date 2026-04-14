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