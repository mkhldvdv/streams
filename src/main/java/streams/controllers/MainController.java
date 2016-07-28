package streams.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import streams.checks.Checks;
import streams.tasks.Tasks;

import java.io.File;
import java.io.IOException;

/**
 * Created by mikhail.davydov on 2016/7/27.
 */
public class MainController {

    // default path in case of USERPROFILE variable couldn't be found
    final static String USER_PROFILE_PATH = System.getProperty("user.home");
    private boolean closedError;
    private int buffer;
    private Tasks tasks;

    Task inputWorker;
    Task outputWorker;

    @FXML
    private Button start;
    @FXML
    private TextField input;
    @FXML
    private Button inputFile;
    @FXML
    private TextField output;
    @FXML
    private Button outputFile;
    @FXML
    private TextField bufferInput;
    @FXML
    private ProgressBar progress;
    @FXML
    private Button pauseInput;
    @FXML
    private Button pauseOutput;
    @FXML
    private Button exit;
    @FXML
    private Button cancelCopy;

    public void pressExitButton(ActionEvent event) {
        // close the application
        Platform.exit();
    }

    public void pressStartButton(ActionEvent event) throws IOException {
        // check input fields
        if (isTrue()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect values entered\nPlease check the form");
            alert.setHeaderText(null);
            alert.setOnCloseRequest(closeEvent -> closedError = true);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK || closedError) {
                // return to the main form
                return;
            }
        }

        tasks = new Tasks(buffer);

        input.setDisable(true);
        inputFile.setDisable(true);
        output.setDisable(true);
        outputFile.setDisable(true);
        bufferInput.setDisable(true);
        progress.setVisible(true);
        pauseInput.setVisible(true);
        pauseOutput.setVisible(true);
        start.setDisable(true);
        exit.setVisible(false);
        cancelCopy.setVisible(true);

        progress.setProgress(0);
        inputWorker = tasks.createIn(input.getText(), buffer);
        outputWorker = tasks.createOut(output.getText());

        // bind progress to the task
        progress.progressProperty().unbind();
        progress.progressProperty().bind(inputWorker.progressProperty());

        // start copying process
        try {
            new Thread(inputWorker).start();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect values entered\nPlease check the main form");
            alert.setHeaderText(null);
            alert.setOnCloseRequest(closeEvent -> closedError = true);
            alert.showAndWait();
        }
        new Thread(outputWorker).start();
    }

    public void pressCancelCopyButton(ActionEvent event) {
        // stop the process
        inputWorker.cancel();
        outputWorker.cancel();

        input.setDisable(false);
        inputFile.setDisable(false);
        output.setDisable(false);
        outputFile.setDisable(false);
        bufferInput.setDisable(false);
        progress.setVisible(false);
        pauseInput.setVisible(false);
        pauseOutput.setVisible(false);
        start.setDisable(false);
        exit.setVisible(true);
        cancelCopy.setVisible(false);

        // unbind progress to the task
        progress.progressProperty().unbind();
        progress.setProgress(0);
    }

    public void pressInputFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select input file");
        chooser.setInitialDirectory(new File(USER_PROFILE_PATH));
        chooser.getExtensionFilters().clear();

        // start file chooser by clicking on the button
        Stage stage = new Stage();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            input.setText(file.getAbsolutePath());
        }
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

    public void pressPauseInput(ActionEvent event) throws InterruptedException {
        if (inputWorker.isRunning()) {
            inputWorker.wait();
            pauseInput.setText("Resume Input");
        } else {
            inputWorker.notify();
            pauseInput.setText("Pause Input");
        }
    }

    private boolean isTrue() {
        buffer = Checks.isBufferInputCorrect(bufferInput.getText());
        return (Checks.isInputNull(input.getText(), output.getText(), bufferInput.getText()) || buffer < 1);
    }
}
