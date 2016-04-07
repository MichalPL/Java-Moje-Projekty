package NeverwinterLiga;
import java.util.*;

/**
 * Created by Michal on 2016-04-06.
 */
public class TeamFixer {
    private double team1Win;
    private double team2Win;
    private AlertManager alertManager;
    private List<Integer> team1;
    private List<Integer> team2;
    private Map<Integer, Double> team1map;
    private Map<Integer, Double> team2map;

    public TeamFixer(Map<Integer, Double> team1map, Map<Integer, Double> team2map, AlertManager alertManager) {
        this.team1map = team1map;
        this.team2map = team2map;
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
        this.alertManager = alertManager;
    }

    public void fix(double team1Win, double team2Win, LinkedHashMap<Integer,Double> mapPeopleWithScore)
    {
        double team1wCopy = team1Win;
        double team2wCopy = team2Win;
       try {
            for (int i = 0; i < team1map.size(); i++) {
                double val1 = 0;
                double val2 = 0;
                if (team1wCopy >= team2wCopy) {
                    val1 = (new ArrayList<Double>(team1map.values())).get(i);
                    val2 = (new ArrayList<Double>(team2map.values())).get(i);
                } else {
                    val1 = (new ArrayList<Double>(team2map.values())).get(i);
                    val2 = (new ArrayList<Double>(team1map.values())).get(i);
                }
                double sub = val1 - val2;
                double teamSub = Math.abs(team1wCopy - team2wCopy);
                if ((val1 > val2 && sub <= teamSub)) {
                    team1.add((new ArrayList<Integer>(team2map.keySet())).get(i));
                    team2.add((new ArrayList<Integer>(team1map.keySet())).get(i));

                    team1wCopy += (new ArrayList<Double>(team2map.values())).get(i);
                    team2wCopy += (new ArrayList<Double>(team1map.values())).get(i);
                } else {
                    team1.add((new ArrayList<Integer>(team1map.keySet())).get(i));
                    team2.add((new ArrayList<Integer>(team2map.keySet())).get(i));

                    team1wCopy += (new ArrayList<Double>(team1map.values())).get(i);
                    team2wCopy += (new ArrayList<Double>(team2map.values())).get(i);
                }

                team1wCopy -= (new ArrayList<Double>(team1map.values())).get(i);
                team2wCopy -= (new ArrayList<Double>(team2map.values())).get(i);
            }
            this.team1Win = 0;
            this.team2Win = 0;
            Collections.sort(team1, new ArrayListComparator());
            Collections.sort(team2, new ArrayListComparator());
            for (int k = 0; k < team1.size(); k++) {
                this.team1Win += mapPeopleWithScore.get(team1.get(k));
                this.team2Win += mapPeopleWithScore.get(team2.get(k));
            }
           this.team1Win -= 1000;
           this.team2Win -= 1000;
       } catch (Exception ignored) {
            alertManager.setError("Błąd przy naprawianiu drużyn!");
       }
    }

    public List<Integer> getTeam1()
    {
        return team1;
    }

    public List<Integer> getTeam2()
    {
        return team2;
    }

    public double getTeam1Win() {
        return team1Win;
    }

    public double getTeam2Win() {
        return team2Win;
    }

}
