package espresso.youtube.Front;

import espresso.youtube.Client.Client;
import espresso.youtube.models.video.Client_video;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static espresso.youtube.Front.LoginMenu.client;

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
//            File file = Client_video.get_media("1","d14521bf-3e3f-4b37-9a6e-be9a1848507f" ,"mp4", "video", (int) client.requests.get(0).get_part("client_handler_id") , 1);
//            Media media = new Media(file.toURI().toString());
//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//            MediaView mediaView = new MediaView(mediaPlayer);
            for(int i = 0 ; i < 1; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Preview_Box.fxml"));
                AnchorPane videoPane = loader.load();
//                mediaView.fitWidthProperty().bind(((VBox)videoPane.getChildren().get(0)).widthProperty());
//                mediaView.fitHeightProperty().bind(((VBox)videoPane.getChildren().get(0)).heightProperty());
//                ((VBox)videoPane.getChildren().get(0)).getChildren().add(mediaView);
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