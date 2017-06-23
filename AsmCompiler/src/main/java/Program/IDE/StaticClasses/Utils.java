package Program.IDE.StaticClasses;

import Program.IDE.Objects.TreeObject;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import org.apache.commons.io.FilenameUtils;

import java.util.List;
import java.util.stream.Collectors;

import static Program.IDE.StaticClasses.StaticVariable.*;

/**
 * Created by Michal on 2017-02-06.
 */
public class Utils {
    static public TreeObject getTreeObjectByTab(Tab tab) {
        for (TreeObject obj: editors) {
            if(obj.getTab().equals(tab))
                return obj;
        }
        return null;
    }

    static public TreeObject getTreeObjectByTreeItem(TreeItem treeItem) {
        if(!treeItem.isLeaf()) return null;

        for (TreeObject obj: editors) {
            if(obj.getItem().equals(treeItem)) {
                return obj;
            }
        }
        return null;
    }

    static public TreeObject getTreeObjectByPath(String path) {
        for (TreeObject obj: editors) {
            if(obj.getPath().equals(path)) {
                return obj;
            }
        }
        return null;
    }

    static public List<TreeObject> getTreeObjectListByExtension(String extension) {
        return editors.stream().filter(obj -> FilenameUtils.getExtension(obj.getPath()).equals(extension)).collect(Collectors.toList());
    }

    static public String merge(List<String> items) {
        String code = "";
        for (String s: items) {
            code += s;
        }
        return code;
    }

    static public String getFistWord(String s) {
        if(s.contains(" ")){
            return s.substring(0, s.indexOf(" "));
        } else {
            return s;
        }
    }
}
