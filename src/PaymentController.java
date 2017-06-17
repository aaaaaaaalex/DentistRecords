import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.org.apache.xpath.internal.operations.Number;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PaymentController extends ListController{

    private int targetPatientID;
    private int targetInvoiceID;

    ResourceBundle r;
    private Label[] headerLabels;
    private DataManager dataManager;

    //-------------------------
    //GUI Element References
    @FXML
    GridPane paymentList;
    @FXML
    Button addPayment, removePayment, back;
    @FXML
    ScrollPane scrollBox;
    @FXML
    VBox contentPane;

    public PaymentController(){
        headerLabels = new Label[3];
        this.targetPatientID = -1;
        this.targetInvoiceID = -1;
    }


    //------------------------------------------------------------------
    //         Data Management
    //------------------------------------------------------------------
    public void addPayment(){
        String[] s = {r.getString("cash_amount"), r.getString("date")};
        s = ModalInputBox.getValues(s, r);
        Payment p;
        if(ModalInputBox.validate(s)) {
            try {
                p = new Payment();
                p.setPaymentAmnt(Integer.valueOf(s[0]));

                // Create a date format for parsing user input
                SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd", r.getLocale());
                Date pDate = dformat.parse(s[1]);
                p.setPaymentDate(pDate);
                dataManager.addPayment(p, this.targetInvoiceID, this.targetPatientID);
            } catch (NumberFormatException | ParseException e) {
                System.out.println("A parsing error occurred on input");
            }
        }
        this.updatePaymentList();
    }


    public void removePayment(){
        String[] s = {r.getString("identification")};
        try {
            int id = Integer.valueOf(ModalInputBox.getValues(s, r)[0]);
            dataManager.removePayment(id);
        }catch(NumberFormatException e){}

        this.updatePaymentList();
    }

    public void editPayment(ActionEvent e) {
    }

    //--------------------------------------------------------------------
    // Utility
    //--------------------------------------------------------------------

    // List generation function
    private void updatePaymentList(){

        this.paymentList.getChildren().remove(0, this.paymentList.getChildren().size());
        for(int i = 0; i < headerLabels.length; i++) {
            paymentList.add(headerLabels[i], i, 0);
        }
        Separator headSeparator = new Separator();
        headSeparator.setOrientation(Orientation.HORIZONTAL);
        paymentList.add(headSeparator, 0, 1, headerLabels.length, 1);

        //after removing obsolete elements from the list, repopulate with new Patients from the DataManager
        ArrayList<Payment> payments = dataManager.getPayments(this.targetPatientID, this.targetInvoiceID);
        if(payments != null) {
            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);

                //The row index of each element is i+2 so that the headers don't
                // get overwritten. Ideally, this could be made dynamic by getting
                // the number of existing rows in the gridLayout, however, I cant
                // find such a method.
                this.constructGridRow(paymentList, p, i + 2);
            }
        }else{
            System.out.println("array is null");
        }
    }

    // This process was taken out of the updatePatientList method
    // to allow for dynamic list population.
    private void constructGridRow(GridPane gridPane, Payment p, int rowNumber){

        Label pNo = new Label(String.valueOf(p.getPaymentNo()));
        pNo.setPadding(new Insets(0, 5, 0, 5));

        Label amount = new Label(String.valueOf(p.getPaymentAmnt()));
        amount.setPadding(new Insets(0, 5, 0, 5));

        Label date = new Label((p.getPaymentDate()).toString());
        date.setPadding(new Insets(0, 5, 0, 5));

        Button editPaymentButton = new Button(r.getString("edit"));
        editPaymentButton.setId(String.valueOf(p.getPaymentNo()));
        editPaymentButton.setOnAction(this::editPayment); //Thank the lord for interface auto-generation!

        gridPane.add(pNo, 0, rowNumber);
        gridPane.add(amount, 1, rowNumber);
        gridPane.add(date, 2, rowNumber);
    }

    private void initializeHeaders(){
        headerLabels[0] = new Label(r.getString("payment_no"));
        headerLabels[1] = new Label(r.getString("cash_amount"));
        headerLabels[2] = new Label(r.getString("date"));

        for(int i = 0; i < headerLabels.length; i++){
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
        updatePaymentList();
    }
    @Override
    public void setResourceBundle(ResourceBundle r){
        this.r = r;
        initializeHeaders();
    }
}
