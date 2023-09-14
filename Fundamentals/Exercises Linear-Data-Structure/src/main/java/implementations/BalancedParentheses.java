package implementations;

import interfaces.Solvable;

import java.util.ArrayList;
import java.util.List;

public class BalancedParentheses implements Solvable {
    private String parentheses;

    public BalancedParentheses(String parentheses) {
        this.parentheses = parentheses;
    }

    @Override
    public Boolean solve() {


        if (this.parentheses.length() == 0) {
            return null;
        }

        char[] charArray = this.parentheses
                .replaceAll("\\s+", "")
                .toCharArray();
        int size = charArray.length;


        if (size % 2 != 0) {
            return false;
        }

        for (int i = 0; i <size ; i++) {

        }




        for (int i = 0; i < size / 2; i++) {
            char char1 = charArray[i];
            char char2 = charArray[size - 1 - i];

            switch (char1) {
                case '{':
                    if (char2 != '}')
                        return false;
                    break;

                case '[':
                    if (char2 != ']')
                        return false;
                    break;

                case '(':
                    if (char2 != ')')
                        return false;
                    break;

                default:
                    return false;
            }


        }
        return true;
    }
}