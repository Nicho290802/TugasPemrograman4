package com.tp4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Evaluator {
    private String infixExpression = "";

    public void setInfixExpression(String val) {
        this.infixExpression = val;
        errorList = new ArrayList<>();
        validateInfixExpression();
    }

    private ArrayList<String> errorList = new ArrayList<>();

    private void addError(String error) {
        if (!this.errorList.contains(error)) {
            this.errorList.add(error);
        }
    }

    public String getErrorList() {
        return this.errorList.toString();
    }

    private void validateInfixExpression() {
        if (this.infixExpression.isBlank()) {
            return;
        }
        if (!Pattern.compile("^[\\d+\\-*/ ()^]+$", Pattern.CASE_INSENSITIVE)
                .matcher(this.infixExpression).matches()) {
            addError("Invalid character(s)");
        }
    }

    private static Integer getTokenPower(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }

    private final static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public String getPostfixExpression() {
        StringBuilder postfixExpression = new StringBuilder();
        ArrayList<String> operatorStack = new ArrayList<>();

        StringTokenizer tokens = new StringTokenizer(this.infixExpression, "+-*/^() ", true);

        boolean operandTurn = true;

        tokenLoop:
        while (tokens.hasMoreTokens()) {
            String currentToken = tokens.nextToken();

            if (getTokenPower(currentToken) == 0) {
                if (isNumeric(currentToken)) {
                    if (!operandTurn) {
                        addError("Missing operator");
                    } else {
                        operandTurn = false;
                    }
                    postfixExpression.append(currentToken);
                    postfixExpression.append(" ");
                } else if (currentToken.equals("(")) {
                    operatorStack.add(currentToken);
                } else if (currentToken.equals(")")) {
                    if (!operatorStack.contains("(")) {
                        addError("Missing open parenthesis");
                        continue;
                    }
                    while (!operatorStack.get(operatorStack.size() - 1).equals("(")) {
                        postfixExpression.append(operatorStack.remove(operatorStack.size() - 1));
                        postfixExpression.append(" ");
                    }
                    operatorStack.remove(operatorStack.size() - 1);
                }
            } else {
                if (operandTurn) {
                    addError("Missing operand");
                } else {
                    operandTurn = true;
                }
                while (!operatorStack.isEmpty()) {
                    String latestOperator = operatorStack.get(operatorStack.size() - 1);
                    if (getTokenPower(latestOperator) > getTokenPower(currentToken)) {
                        postfixExpression.append(latestOperator);
                        operatorStack.remove(operatorStack.size() - 1);
                    } else if (getTokenPower(latestOperator) < getTokenPower(currentToken)) {
                        operatorStack.add(currentToken);
                        continue tokenLoop;
                    } else {
                        if (currentToken.equals("^")) {
                            operatorStack.add(currentToken);
                            continue tokenLoop;
                        }
                        postfixExpression.append(latestOperator);
                        operatorStack.remove(operatorStack.size() - 1);
                    }
                    postfixExpression.append(" ");
                }

                operatorStack.add(currentToken);
            }
        }

        while (!operatorStack.isEmpty()) {
            postfixExpression.append(operatorStack.remove(operatorStack.size() - 1)).append(" ");
        }

        String result = postfixExpression.toString();

        if (result.contains("(")) {
            addError("Missing close parentheses");
        }

        return postfixExpression.toString();
    }

    private static final List<String> operatorList = Arrays.asList("+", "-", "*", "/", "^");

    public String getResult() {
//        if (!this.errorList.isEmpty()) {
//            return "";
//        }

        ArrayList<String> resultStack = new ArrayList<>();

        String postfixExpression = getPostfixExpression();
        StringTokenizer tokens = new StringTokenizer(postfixExpression, "+-*/^() ", true);

        while (tokens.hasMoreTokens()) {
            String currentToken = tokens.nextToken();

            if (operatorList.contains(currentToken)) {
                if (resultStack.size() < 2) {
                    addError("Missing operand");
                    continue;
                }
                long rightOperand = Long.parseLong(resultStack.remove(resultStack.size() - 1));
                long leftOperand = Long.parseLong(resultStack.remove(resultStack.size() - 1));

                switch (currentToken) {
                    case "+":
                        resultStack.add(String.valueOf(leftOperand + rightOperand));
                        break;
                    case "-":
                        resultStack.add(String.valueOf(leftOperand - rightOperand));
                        break;
                    case "*":
                        resultStack.add(String.valueOf(leftOperand * rightOperand));
                        break;
                    case "/":
                        if (rightOperand == 0) {
                            addError("Division by zero");
                            resultStack.add(String.valueOf(leftOperand));
                        } else {
                            resultStack.add(String.valueOf(leftOperand / rightOperand));
                        }
                        break;
                    case "^":
                        resultStack.add(String.valueOf((long) Math.pow(leftOperand, rightOperand)));
                        break;
                }
            } else if (isNumeric(currentToken)) {
                resultStack.add(currentToken);
            }
        }

        if (resultStack.isEmpty()) {
            return "";
        }

        return resultStack.get(0);
    }
}
