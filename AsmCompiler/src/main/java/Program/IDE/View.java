package Program.IDE;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.util.List;

/**
 * Created by Michal on 2017-02-05.
 */
public class View {

    private MenuBar menuBar;
    private TextArea console;

    private TreeView treeView;
    private TreeItem<String> rootItem;
    private MultipleSelectionModel msm;

    private BorderPane borderPane;

    private TabPane tabPane;

    public View(Scene scene) {
        this.menuBar = (MenuBar) scene.lookup("#menubar");
        this.console = (TextArea) scene.lookup("#console");
        this.treeView = (TreeView) scene.lookup("#projectView");
        this.tabPane = (TabPane) scene.lookup("#editorTabs");
        this.borderPane = (BorderPane) scene.lookup("#border");
        msm = treeView.getSelectionModel();
        treeView.setStyle("-fx-font-size: 14px");
    }

    public boolean setNewRootTreeItem(TreeItem rootItem){
        if(rootItem == null) return false;
        this.rootItem = rootItem;
        treeView.setRoot(rootItem);
        rootItem.setExpanded(true);
        msm.select(rootItem);
        return true;
    }

    public boolean addNewTreeItem(TreeItem item) {
        if(item == null) return false;
        rootItem.getChildren().add(item);
        msm.select(item);
        return true;
    }

    public boolean removeTreeItem(TreeItem item) {
        if(item == null) return false;
        rootItem.getChildren().remove(item);
        return true;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public TextArea getConsole() {
        return console;
    }

    public void setConsole(TextArea console) {
        this.console = console;
    }

    public TreeView getTreeView() {
        return treeView;
    }

    public void setTreeView(TreeView treeView) {
        this.treeView = treeView;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
}
