import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Day1 {

    public static Map<String, Integer> words = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );


    public static void main(String[] args) throws FileNotFoundException {
        var scanner = new Scanner(new File("input/day1.txt"));

        int sum = 0;

        while (scanner.hasNextLine()) {
            String originalLine = scanner.nextLine();
            var line = originalLine;

            char[] chars = line.toCharArray();

            Integer firstDigit = null, lastDigit = null;

            int i = 0;
            for (char c : chars) {
                if (isDigit(c)) {
                    var val = Integer.parseInt(String.valueOf(c));

                    if (firstDigit == null) {
                        firstDigit = val;
                    }
                    lastDigit = val;
                }

                for (Map.Entry<String, Integer> e : words.entrySet()) {
                    try {
                        if (e.getKey().equals(line.substring(i, i + e.getKey().length()))) {
                            if (firstDigit == null) {
                                firstDigit = e.getValue();
                            }
                            lastDigit = e.getValue();
                        }
                    } catch (IndexOutOfBoundsException ignored) {}
                }

                i++;
            }

            int coord = 10 * firstDigit + lastDigit;
            System.out.println(originalLine + " | " + line + " : " + coord);

            sum += coord;
        }

        System.out.println(sum);
    }
    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
