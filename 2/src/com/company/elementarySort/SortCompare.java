package com.company.elementarySort;

import edu.princeton.cs.algs4.*;

public class SortCompare {
    public static double meanTime(String sortName, int sampleSize, int numOfTests) {
        Double[] values = new Double[sampleSize];

        long[] times = new long[numOfTests];

        long t;
        for (int i = 0; i < numOfTests; i++) {
            for (int j = 0; j < sampleSize; j++) {
                values[j] = StdRandom.uniform();
            }

            switch (sortName) {
                case "Selection":
                    t = System.currentTimeMillis();
                    MySelectionSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "Insertion":
                    t = System.currentTimeMillis();
                    MyInsertionSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "Shell":
                    t = System.currentTimeMillis();
                    MyShellSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "Merge":
                    t = System.currentTimeMillis();
                    MyMergeSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "MergeBU":
                    t = System.currentTimeMillis();
                    MyMergeSortBU.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "NaturalMerge":
                    t = System.currentTimeMillis();
                    NaturalMergeSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "Quick":
                    t = System.currentTimeMillis();
                    MyQuickSort.sort(values, false);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "QuickIterative":
                    t = System.currentTimeMillis();
                    NonRecurQuickSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
                case "Heap":
                    t = System.currentTimeMillis();
                    MyHeapSort.sort(values);
                    times[i] = System.currentTimeMillis() - t;
                    break;
            }

            if (!isSorted(values)) {
                throw new IllegalStateException(sortName + " test: " + i);
            }
        }

        return mean(times);
    }

    private static double mean(long[] times) {
        double sum = 0;
        for (long time : times) {
            sum += time;
        }

        return sum / times.length;
    }

    public static <T extends Comparable<T>> boolean isSorted(T[] values, int lo, int hi) {
        boolean isSortedAns = true;
        for (int i = lo + 1; i <= hi; i++) {
            if (less(values[i], values[i - 1])) {
                System.out.println("\n" + values[i] +  " is less than " + values[i - 1]);
                isSortedAns = false;
                break;
            }
        }

        return isSortedAns;
    }

    public static <T extends Comparable<T>> boolean isSorted(T[] values) {
        return isSorted(values, 0, values.length - 1);
    }

    private static <T extends Comparable<T>> boolean less(T o1, T o2) {
        return o1.compareTo(o2) < 0;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> int inversions(T[] values) {
        T[] aux = (T[]) new Comparable[values.length];
        T[] aux1 = (T[]) new Comparable[values.length];
        System.arraycopy(values, 0, aux, 0, values.length);
        return inversionsRecur(aux, 0, values.length - 1, aux1);
    }

    private static <T extends Comparable<T>> int inversionsRecur(T[] values, int lo, int hi, T[] aux) {
        if (lo >= hi) {
            return 0;
        }

        int mid = (lo + hi) / 2;
        int inversion1 = inversionsRecur(values, lo, mid, aux);
        int inversion2 = inversionsRecur(values, mid + 1, hi, aux);

        int inversion3 = merge(values, lo, mid, hi, aux);
        return inversion1 + inversion2 + inversion3;
    }

    private static <T extends Comparable<T>> int merge(T[] values, int lo, int mid, int hi, T[] aux) {
        System.arraycopy(values, lo, aux, lo, hi - lo + 1);

        int ptrL = lo, ptrR = mid + 1;
        int inversionsCount = 0;
        for (int i = lo; i <= hi; i++) {
            if (ptrL > mid) {
                values[i] = aux[ptrR++];
            } else if (ptrR > hi) {
                values[i] = aux[ptrL++];
            } else if (aux[ptrR].compareTo(aux[ptrL]) < 0) {
                for (int j = 0; ptrL + j <= mid; j++) {
                    System.out.println(aux[ptrL + j] + " " + aux[ptrR]);
                }
                values[i] = aux[ptrR++];
                inversionsCount += mid - ptrL + 1; // the ptrR entry has inversion will all entries that are left to be merged into value[]
            } else {
                values[i] = aux[ptrL++];
            }
        }
        return inversionsCount;
    }

    // test client
    public static void main(String[] args) {
        final int SAMPLE_SIZE = 100000, TESTS = 10;

//        double selectionTime = SortCompare.meanTime("Selection", SAMPLE_SIZE, TESTS);
//        System.out.println("Selection time: " + selectionTime);
//
//        double insertionTime = SortCompare.meanTime("Insertion", SAMPLE_SIZE, TESTS);
//        System.out.println("Insertion time: " + insertionTime);

        double shellTime = SortCompare.meanTime("Shell", SAMPLE_SIZE, TESTS);
        System.out.println("Shell time: " + shellTime);

        double mergeTime = SortCompare.meanTime("Merge", SAMPLE_SIZE, TESTS);
        System.out.println("Merge time: " + mergeTime);

        double mergeBUTime = SortCompare.meanTime("MergeBU", SAMPLE_SIZE, TESTS);
        System.out.println("MergeBU time: " + mergeBUTime);

        double naturalMergeTime = SortCompare.meanTime("NaturalMerge", SAMPLE_SIZE, TESTS);
        System.out.println("NaturalMerge time: " + naturalMergeTime);

        double quickTime = SortCompare.meanTime("Quick", SAMPLE_SIZE, TESTS);
        System.out.println("Quick time: " + quickTime);

        double quickIterTime = SortCompare.meanTime("QuickIterative", SAMPLE_SIZE, TESTS);
        System.out.println("Non-recursive Quick time: " + quickIterTime);

        double heapTime = SortCompare.meanTime("Heap", SAMPLE_SIZE, TESTS);
        System.out.println("Heap time: " + heapTime);
    }
}
//
//class Exercise {
//    public int countIntersection(Point2D[] a, Point2D[] b) {
//        int ptrA = 0, ptrB = 0;
//        int count = 0;
//        Arrays.sort(a, Point2D::compareTo);
//        Arrays.sort(b, Point2D::compareTo); // any sort will do, if 2 arrays are sorted using same comparator -
//        // using 2 pointer method to traverse both arrays
//        // so when pointers exceeds one point, points that are determined "lesser" by the comparator can't exist after that in neither array
//        while (ptrA < a.length && ptrB < b.length) {
//            if (a[ptrA].compareTo(b[ptrB]) == 0) {
//                count++;
//                ptrA++; ptrB++;
//            } else if (a[ptrA].compareTo(b[ptrB]) < 0) {
//                ptrA++;
//            } else {
//                ptrB++;
//            }
//        }
//        return count;
//    }
//
//    public boolean isPermutation(int[] a, int[] b) {
//        Arrays.sort(a);
//        Arrays.sort(b);
//        return Arrays.equals(a, b);
//    }
//
//    public void dutchFlag(Bucket[] buckets) {
//        // only 3 states - red white blue - so while maintaining sorted left sub array, we can mark the current ending of red part and white part
//        // Color is red: to get to red part, swap(whiteEnd + 1, current) then swap(redEnd + 1, whiteEnd + 1)
//        // -> swap blue first with current, and then blueFirst (which not holds current) with white first - shifts white and blue by one
//        // Color is white: to get to white part, swap(whiteEnd + 1, current)
//        // Color is blue: no need to move, as blue is last
//        int redEnd = 0, whiteEnd = 0;
//        for (int i = 1; i < buckets.length; i++) {
//            PebbleColor pebbleColor = buckets[i].color();
//            if (pebbleColor.equals(PebbleColor.RED)) {
//                swap(whiteEnd + 1, i);
//                swap(redEnd + 1, whiteEnd + 1);
//            } else if (pebbleColor.equals(PebbleColor.WHITE)) {
//                swap(whiteEnd + 1, i);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//
//    }
//}
