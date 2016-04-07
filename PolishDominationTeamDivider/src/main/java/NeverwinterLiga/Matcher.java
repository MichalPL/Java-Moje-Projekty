package NeverwinterLiga;
import javafx.scene.Parent;

import java.util.*;

/**
 * Created by Michal on 2016-04-06.
 */
public class Matcher {
    private DataWorker dataWorker;
    private TeamFixer teamFixer;
    private TeamDivider teamDivider;
    private Weight weight;
    private AlertManager alertManager;

    private Map<Integer, Double> team1map;
    private Map<Integer, Double> team2map;

    private List<Integer> team1;
    private List<Integer> team2;

    private LinkedHashMap<Integer,Double> mapPeopleWithScore;

    private int allPlayersCount = 0;
    private int playersCount = 0;

    private double team1Win = 0;
    private double team2Win = 0;
    private double sumTeamsWins = 0;

    public Matcher(DataWorker dataWorker, Utils utils, int allPlayersCount, int playersCount,
                   Weight weight, AlertManager alertManager)
    {
        this.dataWorker = dataWorker;
        this.allPlayersCount = allPlayersCount;
        this.playersCount = playersCount;
        this.weight = weight;
        team1map = new LinkedHashMap<Integer, Double>();
        team2map = new LinkedHashMap<Integer, Double>();
        this.alertManager = alertManager;
        teamFixer = new TeamFixer(team1map, team2map, alertManager);
        teamDivider = new TeamDivider(utils, team1map, team2map, alertManager);
    }

    public void match() throws Exception {
        int weightExp = weight.getExp();
        int weightSet = weight.getSet();
        int weightItem = weight.getItem();
        int weightLeader = weight.getLeader();
        int countExpAnswers = 3;
        int countSetAnswers = 3;
        int countItemAnswers = 4300;
        if(allPlayersCount%2 != 0) {
            playersCount--;
        }
        List<Integer> list = dataWorker.getRandomRange(allPlayersCount, playersCount);
        mapPeopleWithScore = new LinkedHashMap<Integer, Double>();
        try {
            for (int i = 0; i < list.size(); i++) {
                int exp = dataWorker.getExp(i);
                int set = dataWorker.getSet(i);
                float item = dataWorker.getItem(i);
                int leader = dataWorker.getLeader(i);
                double score = Utils.round((double) exp * weightExp / (double) countExpAnswers
                        + (double) set * weightSet / (double) countSetAnswers
                        + (double) item * weightItem / (double) countItemAnswers
                        + (double) leader * weightLeader, 3);
                mapPeopleWithScore.put(i, score);
            }
        } catch (Exception ignored) {
            alertManager.setError("Problem z liczeniem wag ;/ Sprawdź puste linie!");
        }
        divide();
        fix();
    }

    private void divide()
    {
        teamDivider.divide(mapPeopleWithScore);
        team1Win = teamDivider.getTeam1Win();
        team2Win = teamDivider.getTeam2Win();
        sumTeamsWins = teamDivider.getSumTeamsWins();
    }

    private void fix()
    {
        teamFixer.fix(team1Win, team2Win, mapPeopleWithScore);
        team1 = teamFixer.getTeam1();
        team2 = teamFixer.getTeam2();
        team1Win = teamFixer.getTeam1Win();
        team2Win = teamFixer.getTeam2Win();
        sumTeamsWins = Utils.round(team1Win + team2Win,3);
//        System.out.println("aaa" + team1Win + " " + team2Win + " " + sumTeamsWins);
    }

    public List<Integer> getTeam1()
    {
        return team1;
    }

    public List<Integer> getTeam2()
    {
        return team2;
    }

    public Map<Integer, Double> getTeam1map()
    {
        return team1map;
    }

    public Map<Integer, Double> getTeam2map()
    {
        return team2map;
    }

    public double getTeam1Chance()
    {
        return team1Win;
    }

    public double getTeam2Chance()
    {
        return team2Win;
    }

    public double getSumTeamsChance()
    {
        return sumTeamsWins;
    }
}
