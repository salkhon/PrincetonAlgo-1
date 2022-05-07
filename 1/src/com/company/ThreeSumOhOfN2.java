package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// using Oh of N TwoSum, N2 ThreeSum can be easily done
public class ThreeSumOhOfN2 {
    private int[] integers;

    public ThreeSumOhOfN2(int[] integers) {
        this.integers = integers;
    }

    public void setIntegers(int[] integers) {
        this.integers = integers;
    }

    public List<Integer[]> findZeroTriples(int fromIndex, int toIndex) {
        List<Integer[]> triples = new ArrayList<>();
        TwoSumOhOfN twoSumOhOfN = new TwoSumOhOfN(this.integers);
        for (int i = fromIndex; i < toIndex - 2; i++) {
            List<Integer[]> pairsSummingToCancelCurrentInt = twoSumOhOfN.findZeroSumPairs(i + 1, toIndex, this.integers[i]);

            if (!pairsSummingToCancelCurrentInt.isEmpty()) {
                for (Integer[] pair : pairsSummingToCancelCurrentInt) {
                    Integer[] temp = { this.integers[i], pair[0], pair[1]};
                    triples.add(temp);
                }
            }
        }

        return triples;
    }

    // test client
    public static void main(String[] args) {
        int[] integers4K = new In("4Kints.txt").readAllInts();

        Arrays.sort(integers4K);
        ThreeSumOhOfN2 threeSumOhOfN2 = new ThreeSumOhOfN2(integers4K);

        final int TEST = 5;
        int[] times = new int[TEST];

        List<Integer[]> triples = null;
        for (int i = 0; i < TEST; i++) {
            long t1 = System.currentTimeMillis();
            triples = threeSumOhOfN2.findZeroTriples(0, integers4K.length);
            long t2 = System.currentTimeMillis();
            times[i] = (int) (t2 - t1);
        }
        triples.forEach((triple) -> System.out.println(Arrays.toString(triple)));
        double mean4K = StdStats.mean(times);
        System.out.println("4K runtime: " + mean4K);
        int count4K = triples.size();

        int[] integers8K = new In("8Kints.txt").readAllInts();
        Arrays.sort(integers8K);
        threeSumOhOfN2.setIntegers(integers8K);

        for (int i = 0; i < TEST; i++) {
            long t1 = System.currentTimeMillis();
            triples = threeSumOhOfN2.findZeroTriples(0, integers8K.length);
            long t2 = System.currentTimeMillis();
            times[i] = (int) (t2 - t1);
        }
        triples.forEach((triple) -> System.out.println(Arrays.toString(triple)));
        double mean8K = StdStats.mean(times);
        System.out.println("8K runtime: " + mean8K);

        System.out.println("Doubling Ratio: " + (Math.log(mean8K) - Math.log(mean4K)) / Math.log(2));

        System.out.println("Count4K: " + count4K);
        System.out.println("Count8K: " + triples.size());
    }
}

// two pointer approach?
class TwoSumOhOfN {
    private int[] integers;

    public TwoSumOhOfN(int[] integers) {
        this.integers = integers;
    }

    public void setIntegers(int[] integers) {
        this.integers = integers;
    }

    public List<Integer[]> findZeroSumPairs(int fromIndex, int toIndex, int comparisonMargin) {
        List<Integer[]> pairs = new ArrayList<>();
        int lo = fromIndex, hi = toIndex - 1;
        int targetSum = -comparisonMargin;

        while (lo <= hi) {
            int sum = this.integers[lo] + this.integers[hi];

            if (sum > targetSum) {
                hi--;
            } else if (sum < targetSum) {
                lo++;
            } else {
                Integer[] temp = { this.integers[lo], this.integers[hi] };
                pairs.add(temp);
                hi--; lo++;
            }
        }

        return pairs;
    }

    // test client
    public static void main(String[] args) {
        int[] integers4K = new In("4Kints.txt").readAllInts();

        Arrays.sort(integers4K);
        TwoSumOhOfN twoSumOhOfN = new TwoSumOhOfN(integers4K);

        final int TEST = 5;
        int[] times = new int[TEST];

        List<Integer[]> pairs = null;
        for (int i = 0; i < TEST; i++) {
            long t1 = System.nanoTime();
            pairs = twoSumOhOfN.findZeroSumPairs(0, integers4K.length, -1);
            long t2 = System.nanoTime();
            times[i] = (int) (t2 - t1);
        }
        pairs.forEach((pair) -> System.out.println(Arrays.toString(pair)));
        double mean4K = StdStats.mean(times);
        System.out.println("4K runtime: " + mean4K);

        int[] integers8K = new In("8Kints.txt").readAllInts();
        twoSumOhOfN.setIntegers(integers8K);
        Arrays.sort(integers8K);
        for (int i = 0; i < TEST; i++) {
            long t1 = System.nanoTime();
            pairs = twoSumOhOfN.findZeroSumPairs(0, integers8K.length, -1);
            long t2 = System.nanoTime();
            times[i] = (int) (t2 - t1);
        }
        pairs.forEach((pair) -> System.out.println(Arrays.toString(pair)));
        double mean8K = StdStats.mean(times);
        System.out.println("8K runtime: " + mean8K);

        System.out.println("Doubling Ratio: " + (Math.log(mean8K) - Math.log(mean4K)) / Math.log(2));


    }
}







//// Hashing for all ints run out of memory, DOES NOT WORK
//public class ThreeSumOhOfN2 {
//    private final int[] integers;
//    private final boolean[] posRecord;
//    private final boolean[] negRecords;
//
//    public ThreeSumOhOfN2(int[] integers) {
//        this.integers = integers;
//        this.posRecord = new boolean[Integer.MAX_VALUE];
//        this.negRecords = new boolean[Integer.MAX_VALUE]; // negRecords[0] for -1
//
//        for (int integer : integers) {
//            if (integer >= 0) {
//                this.posRecord[integer] = true;
//            } else {
//                this.negRecords[integer + 1] = true;
//            }
//        }
//    }
//
//    public List<Integer[]> getZeroTriplets() {
//        List<Integer[]> results = new ArrayList<>();
//
//        for (int i = 0; i < this.integers.length - 2; i++) {
//            for (int j = 0; j < this.integers.length - 1; j++) {
//                int target = this.integers[i] + this.integers[j];
//                if (target >= 0) {
//                    if (this.negRecords[target + 1]) { // target exists as a negative number
//                        Integer[] temp = { this.integers[i], this.integers[j], -target };
//                        results.add(temp);
//                    }
//                } else {
//                    if (this.posRecord[-target]) { // target exists as a positive number
//                        Integer[] temp = { this.integers[i], this.integers[j], -target };
//                        results.add(temp);
//                    }
//                }
//            }
//        }
//
//        return results;
//    }
//
//    // test client
//    public static void main(String[] args) {
//        int[] integers = new In("4Kints.txt").readAllInts();
//        Arrays.sort(integers);
//        ThreeSumOhOfN2 threeSumOhOfN2 = new ThreeSumOhOfN2(integers);
//
//        List<Integer[]> triplets = null;
//        final int TESTS = 5;
//        int[] times = new int[TESTS];
//        for (int i = 0; i < TESTS; i++) {
//            long t1 = System.currentTimeMillis();
//            triplets = threeSumOhOfN2.getZeroTriplets();
//            long t2 = System.currentTimeMillis();
//            times[i] = (int) (t2 - t1);
//        }
//
//        triplets.forEach((triplet) -> System.out.println(triplet[0] + " " + triplet[1] + " " + triplet[2]));
//
//        System.out.println("Count: " + triplets.size());
//        System.out.println("Runtime: " + StdStats.mean(times));
//    }
//}
