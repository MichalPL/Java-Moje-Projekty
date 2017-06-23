package Program.IDE.Managers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Michal on 2017-02-06.
 */
public class DateManager {
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
