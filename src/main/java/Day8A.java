import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Day8A {

    public record Location(String name, String left, String right) {

        public static Location parse(String line) {
            return new Location(line.substring(0, 3), line.substring(7, 10), line.substring(12, 15));
        }

        public boolean isDestination() {
            return name.equals("ZZZ");
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

        long steps = 0;
        Location currentLocation = locations.get("AAA");
        while (!currentLocation.isDestination()) {
            char instruction = instructions[(int) (steps % instructions.length)];
            currentLocation = locations.get(instruction == 'L' ? currentLocation.left : currentLocation.right);
            steps++;
        }

        System.out.println("Steps: " + steps);
    }
}
