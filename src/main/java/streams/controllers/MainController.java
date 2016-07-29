package streams.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import streams.checks.Checks;
import streams.tasks.CustomTask;
import streams.tasks.Tasks;

import java.io.*;

/**
 * Created by mikhail.davydov on 2016/7/27.
 */
public class MainController {

    // default path in case of USERPROFILE variable couldn't be found
    final static String USER_PROFILE_PATH = System.getProperty("user.home");
    private boolean closedError;
    private int buffer;
    private Tasks tasks;

    CustomTask inputWorker;
    CustomTask outputWorker;
    CustomTask monitor;

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
            callAlertButton("Incorrect values entered\nPlease check the form");
            return;
        }

        // check input file exists
        InputStream is = null;
        try {
            is = new FileInputStream(input.getText());
        } catch (FileNotFoundException e) {
            callAlertButton("No input file exits");
            return;
        } finally {
            if (is != null) {
                is.close();
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
        inputWorker = tasks.createIn(input.getText());
        outputWorker = tasks.createOut(output.getText());
        monitor = tasks.monitorState(buffer);

        // bind progress to the task
        progress.progressProperty().unbind();
        progress.progressProperty().bind(monitor.progressProperty());

        // start copying process
        new Thread(inputWorker).start();
        new Thread(outputWorker).start();
        new Thread(monitor).start();
    }

    private void callAlertButton(String cause) {
        Alert alert = new Alert(Alert.AlertType.WARNING, cause);
        alert.setHeaderText(null);
        alert.setOnCloseRequest(closeEvent -> closedError = true);
        alert.showAndWait();

//        if (alert.getResult() == ButtonType.OK || closedError) {
//            // return to the main form
//            return;
//        }
    }

    public void pressCancelCopyButton(ActionEvent event) {
        // stop the process
        inputWorker.cancel();
        outputWorker.cancel();
        monitor.stopThread();

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
        if (inputWorker.getRun()) {
            inputWorker.pause();
            inputWorker.setRun(false);
            pauseInput.setText("Resume Input");
        } else {
            inputWorker.resume();
            inputWorker.setRun(true);
            pauseInput.setText("Pause Input");
        }
    }

    public void pressPauseOutput(ActionEvent event) throws InterruptedException {
        if (outputWorker.getRun()) {
            outputWorker.pause();
            outputWorker.setRun(false);
            pauseOutput.setText("Resume Output");
        } else {
            outputWorker.resume();
            outputWorker.setRun(true);
            pauseOutput.setText("Pause Output");
        }
    }

    private boolean isTrue() {
        buffer = Checks.isBufferInputCorrect(bufferInput.getText());
        return (Checks.isInputNull(input.getText(), output.getText(), bufferInput.getText()) || buffer < 1);
    }
}
