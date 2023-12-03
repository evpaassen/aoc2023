import java.io.File;
import java.util.Scanner;

public class Day3A {

    public static void main(String[] args) throws Exception {
        File file = new File("input/day3.txt");
        var scanner = new Scanner(file);

        int lines = 0;
        while (scanner.hasNextLine()) {
            lines++;
            scanner.nextLine();
        }
        scanner.close();

        int sum = 0;

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
            int width = schematic[y].length;

            int number = 0;
            boolean adjacentToSymbol = false;

            for (int x = 0; x < width; x++) {
                char c = schematic[y][x];

                if (isDigit(c)) {
                    number = number * 10 + Integer.parseInt("" + c);
                    adjacentToSymbol |= isAdjacentToSymbol(schematic, x, y);
                } else {
                    if (adjacentToSymbol) {
                        sum += number;
                    }

                    // Reset
                    number = 0;
                    adjacentToSymbol = false;
                }
            }

            if (adjacentToSymbol) {
                sum += number;
            }

            // Reset
            number = 0;
            adjacentToSymbol = false;
        }

        System.out.println("===============");
        System.out.println("Answer:" + sum);
    }

    private static boolean isAdjacentToSymbol(char[][] schematic, int x, int y) {
        boolean a = false;

        // Above
        a |= isSymbol(schematic, x - 1, y - 1);
        a |= isSymbol(schematic, x, y - 1);
        a |= isSymbol(schematic, x + 1, y - 1);

        // Left
        a |= isSymbol(schematic, x - 1, y);

        // Right
        a |= isSymbol(schematic, x + 1, y);

        // Below
        a |= isSymbol(schematic, x - 1, y + 1);
        a |= isSymbol(schematic, x, y + 1);
        a |= isSymbol(schematic, x + 1, y + 1);

        return a;
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

    public static boolean isSymbol(char c) {
        return !isDigit(c) && c != '.';
    }

    public static boolean isSymbol(char[][] schematic, int x, int y) {
        return isWithinBounds(schematic, x, y) && isSymbol(schematic[y][x]);
    }
}
