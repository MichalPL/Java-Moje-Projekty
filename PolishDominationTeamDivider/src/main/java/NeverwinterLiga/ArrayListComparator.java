package NeverwinterLiga;
import java.util.Comparator;

/**
 * Created by Michal on 2016-04-06.
 */
public class ArrayListComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }
}
