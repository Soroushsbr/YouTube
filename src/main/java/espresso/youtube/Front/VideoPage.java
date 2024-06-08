package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
}
