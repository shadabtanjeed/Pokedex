package doodledo.lab08_1b_210041160;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Pokedex");
        stage.setScene(scene);
        stage.setResizable(false);

        Image applicationIcon = new Image(getClass().getResourceAsStream("/doodledo/lab08_1b_210041160/Pokédex_logo.png"));
        stage.getIcons().add(applicationIcon);


        stage.show();
    }



    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ResultSet rs = DbConnection.executeQuery("SELECT id, identifier FROM pokemon LIMIT 10");
//        while (rs.next()) {
//            System.out.println("ID: " + rs.getInt("id") + ", Identifier: " + rs.getString("identifier"));
//        }
        launch();
    }

}
