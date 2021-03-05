package entertainment;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import java.util.ArrayList;
import java.util.Comparator;

public final class Recommends {
    private ActionInputData command;

    public void setCommand(final ActionInputData command) {
        this.command = command;
    }

    public String getCommandType() {
        return command.getType();
    }
    /**
     * se parcurge lista de filme/seriale si cand se gasestie primul video
     * care nu a fost gasit in istoricul utilizatorului din comanda primita,
     * se afiseaza
     */
    public String solveStandardRecommendation(final ArrayList<UserInputData> users,
                                              final ArrayList<MovieInputData> movies,
                                              final ArrayList<SerialInputData> shows) {

        String result = "StandardRecommendation result: ";
        for (UserInputData user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                boolean found = false;
                for (int j = 0; j < movies.size() && !found; j++) {
                    if (!user.getHistory().containsKey(movies.get(j).getTitle())) {
                        result += movies.get(j).getTitle();
                        found = true;
                    }
                }

                if (!found) {
                    for (int j = 0; j < shows.size() && !found; j++) {
                        if (!user.getHistory().containsKey(shows.get(j).getTitle())) {
                            result += shows.get(j).getTitle();
                            found = true;
                        }
                    }
                }
            }
        }
        return result;
    }
    /**
     * se calculeaza media rating-urilor filmelor/serialelor, se face sortarea,
     * si se intoarce primul video
     */
    public String solveBestUnseenRecommendation(final ArrayList<MovieInputData> movies,
                                                final ArrayList<SerialInputData> shows) {

        String result = "BestRatedUnseenRecommendation result: ";
        movies.sort(Comparator.comparing(MovieInputData::getAverageRating).reversed());
        shows.sort(Comparator.comparing(SerialInputData::getAverageRating).reversed());

        double maxRating;
        if (movies.get(0).getAverageRating() > shows.get(0).getAverageRating()) {
            maxRating = movies.get(0).getAverageRating();
        } else {
            maxRating = shows.get(0).getAverageRating();
        }

        boolean found = false;
        for (int i = 0; i < movies.size() && !found; i++) {
            if (movies.get(i).getAverageRating() == maxRating) {
                result += movies.get(i).getTitle();
                found = true;
            }
        }

        if (!found) {
            for (int i = 0; i < shows.size() && !found; i++) {
                if (shows.get(i).getAverageRating() == maxRating) {
                    result += shows.get(i).getTitle();
                    found = true;
                }
            }
        }

        return result;
    }
    /**
     * se parcurge lista de filme/seriale , daca video-ul se potriveste cu criteriile din
     * comanda, se cauta in istoricul utilizatorului, iar daca nu exista , se adauga
     * in lista(lista creata cu ajutorul clasei Video, pentru a putea salva si filme
     * si seriale).Se face sortarea si apoi afisarea.
     */
    public String solveSearchRecommendation(final ArrayList<UserInputData> users,
                                            final ArrayList<MovieInputData> movies,
                                            final ArrayList<SerialInputData> shows) {

        String result = "SearchRecommendation result: [";
        ArrayList<Video> videos = new ArrayList<>();
        Query query = new Query();
        query.findAverageRating(movies, shows);

        for (UserInputData user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                for (MovieInputData movie : movies) {
                    if (movie.getGenres().contains(command.getGenre())) {
                        if (!user.getHistory().containsKey(movie.getTitle())) {
                            Video v = new Video(movie.getTitle(), movie.
                                    getAverageRating());
                            videos.add(v);
                        }
                    }
                }

                for (SerialInputData show : shows) {
                    if (show.getGenres().contains(command.getGenre())) {
                        if (!user.getHistory().containsKey(show.getTitle())) {
                            Video v = new Video(show.getTitle(), show.
                                    getAverageRating());
                            videos.add(v);
                        }
                    }
                }
            }
        }

        if (videos.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        } else {
            videos.sort(Comparator.comparing(Video::getRating).thenComparing(Video::getTitle));
            for (Video video : videos) {
                result += video.getTitle();
                result += ", ";
            }
        }

        result = result.substring(0, result.length() - 2);
        result += "]";

        return result;
    }
}
