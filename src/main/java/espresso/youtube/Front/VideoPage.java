package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoPage implements Initializable {
    @FXML
    VBox leftVbox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Box.fxml"));
        AnchorPane videoPane = loader.load();
        //to set hover action to show video icons when mouse hovers it
        videoPane.getChildren().get(2).setOnMouseEntered(event -> hoverVideo((AnchorPane)videoPane.getChildren().get(2)));
        videoPane.getChildren().get(2).setOnMouseExited(event -> unhoverVideo((AnchorPane)videoPane.getChildren().get(2)));
        leftVbox.getChildren().add(videoPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void hoverVideo(AnchorPane videoPane){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(videoPane.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(videoPane.opacityProperty(), 1 ))
        );
        timeline.play();
    }
    public void unhoverVideo(AnchorPane videoPane){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(videoPane.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(videoPane.opacityProperty(), 0 ))
        );
        timeline.play();
    }
    @FXML
    ScrollPane dashboardPane;
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
    //this method place the video and plays it(this needs the video from database)
    public void appendVideo(Media media, AnchorPane videoPane){
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.play();
        //make the video to be middle of vbox
        mediaView.fitWidthProperty().bind(((VBox)videoPane.getChildren().get(0)).widthProperty());
        mediaView.fitHeightProperty().bind(((VBox)videoPane.getChildren().get(0)).heightProperty());

        ((VBox) videoPane.getChildren().get(0)).getChildren().add(mediaView);
    }
}