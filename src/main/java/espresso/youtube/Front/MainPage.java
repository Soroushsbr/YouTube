package espresso.youtube.Front;

import espresso.youtube.Client.Client;
import espresso.youtube.models.video.Client_video;
import espresso.youtube.models.video.Video;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.UUID;

import static espresso.youtube.Front.LoginMenu.client;
import static espresso.youtube.Front.LoginMenu.darkmode;

public class MainPage implements Initializable {
    @FXML
    AnchorPane parent;
    @FXML
    VBox leftSideBox;
    @FXML
    AnchorPane notifPane;
    @FXML
    VBox videosBox;
    @FXML
    Circle profile;
    @FXML
    AnchorPane guidePane;
    @FXML
    Rectangle backWindow;
    @FXML
    AnchorPane profPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //todo: replace the test image with the actual image
            ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("Images/test.jpg").openStream()));
            profile.setFill(pattern);
            appendTheme();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        appendVideos();
    }


    //to show the notifications of user

    //this method gets videos from server and show them to user
    public void appendVideos(){
        try {
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();

            //todo: put this in a thread

            System.out.println("Waiting...");
            cv.get_videos(client.getReq_id());
            ArrayList<Video> videos ;

            while (true) {
                if (client.requests.get(client.getReq_id()) != null) {
                    videos = client.requests.get(client.getReq_id()).getVideos_list();
                    break;
                }
                Thread.sleep(50);
            }
            System.out.println("Done.");

            int i = 0;
            videosBox.getChildren().clear();
            while (i <videos.size()) {
                HBox previewBox = new HBox();
                previewBox.setSpacing(10);
                previewBox.getChildren().clear();
                for (int j = 0; j < 3; j++) {
                    if (i < videos.size()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Preview_Box.fxml"));
                        AnchorPane videoPane = loader.load();
//
//                        File file = Client_video.get_media(id, "mp4", "video", (int) client.requests.get(0).get_part("client_handler_id"), 100);
//                        Media media = new Media(file.toURI().toString());
//                        MediaPlayer mediaPlayer = new MediaPlayer(media);
//                        MediaView mediaView = new MediaView(mediaPlayer);
//                        mediaView.fitWidthProperty().bind(((VBox)((AnchorPane) videoPane.getChildren().get(4)).getChildren().get(0)).widthProperty());
//                        mediaView.fitHeightProperty().bind(((VBox)((AnchorPane) videoPane.getChildren().get(4)).getChildren().get(0)).heightProperty());
//                        ((VBox)((AnchorPane) videoPane.getChildren().get(4)).getChildren().get(0)).getChildren().add(mediaView);
                        int finalI = i;
                        ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(0)).setText(videos.get(finalI).getTitle());
                        ((Button)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(3)).setOnAction(event -> switchToVideoPage(event,videos.get(finalI)));
                        ((AnchorPane) videoPane.getChildren().get(2)).setOnMouseEntered(mouseEvent -> hoverPreview(((AnchorPane) videoPane.getChildren().get(2))));
                        ((AnchorPane) videoPane.getChildren().get(2)).setOnMouseExited(mouseEvent -> unhoverPreview(((AnchorPane) videoPane.getChildren().get(2))));
                        previewBox.getChildren().add(videoPane);
                        i++;
                    } else {
                        break;
                    }
                }

                videosBox.getChildren().add(previewBox);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            parent.getStylesheets().add(this.getClass().getResource("Style/Dark/Main.css").toExternalForm());
        }else{
            parent.getStylesheets().clear();
            parent.getStylesheets().add(this.getClass().getResource("Style/Light/Main.css").toExternalForm());
        }
    }

    public void hoverPreview(AnchorPane pane){
        Timeline tx = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pane.scaleXProperty(), pane.getScaleX())),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(pane.scaleXProperty(), 1.04))
        );
        tx.play();
        Timeline ty = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pane.scaleYProperty(), pane.getScaleY())),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(pane.scaleYProperty(), 1.04))
        );
        ty.play();
    }

    public void unhoverPreview(AnchorPane pane){
        Timeline tx = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pane.scaleXProperty(), pane.getScaleX())),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(pane.scaleXProperty(), 1))
        );
        tx.play();
        Timeline ty = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pane.scaleYProperty(), pane.getScaleY())),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(pane.scaleYProperty(), 1))
        );
        ty.play();
    }


    public void switchToVideoPage(ActionEvent event ,Video video){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Page.fxml"));
            root = loader.load();
            VideoPage videoPage = loader.getController();
            videoPage.setVID(video);
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

    public void switchToDashboard(ActionEvent event){
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

}