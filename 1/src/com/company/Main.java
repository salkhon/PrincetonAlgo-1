package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of nodes: ");
        int N = scanner.nextInt();
        UF Uf = new QuickUnionUF(N);

        while (true) {
            System.out.print("Enter 2 nodes to connect (negative to quit): ");
            int p = scanner.nextInt();
            int q = scanner.nextInt();
            if (p < 0 || q < 0) {
                break;
            }
            Uf.union(p, q); // isConnected checking will be implemented by the union method
        }

        System.out.println(Uf.connected(1, 6) + " " + Uf.connected(4, 6));
    }

    public static class TimeStampInLogFile {
        private int p;
        private int q;
        private String time;

        public int getP() {
            return p;
        }

        public int getQ() {
            return q;
        }

        public String getTime() {
            return time;
        }
    }

    // each linking reduces root number by 1
    // N - 1 linking will connect the entire tree
//    public static TimeStampInLogFile earliestTime() {
//        int M = 50, N = 50;
//        TimeStampInLogFile[] timeStampInLogFile = new TimeStampInLogFile[M];
//
//        for (int i = 0; i < M; i++) {
//            // read timestamp from logFile
//        }
//
//        WeightedPathCompressedQUUF weightedPathCompressedQUUF = new WeightedPathCompressedQUUF(N);
//
//        for (int i = 0; i < M; i++) {
//            weightedPathCompressedQUUF.union(timeStampInLogFile[i].getP(), timeStampInLogFile[i].getQ());
//            if (weightedPathCompressedQUUF.isAllConnected()) {
//                return timeStampInLogFile[i];
//            }
//        }
//
//        return null;
//    }
}
