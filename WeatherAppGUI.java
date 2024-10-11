package cloudscope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import org.json.simple.JSONObject;

public class WeatherAppGUI extends JFrame {
    private JTextField locationField;
    private JLabel temperatureLabel, conditionLabel, feelsLikeLabel, weatherIconLabel;
    private JLabel humidityLabel, windspeedLabel, pressureLabel, visibilityLabel;
    private JPanel tempBox;

    public WeatherAppGUI() {
        // Set up the JFrame
        setTitle("WeatherApp Pro");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout());

        // Main panel with a dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(37, 42, 64));  // Dark background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top input section
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setOpaque(false);  // Transparent for dark background
        JLabel locationLabel = new JLabel("Search for location:");
        locationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        locationLabel.setForeground(Color.WHITE);

        locationField = new JTextField(20);
        locationField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        locationField.setBackground(new Color(56, 60, 85));
        locationField.setForeground(Color.WHITE);
        locationField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding inside text field
        locationField.setPreferredSize(new Dimension(300, 40));

        // "Get Weather" Button with hover effect
        JButton fetchWeatherButton = new JButton("Get Weather");
        fetchWeatherButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        fetchWeatherButton.setPreferredSize(new Dimension(150, 40));
        fetchWeatherButton.setBackground(new Color(100, 149, 237));  // Light blue button
        fetchWeatherButton.setForeground(Color.WHITE);
        fetchWeatherButton.setFocusPainted(false);
        fetchWeatherButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding inside button

        // Hover effect (change color on hover)
        fetchWeatherButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fetchWeatherButton.setBackground(new Color(65, 105, 225));  // Darker blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fetchWeatherButton.setBackground(new Color(100, 149, 237)); // Revert to original color
            }
        });

        inputPanel.add(locationLabel);
        inputPanel.add(locationField);
        inputPanel.add(fetchWeatherButton);

        // Weather data section with shadow boxes
        JPanel weatherPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // Split into 2 sections
        weatherPanel.setOpaque(false);

        // Left section for Temperature and Condition display in a box
        tempBox = createBoxPanel();
        tempBox.setLayout(new BorderLayout());

        // Placeholder icon
        weatherIconLabel = new JLabel();
        weatherIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weatherIconLabel.setIcon(new ImageIcon("icons/cloudy.png")); // Default weather icon
        tempBox.add(weatherIconLabel, BorderLayout.NORTH);

        // Initially hide weather data
        temperatureLabel = createLabel("", 60, Color.WHITE);
        conditionLabel = createLabel("", 24, Color.WHITE);
        feelsLikeLabel = createLabel("", 18, new Color(173, 216, 230));  // Light blue

        JPanel tempDetails = new JPanel(new GridLayout(2, 1)); // Column layout for condition and feels like
        tempDetails.setOpaque(false);
        tempDetails.add(conditionLabel);
        tempDetails.add(feelsLikeLabel);

        tempBox.add(temperatureLabel, BorderLayout.CENTER);
        tempBox.add(tempDetails, BorderLayout.SOUTH);

        // Right section for detailed weather info in a box
        JPanel detailsBox = createBoxPanel();
        detailsBox.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel humTitle = createLabel("Humidity:", 18, Color.WHITE);
        humidityLabel = createLabel("", 18, new Color(173, 216, 230));

        JLabel windTitle = createLabel("Windspeed:", 18, Color.WHITE);
        windspeedLabel = createLabel("", 18, new Color(173, 216, 230));

        JLabel pressTitle = createLabel("Pressure:", 18, Color.WHITE);
        pressureLabel = createLabel("", 18, new Color(173, 216, 230));

        JLabel visibTitle = createLabel("Visibility:", 18, Color.WHITE);
        visibilityLabel = createLabel("", 18, new Color(173, 216, 230));

        detailsBox.add(humTitle);
        detailsBox.add(humidityLabel);
        detailsBox.add(windTitle);
        detailsBox.add(windspeedLabel);
        detailsBox.add(pressTitle);
        detailsBox.add(pressureLabel);
        detailsBox.add(visibTitle);
        detailsBox.add(visibilityLabel);

        // Add the two box panels to the main weather panel
        weatherPanel.add(tempBox);
        weatherPanel.add(detailsBox);

        // Add components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(weatherPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Set button action
        fetchWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeatherData();
            }
        });

        // Allow enter key to trigger fetch action
        locationField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeatherButton.doClick();
            }
        });
    }

    // Helper method to create a JPanel with a box-like appearance and shadow effect
    private JPanel createBoxPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(56, 60, 85));  // Box background color
        panel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(5, 5, 15, 5, new Color(30, 30, 50)),  // Simulated shadow
                new EmptyBorder(20, 20, 20, 20)  // Padding inside the box
        ));
        panel.setOpaque(true);
        return panel;
    }

    // Helper method to create consistent labels
    private JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setForeground(color);
        return label;
    }

    // Fetch weather data and update the result area
    private void fetchWeatherData() {
        String location = locationField.getText();
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a location.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simulate a delay in fetching data with a timer
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch weather data using WeatherApp (simulation)
                JSONObject weatherData = WeatherApp.getWeatherData(location);

                if (weatherData != null) {
                    double temperature = (double) weatherData.get("temperature");
                    String condition = (String) weatherData.get("weather_condition");
                    long humidity = (long) weatherData.get("humidity");
                    double windspeed = (double) weatherData.get("windspeed");

                    // Update weather icon
                    updateWeatherIcon(condition);

                    // Update result labels with weather data
                    temperatureLabel.setText(temperature + "°C");
                    conditionLabel.setText(condition);
                    feelsLikeLabel.setText("Feels like " + (temperature + 5) + "°C");
                    humidityLabel.setText(humidity + "%");
                    windspeedLabel.setText(windspeed + " mph");
                    pressureLabel.setText("30.13 in"); // Example value
                    visibilityLabel.setText("10 mi"); // Example value
                } else {
                    JOptionPane.showMessageDialog(WeatherAppGUI.this, "Error fetching weather data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Update weather icon based on condition
    private void updateWeatherIcon(String condition) {
        String iconPath = "src/cloudscope/";

        switch (condition.toLowerCase()) {
            case "sunny":
                iconPath += "sunny.png";
                break;
            case "rainy":
                iconPath += "rainy.png";
                break;
            case "cloudy":
                iconPath += "cloudy.png";
                break;
            default:
                iconPath += "default.png";
                break;
        }

        weatherIconLabel.setIcon(new ImageIcon(iconPath));
        // Load the image
        ImageIcon icon = new ImageIcon(iconPath);
        // Resize the image
        Image resizedImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        // Set the resized image to the label
        weatherIconLabel.setIcon(new ImageIcon(resizedImage));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherAppGUI().setVisible(true);
            }
        });
    }
}
