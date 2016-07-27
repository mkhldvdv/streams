package streams;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by mikhail.davydov on 2016/7/27.
 */
public class StreamsApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/main.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent mainWindow = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Streams init");
        stage.setScene(new Scene(mainWindow));
        stage.show();
    }
}
