package Program.IDE.Objects;

import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;

/**
 * Created by Michal on 2017-02-06.
 */
public class TreeObject {
    private Tab tab;
    private TreeItem item;
    private String path;
    private boolean opened;
    private Editor editor;

    public TreeObject() {
    }

    public TreeObject(Tab tab, TreeItem item, String path, boolean opened, Editor editor) {
        this.tab = tab;
        this.item = item;
        this.path = path;
        this.opened = opened;
        this.editor = editor;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public TreeItem getItem() {
        return item;
    }

    public void setItem(TreeItem item) {
        this.item = item;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
}
