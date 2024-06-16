package espresso.youtube.Front;

import espresso.youtube.Client.Client;
import espresso.youtube.Client.Handle_Server_Response;
import espresso.youtube.models.account.Client_account;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginMenu implements Initializable {
    @FXML
    AnchorPane backgroundPane;
    @FXML
    AnchorPane youtubeBackground;
    @FXML
    Pane logInPane;
    @FXML
    Pane signUpPane;
    @FXML
    Hyperlink changeOption;
    @FXML
    TextField signupUsernameTF;
    @FXML
    TextField singupgmailTF;
    @FXML
    PasswordField singupPasswordTF;
    @FXML
    TextField loginUsernameTF;
    @FXML
    PasswordField loginPasswordTF;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private Client client;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.client = new Client();
            Handle_Server_Response handleServerResponse = new Handle_Server_Response(client.getClient(), client.requests);
            Thread listener = new Thread(handleServerResponse);
            listener.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //set the background video
        URL file = getClass().getResource("Images/back2.mp4");
        Media media = new Media(file.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        //make the video loop
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Rewind to the beginning
            mediaPlayer.play(); // Restart playback
        });
        ((AnchorPane) backgroundPane.getChildren().get(0)).getChildren().add(mediaView);
        mediaPlayer.play();

        //animation for youtube logo
        Timeline timelineFade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(youtubeBackground.opacityProperty(), 1 ))
        );
        timelineFade.play();
        Timeline timelineMove = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutYProperty(), youtubeBackground.getLayoutY())),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(youtubeBackground.layoutYProperty(), 60 ))
        );
        timelineMove.play();
        Timeline timelineMove2 = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutYProperty(), 60)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(youtubeBackground.layoutYProperty(), 80 ))
        );
        timelineMove.setOnFinished(event -> timelineMove2.play());
    }

    public void signUp(ActionEvent event) throws InterruptedException {
        String username = signupUsernameTF.getText();
        String gmail = singupgmailTF.getText();
        String password = singupPasswordTF.getText();

        if(username.equals("")){
            applyShakeEffect(signupUsernameTF);
        }
        if (gmail.equals("")){
            applyShakeEffect(singupgmailTF);
        }
        if(password.equals("")){
            applyShakeEffect(singupPasswordTF);
        }
        if(!(username.equals("") || gmail.equals("") || password.equals(""))) {
            Client_account client_account = new Client_account(client.getOut());
            client.setReq_id();
            client_account.sign_up(username, password, gmail, client.getReq_id());
            while (true) {
                Thread.sleep(50);
//                if (client.requests.get(1) != null) {
//                    System.out.println("hi");
//                }
                if (client.requests.get(client.getReq_id()) != null) {
                    if ((boolean) client.requests.get(client.getReq_id()).get_part("isSuccessful")) {
                        switchToMainPage(event, this.client);
                        System.out.println("hi");
                        break;
//                        return;
                    }
                    if (!(boolean) client.requests.get(client.getReq_id()).get_part("isValidGmail")) {
                        applyShakeEffect(singupgmailTF);
//                        return;
                    }
                    if (!(boolean) client.requests.get(client.getReq_id()).get_part("isValidUsername")) {
                        applyShakeEffect(signupUsernameTF);
//                        return;
                    }

                    break;
                }
                System.out.println("still running");
            }

        }
    }

    public void switchToMainPage(ActionEvent event, Client client){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            //set client for next stage
            MainPage mainPage = loader.load();
            mainPage.setClient(client);

            stage.show();
        }catch (IOException ignored){
        }
    }

    public void logIn(ActionEvent event) throws InterruptedException {
        String username = loginUsernameTF.getText();
        String password = loginPasswordTF.getText();

        if(username.equals("")){
            applyShakeEffect(loginUsernameTF);
        }
        if(password.equals("")){
            applyShakeEffect(loginPasswordTF);
        }
        if(!(username.equals("") || password.equals(""))) {
            Client_account client_account = new Client_account(client.getOut());
            client.setReq_id();
            client_account.login(username, password, client.getReq_id());
            while (true) {
                Thread.sleep(200);
                if (client.requests.get(client.getReq_id()) != null) {
                    if ((boolean) client.requests.get(client.getReq_id()).get_part("isSuccessful")) {
                        switchToMainPage(event, client);
                    } else {
                        applyShakeEffect(loginUsernameTF);
                        applyShakeEffect(loginPasswordTF);
                    }
                    return;
                }
            }
        }
    }

    private void applyShakeEffect(Node textField) {
        TranslateTransition shakeTransition = new TranslateTransition(Duration.millis(70), textField);
        shakeTransition.setFromX(0);
        shakeTransition.setToX(10);
        shakeTransition.setCycleCount(4);
        shakeTransition.setAutoReverse(true);
        shakeTransition.play();
    }


    /*
    * this method change the option between sign up and login.
    * it includes animation for each panel.
    * using Timeline for animation , this class gets first and last of action and the time to do it.
    * */
    public void changeOptionScene(){
        //to move the logo to the left
        if(changeOption.getText().equals("Sign Up")) {
            Timeline timelineMoveYT = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutXProperty(), youtubeBackground.getLayoutX())),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(youtubeBackground.layoutXProperty(), 40))
            );
            timelineMoveYT.play();
            //fade the login pane
            Timeline timelineMoveLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.layoutYProperty(), logInPane.getLayoutY())),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(logInPane.layoutYProperty(), 22))
            );
            timelineMoveLog.play();
            Timeline timelineFadeLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(logInPane.opacityProperty(), 0))
            );
            timelineFadeLog.play();
            //fade signup pane
            Timeline timelineMoveSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.layoutYProperty(), 25)),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(signUpPane.layoutYProperty(), 18))
            );
            timelineMoveSign.play();
            Timeline timelineFadeSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(signUpPane.opacityProperty(), 1))
            );
            timelineFadeSign.play();
            changeOption.setText("Log In");
        }else{
            Timeline timelineMoveYT = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(youtubeBackground.layoutXProperty(), youtubeBackground.getLayoutX())),
                    new KeyFrame(Duration.seconds(0.7), new KeyValue(youtubeBackground.layoutXProperty(), 440))
            );
            timelineMoveYT.play();
            //fade the login pane
            Timeline timelineMoveLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.layoutYProperty(), logInPane.getLayoutY())),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(logInPane.layoutYProperty(), 18))
            );
            timelineMoveLog.play();
            Timeline timelineFadeLog = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(logInPane.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(logInPane.opacityProperty(), 1))
            );
            timelineFadeLog.play();
            //fade signup pane
            Timeline timelineMoveSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.layoutYProperty(), 18)),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(signUpPane.layoutYProperty(), 25))
            );
            timelineMoveSign.play();
            Timeline timelineFadeSign = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(signUpPane.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(signUpPane.opacityProperty(), 0))
            );
            timelineFadeSign.play();
            changeOption.setText("Sign Up");
        }
    }
    //todo: make animation for when user input the wrong pass or name.
}