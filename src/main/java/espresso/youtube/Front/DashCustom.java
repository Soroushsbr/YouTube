package espresso.youtube.Front;

import espresso.youtube.models.video.Client_video;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
                new FileChooser.ExtensionFilter("Image Files", "*.png")
        );
        File selectedProf = fileChooser.showOpenDialog(new Stage());
        if(selectedProf != null){
            Client_video cv = new Client_video(client.getOut());
            cv.upload_media(selectedProf , client.getUser_id(), "png", "picture" , (int) client.requests.get(0).get_part("client_handler_id"));
        }
    }

    public void changeBanner(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
        );
        File selectedThumbnail = fileChooser.showOpenDialog(new Stage());
        if(selectedThumbnail != null){
            //todo
        }
    }
    public void changeWatermark(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
        );
        File selectedThumbnail = fileChooser.showOpenDialog(new Stage());
        if(selectedThumbnail != null){
            //todo
        }
    }

    public void removeProfile(){
        //todo
    }

    public void changeName(){
        //todo
    }
    public void changeUsername(){
        //todo
    }
}