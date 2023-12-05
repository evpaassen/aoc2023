import java.io.File;
import java.util.*;

public class Day5B {

    public record Mapping(String src, String dst) {}

    public record Range(long dstStart, long srcStart, long length) {

        public long dstEndExclusive() {
            return dstStart + length;
        }

        public long srcEndExclusive() {
            return srcStart + length;
        }

        @Override
        public String toString() {
            return "{" + srcStart + "-" + srcEndExclusive() + " : " + dstStart + "-" + dstEndExclusive() + "}";
        }

        public long map(long srcValue) {
            if (srcValue >= srcStart && srcValue < srcEndExclusive()) {
                long offset = srcValue - srcStart;
                return dstStart + offset;
            } else {
                throw new IllegalArgumentException("Out of bounds");
            }
        }
    }

    public record SeedRange(long start, long length) {

        public long endExclusive() {
            return start + length;
        }
    }

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day5.txt"));

        List<SeedRange> seedRanges = new ArrayList<>();

        boolean blank = false;
        String srcCat = null, dstCat = null;

        Map<Mapping, SortedSet<Range>> mappings = new HashMap<>();

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();

            if (seedRanges.isEmpty()) {
                // Line 1
                List<Long> firstLineNrs = Arrays.stream(line.substring(line.indexOf(':') + 2).split(" "))
                        .map(Long::parseLong)
                        .toList();

                int n = 0;
                while (n < firstLineNrs.size()) {
                    seedRanges.add(new SeedRange(firstLineNrs.get(n), firstLineNrs.get(n + 1)));
                    n = n + 2;
                }
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

                SortedSet<Range> ranges = mappings.computeIfAbsent(new Mapping(srcCat, dstCat), k -> new TreeSet<>(Comparator.comparingLong(Range::srcStart)));
                ranges.add(new Range(dstRangeStart, srcRangeStart, length));
            }
        }

        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("seed", "soil"))).stream()).toList();
        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("soil", "fertilizer"))).stream()).toList();
        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("fertilizer", "water"))).stream()).toList();
        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("water", "light"))).stream()).toList();
        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("light", "temperature"))).stream()).toList();
        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("temperature", "humidity"))).stream()).toList();
        seedRanges = seedRanges.stream().flatMap(sr -> project(sr, mappings.get(new Mapping("humidity", "location"))).stream()).toList();

        long minLocation = seedRanges.stream().map(sr -> sr.start).reduce(Long.MAX_VALUE, Long::min);

        System.out.println("Closest location: " + minLocation);
    }

    public static List<SeedRange> project(SeedRange seedRange, SortedSet<Range> mappings) {
        List<SeedRange> ret = new ArrayList<>();

        long position = seedRange.start;

        for (Range mappingRange : mappings) {
            if (seedRange.endExclusive() <= mappingRange.srcStart) {
                // Range after seed range, stop.
                break;
            }

            if (seedRange.start >= mappingRange.srcEndExclusive()) {
                // Range before seed range, skip.
                continue;
            }

            if (position < mappingRange.srcStart) {
                ret.add(new SeedRange(position, mappingRange.srcStart - seedRange.start));
                position = mappingRange.srcStart;
            }

            long length = Math.min(seedRange.endExclusive(), mappingRange.srcEndExclusive()) - position;
            ret.add(new SeedRange(mappingRange.map(position), length));
            position += length;
        }

        if (position < seedRange.endExclusive()) {
            ret.add(new SeedRange(position, seedRange.endExclusive() - position));
        }

        return ret;
    }
}
