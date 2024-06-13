package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

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
    @FXML
    ScrollPane dashboardPane;

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
            ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("Images/test.jpg").openStream()));
            profile.setFill(pattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        appendVideos();
    }

    //to show the notifications of user
    public void selectNotif(){
        if(notifPane.isVisible()){
            notifPane.setVisible(false);
        }else {
            notifPane.setVisible(true);
        }
    }

    //this method gets videos from server and show them to user
    public void appendVideos(){
        try {
            HBox hBox = new HBox();
            hBox.getChildren().clear();
            for(int i = 0 ; i < 3; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Preview_Box.fxml"));
                AnchorPane videoPane = loader.load();
//                this can remove the red line blow video
//                (((AnchorPane) videoPane.getChildren().get(1)).getChildren().get(1)).setVisible(false);
                hBox.getChildren().add(videoPane);
            }
            videosBox.getChildren().clear();
            videosBox.getChildren().add(hBox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showDash(){
        if(dashboardPane.isVisible()){
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(dashboardPane.layoutXProperty(), dashboardPane.getLayoutX())),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(dashboardPane.layoutXProperty(), -200))
            );
            timeline.play();
            timeline.setOnFinished(event -> dashboardPane.setVisible(false));
        }else {
            dashboardPane.setVisible(true);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(dashboardPane.layoutXProperty(), dashboardPane.getLayoutX())),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(dashboardPane.layoutXProperty(), 0))
            );
            timeline.play();
        }
    }

}