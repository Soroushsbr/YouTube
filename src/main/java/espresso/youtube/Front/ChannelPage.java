package espresso.youtube.Front;

import espresso.youtube.models.channel.Channel;
import espresso.youtube.models.channel.Client_channel;
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
import javafx.scene.control.TextField;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static espresso.youtube.Front.LoginMenu.client;
import static espresso.youtube.Front.LoginMenu.darkmode;

public class ChannelPage implements Initializable {
    private ArrayList<String> searches;
    @FXML
    AnchorPane parent;
    @FXML
    AnchorPane searchPane;
    @FXML
    AnchorPane profilePane;
    @FXML
    VBox searchBox;
    @FXML
    TextField searchField;
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
    @FXML
    Circle channelProfile;
    @FXML
    Circle profile;
    @FXML
    Circle profile2;
    @FXML
    Text urName;
    @FXML
    Text urUsername;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setProfile();
        appendTheme();
    }
    public void changeTheme(){
        darkmode = !darkmode;
        appendTheme();
        profilePane.setVisible(false);
    }
    public void appendTheme(){
        //todo: database changes
        if(darkmode){
            parent.getStylesheets().clear();
            parent.getStylesheets().add(this.getClass().getResource("Style/Dark/Channel.css").toExternalForm());
        }else{
            parent.getStylesheets().clear();
            parent.getStylesheets().add(this.getClass().getResource("Style/Light/Channel.css").toExternalForm());
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
    public void selectProf(){
//        notifPane.setVisible(false);
        if(profilePane.isVisible()){
            profilePane.setVisible(false);
        }else {
            profilePane.setVisible(true);
        }
    }
    public void setProfile(){
        urName.setText(client.getChannel().getName());
        urUsername.setText(client.getChannel().getUsername());
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
                profile2.setFill(pattern);
                thread.interrupt();
            }
        });
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
        Client_channel cc2 = new Client_channel(client.getOut());
        client.setReq_id();
        int req2 = client.getReq_id();
        cc2.get_info(channel.getId(), req2);
        while (true){
            if (client.requests.get(req2) != null){
                channel.setName((String) client.requests.get(req2).get_part("title"));
                channel.setUsername((String) client.requests.get(req2).get_part("username"));
                channel.setDescription((String) client.requests.get(req2).get_part("description"));
                channel.setCreated_at(client.requests.get(req2).getCreated_at());
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        nameTxt.setText(channel.getName());
        infoTxt.setText("@" + channel.getUsername() + " • ");
        if(channel.getDescription() == (null)){
            channel.setDescription("");
        }
        bioTxt.setText(channel.getDescription() + "\nJoined " + Formatter.formatTime(channel.getCreated_at()));
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

        Task<File> taskProf = new Task<File>() {
            @Override
            protected File call() throws Exception {
                client.setReq_id();
                int req = client.getReq_id();
                return Client_video.get_media("profile", channel.getId(), "jpg", "picture", (int) client.requests.get(0).get_part("client_handler_id"), req );
            }
        };
        Thread threadProf = new Thread(taskProf);
        threadProf.start();
        taskProf.setOnSucceeded(event -> {
            if(taskProf.getValue() != null) {
                ImagePattern pattern = new ImagePattern(new Image(taskProf.getValue().toURI().toString()));
                channelProfile.setFill(pattern);
                threadProf.interrupt();
            }
        });
    }
    public void setChannel(Channel channel) throws IOException {
        this.channel = channel;
        initializeChannel();
        Client_video cv = new Client_video(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        ArrayList<Video> videos;
        cv.get_all_posts_of_a_channel(channel.getId(), client.getChannel_id(), req);

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
                    ((Label)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(2)).setText(Formatter.formatSeconds(videos.get(finalI).getLength()));
                    ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(0)).setText(videos.get(finalI).getTitle());
                    ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(1)).setText(videos.get(finalI).getChannel().getName());
                    ((Line)((AnchorPane) videoPane.getChildren().get(2)).getChildren().get(3)).setVisible(videos.get(finalI).getWatched());
                    ((Label)((VBox) videoPane.getChildren().get(0)).getChildren().get(2)).setText(videos.get(finalI).getViews() + " views • " + Formatter.formatTime(videos.get(finalI).getCreated_at()));
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
                        taskProf.setOnSucceeded(event -> {
                            if(taskProf.getValue() != null) {
                                ImagePattern pattern = new ImagePattern(new Image(taskProf.getValue().toURI().toString()));
                                ((Circle) videoPane.getChildren().get(1)).setFill(pattern);
                                threadProf.interrupt();
                            }
                        });
                    });

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
    public void switchToYourChannel(ActionEvent event){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Channel.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            ChannelPage channelPage = loader.getController();
            channelPage.setChannel(client.getChannel());
            //set client for next stage

            stage.show();
        }catch (IOException ignored){
        }
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
            MainPage mainPage = loader.getController();
            mainPage.appendVideos();
            //set client for next stage

            stage.show();
        }catch (IOException ignored){
        }
    }
    public void showLiked(ActionEvent event){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            MainPage mainPage = loader.getController();
            mainPage.showLiked();
            //set client for next stage

            stage.show();
        }catch (IOException ignored){
        }
    }
    public void setting(ActionEvent event){
        client.setUser_id("");
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Setting.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        }catch (IOException ignored){
        }
    }
    public void switchtoPlaylists(ActionEvent event){
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            MainPage mainPage = loader.getController();
            mainPage.showPlaylists();
            //set client for next stage

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
    public void selectSearch(){
        searchPane.setVisible(true);
        Client_video cv = new Client_video(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        cv.get_search_titles(req);
        ArrayList<String> titles ;
        while (true){
            if(client.requests.get(req) != null){
                titles = ((ArrayList<String>)(client.requests.get(req).get_part("titles")));
                break;
            }
        }
        Collections.sort(titles, Comparator.comparingInt(String::length));
        this.searches = titles;
        appendSearch(searches);
    }
    public void hideSearch(){
        searchPane.setVisible(false);
    }
    public void appendSearch(ArrayList<String> titles){
        searchBox.getChildren().clear();
        for(String title : titles){
            Button button = new Button(title);
            button.setOnAction(event -> {
                searchField.setText(button.getText());
            });
            button.setPrefWidth(420);
            button.setPrefHeight(50);
            button.setStyle("-fx-font-size: 15px;");
            searchBox.getChildren().add(button);
        }
    }
    public void showResult(ActionEvent event) {
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            MainPage mainPage = loader.getController();
            mainPage.showResult(searchField.getText());
            //set client for next stage

            stage.show();
        }catch (IOException ignored){
        }
    }
    public void searching(){
        String search = searchField.getText();
        ArrayList<String> result = searchWords(searches, search);
        appendSearch(result);
    }
    public static ArrayList<String> searchWords(ArrayList<String> words, String searchString) {
        ArrayList<String> matchingWords = new ArrayList<>();
        Pattern pattern = Pattern.compile("^" + searchString + "\\w*\\b", Pattern.CASE_INSENSITIVE);

        for (String word : words) {
            Matcher matcher = pattern.matcher(word);
            if (matcher.find()) {
                matchingWords.add(word);
            }
        }

        return matchingWords;
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
