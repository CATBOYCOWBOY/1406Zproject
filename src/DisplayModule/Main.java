package DisplayModule;
import SearchModule.ProjectTesterImp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    ProjectTesterImp searcher = new ProjectTesterImp();

    public static void main(String[] args) {launch(args);}

    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SearchPane.fxml")));
        primaryStage.setTitle("Fruit finder");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
