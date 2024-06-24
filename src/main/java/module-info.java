module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens espresso.youtube to javafx.fxml;
    opens espresso.youtube.Front to javafx.fxml;
    opens espresso.youtube.models.video;
    opens espresso.youtube.models.account;
    
    exports espresso.youtube;
    exports espresso.youtube.Server;
    exports espresso.youtube.models;
    exports espresso.youtube.models.account;
    exports espresso.youtube.Front;
    exports espresso.youtube.Client;
}
