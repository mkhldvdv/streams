package streams.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by mikhail.davydov on 2016/7/27.
 */
public class MainController {

    // default path in case of USERPROFILE variable couldn't be found
    final static String USER_PROFILE_PATH = System.getProperty("user.home");

    @FXML
    private Button start;

    @FXML
    private TextField input;

    @FXML
    private TextField output;

    @FXML
    private TextField buffer;

    public void pressStartButton(ActionEvent event) throws IOException {
        // hide the main window
        ((Stage) start.getScene().getWindow()).close();

        // open the flow window
        String fxmlFile = "/fxml/flow.fxml";
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent flowWindow = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Streams control");
        stage.setScene(new Scene(flowWindow));
        stage.show();
    }

    public void pressCancelButton(ActionEvent event) {
        // close the application
        Platform.exit();
    }

    public void pressInputFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select input file");
        chooser.setInitialDirectory(new File(USER_PROFILE_PATH));
        chooser.getExtensionFilters().clear();

        // start file chooser by clicking on the button
        Stage stage = new Stage();
        File file = chooser.showOpenDialog(stage);
        if (file != null) input.setText(file.getAbsolutePath());
    }

    public void pressOutputFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select output file");
        chooser.setInitialDirectory(new File(USER_PROFILE_PATH));
        chooser.getExtensionFilters().clear();

        // start file chooser by clicking on the button
        Stage stage = new Stage();
        File file = chooser.showSaveDialog(stage);
        if (file != null) output.setText(file.getAbsolutePath());
    }

    public void checkBufferField(ActionEvent event) {

    }
}
