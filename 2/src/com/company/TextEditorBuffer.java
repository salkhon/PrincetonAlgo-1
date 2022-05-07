package com.company;

import java.util.Stack;

public class TextEditorBuffer {
    private MyStack<Character> leftStack;
    private MyStack<Character> rightStack;

    public TextEditorBuffer() {
        this.leftStack = new MyStackResizingArray<>(Character[].class);
        this.rightStack = new MyStackResizingArray<>(Character[].class);
    }

    public void insert(char c) {
        this.leftStack.push(c);
    }

    public char delete() {
        return this.leftStack.pop();
    }

    public void left(int k) {
        for (int i = 0; i < k; i++) {
            this.rightStack.push(this.leftStack.pop());
        }
    }

    public void right(int k) {
        for (int i = 0; i < k; i++) {
            this.leftStack.push(this.rightStack.pop());
        }
    }

    public int size() {
        return this.leftStack.size() + this.rightStack.size();
    }

    // test client
    public static void main(String[] args) {
        
    }
}
