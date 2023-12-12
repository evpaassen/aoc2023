import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12B {

    public static final char OPERATIONAL = '.';
    public static final char DAMAGED = '#';
    public static final char UNKNOWN = '?';

    public record Line(char[] springs, List<Integer> damagedGroups) {

        public long possibleArrangements() {
            List<List<Boolean>> possibilities = new ArrayList<>();
            possibilities.add(new ArrayList<>(springs.length));

            for (int i = 0; i < springs.length; i++) {
                char status = springs[i];
                if (status == UNKNOWN) {
                    int jMax = possibilities.size();
                    for (int j = 0; j < jMax; j++) {
                        List<Boolean> p = possibilities.get(j);

                        ArrayList<Boolean> tmp = new ArrayList<>(springs.length);
                        tmp.addAll(p);
                        tmp.add(true);
                        possibilities.add(tmp);

                        p.add(false);
                    }
                } else {
                    for (List<Boolean> p : possibilities) {
                        p.add(status == DAMAGED);
                    }
                }
            }

            return possibilities.stream().filter(this::isValid).count();
        }

        public boolean isValid(List<Boolean> damagedSprings) {
            ArrayDeque<Integer> expectedGroups = new ArrayDeque<>(damagedGroups);

            int damagedGroup = 0;
            for (int i = 0; i < springs.length; i++) {
                if (damagedSprings.get(i)) {
                    // Damaged
                    damagedGroup++;
                } else {
                    // Operational
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

            return expectedGroups.isEmpty();
        }

        public static Line parse(String line) {
            String[] segments = line.split(" ");

            List<Integer> dmgGroups = segments.length < 2 ? List.of() :
                    Arrays.stream(segments[1].split(",")).map(Integer::parseInt).toList();

            List<Integer> unfoldedDmgGroups = new ArrayList<>(dmgGroups.size() * 5);
            for (int i = 0; i < 5; i++) {
                unfoldedDmgGroups.addAll(dmgGroups);
            }
            
            String unfoldedSprings = segments[0] + segments[0] + segments[0] + segments[0] + segments[0];
            return new Line(unfoldedSprings.toCharArray(), unfoldedDmgGroups);
        }
    }

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day12-example.txt"));

        long sum = reader.lines()
                .parallel()
                .map(Line::parse)
                .map(Line::possibleArrangements)
                .reduce(Long::sum)
                .orElse(0L);

        System.out.println("Answer: " + sum);
    }
}
