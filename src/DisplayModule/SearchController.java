package DisplayModule;
import SearchModule.ProjectTesterImp;
import SearchModule.SearchResult;
import SearchModule.WebRequester;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SearchController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField SearchBar;
    @FXML
    private ListView<String> ResultsView;
    @FXML
    private TextField CrawlBar;
    @FXML
    private AnchorPane CrawlPane;

    ProjectTesterImp searcher;
    Boolean boost;
    public SearchController () {
        searcher = new ProjectTesterImp();
        boost = false;
    }

    public void switchToCrawlPane (MouseEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Assets/CrawlPane.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeMenu (MouseEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Assets/SearchPane.fxml")));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pageRankToggle () {
        boost = !boost;
    }

    public void crawlDataSet () { //no initialize method call as searcher calls initialize whenever crawl is called
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Crawling, please wait");
        alert.setHeaderText("Crawling in progress");
        alert.setContentText("Please do not close this window");

        if (!Objects.equals(CrawlBar.getText(), "")) {
            try {
                alert.show();
                WebRequester.readURL(CrawlBar.getText());
                searcher.crawl(CrawlBar.getText());
                alert.setHeaderText("Crawl finished");
                alert.setContentText("Crawl finished, you may now close this message");
            } catch (Exception e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("ERROR: INVALID URL");
                alert.setHeaderText("Invalid URL error");
                alert.setContentText("ERROR: invalid URL. Please make sure seed input is valid URL.");
                alert.show();
            }
        }
    }

    public void SearchQuery () {
        String query = SearchBar.getText();
        if (!Objects.equals(query, "")) {
            ResultsView.getItems().clear();
            for (SearchResult e : searcher.search(query, boost, 10)) {
                ResultsView.getItems().add(String.format("Title: %s, Score: %f", e.getTitle(), e.getScore()));
            }
        }
    }

}
