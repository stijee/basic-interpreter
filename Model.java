import java.util.Hashtable;
import java.util.ArrayList;

/**
 * 
 * MODEL.JAVA - WRITTEN BY AARON KIAH - LAST UPDATE (NOV. 7, 2024)
 * 
 * The Model class represents the backend logic of the BASIC interpreter.
 * It handles loading, executing, and managing state for BASIC-like programs.
 * 
 * The class includes a mechanism to prevent infinite loops by limiting the number 
 * of execution steps.
 */

public class Model {
    private static final int MAX_STEPS = 100; // Limit to prevent infinite loops

    private Hashtable<String, Double> variables = new Hashtable<>();
    private StringBuilder outputBuilder = new StringBuilder();
    private ArrayList<String> programLines = new ArrayList<>();
    private int currentLine = 0;
    private int stepCount = 0;

/**
 * Loads the provided BASIC code into the program, preparing it for execution.
 * Each line of code is trimmed and stored in a list, and any previous program state
 * (such as the current line, step count, and output) is reset.
 *
 * @param code The BASIC code to be loaded, where each line represents a command.
 */

    public void loadProgram(String code) {
        System.out.println("Loading program...");
        programLines.clear();
        for (String line : code.split("\n")) {
            programLines.add(line.trim());
            System.out.println("Loaded line: " + line.trim());
        }
        currentLine = 0;
        stepCount = 0;
        outputBuilder.setLength(0); // Clear previous output
    }

/**
 * Executes the loaded BASIC program line by line. Each line is processed until
 * the end of the program is reached or a step limit is hit to prevent infinite loops.
 * 
 * The method keeps track of the current line and steps executed. If the maximum
 * number of steps (`MAX_STEPS`) is reached, it stops execution and logs an error 
 * indicating a potential infinite loop.
 */

    public void runProgram() {
        System.out.println("Running program...");
        while (currentLine < programLines.size()) {
            if (stepCount >= MAX_STEPS) {
                outputBuilder.append("Error: Program stopped due to potential infinite loop\n");
                System.out.println("Error: Program stopped due to potential infinite loop");
                break;
            }

            System.out.println("Processing line " + (currentLine + 1) + ": " + programLines.get(currentLine));
            processLine(programLines.get(currentLine));
            currentLine++;
            stepCount++;
        }
        System.out.println("Program finished running.");
    }

/**
 * Processes a single line of BASIC code, determining the type of command
 * (e.g., print, assignment, if, goto) and performing the appropriate action.
 * 
 * @param line The line of BASIC code to process.
 */

    private void processLine(String line) {
        int commentIndex = line.indexOf("//");
        if (commentIndex != -1) {
            line = line.substring(0, commentIndex).trim(); // Keep only the code part before //
        }

        line = line.replaceAll("^\\d+", "").trim(); // Remove line number if it exists

        if (line.isEmpty()) {
            System.out.println("Skipping empty line or comment-only line.");
            return;
        }

        System.out.println("Processing: " + line);

        if (line.startsWith("print")) {
            handlePrint(line);
        } else if (line.contains("=") && !line.contains("goto")) { // Detect assignment statements
            evaluateExpression(line);
        } else if (line.startsWith("if")) {
            handleIf(line);
        } else if (line.startsWith("goto")) {
            handleGoto(line);
        } else if (line.equals("end")) {
            currentLine = programLines.size(); // End program
            System.out.println("End of program encountered.");
        } else {
            outputBuilder.append("Error: Unsupported statement: ").append(line).append("\n");
            System.out.println("Error: Unsupported statement: " + line);
        }
    }

/**
 * Handles the "print" command, evaluating the expression and appending
 * the result to the output.
 *
 * @param line The "print" command line to process.
 */

    private void handlePrint(String line) {
        System.out.println("Handling print statement: " + line);
        String expression = line.substring(5).trim();
        try {
            double result = evaluate(expression);
            outputBuilder.append(result).append("\n");
            System.out.println("Print result: " + result);
        } catch (IllegalArgumentException e) {
            outputBuilder.append("Error evaluating print expression: ").append(e.getMessage()).append("\n");
            System.out.println("Error evaluating print expression: " + e.getMessage());
        }
    }

/**
 * Processes an "if" statement by evaluating the condition. If the condition
 * is true, jumps to the specified line number using a "goto" command.
 *
 * @param line The "if" command line to process.
 */

    private void handleIf(String line) {
        System.out.println("Handling if statement: " + line);
        line = line.substring(2).trim();

        if (line.startsWith("(") && line.contains(")")) {
            int closingParenIndex = line.indexOf(")");
            String conditionPart = line.substring(1, closingParenIndex).trim();
            String remainingPart = line.substring(closingParenIndex + 1).trim();

            if (remainingPart.startsWith("goto")) {
                line = conditionPart + " " + remainingPart;
            }
        }

        int gotoIndex = line.indexOf("goto");
        if (gotoIndex == -1) {
            outputBuilder.append("Error: 'if' statement missing 'goto'\n");
            System.out.println("Error: 'if' statement missing 'goto'");
            return;
        }

        String condition = line.substring(0, gotoIndex).trim();
        String targetLine = line.substring(gotoIndex + 4).trim();

        try {
            if (evaluateCondition(condition)) {
                int targetIndex = findLineIndex(targetLine);
                System.out.println("Condition met, jumping to line " + targetLine + " (index " + targetIndex + ")");
                currentLine = targetIndex - 1;
            } else {
                System.out.println("Condition not met, continuing to next line.");
            }
        } catch (IllegalArgumentException e) {
            outputBuilder.append("Error evaluating 'if' condition: ").append(e.getMessage()).append("\n");
            System.out.println("Error evaluating 'if' condition: " + e.getMessage());
        }
    }

/**
 * Processes an "if" statement by evaluating the condition. If the condition
 * is true, jumps to the specified line number using a "goto" command.
 *
 * @param line The "if" command line to process.
 */

    private boolean evaluateCondition(String condition) {
        System.out.println("Evaluating condition: " + condition);
        String[] operators = {"=", ">", "<", ">=", "<="};
        String operator = null;
        String leftPart = null;
        String rightPart = null;

        for (String op : operators) {
            int opIndex = condition.indexOf(op);
            if (opIndex != -1) {
                operator = op;
                leftPart = condition.substring(0, opIndex).trim();
                rightPart = condition.substring(opIndex + op.length()).trim();
                break;
            }
        }

        if (operator == null || leftPart == null || rightPart == null) {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }

        double leftValue = variables.containsKey(leftPart) ? variables.get(leftPart) : evaluate(leftPart);
        double rightValue = variables.containsKey(rightPart) ? variables.get(rightPart) : evaluate(rightPart);

        switch (operator) {
            case "=": return leftValue == rightValue;
            case ">": return leftValue > rightValue;
            case "<": return leftValue < rightValue;
            case ">=": return leftValue >= rightValue;
            case "<=": return leftValue <= rightValue;
            default: throw new IllegalArgumentException("Unsupported operator in condition: " + operator);
        }
    }

/**
 * Handles a "goto" statement, changing the current line to the target line
 * if it exists in the program.
 *
 * @param line The "goto" command line to process.
 */

    private void handleGoto(String line) {
        System.out.println("Handling goto statement: " + line);
        String targetLine = line.substring(4).trim();
        int lineIndex = findLineIndex(targetLine);
        if (lineIndex != -1) {
            System.out.println("Jumping to line " + targetLine + " (index " + lineIndex + ")");
            currentLine = lineIndex - 1;
        } else {
            outputBuilder.append("Error: 'goto' target line not found: ").append(targetLine).append("\n");
            System.out.println("Error: 'goto' target line not found: " + targetLine);
        }
    }

/**
 * Finds the index of a target line in the program based on its line number.
 *
 * @param targetLine The line number to search for.
 * @return The index of the target line, or -1 if not found.
 */

    private int findLineIndex(String targetLine) {
        System.out.println("Finding line index for target line: " + targetLine);
        for (int i = 0; i < programLines.size(); i++) {
            if (programLines.get(i).startsWith(targetLine)) {
                System.out.println("Found target line at index " + i);
                return i;
            }
        }
        System.out.println("Target line not found.");
        return -1;
    }

/**
 * Finds the index of a target line in the program based on its line number.
 *
 * @param targetLine The line number to search for.
 * @return The index of the target line, or -1 if not found.
 */

    public void evaluateExpression(String line) {
        System.out.println("Evaluating expression: " + line);
        line = line.replaceAll(" ", "");
        int equalIndex = line.indexOf("=");
        if (equalIndex == -1) {
            outputBuilder.append("Error: No '=' found in expression.\n");
            System.out.println("Error: No '=' found in expression.");
            return;
        }
        String var = line.substring(0, equalIndex);
        String expression = line.substring(equalIndex + 1);

        try {
            double result = evaluate(expression);
            variables.put(var, result);
            outputBuilder.append(var).append(" = ").append(result).append("\n");
            System.out.println("Assigned " + var + " = " + result);
        } catch (IllegalArgumentException e) {
            outputBuilder.append("Error evaluating expression for ").append(var).append(": ").append(e.getMessage()).append("\n");
            System.out.println("Error evaluating expression for " + var + ": " + e.getMessage());
        }
    }

/**
 * Evaluates a mathematical expression, returning the result as a double.
 *
 * @param expr The expression to evaluate.
 * @return The evaluated result of the expression.
 * @throws IllegalArgumentException if the expression format is invalid.
 */

    private double evaluate(String expr) {
        return parseExpression(expr, new Index(0));
    }

/**
 * Parses and evaluates an expression with addition and subtraction operators.
 *
 * @param expr The expression to parse.
 * @param index The current index position in the expression.
 * @return The evaluated result of the expression.
 */

    private double parseExpression(String expr, Index index) {
        double result = parseTerm(expr, index); // Start with a term to handle higher precedence operations first
        while (index.pos < expr.length()) {
            char ch = expr.charAt(index.pos);
            if (ch == '+') {
                index.pos++;
                result += parseTerm(expr, index); // Addition
            } else if (ch == '-') {
                index.pos++;
                result -= parseTerm(expr, index); // Subtraction
            } else {
                break;
            }
        }
        return result;
    }

/**
 * Parses and evaluates a term with multiplication and division operators.
 *
 * @param expr The expression to parse.
 * @param index The current index position in the expression.
 * @return The evaluated result of the term.
 */

    private double parseTerm(String expr, Index index) {
        double result = parseFactor(expr, index); // Start with a factor to handle parentheses
        while (index.pos < expr.length()) {
            char ch = expr.charAt(index.pos);
            if (ch == '*') {
                index.pos++;
                result *= parseFactor(expr, index); // Multiplication
            } else if (ch == '/') {
                index.pos++;
                result /= parseFactor(expr, index); // Division
            } else {
                break;
            }
        }
        return result;
    }

/**
 * Parses and evaluates a factor, handling numbers, variables, and expressions
 * in parentheses.
 *
 * @param expr The expression to parse.
 * @param index The current index position in the expression.
 * @return The evaluated result of the factor.
 * @throws IllegalArgumentException if the factor is invalid.
 */

    private double parseFactor(String expr, Index index) {
        skipWhitespace(expr, index); // Skip any whitespace

        char ch = expr.charAt(index.pos);
        if (ch == '(') {
            index.pos++;
            double result = parseExpression(expr, index); // Recursively evaluate the expression inside parentheses
            if (index.pos < expr.length() && expr.charAt(index.pos) == ')') {
                index.pos++; // Move past the closing ')'
            }
            return result;
        } else if (Character.isDigit(ch) || ch == '.') {
            int start = index.pos;
            while (index.pos < expr.length() && (Character.isDigit(expr.charAt(index.pos)) || expr.charAt(index.pos) == '.')) {
                index.pos++;
            }
            return Double.parseDouble(expr.substring(start, index.pos)); // Parse the number as double
        } else if (Character.isLetter(ch)) {
            int start = index.pos;
            while (index.pos < expr.length() && Character.isLetter(expr.charAt(index.pos))) {
                index.pos++;
            }
            String varName = expr.substring(start, index.pos);
            if (variables.containsKey(varName)) {
                return variables.get(varName); // Retrieve variable value
            } else {
                throw new IllegalArgumentException("Undefined variable: " + varName);
            }
        }
        throw new IllegalArgumentException("Invalid factor at position: " + index.pos);
    }

/**
 * Skips whitespace characters in an expression, moving the index to the next
 * non-whitespace character.
 *
 * @param expr The expression to parse.
 * @param index The current index position in the expression.
 */

    private void skipWhitespace(String expr, Index index) {
        while (index.pos < expr.length() && Character.isWhitespace(expr.charAt(index.pos))) {
            index.pos++;
        }
    }

/**
 * Skips whitespace characters in an expression, moving the index to the next
 * non-whitespace character.
 *
 * @param expr The expression to parse.
 * @param index The current index position in the expression.
 */

    public String getOutput() {
        return outputBuilder.toString();
    }

/**
 * Clears all stored variables and resets the output, preparing for a new
 * program run.
 */

    public void clearVariables() {
        variables.clear();
        outputBuilder.setLength(0); // Clear output builder
    }

/**
 * Helper class representing an index in an expression, used for parsing.
 */

    private static class Index {
        int pos;

        Index(int pos) {
            this.pos = pos;
        }
    }
}
