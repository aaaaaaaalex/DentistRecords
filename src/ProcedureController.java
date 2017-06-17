import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProcedureController extends ListController{

    private int targetInvoiceID;
    private int targetPatientID;

    ResourceBundle resBundle;
    private Label[] headerLabels;
    private DataManager dataManager;

    //-------------------------
    //GUI Element References
    @FXML
    GridPane procedureList;
    @FXML
    Button addProcedure, removeProcedure, back;
    @FXML
    ScrollPane scrollBox;
    @FXML
    VBox contentPane;

    public ProcedureController(){
        headerLabels = new Label[3];
        this.targetInvoiceID = -1;
        this.targetPatientID = -1;
    }


    //------------------------------------------------------------------
    //         Data Management
    //------------------------------------------------------------------
    public void addProcedure(){
        Procedure proc = ProcedureSelectionBox.viewProcedures(dataManager, this.resBundle);
        if(proc != null) {
            dataManager.addProcedure(proc, this.targetInvoiceID, this.targetPatientID);
            this.updateElementList();
        }
    }


    public void removeProcedure(){
        String[] s = {resBundle.getString("identification")};
        try {
            int id = Integer.valueOf(ModalInputBox.getValues(s, resBundle)[0]);
            dataManager.removeProcedure(this.targetPatientID, this.targetInvoiceID, id);
        }catch(NumberFormatException e){}

        this.updateElementList();
    }


    //--------------------------------------------------------------------
    // Utility
    //--------------------------------------------------------------------

    // List generation function
    private void updateElementList(){

        this.procedureList.getChildren().remove(0, this.procedureList.getChildren().size());
        for(int i = 0; i < headerLabels.length; i++) {
            procedureList.add(headerLabels[i], i, 0);
        }
        Separator headSeparator = new Separator();
        headSeparator.setOrientation(Orientation.HORIZONTAL);
        procedureList.add(headSeparator, 0, 1, headerLabels.length, 1);

        //after removing obsolete elements from the list, repopulate with new Patients from the DataManager
        ArrayList<Procedure> procedures = dataManager.getProcedures(this.targetPatientID, this.targetInvoiceID);
        if(procedures != null) {
            for (int i = 0; i < procedures.size(); i++) {
                Procedure proc = procedures.get(i);
                //The row index of each element is i+2 so that the headers don't
                // get overwritten. Ideally, this could be made dynamic by getting
                // the number of existing rows in the gridLayout, however, I cant
                // find such a method.
                this.constructGridRow(procedureList, proc, i + 2);
            }
        }
    }

    // This process was taken out of the updatePatientList method
    // to allow for dynamic list population.
    private void constructGridRow(GridPane gridPane, Procedure proc, int rowNumber){

        Label procNo = new Label(String.valueOf(proc.getProcNo()));
        procNo.setPadding(new Insets(0, 5, 0, 5));

        Label procName = new Label(String.valueOf(proc.getProcName()));
        procName.setPadding(new Insets(0, 5, 0, 5));

        Label cost = new Label(String.valueOf( proc.getProcCost() ));
        cost.setPadding(new Insets(0, 5, 0, 5));



        gridPane.add(procNo, 0, rowNumber);
        gridPane.add(procName, 1, rowNumber);
        gridPane.add(cost, 2, rowNumber);
    }

    private void initializeHeaders(){
        headerLabels[0] = new Label(resBundle.getString("procedure_no"));
        headerLabels[1] = new Label(resBundle.getString("name"));
        headerLabels[2] = new Label(resBundle.getString("cash_amount"));

        for(int i = 0; i < headerLabels .length; i++){
            headerLabels[i].setPadding(new Insets(0, 5, 0, 5));
        }
    }



    //----------------------------------------------------------------
    //Interface Methods allowing hierarchy-breaking method-calls for
    //extended control.
    @Override
    public void setDataManager(DataManager d) {
        this.dataManager = d;
    }
    @Override
    public void changeScene(ActionEvent e) {
        TestMain.changeScene(e);
    }

    @Override
    public void targetElement(int elementID) {
        this.targetInvoiceID = elementID;
        this.targetPatientID = dataManager.whoOwns_inv(this.targetInvoiceID);
        updateElementList();
    }
    @Override
    public void setResourceBundle(ResourceBundle r){
        this.resBundle = r;
        initializeHeaders();
    }
}
