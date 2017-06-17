import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FatalErrorScreen {

    private String errorMessage;

    public FatalErrorScreen(Exception e){
        this.errorMessage = e.toString();
        StackTraceElement[] st = e.getStackTrace();
        for(int i = 0; i < st.length; i++){
            this.errorMessage += "\n" + st[i].toString();
        }
    }


    public void display(){
        VBox root = new VBox();
        Label notification = new Label("A fatal error has occurred on startup.The application cannot be launched. Your MySQL database may not be running.");
        notification.setTextFill(Color.web("#ff0000"));
        notification.setPadding(new Insets(0, 0, 30, 0));

        Label errorLabel = new Label();
        errorLabel.setText(this.errorMessage);

        ScrollPane sp = new ScrollPane();
        sp.prefHeight(400);
        sp.prefWidth(500);

        sp.setContent(errorLabel);
        root.getChildren().addAll(notification, sp);

        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        Scene scene = new Scene(root, 600, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
