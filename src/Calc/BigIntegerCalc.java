package Calc;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Stack;

public class BigIntegerCalc {
    private static StringBuilder result;
    private static final HashMap<String, BigInteger> vars = new HashMap<>();

    public static boolean proceedExpression(String expression) {
        switch (expression) {
            case "/exit":
                System.out.println("Bye!");
                return false;
            case "/help":
                System.out.println("The program calculates math expressions using Reverse Polish Notation algorithm");
                break;
            default:
                String normal = normalize(expression);
                if (normal.length() > 0 && errorHandler(normal)) {
                    if (normal.contains("=")) {
                        saveVariable(normal);
                    } else {
                        System.out.println(calculate(infix2postfix(normal)));
                    }
                }
        }
        return true;
    }

    public static String normalize(String expression) {
        return expression
                .replaceAll("[+]{2,}|(--)+", "+")
                .replaceAll("\\+-", "-")
                .replaceAll("  ", " ")
                .replaceAll("\\(", "( ")
                .replaceAll("\\)", " )");
    }

    public static boolean errorHandler(String expression) {
        String[] split;
        if (expression.matches("/[a-zA-Z]+")) {
            System.out.println("Unknown command");
            return false;
        }
        //Check declare of a variable
        else if (expression.chars().filter(ch -> ch == '(').count() != expression.chars().filter(ch -> ch == ')').count()) {
            System.out.println("Invalid expression");
            return false;
        } else if (expression.contains("*") || expression.contains("/")) {
            for (String s : expression.split(" ")) {
                if (s.matches("[*/]{2,}")) {
                    System.out.println("Invalid expression");
                    return false;
                }
            }
        } else if (expression.contains("=")) {
            split = expression.replaceAll(" ", "").split("=");
            //Checks if a new value contains a variable = > If variable is declared => Invalid assignment
            if (split.length > 1 && !split[1].matches("-*[0-9]+") && !vars.containsKey(split[1])) {
                System.out.println("Invalid assignment");
                return false;
            }
            if (!split[0].matches("[a-zA-Z]{1,10}")) {
                System.out.println("Invalid identifier");
                return false;
            }
        }
        //Checks if variable is declared => If variable is not declared => "Unknown Variable"
        else {
            split = expression.split("[ ]+");
            for (String s : split) {
                for (Character ch : s.toCharArray()) {
                    String var = ch.toString();
                    try { //Check var is a number
                        if (!var.matches("[+/*()\\-]") && var.length() > 0) new BigInteger(var);
                    } catch (NumberFormatException e) { //Else it's a variable
                        if (!vars.containsKey(var.replaceAll("-", ""))) {
                            System.out.println("Unknown Variable");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void saveVariable(String expression) {
        String[] splitted = expression.replaceAll(" +", "").split("=");
        if (vars.containsKey(splitted[1])) {
            vars.put(splitted[0], vars.get(splitted[1]));
        } else try {
            vars.put(splitted[0], BigInteger.valueOf(Long.parseLong(splitted[1])));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static String infix2postfix(String expression) {
        result = new StringBuilder();
        Stack<String> rpnStack = new Stack<>();
        for (String literal : expression.split(" ")) {
            String operand;
            if (literal.matches("-*[0-9]+")) { //First step is to write a number to result string
                result.append(literal).append(" ");
            } else if (literal.matches("[a-zA-Z]{1,10}")) { //Also check for a variable and put the value
                result.append(vars.get(literal)).append(" ");
            } else { //Second step is to fill a stack with operators that could be a string like "((42"
                for (Character ch : literal.toCharArray()) {
                    operand = ch.toString();
                    //1. If the stack is empty or contains a left parenthesis on top, push the incoming operator on the stack.
                    //4. If the incoming element is a left parenthesis, push it on the stack.
                    if (rpnStack.empty() || rpnStack.peek().matches("\\(") || operand.matches("\\(")) {
                        rpnStack.push(operand);
                        //6. If the incoming element is a right parenthesis, pop the stack and add operators to the result until you see a left parenthesis. Discard the pair of parentheses.
                    } else if (operand.matches("\\)")) {
                        ifLowerEqual(rpnStack, "\\("); //Right parenthesis has lowest precedence
                    } else if (operand.matches("[+\\-]")) {
                        //2. If the incoming operator has higher precedence than the top of the stack, push it on the stack.
                        if (rpnStack.peek().matches("[()]")) { // [+ -] > [( )]
                            rpnStack.push(operand);
                            //3. If the incoming operator has lower or equal precedence than or to the top of the stack, pop the stack and add operators to the result until you see an operator that has a smaller precedence or a left parenthesis on the top of the stack; then add the incoming operator to the stack.
                        } else if (rpnStack.peek().matches("[+\\-*/]")) { // [+ -] <= [* / + -]
                            ifLowerEqual(rpnStack, "[()]");
                            rpnStack.push(operand);
                        }
                    } else if (operand.matches("[*/]")) {
                        if (rpnStack.peek().matches("[()+\\-]")) { // [*/] > [+ - ( )]
                            rpnStack.push(operand);
                        } else if (rpnStack.peek().matches("[*/]")) { // [* /] <= [* /]
                            ifLowerEqual(rpnStack, "[()+\\-]");
                            rpnStack.push(operand);
                        }
                    } else {
                        result.append(operand); //If it's finally a number
                    }
                }
            }
        }
        while (!rpnStack.empty()) { //6. At the end of the expression, pop the stack and add all operators to the result.
            result.append(rpnStack.pop()).append(" ");
        }
        return result.toString();
    }

    private static void ifLowerEqual(Stack<String> rpnStack, String regex) {
        while (!rpnStack.empty() && !rpnStack.peek().matches(regex)) {
            result.append(rpnStack.pop()).append(" ");
        }
        if (regex.equals("\\(")) rpnStack.pop(); //Discard a both left and right parenthesis
    }

    public static BigInteger calculate(String postfixExpression) {
        Stack<BigInteger[]> stack = new Stack<>();
        for (String element : postfixExpression.split(" ")) {
            if (element.matches("-*[0-9]+")) {
                stack.push(new BigInteger[] {new BigInteger(element), BigInteger.ZERO});
            } else if (element.matches("[a-zA-Z]{1,10}")) {
                stack.push(new BigInteger[] {vars.get(element), BigInteger.ZERO});
            } else if (element.matches("[+*/\\-]")) {
                BigInteger[] y = stack.pop();
                BigInteger[] x = stack.pop();
                BigInteger[] result;
                switch (element) {
                    case "*":
                        result = x[0].multiply(BigInteger.TEN).add(x[1])
                                .multiply(y[0].multiply(BigInteger.TEN).add(y[1]))
                                .divideAndRemainder(new BigInteger("100"));
                        stack.push(new BigInteger[] { result[0], result[1].divide(BigInteger.TEN)} );
                        break;
                    case "/":
                        result = x[0].multiply(BigInteger.TEN).add(x[1])
                                .divideAndRemainder(y[0].multiply(BigInteger.TEN).add(y[1]));
                        if (result[1].signum() < 0) {
                            result[1] = result[1]
                                    .divide(BigInteger.TEN)
                                    .mod(x[0].abs()
                                            .multiply(y[0]
                                                    .abs()))
                                    .negate();
                        }
                        else result[1] = result[1].divide(BigInteger.TEN);
                        stack.push(new BigInteger[] { result[0], result[1]});
                        break;
                    case "+":
                        stack.push(x[0].multiply(BigInteger.TEN).add(x[1])
                                .add(y[0].multiply(BigInteger.TEN).add(y[1]))
                                .divideAndRemainder(BigInteger.TEN));
                        break;
                    case "-":
                        stack.push(x[0].multiply(BigInteger.TEN).add(x[1])
                                .subtract(y[0].multiply(BigInteger.TEN).add(y[1]))
                                .divideAndRemainder(BigInteger.TEN));
                        break;
                }
            }
        }
        BigInteger[] result = stack.pop();
        return (result[1].intValue() > 0) ? result[0].add(BigInteger.ONE) :
                (result[1].intValue() < 0) ? result[0].subtract(BigInteger.ONE) : result[0];
    }
}