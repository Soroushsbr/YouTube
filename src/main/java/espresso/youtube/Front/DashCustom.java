package espresso.youtube.Front;

import espresso.youtube.models.channel.Client_channel;
import espresso.youtube.models.video.Client_video;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static espresso.youtube.Front.LoginMenu.client;

public class DashCustom {
    @FXML
    VBox basicInfoBox;
    @FXML
    VBox brandingBox;
    @FXML
    TextField nameField;
    @FXML
    TextField handleField;
    @FXML
    TextArea bioArea;
    @FXML
    Circle profile;
    public void initialize(){
        nameField.setText(client.getChannel().getName());
        handleField.setText(client.getChannel().getUsername());
        if(client.getChannel().getDescription() == null){
            client.getChannel().setDescription("");
        }
        bioArea.setText(client.getChannel().getDescription());
        setProfile();
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
    public void showInfo(){
        brandingBox.setVisible(false);
        basicInfoBox.setVisible(true);
    }
    public void showBrand(){
        basicInfoBox.setVisible(false);
        brandingBox.setVisible(true);
    }

    public void changeProfile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        File selectedProf = fileChooser.showOpenDialog(new Stage());
        if(selectedProf != null){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.send_profile_photo_info(client.getChannel_id(), "jpg",req );
            cv.upload_media(selectedProf ,"profile", client.getChannel_id(), "jpg", "picture" , (int) client.requests.get(0).get_part("client_handler_id"));
            ImagePattern pattern = new ImagePattern(new Image(selectedProf.toURI().toString()));
            profile.setFill(pattern);
        }
    }

    public void changeBanner() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        File selectedProf = fileChooser.showOpenDialog(new Stage());
        if(selectedProf != null){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.send_profile_photo_info(client.getChannel_id(), "jpg",req );
            cv.upload_media(selectedProf ,"banner", client.getChannel_id(), "jpg", "picture" , (int) client.requests.get(0).get_part("client_handler_id"));
        }
    }
    public void changeWatermark() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        File selectedProf = fileChooser.showOpenDialog(new Stage());
        if(selectedProf != null){
            Client_video cv = new Client_video(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            cv.send_profile_photo_info(client.getChannel_id(), "jpg",req );
            cv.upload_media(selectedProf ,"watermark", client.getChannel_id(), "jpg", "picture" , (int) client.requests.get(0).get_part("client_handler_id"));
        }
    }
    public void saveChanges(){
        if(!client.getChannel().getName().equals(nameField.getText()) && !nameField.getText().isEmpty()){
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            client.getChannel().setName(nameField.getText());
            cc.change_channel_name(client.getChannel_id(), nameField.getText(), req);
        }
        if(!client.getChannel().getUsername().equals(handleField.getText()) && !nameField.getText().isEmpty()){
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            client.getChannel().setUsername(handleField.getText());
            cc.change_channel_username(client.getChannel_id(), handleField.getText(), req);
        }
        if(!client.getChannel().getDescription().equals(bioArea.getText()) && !nameField.getText().isEmpty()){
            Client_channel cc = new Client_channel(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            client.getChannel().setDescription(bioArea.getText());
            cc.change_channel_description(client.getChannel_id(), bioArea.getText(), req);
        }
    }
    public void cancelChanges(){
        initialize();
    }
}
