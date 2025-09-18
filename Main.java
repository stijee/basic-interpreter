/**
 * 
 * MAIN.JAVA - WRITTEN BY AARON KIAH - LAST UPDATE (NOV. 7, 2024)
 * 
 * The Main class serves as the entry point for the BASIC interpreter program.
 * It initializes the View, Model, and Controller components, setting up the 
 * MVC (Model-View-Controller) structure for the application.
 */

public class Main {
    
/**
* The main method creates instances of the View and Model, then
* instantiates the Controller to link them. This setup initiates
* the BASIC interpreter's user interface and backend logic.
*
* @param args Command-line arguments (not used in this program).
*/
    public static void main(String[] args) {
        // Create instances of the View and Model
        View view = new View();
        Model model = new Model();

        // Create the Controller, passing the View and Model
        Controller controller = new Controller(view, model);

        
    }
}
