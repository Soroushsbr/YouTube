package espresso.youtube.Front;

import espresso.youtube.Client.Client;
import espresso.youtube.models.video.Client_video;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static espresso.youtube.Front.LoginMenu.client;

public class Dashboard {
    private UUID channelID;
    @FXML
    Rectangle backgroundFade;
    @FXML
    AnchorPane createPane;
    @FXML
    AnchorPane selectVidPane;
    @FXML
    ScrollPane detailsPane;
    @FXML
    Circle circle;
    @FXML
    VBox videoPre;
    @FXML
    Label fileName;
    @FXML
    AnchorPane uploadingPane;
    @FXML
    ProgressBar progressBar;
    @FXML
    VBox upVid;
    @FXML
    Button doneBtn;
    @FXML
    Text uploadText;
    @FXML
    Button nextBtn;
    private File selectedFile;

    public void showUploadPane(){
        detailsPane.setVisible(false);
        uploadingPane.setVisible(false);
        uploadingPane.setLayoutX(112);
        uploadVidScene();
        backgroundFade.setVisible(true);
        createPane.setVisible(true);
    }

    public void hideUploadPane(){
        backgroundFade.setVisible(false);
        createPane.setVisible(false);
        timer.stop();
        stopTimer.stop();
        uploadText.setText("Uploading...");
        upVid.getChildren().clear();
        doneBtn.setDisable(true);
    }

    public void sendFile(File selectedFile) throws IOException, InterruptedException {
        //todo: put this method in a thread
        Client_video client_video = new Client_video(client.getOut());
        client_video.send_video_info("1","title","description","123",1);
        client_video.upload_media(selectedFile,"1","mp4","video",(int) client.requests.get(0).get_part("client_handler_id"));
        while (true) {
            Thread.sleep(100);
            if (client.requests.get(1) != null) {
                System.out.println(client.requests.get(1).get_part("status"));
                break;
            } else
                System.out.println("waiting for response");
        }
    }

    public void selectFile(){
        uploadVidScene();
        nextBtn.setVisible(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select MP4 File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 Files", "*.mp4")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            this.selectedFile =selectedFile;
            detailScene(selectedFile);
        }
    }

    public void selectThumbnail(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );
        File selectedThumbnail = fileChooser.showOpenDialog(new Stage());
        if(selectedThumbnail != null){
            //todo
        }
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

    Timer timer;
    Timer stopTimer;
    public void confirmUpload() throws IOException, InterruptedException {
        nextBtn.setVisible(false);
        sendFile(selectedFile);
        MediaView mediaView = (MediaView) videoPre.getChildren().get(0);
        mediaView.fitWidthProperty().bind(upVid.widthProperty());
        mediaView.fitHeightProperty().bind(upVid.heightProperty());
        upVid.getChildren().add(mediaView);
        uploadingScene();
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProgressBar();
            }
        });
        timer.start();
        stopTimer = new Timer(11000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                progressBar.setVisible(false);
                doneBtn.setDisable(false);
                uploadText.setText("Uploaded Successfully.");
            }
        });
        stopTimer.setRepeats(false);
        stopTimer.start();

    }
    public void updateProgressBar(){
        progressBar.setProgress(progressBar.getProgress() + 0.05);

    }
    //------------animation--------------------------------------------------------
    public void uploadingScene(){
        Timeline timelineFadeVid = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(detailsPane.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(detailsPane.opacityProperty(), 0 ))
        );
        Timeline transitionVid = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(detailsPane.layoutXProperty(), detailsPane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(detailsPane.layoutXProperty(), 12))
        );
        timelineFadeVid.play();
        transitionVid.play();
        timelineFadeVid.setOnFinished(event -> detailsPane.setVisible(false));
        //for detail pane
        uploadingPane.setVisible(true);
        Timeline fadeDetail = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(uploadingPane.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(uploadingPane.opacityProperty(), 1 ))
        );
        Timeline transitionDetail = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(uploadingPane.layoutXProperty(), uploadingPane.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(uploadingPane.layoutXProperty(), 62))
        );
        fadeDetail.play();
        transitionDetail.play();

        Timeline circleTransition = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(circle.layoutXProperty(), circle.getLayoutX())),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(circle.layoutXProperty(), 427))
        );
        circleTransition.play();
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
