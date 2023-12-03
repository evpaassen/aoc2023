import java.io.File;
import java.util.*;

public class Day3B {

    private record Range(int xStartInclusive, int xEndExclusive, int y) {}

    public static void main(String[] args) throws Exception {
        File file = new File("input/day3.txt");
        var scanner = new Scanner(file);

        int lines = 0;
        while (scanner.hasNextLine()) {
            lines++;
            scanner.nextLine();
        }
        scanner.close();

        long sum = 0;

        char[][] schematic = null;

        int l = 0;
        scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char[] chars = line.toCharArray();

            if (schematic == null) {
                schematic = new char[chars.length][lines];
            }

            schematic[l] = chars;

            l++;
        }

        for (int y = 0; y < lines; y++) {
            int w = schematic[y].length;

            for (int x = 0; x < w; x++) {
                char c = schematic[y][x];

                if (isGearSymbol(c)) {
                    List<Integer> adjacentNumbers = findAdjacentNumbers(schematic, x, y);

                    if (adjacentNumbers.size() == 2) {
                        sum += (long) adjacentNumbers.get(0) * (long) adjacentNumbers.get(1);
                    }
                }
            }
        }

        System.out.println("===============");
        System.out.println("Answer:" + sum);
    }

    private static List<Integer> findAdjacentNumbers(char[][] schematic, int x, int y) {
        Set<Range> ranges = new HashSet<>();

        // Above
        findNumberRange(schematic, x - 1, y - 1).ifPresent(ranges::add);
        findNumberRange(schematic, x , y - 1).ifPresent(ranges::add);
        findNumberRange(schematic, x + 1, y - 1).ifPresent(ranges::add);

        // Left
        findNumberRange(schematic, x - 1, y).ifPresent(ranges::add);

        // Right
        findNumberRange(schematic, x + 1, y).ifPresent(ranges::add);

        // Below
        findNumberRange(schematic, x - 1, y + 1).ifPresent(ranges::add);
        findNumberRange(schematic, x , y + 1).ifPresent(ranges::add);
        findNumberRange(schematic, x + 1, y + 1).ifPresent(ranges::add);

        return ranges.stream().map(r -> readNumber(schematic, r)).toList();
    }

    private static Optional<Range> findNumberRange(char[][] schematic, int x, int y) {
        if (!isDigit(schematic, x, y) || !isWithinBounds(schematic, 0, y)) {
            return Optional.empty();
        }

        int xStartInclusive = x;
        for (int i = x - 1; i >= 0; i--) {
            if (isDigit(schematic, i, y)) {
                xStartInclusive = i;
            } else {
                break;
            }
        }

        int xEndExclusive = x + 1;
        for (int i = x + 1; i < schematic[y].length; i++) {
            if (isDigit(schematic, i, y)) {
                xEndExclusive = i + 1;
            } else {
                break;
            }
        }


        return Optional.of(new Range(xStartInclusive, xEndExclusive, y));
    }

    private static Integer readNumber(char[][] schematic, Range r) {
        int number = 0;

        for (int x = r.xStartInclusive; x < r.xEndExclusive; x++) {
            number = number * 10 + Integer.parseInt("" + schematic[r.y][x]);
        }

        return number;
    }

    public static boolean isWithinBounds(char[][] schematic, int x, int y) {
        int h = schematic.length;
        if (y < 0 || y >= h) {
            return false;
        }

        int w = schematic[y].length;
        return x >= 0 && x < w;
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isDigit(char[][] schematic, int x, int y) {
        return isWithinBounds(schematic, x, y) && isDigit(schematic[y][x]);
    }

    public static boolean isSymbol(char c) {
        return !isDigit(c) && c != '.';
    }

    public static boolean isSymbol(char[][] schematic, int x, int y) {
        return isWithinBounds(schematic, x, y) && isSymbol(schematic[y][x]);
    }

    public static boolean isGearSymbol(char c) {
        return c == '*';
    }
}
