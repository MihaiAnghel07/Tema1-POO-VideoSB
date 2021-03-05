package entertainment;

import fileio.*;

import java.util.ArrayList;

public final class SolveAction {
    private ActionInputData command;

    public void setCommand(final ActionInputData command) {
        this.command = command;
    }

    public String getCommand() {
        return command.getType();
    }
    /**
     * metoda calculeaza rezultatul actiunii apeland
     * metodele claselor corespunzatoare
     */
    public String solve(final ArrayList<ActorInputData> actors,
                        final ArrayList<UserInputData> users,
                        final ArrayList<MovieInputData> movies,
                        final ArrayList<SerialInputData> shows) {

        if (command.getActionType().equals("command")) {
            Commands action = new Commands();
            action.setCommand(command);
            if (command.getType().equals("favorite")) {
                return action.solveFavouriteCommand(users);
            }

            if (command.getType().equals("view")) {
                return action.solveViewCommand(users);
            }

            if (command.getType().equals("rating")) {
                return action.solveRatingCommand(movies, shows, users);
            }
        }
        if (command.getActionType().equals("query")) {
            Query query = new Query();
            query.setCommand(command);
            if (command.getObjectType().equals("actors")) {
                if (command.getCriteria().equals("average")) {
                    return query.solveAverageQuery(actors, movies, shows);
                }
                if (command.getCriteria().equals("awards")) {
                    return query.solveAwardsQuery(actors);
                }
                if (command.getCriteria().equals("filter_description")) {
                    return query.solveFilterDescriptionQuery(actors);
                }
            }
            if (command.getObjectType().equals("shows")
                    || command.getObjectType().equals("movies")
                    || command.getObjectType().equals("users")) {

                if (command.getCriteria().equals("ratings")) {
                    return query.solveRatingsQuery(movies, shows);
                }
                if (command.getCriteria().equals("favorite")) {
                    return query.solveFavoriteQuery(users, movies, shows);
                }
                if (command.getCriteria().equals("longest")) {
                    return query.solveLongestQuery(movies, shows);
                }
                if (command.getCriteria().equals("most_viewed")) {
                    return query.solveMostViewedQuery(users, movies, shows);
                }
                if (command.getCriteria().equals("num_ratings")) {
                    return query.solveNumberOfRatings(users);
                }
            }
        }
        if (command.getActionType().equals("recommendation")) {
            Recommends recommendation = new Recommends();
            recommendation.setCommand(command);
            if (command.getType().equals("standard")) {
                return recommendation.solveStandardRecommendation(users, movies, shows);
            }
            if (command.getType().equals("best_unseen")) {
                return recommendation.solveBestUnseenRecommendation(movies, shows);
            }
            if (command.getType().equals("search")) {
                return recommendation.solveSearchRecommendation(users, movies, shows);
            }
        }
        return null;
    }
}
