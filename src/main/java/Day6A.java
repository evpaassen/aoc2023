import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day6A {

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day6.txt"));
        List<Integer> times = parseNumbers(scanner.nextLine());
        List<Integer> records = parseNumbers(scanner.nextLine());

        int product = 1;
        for (int r = 0; r < times.size(); r++) {
            product *= waysToWin(times.get(r), records.get(r));
        }

        System.out.println("Answer: " + product);
    }

    public static List<Integer> parseNumbers(String line) {
        return Arrays.stream(line.substring(line.indexOf(':') + 1).split(" "))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }

    public static Integer waysToWin(int time, int record) {
        int wins = 0;

        for (int chargeTime = 0; chargeTime <= time; chargeTime++) {
            int distance = distance(time, chargeTime);

            if (distance > record) {
                wins++;
            }
        }

        return wins;
    }

    private static int distance(int totalTime, int chargeTime) {
        int travelTime = totalTime - chargeTime;
        int speed = chargeTime;
        return travelTime * speed;
    }
}
