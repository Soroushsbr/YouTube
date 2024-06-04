package espresso.youtube.Front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    VBox leftSideBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homeVbox.fxml"));
            AnchorPane homePane = loader.load();
            leftSideBox.getChildren().clear();
            leftSideBox.getChildren().add(homePane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    AnchorPane notifPane;
    public void selectNotif(){
        if(notifPane.isVisible()){
            notifPane.setVisible(false);
        }else {
            notifPane.setVisible(true);
        }
    }
}
