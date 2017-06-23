package Program.IDE.Managers;

import Program.IDE.Objects.Editor;
import Program.IDE.Objects.TreeObject;
import Program.IDE.View;
import Program.IDE.Workers.FileWorker;
import Program.IDE.StaticClasses.StaticVariable;
import Program.IDE.StaticClasses.Utils;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import org.apache.commons.io.FilenameUtils;
import org.fxmisc.richtext.CodeArea;

import java.io.File;

import static Program.IDE.StaticClasses.StaticVariable.projectPath;

/**
 * Created by Michal on 2017-02-06.
 */
public class TabAndTreeManager {
    private View view;
    private FileWorker fileWorker;
    private ConsoleManager consoleManager;

    public TabAndTreeManager(View view, FileWorker fileWorker, ConsoleManager consoleManager) {
        this.view = view;
        this.fileWorker = fileWorker;
        this.consoleManager = consoleManager;
        addColorCell();
    }

    private void addColorCell() {
        view.getTreeView().setCellFactory(tv -> new TreeCell<String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if ((empty || item == null)) {
                    setText("");
                } else {
                    setText(item);
                }
            }

        });
    }

    public void changeToWrongTab(Label compileLabel) {
        view.getTabPane().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            try {
                ((CodeArea) newValue.getContent()).requestFocus();
                if(view.getTabPane().getTabs().size() == 0) return;
                if(projectPath.equals("")) return;
                if (!FilenameUtils.getExtension(newValue.getText()).equals("wlasm")) {
                    compileLabel.setDisable(true);
                } else if (FilenameUtils.getExtension(new File(newValue.getText()).getName()).equals("wlasm")) {
                    compileLabel.setDisable(false);
                } else {
                    compileLabel.setDisable(true);
                }
            } catch (Exception e) {
                consoleManager.appendText("Wystąpił nieoczekiwany błąd...Przycisk kompiluj może nie działać prawidłowo!");
            }
        });
    }

    //dodawanie do widoku tab i treeitem
    public void addTabAndTreeItem(String name, String path, String content) {
        Tab tab = new Tab(name);
        TreeItem item = new TreeItem(name);
        if(tab.getText().endsWith(".bin")) {
            tab.setId("binary");
        } else {
            tab.setId("wlasmfile");
        }
        addNewTab(tab);
        view.addNewTreeItem(item);

        StaticVariable.editors.add(new TreeObject(tab, item, path, true, null));
        tabSetAction(tab);
        addTabContent(tab, content);


        //zaznaczanie zakladki
        SingleSelectionModel<Tab> selectionModel = view.getTabPane().getSelectionModel();
        selectionModel.select(tab);
    }

    //dodanie codearea z trescia do tab
    private void addTabContent(Tab tab, String con) {
        CodeArea codeArea = new Editor().getCodeArea();
        codeArea.appendText(con);
        tab.setContent(codeArea);
    }

    //zamykanie zmienia na false
    public void tabSetAction(Tab tab) {
        tab.setOnClosed(arg0 -> {
            for (TreeObject to: StaticVariable.editors) {
                if(to.getTab().equals(tab)) {
                    to.setOpened(false);
                }
            }
        });
    }

    public void openTabAndAddTreeItem(TreeObject treeObj, View view) {
        if(treeObj == null) return;
        if(!treeObj.isOpened()) {
            treeObj.setOpened(true);
            addNewTab(treeObj.getTab());
            tabSetAction(treeObj.getTab());
        } else {
            selectTab(treeObj.getTab());
        }
    }

    public void removeTab(Tab tab) {
        view.getTabPane().getTabs().remove(tab);
    }

    public void updateFromThread(TreeObject treeObj)
    {
        Platform.runLater(() -> {
            try {
                updateContent(treeObj);
            } catch (Exception ignored) { }
        });
    }

    public void updateContent(TreeObject treeObj) {
        String oldContent = ((CodeArea) treeObj.getTab().getContent()).getText();
        String newContent = fileWorker.openFile(treeObj.getPath());
        if(!oldContent.equals(newContent)) {
            Editor ed = new Editor();
            treeObj.setEditor(ed);
            treeObj.getTab().setContent(ed.getCodeArea());
            ((CodeArea) treeObj.getTab().getContent()).appendText(newContent);
            consoleManager.appendText("Zaktualizowano plik " + treeObj.getPath());
        }
    }

    public boolean addFilesToView(File[] files) {
        //boolean isWLProject = false;
        if(files == null) return false;
        for (File f: files) {
            if(f.isFile() && Utils.getTreeObjectByPath(f.getPath()) == null) {
                //if(FilenameUtils.getExtension(f.getName()).equals("wlasm") || FilenameUtils.getExtension(f.getName()).equals("bin")) {
                    addTabAndTreeItem(f.getName(), f.getPath(), fileWorker.openFile(f.getPath()));
                    //isWLProject = true;
                //}
            } else {
                consoleManager.appendText("Plik " + f.getPath() + " jest już w projekcie!");
                return false;
            }
        }
        return true;
        //return isWLProject;
    }


    public boolean selectTab(Tab tab) {
        if(tab == null) return false;
        //zaznaczanie zakladki
        SingleSelectionModel<Tab> selectionModel = view.getTabPane().getSelectionModel();
        selectionModel.select(tab);
//        tab.getContent().requestFocus();
        return true;
    }


    public boolean addNewTab(Tab tab) {
        if(tab == null) return false;

        //nowa zakladka
        view.getTabPane().getTabs().add(tab);
        return true;
    }
}

