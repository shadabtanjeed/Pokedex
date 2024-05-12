package doodledo.lab08_1b_210041160;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemController {

    @FXML
    private Label pokeHeight;

    @FXML
    private Label pokeID;

    @FXML
    private ImageView pokeImg;

    @FXML
    private Label pokeName;

    @FXML
    private Label pokeWeight;

    @FXML
    private void onClick(MouseEvent event) {
        myListener.onClickListener(pokemons);
    }

    private Pokemon pokemons;
    private MyListener myListener;

    public void setData(Pokemon pokemon, MyListener myListener) {
        this.pokemons = pokemon;
        this.myListener = myListener;
//        pokeID.setText(pokemon.getID());
        pokeID.setText("#" + pokemon.getID());
        pokeName.setText(pokemon.getName());
        pokeHeight.setText(pokemon.getHeight());
        pokeWeight.setText(pokemon.getWeight());

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() {
                return new Image(pokemon.getImgSrc());
            }
        };

        task.setOnSucceeded(e -> pokeImg.setImage(task.getValue()));
        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }
}