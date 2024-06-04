module espresso.youtube {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens espresso.youtube to javafx.fxml;
    exports espresso.youtube;
    opens espresso.youtube.Front to javafx.fxml;
    exports espresso.youtube.Front;
}