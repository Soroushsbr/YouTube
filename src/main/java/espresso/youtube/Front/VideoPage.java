package espresso.youtube.Front;

import espresso.youtube.Client.Client;
import espresso.youtube.models.video.Client_video;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    String videoID;
    @FXML
    VBox leftVbox;
    @FXML
    AnchorPane actionPane;
    @FXML
    ImageView loading;
    @FXML
    VBox videoBox;
    @FXML
    TextArea addCommentArea;
    @FXML
    HBox addCommentBox;
    private MediaPlayer mediaPlayer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTextarea(addCommentArea , addCommentBox);
    }
    public void setVID(String videoID) throws IOException {
        //to set hover action to show video icons when mouse hovers it
        Client_video cv = new Client_video(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        Task<File> task = new Task<File>() {
            @Override
            protected File call() throws Exception {

                return Client_video.get_media(videoID, "mp4", "video", (int) client.requests.get(0).get_part("client_handler_id"), req);
            }
        };
        task.setOnSucceeded(e ->{
            Media media = new Media(task.getValue().toURI().toString());
            loading.setVisible(false);
            appendVideo(media , actionPane);
        });
        new Thread(task).start();

    }

    public void hoverVideo(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(actionPane.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(actionPane.opacityProperty(), 1 ))
        );
        timeline.play();
    }
    public void unhoverVideo(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(actionPane.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(actionPane.opacityProperty(), 0 ))
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
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        //make the video to be middle of vbox
        mediaView.fitWidthProperty().bind(videoBox.widthProperty());
        mediaView.fitHeightProperty().bind(videoBox.heightProperty());
        //---give buttons actions---
        ((Button)(actionPane).getChildren().get(0)).setOnAction(event -> pause(mediaPlayer , (Button)(actionPane).getChildren().get(0)));
        ((Slider)(actionPane).getChildren().get(8)).setOnMouseDragged(event -> setVolume(mediaPlayer , (Slider)(actionPane).getChildren().get(8)));
        ((Button)(actionPane).getChildren().get(2)).setOnAction(event -> mute(mediaPlayer , (Button)(actionPane).getChildren().get(2)));
        ((Button)(actionPane).getChildren().get(4)).setOnAction(event -> showSpeed((AnchorPane)(actionPane).getChildren().get(9)));
        ((AnchorPane)(actionPane).getChildren().get(9)).getChildren().get(0).setOnMouseDragged(event -> setSpeed(mediaPlayer , (Slider)((AnchorPane)(actionPane).getChildren().get(9)).getChildren().get(0), (Label) ((AnchorPane)(actionPane).getChildren().get(9)).getChildren().get(1)));
        ((Button)((AnchorPane)(actionPane).getChildren().get(9)).getChildren().get(2)).setOnAction(event -> refSpeed((Slider)((AnchorPane)(actionPane).getChildren().get(9)).getChildren().get(0)));

        setProgress(mediaPlayer , ((Slider)(actionPane).getChildren().get(7)));
        //--------------------------
        videoBox.getChildren().add(mediaView);
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

    public void initializeTextarea(TextArea textArea , HBox hbox){
        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            // Check if the user pressed the Enter key
            if (event.getCode() == KeyCode.ENTER) {
                // Update the HBox's height to match the text area's height
                hbox.setPrefHeight(hbox.getPrefHeight() + 21.0);
            }
        });

//        textArea.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                // Update the HBox's height to match the text area's height
//                hbox.setPrefHeight(50.0 + newValue.doubleValue());
//            }
//        });

    }

    public void addComment() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment_View.fxml"));
        VBox commentBox = loader.load();
//        ((Text)((VBox)((HBox)commentBox.getChildren().get(0)).getChildren().get(1)).getChildren().get(1)).setText("hello \n bye");
        leftVbox.getChildren().add(commentBox);
    }
    public void switchToDashboard(ActionEvent event){
        mediaPlayer.stop();
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
    public void switchToMainPage(ActionEvent event){
        mediaPlayer.stop();
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            //set client for next stage

            stage.show();
        }catch (IOException ignored){
        }
    }
}
