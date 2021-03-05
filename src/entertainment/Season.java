package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;

    private List<String> userWhoRated;

    private double averageRating;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.userWhoRated = new ArrayList<>();
    }

    public void setAverageRating(final double averageRating) {
        this.averageRating = averageRating;
    }

    public double getAverageRating() {
        return averageRating;
    }
    /**
     * method that add a user in userWhoRated list
     */
    public void addUserWhoRated(final String userWhoRated) {
        this.userWhoRated.add(userWhoRated);
    }

    public List<String> getUsersWhoRated() {
        return userWhoRated;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }
    /**
     * method that add a rate in ratings list
     */
    public void setRatings(final double ratings) {
        this.ratings.add(ratings);
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}

