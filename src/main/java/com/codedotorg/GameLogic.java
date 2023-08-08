package com.codedotorg;

public class GameLogic {
    
    /** The left boundary for the binary search (lowest number in the range) */
    private static int left = 0;

    /** The right boundary for the binary search (highest number in the range) */
    private static int right = 100;

    /** The computer's guess, which is the middle number between the lowest and highest number */
    private static int guess = (left + right) / 2;

    /**
     * Performs a binary search based on the user's response.
     * If the user responds with "thumbsup", the guess is too low and the left boundary is updated.
     * If the user responds with "thumbsdown", the guess is too high and the right boundary is updated.
     * If the user responds with "stop", the current guess is returned.
     * If the user responds with anything else, -1 is returned.
     *
     * @param userResponse the user's response to the current guess
     * @return the next guess or -1 if the user's response is invalid
     */
    public static int binarySearch(String predictedClass) {
        // Get the predicted class without the leading number
        String userResponse = predictedClass.substring(predictedClass.indexOf(" ") + 1);

        if (userResponse.equals("thumbsup")) {
            left = guess;
            guess = (left + right) / 2;
            return guess;
        }
        else if (userResponse.equals("thumbsdown")) {
            right = guess;
            guess = (left + right) / 2;
            return guess;
        }
        else if (userResponse.equals("stop")) {
            return guess;
        }
        else {
            return -1;
        }
    }

    public static String getUserResponse(String predictedClass, double predictedScore) {
        // Get the predicted class without the leading number
        String userResponse = predictedClass.substring(predictedClass.indexOf(" ") + 1);

        // Create a String containing the result of the predicted class and confidence score
        String userResult = "User: " + userResponse + " (" + predictedScore + " Confidence)";

        // Return the String
        return userResult;
    }

}
