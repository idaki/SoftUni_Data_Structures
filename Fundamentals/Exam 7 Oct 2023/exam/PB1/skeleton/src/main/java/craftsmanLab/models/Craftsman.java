package craftsmanLab.models;

public class Craftsman {
    public String name;
    public double hourlyRate;
    public double totalEarnings;

    public Craftsman(String name, double hourlyRate, double totalEarnings) {
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.totalEarnings = totalEarnings;
    }

    @Override
    public String toString() {
        return "Craftsman{" +
                "name='" + name + '\'' +
                ", hourlyRate=" + hourlyRate +
                ", totalEarnings=" + totalEarnings +
                '}';
    }
}
