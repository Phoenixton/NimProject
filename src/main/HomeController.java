package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by perri on 08/05/2018.
 */
public class HomeController {

    public Button genericEditingButton;
    public Button nymEditingButton;


    public void goToGenericEditing() throws IOException{
        Stage stage;
        Parent root;

        stage=(Stage) genericEditingButton.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("res/genericEditing.fxml"));
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm());

        stage.show();

    }

    public void goToNymEditing() throws IOException{
        Stage stage;
        Parent root;

        stage=(Stage) genericEditingButton.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("res/nymEditing.fxml"));
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);

        stage.setScene(scene);

        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm());
        stage.show();
    }

}
