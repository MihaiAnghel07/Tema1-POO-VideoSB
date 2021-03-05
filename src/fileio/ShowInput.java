package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * General information about show (video), retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public abstract class ShowInput {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    private List<String> userWhoRated;

    private List<Double> ratings;

    private int nrFavorite;

    public ShowInput(final String title, final int year,
                     final ArrayList<String> cast, final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.ratings = new ArrayList<>();
        this.userWhoRated = new ArrayList<>();
    }
    /**
     * method that set number of occurrences in favorite field
     */
    public void setNrFavorite(final int nrFavorite) {
        this.nrFavorite = nrFavorite;
    }
    /**
     * method that get number of occurrences in favorite field
     */
    public int getNrFavorite() {
        return nrFavorite;
    }
    /**
     * method that add a rate in season's rating list
     */
    public void setRatings(final double ratings) {
        this.ratings.add(ratings);
    }
    /**
     * method that get rating list
     */
    public List<Double> getRatings() {
        return ratings;
    }
    /**
     * method that add a user in season's userWhoRated list
     */
    public void addUserWhoRate(final String userWhoRate) {
        this.userWhoRated.add(userWhoRate);
    }
    /**
     * method that get usersWhoRated list
     */
    public List<String> getUsersWhoRated() {
        return userWhoRated;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }
}
