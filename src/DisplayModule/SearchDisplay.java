package DisplayModule;
import SearchModule.SearchResult;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

public class SearchDisplay extends Pane {
    private ListView<String> searchResults;
    private TextField searchBar;
    Button crawlButton, rankButton, searchButton;

    SearchDisplay () throws IOException {
        //setting up elements of the GUI
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SearchPane.fxml")));
            getChildren().add(root);
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    public void update(List<SearchResult> results) {
        searchResults.getItems().clear();
        for (SearchResult e : results) {
            searchResults.getItems().add(String.format("%s : %f", e.getTitle(), e.getScore()));
        }
    }

}
