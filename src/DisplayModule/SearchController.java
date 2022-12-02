package DisplayModule;
import SearchModule.ProjectTesterImp;
import SearchModule.Result;
import SearchModule.SearchResult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    public TextField CrawlBar;

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
        searcher.crawl(CrawlBar.getText());
    }

    public void SearchQuery () {
        System.out.println("here");
        String query = SearchBar.getText();
        if (!Objects.equals(query, "")) {
            for (SearchResult e : searcher.search(query, boost, 10)) {
                ResultsView.getItems().add(String.format("Title: %s, Score: %f", e.getTitle(), e.getScore()));
            }
        }
    }

}
