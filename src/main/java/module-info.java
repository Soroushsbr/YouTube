module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.fasterxml.jackson.databind;


    opens espresso.youtube to javafx.fxml;
    exports espresso.youtube;
    exports espresso.youtube.Server;
    opens espresso.youtube.Front;
    exports espresso.youtube.Front;
    exports espresso.youtube.Client;
}