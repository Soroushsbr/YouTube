package espresso.youtube.Front;

import espresso.youtube.models.channel.Channel;
import espresso.youtube.models.playlist.Client_playlist;
import espresso.youtube.models.playlist.Playlist;
import espresso.youtube.models.video.Client_video;
import espresso.youtube.models.video.Video;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

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
        appendTheme();
        setProfile();
//        appendVideos();
    }
    public void setProfile(){
        client.setReq_id();
        int req = client.getReq_id();
        Task<File> task = new Task<File>() {
            @Override
            protected File call() throws Exception {
                return Client_video.get_media("profile", client.getChannel_id(),"jpg", "picture", (int) client.requests.get(0).get_part("client_handler_id"), req);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        task.setOnSucceeded(e -> {
            if(task.getValue() != null) {
                ImagePattern pattern = new ImagePattern(new Image(task.getValue().toURI().toString()));
                profile.setFill(pattern);
                thread.interrupt();
            }
        });

    }


    //to show the notifications of user

    //this method gets videos from server and show them to user
    public void appendVideos(){
        try {
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();

            System.out.println("Waiting to get videos...");
            cv.get_videos(client.getReq_id(), client.getChannel_id());
            ArrayList<Video> videos ;

            while (true) {
                if (client.requests.get(client.getReq_id()) != null) {
                    videos = client.requests.get(client.getReq_id()).getVideos_list();
                    break;
                }
                Thread.sleep(50);
            }
            System.out.println("Done.");
            videosBox.getChildren().clear();
            int i = 0;

            while (i <videos.size()) {
                HBox previewBox = new HBox();
                previewBox.setSpacing(10);
                previewBox.getChildren().clear();
                for (int j = 0; j < 3; j++) {
                    if (i < videos.size()) {
                        int finalI = i;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Preview_Box.fxml"));
                        AnchorPane videoPane = loader.load();
                        ((Label)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(2)).setText(Formatter.formatSeconds(videos.get(finalI).getLength()));
                        ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(0)).setText(videos.get(finalI).getTitle());
                        ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(1)).setText(videos.get(finalI).getChannel().getName());
                        ((Line)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(3)).setVisible(videos.get(finalI).getWatched());
                        ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(2)).setText(videos.get(finalI).getViews() + " views • " + Formatter.formatTime(videos.get(finalI).getCreated_at()));
                        ((Button) videoPane.getChildren().get(3)).setOnAction(event -> switchTChannelPage(event , videos.get(finalI).getChannel()));
                        ((Button)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(4)).setOnAction(event -> switchToVideoPage(event,videos.get(finalI)));

                        ((AnchorPane) videoPane.getChildren().get(2)).setOnMouseEntered(mouseEvent -> hoverPreview(((AnchorPane) videoPane.getChildren().get(2))));
                        ((AnchorPane) videoPane.getChildren().get(2)).setOnMouseExited(mouseEvent -> unhoverPreview(((AnchorPane) videoPane.getChildren().get(2))));
                        Task<File> task = new Task<File>() {
                            @Override
                            protected File call() throws Exception {
                                client.setReq_id();
                                int req =client.getReq_id();
                                return Client_video.get_media(videos.get(finalI).getVideo_id(), videos.get(finalI).getChannel().getId(), "jpg", "picture", (int) client.requests.get(0).get_part("client_handler_id"), req );
                            }
                        };
                        Thread thread = new Thread(task);
                        thread.start();
                        task.setOnSucceeded(e -> {
                            ((ImageView)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(1)).setImage(new Image(task.getValue().toURI().toString()));
                            thread.interrupt();
                        });
                        thread.join();
                        Task<File> taskProf = new Task<File>() {
                            @Override
                            protected File call() throws Exception {
                                client.setReq_id();
                                int req = client.getReq_id();
                                return Client_video.get_media("profile", videos.get(finalI).getChannel().getId(), "jpg", "picture", (int) client.requests.get(0).get_part("client_handler_id"), req );
                            }
                        };
                        Thread threadProf = new Thread(taskProf);
                        threadProf.start();
                        taskProf.setOnSucceeded(e -> {
                            if(task.getValue() != null) {
                                ImagePattern pattern = new ImagePattern(new Image(taskProf.getValue().toURI().toString()));
                                ((Circle) videoPane.getChildren().get(1)).setFill(pattern);
                                threadProf.interrupt();
                            }
                        });

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

    public void showPlaylists() throws IOException {
        videosBox.getChildren().clear();
        ArrayList<Playlist> playlists ;
        Client_playlist cp = new Client_playlist(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        cp.get_playlists_of_account(client.getChannel_id() ,req);
        while (true){
            if(client.requests.get(req) != null){
                playlists = client.requests.get(req).getPlaylists_list();
                break;
            }
        }

        int i = 0;
        while (i < playlists.size()){
            HBox previewBox = new HBox();
            previewBox.setSpacing(10);
            previewBox.getChildren().clear();
            for(int j = 0 ; j < 3; j ++){
                if(i < playlists.size()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Playlist_Box.fxml"));
                    AnchorPane playlistPane = loader.load();
                    ((Label) playlistPane.getChildren().get(0)).setText(playlists.get(i).getTitle());
                    ((Text) ((AnchorPane) playlistPane.getChildren().get(2)).getChildren().get(8)).setText(String.valueOf(playlists.get(i).getVideos().size()) + " Videos");
                    int finalI = i;
                    if(playlists.get(i).getVideos().size() != 0){
                        ((Button) ((AnchorPane) playlistPane.getChildren().get(2)).getChildren().get(2)).setOnAction(event -> selectPlaylist(event , playlists.get(finalI)));
                    }
                    ((AnchorPane) playlistPane.getChildren().get(2)).setOnMouseEntered(mouseEvent -> hoverPreview(((AnchorPane) playlistPane.getChildren().get(2))));
                    ((AnchorPane) playlistPane.getChildren().get(2)).setOnMouseExited(mouseEvent -> unhoverPreview(((AnchorPane) playlistPane.getChildren().get(2))));
                    Task<File> taskNail = new Task<File>() {
                        @Override
                        protected File call() throws Exception {
                            client.setReq_id();
                            int req = client.getReq_id();
                            System.out.println(playlists.get(finalI).getVideos().get(0).getVideo_id());
                            return Client_video.get_media(playlists.get(finalI).getVideos().get(0).getVideo_id(), playlists.get(finalI).getVideos().get(0).getChannel().getId(), "jpg", "picture", (int) client.requests.get(0).get_part("client_handler_id"), req);
                        }
                    };
                    Thread threadNail = new Thread(taskNail);
                    threadNail.start();
                    try {
                        threadNail.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    taskNail.setOnSucceeded(e -> {

                        ((ImageView) ((AnchorPane) playlistPane.getChildren().get(2)).getChildren().get(1)).setImage(new Image(taskNail.getValue().toURI().toString()));
                        threadNail.interrupt();
                    });
                    Task<File> taskProf = new Task<File>() {
                        @Override
                        protected File call() throws Exception {
                            client.setReq_id();
                            int req = client.getReq_id();
                            return Client_video.get_media("profile", playlists.get(finalI).getUser_id(), "jpg", "picture", (int) client.requests.get(0).get_part("client_handler_id"), req);
                        }
                    };
                    Thread threadProf = new Thread(taskProf);
                    threadProf.start();
                    taskProf.setOnSucceeded(e -> {
                        if (taskProf.getValue() != null) {
                            ImagePattern pattern = new ImagePattern(new Image(taskProf.getValue().toURI().toString()));
                            ((Circle) playlistPane.getChildren().get(1)).setFill(pattern);
                            threadProf.interrupt();
                        }
                    });

                    previewBox.getChildren().add(playlistPane);
                    i++;
                }else {
                    break;
                }
            }
            videosBox.getChildren().add(previewBox);
        }
    }
    public void selectPlaylist(ActionEvent event, Playlist playlist){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Video_Page.fxml"));
            root = loader.load();
            VideoPage videoPage = loader.getController();
            videoPage.appendPlaylist(playlist , 0);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException ignored){
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
        client.setChannel_id("");
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

    public void switchTChannelPage(ActionEvent event , Channel channel){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Channel.fxml"));
            root = loader.load();
            ChannelPage channelPage = loader.getController();
            channelPage.setChannel(channel);
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