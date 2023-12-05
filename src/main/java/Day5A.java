import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Day5A {

    public record Mapping(String src, String dst) {}

    public record Range(long dstStart, long srcStart, long length) {}

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day5.txt"));

        List<Long> seeds = null;

        boolean blank = false;
        String srcCat = null, dstCat = null;

        Map<Mapping, List<Range>> mappings = new HashMap<>();

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();

            if (seeds == null) {
                // Line 1
                seeds = Arrays.stream(line.substring(line.indexOf(':') + 2).split(" "))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
            } else if (line.isEmpty()) {
                // Empty row
                blank = true;
            } else if (blank) {
                // Header row
                srcCat = line.substring(0, line.indexOf("-to-"));
                dstCat = line.substring(line.indexOf("-to-") + 4, line.indexOf(" map"));
                blank = false;
            } else {
                // Mapping row
                blank = false;
                String[] numbers = line.split(" ");

                long dstRangeStart = Long.parseLong(numbers[0]);
                long srcRangeStart = Long.parseLong(numbers[1]);
                long length = Long.parseLong(numbers[2]);

                List<Range> ranges = mappings.computeIfAbsent(new Mapping(srcCat, dstCat), k -> new ArrayList<>());
                ranges.add(new Range(dstRangeStart, srcRangeStart, length));
            }
        }

        long minLocation = seeds.stream()
                .peek(seed -> System.out.print("seed: " + seed))
                .map(seed -> map(mappings, "seed", "soil", seed))
                .peek(soil -> System.out.print(" soil: " + soil))
                .map(soil -> map(mappings, "soil", "fertilizer", soil))
                .peek(fertilizer -> System.out.print(" fertilizer: " + fertilizer))
                .map(fertilizer -> map(mappings, "fertilizer", "water", fertilizer))
                .peek(water -> System.out.print(" water: " + water))
                .map(water -> map(mappings, "water", "light", water))
                .peek(light -> System.out.print(" light: " + light))
                .map(light -> map(mappings, "light", "temperature", light))
                .peek(temperature -> System.out.print(" temperature: " + temperature))
                .map(temperature -> map(mappings, "temperature", "humidity", temperature))
                .peek(humidity -> System.out.print(" humidity: " + humidity))
                .map(humidity -> map(mappings, "humidity", "location", humidity))
                .peek(location -> System.out.println(" location: " + location))
                .reduce(Long.MAX_VALUE, Long::min);

        System.out.println("Closest location: " + minLocation);
    }

    public static long map(Map<Mapping, List<Range>> mappings, String srcCat, String dstCat, long srcValue) {
        List<Range> ranges = mappings.get(new Mapping(srcCat, dstCat));
        return findDstValue(ranges, srcValue);
    }

    public static long findDstValue(List<Range> ranges, long srcValue) {
        Optional<Range> range = ranges.stream().filter(r -> srcValueInRange(r, srcValue)).findFirst();

        if (range.isPresent()) {
            long offset = srcValue - range.get().srcStart;
            return range.get().dstStart + offset;
        } else {
            return srcValue;
        }
    }

    public static boolean srcValueInRange(Range r, long srcValue) {
        return srcValue >= r.srcStart && srcValue < r.srcStart + r.length;
    }
}
