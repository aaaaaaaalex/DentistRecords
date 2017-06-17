import javafx.event.ActionEvent;

import java.util.ResourceBundle;

public abstract class ListController {
    abstract void setDataManager(DataManager d);
    abstract void setResourceBundle(ResourceBundle r);
    abstract void changeScene(ActionEvent e);

    // This method is intended to target a certain list-element
    // In the case of InvoiceController, to list invoices for
    // a target Patient.
    abstract void targetElement(int elementID);
}
