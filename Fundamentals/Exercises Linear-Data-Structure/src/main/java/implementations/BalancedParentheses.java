package implementations;

import interfaces.Solvable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BalancedParentheses implements Solvable {
    private String parentheses;

    public BalancedParentheses(String parentheses) {
        this.parentheses = parentheses;
    }

    @Override
    public Boolean solve() {
        Stack<Character> stack = new Stack<>();

        for (char c : parentheses.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false; // Unmatched closing parenthesis
                }
                char top = stack.pop();
                if ((c == ')' && top != '(') || (c == '}' && top != '{') || (c == ']' && top != '[')) {
                    return false; // Mismatched closing parenthesis
                }
            }
        }

        return stack.isEmpty();

//        if (this.parentheses.length() == 0) {
//            return null;
//        }
//
//        char[] charArray = this.parentheses
//                .toCharArray();
//        int size = charArray.length;
//
//
//        if (size % 2 != 0) {
//            return false;
//        }
//
//        boolean isBalanced = false;
//        for (int i = 0; i <size ; i= i+2) {
//            isBalanced = false;
//            char char1 = charArray[i];
//            char char2 = charArray[i+1];
//            switch (char1) {
//                case '{':
//                    if (char2 == '}')
//                        isBalanced = true;
//                    break;
//
//                case '[':
//                    if (char2 == ']')
//                        isBalanced = true;
//                    break;
//
//                case '(':
//                    if (char2 == ')')
//                        isBalanced = true;
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        if (isBalanced){
//            return true;
//        }
//
//
//
//
//        for (int i = 0; i < size / 2; i++) {
//            char char1 = charArray[i];
//            char char2 = charArray[size - 1 - i];
//
//            switch (char1) {
//                case '{':
//                    if (char2 != '}')
//                        return false;
//                    break;
//
//                case '[':
//                    if (char2 != ']')
//                        return false;
//                    break;
//
//                case '(':
//                    if (char2 != ')')
//                        return false;
//                    break;
//
//                default:
//                    return false;
//            }
//
//
//        }
//        return true;
    }
}