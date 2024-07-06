package espresso.youtube.Front;

import espresso.youtube.models.video.Client_video;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import static espresso.youtube.Front.LoginMenu.client;
import static espresso.youtube.Front.LoginMenu.darkmode;

public class Dashboard implements Initializable {
    private File selectedVideo;
    private File selectedThumbnail;
    private UUID channelID;
    @FXML
    AnchorPane parent;
    @FXML
    VBox box;
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
    @FXML
    TextField titleTF;
    @FXML
    TextArea descriptionTA;
    @FXML
    AnchorPane goChannelSvg;
    @FXML
    AnchorPane profPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard_Customize.fxml"));
            AnchorPane pane = loader.load();
            box.getChildren().add(pane);
            appendTheme();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void selectProf(){
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
            parent.getStylesheets().add(this.getClass().getResource("Style/Dark/Dashboard.css").toExternalForm());
        }else{
            parent.getStylesheets().clear();
            parent.getStylesheets().add(this.getClass().getResource("Style/Light/Dashboard.css").toExternalForm());
        }
    }

    public void showUploadPane(){
        profPane.setVisible(false);
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
        uploadText.setText("Uploading...");
        upVid.getChildren().clear();
        doneBtn.setDisable(true);
    }

    public void sendFile(File selectedFile) throws IOException, InterruptedException {
        //todo: put this method in a thread
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String title = titleTF.getText();
                String description = descriptionTA.getText();
                if(!title.isEmpty() && !description.isEmpty()){
                    client.setReq_id();
                    Client_video client_video = new Client_video(client.getOut());
                    client_video.send_video_info(client.getUser_id(),title,description,"123", client.getReq_id());
                    client_video.upload_media(selectedFile,client.getUser_id(),"mp4","video",(int) client.requests.get(0).get_part("client_handler_id"));

                    while (true) {
                        Thread.sleep(100);
                        if (client.requests.get(client.getReq_id()) != null) {
                            System.out.println(client.requests.get(client.getReq_id()).get_part("status"));
                            break;
                        } else
                            System.out.println("waiting for response");
                    }
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        // to Interrupt the thread after the task done
        //todo -> soroush : do this for every thread you have made
        try{
            thread.join();
        }catch (InterruptedException e){
            System.out.println("the video uploaded");
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
        File selectedVideo = fileChooser.showOpenDialog(new Stage());
        if (selectedVideo != null) {
            this.selectedVideo = selectedVideo;
            detailScene(selectedVideo);
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
            this.selectedThumbnail = selectedThumbnail;
        }else{
//            Media media = new Media(selectedVideo.toURI().toString());
//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//            MediaView mediaView = new MediaView(mediaPlayer);
//            mediaPlayer.setOnReady(() -> {
//                // Pause the video at the specified time
//                mediaPlayer.seek(Duration.seconds(1));
//
//                // Capture snapshot of the MediaView
//                Image snapshot = mediaView.snapshot(null, null);
//
//                // Save snapshot to file (you can change format and path as needed)
//                File file = new File("C:\\Users\\Lenovo\\Downloads\\test.png");
//                try {
//                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
//                    System.out.println("Snapshot saved to: " + file.getAbsolutePath());
//                } catch (IOException e) {
//                    System.out.println("Failed to save snapshot: " + e.getMessage());
//                }
//            });
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
    public void confirmUpload() throws IOException, InterruptedException {
        if(!titleTF.getText().isEmpty() && !descriptionTA.getText().isEmpty()) {
            upVid.getChildren().clear();
            sendFile(selectedVideo);
            nextBtn.setVisible(false);
            MediaView mediaView = (MediaView) videoPre.getChildren().get(0);
            mediaView.fitWidthProperty().bind(upVid.widthProperty());
            mediaView.fitHeightProperty().bind(upVid.heightProperty());

            upVid.getChildren().add(mediaView);
            uploadingScene();
            Timeline timelinePbar = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                    new KeyFrame(Duration.seconds(5), new KeyValue(progressBar.progressProperty(), 1 ))
            );
            timelinePbar.play();
            timelinePbar.setOnFinished(event -> upDone());
        }
    }

    public void upDone(){
        uploadText.setText("Uploaded Successfully.");
        doneBtn.setDisable(false);
        progressBar.setVisible(false);
    }
    public void switchToMainPage(ActionEvent event){
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

    //------------animation--------------------------------------------------------
    public void hoverProfile(){
        Timeline tGochannel = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(goChannelSvg.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(goChannelSvg.opacityProperty(), 1 ))
        );
        tGochannel.play();
    }
    public void unhoverProfile(){
        Timeline tGochannel = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(goChannelSvg.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(goChannelSvg.opacityProperty(), 0 ))
        );
        tGochannel.play();
    }
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
