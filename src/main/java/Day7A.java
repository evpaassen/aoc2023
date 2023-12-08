import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

public class Day7A {

    public static List<Character> CARDS = List.of('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2', '1');

    public enum HandType {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    public record Hand(char[] cards, long bid) {

        public HandType type() {
            int[] cardOccurences = new int[CARDS.size()];

            for (int c = 0; c < 5; c++) {
                char card = cards[c];
                cardOccurences[CARDS.indexOf(card)]++;
            }

            int pairs = 0;
            boolean threeOfAKind = false;
            for (int occ : cardOccurences) {
                switch (occ) {
                    case 5: return HandType.FIVE_OF_A_KIND;
                    case 4: return HandType.FOUR_OF_A_KIND;
                    case 3: threeOfAKind = true; break;
                    case 2: pairs++;
                }
            }

            if (threeOfAKind && pairs == 1) {
                return HandType.FULL_HOUSE;
            } else if (threeOfAKind) {
                return HandType.THREE_OF_A_KIND;
            } else if (pairs == 2) {
                return HandType.TWO_PAIR;
            } else if (pairs == 1) {
                return HandType.ONE_PAIR;
            }

            return HandType.HIGH_CARD;
        }

        public static Hand parse(String line) {
            String[] segments = line.split(" ");
            return new Hand(segments[0].toCharArray(), Long.parseLong(segments[1]));
        }
    }

    public static class HandComparator implements Comparator<Hand> {

        @Override
        public int compare(Hand o1, Hand o2) {
            var cmp = o1.type().compareTo(o2.type());
            if (cmp != 0) {
                return -cmp;
            }

            for (int c = 0; c < 5; c++) {
                cmp = Integer.compare(cardValue(o1.cards[c]), cardValue(o2.cards[c]));
                if (cmp != 0) {
                    return -cmp;
                }
            }

            return 0;
        }

        public int cardValue(char c) {
            return CARDS.indexOf(c);
        }
    }

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new FileReader("input/day7.txt"));

        List<Hand> hands = reader.lines().map(Hand::parse).sorted(new HandComparator()).toList();

        long winnings =  0;
        for (int rank = 1; rank <= hands.size(); rank++) {
            Hand hand = hands.get(rank - 1);
            winnings += hand.bid() * rank;
        }

        System.out.println("Winnings: " + winnings);
    }
}
