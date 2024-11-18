module weather.weatherappmain {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens weather.weatherappmain to javafx.fxml;
    exports weather.weatherappmain;
}