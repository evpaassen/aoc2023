import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day8B {

    public record Location(String name, String left, String right) {

        public static Location parse(String line) {
            return new Location(line.substring(0, 3), line.substring(7, 10), line.substring(12, 15));
        }

        public boolean isStart() {
            return name.endsWith("A");
        }

        public boolean isDestination() {
            return name.endsWith("Z");
        }
    }

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day8.txt"));

        char[] instructions = scanner.nextLine().toCharArray();

        // Skip a line
        scanner.nextLine();

        HashMap<String, Location> locations = new HashMap<>();
        while (scanner.hasNextLine()) {
            Location location = Location.parse(scanner.nextLine());
            locations.put(location.name, location);
        }

        List<Location> startLocations = locations.values().stream().filter(Location::isStart).toList();
        long[] steps = new long[startLocations.size()];

        for (int l = 0; l < startLocations.size(); l++) {
            Location currentLocation = startLocations.get(l);

            while (!currentLocation.isDestination()) {
                char instruction = instructions[(int) (steps[l] % instructions.length)];
                currentLocation = locations.get(instruction == 'L' ? currentLocation.left : currentLocation.right);
                steps[l]++;
            }
        }

        var s = Arrays.stream(steps).mapToObj(Long::toString).map(BigInteger::new).reduce(Day8B::lcm).orElseThrow();
        System.out.println("Steps: " + s);
    }

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        BigInteger gcd = number1.gcd(number2);
        BigInteger absProduct = number1.multiply(number2).abs();
        return absProduct.divide(gcd);
    }
}
