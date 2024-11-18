package weather.weatherappmain;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherController {

    @FXML
    private Label name;

    @FXML
    private Label temperatureNow;

    @FXML
    private Label maxTemp;

    @FXML
    private Label minTemp;

    @FXML
    private TextField searchBox;

    @FXML
    private Button searchButton;

    private final String apiKey = "c1065d994625de6b07ac608566f18177";

    @FXML
    private void initialize() {
        // Add key listener to handle "Enter" key press in the TextField
        searchBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearchButtonClick();
            }
        });
        searchButton.setOnMousePressed(event -> searchButton.setStyle("-fx-background-color: #C5D3E8; -fx-background-radius: 30;"));
        searchButton.setOnMouseReleased(event -> searchButton.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 30;"));
    }



    @FXML
    private void handleSearchButtonClick() {
        String cityName = searchBox.getText().trim();
        if (cityName.isEmpty()) {
            name.setText("Please enter a valid city name.");
            return;
        }

        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric";

        try {
            JSONObject weatherData = fetchWeatherData(urlString);
            if (weatherData != null) {
                updateWeatherUI(weatherData);
            } else {
                name.setText("City not found!");
                temperatureNow.setText("");
                minTemp.setText("");
                maxTemp.setText("");
            }
        } catch (Exception e) {
            name.setText("Error fetching weather data!");
            e.printStackTrace();
        }
    }

    private JSONObject fetchWeatherData(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // HTTP OK
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return new JSONObject(response.toString());
        }
        return null;
    }

    private void updateWeatherUI(JSONObject weatherData) {
        String city = weatherData.getString("name");
        JSONObject main = weatherData.getJSONObject("main");
        JSONObject sys = weatherData.getJSONObject("sys");
        double temperature = main.getDouble("temp");
        double tempMin = main.getDouble("temp_min");
        double tempMax = main.getDouble("temp_max");
        String country = sys.getString("country");

        if(city.equals("Baku")){
            name.setText("City: " + city + ", " + country + " (Vətən!)");
        }else{
            name.setText("City: " + city + ", " + country);
        }

        temperatureNow.setText("Temperature: " + temperature + "°C");
        minTemp.setText("Min Temp: " + tempMin + "°C");
        maxTemp.setText("Max Temp: " + tempMax + "°C");

        fadeInLabels(name, temperatureNow, minTemp, maxTemp);
    }

    private void fadeInLabels(Label... labels) {
        for (Label label : labels) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(800), label);
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(1.0);
            fadeTransition.play();
        }
    }
}
