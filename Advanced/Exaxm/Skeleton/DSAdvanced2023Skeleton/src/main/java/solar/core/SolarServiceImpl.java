package solar.core;

import solar.models.Inverter;
import solar.models.PVModule;

import java.util.*;
import java.util.stream.Collectors;

public class SolarServiceImpl implements SolarService {
    private Map<String, Inverter> inverterById;
    private Map<String, List<String>> pvModulesByInverterId;
    private Map<String, PVModule> pvModulesByName;

    public SolarServiceImpl() {
        this.inverterById = new HashMap<>();
        this.pvModulesByInverterId = new HashMap<>();
        this.pvModulesByName = new HashMap<>();
    }

    @Override
    public void addInverter(Inverter inverter) {
        Inverter temp = tryGetInverter(inverter.id);

        if (temp != null) {
            throw new IllegalArgumentException("Inverter already exists. Cannot be added");
        }

        inverterById.put(inverter.id, inverter);
        pvModulesByInverterId.put(inverter.id, new ArrayList<>());
    }

    private Inverter tryGetInverter(String inverterId) {
        return inverterById.get(inverterId);
    }

    private PVModule tryGetPvModuleByName(String arrayId) {
        return pvModulesByName.get(arrayId);
    }

    @Override
    public void addArray(Inverter inverter, String arrayId) {
        Inverter temp = tryGetInverter(inverter.id);
        PVModule tempPVmodule = tryGetPvModuleByName(arrayId);

        if (temp == null || tempPVmodule != null) {
            throw new IllegalArgumentException("Inverter is missing or this arrayId is already in use");
        }

        pvModulesByName.put(arrayId, null);
    }

    private boolean inverterHasCapacity(Inverter inverter) {
        int capacity = inverter.maxPvArraysConnected;
        int currentNumPVModulesConnected = pvModulesByInverterId
                .get(inverter.id)
                .size();
        return capacity > currentNumPVModulesConnected;
    }

    @Override
    public void addPanel(Inverter inverter, String arrayId, PVModule pvModule) {
        Inverter tempInverter = tryGetInverter(inverter.id);

        if (tempInverter == null || !pvModulesByInverterId.containsKey(inverter.id) || isPanelConnected(pvModule)) {
            throw new IllegalArgumentException("Inverter is missing or array is not associated or PVModule is already in use");
        }

        pvModulesByInverterId.get(inverter.id).add(arrayId);
        pvModulesByName.put(arrayId, pvModule);
    }

    @Override
    public boolean containsInverter(String id) {
        return inverterById.containsKey(id);
    }

    @Override
    public boolean isPanelConnected(PVModule pvModule) {
        return pvModulesByName.containsValue(pvModule);
    }

    @Override
    public Inverter getInverterByPanel(PVModule pvModule) {
        for (Map.Entry<String, List<String>> entry : pvModulesByInverterId.entrySet()) {
            if (entry.getValue().stream().anyMatch(arrayId -> pvModule.equals(pvModulesByName.get(arrayId)))) {
                return tryGetInverter(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public void replaceModule(PVModule oldModule, PVModule newModule) {
        if (!isPanelConnected(oldModule) || pvModulesByName.containsValue(newModule)) {
            throw new IllegalArgumentException("Old module not in use or new module already in use");
        }

        String arrayId = null;
        for (Map.Entry<String, PVModule> entry : pvModulesByName.entrySet()) {
            if (oldModule.equals(entry.getValue())) {
                arrayId = entry.getKey();
                break;
            }
        }

        if (arrayId != null) {
            pvModulesByName.put(arrayId, newModule);
        }
    }

    @Override
    public Collection<Inverter> getByProductionCapacity() {
        List<Inverter> inverters = new ArrayList<>(inverterById.values());
        inverters.sort(Comparator.comparingInt(inverter -> {
            int totalProduction = pvModulesByInverterId.get(inverter.id).stream()
                    .mapToInt(arrayId -> Objects.requireNonNull(pvModulesByName.get(arrayId)).maxWattProduction)
                    .sum();
            return totalProduction;
        }));
        return inverters;
    }

    @Override
    public Collection<Inverter> getByNumberOfPVModulesConnected() {
        List<Inverter> inverters = new ArrayList<>(inverterById.values());
        inverters.sort(Comparator.comparingInt(inverter -> {
            int numModules = pvModulesByInverterId.get(inverter.id).size();
            int numArrays = pvModulesByInverterId.get(inverter.id).stream().distinct().collect(Collectors.toList()).size();
            return 1000 * numModules + numArrays;
        }));
        return inverters;
    }

    @Override
    public Collection<PVModule> getByWattProduction() {
        List<PVModule> modules = new ArrayList<>(pvModulesByName.values());
        modules.sort(Comparator.comparingInt(module -> module.maxWattProduction));
        return modules;
    }
}
