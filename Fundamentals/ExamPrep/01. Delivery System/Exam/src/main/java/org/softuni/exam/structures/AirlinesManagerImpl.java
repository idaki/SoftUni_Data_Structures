package org.softuni.exam.structures;

import org.softuni.exam.entities.Airline;
import org.softuni.exam.entities.Flight;

import java.util.*;
import java.util.stream.Collectors;

public class AirlinesManagerImpl implements AirlinesManager {
    private Map<String, Airline> airlines;
    private Map<String, List<Flight>> flightsByAirline;
    private Map<String, List<Flight>> completedFlights;

    private Set<String> flightIds;

    public AirlinesManagerImpl() {
        this.airlines = new LinkedHashMap<>();
        this.flightsByAirline = new LinkedHashMap<>();
        this.completedFlights = new HashMap<>();
        this.flightIds = new HashSet<>();
    }


    @Override
    public void addAirline(Airline airline) {
        airlines.putIfAbsent(airline.getId(), airline);
    }

    @Override
    public void addFlight(Airline airline, Flight flight) {
        if (!airlines.containsKey(airline.getId())) {
            throw new IllegalArgumentException("Airline does not exist.");
        }

        flightsByAirline.computeIfAbsent(airline.getId(), k -> new ArrayList<>());
        flightsByAirline.get(airline.getId()).add(flight);

        // Add the flight ID to the HashSet
        flightIds.add(flight.getId());
    }

    @Override
    public boolean contains(Airline airline) {
        return airlines.containsKey(airline.getId());
    }

    @Override
    public boolean contains(Flight flight) {
        // Check if the flight ID is in the HashSet for quick lookup
        return flightIds.contains(flight.getId());
    }

    @Override
    public void deleteAirline(Airline airline) {
        if (!airlines.containsKey(airline.getId())) {
            throw new IllegalArgumentException("Airline does not exist.");
        }

        List<Flight> airlineFlights = flightsByAirline.remove(airline.getId());
        completedFlights.values().forEach(flightList -> flightList.removeAll(airlineFlights));
        airlines.remove(airline.getId());
    }

    @Override
    public Iterable<Flight> getAllFlights() {
        return flightsByAirline.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Flight performFlight(Airline airline, Flight flight) {
        if (!flightsByAirline.containsKey(airline.getId())) {
            throw new IllegalArgumentException("Airline does not exist.");
        }

        List<Flight> airlineFlights = flightsByAirline.get(airline.getId());
        if (!airlineFlights.contains(flight)) {
            throw new IllegalArgumentException("Flight does not exist for this airline.");
        }

        Flight currentFlight = airlineFlights.stream()
                .filter(f -> f.getId().equals(flight.getId()))
                .findFirst()
                .orElse(null);
        currentFlight.setCompleted(true);

        completedFlights.computeIfAbsent(airline.getId(), k -> new ArrayList<>());
        completedFlights.get(airline.getId()).add(currentFlight);

        return currentFlight;
    }

    @Override
    public Iterable<Flight> getCompletedFlights() {
        return completedFlights.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Flight> getFlightsOrderedByNumberThenByCompletion() {
        List<Flight> allFlights = new ArrayList<>();
        for (Flight flight : getAllFlights()) {
            allFlights.add(flight);
        }

        return allFlights.stream()
                .sorted(Comparator.comparing(Flight::isCompleted)
                        .thenComparing(Flight::getNumber))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Airline> getAirlinesOrderedByRatingThenByCountOfFlightsThenByName() {
        return airlines.values().stream()
                .sorted(Comparator
                        .comparing(Airline::getRating)
                        .reversed()
                        .thenComparing(airline -> flightsByAirline.getOrDefault(airline.getId(), Collections.emptyList()).size())
                        .reversed()
                        .thenComparing(Airline::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Airline> getAirlinesWithFlightsFromOriginToDestination(String origin, String destination) {
        return airlines.values().stream()
                .filter(airline -> flightsByAirline.getOrDefault(airline.getId(), Collections.emptyList())
                        .stream()
                        .anyMatch(flight -> !flight.isCompleted()
                                && flight.getOrigin().equals(origin)
                                && flight.getDestination().equals(destination)))
                .collect(Collectors.toList());
    }
}
