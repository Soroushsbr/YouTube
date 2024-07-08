package espresso.youtube.Front;

import espresso.youtube.models.channel.Channel;
import espresso.youtube.models.channel.Client_channel;
import espresso.youtube.models.video.Client_video;
import espresso.youtube.models.video.Video;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static espresso.youtube.Front.LoginMenu.client;

public class ChannelPage implements Initializable {
    private Channel channel;
    @FXML
    AnchorPane profPane;
    @FXML
    AnchorPane guidePane;
    @FXML
    Rectangle backWindow;
    @FXML
    VBox contentBox;
    @FXML
    HBox btnBox;
    @FXML
    Text nameTxt;
    @FXML
    Text infoTxt;
    @FXML
    Text bioTxt;
    @FXML
    Button subBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initializeChannel(){
        nameTxt.setText(channel.getName());
        infoTxt.setText("@" + channel.getUsername() + " • ");
        bioTxt.setText(channel.getDescription());
        sendRequestInfo();

        if(channel.getId().equals(client.getChannel_id())){
            btnBox.getChildren().remove(0);
        }else {
            profPane.setVisible(false);
            btnBox.getChildren().remove(1);
        }
    }
    public void sendRequestInfo(){
        Client_channel cc = new Client_channel(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        cc.number_of_subscribers(channel.getId(), req);
        while (true) {
            if (client.requests.get(req) != null) {
                infoTxt.setText(infoTxt.getText() + Formatter.formatNumber((int) client.requests.get(req).get_part("number_of_subscribers")) + " subscribers • ");
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setChannel(Channel channel) throws IOException {
        this.channel = channel;
        initializeChannel();
        Client_video cv = new Client_video(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        ArrayList<Video> videos;
        cv.get_all_posts_of_a_channel(channel.getId(), req);

        System.out.println("Waiting to get videos...");
        while (true){
            if(client.requests.get(req) != null){
                videos = client.requests.get(req).getVideos_list();
                break;
            }
        }
        System.out.println("Done.");

        contentBox.getChildren().removeIf(node -> contentBox.getChildren().indexOf(node) != 0);
        int i = 0;
        while (i <videos.size()) {
            HBox previewBox = new HBox();
            previewBox.setSpacing(7);
            previewBox.getChildren().clear();
            for (int j = 0; j < 3; j++) {
                if (i < videos.size()) {
                    int finalI = i;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Preview_Box.fxml"));
                    AnchorPane videoPane = loader.load();
                    ((Label)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(1)).setText(Formatter.formatSeconds(videos.get(finalI).getLength()));
                    ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(0)).setText(videos.get(finalI).getTitle());
                    ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(1)).setText(videos.get(finalI).getChannel().getName());
                    ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(2)).setText(videos.get(finalI).getViews() + "views • " + Formatter.formatTime(videos.get(finalI).getCreated_at()));
                    ((Button)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(3)).setOnAction(event -> switchToVideoPage(event,videos.get(finalI)));

                    ((AnchorPane) videoPane.getChildren().get(2)).setOnMouseEntered(mouseEvent -> hoverPreview(((AnchorPane) videoPane.getChildren().get(2))));
                    ((AnchorPane) videoPane.getChildren().get(2)).setOnMouseExited(mouseEvent -> unhoverPreview(((AnchorPane) videoPane.getChildren().get(2))));
                    previewBox.getChildren().add(videoPane);
                    i++;
                } else {
                    break;
                }
            }

            contentBox.getChildren().add(previewBox);
        }
        infoTxt.setText(infoTxt.getText() + Formatter.formatNumber(videos.size()) + " videos");
    }
    public void changeProf(){

    }
    public void subscribe(){
        if(subBtn.getText().equals("Subscribe")){
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cc.subscribe(channel.getId(), client.getChannel_id(),req );
            subBtn.setText("Unsubscribe");
        }else {
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cc.unsubscribe(channel.getId(), client.getChannel_id(),req );
            subBtn.setText("Subscribe");
        }
    }
    public void hoverProf(){
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(profPane.opacityProperty(), profPane.getOpacity())),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(profPane.opacityProperty(), 1 ))
        );
        t.play();
    }
    public void unhoverProf(){
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(profPane.opacityProperty(), profPane.getOpacity())),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(profPane.opacityProperty(), 0 ))
        );
        t.play();
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

}
