package espresso.youtube.Front;

import espresso.youtube.models.account.Account;
import espresso.youtube.models.account.Client_account;
import espresso.youtube.models.account.Server_account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static espresso.youtube.Front.LoginMenu.client;
import static espresso.youtube.Front.LoginMenu.darkmode;

public class Setting {
    private Account account;
    @FXML
    TextField usernameTF;
    @FXML
    AnchorPane parent;
    @FXML
    TextField gmailTF;
    @FXML
    TextField passwordTF;
    @FXML
    Text idTxt;

    public void initializeSetting(){
        appendTheme();
        account = new Account();
        Client_account ca = new Client_account(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        ca.get_info(client.getUser_id() , req);
        while (true){
            if(client.requests.get(req) !=null){
                account.setUsername((String) client.requests.get(req).get_part("username"));
                account.setGmail((String) client.requests.get(req).get_part("gmail"));
                break;
            }
        }
        usernameTF.setText(account.getUsername());
        gmailTF.setText(account.getGmail());
        idTxt.setText("Your YouTube ID: " + client.getUser_id());
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

    public void saveChange() throws InterruptedException {
        if(!usernameTF.getText().isEmpty() && !usernameTF.getText().equals(account.getUsername())){
            Client_account ca = new Client_account(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            ca.change_username(usernameTF.getText(), client.getUser_id(), req);
            while (true){
                if(client.requests.get(req) != null){
                    if((boolean)client.requests.get(req).get_part("isSuccessful")){
                        account.setUsername(usernameTF.getText());
                    }else {
                        usernameTF.setText(account.getUsername());
                    }
                    break;
                }
                Thread.sleep(50);
            }
        }
        if(!gmailTF.getText().isEmpty() && !gmailTF.getText().equals(account.getGmail())){
            Client_account ca = new Client_account(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            ca.change_email(gmailTF.getText(), client.getUser_id(), req);
            while (true){
                if(client.requests.get(req) != null){
                    if((boolean)client.requests.get(req).get_part("isSuccessful")){
                        account.setGmail(gmailTF.getText());
                    }else {
                        gmailTF.setText(account.getGmail());
                    }
                    break;
                }
                Thread.sleep(50);
            }
        }
        if (!passwordTF.getText().isEmpty()){
            Client_account ca = new Client_account(client.getOut());
            client.setReq_id();
            int req = client.getReq_id();
            ca.change_password(passwordTF.getText(), client.getUser_id(), req);
        }
    }
    public void cancelChanges(){
        usernameTF.setText(account.getUsername());
        gmailTF.setText(account.getGmail());
    }
    public void logout(ActionEvent event){
        client.setUser_id("");
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
    public void main(ActionEvent event){
        client.setUser_id("");
        Parent root;
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        }catch (IOException ignored){
        }
    }
    public void deleteAcc(ActionEvent event){
        Client_account ca = new Client_account(client.getOut());
        client.setReq_id();
        int req = client.getReq_id();
        ca.account_delete(client.getUser_id(), req);
        logout(event);
    }
}
