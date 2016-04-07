package NeverwinterLiga;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Michal on 2016-04-06.
 */
public class TeamDivider {
    private Utils utils;
    private Map<Integer, Double> team1map;
    private Map<Integer, Double> team2map;
    private AlertManager alertManager;
    private double team1Win = 0;
    private double team2Win = 0;
    private double sumTeamsWins = 0;

    public TeamDivider(Utils utils, Map<Integer, Double> team1map, Map<Integer, Double> team2map, AlertManager alertManager) {
        this.utils = utils;
        this.team1map = team1map;
        this.team2map = team2map;
        this.alertManager = alertManager;
    }

    public void divide(LinkedHashMap<Integer,Double> map)
    {
        LinkedHashMap sortedMap = utils.sort(map);
        int i = 0;
        double sub = 0;
        double sub2 = 0;
        double scoreSum = 0, scoreSum2 = 0;
        try {
            for (int j = map.size() - 1; j > map.size() / 2; j -= 2) {
                    int val1 = (new ArrayList<Integer>(sortedMap.keySet())).get(i);
                    int val2 = (new ArrayList<Integer>(sortedMap.keySet())).get(j);
                    int val3 = 0;
                    int val4 = 0;
                    if(j > map.size() / 2) {
                        val3 = (new ArrayList<Integer>(sortedMap.keySet())).get(i + 1);
                        val4 = (new ArrayList<Integer>(sortedMap.keySet())).get(j - 1);
                    }
                    sub = scoreSum - scoreSum2;
                    scoreSum = Utils.round((double) sortedMap.get(val1) + (double) sortedMap.get(val2), 3);
                    scoreSum2 = Utils.round((double) sortedMap.get(val3) + (double) sortedMap.get(val4), 3);
                    sub2 = scoreSum - scoreSum2;

                    if ((sub > 0 && sub2 > 0) || (sub < 0 && sub2 < 0)) {
                        if(j > map.size() / 2) {
                            team1map.put(val3, (double) sortedMap.get(val3));
                            team1map.put(val4, (double) sortedMap.get(val4));
                        }
                        team2map.put(val1, (double) sortedMap.get(val1));
                        team2map.put(val2, (double) sortedMap.get(val2));

                    } else {
                        team1map.put(val1, (double) sortedMap.get(val1));
                        team1map.put(val2, (double) sortedMap.get(val2));

                        if(j > map.size() / 2) {
                            team2map.put(val3, (double) sortedMap.get(val3));
                            team2map.put(val4, (double) sortedMap.get(val4));
                        }
                    }
                    i += 2;
            }

            int val1 = (new ArrayList<Integer>(sortedMap.keySet())).get(map.size() / 2 - 1);
            int val2 = (new ArrayList<Integer>(sortedMap.keySet())).get(map.size() / 2);
            if((map.size()/2)%2 != 0) {
                if ((sub > 0 && val1 > val2) || (sub < 0 && val1 < val2)) {
                    team1map.put(val2, (double) sortedMap.get(val2));
                    team2map.put(val1, (double) sortedMap.get(val1));
                } else {
                    team1map.put(val1, (double) sortedMap.get(val1));
                    team2map.put(val2, (double) sortedMap.get(val2));
                }
            }

            for (int k = 0; k < team1map.size(); k++) {
                team1Win += (new ArrayList<Double>(team1map.values())).get(k);
                team2Win += (new ArrayList<Double>(team2map.values())).get(k);
            }
            team1Win = Utils.round(team1Win, 3);
            team2Win = Utils.round(team2Win, 3);
            sumTeamsWins = Utils.round(team1Win + team2Win, 3);
        } catch (Exception ignored) {
            alertManager.setError("Błąd przy dzieleniu drużyn!");
        }
    }

    public double getTeam1Win()
    {
        return team1Win;
    }

    public double getTeam2Win()
    {
        return team2Win;
    }

    public double getSumTeamsWins()
    {
        return sumTeamsWins;
    }
}
