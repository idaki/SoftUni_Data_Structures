package renovation.core;

import renovation.models.Laminate;
import renovation.models.Tile;
import renovation.models.WoodType;

import java.util.*;
import java.util.stream.Collectors;

public class RenovationImpl implements Renovation {
    private final List<Tile> tiles;
    private final List<Laminate> laminates;
    private double tilesArea;

    public RenovationImpl() {
        tiles = new ArrayList<>();
        laminates = new ArrayList<>();
        tilesArea = 0;
    }

    @Override
    public void deliverTile(Tile tile) {
        double currentTileArea = tile.height * tile.width;
        if (currentTileArea + tilesArea > 30) {
            throw new IllegalArgumentException();
        }
        tiles.add(tile);
        tilesArea += currentTileArea;
    }

    @Override
    public void deliverFlooring(Laminate laminate) {
        laminates.add(laminate);
    }

    @Override
    public double getDeliveredTileArea() {
        return tilesArea;
    }

    @Override
    public boolean isDelivered(Laminate laminate) {
        return laminates.contains(laminate);
    }

    @Override
    public void returnTile(Tile tile) {
        if (!tiles.contains(tile)) {
            throw new IllegalArgumentException();
        }

        tiles.remove(tile);
        tilesArea -= tile.height * tile.width;
    }

    @Override
    public void returnLaminate(Laminate laminate) {
        int lastIndex = laminates.lastIndexOf(laminate);
        if (lastIndex == -1) {
            throw new IllegalArgumentException();
        }

        laminates.remove(lastIndex);
    }

    @Override
    public Collection<Laminate> getAllByWoodType(WoodType wood) {
        return laminates.stream()
                .filter(l -> l.woodType.equals(wood))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Tile> getAllTilesFitting(double width, double height) {
        return tiles.stream()
                .filter(t -> t.width <= width && t.height <= height)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Tile> sortTilesBySize() {
        return tiles.stream()
                .sorted(Comparator.comparingDouble((Tile t) -> t.width * t.height)
                        .thenComparing(t -> t.depth))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Laminate> layFlooring() {
        int lastIndex = laminates.size() - 1;
        return new Iterator<Laminate>() {
            private int currentIndex = lastIndex;

            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public Laminate next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return laminates.get(currentIndex--);
            }
        };
    }
}




