import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day6B {

    public static void main(String[] args) throws Exception {
        var scanner = new Scanner(new File("input/day6.txt"));
        long time = parseNumber(scanner.nextLine());
        long record = parseNumber(scanner.nextLine());

        Long waysToWin = waysToWin(time, record);

        System.out.println("Answer: " + waysToWin);
    }

    public static long parseNumber(String line) {
        return Long.parseLong(line.replaceAll(" ", "").substring(line.indexOf(':') + 1));
    }

    public static Long waysToWin(long time, long record) {
        long wins = 0;

        for (int chargeTime = 0; chargeTime <= time; chargeTime++) {
            long distance = distance(time, chargeTime);

            if (distance > record) {
                wins++;
            }
        }

        return wins;
    }

    private static long distance(long totalTime, long chargeTime) {
        long travelTime = totalTime - chargeTime;
        long speed = chargeTime;
        return travelTime * speed;
    }
}
