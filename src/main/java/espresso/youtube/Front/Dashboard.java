package espresso.youtube.Front;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.UUID;

public class Dashboard {
    private UUID channelID;
    private UUID userID;
    @FXML
    Rectangle backgroundFade;
    @FXML
    AnchorPane uploadVidPane;
    public void showUploadPane(){
        backgroundFade.setVisible(true);
        uploadVidPane.setVisible(true);
    }

    public void hideUploadPane(){
        backgroundFade.setVisible(false);
        uploadVidPane.setVisible(false);
    }

    public void selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select MP4 File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 Files", "*.mp4")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // handle the file
        }
    }
}
