import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9A {

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day9.txt"));

        long sum = reader.lines()
                .map(Day9A::parseValues)
                .map(Day9A::predictNextValue)
                .reduce(Long::sum)
                .orElse(0L);

        System.out.println("Answer: " + sum);
    }

    private static long predictNextValue(List<Integer> values) {
        List<List<Integer>> sequences = new ArrayList<>();
        sequences.add(values);

        while (!allZeroes(sequences.get(sequences.size() - 1))) {
            sequences.add(generateDiffSequence(sequences.get(sequences.size() - 1)));
        }

        int diff = 0;
        for (int s = sequences.size() - 2; s >= 0; s--) {
            List<Integer> sequence = sequences.get(s);
            diff = sequence.get(sequence.size() - 1) + diff;
        }

        return diff;
    }

    private static boolean allZeroes(List<Integer> values) {
        return values.stream().allMatch(v -> v == 0);
    }

    private static List<Integer> generateDiffSequence(List<Integer> values) {
        List<Integer> diff = new ArrayList<>(values.size() - 1);
        for (int i = 0; i < values.size() - 1; i++) {
            diff.add(values.get(i + 1) - values.get(i));
        }
        return diff;
    }

    public static List<Integer> parseValues(String line) {
        return Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .toList();
    }
}
