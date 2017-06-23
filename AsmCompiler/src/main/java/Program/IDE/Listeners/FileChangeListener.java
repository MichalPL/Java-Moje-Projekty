package Program.IDE.Listeners;

import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Managers.TabAndTreeManager;
import Program.IDE.StaticClasses.Utils;

import java.io.IOException;
import java.nio.file.*;

import static Program.IDE.StaticClasses.StaticVariable.projectPath;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by Michal on 2017-02-07.
 */
public class FileChangeListener implements Runnable{

    private ConsoleManager consoleManager;
    private TabAndTreeManager tabAndTreeManager;

    public FileChangeListener(ConsoleManager consoleManager, TabAndTreeManager tabAndTreeManager) {
        this.consoleManager = consoleManager;
        this.tabAndTreeManager = tabAndTreeManager;
    }

    @Override
    public void run() {
        final Path path = FileSystems.getDefault().getPath(projectPath);
        FileSystem fs = path.getFileSystem ();
        try(WatchService service = fs.newWatchService()) {
            path.register(service, ENTRY_MODIFY);
            WatchKey key = null;
            boolean i = false;
            while(true) {
                key = service.take();
                WatchEvent.Kind<?> kind;
                for(WatchEvent<?> watchEvent : key.pollEvents()) {
                    kind = watchEvent.kind();
                    if (ENTRY_MODIFY == kind) {
                        if(i) {
                            Path newPath = ((WatchEvent<Path>) watchEvent).context();
                            tabAndTreeManager.updateFromThread(Utils.getTreeObjectByPath(projectPath + "\\" + newPath.toString()));
                        }
                        i = !i;
                    }
                }
                if(!key.reset()) {
                    break;
                }
            }

        } catch(IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
    }
}
