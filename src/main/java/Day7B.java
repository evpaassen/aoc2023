import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day7B {

    public static List<Character> CARDS = List.of('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', '1', 'J');

    public enum HandType {
        FIVE_OF_A_KIND(null),
        FOUR_OF_A_KIND(FIVE_OF_A_KIND),
        FULL_HOUSE(FOUR_OF_A_KIND),
        THREE_OF_A_KIND(FOUR_OF_A_KIND),
        TWO_PAIR(FULL_HOUSE),
        ONE_PAIR(THREE_OF_A_KIND),
        HIGH_CARD(ONE_PAIR);

        private final HandType upgrade;

        HandType(HandType upgrade) {
            this.upgrade = upgrade;
        }

        public HandType upgrade() {
            return this.upgrade == null ? this : this.upgrade;
        }
    }

    public record Hand(char[] cards, long bid) {

        public HandType type() {
            int jokers = 0;
            int[] cardOccurences = new int[CARDS.size() - 1];

            for (int c = 0; c < 5; c++) {
                char card = cards[c];
                if (card == 'J') {
                    jokers++;
                } else {
                    cardOccurences[CARDS.indexOf(card)]++;
                }
            }

            HandType type = typeWithoutJokers(cardOccurences);
            for (int j = 0; j < jokers; j++) {
                type = type.upgrade();
            }
            return type;
        }

        private static HandType typeWithoutJokers(int[] cardOccurences) {
            int pairs = 0;
            boolean threeOfAKind = false;
            for (int occ : cardOccurences) {
                switch (occ) {
                    case 5:
                        return HandType.FIVE_OF_A_KIND;
                    case 4:
                        return HandType.FOUR_OF_A_KIND;
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

        @Override
        public String toString() {
            return "Hand{cards=" + Arrays.toString(cards) + ", bid=" + bid + ", type=" + this.type() + '}';
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
            System.out.println(hand);
            winnings += hand.bid() * rank;
        }

        System.out.println("Winnings: " + winnings);
    }
}
