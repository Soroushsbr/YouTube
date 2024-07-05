package espresso.youtube.Front;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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

    public void changeProfile(){
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
