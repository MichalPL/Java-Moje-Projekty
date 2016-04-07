package NeverwinterLiga;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

/**
 * Created by Michal on 2016-04-06.
 */
public class Printer {
    Matcher matcher;
    InputWorker inputWorker;
    private AlertManager alertManager;

    public Printer(Matcher matcher, InputWorker inputWorker, AlertManager alertManager) {
        this.matcher = matcher;
        this.inputWorker = inputWorker;
    }

    public void printAllData()
    {
        try {
            ArrayList<String> people = inputWorker.getNick();
            String people1 = "";
            String people2 = "";
            for (int i = 0; i < matcher.getTeam1().size() - 1; i++) {
                people1 += people.get(matcher.getTeam1().get(i)) + "(" + matcher.getTeam1().get(i) + "), ";
                people2 += people.get(matcher.getTeam2().get(i)) + "(" + matcher.getTeam2().get(i) + "), ";
            }
            people1 += people.get(matcher.getTeam1().get(matcher.getTeam1().size() - 1)) + "(" + matcher.getTeam1().get(matcher.getTeam1().size() - 1) + ")";
            people2 += people.get(matcher.getTeam2().get(matcher.getTeam2().size() - 1)) + "(" + matcher.getTeam2().get(matcher.getTeam2().size() - 1) + ")";
            String s = "Team 1: " + people1 + "\n\n"
                    + "Team 2: " + people2 + "\n\n"
                    + "Szansa na wygraną teamu 1: " + Utils.round(matcher.getTeam1Chance() * 100.0 / matcher.getSumTeamsChance(), 2) + "%\n"
                    + "Szansa na wygraną teamu 2: " + Utils.round(matcher.getTeam2Chance() * 100.0 / matcher.getSumTeamsChance(), 2) + "%";
            TextArea log = inputWorker.getTextArea("log");
            log.setText(s);
        } catch (Exception ignored) {
            alertManager.setError("Problem z wyświetleniem!");
        }
    }
}
