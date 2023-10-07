package craftsmanLab.core;

import craftsmanLab.models.ApartmentRenovation;
import craftsmanLab.models.Craftsman;

import java.util.*;
import java.util.stream.Collectors;

public class CraftsmanLabImpl implements CraftsmanLab {
    private final List<ApartmentRenovation> jobs;
    private final List<Craftsman> craftsmen;
    private final Map<ApartmentRenovation, Craftsman> craftsmanByJob;
    private final Map<ApartmentRenovation, Double> jobByCost;

    public CraftsmanLabImpl() {
        jobs = new ArrayList<>();
        craftsmen = new ArrayList<>();
        craftsmanByJob = new LinkedHashMap<>();
        jobByCost = new LinkedHashMap<>();
    }

    @Override
    public void addApartment(ApartmentRenovation job) {
        if (exists(job)) {
            throw new IllegalArgumentException();
        }
        jobs.add(job);
    }

    @Override
    public void addCraftsman(Craftsman craftsman) {
        if (exists(craftsman)) {
            throw new IllegalArgumentException();
        }
        craftsmen.add(craftsman);
    }

    @Override
    public boolean exists(ApartmentRenovation job) {
        return jobs.contains(job);
    }

    @Override
    public boolean exists(Craftsman craftsman) {
        return craftsmen.contains(craftsman);
    }

    @Override
    public void removeCraftsman(Craftsman craftsman) {
        if (!craftsmen.contains(craftsman)) {
            throw new IllegalArgumentException();
        }

        if (craftsmanByJob.containsValue(craftsman)) {
            throw new IllegalArgumentException();
        }

        craftsmen.remove(craftsman);
    }

    @Override
    public Collection<Craftsman> getAllCraftsmen() {
        return new ArrayList<>(craftsmen);
    }

    @Override
    public void assignRenovations() {
        for (ApartmentRenovation job : jobs) {
            if (!craftsmanByJob.containsKey(job)) {
                Craftsman lowestEarningsCraftsman = craftsmen.stream()
                        .min(Comparator.comparingDouble(c -> c.totalEarnings))
                        .orElseThrow(IllegalStateException::new);

                double cost = job.workHoursNeeded * lowestEarningsCraftsman.hourlyRate;
                lowestEarningsCraftsman.totalEarnings += cost;
                craftsmanByJob.put(job, lowestEarningsCraftsman);
            }
        }
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
        return craftsmen.stream()
                .min(Comparator.comparingDouble(c->c.totalEarnings))
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    public Collection<ApartmentRenovation> getApartmentsByRenovationCost() {
        jobByCost.clear();
        for (ApartmentRenovation job : jobs) {
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
        return jobs.stream()
                .sorted(Comparator.comparing(j->j.deadline))
                .limit(count)
                .collect(Collectors.toList());
    }
    }
