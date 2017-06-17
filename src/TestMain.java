import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


public class TestMain extends Application{

    private static boolean successfulLaunch = true;
    private static Exception e;

    //--------------------------------------
    // RESOURCES
    private static Locale currentLocale;
    public static ResourceBundle resBundle;
    private static DataManager dataManager;


    //-------------------------------------
    // Window elements / controllers
    static private Stage stage;
    static private Scene patientScene;
    static private ListController patientController, invoiceController, paymentController, procedureController;
    static private OptionsController options;
    static private Scene invoiceScene, procedureScene, paymentScene;
    static private Parent menu;


    public static void main (String[] args){

        try {
            // data management module can theoretically be swapped out just by changing this initialization
            dataManager = new DataManager_db();
            dataManager.readFromFile();
            currentLocale = dataManager.loadLocale();
            resBundle = ResourceBundle.getBundle("res.localeBundle", currentLocale);
        }catch(SQLException ex){
            // this will catch a null-pointer/ SQLException if any modules fail to initialise.
            // an error screen can then be presented to the user before the program terminates.

            System.out.println("An error has occurred. The program cannot be started.");
            successfulLaunch = false;
            e = ex;
        }

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        if(!successfulLaunch){
            FatalErrorScreen fe = new FatalErrorScreen(e);
            fe.display();
        }
        else {

            //FXMLLoader instantiates the appropriate controller class for every FXML file.
            //This is inconvenient if the controllers are intended to interact with data
            //from other classes/objects, since the line of inheritance must be broken in
            //order to pass arguments.

            // A new FXML Loader must be instantiated for every layout file, in order for the
            // getController() method to return a not-null value.

            FXMLLoader menuLoader = new FXMLLoader(
                    getClass().getResource("optionsRibbon.fxml"),
                    resBundle);
            menu = menuLoader.load();        //load the menu layout.
            options = menuLoader.getController();
            options.setResourceBundle(resBundle);
            options.setDataManager(dataManager);

            FXMLLoader pLoader = new FXMLLoader(
                    getClass().getResource("PatientManagement_layout.fxml"),
                    resBundle);
            VBox patientLayout = pLoader.load();
            patientLayout.getChildren().add(0, menu); // add the menu to index 0 of the layout.
            patientScene = new Scene(patientLayout);  // create new scene for each layout loaded.
            patientController = pLoader.getController(); // break the class hierarchy, retrieve the instantiated controller.
            patientController.setResourceBundle(resBundle); //provide the controller with the necessary resources.
            patientController.setDataManager(dataManager);

            FXMLLoader iLoader = new FXMLLoader(
                    getClass().getResource("InvoiceManagement_layout.fxml"),
                    resBundle);
            VBox invoiceLayout = iLoader.load();
            invoiceScene = new Scene(invoiceLayout);
            invoiceController = iLoader.getController();
            invoiceController.setResourceBundle(resBundle);
            invoiceController.setDataManager(dataManager);

            FXMLLoader procLoader = new FXMLLoader(
                    getClass().getResource("ProcedureManagement_layout.fxml"),
                    resBundle);
            VBox procedureLayout = procLoader.load();
            procedureScene = new Scene(procedureLayout);
            procedureController = procLoader.getController();
            procedureController.setResourceBundle(resBundle);
            procedureController.setDataManager(dataManager);

            FXMLLoader paymentLoader = new FXMLLoader(
                    getClass().getResource("PaymentManagement_layout.fxml"),
                    resBundle);
            VBox paymentLayout = paymentLoader.load();
            paymentScene = new Scene(paymentLayout);
            paymentController = paymentLoader.getController();
            paymentController.setResourceBundle(resBundle);
            paymentController.setDataManager(dataManager);


            stage = primaryStage;
            stage.setMinWidth(200);
            stage.setMinHeight(100);
            stage.setMaxHeight(600);
            stage.setMaxWidth(800);
            stage.setScene(patientScene);
            stage.show();
        }
    }

    public static void exit(){
        stage.close();
    }

    // This method is called by the Controller classes to change
    // the currently-displayed scene.
    public static void changeScene(ActionEvent e){

        String ID = ((Node)e.getSource()).getId();
        if(((Node) e.getSource()).getScene() == patientScene){
            if(!ID.equals("back")) {
                stage.setScene(invoiceScene);
                invoiceController.targetElement(Integer.valueOf(ID));
            }
        }else if(((Node) e.getSource()).getScene() == invoiceScene){
            if(ID.equals("back")) {
                stage.setScene(patientScene);
            }
            else{
                String parentID = ((Node) e.getSource()).getParent().getId();
                if(parentID.equals("viewPayment")){
                    paymentController.targetElement(Integer.valueOf(ID));
                    stage.setScene(paymentScene);
                }
                else if(parentID.equals("viewProc")){
                    procedureController.targetElement(Integer.valueOf(ID));
                    stage.setScene(procedureScene);
                }
            }
        }else if(((Node) e.getSource()).getScene() == paymentScene){
            if(ID.equals("back"))stage.setScene(invoiceScene);
        }else if(((Node) e.getSource()).getScene() == procedureScene){
            if(ID.equals("back"))stage.setScene(invoiceScene);
        }

        ((VBox)stage.getScene().getRoot()).getChildren().add(0, menu);
    }


}
