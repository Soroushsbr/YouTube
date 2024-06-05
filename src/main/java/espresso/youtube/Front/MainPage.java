package espresso.youtube.Front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPage implements Initializable {

    @FXML
    VBox leftSideBox;
    @FXML
    AnchorPane notifPane;
    @FXML
    VBox videosBox;
    @FXML
    Circle profile;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homeVbox.fxml"));
            AnchorPane homePane = loader.load();
            leftSideBox.getChildren().clear();
            leftSideBox.getChildren().add(homePane);
            //here it sets the user profile picture
            //I put a random picture to see if it's working
            //todo: replace the test image with the actual image
            ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("test.jpg").openStream()));
            profile.setFill(pattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        test();
    }

    //to show the notifications of user
    public void selectNotif(){
        if(notifPane.isVisible()){
            notifPane.setVisible(false);
        }else {
            notifPane.setVisible(true);
        }
    }

    /*
    * this is for putting videos in the vbox, and I'm still working on it.
    public void test(){
        try {
            HBox hBox = new HBox();
            hBox.getChildren().clear();
            for(int i = 0 ; i < 3; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Box.fxml"));
                AnchorPane videoPane = loader.load();
                hBox.getChildren().add(videoPane);
            }
            videosBox.getChildren().clear();
            videosBox.getChildren().add(hBox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
}
