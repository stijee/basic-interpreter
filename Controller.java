import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;

/**
 * 
 * CONTROLLER.JAVA - WRITTEN BY AARON KIAH - LAST UPDATE (NOV. 7, 2024)
 * 
 * The Controller class connects the Model and View, facilitating
 * the interaction between the user interface and program logic.
 * It handles user actions such as running code, loading and saving files, 
 * and resetting or displaying information.
 */

public class Controller {
    private View view;
    private Model model;

/**
* Initializes the controller with references to the view and model.
* Sets up listeners for various user actions.
*
* @param view The view component of the program.
* @param model The model component of the program.
*/

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        // Set up listeners for the buttons in the view
        view.addRunButtonListener(new RunButtonListener());
        view.addLoadButtonListener(new LoadButtonListener());
        view.addSaveButtonListener(new SaveButtonListener());
        view.addResetButtonListener(new ResetButtonListener());
        view.addInfoButtonListener(new InfoButtonListener());
    }

/**
* Inner class for handling the "Run" button action.
* When the "Run" button is pressed, it initiates the program's execution.
*/

    private class RunButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            runCode();
        }
    }

/**
* Executes the code entered in the view by loading it into the model,
* running it, and displaying the output in a separate output window.
*/

    private void runCode() {
        System.out.println("Running code from view...");
        
        // Get the code from the view
        String code = view.getCodeInput();
        
        model.loadProgram(code);           // Load the program into Model
        model.runProgram();                // Execute the loaded program
        String output = model.getOutput(); 
        view.showOutput(output);           
        
        System.out.println("Run completed. Output displayed.");
    }
    
/**
* Inner class for handling the "Load" button action.
* When the "Load" button is pressed, it opens a file chooser to load a BASIC code file,
* reads the contents, and displays them in the code input area.
*/

    private class LoadButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null); // Using null as the parent component
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder code = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        code.append(line).append("\n");
                    }
                    view.setCodeInput(code.toString()); // Display loaded code in the code area
                    view.displayMessage("File loaded successfully.");
                } catch (IOException ex) {
                    view.displayError("Error loading file: " + ex.getMessage());
                }
            }
        }
    }

/**
* Inner class for handling the "Save" button action.
* When the "Save" button is pressed, it opens a file chooser to specify a save location,
* and writes the contents of the code input area to the selected file.
*/

    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null); // Using null as the parent component
            
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(view.getCodeInput()); // Write the code area content to file
                    view.displayMessage("File saved successfully.");
                } catch (IOException ex) {
                    view.displayError("Error saving file: " + ex.getMessage());
                }
            }
        }
    }

/**
* Inner class for handling the "Reset" button action.
* When the "Reset" button is pressed, it clears the code input and output areas in the view.
*/

    private class ResetButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.clearCodeInput();
            view.clearOutput();
        }
    }

/**
* Inner class for handling the "Info" button action.
* When the "Info" button is pressed, it displays a dialog with information about the program.
*/

    private class InfoButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.showInfoDialog(); // Show the program information dialog
        }
    }
}
