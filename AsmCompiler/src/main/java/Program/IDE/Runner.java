package Program.IDE;

import Program.Compiler.Managers.CompilerManager;
import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Managers.TabAndTreeManager;
import Program.IDE.Workers.FileWorker;
import Program.Compiler.Preprocessor.Preprocessor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michal on 2017-02-06.
 */
public class Runner {
    public boolean run(View view, FileWorker fileWorker, File file, TabAndTreeManager tabAndTreeManager) {
        ConsoleManager consoleManager = new ConsoleManager(view);
        CompilerManager compilerManager = new CompilerManager(consoleManager, fileWorker, tabAndTreeManager);
        return compilerManager.run(file);
    }
}
