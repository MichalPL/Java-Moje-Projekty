package NeverwinterLiga;
import java.util.*;

/**
 * Created by Michal on 2016-04-06.
 */
public class DataWorker {
//    private Random rand;
    private InputWorker inputWorker;
    private AlertManager alertManager;

    public DataWorker(InputWorker inputWorker, AlertManager alertManager) {
//        rand = new Random();
        this.inputWorker = inputWorker;
        inputWorker.getData();
        this.alertManager = alertManager;
    }

    public List<Integer> getRandomRange(int max, int quantity) throws Exception {
        Random rng = new Random();
        List<Integer> generated = new ArrayList<Integer>();
        int size = 0;
        try {
            while (size < quantity) {
                Integer next = rng.nextInt(max) + 1;
                if (!generated.contains(next)) {
                    generated.add(next);
                    size++;
                }
            }
        } catch (Exception ignored) {
            alertManager.setError("Ilosc graczy nie moze byc mniejsza od losowanej!!");
        }
        return generated;
    }

    public int getExp(int i)
    {
        return inputWorker.getExp().get(i);
        //return rand.nextInt((3-1)+1) + 1; //((max - min) + 1) + min;
    }

    public int getSet(int i)
    {
        return inputWorker.getSet().get(i);
        //return rand.nextInt((2-1)+1) + 1; //((max - min) + 1) + min;
    }

    public int getItem(int i)
    {
        return inputWorker.getItem().get(i);
        //return rand.nextInt((4300-2000)+1) + 2000; //((max - min) + 1) + min;
    }

    public int getLeader(int i)
    {
        return inputWorker.getLeader().get(i);
        //return rand.nextInt((3-1)+1) + 1; //((max - min) + 1) + min;
    }
}
