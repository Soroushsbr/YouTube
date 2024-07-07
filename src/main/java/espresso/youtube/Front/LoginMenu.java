package espresso.youtube.Front;

import espresso.youtube.Client.Client;
import espresso.youtube.Client.Handle_Server_Response;
import espresso.youtube.models.account.Client_account;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
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
    public static Client client;
    public static boolean darkmode;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client();
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

    public void signUp(ActionEvent event) {
        String username = signupUsernameTF.getText();
        String gmail = singupgmailTF.getText();
        String password = singupPasswordTF.getText();

        if(username.isEmpty()){
            applyShakeEffect(signupUsernameTF);
        }
        if(gmail.isEmpty()){
            applyShakeEffect(singupgmailTF);
        }
        if(password.isEmpty()){
            applyShakeEffect(singupPasswordTF);
        }
        if(!username.isEmpty() && !gmail.isEmpty() && !password.isEmpty()) {
            darkmode = true;
            //here it validate the data from database without lag
            client.setReq_id();
            int req = client.getReq_id();
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    Client_account client_account = new Client_account(client.getOut());
                    client_account.sign_up(username, password, gmail, client.getReq_id());

                    while (true) {
                        //checks for if the response is available or not
                        if (client.requests.get(client.getReq_id()) != null) {
                            if ((boolean) client.requests.get(req).get_part("isSuccessful")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        Thread.sleep(50);
                    }
                }
            };

            task.setOnSucceeded(e -> {
                if (task.getValue()) {
                    client.setChannel_id((String) client.requests.get(req).get_part("ChannelID"));
                    client.setUser_id((String) client.requests.get(req).get_part("UserID"));
                    switchToMainPage(event, this.client);
                } else {
                    if (!(boolean) client.requests.get(client.getReq_id()).get_part("isValidGmail")) {
                        applyShakeEffect(singupgmailTF);
                    }
                    if (!(boolean) client.requests.get(client.getReq_id()).get_part("isValidUsername")) {
                        applyShakeEffect(signupUsernameTF);
                    }
                }
            });
            //use the task as a thread so it doesn't lag
            new Thread(task).start();
        }
    }




    public void logIn(ActionEvent event) {
        String username = loginUsernameTF.getText();
        String password = loginPasswordTF.getText();

        //checks if input is empty or not
        if(username.isEmpty()){
            applyShakeEffect(loginUsernameTF);
        }
        if(password.isEmpty()){
            applyShakeEffect(loginPasswordTF);
        }

        //checks for if the user can log in or not
        if(!username.isEmpty() && !password.isEmpty()) {
            darkmode = true;
            client.setReq_id();
            //todo: save req id  before doing another thread
            int req = client.getReq_id();
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    Client_account client_account = new Client_account(client.getOut());
                    client_account.login(username, password, client.getReq_id());

                    while (true) {
                        //checks for if the response is available or not
                        if (client.requests.get(req) != null) {
                            return (boolean) client.requests.get(req).get_part("isSuccessful");
                        }
                        Thread.sleep(50);
                    }
                }
            };
            //after the task done it goes for actions in stage
            task.setOnSucceeded(e -> {
                if (task.getValue()) {
                    client.setChannel_id((String) client.requests.get(req).get_part("ChannelID"));
                    client.setUser_id((String) client.requests.get(req).get_part("UserID"));
                    switchToMainPage(event, client);
                } else {
                    applyShakeEffect(loginUsernameTF);
                    applyShakeEffect(loginPasswordTF);
                }
            });

            new Thread(task).start();
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

            stage.show();
        }catch (IOException ignored){
        }
    }

    //shakes the Node object, I used this for text field so when the user input is wrong it shakes.
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
}