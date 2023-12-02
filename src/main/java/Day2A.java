import java.io.File;
import java.util.*;

public class Day2A {

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

//            System.out.println("=========================");
//            System.out.println("Game: " + game);
//
//            for (Map<Color, Integer> hand : hands) {
//                System.out.println("R: " + hand.getOrDefault(Color.red, 0) + " | G: " + hand.getOrDefault(Color.green, 0) + " | B: " + hand.getOrDefault(Color.blue, 0) + " | Valid: " + isValidHand(hand));
//            }

            if (hands.stream().allMatch(Day2A::isValidHand)) {
                sum += game;
            }
        }

        System.out.println("Answer: " + sum);
    }

    public static int parseGame(String line) {
        return Integer.parseInt(line.substring(5, line.indexOf(':')));
    }

    public static List<Map<Color, Integer>> parseHands(String line) {
        return Arrays.stream(line.substring(line.indexOf(':')  + 2).split("; ")).map(Day2A::parseHand).toList();
    }

    public static Map<Color, Integer> parseHand(String hand) {
        var map = new HashMap<Color, Integer>();

        for (String s : hand.split(", ")) {
            String[] parts = s.split(" ");
            map.put(Color.valueOf(parts[1]), Integer.parseInt(parts[0]));
        }

        return map;
    }

    public static boolean isValidHand(Map<Color, Integer> hand) {
        return hand.getOrDefault(Color.red, 0) <= 12
                && hand.getOrDefault(Color.green, 0) <= 13
                && hand.getOrDefault(Color.blue, 0) <= 14;
    }
}
