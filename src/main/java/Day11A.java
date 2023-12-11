import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day11A {

    public static class Image {

        private final char[][] map;

        public Image(char[][] map) {
            this.map = map;
        }

        public Image expand() {
            List<Integer> emptyRows = emptyRows();
            List<Integer> emptyColumns = emptyColumns();

            char[][] expandedMap = new char[map.length + emptyRows.size()][map[0].length + emptyColumns.size()];

            int origY = 0;
            for (int y = 0; y < expandedMap.length; y++) {
                expandRow(expandedMap, y, origY, emptyColumns);

                if (emptyRows.contains(origY)) {
                    y++;
                    expandRow(expandedMap, y, origY, emptyColumns);
                }

                origY++;
            }

            return new Image(expandedMap);
        }

        private void expandRow(char[][] expandedMap, int y, int origY, List<Integer> emptyColumns) {
            int origX = 0;
            for (int x = 0; x < expandedMap[0].length; x++) {
                char origValue = map[origY][origX];
                expandedMap[y][x] = origValue;

                if (emptyColumns.contains(origX)) {
                    x++;
                    expandedMap[y][x] = map[origY][origX];
                }

                origX++;
            }
        }

        private List<Integer> emptyRows() {
            List<Integer> emptyRows = new ArrayList<>();

            for (int y = 0; y < map.length; y++) {
                boolean allEmpty = true;

                for (int x = 0; x < map[y].length; x++) {
                    allEmpty &= map[y][x] == '.';
                }

                if (allEmpty) {
                    emptyRows.add(y);
                }
            }

            return emptyRows;
        }

        private List<Integer> emptyColumns() {
            List<Integer> emptyColumns = new ArrayList<>();

            for (int x = 0; x < map[0].length; x++) {
                boolean allEmpty = true;

                for (int y = 0; y < map.length; y++) {
                    allEmpty &= map[y][x] == '.';
                }

                if (allEmpty) {
                    emptyColumns.add(x);
                }
            }

            return emptyColumns;
        }

        public List<Space> findGalaxies() {
            List<Space> galaxies = new ArrayList<>();

            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (map[y][x] == '#') {
                        galaxies.add(new Space(y, x));
                    }
                }
            }

            return galaxies;
        }

        public void print() {
            Arrays.stream(map).forEach(l -> System.out.println(new String(l)));
        }

        public class Space {

            final int y, x;

            public Space(int y, int x) {
                this.y = y;
                this.x = x;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Space space = (Space) o;
                return y == space.y && x == space.x;
            }

            @Override
            public int hashCode() {
                return Objects.hash(y, x);
            }

            public char value() {
                return map[y][x];
            }

            @Override
            public String toString() {
                return "Space{" +
                        "y=" + y +
                        ", x=" + x +
                        '}';
            }

            public long distanceTo(Space other) {
                int diffY = Math.abs(y - other.y);
                int diffX = Math.abs(x - other.x);
                return diffY + diffX;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day11.txt"));

        var map = new Image(reader.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new));

        map.print();
        System.out.println("\n");

        Image expandedMap = map.expand();
        expandedMap.print();
        System.out.println("\n");

        long totalDistance = 0;

        List<Image.Space> galaxies = expandedMap.findGalaxies();
        int numberOfPaths = 0;
        for (int g = 0; g < galaxies.size(); g++) {
            for (int i = g + 1; i < galaxies.size(); i++) {
                totalDistance += galaxies.get(g).distanceTo(galaxies.get(i));
                numberOfPaths++;
            }
        }

        System.out.println("Number of galaxies: " + galaxies.size());
        System.out.println("Number of paths: " + numberOfPaths);
        System.out.println("Total distance: " + totalDistance);
    }
}
