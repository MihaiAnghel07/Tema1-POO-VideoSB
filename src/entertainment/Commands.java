package entertainment;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import java.util.ArrayList;
import java.util.Map;

public final class Commands {
    private ActionInputData command;

    public void setCommand(final ActionInputData command) {
        this.command = command;
    }

    public String getCommandType() {
        return command.getType();
    }
    /**
     * se verifica daca filmul/show-ul a fost vizionat, daca da se verifica si daca se afla
     * sau nu in lista de favorite si se face prelucrarea
     */
    public String solveFavouriteCommand(final ArrayList<UserInputData> users) {

        String result = "";
        for (UserInputData user : users) {
            boolean isFavourite = false;
            if (user.getUsername().equals(command.getUsername())) {
                if (user.getHistory().containsKey(command.getTitle())) {
                    for (int k = 0; k < user.getFavoriteMovies().size(); k++) {
                        if (user.getFavoriteMovies().get(k).equals(command.getTitle())) {
                            result = result + "error -> " + command.getTitle()
                                    + " is already in favourite list";
                            isFavourite = true;
                            break;
                        }
                    }

                    if (!isFavourite) {
                        user.getFavoriteMovies().add(command.getTitle());
                        result = result + "success -> " + command.getTitle()
                                + " was added as favourite";
                    }

                } else {
                    result = result + "error -> " + command.getTitle() + " is not seen";
                    break;
                }
            }
        }
        return result;
    }
    /**
     * se verifica daca filmul/show-ul exista in istoric , se incrementeaza numarul de vizualizari
     * altfel, se adauga in istoric si numarul de vizualizari = 1
     */
    public String solveViewCommand(final ArrayList<UserInputData> users) {

        String result = "";
        for (UserInputData user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (user.getHistory().containsKey(command.getTitle())) {
                    for (Map.Entry<String, Integer> it : user.getHistory().entrySet()) {
                        if (it.getKey().equals(command.getTitle())) {
                            it.setValue(it.getValue() + 1);
                             result = result + "success -> " + command.getTitle()
                                     + " was viewed with total views of " + it.getValue();
                             break;
                        }
                    }
                } else {
                    user.getHistory().putIfAbsent(command.getTitle(), 1);
                    result = result + "success -> " + command.getTitle()
                            + " was viewed with total views of 1";
                    break;
                }
            }
        }
        return result;
    }
    /**
     * se verifica daca este filmul/show-ul se afla in istoric, in caz afirmativ
     * se parcurge lista filmului/sezonului in care sunt salvate numele celor care
     * au dat rating deja, se in cazul in care nu exista numele prezentului user,
     * se da rating si utilizatorul este introdus in lista celor care au dat rating
     */
    public String solveRatingCommand(final ArrayList<MovieInputData> movies,
                                     final ArrayList<SerialInputData> shows,
                                     final ArrayList<UserInputData> users) {

        for (UserInputData user : users) {
            if (user.getUsername().equals(command.getUsername())) {
                if (!user.getHistory().containsKey(command.getTitle())) {
                    return "error -> " + command.getTitle() + " is not seen";
                }
            }
        }

        String result = "";
        boolean wasRated = false;
        for (int i = 0; i < movies.size() && !wasRated; i++) {
            if (movies.get(i).getTitle().equals(command.getTitle())) {
                for (int j = 0; j < movies.get(i).getUsersWhoRated().size(); j++) {
                    if (command.getUsername().equals(movies.get(i).getUsersWhoRated().get(j))) {
                        result += "error -> " + command.getTitle() + " has been already rated";
                        wasRated = true;
                    }
                }

                if (!wasRated) {
                    wasRated = true;
                    movies.get(i).setRatings(command.getGrade());
                    movies.get(i).addUserWhoRate(command.getUsername());
                    result += "success -> " + command.getTitle() + " was rated with "
                            + command.getGrade() + " by " + command.getUsername();
                    boolean add = false;
                    for (int k = 0; k < users.size() && !add; k++) {
                        if (users.get(k).getUsername().equals(command.getUsername())) {
                            if (users.get(k).getHistory().containsKey(command.getTitle())) {
                                users.get(k).setNumberOfRatings(users.get(k)
                                        .getNumberOfRatings() + 1);
                                add = true;
                            }
                        }
                    }
                }
            }
        }
        if (!wasRated) {
            for (int i = 0; i < shows.size() && !wasRated; i++) {
                if (shows.get(i).getTitle().equals(command.getTitle())) {
                    for (int j = 0; j < shows.get(i).getSeasons().get(
                            command.getSeasonNumber() - 1).getUsersWhoRated().size(); j++) {
                        if (command.getUsername().equals(shows.get(i).getSeasons().
                                get(command.getSeasonNumber() - 1).
                                getUsersWhoRated().get(j))) {
                            result += "error -> " + command.getTitle()
                                    + " has been already rated";
                            wasRated = true;
                        }
                    }

                    if (!wasRated) {
                        wasRated = true;
                        shows.get(i).getSeasons().get(command.getSeasonNumber() - 1).
                                setRatings(command.getGrade());
                        shows.get(i).getSeasons().get(command.getSeasonNumber() - 1).
                                addUserWhoRated(command.getUsername());
                        result += "success -> " + command.getTitle() + " was rated with "
                                + command.getGrade() + " by " + command.getUsername();
                        boolean add = false;
                        for (int k = 0; k < users.size() && !add; k++) {
                            if (users.get(k).getUsername().equals(command.getUsername())) {
                                if (users.get(k).getHistory().containsKey(command.getTitle())) {
                                    users.get(k).setNumberOfRatings(users.get(k)
                                            .getNumberOfRatings() + 1);
                                    add = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
