import java.util.*;

public class Main {
    static final int ITEM_PRICE = 8000000;
    static final int[] DISCOUNTS = {0, 5, 10, 20, 25};
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] items = input.split(",");
        Map<String, Integer> itemCount = new HashMap<>();
        for (String item : items) {
            itemCount.put(item, itemCount.getOrDefault(item, 0) + 1);
        }
        List<Integer> counts = new ArrayList<>(itemCount.values());
        int minCost = calculateMinCost(counts);
        System.out.println(minCost);
    }
    private static int calculateMinCost(List<Integer> counts) {
        int[] minCost = {Integer.MAX_VALUE};
        findMinCostRecursively(counts, 0, minCost);
        return minCost[0];
    }
    private static void findMinCostRecursively(List<Integer> counts, int currentCost, int[] minCost) {
        int remainingItems = counts.stream().mapToInt(Integer::intValue).sum();
        if (remainingItems == 0) {
            minCost[0] = Math.min(minCost[0], currentCost);
            return;
        }
        //int groupSize = Math.min(5, remainingItems); groupSize >= 1; groupSize--
        for (int groupSize = 1;groupSize <= 15; groupSize++) {
            if (isGroupPossible(counts, groupSize)) {
                List<Integer> newCounts = new ArrayList<>(counts);
                createGroup(newCounts, groupSize);
                int groupCost = computeGroupCost(groupSize);
                findMinCostRecursively(newCounts, currentCost + groupCost, minCost);
            }
            if (remainingItems > 0) {
                int singleItemsCost = counts.stream().mapToInt(count -> count * ITEM_PRICE).sum();
                minCost[0] = Math.min(minCost[0], currentCost + singleItemsCost);
            }
        }

//        if (remainingItems > 0) {
//            int singleItemsCost = counts.stream().mapToInt(count -> count * ITEM_PRICE).sum();
//            minCost[0] = Math.min(minCost[0], currentCost + singleItemsCost);
//        }
    }

    private static boolean isGroupPossible(List<Integer> counts, int groupSize) {
        return counts.stream().filter(count -> count > 0).count() >= groupSize;
    }

    private static void createGroup(List<Integer> counts, int groupSize) {
        int itemsFormed = 0;
        for (int i = 0; i < counts.size() && itemsFormed < groupSize; i++) {
            if (counts.get(i) > 0) {
                counts.set(i, counts.get(i) - 1);
                itemsFormed++;
            }
        }
    }

    private static int computeGroupCost(int groupSize) {
        int discount = DISCOUNTS[groupSize - 1]; // Adjusted to use the correct discount
        int groupCost = groupSize * ITEM_PRICE;
        return groupCost - (groupCost * discount / 100);
    }
}
