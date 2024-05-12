package doodledo.lab08_1b_210041160;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PokeViewController implements Initializable {
    @FXML
    private Button add_favourite_button;

    @FXML
    private VBox chosenCard;

    @FXML
    private Label chosenHeight;

    @FXML
    private ImageView chosenImage;

    @FXML
    private Label chosenName;

    @FXML
    private Label chosenWeight;

    @FXML
    private Label chosenID;

    @FXML
    private Button favourites_view;

    @FXML
    private Button home_view;

    @FXML
    private Button remove_favourite_button;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TextField searchbox;

    @FXML
    private Button searchbutton;

    private List<Pokemon> pokemons = new ArrayList<>();
    private int currentPage = 0;
    private static final int ENTRIES_PER_PAGE = 20;
    private MyListener myListener;
    private Pokemon chosenPokemon;

    private boolean favouritesOnly = false;
    private String currentSearchTerm = null;

    private List<Pokemon> getDate(int page, boolean favouritesOnly, String searchTerm) throws SQLException, ClassNotFoundException {
        List<Pokemon> pokemons = new ArrayList<>();
        Connection con = DbConnection.connect();
        Statement stmt = con.createStatement();
        String query = "SELECT id, identifier, height, weight, favourite FROM pokemon";
        boolean whereClauseAdded = false;
        if (favouritesOnly) {
            query += " WHERE favourite IS NOT NULL AND favourite = 1";
            whereClauseAdded = true;
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            if (!whereClauseAdded) {
                query += " WHERE";
                whereClauseAdded = true;
            } else {
                query += " AND";
            }
            query += " LOWER(identifier) LIKE '%" + searchTerm.toLowerCase() + "%'";
        }
        query += " LIMIT " + (page * ENTRIES_PER_PAGE) + ", " + ENTRIES_PER_PAGE;
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("identifier");
            String height = rs.getString("height");
            String weight = rs.getString("weight");
            int favourite = rs.getInt("favourite");
            String imgSrc = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + id + ".png";
            Pokemon pokemon = new Pokemon(id, name, height, weight, imgSrc);
            pokemon.setFavourite(favourite);
            pokemons.add(pokemon);
        }
        DbConnection.close(rs, stmt, con);
        return pokemons;
    }

    private void setChosenPokemon(Pokemon pokemon) {
        chosenPokemon = pokemon;
        chosenName.setText(pokemon.getName());
        chosenID.setText("#" + pokemon.getID());
        chosenHeight.setText(pokemon.getHeight());
        chosenWeight.setText(pokemon.getWeight());
        chosenImage.setImage(new javafx.scene.image.Image(pokemon.getImgSrc()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPage(currentPage, favouritesOnly, true, currentSearchTerm);

        scroll.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == scroll.getVmax()) {
                loadPage(++currentPage, favouritesOnly, false, currentSearchTerm);
            }
        });
    }

    private void loadPage(int page, boolean favouritesOnly, boolean isNewView, String searchTerm) {
        if (isNewView) {
            pokemons.clear();
            grid.getChildren().clear();
        }

        try {
            try {
                pokemons.addAll(getDate(page, favouritesOnly, searchTerm));
                if(pokemons.size()>0){
                    setChosenPokemon(pokemons.get(0));
                    myListener=new MyListener() {
                        @Override
                        public void onClickListener(Pokemon pokemon) {
                            setChosenPokemon(pokemon);
                        }
                    };
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int column=0, row=0;

        for(int i=0; i<pokemons.size(); i++){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/doodledo/lab08_1b_210041160/Item.fxml"));
            AnchorPane anchorPane = null;

            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ItemController controller = fxmlLoader.getController();

            controller.setData(pokemons.get(i), myListener);

            if(column==3){
                column=0;
                row++;
            }

            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

            grid.add(anchorPane, column++, row);
            GridPane.setMargin(anchorPane, new Insets(5));
        }
    }

    @FXML
    private void handleAddFavouriteButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if(chosenPokemon.getFavourite()==1){
            System.out.println("Already in favourites");
            return;
        }

        chosenPokemon.setFavourite(1);

        DbConnection.executeUpdate("UPDATE pokemon SET favourite = 1 WHERE id = " + chosenPokemon.getID());
        System.out.println("Added to favourites");
    }

    @FXML
    private void handleRemoveFavouriteButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (chosenPokemon.getFavourite() == 1) {
            chosenPokemon.setFavourite(0);

            DbConnection.executeUpdate("UPDATE pokemon SET favourite = 0 WHERE id = " + chosenPokemon.getID());
            System.out.println("Removed from favourites");
        }

        else{
            System.out.println("Not in favourites");
        }
    }

    @FXML
    private void handleFavouritesViewButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        favourites_view.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-background-radius: 30; -fx-border-color: #4682B4; -fx-border-width: 2; -fx-border-radius: 30;");
        home_view.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 30; -fx-border-color: #4682B4; -fx-border-width: 2; -fx-border-radius: 30;");


        favouritesOnly = true;
        currentSearchTerm = null;
        loadPage(0, favouritesOnly, true, currentSearchTerm);
    }

    @FXML
    private void handleHomeViewButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        home_view.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-background-radius: 30; -fx-border-color: #4682B4; -fx-border-width: 2; -fx-border-radius: 30;");
        favourites_view.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 30; -fx-border-color: #4682B4; -fx-border-width: 2; -fx-border-radius: 30;");


        favouritesOnly = false;
        currentSearchTerm = null;
        loadPage(0, favouritesOnly, true, currentSearchTerm);
    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        currentSearchTerm = searchbox.getText();
        loadPage(0, favouritesOnly, true, currentSearchTerm);
    }
}