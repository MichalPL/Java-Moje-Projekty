package Program.IDE.Managers;

import Program.IDE.StaticClasses.StaticVariable;
import Program.IDE.View;

import static Program.IDE.Managers.DateManager.*;

/**
 * Created by Michal on 2017-02-06.
 */
public class ConsoleManager {
    private View view;

    public ConsoleManager(View view) {
        this.view = view;
    }

    public void clear() {
        view.getConsole().setText("");
        StaticVariable.lineCounter = 0;
        view.getConsole().setId("console");
    }

    public void appendText(String text) {
        if(StaticVariable.lineCounter > 100) clear();
        if(StaticVariable.lineCounter == 0)
            view.getConsole().appendText(getDate() + "|| " + text);
        else
            view.getConsole().appendText("\n" + getDate() + "|| " + text);
        StaticVariable.lineCounter++;
    }

    public void addText(String text) {
        if(StaticVariable.lineCounter > 100) clear();
        view.getConsole().appendText(text);

        StaticVariable.lineCounter++;
    }
}
