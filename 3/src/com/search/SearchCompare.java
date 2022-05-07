package com.search;

import edu.princeton.cs.algs4.In;
// the main intuition of red-black and avl is keeping balance, and ROTATION as means of doing it
public class SearchCompare {
    public static long time(String search) {
        SymbolTable<String, Integer> symbolTable;
        switch (search) {
            case "SequentialLinkedList":
                symbolTable = new MySeqSearchST<>();
                break;
            case "BinarySearchArray":
                symbolTable = new MyBinSearchSymbolTable<>();
                break;
            case "BinarySearchTree":
                symbolTable = new MyBinarySearchTree<>();
                break;
            case "RedBlackBST": default:
                symbolTable = new MyRedBlackBST<>();
                break;
            case "AVL_BST":
                symbolTable = new AVL_BST<>();
        }

        In in = new In("leipzig1M.txt");

        int i = 0;
        long t1 = System.currentTimeMillis();
        while (in.hasNextLine()) {
            symbolTable.put(in.readString(), i++);
        }
        return System.currentTimeMillis() - t1;
    }

    // test client
    public static void main(String[] args) {
//        System.out.println("SeqSearch: " + SearchCompare.time("SequentialLinkedList"));
//        System.out.println("BinSearchArray: " + SearchCompare.time("BinarySearchArray"));
        System.out.println("BST: " + SearchCompare.time("BinarySearchTree"));
        System.out.println("RedBlackBST: " + SearchCompare.time("RedBlackBST"));
        System.out.println("AVL: " + SearchCompare.time("AVL_BST"));
    }
}
