import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 
 * VIEW.JAVA - WRITTEN BY AARON KIAH - LAST UPDATE (NOV. 7, 2024)
 * 
 * The View class represents the user interface for the BASIC interpreter,
 * allowing users to enter code, execute it, and view output in a separate window.
 * It provides the main window for code input and an additional popup window for program output.
 */

public class View {
    private JFrame mainFrame;
    private JFrame outputWindow; // Separate output window
    private JTextArea codeArea;
    private JTextArea outputArea;
    private JButton runButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton resetButton;
    private JButton infoButton;

/**
* Constructs the View, setting up the main UI components including the main frame,
* buttons, code input area, and the output window.
*/

    public View() {
        // Set up the main frame for the BASIC Interpreter
        mainFrame = new JFrame("BASIC Interpreter");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Code input area
        codeArea = new JTextArea();
        JScrollPane codeScrollPane = new JScrollPane(codeArea);
        mainFrame.add(codeScrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        runButton = new JButton("Run");
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");
        resetButton = new JButton("Reset");
        infoButton = new JButton("Info");

        buttonPanel.add(runButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(infoButton);
        mainFrame.add(buttonPanel, BorderLayout.NORTH);

        mainFrame.setVisible(true);

        // Initialize the output window but keep it hidden
        outputWindow = new JFrame("Program Output");
        outputWindow.setSize(400, 300);
        outputWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        outputWindow.setLayout(new BorderLayout());

        // Output display area in the separate window
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.GREEN);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputWindow.add(outputScrollPane, BorderLayout.CENTER);

        // Add listener for the Info button to show program information dialog
        infoButton.addActionListener(e -> showInfoDialog());
    }

/**
* Displays program information in a dialog box when the "Info" button is clicked.
*/
   
    public void showInfoDialog() {
        String infoMessage = """
                BASIC Interpreter Program - By: Aaron Kiah
                Last Edit: |Nov. 7, 2024|
                -------------------------
                This program interprets BASIC-like commands with support for:
                  - Arithmetic operations
                  - Loops
                  - Conditional statements

                Test Programs:
                --------------
                You can use the following test programs included in the program files:
                - Test Program A: Demonstrates a simple loop with arithmetic.
                - Test Program B: Shows an example of an endless loop.
                - Test Program C: Evaluates a complex arithmetic expression.

                Please refer to these examples for testing and understanding the interpreter.
                """;
        JOptionPane.showMessageDialog(mainFrame, infoMessage, "Program Information", JOptionPane.INFORMATION_MESSAGE);
    }

/**
* Displays the provided output text in the separate output window.
*
* @param output The output to display in the output window.
*/

    public void showOutput(String output) {
        outputArea.setText(output);
        outputWindow.setVisible(true); // Show the output window when output is ready
    }

/**
* Returns the "Run" button.
* 
* @return The button to run the BASIC code.
*/

    public JButton getRunButton() {
        return runButton;
    }

/**
* Returns the "Load" button.
* 
* @return The button to load a code file.
*/

    public JButton getLoadButton() {
        return loadButton;
    }

/**
* Returns the "Save" button.
* 
* @return The button to save the current code to a file.
*/

    public JButton getSaveButton() {
        return saveButton;
    }

/**
* Returns the "Reset" button.
* 
* @return The button to clear the code input area.
*/

    public JButton getResetButton() {
        return resetButton;
    }

/**
* Returns the "Info" button.
* 
* @return The button to display program information.
*/

    public JButton getInfoButton() {
        return infoButton;
    }

/**
* Returns the text entered in the code input area.
* 
* @return The BASIC code input by the user.
*/
    
    public String getCodeInput() {
        return codeArea.getText();
    }

/**
* Sets the text in the code input area to the specified code.
* 
* @param code The code to display in the code input area.
*/

    public void setCodeInput(String code) {
        codeArea.setText(code);
    }

/**
* Clears the text in the code input area.
*/

    public void clearCodeInput() {
        codeArea.setText("");
    }

/**
* Clears the text in the output area.
*/

    public void clearOutput() {
        outputArea.setText("");
    }

/**
* Displays an error message in a dialog box.
* 
* @param error The error message to display.
*/

    public void displayError(String error) {
        JOptionPane.showMessageDialog(mainFrame, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

/**
* Displays a general message in a dialog box.
* 
* @param message The message to display.
*/

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

/**
* Adds an ActionListener to the "Run" button.
* 
* @param listener The ActionListener to add.
*/

    public void addRunButtonListener(ActionListener listener) {
        runButton.addActionListener(listener);
    }

/**
* Adds an ActionListener to the "Load" button.
* 
* @param listener The ActionListener to add.
*/

    public void addLoadButtonListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

/**
* Adds an ActionListener to the "Save" button.
* 
* @param listener The ActionListener to add.
*/

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

/**
* Adds an ActionListener to the "Reset" button.
* 
* @param listener The ActionListener to add.
*/

    public void addResetButtonListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

/**
* Adds an ActionListener to the "Info" button.
* 
* @param listener The ActionListener to add.
*/

    public void addInfoButtonListener(ActionListener listener) {
        infoButton.addActionListener(listener);
    }
}
