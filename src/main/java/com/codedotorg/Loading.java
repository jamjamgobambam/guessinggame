package com.codedotorg;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class Loading {
    
    /** Displays text letting the user know the camera is loading */
    private Label cameraLoadingLabel;

    /** The progress indicator for while the camera is loading */
    private ProgressIndicator progress;

    public Loading() {
        progress = new ProgressIndicator();
        progress.setId("loading");
        cameraLoadingLabel = new Label("Camera loading...");
    }

    public Label getCameraLoadingLabel() {
        return cameraLoadingLabel;
    }

    public ProgressIndicator getProgressIndicator() {
        return progress;
    }

    /**
     * Hides the camera view and displays the loading animation.
     */
    public void showLoadingAnimation() {
        cameraView.setVisible(false);
        cameraLoadingLabel.setVisible(true);
        progress.setVisible(true);
    }

    /**
     * Hides the loading animation by removing the progress bar and camera loading label from the root layout
     * and making the camera view visible.
     */
    public void hideLoadingAnimation() {
        rootLayout.getChildren().remove(progress);
        rootLayout.getChildren().remove(cameraLoadingLabel);
        cameraView.setVisible(true);
    }
}
