package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Channel {
    @FXML
    AnchorPane profPane;
    public void changeProf(){

    }
    public void hoverProf(){
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(profPane.opacityProperty(), profPane.getOpacity())),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(profPane.opacityProperty(), 1 ))
        );
        t.play();
    }
    public void unhoverProf(){
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(profPane.opacityProperty(), profPane.getOpacity())),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(profPane.opacityProperty(), 0 ))
        );
        t.play();
    }
    public void switchToDashboard(ActionEvent event){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        }catch (IOException ignored){
        }
    }
}
