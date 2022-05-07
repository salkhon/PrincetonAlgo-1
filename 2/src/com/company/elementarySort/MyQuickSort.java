package com.company.elementarySort;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class MyQuickSort {
    private static final int CUTOFF = 1;

    // quick select k-th smallest element in values
    public static <T extends Comparable<T>> T select(T[] values, int k) {
        if (k >= values.length) {
            return null;
        }
        MyKnuthShuffle.shuffle(values);
        //putLargestAtRight(values);
        return select(values, k, 0, values.length - 1);
    }

    public static <T extends Comparable<T>> T select(T[] values, int k, int lo, int hi) {
        int pivot = partition(values, lo, hi);

        while (pivot != k) {
            if (pivot < k) {
                lo = pivot + 1;
            } else {
                hi = pivot - 1;
            }
            pivot = partition(values, lo, hi);
        }

        return values[k];
    }

    public static <T extends Comparable<T>> void sort(T[] values, boolean hasManyDuplicateKeys) {
        MyKnuthShuffle.shuffle(values);
        //putLargestAtRight(values); // makes ptrL right bound checking redundant
        if (hasManyDuplicateKeys) {
            sort3way(values, 0, values.length - 1);
            //sort3Way2(values, 0, values.length - 1);
        } else {
            sort(values, 0, values.length - 1);
        }
        // left off at CUTOFF, which is a partially sorted array
        // so call to insertion sort in one pass over the partially sorted array
        MyInsertionSort.sort(values, 0, values.length - 1);
    }

    public static <T extends Comparable<T>> void sort(T[] values, int lo, int hi) {
        if (hi - lo + 1 <= CUTOFF) {
            // just leave the partially sorted segments, which will be handled by insertion sort in one pass in the end
            return;
        }

        int pivot = partition(values, lo, hi);

        sort(values, lo, pivot - 1);
        sort(values, pivot + 1, hi);
    }

    private static <T extends Comparable<T>> int partition(T[] values, int lo, int hi) {
        int ptrL = lo + 1, ptrG = hi; // LE : less or equals, G : greater

        //medianOfThree(values, lo, hi);
        while (ptrL <= ptrG) {
            while (ptrL <= hi && MyInsertionSort.less(values[ptrL], values[lo])) {
                ptrL++;
            }

            // checking that ptrG > lo is redundant, because value[lo] acts as a sentinel for less(values[lo], values[ptrG]) // !lessOrEquals()
            while (MyInsertionSort.less(values[lo], values[ptrG])) {
                ptrG--;
            }

            if (ptrL <= ptrG) {
                MyInsertionSort.exchange(values, ptrL++, ptrG--);
            }
        }

        MyInsertionSort.exchange(values, lo, ptrG);
        return ptrG;
    }

    private static <T extends Comparable<T>> void putLargestAtRight(T[] values) {
        int max = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].compareTo(values[max]) > 0) {
                max = i;
            }
        }

        MyInsertionSort.exchange(values, max, values.length - 1);
    }

    private static <T extends Comparable<T>> void medianOfThree(T[] values, int lo, int hi) {
        int mid = (lo + hi) / 2;
        @SuppressWarnings("unchecked")
        T[] sampleOf3 = (T[]) new Comparable<?>[3];
        sampleOf3[0] = values[lo]; sampleOf3[1] = values[mid]; sampleOf3[2] = values[hi]; // cutoff to insertion sort ensures that no < 3 subarray will bee called for medianOfThree
        MyInsertionSort.sort(sampleOf3);
        values[lo] = sampleOf3[1]; // putting the median on the arbitrary pivot lo
        values[mid] = sampleOf3[0];
        values[hi] = sampleOf3[2];
        MyInsertionSort.exchange(values, mid, lo + 1); // will start ptrL from lo + 1, and ptrL will act as sentinel for ptrG
        // now the value[] has sentinel on both edge for partitioning on values[mid], with makes both ptr bound check redundant
    }

    public static <T extends Comparable<T>> void sort3way(T[] values, int lo, int hi) {
        if (hi - lo + 1 <= CUTOFF) {
            return;
        }
//        int[] equalPivotBoundaries = partitionDijkstra3Way(values, lo, hi);

        // 3 way Dijkstra partition
        int ptrL = lo, ptrG = hi, i = lo + 1;
        T pivot = values[lo];
        int comp;
        while (i < ptrG) {
            comp = values[i].compareTo(pivot);
            if (comp < 0) {
                MyInsertionSort.exchange(values, i++, ptrL++);
            } else if (comp > 0) {
                MyInsertionSort.exchange(values, i, ptrG--);
            } else {
                i++;
            }
        }

        sort3way(values, lo, ptrL - 1);
        sort3way(values, ptrG + 1, hi);
    }

    // needs to return pair of values, thus array creation is needed, better to write code in the sort method
    @Deprecated
    private static <T extends Comparable<T>> int[] partitionDijkstra3Way(T[] values, int lo, int hi) {
        int ptrL = lo, ptrG = hi, i = lo;
        while (i <= ptrG) {
            if (MyInsertionSort.less(values[i], values[lo])) {
                MyInsertionSort.exchange(values, i++, ptrL++);
            } else if (MyInsertionSort.less(values[lo], values[i])) {
                MyInsertionSort.exchange(values, i, ptrG--);
            } else {
                i++;
            }
        }

//        int[] boundaries = new int[2];
//        boundaries[0] = ptrL - 1; boundaries[1] = ptrG + 1;
//        return boundaries;
        return null;
    }

    // J.Bentley and D. Mcllroy
    private static <T extends Comparable<T>> void fast3WayPartition(T[] values, int lo, int hi) {
        int leftEquals = lo + 1, rightEquals = hi;
        int ptrL = lo + 1, ptrR = hi;

        while (ptrL <= ptrR) {
            while (ptrL <= hi && values[ptrL].compareTo(values[lo]) <= 0) {
                if (values[ptrL].compareTo(values[lo]) == 0) {
                    MyInsertionSort.exchange(values, leftEquals++, ptrL);
                }
                ptrL++;
            }

            while (ptrR >= lo && values[ptrR].compareTo(values[lo]) >= 0) {
                if (values[ptrR].compareTo(values[lo]) == 0) {
                    MyInsertionSort.exchange(values, rightEquals--, ptrR);
                }
                ptrR--;
            }

            if (ptrL <= ptrR) {
                MyInsertionSort.exchange(values, ptrL, ptrR);
            }
        }

        while (leftEquals != lo) {
            MyInsertionSort.exchange(values, --leftEquals, --ptrL);
        }

        while (rightEquals != hi) {
            MyInsertionSort.exchange(values, ++rightEquals, ++ptrR);
        }
    }

    // using less instead of lessOrEquals will distribute duplicate keys of the pivot on both sides of the partition
    // that would lower the density of duplicate keys on partitioned sub-arrays, and lower the chance of going quadratic
    // would take more partitioning to have the same density
    // stopping ptrL and ptrG on equals moves both pointers and they cross near the middle - on high equal density too -
    // which partitions in logN
//    private static <T extends Comparable<T>> boolean lessOrEquals(T o1, T o2) {
//        return o1.compareTo(o2) <= 0;
//    }

    // test client
    public static void main(String[] args) {
        Character[] word = { 'B', 'A', 'B', 'A', 'B', 'A', 'B', 'A', 'C', 'A', 'D', 'A', 'B', 'R', 'A', 'X', 'W', 'W', 'W', 'M', 'M' };
        MyQuickSort.fast3WayPartition(word, 0, word.length - 1);
        System.out.println(Arrays.toString(word));
        Integer[] integers = new Integer[] { 7, 10, 5, 3, 8, 4, 2, 9, 6 };
        Character[] characters = new Character[] { 'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E' }; // 'M', 'E', 'R', 'G', 'E',
        System.out.println("number of inversions in \"sortexample\" is " + SortCompare.inversions(characters) + " \nis sorted? " + SortCompare.isSorted(characters));

        MyQuickSort.sort(integers, false);
        MyQuickSort.sort(characters, false);

        int SIZE = 100;
        Double[] doubles = new Double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            doubles[i] = StdRandom.uniform() * 100;
        }
        MyQuickSort.sort(doubles, false);

        for (int i : integers) {
            System.out.print(i +  " ");
        }
        System.out.println();
        for (char c : characters) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (double d : doubles) {
            System.out.print(d + " ");
        }
        System.out.println("\nIs doubles sorted? : " + SortCompare.isSorted(doubles));
        System.out.println("NaturalMergeSort: " + SortCompare.meanTime("NaturalMerge", 100000, 10));
        System.out.println("Merge: " + SortCompare.meanTime("Merge", 100000, 10));
        System.out.println("MergeBU: " + SortCompare.meanTime("MergeBU", 100000, 10));
        System.out.println("Quick: " + SortCompare.meanTime("Quick", 100000, 10));

        SIZE = 100000;
        Integer[] lotsOfEquals = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) {
            lotsOfEquals[i] = StdRandom.uniform(5);
        }
        Integer[] lotsOfEquals2 = Arrays.copyOf(lotsOfEquals, lotsOfEquals.length);

        long t = System.nanoTime();
        partitionDijkstra3Way(lotsOfEquals, 0, lotsOfEquals.length - 1);
        System.out.println("3-way partition Dijkstra: " + (System.nanoTime() - t));
        t = System.nanoTime();
        fast3WayPartition(lotsOfEquals2, 0, lotsOfEquals2.length - 1);
        System.out.println("3-way partition fast (Bentley-Mcllroy): " + (System.nanoTime() - t));
    }
}
//
//class Exercise {
//    public Tool[][] separateNutsAndBolts(Tool[] tools) {
//        int ptrN = 0, ptrB = tools.length;
//        while (ptrN <= ptrB) {
//            while (tools[ptrN].isNut()) {
//                ptrN++;
//            }
//
//            while (tools[ptrB].isBolt()) {
//                ptrB--;
//            }
//
//            if (ptrN < ptrB) {
//                exchange(tools, ptrN, ptrB);
//            }
//        }
//        Tool[] nuts = Arrays.copyOfRange(tools, 0, tools.length / 2 + 1);
//        Tool[] bolts = Arrays.copyOfRange(tools, tools.length / 2  + 1, tools.length);
//
//        Tool[][] tools1 = new Tool[2][tools.length / 2];
//        tools1[0] = nuts; tools1[1] = bolts;
//        return tools1;
//    }
//
//    public void sortTools(Tool[] toolsMixed) {
//        Tool[][] tools = separateNutsAndBolts(toolsMixed);
//        // tools[0] is nuts, tools[1] is bolts
//
//        sortTools(tools[0], tools[1], 0, toolsMixed.length / 2);
//    }
//
//    public void sortTools(Tool[] nuts, Tool[] bolts, int lo, int hi) {
//        if (lo >= hi) {
//            return;
//        }
//
//        int pivotBolt = partition(bolts, nuts[lo], lo, hi); // partition bolts, with the pivot nut
//        partition(nuts, bolts[pivotBolt], lo, hi); // partition nuts, with the pivot bolt returned from the bolt partition
//
//        sortTools(nuts, bolts, lo, pivotBolt - 1);
//        sortTools(nuts, bolts, pivotBolt + 1, hi);
//    }
//
//    private void partition(Tool[] partitionTarget, Tool correspondingPivot, int lo, int hi) {
//        int ptrL = lo, ptrG = hi;
//        int pivot = lo;
//        while (ptrL <= ptrG) {
//            while (correspondingPivot.compareTo(partitionTarget[ptrL]) > 0) {
//                ptrL++;
//            }
//
//            while (correspondingPivot.compareTo(partitionTarget[ptrG]) < 0) {
//                ptrG++;
//            }
//
//            if (correspondingPivot.compareTo(partitionTarget[ptrL]) == 0) {
//                pivot = ptrL;
//                continue;
//            } else if (correspondingPivot.compareTo(partitionTarget[ptrG]) == 0) {
//                pivot = ptrG;
//                continue;
//            }
//
//            if (ptrL <= ptrG) {
//                exchange(partitionTarget, ptrL, ptrG);
//            }
//        }
//
//        if (pivot > ptrG) {
//            exchange(partitionTarget, ptrL, pivot);
//        } else {
//            exchange(partitionTarget, ptrG, pivot);
//        }
//    }
//
//    ////////////////////////////////////////////////////
//
//    public static <T extends Comparable<T>> T select(T[] a, T[] b, int k) {
//        // we know that the k-th smallest entry must be in 0-k of a[] and 0-k of b[]
//        // first we binary search focusing on a[], find corresponding position on b[],
//        // a[i] is the k-th entry iff k - i elements of b[] fills the rest of the slot to make k elements in the combined array
//        // that can only happen if b[k - i - 1] <= a[i] && b[k - i] >= a[i]
//
//        T ans = search(a, b, k);
//        if (ans == null) {
//            ans = search(b, a, k);
//        }
//        return ans;
//    }
//
//    public static <T extends Comparable<T>> T search(T[] a, T[] b, int k) {
//        int lo = 0, hi = k;
//        int mid = (lo + hi) / 2;
//        while (lo <= hi) {
//            int aiComp = isAiKthEntry(a, mid, k, b);
//
//            if (aiComp > 0) {
//                lo = mid + 1;
//            } else if (aiComp < 0) {
//                hi = mid - 1;
//            } else  {
//                return a[mid];
//            }
//
//            mid = (lo + hi) / 2;
//        }
//        return null;
//    }
//
//    private static <T extends Comparable<T>> int isAiKthEntry(T[] a, int i, int k, T[] b) {
//        int ans;
//        if (b[k - i].compareTo(a[i]) < 0) {
//            ans = 1; // need more elements
//        } else if (k - i > 0 && b[k - i].compareTo(a[i]) > 0) {
//            ans = -1; // need fewer elements
//        } else {
//            ans = 0;
//        }
//
//        return ans;
//    }
//}
