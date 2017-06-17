// this class builds the procedure selection window.
// this class is used for data retrieval, so
// managing it will be easier if it isn't written
// in FXML

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProcedureSelectionBox {

    private static int chosenProc;
    private static Stage popup;
    private static GridPane procContainer;
    private static ArrayList<Procedure> procs;
    private static Insets padding;
    private static ResourceBundle resBundle;

    public static Procedure viewProcedures(DataManager dataManager, ResourceBundle res){
        procs = dataManager.getProcedures();
        resBundle = res;
        chosenProc = -1;

        padding = new Insets(0, 0, 0, 6);
        popup = new Stage();
        Scene scene;
        procContainer = new GridPane();
        ScrollPane gridHolder = new ScrollPane(procContainer);
        gridHolder.setPrefSize(400, 300);
        HBox contentHolder = new HBox(gridHolder);

        VBox controls = new VBox();
        controls.setPadding(padding);

        Button addProc = new Button(
                resBundle.getString("add")
        );
        addProc.setOnAction( e -> {
            String[] values = {
                    resBundle.getString("name"),
                    resBundle.getString("cost")
            };
            values = ModalInputBox.getValues(values, resBundle);
            if(ModalInputBox.validate(values)){
                Procedure p = new Procedure(values[0], Double.valueOf(values[1]));
                dataManager.addProcedure(p);

                refreshGrid(dataManager);
            }
        });

        Button removeProc = new Button(
                resBundle.getString("remove")
        );
        removeProc.setOnAction( e -> {
            String[] s = {resBundle.getString("identification")};
            s = ModalInputBox.getValues(s, resBundle);
            if(ModalInputBox.validate(s)){
                int ID;
                try{
                    ID = Integer.valueOf(s[0]);
                    dataManager.removeProcedure(ID);
                }catch(NumberFormatException ex){
                    System.out.println("ID could not be parsed as a number");
                }
            }
            refreshGrid(dataManager);
        });

        controls.getChildren().addAll(addProc, removeProc);
        contentHolder.getChildren().add(controls);
        scene = new Scene(contentHolder);

        refreshGrid(dataManager);

        popup.setScene(scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();

        if(chosenProc == -1) return null;
        return procs.get(chosenProc);
    }


    //called only by this class, is used to handle changes in the procedure list
    private static void refreshGrid(DataManager dataManager){
        procContainer.getChildren().clear();
        Label[] headers = {
                new Label(resBundle.getString("procedure_no")),
                new Label(resBundle.getString("name")),
                new Label(resBundle.getString("cost"))
        };

        for(int i = 0; i < headers.length; i++){
            procContainer.add(headers[i], i, 0);
            headers[i].setPadding(padding);
        }

        procs = dataManager.getProcedures();

        for(int i = 0; i < procs.size(); i++){
            Procedure p = procs.get(i);
            Label p_no =  new Label(String.valueOf(p.getProcNo()));
            p_no.setPadding(padding);
            Label p_name = new Label(p.getProcName());
            p_name.setPadding(padding);
            Label p_cost = new Label(String.valueOf(p.getProcCost()));
            p_cost.setPadding(padding);

            procContainer.add(p_no, 0, i+1);
            procContainer.add(p_name, 1, i+1);
            procContainer.add(p_cost, 2, i+1);

            //add a select button for every list entry
            Button select = new Button(resBundle.getString("select"));
            select.setPadding(padding);
            select.setId(String.valueOf(i));
            select.setOnAction(e -> {
                        chosenProc = Integer.valueOf(select.getId());
                        popup.close();
                    }
            );
            procContainer.add(select, 3, i+1);
        }
    }


}
