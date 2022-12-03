package DisplayModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {launch(args);}

    @Override
    public void start (Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("Assets/SearchPane.fxml")));

            Image icon = new Image(new File("src/DisplayModule/Assets/ICON.png").toURI().toString());

            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("FrootFinder 2K23");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
