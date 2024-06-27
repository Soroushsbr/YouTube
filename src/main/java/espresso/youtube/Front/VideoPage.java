package espresso.youtube.Front;

import espresso.youtube.models.video.Client_video;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import static espresso.youtube.Front.LoginMenu.client;

public class VideoPage implements Initializable {
    @FXML
    VBox leftVbox;
    String videoID;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Box.fxml"));
////            AnchorPane videoPane = loader.load();
////            //to set hover action to show video icons when mouse hovers it
////            videoPane.getChildren().get(2).setOnMouseEntered(event -> hoverVideo((AnchorPane)videoPane.getChildren().get(2)));
////            videoPane.getChildren().get(2).setOnMouseExited(event -> unhoverVideo((AnchorPane)videoPane.getChildren().get(2)));
////            System.out.println(videoID);
////            File file = Client_video.get_media("1",videoID ,"mp4", "video", (int) client.requests.get(0).get_part("client_handler_id") , 1);
////            Media media = new Media(file.toURI().toString());
////            appendVideo(media , videoPane);
////            leftVbox.getChildren().add(videoPane);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
    public void setVID(String videoID) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Box.fxml"));
        AnchorPane videoPane = loader.load();
        //to set hover action to show video icons when mouse hovers it
        videoPane.getChildren().get(2).setOnMouseEntered(event -> hoverVideo((AnchorPane)videoPane.getChildren().get(2)));
        videoPane.getChildren().get(2).setOnMouseExited(event -> unhoverVideo((AnchorPane)videoPane.getChildren().get(2)));
        Client_video cv = new Client_video(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        Task<File> task = new Task<File>() {
            @Override
            protected File call() throws Exception {

                return Client_video.get_media(videoID, "mp4", "video", (int) client.requests.get(0).get_part("client_handler_id"), 1);
            }
        };
        task.setOnSucceeded(e ->{
            Media media = new Media(task.getValue().toURI().toString());
            ((ImageView) videoPane.getChildren().get(13)).setVisible(false);
            appendVideo(media , videoPane);
        });
        leftVbox.getChildren().add(videoPane);
        new Thread(task).start();

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
        //make the video to be middle of vbox
        mediaView.fitWidthProperty().bind(((VBox)videoPane.getChildren().get(0)).widthProperty());
        mediaView.fitHeightProperty().bind(((VBox)videoPane.getChildren().get(0)).heightProperty());
        //---give buttons actions---
        ((Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(0)).setOnAction(event -> pause(mediaPlayer , (Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(0)));
        ((Slider)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(8)).setOnMouseDragged(event -> setVolume(mediaPlayer , (Slider)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(8)));
        ((Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(2)).setOnAction(event -> mute(mediaPlayer , (Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(2)));
        ((Button)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(4)).setOnAction(event -> showSpeed((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)));
        ((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(0).setOnMouseDragged(event -> setSpeed(mediaPlayer , (Slider)((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(0), (Label) ((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(1)));
        ((Button)((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(2)).setOnAction(event -> refSpeed((Slider)((AnchorPane)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(9)).getChildren().get(0)));

        setProgress(mediaPlayer , ((Slider)((AnchorPane)videoPane.getChildren().get(2)).getChildren().get(7)));
        //--------------------------
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
        volume.valueProperty().addListener((obs, oldval, newVal) -> {
            double percentage = (newVal.doubleValue() - volume.getMin()) / (volume.getMax() - volume.getMin()) * 100;
            volume.lookup(".track").setStyle("-fx-background-color: linear-gradient(to right, red " + percentage + "%, rgba(211, 211, 211, 1) " + percentage + "%);");
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
        speed.valueProperty().addListener((obs, oldval, newVal) -> {
            double percentage = (newVal.doubleValue() - speed.getMin()) / (speed.getMax() - speed.getMin()) * 100;
            speed.lookup(".track").setStyle("-fx-background-color: linear-gradient(to right, red " + percentage + "%, rgba(211, 211, 211, 1) " + percentage + "%);");
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
        slider.valueProperty().addListener((obs, oldval, newVal) -> {
            double percentage = (newVal.doubleValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100;
            slider.lookup(".track").setStyle("-fx-background-color: linear-gradient(to right, red " + percentage + "%, rgba(211, 211, 211, 1) " + percentage + "%);");
        });
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

    public void addComment() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment_View.fxml"));
        HBox commentBox = loader.load();
        leftVbox.getChildren().add(commentBox);
    }
}
