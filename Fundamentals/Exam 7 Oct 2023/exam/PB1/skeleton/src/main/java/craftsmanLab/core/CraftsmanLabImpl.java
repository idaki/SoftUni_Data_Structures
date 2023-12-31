package craftsmanLab.core;

import craftsmanLab.models.ApartmentRenovation;
import craftsmanLab.models.Craftsman;

import java.util.*;
import java.util.stream.Collectors;

public class CraftsmanLabImpl implements CraftsmanLab {
    private Map<String, ApartmentRenovation> jobs;
    private Map<String, Craftsman> craftsmen;

    private PriorityQueue<Craftsman> lowestEarningCraftsman;
    private Map<ApartmentRenovation, Craftsman> craftsmanByJob;
    private Map<ApartmentRenovation, Double> jobByCost;

    public CraftsmanLabImpl() {
        jobs = new LinkedHashMap<>();
        craftsmen = new LinkedHashMap<>();
        lowestEarningCraftsman = new PriorityQueue<>(Comparator.comparingDouble(c -> c.totalEarnings));
        craftsmanByJob = new LinkedHashMap<>();
        jobByCost = new LinkedHashMap<>();
    }

    @Override
    public void addApartment(ApartmentRenovation job) {
        if (exists(job)) {
            throw new IllegalArgumentException();
        }
        jobs.put(job.address, job);

    }

    @Override
    public void addCraftsman(Craftsman craftsman) {
        if (exists(craftsman)) {
            throw new IllegalArgumentException();
        }
        craftsmen.put(craftsman.name, craftsman);
        lowestEarningCraftsman.offer(craftsman);
    }

    @Override
    public boolean exists(ApartmentRenovation job) {
        String address = job.address;
        return jobs.containsKey(address);
    }

    @Override
    public boolean exists(Craftsman craftsman) {
        return craftsmen.containsKey(craftsman.name);
    }

    @Override
    public void removeCraftsman(Craftsman craftsman) {
        if (!craftsmen.containsKey(craftsman.name)) {
            throw new IllegalArgumentException();
        }

        craftsmen.remove(craftsman.name);
    }

    @Override
    public Collection<Craftsman> getAllCraftsmen() {
        return craftsmen.values();
    }

    @Override
    public void assignRenovations() {
        for (ApartmentRenovation job : jobs.values()) {
            if (lowestEarningCraftsman.isEmpty()) {
                break;
            }
            Craftsman lowestEarningsCraftsman = getLowestEarningsCraftsman();

            double cost = job.workHoursNeeded * lowestEarningsCraftsman.hourlyRate;
            lowestEarningsCraftsman.totalEarnings += cost;
            lowestEarningCraftsman.offer(lowestEarningsCraftsman);
            craftsmanByJob.put(job, lowestEarningsCraftsman);
        }
    }


    private Craftsman getLowestEarningsCraftsman() {
        return lowestEarningCraftsman.poll();
    }

    @Override
    public Craftsman getContractor(ApartmentRenovation job) {
        Craftsman craftsman = craftsmanByJob.get(job);
        if (craftsman == null) {
            throw new IllegalArgumentException();
        }
        return craftsman;
    }

    @Override
    public Craftsman getLeastProfitable() {
        return getLowestEarningsCraftsman();
    }

    @Override
    public Collection<ApartmentRenovation> getApartmentsByRenovationCost() {
        jobByCost.clear();
        for (ApartmentRenovation job : jobs.values()) {
            Craftsman craftsman = craftsmanByJob.getOrDefault(job, new Craftsman("", 0.0, 0.0));
            double cost = job.workHoursNeeded * craftsman.hourlyRate;
            jobByCost.put(job, cost);
        }

        return jobByCost.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ApartmentRenovation> getMostUrgentRenovations(int count) {
        return jobs.values().stream()
                .sorted(Comparator.comparing(j -> j.deadline))
                .limit(count)
                .collect(Collectors.toList());
    }
}
