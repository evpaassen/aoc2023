import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Day10B {

    public static class TileMap {

        private final char[][] map;
        private final Tile start;
        private final List<Tile> loop;


        public TileMap(char[][] map) {
            this.map = map;
            this.start = findStart();
            this.loop = findLoop();
            reconstructStartTile();
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
            Set<Tile> visited = new HashSet<>();
            Stack<Tile> path = new Stack<>();
            Tile currentTile = start;
            while (path.isEmpty() || !start.equals(currentTile)) {
                visited.add(currentTile);

                List<Tile> nextSteps = currentTile.step()
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

        public void reconstructStartTile() {
            Tile next = loop.get(1);
            Tile prev = loop.get(loop.size() - 1);

            char[][] matrix = new char[][] {
                    new char[] { '.', '.', '.' },
                    new char[] { '.', '.', '.' },
                    new char[] { '.', '.', '.' }
            };
            matrix[1][1] = 'X';
            matrix[next.y - start.y + 1][next.x - start.x + 1] = 'X';
            matrix[prev.y - start.y + 1][prev.x - start.x + 1] = 'X';

            String minimap = new String(matrix[0]) + "\n" + new String(matrix[1]) + "\n"+ new String(matrix[2]);

            System.out.println("\n" + minimap + "\n");

            switch (minimap) {
                case """
                        .X.
                        .X.
                        .X.""":
                    map[start.y][start.x] = '|';
                    break;

                case """
                     ...
                     XXX
                     ...""":
                    map[start.y][start.x] = '-';
                    break;

                case """
                     ...
                     .XX
                     .X.""":
                    map[start.y][start.x] = 'F';
                    break;

                case """
                     .X.
                     .XX
                     ...""":
                    map[start.y][start.x] = 'L';
                    break;

                case """
                     .X.
                     XX.
                     ...""":
                    map[start.y][start.x] = 'J';
                    break;

                case """
                     ...
                     XX.
                     .X.""":
                    map[start.y][start.x] = '7';
                    break;

                default:
                    throw new IllegalArgumentException("Unable to reconstruct start tile");
            }
        }

        public int enclosedTiles() {
            int enclosedTiles = 0;

            Set<Tile> loop = new HashSet<>(this.loop);

            for (int y = 0; y < map.length; y++) {
                boolean inside = false;
                boolean inHorizontalPipe = false;
                boolean leftCornerDown = false;

                for (int x = 0; x < map[y].length; x++) {
                    Tile t = new Tile(y, x);
                    if (inHorizontalPipe) {
                        if (t.isRightCorner()) {
                            boolean rightCornerDown = t.value() == '7';
                            if (leftCornerDown != rightCornerDown) {
                                inside = !inside;
                            }
                            inHorizontalPipe = false;
                        }
                    } else if (t.isLeftCorner() && loop.contains(t)) {
                        leftCornerDown = t.value() == 'F';
                        inHorizontalPipe = true;
                    } else if (t.value() == '|' && loop.contains(t)) {
                        inside = !inside;
                    } else if (inside) {
                        map[y][x] = 'I';
                        enclosedTiles++;
                    }
                }
            }

            return enclosedTiles;
        }

//        public TileMap filterLoop() {
//            char[][] filteredMap = new char[map.length][map[0].length];
//
//            Set<Tile> loop = new HashSet<>(this.loop);
//
//            for (int y = 0; y < filteredMap.length; y++) {
//                for (int x = 0; x < filteredMap[0].length; x++) {
//                    Tile t = new Tile(y, x);
//                    filteredMap[y][x] = loop.contains(t) ? map[y][x] : '.';
//                }
//            }
//
//            return new TileMap(filteredMap);
//        }

//        public void print() {
//            Arrays.stream(map).forEach(l -> System.out.println(new String(l)));
//        }

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
                Stream<Tile> nextSteps = switch (value()) {
                    case '|' -> Stream.of(
                            new Tile(y - 1, x),
                            new Tile(y + 1, x)
                    );
                    case '-' -> Stream.of(
                            new Tile(y, x - 1),
                            new Tile(y, x + 1)
                    );
                    case 'L' -> Stream.of(
                            new Tile(y - 1, x),
                            new Tile(y, x + 1)
                    );
                    case 'J' -> Stream.of(
                            new Tile(y - 1, x),
                            new Tile(y, x - 1)
                    );
                    case '7' -> Stream.of(
                            new Tile(y + 1, x),
                            new Tile(y, x - 1)
                    );
                    case 'F' -> Stream.of(
                            new Tile(y + 1, x),
                            new Tile(y, x + 1)
                    );
                    case 'S' -> Stream.of(
                            new Tile(y - 1, x),
                            new Tile(y + 1, x),
                            new Tile(y, x - 1),
                            new Tile(y, x + 1)
                    );
                    default -> Stream.of();
                };

                return nextSteps.filter(Tile::isValid).filter(Tile::isPipe);
            }

            public boolean isValid() {
                return y >= 0 && y < map.length && x >= 0 && x < map[y].length;
            }

            public boolean isPipe() {
                return value() != '.';
            }

            public boolean isLeftCorner() {
                return value() == 'L' || value() == 'F';
            }

            public boolean isRightCorner() {
                return value() == '7' || value() == 'J';
            }
        }
    }

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day10.txt"));

        var map = new TileMap(reader.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new));

//        map.print();
//        System.out.println("\n");

//        TileMap filteredMap = map.filterLoop();
//        filteredMap.print();
//        System.out.println("\n");

        System.out.println("Enclosed tiles: " + map.enclosedTiles());
    }
}
