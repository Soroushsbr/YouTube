package espresso.youtube.Front;


import espresso.youtube.models.video.Client_video;
import espresso.youtube.models.video.Video;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
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
import static espresso.youtube.Front.LoginMenu.darkmode;

public class VideoPage implements Initializable {
    private MediaPlayer mediaPlayer;
    private Video video;
    @FXML
    AnchorPane parent;
    @FXML
    VBox leftVbox;
    @FXML
    AnchorPane guidePane;
    @FXML
    Rectangle backWindow;
    @FXML
    AnchorPane actionPane;
    @FXML
    ImageView loading;
    @FXML
    VBox videoBox;
    @FXML
    TextArea addCommentArea;
    @FXML
    VBox addCommentBox;
    @FXML
    Text descriptionTxt;
    @FXML
    Text titleTxt;
    @FXML
    Button commentBtn;
    @FXML
    AnchorPane profPane;
    @FXML
    AnchorPane notifPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTextarea(addCommentArea , addCommentBox);
        appendTheme();
    }
    public void selectNotif(){
        profPane.setVisible(false);
        if(notifPane.isVisible()){
            notifPane.setVisible(false);
        }else {
            notifPane.setVisible(true);
        }
    }

    public void selectProf(){
        notifPane.setVisible(false);
        if(profPane.isVisible()){
            profPane.setVisible(false);
        }else {
            profPane.setVisible(true);
        }
    }

    public void logout(ActionEvent event){
        client.setUser_id("");
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_Menu.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        }catch (IOException ignored){
        }
    }

    public void changeTheme(){
        darkmode = !darkmode;
        appendTheme();
        profPane.setVisible(false);
    }
    public void appendTheme(){
        //todo: database changes
        if(darkmode){
            parent.getStylesheets().clear();
            parent.getStylesheets().add(this.getClass().getResource("Style/Dark/Video.css").toExternalForm());
        }else{
            parent.getStylesheets().clear();
            parent.getStylesheets().add(this.getClass().getResource("Style/Light/Video.css").toExternalForm());
        }
    }
    public void setVID(Video video) throws IOException {
        this.video = video;
        //to set hover action to show video icons when mouse hovers it
        Client_video cv = new Client_video(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        Task<File> task = new Task<File>() {
            @Override
            protected File call() throws Exception {
                return Client_video.get_media(video.getVideo_id(), video.getOwner_id() ,"mp4", "video", (int) client.requests.get(0).get_part("client_handler_id"), req);
            }
        };
        task.setOnSucceeded(e ->{
            Media media = new Media(task.getValue().toURI().toString());
            loading.setVisible(false);
            appendVideo(media , actionPane);
        });
        new Thread(task).start();

        titleTxt.setText(video.getTitle());
        descriptionTxt.setText(video.getDescription());
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

    public void initializeTextarea(TextArea textArea , VBox vbox){
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Enable button if TextArea is not empty, disable otherwise
                commentBtn.setDisable(newValue.trim().isEmpty());
            }
        });
        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                adjustVBoxHeight(textArea , vbox);
            }
        });

    }

    private void adjustVBoxHeight(TextArea textArea , VBox vbox) {
        // Calculate preferred height based on text content
        double prefHeight = computeTextHeight(textArea.getText(), textArea.getFont(), textArea.getWidth());

        // Set VBox preferred height to accommodate TextArea
        vbox.setPrefHeight(prefHeight + 20);
    }

    private double computeTextHeight(String text, javafx.scene.text.Font font, double width) {
        // Helper method to compute text height based on font and content
        javafx.scene.text.Text helper = new javafx.scene.text.Text();
        helper.setFont(font);
        helper.setText(text);
        helper.setWrappingWidth(width);
        return helper.getLayoutBounds().getHeight();
    }
    public void openEmoji(){
    }
    public void addComment(){
        String content = addCommentArea.getText();
    }
    public void cancelComment(){
        addCommentArea.clear();
    }
    public void appendComments() throws IOException {
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
    public void showGuide(){
        backWindow.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(guidePane.layoutXProperty(), guidePane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(guidePane.layoutXProperty(), 0))
        );
        timeline.play();
    }

    public void hideGuide(){
        backWindow.setVisible(false);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(guidePane.layoutXProperty(), guidePane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(guidePane.layoutXProperty(), -200))
        );
        timeline.play();
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
