import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientController extends ListController{

    private int targetPatient;

    ResourceBundle r;
    private Label[] headerLabels;
    private DataManager dataManager;

    //-------------------------
    //GUI Element References
    @FXML
    GridPane patientList;
    @FXML
    Button addPatient, removePatient;
    @FXML
    ScrollPane scrollBox;
    @FXML
    VBox contentPane;

    public PatientController(){
        headerLabels = new Label[4];
        this.targetPatient = -1;
    }


    //------------------------------------------------------------------
    //         Data Management
    //------------------------------------------------------------------
    public void addPatient(){
        String[] s = {r.getString("name"), r.getString("address"), r.getString("phone_number")};
        s = ModalInputBox.getValues(s, r);

        // Check if strings are null, and if not, if they are empty, not-null
        if((s[0] != null &&  s[1]!= null)  &&  !(s[0].equals("") || s[1].equals(""))) {
            Patient p = new Patient(s[0], s[1]);
            try {
                if (s[2] != null && !s[2].equals("")) p.setPhoneNo(Integer.valueOf(s[2]));
            } catch (NumberFormatException e){
                p.setPhoneNo(0);
            }
            dataManager.addPatient(p);
        }
        this.updatePatientList();
    }

    public void removePatient(){
        String[] s = {r.getString("identification")};

        try {
            int id = Integer.valueOf(ModalInputBox.getValues(s, r)[0]);
            dataManager.removePatient(id);
        }catch(NumberFormatException e){}

        this.updatePatientList();
    }
    public void editPatient(ActionEvent e){
        String[] s = {r.getString("name"), r.getString("address"), r.getString("phone_number")};
        s = ModalInputBox.getValues(s, r);
        Patient p;

        // Check if strings are null, and if not, if they are empty, not-null
        if(ModalInputBox.validate(s)) {
            try {
                int patientID = Integer.valueOf(((Button) e.getSource()).getId());
                p = dataManager.findPatient(patientID);
                if (p != null) {

                    p.setPhoneNo(Integer.valueOf(s[2]));
                    p.setName(s[0]);
                    p.setAddress(s[1]);

                    dataManager.removePatient(p.getPatientNo());
                    dataManager.addPatient(p);
                }
            } catch(NumberFormatException ex){
                System.out.println("Phone number invalid");
            }
        }


        this.updatePatientList();
    }

    //--------------------------------------------------------------------
    // Utility
    //--------------------------------------------------------------------

    // List generation function
    private void updatePatientList(){
        ArrayList<Patient> patients = dataManager.getPatients();

        this.patientList.setGridLinesVisible(true);
        this.patientList.getChildren().remove(0, this.patientList.getChildren().size());
        for(int i = 0; i < headerLabels.length; i++) {
            patientList.add(headerLabels[i], i, 0);
        }
        Separator headSeparator = new Separator();
        headSeparator.setOrientation(Orientation.HORIZONTAL);
        patientList.add(headSeparator, 0, 1, headerLabels.length, 1);

        //after removing obsolete elements from the list, repopulate with new Patients from the DataManager
        //only print all patients if no specific patient is specified.
        if(this.targetPatient == -1) {
            for (int i = 0; i < patients.size(); i++) {
                Patient p = patients.get(i);

                //The row index of each element is i+2 so that the headers don't
                // get overwritten. Ideally, this could be made dynamic by getting
                // the number of existing rows in the gridLayout, however, I cant
                // find such a method.
                this.constructGridRow(patientList, p, i+2);

            }
        }else{
            Patient p = dataManager.findPatient(this.targetPatient);
            this.constructGridRow(patientList, p, 2);
        }
    }

    // This process was taken out of the updatePatientList method
    // to allow for dynamic list population.
    private void constructGridRow(GridPane gridPane, Patient p, int rowNumber){
        Label name = new Label(p.getName());
        name.setPadding(new Insets(0, 5, 0, 5));

        Label address = new Label(p.getAddress());
        address.setPadding(new Insets(0, 5, 0, 5));

        Label phoneNo = new Label(String.valueOf(p.getPhoneNo()));
        phoneNo.setPadding(new Insets(0, 5, 0, 5));

        Label ID = new Label(String.valueOf(p.getPatientNo()));
        ID.setPadding(new Insets(0, 5, 0, 5));

        Button editPatientButton = new Button(r.getString("edit"));
        editPatientButton.setId(String.valueOf(p.getPatientNo()));
        editPatientButton.setOnAction(this::editPatient); //Thank the lord for interface auto-generation!

        Button viewPatientButton = new Button(r.getString("view"));
        viewPatientButton.setId(String.valueOf(p.getPatientNo()));
        viewPatientButton.setOnAction(this::changeScene);

        gridPane.add(name, 0, rowNumber);
        gridPane.add(address, 1, rowNumber);
        gridPane.add(phoneNo, 2, rowNumber);
        gridPane.add(ID, 3, rowNumber);
        gridPane.add(editPatientButton, 4, rowNumber);
        gridPane.add(viewPatientButton, 5, rowNumber);
    }

    private void initializeHeaders(){
        headerLabels[0] = new Label(r.getString("name"));
        headerLabels[1] = new Label(r.getString("address"));
        headerLabels[2] = new Label(r.getString("phone_number"));
        headerLabels[3] = new Label(r.getString("identification"));

        for(int i = 0; i < headerLabels.length; i++){
            headerLabels[i].setPadding(new Insets(0, 5, 0, 5));
        }
    }



    //----------------------------------------------------------------
    //Interface Methods allowing inheritance-breaking method-calls for
    //extended control.
    @Override
    public void setDataManager(DataManager d) {
        this.dataManager = d;
        this.updatePatientList();
    }

    @Override
    public void changeScene(ActionEvent e) {
        TestMain.changeScene(e);
    }

    @Override
    public void targetElement(int elementID) {
        this.targetPatient = elementID;
    }

    @Override
    public void setResourceBundle(ResourceBundle r){
        this.r = r;
        initializeHeaders();
    }
}
