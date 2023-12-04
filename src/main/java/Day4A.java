import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Day4A {

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day4.txt"));

        int sum = 0;

        int card = 0;
        while (scanner.hasNextLine()) {
            card++;

            var line = scanner.nextLine();

            Set<Integer> winningNumbers = parseWinningNumbers(line);
            Set<Integer> myNumbers = parseMyNumbers(line);

            winningNumbers.retainAll(myNumbers);
            double points = winningNumbers.isEmpty() ? 0 : Math.pow(2, winningNumbers.size() - 1);

            System.out.println("Card " + card + ": " + points);

            sum += points;
        }

        System.out.println("Answer: " + sum);
    }

    private static Set<Integer> parseWinningNumbers(String line) {
        return parseNumbers(line.substring(line.indexOf(":") + 2, line.indexOf("|") - 1));
    }

    private static Set<Integer> parseMyNumbers(String line) {
        return parseNumbers(line.substring(line.indexOf("|") + 2));
    }

    private static Set<Integer> parseNumbers(String numbers) {
        return Arrays.stream(numbers.split(" "))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
