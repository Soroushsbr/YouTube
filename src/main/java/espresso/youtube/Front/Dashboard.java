package espresso.youtube.Front;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.UUID;

public class Dashboard {
    private UUID channelID;
    private UUID userID;
    @FXML
    Rectangle backgroundFade;
    @FXML
    AnchorPane uploadVidPane;
    @FXML
    AnchorPane selectVidPane;
    @FXML
    AnchorPane detailsPane;
    @FXML
    Circle circle;
    @FXML
    VBox videoPre;
    @FXML
    Label fileName;
    public void showUploadPane(){
        backgroundFade.setVisible(true);
        uploadVidPane.setVisible(true);
    }

    public void hideUploadPane(){
        backgroundFade.setVisible(false);
        uploadVidPane.setVisible(false);
    }

    public void selectFile(){
        uploadVidScene();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select MP4 File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 Files", "*.mp4")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            detailScene(selectedFile);
        }
    }

    public void detailScene(File selectedFile){
        Timeline timelineFadeVid = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(selectVidPane.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(selectVidPane.opacityProperty(), 0 ))
        );
        Timeline transitionVid = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(selectVidPane.layoutXProperty(), selectVidPane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(selectVidPane.layoutXProperty(), 12))
        );
        timelineFadeVid.play();
        transitionVid.play();
        timelineFadeVid.setOnFinished(event -> selectVidPane.setVisible(false));
        //for detail pane
        detailsPane.setVisible(true);
        Timeline fadeDetail = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(detailsPane.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(detailsPane.opacityProperty(), 1 ))
        );
        Timeline transitionDetail = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(detailsPane.layoutXProperty(), detailsPane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(detailsPane.layoutXProperty(), 62))
        );
        fadeDetail.play();
        transitionDetail.play();

        Timeline circleTransition = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(circle.layoutXProperty(), circle.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(circle.layoutXProperty(), 389))
        );
        circleTransition.play();
        circleTransition.setOnFinished(event -> setPreview(selectedFile));
    }

    public void setPreview(File selectedFile){
        videoPre.getChildren().clear();
        fileName.setText("File Name: " + selectedFile.getName());
        Media media = new Media(selectedFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        //make the video to be middle of vbox
        mediaView.fitWidthProperty().bind(videoPre.widthProperty());
        mediaView.fitHeightProperty().bind(videoPre.heightProperty());
        videoPre.getChildren().add(mediaView);
    }

    public void uploadVidScene(){
        selectVidPane.setVisible(true);
        Timeline timelineFadeVid = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(selectVidPane.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(selectVidPane.opacityProperty(), 1 ))
        );
        Timeline transitionVid = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(selectVidPane.layoutXProperty(), selectVidPane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(selectVidPane.layoutXProperty(), 62))
        );
        timelineFadeVid.play();
        transitionVid.play();
        //for detail pane
        Timeline fadeDetail = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(detailsPane.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(detailsPane.opacityProperty(), 0 ))
        );
        Timeline transitionDetail = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(detailsPane.layoutXProperty(), detailsPane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(detailsPane.layoutXProperty(), 112))
        );
        fadeDetail.play();
        transitionDetail.play();
        fadeDetail.setOnFinished(event -> detailsPane.setVisible(false));

        Timeline circleTransition = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(circle.layoutXProperty(), circle.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(circle.layoutXProperty(), 351))
        );
        circleTransition.play();
    }
}
