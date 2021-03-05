package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.SolveAction;
import fileio.*;
import org.json.simple.JSONArray;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        ArrayList<UserInputData> users = new ArrayList<>();
        users.addAll(input.getUsers());
        ArrayList<ActionInputData> commands = new ArrayList<>();
        commands.addAll(input.getCommands());
        ArrayList<MovieInputData> movies = new ArrayList<>();
        movies.addAll(input.getMovies());
        ArrayList<SerialInputData> shows = new ArrayList<>();
        shows.addAll(input.getSerials());
        ArrayList<ActorInputData> actors = new ArrayList<>();
        actors.addAll(input.getActors());
        SolveAction solveAction = new SolveAction();

        for (ActionInputData command : commands) {
            solveAction.setCommand(command);
            arrayResult.add(fileWriter.writeFile(command.getActionId(),
                    command.getTitle(), solveAction.solve(actors, users, movies, shows)));
        }

        fileWriter.closeJSON(arrayResult);
    }
}