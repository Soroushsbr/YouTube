module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;

    opens espresso.youtube to javafx.fxml;
    opens espresso.youtube.Front to javafx.fxml;
    
    exports espresso.youtube;
    exports espresso.youtube.Server;
    exports espresso.youtube.models;
    exports espresso.youtube.models.account;
    exports espresso.youtube.Front;
    exports espresso.youtube.Client;
}
