package espresso.youtube.Front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Shorts implements Initializable {
    @FXML
    VBox shortsBox;
    public void appendShorts(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Preview_Box.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            for(int i = 0 ; i < 5 ; i ++){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Shorts_Box.fxml"));
                AnchorPane shortPane = loader.load();
                shortsBox.getChildren().add(shortPane);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    ScrollPane scrollPane;


    public void down(){


        double currentVvalue = scrollPane.getVvalue();

        scrollPane.setVvalue(currentVvalue + 1.0 / (((double)((int) (shortsBox.getHeight()) / 650))));
        System.out.println(shortsBox.getHeight());

//        scrollPane.setVvalue(currentVvalue + 1.0);
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
