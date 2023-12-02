import java.io.File;
import java.util.*;

public class Day2B {

    public enum Color {
        red,
        green,
        blue
    }

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day2.txt"));

        int sum = 0;

        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();

            var game = parseGame(line);
            var hands = parseHands(line);

            var power = calculatePower(hands);
            System.out.println("Game " + game + ": " + power);

            sum += calculatePower(hands);
        }

        System.out.println("Answer: " + sum);
    }

    public static int parseGame(String line) {
        return Integer.parseInt(line.substring(5, line.indexOf(':')));
    }

    public static List<Map<Color, Integer>> parseHands(String line) {
        return Arrays.stream(line.substring(line.indexOf(':')  + 2)
                .split("; "))
                .map(Day2B::parseHand)
                .toList();
    }

    public static Map<Color, Integer> parseHand(String hand) {
        var map = new HashMap<Color, Integer>();

        for (String s : hand.split(", ")) {
            String[] parts = s.split(" ");
            map.put(Color.valueOf(parts[1]), Integer.parseInt(parts[0]));
        }

        return map;
    }

    public static int calculatePower(List<Map<Color, Integer>> hands) {
        int maxRed = hands.stream().map(h -> h.getOrDefault(Color.red, 0)).max(Integer::compareTo).orElse(0);
        int maxGreen = hands.stream().map(h -> h.getOrDefault(Color.green, 0)).max(Integer::compareTo).orElse(0);
        int maxBlue = hands.stream().map(h -> h.getOrDefault(Color.blue, 0)).max(Integer::compareTo).orElse(0);

        return maxRed * maxGreen *  maxBlue;
    }
}
