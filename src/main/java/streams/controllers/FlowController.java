package streams.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mikhail.davydov on 2016/7/27.
 */
public class FlowController {

    @FXML
    private Button flowCancel;

    public void pressCancelButton(ActionEvent event) throws IOException {
        // close the flow window
        ((Stage) flowCancel.getScene().getWindow()).close();

        // open the main window
        String fxmlFile = "/fxml/main.fxml";
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent mainWindow = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Streams init");
        stage.setScene(new Scene(mainWindow));
        stage.show();
    }
}
