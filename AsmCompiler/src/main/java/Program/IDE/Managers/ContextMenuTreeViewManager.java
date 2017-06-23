package Program.IDE.Managers;

import Program.IDE.Objects.TreeObject;
import Program.IDE.Workers.FileWorker;
import Program.IDE.StaticClasses.Utils;
import Program.IDE.View;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Program.IDE.Managers.DateManager.*;
import static Program.IDE.Managers.DialogManager.*;
import static Program.IDE.StaticClasses.StaticVariable.*;
import static Program.IDE.StaticClasses.Utils.*;

/**
 * Created by Michal on 2017-02-06.
 */
public class ContextMenuTreeViewManager {

    private View view;
    private TabAndTreeManager tabAndTreeManager;
    private FileWorker fileWorker;
    private ConsoleManager consoleManager;

    public ContextMenuTreeViewManager(View view, TabAndTreeManager tabAndTreeManager, FileWorker fileWorker, ConsoleManager consoleManager) {
        this.view = view;
        this.tabAndTreeManager = tabAndTreeManager;
        this.fileWorker = fileWorker;
        this.consoleManager = consoleManager;
    }

    public void setContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Otwórz");
        MenuItem item2 = new MenuItem("Nowy plik");
        MenuItem item3 = new MenuItem("Dodaj do projektu");
        MenuItem item4 = new MenuItem("Oczyść projekt");
        MenuItem item5 = new MenuItem("Pokaż w folderze");
        MenuItem item6 = new MenuItem("Usuń z projektu");
        MenuItem item7 = new MenuItem("Usuń z folderu");

        item1.setStyle("font-weight: bold");
        contextMenu.getItems().addAll(item1, new SeparatorMenuItem(),
                item2, item3,  item4, new SeparatorMenuItem(),
                item5, new SeparatorMenuItem(),
                item6,  item7);


        //otwieranie/aktywowanie zakladki
        item1.setOnAction(arg0 -> {
            TreeItem treeItem = (TreeItem) view.getTreeView().getSelectionModel().getSelectedItem();
            tabAndTreeManager.openTabAndAddTreeItem(getTreeObjectByTreeItem(treeItem), view);
        });

        //double click na item
        view.getTreeView().setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2) {
                TreeItem treeItem = (TreeItem) view.getTreeView().getSelectionModel().getSelectedItem();
                tabAndTreeManager.openTabAndAddTreeItem(getTreeObjectByTreeItem(treeItem), view);
            }
        });

        //nowy plik
        item2.setOnAction(arg0 -> {
            String fileName = createInputDialog("Nowy plik", "Proszę wpisać nazwę nowego pliku", "Nazwa:", "");
            if(fileName == null) {
                consoleManager.appendText("Nie udało się utworzyć nowego pliku, ponieważ nazwa nie może być pusta!");
                return;
            }
            //dodanie rozszerzenia
            if(!fileName.endsWith(".wlasm"))
                fileName = fileName + ".wlasm";

            File file = new File(projectPath + "\\" + fileName);

            if(!file.exists()) {
                fileWorker.createFile(file);
                fileWorker.addToFile(file, "//File created at " + getDate());
                tabAndTreeManager.addTabAndTreeItem(fileName, projectPath + "\\" + fileName, fileWorker.openFile(projectPath + "\\" + fileName));

            } else {
                consoleManager.appendText("Nie udało się utworzyć nowego pliku, ponieważ plik już istnieje!");
            }
        });

        //dodaj do projektu
        item3.setOnAction(arg0 -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Otwórz plik");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki binarne (*.bin) lub Pliki asemblerowe (*.wlasm)", "*.bin", "*.wlasm");
//            fileChooser.getExtensionFilters().add(extFilter2);
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File(projectPath));
            List<File> files = fileChooser.showOpenMultipleDialog(null); //lista niemodyfikowalna

            if(files == null) return;

            List<File> filesToModify = new ArrayList<>(files);
            for (int i = 0; i < filesToModify.size(); i++) {
                if(!filesToModify.get(i).getParent().equals(projectPath) || filesToModify.get(i).getParent().equals(projectPath + "\\excluded")) {
                    File copiedFile = new File(projectPath + "\\" + filesToModify.get(i).getName());
                    fileWorker.copyFile(filesToModify.get(i), copiedFile);
                    filesToModify.set(i, copiedFile);
                }
            }

            tabAndTreeManager.addFilesToView(filesToModify.toArray(new File[filesToModify.size()]));
        });

        //oczysc projekt
        item4.setOnAction(arg0 -> {
            Optional<ButtonType> result = createOkDialog("", "Oczyszczanie projektu", "Czy na pewno chcesz usunąć niektóre pliki, aby oczyścić projekt?");
            File[] files = new File(projectPath).listFiles();
            if(files == null) return;
            for (File f: files) {
                if(f.isFile()) {
                    if(FilenameUtils.getExtension(f.getName()).equals("pre_wlasm") || FilenameUtils.getExtension(f.getName()).equals("bin")) {
                        TreeObject treeObject = Utils.getTreeObjectByPath(f.getPath());
                        if(treeObject != null) {//bin
                            removeFromProject(treeObject.getItem());
                        }
                        fileWorker.deleteFile(f);
                    }
                }
            }
        });

        //pokaz w folderze
        item5.setOnAction(arg0 -> {
            TreeItem treeItem = (TreeItem) view.getTreeView().getSelectionModel().getSelectedItem();
            String pathToOpen = projectPath;
            if(treeItem.isLeaf()) {
                pathToOpen = Utils.getTreeObjectByTreeItem(treeItem).getPath();
            }
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + pathToOpen);
            } catch (IOException e) {
                consoleManager.appendText("Nie udało się otworzyć lokalizacji pliku!");
            }
        });

        //usuwanie z projektu
        item6.setOnAction(arg0 -> {
            Optional<ButtonType> result = createOkDialog("", "Usuwanie z projektu", "Czy na pewno chcesz usunąć plik z projektu?");
            if (result.get() == ButtonType.OK) {
                removeFromProject((TreeItem) view.getTreeView().getSelectionModel().getSelectedItem());
            }
        });

        //usuwanie z folderu
        item7.setOnAction(arg0 -> {
            Optional<ButtonType> result = createOkDialog("", "Usuwanie z folderu", "Czy na pewno chcesz usunąć plik na stałe z folderu?");
            if (result.get() == ButtonType.OK){
                TreeItem treeItem = (TreeItem) view.getTreeView().getSelectionModel().getSelectedItem();
                if(treeItem == null) return;
                TreeObject treeObject = Utils.getTreeObjectByTreeItem(treeItem);
                tabAndTreeManager.removeTab(treeObject.getTab());
                view.removeTreeItem(treeItem);
                editors.remove(treeObject);
                try {
                    fileWorker.deleteFile(new File(treeObject.getPath()));
                } catch (Exception e) {
                    consoleManager.appendText("Błąd! Plik nie istnieje!");
                }
            }
        });

        item1.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        item2.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        item5.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        item6.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        item7.setAccelerator(new KeyCodeCombination(KeyCode.DELETE, KeyCombination.CONTROL_DOWN));

        view.getTreeView().setContextMenu(contextMenu);
    }

    private void removeFromProject(TreeItem treeItem) {
        if(!treeItem.isLeaf()) return;
        TreeObject to = getTreeObjectByTreeItem(treeItem);
        fileWorker.createDir(new File(FilenameUtils.getFullPath(to.getPath()) + "\\excluded"));
        fileWorker.moveFile(new File(to.getPath()), new File(FilenameUtils.getFullPath(to.getPath()) + "\\excluded\\" +FilenameUtils.getName(to.getPath()) ));
        tabAndTreeManager.removeTab(to.getTab());
        view.removeTreeItem(treeItem);
        editors.remove(to);
    }
}
