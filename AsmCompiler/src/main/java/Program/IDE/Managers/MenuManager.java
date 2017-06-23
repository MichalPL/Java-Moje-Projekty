package Program.IDE.Managers;

import Program.IDE.Listeners.FileChangeListener;
import Program.IDE.Objects.TreeObject;
import Program.IDE.Runner;
import Program.IDE.View;
import Program.IDE.Workers.FileWorker;
import Program.IDE.StaticClasses.Utils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import static Program.IDE.Managers.DateManager.getDate;
import static Program.IDE.StaticClasses.StaticVariable.*;
import static java.util.Arrays.asList;

/**
 * Created by Michal on 2017-02-06.
 */
public class MenuManager {
    private MenuBar menuBar;
    private File openedFile;
    private ConsoleManager consoleManager;
    private FileWorker fileWorker;
    private ContextMenuTreeViewManager contextMenuTreeViewManager;
    private TabAndTreeManager tabAndTreeManager;
    private View view;

    private MenuItem newProject;
    private MenuItem openProjectItem;
    private MenuItem closeProject;
    private MenuItem saveFile;
    private MenuItem saveAllFiles;
    private MenuItem exitItem;

    private MenuItem setNextTab;
    private MenuItem setPrevTab;

    private Label compileLabel;

    public MenuManager(View view, FileWorker fileWorker, TabAndTreeManager tabAndTreeManager, ConsoleManager consoleManager) {
        this.menuBar = view.getMenuBar();
        this.fileWorker = fileWorker;
        this.tabAndTreeManager = tabAndTreeManager;
        this.view = view;
        contextMenuTreeViewManager = new ContextMenuTreeViewManager(view, tabAndTreeManager, fileWorker, consoleManager);
        this.consoleManager = consoleManager;
    }

    public void start() {
        consoleManager.appendText("Inicjalizacja środowiska...");
        consoleManager.appendText("Inicjalizacja menu...");
        setFirstMenu();
        setSecondMenu();
        setCompileMenu();

        consoleManager.addText("Zakończone");

        consoleManager.appendText("Inicjalizacja słuchaczy...");
        setDisableMenuItems(true);
        setActionNewProject();
        setActionOpenProject();
        setActionCloseProject();
        setActionSaveFiles();
        setActionSaveAllFiles();
        setActionCompile();
        setActionExit();
        setActionOnNextTab();
        setActionOnPrevTab();
        consoleManager.addText("Zakończone");

        consoleManager.appendText("Inicjalizacja środowiska zakończona!");
    }

    private void setFirstMenu() {
        addMenu("Plik", asList("Nowy projekt", "Otwórz projekt", "Zamknij projekt",  "Zapisz", "Zapisz wszystko", "Wyjście"));
        newProject = menuBar.getMenus().get(0).getItems().get(0);
        openProjectItem = menuBar.getMenus().get(0).getItems().get(1);
        closeProject = menuBar.getMenus().get(0).getItems().get(2);
        saveFile = menuBar.getMenus().get(0).getItems().get(3);
        saveAllFiles = menuBar.getMenus().get(0).getItems().get(4);
        exitItem = menuBar.getMenus().get(0).getItems().get(5);


        openProjectItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAllFiles.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
    }

    private void setCompileMenu() {
        compileLabel = new Label("Buduj i kompiluj");
        Image image = new Image(getClass().getResourceAsStream("/img/run.png"));
        compileLabel.setGraphic(new ImageView(image));

        Menu compile = new Menu();
        compileLabel.setId("compile");
        compile.setGraphic(compileLabel);
        menuBar.getMenus().add(compile);

        compile.setAccelerator(new KeyCodeCombination(KeyCode.F5));
    }

    private void setSecondMenu() {
        addMenu("Edycja", asList("Przełącz na następną zakładkę","Przełącz na poprzednią zakładkę"));
        setNextTab = menuBar.getMenus().get(1).getItems().get(0);
        setPrevTab = menuBar.getMenus().get(1).getItems().get(1);

//        setNextTab.setAccelerator(new KeyCodeCombination(KeyCode.TAB, KeyCombination.SHIFT_DOWN));
//        setPrevTab.setAccelerator(new KeyCodeCombination(KeyCode.CONTROL, KeyCombination.SHIFT_DOWN));
    }

    private void addMenu(String name, List<String> items) {
        Menu menu = new Menu(name);
        for (String s: items) {
            menu.getItems().add(new MenuItem(s));
        }
        menuBar.getMenus().add(menu);
    }

    private void setDisableMenuItems(boolean disabled) {
        closeProject.setDisable(disabled);
        saveFile.setDisable(disabled);
        saveAllFiles.setDisable(disabled);
        compileLabel.setDisable(disabled);
        view.getMenuBar().getMenus().get(1).setDisable(disabled);
    }

    /*
    KOMPILOWANIE
     */

    private void setActionCompile() {
        compileLabel.setOnMouseClicked(event -> {

            consoleManager.appendText("Zapisywanie plików...");
            saveAllFiles();
            consoleManager.addText("Zakończono");
            Runner runner = new Runner();
            Tab selectedTab = view.getTabPane().getSelectionModel().getSelectedItem();
            TreeObject treeObject = Utils.getTreeObjectByTab(selectedTab);
            if(treeObject == null) {
                consoleManager.appendText("Wystąpił błąd podczas rozpoznawnia pliku do kompilacji!");
                return;
            }
            consoleManager.appendText("Praca nad plikiem: " + treeObject.getPath());
            File fileToCompile = new File(treeObject.getPath());
            if(!runner.run(view, fileWorker, fileToCompile, tabAndTreeManager)) {
                consoleManager.appendText("Błąd! Sprawdź błędy!");
                return;
            }
            File compiledFile = new File(fileToCompile.getParent() + "\\" + FilenameUtils.getBaseName(fileToCompile.getName()) + ".bin");
            if(!compiledFile.exists()) {
                consoleManager.appendText("Błąd! Nie wygenerowano pliku!");
                return;
            }

            TreeObject treeObjectComplied = Utils.getTreeObjectByPath(compiledFile.getPath());
            if(treeObjectComplied == null) {
                tabAndTreeManager.addTabAndTreeItem(compiledFile.getName(), compiledFile.getPath(), fileWorker.openFile(compiledFile.getPath()));
            } else {
                tabAndTreeManager.openTabAndAddTreeItem(treeObjectComplied, view);
            }
        });
    }

    /*
    NOWY PROJEKT
     */
    private void setActionNewProject() {
        newProject.setOnAction(arg0 -> {
            String fileName = DialogManager.createInputDialog("Nowy projekt", "Tworzenie nowego projektu", "Wpisz nazwę nowego projektu", "");
            if(fileName == null || fileName.isEmpty()) return;
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("New Project");
            openedFile = dirChooser.showDialog(null);
            String projPath = openedFile.getPath() + "\\" + fileName;
            fileWorker.createDir(new File(projPath));
            fileWorker.createFile(new File(projPath + "\\" + "project.wlproj"));
            File projFile = new File(projPath + "\\" + "main.wlasm");
            fileWorker.createFile(projFile);
            fileWorker.addToFile(projFile, "//File created at " + getDate());
            clearProject();
            openProjectByFile(projPath + "\\" + "project.wlproj");
        });
    }

    /*
    ZAMYKANIE PROJEKT
     */
    private void setActionCloseProject() {
        closeProject.setOnAction(arg0 -> {
            clearProject();
            setDisableMenuItems(true);
        });
    }

    /*
    OTWIERANIE PROJEKTU
     */
    private void setActionOpenProject() {
        openProjectItem.setOnAction(arg0 -> {
//            DirectoryChooser dirChooser = new DirectoryChooser();
//            dirChooser.setTitle("Open Project");
//            openedFile = dirChooser.showDialog(null);
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Plik projektu (*.wlproj)", "*.wlproj");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Open Project");
            openedFile = fileChooser.showOpenDialog(null);
            if(openedFile != null){
                consoleManager.appendText("Otwieranie projektu...");
                clearProject();
                openProjectByFile(openedFile.getPath());
            }
        });
    }


    private void clearProject() {
        projectPath = "";
        view.getTabPane().getTabs().clear();
        view.getTreeView().setRoot(null);
        editors.clear();
    }

    private void openProjectByFile(String path) {
        if(path.isEmpty()) return;
        File file = new File(path);
        projectPath = file.getParent();
        String projectName = file.getName();
        FilenameFilter filter = (dir, name) -> (name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".wlasm"));
        File[] files = new File(projectPath).listFiles(filter);
        setProjectRoot(new TreeItem(projectName));

        contextMenuTreeViewManager.setContextMenu();
        setDisableMenuItems(false);

        if(tabAndTreeManager.addFilesToView(files)) {
            tabAndTreeManager.changeToWrongTab(compileLabel);
            try {
                new Thread(new FileChangeListener(consoleManager, tabAndTreeManager)).start();
            } catch (Exception e) {
                consoleManager.appendText("Nie udało się uruchomić nasłuchiwania na folder projektu!");
            }
            consoleManager.addText("Zakończone");
        } else {
            consoleManager.addText("Przerwane: To nie jest folder projektu White Lion Asm!");
            clearProject();
        }

    }

//    private void openProjectByFolder(String path) {
//        if(path.isEmpty()) return;
//        File folder = new File(path);
//        projectPath = path;
//        String projectName = folder.getName();
//        FilenameFilter filter = (dir, name) -> (name.toLowerCase().endsWith(".bin") || name.toLowerCase().endsWith(".wlasm"));
//        File[] files = folder.listFiles(filter);
//        TreeItem root = new TreeItem(projectName);
//        setProjectRoot(root);
//        contextMenuTreeViewManager.setContextMenu();
//        setDisableMenuItems(false);
//        if(tabAndTreeManager.addFilesToView(files)) {
//            tabAndTreeManager.changeToWrongTab(compileLabel);
//            try {
//                new Thread(new FileChangeListener(consoleManager, tabAndTreeManager)).start();
//            } catch (Exception e) {
//                consoleManager.appendText("Nie udało się uruchomić nasłuchiwania na folder projektu!");
//            }
//            consoleManager.addText("Zakończone");
//        } else {
//            consoleManager.addText("Przerwane: To nie jest folder projektu White Lion Asm!");
//            clearProject();
//        }
//    }

    private void setProjectRoot(TreeItem root) {
        view.setNewRootTreeItem(root);
    }


    /*
    ZAPISYWANIE
     */
    private void setActionSaveFiles() {
        saveFile.setOnAction(arg0 -> {
            Tab selectedTab = view.getTabPane().getSelectionModel().getSelectedItem();
            TreeObject treeObject = Utils.getTreeObjectByTab(selectedTab);
            if(treeObject == null) return;
            fileWorker.saveFile(treeObject.getPath(), ((CodeArea)selectedTab.getContent()).getText());
            consoleManager.appendText("Zapisano plik " + treeObject.getPath());
        });
    }

    private void setActionSaveAllFiles() {
        saveAllFiles.setOnAction(arg0 -> {
            saveAllFiles();
        });
    }

    private void saveAllFiles() {
        if(editors.isEmpty()) return;
        for (TreeObject to: editors ) {
            fileWorker.saveFile(to.getPath(), ((CodeArea)to.getTab().getContent()).getText());
            consoleManager.appendText("Zapisano plik " + to.getPath());
        }
    }

    /*
    ZAMYKANIE
     */

    private void setActionExit() {
        exitItem.setOnAction(arg0 -> {
            System.exit(0);
        });
    }


    /*
    PRZELACZANIE ZAKLADEK
     */

    private void setActionOnNextTab() {
        setNextTab.setOnAction(arg0 -> {
            if(view.getTabPane().getSelectionModel().getSelectedIndex() == view.getTabPane().getTabs().size() - 1){
                view.getTabPane().getSelectionModel().selectFirst();
            } else {
                view.getTabPane().getSelectionModel().selectNext();
            }
        });
    }

    private void setActionOnPrevTab() {
        setPrevTab.setOnAction(arg0 -> {
            if(view.getTabPane().getSelectionModel().getSelectedIndex() == 0){
                view.getTabPane().getSelectionModel().selectLast();
            } else {
                view.getTabPane().getSelectionModel().selectPrevious();
            }
        });
    }
}
