package com.codedotorg;

import com.codedotorg.modelmanager.CameraController;
import com.codedotorg.modelmanager.ModelManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuessingGame {

    /** The main window of the app */
    private Stage window;

    /** The main scene of the app to display the game */
    private MainScene game;

    /** Manages the TensorFlow model used for image classification */
    private ModelManager model;

    /** Controls the camera capture and provides frames to the TensorFlow model for classification */
    private CameraController cameraController;

    /**
     * Constructor for the GuessingGame class.
     * Initializes a new CameraController, ModelManager, and MainScene.
     */
    public GuessingGame() {
        cameraController = new CameraController();
        model = new ModelManager();
        game = new MainScene();
    }
    
    /**
     * Starts the Guessing Game application.
     * Sets the title of the primary stage to "Guessing Game".
     * Adds a shutdown hook to stop the camera capture when the app is closed.
     * Calls the showMainScreen() method to display the main screen.
     *
     * @param primaryStage the primary stage of the application
     */
    public void startApp(Stage primaryStage) {
        this.window = primaryStage;
        window.setTitle("Guessing Game");

        // Shutdown hook to stop the camera capture when the app is closed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cameraController.stopCapture();
        }));

        showMainScreen();
    }

    
    /**
     * Displays the main screen of the game, which includes the camera view
     * and the computer's guess. Calls createMainScreen() to create the main
     * screen to show, sets the scene to the window and shows it. Starts
     * capturing the webcam, initially hides the camera view and shows the
     * loading animation. Updates the prediction and computer's guess.
     */
    public void showMainScreen() {
        // Create a new Scene object
        Scene mainScene = game.createMainScene(cameraController);

        // Set mainScene as the active scene in the window
        window.setScene(mainScene);

        // Display the window
        window.show();

        // Capture the camera view and set the model for the cameraController object
        cameraController.captureCamera(game.getCameraView(), model);

        // Retrieve the Loading object
        Loading cameraLoading = game.getLoadingAnimation();

        // Show the loading animation while the camera is loading
        cameraLoading.showLoadingAnimation(game.getCameraView());

        // Update the game results
        updateGameResults();
    }

    /**
     * Updates the game results by getting the predicted class and score
     * from the CameraController, removing the loading animation and
     * showing the camera view, updating the label to prompt the user to
     * think of a number, getting the result of the user's response, updating
     * the prediction label with the predicted class and confidence score,
     * and updating the computer guess label.
     */
    private void updateGameResults() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            // Get the predicted class and score from the CameraController
            String predictedClass = cameraController.getPredictedClass();
            double predictedScore = cameraController.getPredictedScore();

            if (predictedClass != null) {
                // Retrieve the Loading object
                Loading cameraLoading = game.getLoadingAnimation();

                // Remove the loading animation and show the camera view
                cameraLoading.hideLoadingAnimation(game.getRootLayout(), game.getCameraView());

                // Update the label to prompt the user to think of a number
                game.setPromptLabelText("Higher, Lower, or Correct?");

                // Get the result of the user's response
                String userResult = GameLogic.getUserResponse(predictedClass, predictedScore);

                // Update the prediction label with the predicted class and confidence score
                Platform.runLater(() -> game.setPredictionLabelText(userResult));

                // Update the label with the computer's guess
                updateComputerGuessLabel(predictedClass);
            }
        }));
        
        // Specify that the animation should repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Start the animation
        timeline.play();
    }

    /**
     * Updates the computer guess label with the result
     * of a binary search on the predicted class.
     *
     * @param predictedClass the predicted class to search for
     */
    private void updateComputerGuessLabel(String predictedClass) {
        // Get the result of the computer's guess
        int result = GameLogic.binarySearch(predictedClass);

        // End the game if the guess is correct
        if (GameLogic.isGuessCorrect(predictedClass)) {
            window.setScene(game.correctGuessScene(result, window, cameraController));
        }
        else {
            // Create a String with the computer's guess
            String computerGuess = "Computer Guess: " + result;

            // Update the computer guess label with the number the computer guessed
            Platform.runLater(() -> game.setComputerGuessLabel(computerGuess));
        }
    }

}
