// I decided not to create an FXML file for this scene, since it
// will be instantiated many times across many classes,
// making the FXMLLoader-based option very inefficient.

// This class is also made solely for returning information,
// meaning that to use FXMLLoader, inheritance would be proken
// when passing up data.

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.util.ResourceBundle;

public class ModalInputBox {

    // This static method takes an array of strings,
    // creating a text-input field and label for each element.
    public static String[] getValues(String[] values, ResourceBundle r){
        //Make a String array 'response' that contains an answer for every input
        String[] response = new String[values.length];

        Label[] labels = new Label[values.length];
        TextField[] fields = new TextField[values.length];;
        GridPane layout = new GridPane();

        Button submit = new Button(r.getString("submit"));
        Button cancel = new Button(r.getString("cancel"));

        Scene scene;
        Stage stage = new Stage();

        // Add a label and text field to every row, at columns 0 and 1,
        // then add the Submit and Cancel buttons directly beneath them.

        int rowCount = 0;
        for(int i = 0; i < values.length; i++){
            labels[i] = new Label(values[i]);
            fields[i] = new TextField();

            layout.add(labels[i], 0, i);
            layout.add(fields[i], 1, i);

            rowCount = i+1;
        }

        layout.add(submit, 0, rowCount);
        layout.add(cancel, 1, rowCount);

        submit.setOnAction(e -> {
                    for (int i = 0; i < values.length; i++) {
                        response[i] = fields[i].getText();
                    }
                    stage.close();
                }
        );

        cancel.setOnAction( e -> stage.close() );

        scene = new Scene(layout);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return response;
    }


    public static boolean validate(String[] values){;
        for(int i = 0; i < values.length; i++){
            if(values[i] == null || values[i].equals("")){
                return false;
            }
        }
        return true;
    }


}
