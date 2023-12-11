import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day11B {

    public static class Image {

        private final char[][] map;
        private final List<Integer> emptyRows;
        private final List<Integer> emptyColumns;

        public Image(char[][] map) {
            this.map = map;
            this.emptyRows = emptyRows();
            this.emptyColumns = emptyColumns();
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
                int minY = Math.min(y, other.y);
                int maxY = Math.max(y, other.y);
                int minX = Math.min(x, other.x);
                int maxX = Math.max(x, other.x);

                long emptyRowsPassed = emptyRows.stream().filter(c -> c > minY && c < maxY).count();
                long emptyColumnsPassed = emptyColumns.stream().filter(c -> c > minX && c < maxX).count();

                int diffY = Math.abs(y - other.y);
                int diffX = Math.abs(x - other.x);

                return diffY + diffX + 999_999 * (emptyRowsPassed + emptyColumnsPassed);
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

        long totalDistance = 0;

        List<Image.Space> galaxies = map.findGalaxies();
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
