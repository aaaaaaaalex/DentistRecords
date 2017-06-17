import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class InvoiceController extends ListController{

    private int targetPatientID;

    ResourceBundle r;
    private Label[] headerLabels;
    private DataManager dataManager;

    //-------------------------
    //GUI Element References
    @FXML
    GridPane invoiceList;
    @FXML
    Button addInvoice, removeInvoice, back;
    @FXML
    ScrollPane scrollBox;
    @FXML
    VBox contentPane;

    public InvoiceController(){
        headerLabels = new Label[3];
        this.targetPatientID = -1;
    }


    //------------------------------------------------------------------
    //         Data Management
    //------------------------------------------------------------------
    public void addInvoice(){
        String[] s = {r.getString("date")};
        s = ModalInputBox.getValues(s, r);

        // Check if strings are null, and if not, if they are empty, not-null
        if(ModalInputBox.validate(s)) {
            try {
                Invoice inv = new Invoice();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", r.getLocale());
                Date d = df.parse(s[0]);
                inv.setInvoiceDate(d);
                dataManager.addInvoice(this.targetPatientID, inv);

            }catch(NumberFormatException e){
                System.out.println("string was not successfully converted. (InvoiceController:addInvoice)");
            } catch (ParseException e) {
                System.out.println("Date formatting error");
            }
        }
        this.updateElementList();
    }


    public void removeInvoice(){
        String[] s = {r.getString("identification")};

        try {
            int id = Integer.valueOf(ModalInputBox.getValues(s, r)[0]);
            dataManager.removeInvoice(this.targetPatientID, id);
        }catch(NumberFormatException e){}

        this.updateElementList();
    }

    public void editInvoice(ActionEvent e){
        Invoice inv;
        String[] s = {r.getString("date")};
        s = ModalInputBox.getValues(s, r);

        // Check if strings are null, and if not, if they are empty, not-null
        if(ModalInputBox.validate(s)){
            int id = Integer.valueOf(((Button)e.getSource()).getId()); //find the ID of the invoice
            inv = dataManager.findInvoice(this.targetPatientID, id);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", r.getLocale());
            try {
                Date date = df.parse(s[0]);
                inv.setInvoiceDate(date);
            } catch (ParseException e1) {
                System.out.println("Date format failed");
                e1.printStackTrace();
            }

            if(dataManager.findInvoice(this.targetPatientID, id) != null) {
                dataManager.removeInvoice(this.targetPatientID, id);
                dataManager.addInvoice(this.targetPatientID, inv);
            }
        }

        this.updateElementList();
    }

    //--------------------------------------------------------------------
    // Utility
    //--------------------------------------------------------------------

    // List generation function
    private void updateElementList(){

        this.invoiceList.getChildren().remove(0, this.invoiceList.getChildren().size());
        for(int i = 0; i < headerLabels.length; i++) {
            invoiceList.add(headerLabels[i], i, 0);
        }
        Separator headSeparator = new Separator();
        headSeparator.setOrientation(Orientation.HORIZONTAL);
        invoiceList.add(headSeparator, 0, 1, headerLabels.length, 1);

        //after removing obsolete elements from the list, repopulate with new Patients from the DataManager
        ArrayList<Invoice> invoices = dataManager.getInvoices(this.targetPatientID);
        if(invoices != null) {
            for (int i = 0; i < invoices.size(); i++) {
                Invoice inv = invoices.get(i);

                //The row index of each element is i+2 so that the headers don't
                // get overwritten. Ideally, this could be made dynamic by getting
                // the number of existing rows in the gridLayout, however, I cant
                // find such a method.
                this.constructGridRow(invoiceList, inv, i + 2);
            }
        }
    }

    // This process was taken out of the updatePatientList method
    // to allow for dynamic list population.
    private void constructGridRow(GridPane gridPane, Invoice inv, int rowNumber){

        Label invNo = new Label(String.valueOf(inv.getInvoiceNo()));
        invNo.setPadding(new Insets(0, 5, 0, 5));

        Label amount = new Label(String.valueOf(inv.getInvoiceAmnt()));
        amount.setPadding(new Insets(0, 5, 0, 5));

        Label date = new Label((inv.getInvoiceDate()).toString());
        date.setPadding(new Insets(0, 5, 0, 5));

        Button editInvoiceButton = new Button(r.getString("edit"));
        editInvoiceButton.setId(String.valueOf(inv.getInvoiceNo()));
        editInvoiceButton.setOnAction(this::editInvoice); //Thanks for the interface auto-generation!

        Pane procButtonContainer = new Pane();
        procButtonContainer.setId("viewProc");
        Button viewInvoiceProcsButton = new Button(
                r.getString("view")
                        + "("
                        + r.getString("procedure")
                        + ")"
        );
        Pane payButtonContainer = new Pane();
        payButtonContainer.setId("viewPayment");
        Button viewInvoicePaysButton = new Button(
                r.getString("view")
                        + "("
                        + r.getString("payment")
                        + ")"
        );

        viewInvoiceProcsButton.setId(String.valueOf(inv.getInvoiceNo()));
        viewInvoicePaysButton.setId(String.valueOf(inv.getInvoiceNo()));

        viewInvoiceProcsButton.setOnAction(this::changeScene);
        viewInvoicePaysButton.setOnAction(this::changeScene);

        procButtonContainer.getChildren().add(viewInvoiceProcsButton);
        payButtonContainer.getChildren().add(viewInvoicePaysButton);

        gridPane.add(invNo, 0, rowNumber);
        gridPane.add(amount, 1, rowNumber);
        gridPane.add(date, 2, rowNumber);
        gridPane.add(editInvoiceButton, 4, rowNumber);
        gridPane.add(procButtonContainer, 5, rowNumber);
        gridPane.add(payButtonContainer, 6, rowNumber);
    }

    private void initializeHeaders(){
        headerLabels[0] = new Label(r.getString("invoice_no"));
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
        this.updateElementList();
    }
    @Override
    public void changeScene(ActionEvent e) {
        TestMain.changeScene(e);
    }

    @Override
    public void targetElement(int elementID) {
        this.targetPatientID = elementID;
        updateElementList();
    }
    @Override
    public void setResourceBundle(ResourceBundle r){
        this.r = r;
        initializeHeaders();
    }
}
