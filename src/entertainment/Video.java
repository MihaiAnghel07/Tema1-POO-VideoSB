package entertainment;

public final class Video {
    private String title;
    private double rating;

    public Video(final String title, final double rating) {
        this.title = title;
        this.rating = rating;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }
}
