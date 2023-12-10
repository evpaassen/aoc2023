import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Day10A {

    public static class TileMap {

        private final char[][] map;

        public TileMap(char[][] map) {
            this.map = map;
        }

        public Tile findStart() {
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    Tile t = new Tile(y, x);
                    if (t.value() == 'S') {
                        return t;
                    }
                }
            }

            throw new IllegalArgumentException("Map has no starting position");
        }

        private List<Tile> findLoop() {
            Tile start = findStart();

            Set<TileMap.Tile> visited = new HashSet<>();
            Stack<TileMap.Tile> path = new Stack<>();
            TileMap.Tile currentTile = start;
            while (path.isEmpty() || !start.equals(currentTile)) {
                visited.add(currentTile);

                List<TileMap.Tile> nextSteps = currentTile.step()
                        .filter(t -> t.equals(start) && path.size() > 1 || !visited.contains(t)).toList();

                if (nextSteps.isEmpty()) {
                    path.pop();
                    currentTile = path.peek();
                } else {
                    path.add(currentTile);
                    currentTile = nextSteps.get(0);
                }
            }

            return List.copyOf(path);
        }

        public class Tile {

            final int y, x;

            public Tile(int y, int x) {
                this.y = y;
                this.x = x;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Tile tile = (Tile) o;
                return y == tile.y && x == tile.x;
            }

            @Override
            public int hashCode() {
                return Objects.hash(y, x);
            }

            public char value() {
                return map[y][x];
            }

            public Stream<Tile> step() {
                Stream<Tile> nextSteps;
                switch (value()) {
                    case '|':
                        nextSteps = Stream.of(
                                new Tile(y - 1, x),
                                new Tile(y + 1, x)
                        );
                        break;

                    case '-':
                        nextSteps = Stream.of(
                                new Tile(y, x - 1),
                                new Tile(y, x + 1)
                        );
                        break;

                    case 'L':
                        nextSteps = Stream.of(
                                new Tile(y - 1, x),
                                new Tile(y, x + 1)
                        );
                        break;

                    case 'J':
                        nextSteps = Stream.of(
                                new Tile(y - 1, x),
                                new Tile(y, x - 1)
                        );
                        break;

                    case '7':
                        nextSteps = Stream.of(
                                new Tile(y + 1, x),
                                new Tile(y, x - 1)
                        );
                        break;

                    case 'F':
                        nextSteps = Stream.of(
                                new Tile(y + 1, x),
                                new Tile(y, x + 1)
                        );
                        break;

                    case 'S':
                        nextSteps = Stream.of(
                                new Tile(y - 1, x),
                                new Tile(y + 1, x),
                                new Tile(y, x - 1),
                                new Tile(y, x + 1)
                        );
                        break;

                    case '.':
                    default:
                        nextSteps = Stream.of();
                }

                return nextSteps.filter(Tile::isValid).filter(Tile::isPipe);
            }

            public boolean isValid() {
                return y >= 0 && y < map.length && x >= 0 && x < map[y].length;
            }

            public boolean isPipe() {
                return value() != '.';
            }
        }
    }

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day10.txt"));

        var map = new TileMap(reader.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new));
        
        List<TileMap.Tile> loop = map.findLoop();

        System.out.println("Max steps: " + (int) Math.ceil((loop.size() - 1) / 2.0));
    }
}
