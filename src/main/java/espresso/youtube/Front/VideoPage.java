package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoPage implements Initializable {
    @FXML
    VBox leftVbox;
    @FXML
    VBox rightBox;
    @FXML
    ScrollPane dashboardPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Box.fxml"));
            AnchorPane videoPane = loader.load();
            //to set hover action to show video icons when mouse hovers it
            videoPane.getChildren().get(2).setOnMouseEntered(event -> hoverVideo((AnchorPane)videoPane.getChildren().get(2)));
            videoPane.getChildren().get(2).setOnMouseExited(event -> unhoverVideo((AnchorPane)videoPane.getChildren().get(2)));
            leftVbox.getChildren().add(videoPane);
//        appendVideo(mediaPlayer , videoPane);
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
    public void appendVideo(MediaPlayer mediaPlayer, AnchorPane videoPane){
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.play();
        //make the video to be middle of vbox
        mediaView.fitWidthProperty().bind(((VBox)videoPane.getChildren().get(0)).widthProperty());
        mediaView.fitHeightProperty().bind(((VBox)videoPane.getChildren().get(0)).heightProperty());

        ((Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(0)).setOnAction(event -> pause(mediaPlayer , (Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(0)));
        ((Slider)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(8)).setOnMouseDragged(event -> setVolume(mediaPlayer , (Slider)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(8)));
        ((Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(2)).setOnAction(event -> mute(mediaPlayer , (Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(2)));
        ((Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(4)).setOnAction(event -> showSpeed((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)));
        ((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(0).setOnMouseDragged(event -> setSpeed(mediaPlayer , (Slider)((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(0), (Label) ((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(1)));
        ((Button)((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(2)).setOnAction(event -> refSpeed((Slider)((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(0)));

        //slider setting
        setProgress(mediaPlayer , ((Slider)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(7)));
        //--------------
        ((VBox) videoPane.getChildren().get(0)).getChildren().add(mediaView);
    }

    public void pause(MediaPlayer mediaPlayer , Button btn){
        if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
            btn.getStyleClass().removeAll("pause");
            btn.getStyleClass().add("play");
        }else {
            mediaPlayer.play();
            btn.getStyleClass().removeAll("play");
            btn.getStyleClass().add("pause");
        }
    }

    public void setVolume(MediaPlayer mediaPlayer , Slider volume ){
        volume.valueProperty().addListener((obs, oldVal, newVal) -> {
            mediaPlayer.setVolume(newVal.doubleValue());
        });
    }

    public void mute(MediaPlayer mediaPlayer , Button btn ){
        if(mediaPlayer.isMute()){
            mediaPlayer.setMute(false);
            btn.getStyleClass().removeAll("unmute");
            btn.getStyleClass().add("mute");
        }else {
            mediaPlayer.setMute(true);
            btn.getStyleClass().removeAll("mute");
            btn.getStyleClass().add("unmute");
        }
    }

    public void setSpeed(MediaPlayer mediaPlayer , Slider speed , Label speedLabel){
        speed.valueProperty().addListener((obs, oldVal, newVal) -> {
            BigDecimal x = BigDecimal.valueOf(newVal.doubleValue());
            mediaPlayer.setRate(newVal.doubleValue());
            speedLabel.setText(x.setScale(2, RoundingMode.HALF_DOWN) + "x");
        });
    }

    public void refSpeed(Slider speed){
        speed.setValue(1.0);
    }

    public void showSpeed(AnchorPane speedPane){
        if(speedPane.isVisible()) {
            Timeline timelineFade = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(speedPane.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(speedPane.opacityProperty(), 0))
            );
            timelineFade.play();
            timelineFade.setOnFinished(event -> speedPane.setVisible(false));
        }else{
            speedPane.setVisible(true);
            Timeline timelineFade = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(speedPane.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(speedPane.opacityProperty(), 1))
            );
            timelineFade.play();
        }
    }
    public void setProgress(MediaPlayer mediaPlayer , Slider slider){
        //set the length of video as max of the slider
        if (mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN) {
            mediaPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                if (newStatus == MediaPlayer.Status.READY) {
                    initializeSlider(mediaPlayer , slider);
                }
            });
        } else {
            initializeSlider(mediaPlayer , slider);
        }

        slider.valueChangingProperty().addListener((observable, wasChanging, changing) -> {
            if (!changing) {
                // Seek video when slider drag is complete (mouse released)
                mediaPlayer.seek(Duration.seconds(slider.getValue()));
            }
        });
    }

    private void initializeSlider(MediaPlayer mediaPlayer , Slider slider) {
        slider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) ->
                slider.setValue(newTime.toSeconds())
        );
    }
}
