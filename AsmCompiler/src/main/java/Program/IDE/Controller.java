package Program.IDE;

import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Managers.MenuManager;
import Program.IDE.Managers.TabAndTreeManager;
import Program.IDE.Workers.FileWorker;

public class Controller {

    public Controller(View view) {

        ConsoleManager consoleManager = new ConsoleManager(view);

        FileWorker fileWorker = new FileWorker(consoleManager);

        TabAndTreeManager tabAndTreeManager = new TabAndTreeManager(view, fileWorker, consoleManager);

        MenuManager menuManager = new MenuManager(view, fileWorker, tabAndTreeManager, consoleManager);
        menuManager.start();

    }
}
