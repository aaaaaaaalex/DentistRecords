import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;

import java.util.Locale;
import java.util.ResourceBundle;

public class OptionsController extends ListController{

    private DataManager dataManager;
    private ResourceBundle resBundle;

    public OptionsController(){

    }

    public void save(){
        this.dataManager.writeToFile();
    }
    public void exit(){
        dataManager.writeToFile();
        TestMain.exit();
    }

    public void maintainProcs(){
        if(resBundle == null) System.out.println("OptionsController::maintainProcs: problem here");
        ProcedureSelectionBox.viewProcedures(dataManager, resBundle);
    }

    public void triggerReport(){
        dataManager.generateReport(".//" , "patients.txt");
    }
    public void triggerFilteredReport(){
        dataManager.generateReport(".//", "overdue_patients.txt", 6);
    }
    public void changeLanguage(ActionEvent e){
        String language = ((RadioMenuItem)e.getSource()).getId();
        Locale l = new Locale(language);
        dataManager.saveLocale(l);
    }


    @Override
    void setDataManager(DataManager d) {
        this.dataManager = d;
    }

    @Override
    void setResourceBundle(ResourceBundle r) {
        this.resBundle = r;
    }

    @Override
    void changeScene(ActionEvent e) {}

    @Override
    void targetElement(int elementID) {}
}
