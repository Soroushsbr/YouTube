package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class LoginMenu implements Initializable {
    @FXML
    AnchorPane backgroundPane;
    @FXML
    AnchorPane youtubeBackground;
    @FXML
    Pane logInPane;
    @FXML
    Pane signUpPane;
    @FXML
    Hyperlink changeOption;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set the background video
        URL file = getClass().getResource("back2.mp4");
        Media media = new Media(file.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        //make the video loop
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Rewind to the beginning
            mediaPlayer.play(); // Restart playback
        });
        ((AnchorPane) backgroundPane.getChildren().get(0)).getChildren().add(mediaView);
        mediaPlayer.play();

        //animation for youtube logo
        Timeline timelineFade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(youtubeBackground.opacityProperty(), 1 ))
        );
        timelineFade.play();
        Timeline timelineMove = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutYProperty(), youtubeBackground.getLayoutY())),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(youtubeBackground.layoutYProperty(), 70 ))
        );
        timelineMove.play();
        Timeline timelineMove2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutYProperty(), 70)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(youtubeBackground.layoutYProperty(), 90 ))
        );
        timelineMove.setOnFinished(event -> timelineMove2.play());
    }
    /*
    * this method change the option between sign up and login.
    * it includes animation for each panel.
    * using Timeline for animation , this class gets first and last of action and the time to do it.
    * */
    public void changeOptionScene(){
        //to move the logo to the left
        if(changeOption.getText().equals("Sign Up")) {
            Timeline timelineMoveYT = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutXProperty(), youtubeBackground.getLayoutX())),
                    new KeyFrame(Duration.seconds(0.8), new KeyValue(youtubeBackground.layoutXProperty(), 40))
            );
            timelineMoveYT.play();
            //fade the login pane
            Timeline timelineMoveLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.layoutYProperty(), logInPane.getLayoutY())),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(logInPane.layoutYProperty(), 22))
            );
            timelineMoveLog.play();
            Timeline timelineFadeLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(logInPane.opacityProperty(), 0))
            );
            timelineFadeLog.play();
            //fade signup pane
            Timeline timelineMoveSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.layoutYProperty(), 25)),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(signUpPane.layoutYProperty(), 18))
            );
            timelineMoveSign.play();
            Timeline timelineFadeSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(signUpPane.opacityProperty(), 1))
            );
            timelineFadeSign.play();
            changeOption.setText("Log In");
        }else{
            Timeline timelineMoveYT = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutXProperty(), youtubeBackground.getLayoutX())),
                    new KeyFrame(Duration.seconds(0.8), new KeyValue(youtubeBackground.layoutXProperty(), 440))
            );
            timelineMoveYT.play();
            //fade the login pane
            Timeline timelineMoveLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.layoutYProperty(), logInPane.getLayoutY())),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(logInPane.layoutYProperty(), 18))
            );
            timelineMoveLog.play();
            Timeline timelineFadeLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(logInPane.opacityProperty(), 1))
            );
            timelineFadeLog.play();
            //fade signup pane
            Timeline timelineMoveSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.layoutYProperty(), 18)),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(signUpPane.layoutYProperty(), 25))
            );
            timelineMoveSign.play();
            Timeline timelineFadeSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(signUpPane.opacityProperty(), 0))
            );
            timelineFadeSign.play();
            changeOption.setText("Sign Up");
        }
    }
    //todo: make animation for when user input the wrong pass or name.
}
