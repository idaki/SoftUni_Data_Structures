package org.softuni.exam.structures;

import org.softuni.exam.entities.Deliverer;
import org.softuni.exam.entities.Package;

import java.util.*;
import java.util.stream.Collectors;

public class DeliveriesManagerImpl implements DeliveriesManager {
    private Map<String, Deliverer> deliverers;
    private Map<String, Package> packeges;
    private Map<String, Package > assignedPackages;
    private Map<String, Package> unassignedPackages;
    private Map<String, Integer> packegesByDelivere;

    public DeliveriesManagerImpl() {
        this.deliverers = new LinkedHashMap<>();
        this.packeges = new LinkedHashMap<>();
        this.assignedPackages = new LinkedHashMap<>();
        this.unassignedPackages = new LinkedHashMap<>();
        this.packegesByDelivere = new HashMap<>();
    }

    @Override
    public void addDeliverer(Deliverer deliverer) {
        if (!deliverers.containsKey(deliverer.getId())) {
            deliverers.put(deliverer.getId(), deliverer);
            packegesByDelivere.put(deliverer.getId(), 0);
        }
    }

    @Override
    public void addPackage(Package _package) {
        if (!packeges.containsKey(_package.getId())) {
            packeges.put(_package.getId(), _package);
            unassignedPackages.put(_package.getId(), _package);
        }
    }

    @Override
    public boolean contains(Deliverer deliverer) {

        return deliverers.containsKey(deliverer.getId());
    }

    @Override
    public boolean contains(Package _package) {
        if (packeges.containsKey(_package.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public Iterable<Deliverer> getDeliverers() {
        return deliverers.values();
    }

    @Override
    public Iterable<Package> getPackages() {
        return new ArrayList<>(packeges.values());
    }

    @Override
    public void assignPackage(Deliverer deliverer, Package _package) throws IllegalArgumentException {
        if (!deliverers.containsKey(deliverer.getId())) {
            throw new IllegalArgumentException("Deliverer not found");
        }
        assignedPackages.put(_package.getId(), _package);
        unassignedPackages.remove(_package.getId());
        int currentCountPackages = packegesByDelivere.get(deliverer.getId());
        packegesByDelivere.put(deliverer.getId(), currentCountPackages+1);
    }

    @Override
    public Iterable<Package> getUnassignedPackages() {
        return unassignedPackages.values();
    }

    @Override
    public Iterable<Package> getPackagesOrderedByWeightThenByReceiver() {

        return packeges.values().stream()
                .sorted(Comparator.comparing(Package::getWeight)
                        .reversed()
                        .thenComparing(Comparator.comparing(Package::getReceiver)))
                .collect(Collectors.toList());



    }

    @Override
    public Iterable<Deliverer> getDeliverersOrderedByCountOfPackagesThenByName() {

        return deliverers.values().stream()
                .sorted(Comparator.comparing((Deliverer d)->packegesByDelivere.get(d.getId()))
                        .reversed()
                        .thenComparing(Deliverer::getName))
                .collect(Collectors.toList());
    }
}
