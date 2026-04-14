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