package com.company;

/*  2logN
* it is not necessary to break the array
* in normal binary search comparison with the key is done to find the target reference,
* and the boundary pointers are moved according to the MID value relative to the key
* in a bitonic array, simply checking the MID value is not enough to move the boundary pointers
* we have to determine WHICH boundary pointer to move
* on first search we might target the increasing part of the array, so
* if (mid is in increasing part) {
*       move hi or lo as in normal binary search
*       continue;
* } else {
*       if (mid == key) return midIndex;
*       else
*           move hi to mid, to shift the boundaries toward increasing array
*           continue;
* }
*
* - X -
*
* then we could repeat the same process targeting the decreasing part of the array
* */


///* (3lgN)
//* binary searches for bitonic point, then searches for key in increasing part of the array
//* if not found, searches for key in decreasing part of the array, by setting the binary search configuration to reversed
//* reversed binary search moves hi pointer leftwards if key is lower than mid - reversed compared to the normal method
//* logN for bitonic point search, logN for increasing array search, logN for decreasing array search*/
public class BitonicSearch {
    private final int[] integers;

    public BitonicSearch(int[] integers) {
        this.integers = integers;
    }

    private int findBitonicPoint() {
        // binary search for bitonic point
        int lo = 0, hi = this.integers.length - 1;

        int index = -1;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;

            if (this.integers[mid] < this.integers[mid + 1]) { // mid in the increasing section
                lo = mid + 1;
            } else if (this.integers[mid - 1] > this.integers[mid]) { // next to mid if lesser (fell through first check) AND prev to mid is bigger - mid in decrease section
                hi = mid - 1;
            } else { // next to mid is lesser than mid, prev to mid is lesser than mid - bitonic point!
                index = mid;
                break;
            }
        }

        return index;
    }

    public int find(int key) {
        int bitonicIndex = findBitonicPoint();
        MyBinarySearch myBinarySearch = new MyBinarySearch(this.integers);
        int index = myBinarySearch.find(0, bitonicIndex + 1, key);

        if (index <= 0) {
            myBinarySearch.setReversed(true);
            index = myBinarySearch.find(bitonicIndex + 1, this.integers.length, key);
        }

        return index;
    }

    // test client
    public static void main(String[] args) {
        int[] integers = { 4, 7, 12, 43, 65, 76, 101, 332, 443, 560, 559, 500, 499, 300, 200, 150, 32, 2, 1 };
        BitonicSearch bitonicSearch = new BitonicSearch(integers);
        System.out.println(bitonicSearch.find(200));
    }
}

//    public int eggDrop(int n) {
//        // version 0
////        boolean eggBreaks = false;
////        for (int i = 1; i <= n && !eggBreaks; i++) {
////            eggBreaks = dropEgg(i);
////        }
////
////        return i;
//
//        //version 1
////        int lo = 1, hi = n;
////
////        while (lo <= hi) {
////            int mid = (lo + hi) / 2;
////
////            boolean eggBreak = dropEgg(mid);
////
////            if (eggBreak) {
////                if (dropEgg(mid - 1)) { // breaking floor
////                    return mid;
////                }
////
////                // target floor is lower
////                hi = mid - 1;
////            } else {
////                // target floor os higher
////                lo = mid + 1;
////            }
////        }
//
//        // version 2
//
//        int lo = 1, hi = 1;
//
//        boolean eggBreaks = false;
//        eggBreaks = dropEgg(hi);
//
//        while (!eggBreaks) {
//
//            lo = hi;
//            hi *= 2;
//            if (hi > n) {
//                hi = n;
//                break;
//            }
//            eggBreaks = dropEgg(hi);
//        }
//        // after the loop hi-lo interval will be between pow(2, floor(logT)) and pow(2, floor(logT) + 1), thats 2T - T = T element interval with 1 egg wastage, and logT iterations
//        // binary search inside T elements, will cost logT, total cost 2logT iterations, logT egg wastage
//
//        return binarySearchForDropPoint(lo, hi);
//    }


