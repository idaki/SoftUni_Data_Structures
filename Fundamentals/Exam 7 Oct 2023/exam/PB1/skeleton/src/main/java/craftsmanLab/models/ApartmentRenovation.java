package craftsmanLab.models;

import java.time.LocalDate;

public class ApartmentRenovation {
    public String address;
    public double area;
    public double workHoursNeeded;
    public LocalDate deadline;

    public ApartmentRenovation(String address, double area, double workHoursNeeded, LocalDate deadline) {
        this.address = address;
        this.area = area;
        this.workHoursNeeded = workHoursNeeded;
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "ApartmentRenovation{" +
                "address='" + address + '\'' +
                ", area=" + area +
                ", workHoursNeeded=" + workHoursNeeded +
                ", deadline=" + deadline +
                '}';
    }
}
