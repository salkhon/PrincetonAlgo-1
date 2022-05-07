package com.search;

import edu.princeton.cs.algs4.In;

public class FrequencyCounter {
    public static void main(String[] args) {
        final int WORD_SIZE = 1;
        In in = new In("leipzig1M.txt");
        MyBinarySearchTree<String, Integer> frequencyCount = new MyBinarySearchTree<>();
        String word;
        int numGetCalls = 0, numPutCalls = 0;
        long t1 = System.currentTimeMillis();
        while (in.hasNextLine()) {
            word = in.readString();
            if (word.length() >= WORD_SIZE) {
                if (frequencyCount.contains(word)) {
                    frequencyCount.put(word, frequencyCount.get(word) + 1);
                    numGetCalls++;
                } else {
                    frequencyCount.put(word, 1);
                }
                numPutCalls++;
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println(frequencyCount);
        System.out.println("Time: " + (t2 - t1));
        System.out.println("put() : " + numPutCalls);
        System.out.println("get() : " + numGetCalls);
        System.out.println("distinct words : " + (numPutCalls - numGetCalls) + " size: " + frequencyCount.size());

        word = "";
        int freq = 0;
        for (String s : frequencyCount) {
            int currFreq = frequencyCount.get(s);
            if (currFreq > freq) {
                word = s;
                freq = currFreq;
            }
        }
        System.out.println("Max frequency word: " + word + " " + freq + " times");
    }
}
