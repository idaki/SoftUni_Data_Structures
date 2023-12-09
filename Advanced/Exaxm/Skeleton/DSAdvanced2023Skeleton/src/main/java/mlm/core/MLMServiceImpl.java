package mlm.core;

import mlm.models.Seller;

import java.util.*;

public class MLMServiceImpl implements MLMService {
    private Map<String, Seller> sellers = new HashMap<>();
    private Map<String, String> hierarchy = new HashMap<>();

    @Override
    public void addSeller(Seller seller) {
        if (exists(seller)) {
            throw new IllegalArgumentException("Seller already exists");
        }
        sellers.put(seller.id, seller);
    }

    @Override
    public void hireSeller(Seller parent, Seller newHire) {
        if (!exists(parent) || exists(newHire)) {
            throw new IllegalArgumentException("Invalid parent or new hire");
        }

        parent = findSeller(parent.id);
        hierarchy.put(newHire.id, parent.id);
        sellers.put(newHire.id, newHire);
    }

    @Override
    public boolean exists(Seller seller) {
        return sellers.containsKey(seller.id);
    }

    @Override
    public void fire(Seller seller) {
        if (!exists(seller)) {
            throw new IllegalArgumentException("Seller does not exist");
        }

        seller = findSeller(seller.id);

        for (String hireId : hierarchy.keySet()) {
            if (hierarchy.get(hireId).equals(seller.id)) {
                // Transfer the hire to the seller who hired the current seller
                hierarchy.put(hireId, findHiringParent(seller.id).id);
            }
        }

        sellers.remove(seller.id);
    }

    private Seller findSeller(String sellerId) {
        return sellers.get(sellerId);
    }


    private Seller findHiringParent(String sellerId) {
        String parentId = hierarchy.get(sellerId);
        return sellers.get(parentId);
    }

    @Override
    public void makeSale(Seller seller, int amount) {
        if (!exists(seller)) {
            throw new IllegalArgumentException("Seller does not exist");
        }

        seller = findSeller(seller.id);
        int totalEarnings = amount;

        List<Seller> sellersToUpdate = new ArrayList<>();

        while (hierarchy.containsKey(seller.id)) {
            String parentId = hierarchy.get(seller.id);
            Seller parent = sellers.get(parentId);
            sellersToUpdate.add(parent);
            seller = parent;
        }

        // Update all sellers in batch
        int commission = (int) (amount * 0.05);
        for (Seller s : sellersToUpdate) {
            s.earnings += commission;
            totalEarnings -= commission;
        }


        seller.earnings += totalEarnings;
    }

    @Override
    public Collection<Seller> getByProfits() {
        List<Seller> sortedSellers = new ArrayList<>(sellers.values());
        sortedSellers.sort(Comparator.comparingInt(s -> s.earnings));
        Collections.reverse(sortedSellers);
        return sortedSellers;
    }

    @Override
    public Collection<Seller> getByEmployeeCount() {
        List<Seller> sortedSellers = new ArrayList<>(sellers.values());
        sortedSellers.sort(Comparator.comparingInt(s -> countEmployees(s.id)));
        Collections.reverse(sortedSellers);
        return sortedSellers;
    }

    @Override
    public Collection<Seller> getByTotalSalesMade() {
        List<Seller> sortedSellers = new ArrayList<>(sellers.values());
        sortedSellers.sort(Comparator.comparingInt(s -> -s.earnings));
        return sortedSellers;
    }

    private int countEmployees(String sellerId) {
        int count = 0;
        for (String hireId : hierarchy.keySet()) {
            if (hierarchy.get(hireId).equals(sellerId)) {
                count++;
            }
        }
        return count;
    }
}
