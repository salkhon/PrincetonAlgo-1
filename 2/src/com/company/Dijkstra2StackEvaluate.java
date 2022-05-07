package com.company;

public class Dijkstra2StackEvaluate {
    private String expression;
    private MyStackResizingArray<Integer> valueStack;
    private MyStackResizingArray<Character> operatorStack;

    public Dijkstra2StackEvaluate() {
    }

    public int evaluate(String expression) {
        this.expression = expression;
        this.valueStack = new MyStackResizingArray<>(Integer[].class);
        this.operatorStack = new MyStackResizingArray<>(Character[].class);

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);

            if (Character.isDigit(current)) {
                StringBuilder number = new StringBuilder("" + current);
                while (Character.isDigit(expression.charAt(++i))) {
                    number.append(expression.charAt(i));
                }
                i--; // for loop also increments
                this.valueStack.push(Integer.parseInt(number.toString()));
            } else if (current == '+' || current == '-' || current == '*' || current == '/') {
                this.operatorStack.push(current);
            } else if (current == ')') {
                int operand2 = this.valueStack.pop();
                int operand1 = this.valueStack.pop();
                char operator = this.operatorStack.pop();

                int result;
                if (operator == '+') {
                    result = operand1 + operand2;
                } else if (operator == '-') {
                    result = operand1 - operand2;
                } else if (operator == '*') {
                    result = operand1 * operand2;
                } else {
                    result = operand1 / operand2;
                }

                this.valueStack.push(result);
            }
        }

        return this.valueStack.pop();
    }

    // test client
    public static void main(String[] args) {
        Dijkstra2StackEvaluate dijkstra2StackEvaluate = new Dijkstra2StackEvaluate();

        String expression = "(((2*3)*(3/2))+((4-2)*23))";
        System.out.println(expression + " = " + dijkstra2StackEvaluate.evaluate(expression));
    }
}
