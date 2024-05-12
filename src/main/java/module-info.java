module doodledo.lab08_1b_210041160 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;


    opens doodledo.lab08_1b_210041160 to javafx.fxml;
    exports doodledo.lab08_1b_210041160;
}