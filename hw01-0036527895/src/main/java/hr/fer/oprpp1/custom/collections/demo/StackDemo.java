package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

import java.util.Arrays;

public class StackDemo {

    public static void main(String[] args) {
        Object[] elements = args[0].split(" ");
        String[] operators = new String[]{"+", "-", "/", "*", "%"};
        for(Object element : elements) System.out.println(element.toString());
        ObjectStack objectStack = new ObjectStack();

        for(Object element : elements) {
            if(!Arrays.asList(operators).contains(element)) {
                objectStack.push(element);
            } else {
                Integer arg2 = Integer.parseInt(objectStack.pop().toString());
                Integer arg1 = Integer.parseInt(objectStack.pop().toString());
                switch ((String) element) {
                    case "+" -> objectStack.push(arg1 + arg2);
                    case "-" -> objectStack.push(arg1 - arg2);
                    case "/" -> {
                        if (arg2 == 0) throw new IllegalArgumentException("DONT DIVIDE BY ZERO");
                        objectStack.push(arg1 / arg2);
                    }
                    case "*" -> objectStack.push(arg1 * arg2);
                    case "%" -> objectStack.push(arg1 % arg2);
                }
            }
        }
        if(objectStack.size() != 1) throw new IllegalArgumentException("Stack is left with more than 1 element! Check expression.");
        System.out.println(objectStack.pop());
    }
}
