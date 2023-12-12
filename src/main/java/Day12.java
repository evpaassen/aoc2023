import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Day12 {

    public static final char OPERATIONAL = '.';
    public static final char DAMAGED = '#';
    public static final char UNKNOWN = '?';

    public record Line(char[] springs, List<Integer> damagedGroups) {

        public long possibleArrangements() {
            List<String> possibilities = List.of("");

            for (int i = 0; i < springs.length; i++) {
                char status = springs[i];
                if (status == UNKNOWN) {
                    possibilities = possibilities.stream()
                            .flatMap(s -> Stream.of(s + OPERATIONAL, s + DAMAGED))
                            .toList();
                } else {
                    possibilities = possibilities.stream().map(s -> s + status).toList();
                }
            }

            return possibilities.stream().map(String::toCharArray).filter(this::isValid).count();
        }

        public boolean isValid(char[] springs) {
            ArrayDeque<Integer> expectedGroups = new ArrayDeque<>(damagedGroups);

            int damagedGroup = 0;
            for (int i = 0; i < springs.length; i++) {
                if (springs[i] == DAMAGED) {
                    damagedGroup++;
                } else if (springs[i] == OPERATIONAL) {
                    if (damagedGroup > 0) {
                        try {
                            Integer expected = expectedGroups.removeFirst();
                            if (expected != damagedGroup) {
                                return false;
                            }
                        } catch (NoSuchElementException e) {
                            return false;
                        }
                    }

                    damagedGroup = 0;
                }
            }

            if (damagedGroup > 0) {
                try {
                    Integer expected = expectedGroups.removeFirst();
                    if (expected != damagedGroup) {
                        return false;
                    }
                } catch (NoSuchElementException e) {
                    return false;
                }
            }

            return true;
        }

        public static Line parse(String line) {
            String[] segments = line.split(" ");

            List<Integer> dmgGroups = segments.length < 2 ? List.of() :
                    Arrays.stream(segments[1].split(",")).map(Integer::parseInt).toList();

            return new Line(segments[0].toCharArray(), dmgGroups);
        }
    }

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day12-example.txt"));

        long sum = reader.lines()
                .map(Line::parse)
                .map(Line::possibleArrangements)
                .reduce(Long::sum)
                .orElse(0L);

        System.out.println("Answer: " + sum);
    }
}
