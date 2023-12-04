import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Day4B {

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day4.txt"));

        List<Integer> copies = new ArrayList<>();
        List<Integer> matchingNumbers = new ArrayList<>();

        int card = 0;
        while (scanner.hasNextLine()) {
            card++;
            copies.add(1);

            var line = scanner.nextLine();

            Set<Integer> winningNumbers = parseWinningNumbers(line);
            Set<Integer> myNumbers = parseMyNumbers(line);

            winningNumbers.retainAll(myNumbers);
            matchingNumbers.add(winningNumbers.size());

            System.out.println("Card " + card + ": " + matchingNumbers);
        }

        for (int i = 0; i < copies.size(); i++) {
            int c = copies.get(i);

            for (int m = 1; m <= matchingNumbers.get(i); m++) {
                if (i + m < copies.size()) {
                    copies.set(i + m, copies.get(i + m) + c);
                }
            }
        }

        Integer cards = copies.stream().reduce(0, Integer::sum);
        System.out.println("Answer: " + cards);
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
