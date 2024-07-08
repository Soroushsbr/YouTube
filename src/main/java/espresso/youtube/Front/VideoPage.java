package espresso.youtube.Front;


import espresso.youtube.models.channel.Client_channel;
import espresso.youtube.models.comment.Client_comment;
import espresso.youtube.models.comment.Comment;
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
import javafx.scene.layout.HBox;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

import static espresso.youtube.Front.LoginMenu.client;
import static espresso.youtube.Front.LoginMenu.darkmode;

public class VideoPage implements Initializable {
    private MediaPlayer mediaPlayer;
    private Video video;
    private boolean likeFlag , dislikeFlag;
    @FXML
    AnchorPane parent;
    @FXML
    Label totoalTime;
    @FXML
    Label currentTime;
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
    Label subsLabel;
    @FXML
    Button subBtn;
    @FXML
    Label channelLabel;
    @FXML
    Text likesCntText;
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
                return Client_video.get_media(video.getVideo_id(), video.getChannel().getId() ,"mp4", "video", (int) client.requests.get(0).get_part("client_handler_id"), req);
            }
        };
        task.setOnSucceeded(e ->{
            Media media = new Media(task.getValue().toURI().toString());
            loading.setVisible(false);
            appendVideo(media , actionPane);
        });
        new Thread(task).start();

        sendRequestInfo();
        appendComments();
        channelLabel.setText(video.getChannel().getName());
        totoalTime.setText( "/" + Formatter.formatSeconds(video.getLength()));
        titleTxt.setText(video.getTitle());
        descriptionTxt.setText(video.getDescription());
    }
    public void sendRequestInfo(){
        //-----checks if user subscribed or not------------
        Client_channel cc = new Client_channel(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        cc.check_if_user_subscribed(client.getChannel_id() , video.getChannel().getId() , req);
        while (true) {
            if (client.requests.get(req) != null) {
                if((boolean) client.requests.get(req).get_part("is_subscribed")){
                    subBtn.setText("Unsubscribe");
                }else {
                    subBtn.setText("Subscribe");
                }
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //----get number of subscriber of video channel-----------
        Client_channel cc2 = new Client_channel(client.getOut());
        client.setReq_id();
        int req2 = client.getReq_id();
        cc2.number_of_subscribers(video.getChannel().getId(), req2);
        while (true) {
            if (client.requests.get(req2) != null) {
                subsLabel.setText(Formatter.formatNumber((int) client.requests.get(req2).get_part("number_of_subscribers")) + " subscribers");
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //----add a view count for video------------------
        Client_video cv2 = new Client_video(client.getOut());
        client.setReq_id();
        int req4 = client.getReq_id();
        cv2.add_to_post_viewers(video.getVideo_id() , client.getChannel_id() , req4);
        //----get user like -----------------
        Client_video cv3 = new Client_video(client.getOut());
        client.setReq_id();
        int req5 = client.getReq_id();
        cv3.check_user_likes_post(video.getVideo_id(), client.getChannel_id(), req5);
        while (true) {
            if (client.requests.get(req5) != null) {
                likeFlag = (boolean) client.requests.get(req5).get_part("user_likes_post");
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //----get user dislike-----------
        Client_video cv4 = new Client_video(client.getOut());
        client.setReq_id();
        int req6 = client.getReq_id();
        cv4.check_user_dislikes_post(video.getVideo_id(), client.getChannel_id(), req6);
        while (true) {
            if (client.requests.get(req6) != null) {
                dislikeFlag = (boolean) client.requests.get(req6).get_part("user_dislikes_post");
                System.out.println(dislikeFlag);
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //----number of likes------------
        Client_video cv5 = new Client_video(client.getOut());
        client.setReq_id();
        int req7 = client.getReq_id();
        cv5.number_of_likes(video.getVideo_id(),req7);
        while (true) {
            if (client.requests.get(req7) != null) {
                likesCntText.setText(String.valueOf((int) client.requests.get(req7).get_part("number_of_likes")));
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void subscribe(){
        if(subBtn.getText().equals("Subscribe")){
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cc.subscribe(video.getChannel().getId(), client.getChannel_id(),req );
            subBtn.setText("Unsubscribe");
        }else {
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cc.unsubscribe(video.getChannel().getId(), client.getChannel_id(),req );
            subBtn.setText("Subscribe");
        }
    }
    public void like(){
        if(likeFlag){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.remove_user_like_from_post(video.getVideo_id() , client.getChannel_id() ,req);
            likeFlag =false;
            likesCntText.setText(String.valueOf(Integer.parseInt(likesCntText.getText()) - 1));
        }else if(dislikeFlag){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.remove_user_dislike_from_post(video.getVideo_id() , client.getChannel_id() ,req);
            Client_video cv2 = new Client_video(client.getOut());
            client.setReq_id();
            int req2 = client.getReq_id();
            cv2.like(video.getVideo_id(), client.getChannel_id(), req2);
            dislikeFlag = false;
            likeFlag = true;
            likesCntText.setText(String.valueOf(Integer.parseInt(likesCntText.getText()) + 1));
        }else {
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.like(video.getVideo_id(), client.getChannel_id(), req);
            likeFlag = true;
            likesCntText.setText(String.valueOf(Integer.parseInt(likesCntText.getText()) + 1));
        }
    }
    public void dislike(){
        if(dislikeFlag){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            dislikeFlag = false;
            cv.remove_user_dislike_from_post(video.getVideo_id() , client.getChannel_id() ,req);
        }else if(likeFlag){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.remove_user_like_from_post(video.getVideo_id() , client.getChannel_id() ,req);
            Client_video cv2 = new Client_video(client.getOut());
            client.setReq_id();
            int req2 = client.getReq_id();
            cv2.dislike(video.getVideo_id(), client.getChannel_id(), req2);
            likeFlag = false;
            dislikeFlag = true;
            likesCntText.setText(String.valueOf(Integer.parseInt(likesCntText.getText()) - 1));
        }else {
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.dislike(video.getVideo_id(), client.getChannel_id(), req);
            dislikeFlag = true;
        }
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
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) ->
                currentTime.setText(Formatter.formatSeconds((int) newTime.toSeconds()))
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
    public void addComment() throws IOException {
        String content = addCommentArea.getText();
        Client_comment cc = new Client_comment(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        cc.put_comment(content , client.getChannel_id(), video.getVideo_id() , req);
        addCommentArea.clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment_View.fxml"));
        VBox commentBox = loader.load();
        ((Hyperlink)((VBox)((HBox)commentBox.getChildren().get(0)).getChildren().get(1)).getChildren().get(0)).setText("@You");
        ((Text)((VBox)((HBox)commentBox.getChildren().get(0)).getChildren().get(1)).getChildren().get(1)).setText(content);
        leftVbox.getChildren().add(commentBox);
    }
    public void cancelComment(){
        addCommentArea.clear();
    }
    public void appendComments() throws IOException {
        ArrayList<Comment> comments ;
        Client_comment cc = new Client_comment(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        cc.load_comments(video.getVideo_id(), req);
        while (true) {
            if (client.requests.get(req) != null) {
                comments = client.requests.get(req).getComments_list();
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for(int i = 0 ; i < comments.size() ; i++){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Comment_View.fxml"));
            VBox commentBox = loader.load();
            ((Hyperlink)((VBox)((HBox)commentBox.getChildren().get(0)).getChildren().get(1)).getChildren().get(0)).setText("@" + comments.get(i).getUsername());
            ((Text)((VBox)((HBox)commentBox.getChildren().get(0)).getChildren().get(1)).getChildren().get(1)).setText(comments.get(i).getMessage());
            leftVbox.getChildren().add(commentBox);
        }
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
