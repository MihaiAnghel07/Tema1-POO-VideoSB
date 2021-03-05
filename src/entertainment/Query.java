package entertainment;

import actor.ActorsAwards;
import fileio.ActionInputData;
import fileio.SerialInputData;
import fileio.ActorInputData;
import fileio.UserInputData;
import fileio.MovieInputData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;


public final class Query {
    private ActionInputData command;

    public void setCommand(final ActionInputData command) {
        this.command = command;
    }

    public String getCommandType() {
        return command.getType();
    }
    /**
     * aceasta metoda calculeaza media rating-urilor parcurgand listele de rating
     * corespunzatoare filmelor,sezoanelor si o salveaza intr-un camp creat.
     */
    public void findAverageRating(final ArrayList<MovieInputData> movies,
                                   final ArrayList<SerialInputData> shows) {

        for (MovieInputData movie : movies) {
            double rating = 0;
            if (movie.getRatings().size() != 0) {
                for (int j = 0; j < movie.getRatings().size(); j++) {
                    rating += movie.getRatings().get(j);
                }
                movie.setAverageRating(rating / movie.getRatings().size());
            } else {
                movie.setAverageRating(0);
            }
        }

        for (SerialInputData show : shows) {
            double ratingforshow = 0;
            for (int j = 0; j < show.getSeasons().size(); j++) {
                double ratingForSeason = 0;
                int cnt = 0;

                if (show.getSeasons().get(j).getRatings().size() != 0) {
                    int len = show.getSeasons().get(j).getRatings().size();
                    for (int k = 0; k < len; k++) {
                        ratingForSeason += show.getSeasons().get(j).getRatings().get(k);
                        cnt++;
                    }
                    show.getSeasons().get(j).setAverageRating(ratingForSeason / cnt);
                } else {
                    show.getSeasons().get(j).setAverageRating(0);
                }

                if (show.getSeasons().get(j).getAverageRating() != 0) {
                    ratingforshow += show.getSeasons().get(j).getAverageRating();
                }
            }
            if (ratingforshow != 0) {
                show.setAverageRating(ratingforshow / show.getSeasons().size());
            } else {
                show.setAverageRating(0);
            }
        }

    }
    /**
     * metoda calculeaza durata totala a showrilor
     */
    private void findDurationShow(final ArrayList<SerialInputData> shows) {

        for (SerialInputData show : shows) {
            int duration = 0;
            for (int j = 0; j < show.getSeasons().size(); j++) {
                duration += show.getSeasons().get(j).getDuration();
            }
            show.setDuration(duration);
        }
    }
    /**
     * calculeaza totalul vizualizarilor pentru filme si seriale
     */
    public void findNrViews(final ArrayList<UserInputData> users,
                            final ArrayList<MovieInputData> movies,
                            final ArrayList<SerialInputData> shows) {

        for (MovieInputData movie : movies) {
            for (UserInputData user : users) {
                if (user.getHistory().containsKey(movie.getTitle())) {
                    movie.setNrViews(user.getHistory().get(movie.getTitle()));
                }
            }
        }

        for (SerialInputData show : shows) {
            for (UserInputData user : users) {
                if (user.getHistory().containsKey(show.getTitle())) {
                    show.setNrViews(user.getHistory().get(show.getTitle()));
                }
            }
        }

    }
    /**
     * se afla media rating-urilor filmelor/serialelor, se parcurge lista de filme
     * a fiecarui actor si se asigneaza fiecarui actor media rating-urilor in care a jucat
     * se face sortarea si se afiseaza
     */
    public String solveAverageQuery(final ArrayList<ActorInputData> actors,
                                    final ArrayList<MovieInputData> movies,
                                    final ArrayList<SerialInputData> shows) {

        String result = "";
        findAverageRating(movies, shows);
        for (ActorInputData actor : actors) {
            double rating = 0;
            int cnt = 0;
            for (int j = 0; j < actor.getFilmography().size(); j++) {
                boolean found = false;
                for (int k = 0; k < movies.size() && !found; k++) {
                    if (actor.getFilmography().get(j)
                            .equals(movies.get(k).getTitle())) {
                        if (movies.get(k).getAverageRating() != 0) {
                            rating += movies.get(k).getAverageRating();
                            cnt++;
                        }
                        found = true;
                    }
                }

                if (!found) {
                    for (int k = 0; k < shows.size() && !found; k++) {
                        if (actor.getFilmography().get(j)
                                .equals(shows.get(k).getTitle())) {
                            if (shows.get(k).getAverageRating() != 0) {
                                rating += shows.get(k).getAverageRating();
                                cnt++;
                            }
                            found = true;
                        }
                    }
                }
            }

            if (cnt != 0) {
                actor.setAverageRating(rating / cnt);
            } else {
                actor.setAverageRating(0);
            }
        }

        if (command.getSortType().equals("asc")) {
            actors.sort(Comparator.comparing(ActorInputData::getAverageRating).
                    thenComparing(ActorInputData::getName));
        } else {
            actors.sort(Comparator.comparing(ActorInputData::getAverageRating).reversed().
                    thenComparing(ActorInputData::getName));
        }

        int len;
        if (command.getNumber() > actors.size()) {
            len = actors.size();
        } else {
            len = command.getNumber();
        }

        result += "Query result: [";
        int initialLength = result.length();

        int i = 0;
        while (actors.get(i).getAverageRating() == 0) {
            i++;
        }

        int j = 0;
        while (j < len && i < actors.size()) {
            if (actors.get(i).getAverageRating() != 0) {
                result += actors.get(i).getName() + ", ";
                j++;
            }
            i++;
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
    /**
     * se parcurge lista de award a fiecarui actor si in cazul in care are toate
     * awards -urile din comanda, se salveaza in lista apoi se face sortarea
     */
    public String solveAwardsQuery(final ArrayList<ActorInputData> actors) {

        String result = "Query result: [";
        int initialLength = result.length();
        ArrayList<ActorInputData> aux = new ArrayList<>();

        for (ActorInputData actor : actors) {
            boolean hasAll = true;
            int nrAwards = 0;
            int awardsPosition = command.getFilters().size() - 1;
            for (int k = 0; k < command.getFilters().get(awardsPosition).size() && hasAll; k++) {
                boolean found = false;
                for (Map.Entry<ActorsAwards, Integer> it : actor.getAwards().entrySet()) {
                    if (it.getKey().toString().equals(command.getFilters().get(awardsPosition)
                                                                                     .get(k))) {
                        found = true;
                        nrAwards += it.getValue();
                    }
                }
                if (!found) {
                    hasAll = false;
                    break;
                }
            }
            if (hasAll) {
                actor.setNrAwards(nrAwards);
                aux.add(actor);
            }
        }

        if (command.getSortType().equals("asc")) {
            aux.sort(Comparator.comparing(ActorInputData::getNrAwards));
        } else {
            aux.sort(Comparator.comparing(ActorInputData::getNrAwards).reversed());
        }

        for (ActorInputData actorInputData : aux) {
            result += actorInputData.getName();
            result += ", ";
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
    /**
     * se face sortarea actorilor in functie de nume, se cauta keywords-urile
     * din comanda si in caz afirmativ se formeaza lista cu numele actorilor
     */
    public String solveFilterDescriptionQuery(final ArrayList<ActorInputData> actors) {

        String result = "Query result: [";
        int initialLength = result.length();
        actors.sort(Comparator.comparing(ActorInputData::getName));

        for (ActorInputData actor : actors) {
            boolean contain = true;
            for (int j = 0; j < command.getFilters().get(2).size() && contain; j++) {
                if (!actor.getCareerDescription().toLowerCase().contains(
                        command.getFilters().get(2).get(j).toLowerCase())) {
                    contain = false;
                }
            }
            if (contain) {
                result += actor.getName();
                result += ", ";
            }
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
    /**
     * se afla ratingul mediu, se face sortarea filmelor/serialelor in functie de
     * aceasta medie, se verifica daca fiecare film se potriveste cu ceea ce se cere
     * in comanda si in caz afirmativ se afiseaza
     */
    public String solveRatingsQuery(final ArrayList<MovieInputData> movies,
                                    final ArrayList<SerialInputData> shows) {

        findAverageRating(movies, shows);
        String result = "Query result: [";
        int initialLength = result.length();

        if (command.getSortType().equals("asc")) {
            movies.sort(Comparator.comparing(MovieInputData::getAverageRating));
            shows.sort(Comparator.comparing(SerialInputData::getAverageRating));
        } else {
            movies.sort(Comparator.comparing(MovieInputData::getAverageRating).reversed());
            shows.sort(Comparator.comparing(SerialInputData::getAverageRating).reversed());
        }

        int len;
        if (command.getObjectType().equals("movies")) {
            if (command.getNumber() < movies.size()) {
                len = command.getNumber();
            } else {
                len = movies.size();
            }

            int i = 0, j = 0;
            while (i < movies.size() && j < len) {
                if (movies.get(i).getAverageRating() != 0) {
                    boolean match = false;
                    if (command.getFilters().get(0).contains(String.valueOf(movies.get(i)
                                        .getYear()))) {
                        for (int k = 0; k < movies.get(i).getGenres().size() && !match; k++) {
                            if (command.getFilters().get(1).contains(movies.get(i).getGenres()
                                                .get(k))) {
                                match = true;
                            }
                        }

                        if (match) {
                            j++;
                            result += movies.get(i).getTitle();
                            result += ", ";
                        }
                    }
                }
                i++;
            }
        } else {
            if (command.getNumber() < shows.size()) {
                len = command.getNumber();
            } else {
                len = shows.size();
            }

            int i = 0, j = 0;
            while (i < shows.size() && j < len) {
                if (shows.get(i).getAverageRating() != 0) {
                    boolean match = false;
                    if (command.getFilters().get(0).contains(String.valueOf(shows.get(i)
                                        .getYear()))) {
                        for (int k = 0; k < shows.get(i).getGenres().size() && !match; k++) {
                            if (command.getFilters().get(1).contains(shows.get(i).getGenres()
                                                .get(k))) {
                                match = true;
                            }
                        }

                        if (match) {
                            j++;
                            result += shows.get(i).getTitle();
                            result += ", ";
                        }
                    }
                }
                i++;
            }
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
    /**
     * fiecarui film/serial i-am creat un camp in care se salveaza numarul de aparitii
     * in lista de filme favorite a user-ilor, se face sortarea in functie de acest camp
     * si se afiseaza rezultatul
     */
    public String solveFavoriteQuery(final ArrayList<UserInputData> users,
                                     final ArrayList<MovieInputData> movies,
                                     final ArrayList<SerialInputData> shows) {

        String result = "Query result: [";
        int initialLength = result.length();

        if (command.getObjectType().equals("movies")) {
            for (MovieInputData movie : movies) {
                boolean match = false;
                if (command.getFilters().get(0).contains(String.valueOf(movie.getYear()))) {
                    for (int k = 0; k < movie.getGenres().size() && !match; k++) {
                        if (command.getFilters().get(1).contains(movie.getGenres().get(k))) {
                            match = true;
                        }
                    }

                    if (match) {
                        for (UserInputData user : users) {
                            if (user.getFavoriteMovies().contains(movie.getTitle())) {
                                movie.setNrFavorite(movie.getNrFavorite() + 1);
                            }
                        }
                    }
                }
            }

            if (command.getSortType().equals("asc")) {
                movies.sort(Comparator.comparing(MovieInputData::getNrFavorite));
            } else {
                movies.sort(Comparator.comparing(MovieInputData::getNrFavorite).reversed());
            }

            int len;
            if (command.getNumber() < movies.size()) {
                len = command.getNumber();
            } else {
                len = movies.size();
            }

            for (int i = 0; i < len; i++) {
                if (movies.get(i).getNrFavorite() != 0) {
                    result += movies.get(i).getTitle();
                    result += ", ";
                }
            }

        } else {
            for (SerialInputData show : shows) {
                boolean match = false;
                if (command.getFilters().get(0).contains(String.valueOf(show.getYear()))) {
                    for (int k = 0; k < show.getGenres().size() && !match; k++) {
                        if (command.getFilters().get(1).contains(show.getGenres().get(k))) {
                            match = true;
                        }
                    }

                    if (match) {
                        for (UserInputData user : users) {
                            if (user.getFavoriteMovies().contains(show.getTitle())) {
                                show.setNrFavorite(show.getNrFavorite() + 1);
                            }
                        }
                    }
                }
            }

            if (command.getSortType().equals("asc")) {
                shows.sort(Comparator.comparing(SerialInputData::getNrFavorite));
            } else {
                shows.sort(Comparator.comparing(SerialInputData::getNrFavorite).reversed());
            }

            int len;
            if (command.getNumber() < shows.size()) {
                len = command.getNumber();
            } else {
                len = shows.size();
            }

            for (int i = 0; i < len; i++) {
                if (shows.get(i).getNrFavorite() != 0) {
                    result += shows.get(i).getTitle();
                    result += ", ";
                }
            }
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
    /**
     * fiecarui film/serial i-am creat un camp in care se salveaza durata totala,
     * se face sortarea in functie de durata, se verifica criteriile din comanda
     * si se afiseaza rezultatul
     */
    public String solveLongestQuery(final ArrayList<MovieInputData> movies,
                                    final ArrayList<SerialInputData> shows) {

        findDurationShow(shows);
        String result = "Query result: [";
        int initialLength = result.length();

        if (command.getSortType().equals("asc")) {
            shows.sort(Comparator.comparing(SerialInputData::getDuration));
            movies.sort(Comparator.comparing(MovieInputData::getDuration));
        } else {
            shows.sort(Comparator.comparing(SerialInputData::getDuration).reversed());
            movies.sort(Comparator.comparing(MovieInputData::getDuration).reversed());
        }

        int len;
        if (command.getObjectType().equals("movies")) {
            if (command.getNumber() < movies.size()) {
                len = command.getNumber();
            } else {
                len = movies.size();
            }

            int i = 0, j = 0;
            while (i < movies.size() && j < len) {
                if (movies.get(i).getDuration() != 0) {
                    boolean match = false;
                    if (command.getFilters().get(0).contains(String.valueOf(movies.get(i)
                                            .getYear()))) {
                        for (int k = 0; k < movies.get(i).getGenres().size() && !match; k++) {
                            if (command.getFilters().get(1).contains(movies.get(i).getGenres()
                                                    .get(k))) {
                                match = true;
                            }
                        }

                        if (match) {
                            j++;
                            result += movies.get(i).getTitle();
                            result += ", ";
                        }
                    }
                }
                i++;
            }
        } else {
            if (command.getNumber() < shows.size()) {
                len = command.getNumber();
            } else {
                len = shows.size();
            }

            int i = 0, j = 0;
            while (i < shows.size() && j < len) {
                if (shows.get(i).getDuration() != 0) {
                    boolean match = false;
                    if (command.getFilters().get(0).contains(String.valueOf(shows.get(i)
                                            .getYear()))) {
                        for (int k = 0; k < shows.get(i).getGenres().size() && !match; k++) {
                            if (command.getFilters().get(1).contains(shows.get(i).getGenres()
                                                    .get(k))) {
                                match = true;
                            }
                        }

                        if (match) {
                            j++;
                            result += shows.get(i).getTitle();
                            result += ", ";
                        }
                    }
                }
                i++;
            }
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
    /**
     * se afla numarul de vizualizari, se sorteaza video-urile in functie de acest
     * numar, se verifica criteriile si se afiseaza
     */
    public String solveMostViewedQuery(final ArrayList<UserInputData> users,
                                       final ArrayList<MovieInputData> movies,
                                       final ArrayList<SerialInputData> shows) {

        findNrViews(users, movies, shows);
        String result = "Query result: [";
        int initialLength = result.length();

        if (command.getSortType().equals("asc")) {
            movies.sort(Comparator.comparing(MovieInputData::getNrViews));
            shows.sort(Comparator.comparing(SerialInputData::getNrViews));
        } else {
            movies.sort(Comparator.comparing(MovieInputData::getNrViews).reversed());
            shows.sort(Comparator.comparing(SerialInputData::getNrViews).reversed());
        }

        int len;
        if (command.getObjectType().equals("movies")) {
            if (command.getNumber() < movies.size()) {
                len = command.getNumber();
            } else {
                len = movies.size();
            }

            int i = 0, j = 0;
            while (i < movies.size() && j < len) {
                if (movies.get(i).getNrViews() != 0) {
                    boolean match = false;
                    if (command.getFilters().get(0).contains(String.valueOf(movies
                                                        .get(i).getYear()))) {
                        for (int k = 0; k < movies.get(i).getGenres().size() && !match; k++) {
                            if (command.getFilters().get(1).contains(movies
                                                                .get(i).getGenres().get(k))) {
                                match = true;
                            }
                        }

                        if (match) {
                            j++;
                            result += movies.get(i).getTitle();
                            result += ", ";
                        }
                    }
                }
                i++;
            }
        } else {
            if (command.getNumber() < shows.size()) {
                len = command.getNumber();
            } else {
                len = shows.size();
            }

            int i = 0, j = 0;
            while (i < shows.size() && j < len) {
                if (shows.get(i).getNrViews() != 0) {
                    boolean match = false;
                    if (command.getFilters().get(0).contains(String.valueOf(shows.
                                            get(i).getYear()))) {
                        for (int k = 0; k < shows.get(i).getGenres().size() && !match; k++) {
                            if (command.getFilters().get(1).contains(shows.get(i).
                                                    getGenres().get(k))) {
                                match = true;
                            }
                        }

                        if (match) {
                            j++;
                            result += shows.get(i).getTitle();
                            result += ", ";
                        }
                    }
                }
                i++;
            }
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;

    }
    /**
     * se ordoneaza user-ii in functie de numarul de rating-ri pe care le-au dat
     * (numar calculat in metoda solveRatingCommand din clasa Commands) si se
     * afiseaza
     */
    public String solveNumberOfRatings(final ArrayList<UserInputData> users) {

        String result = "Query result: [";
        int initialLength = result.length();

        if (command.getSortType().equals("asc")) {
            users.sort(Comparator.comparing(UserInputData::getNumberOfRatings)
                    .thenComparing(UserInputData::getUsername));
        } else {
            users.sort(Comparator.comparing(UserInputData::getNumberOfRatings)
                    .thenComparing(UserInputData::getUsername).reversed());
        }

        int len;
        if (command.getNumber() < users.size()) {
            len = command.getNumber();
        } else {
            len = users.size();
        }

        int i = 0, j = 0;
        while (i < users.size() && j < len) {
            if (users.get(i).getNumberOfRatings() != 0) {
                result += users.get(i).getUsername();
                result += ", ";
                j++;
            }
            i++;
        }

        if (result.length() > initialLength) {
            result = result.substring(0, result.length() - 2);
        }
        result += "]";

        return result;
    }
}

