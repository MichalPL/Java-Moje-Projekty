package NeverwinterLiga;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Michal on 2016-04-06.
 */
public class InputWorker {
    private Scene scene;
    private ArrayList<String> nick;
    private ArrayList<Integer> exp;
    private ArrayList<Integer> set;
    private ArrayList<Integer> item;
    private ArrayList<Integer> leader;
    AlertManager alertManager;

    private Weight weight;

    public InputWorker(Scene scene, Weight weight, AlertManager alertManager) {
        this.scene = scene;
        this.weight = weight;
        nick = new ArrayList<String>();
        exp = new ArrayList<Integer>();
        set = new ArrayList<Integer>();
        item = new ArrayList<Integer>();
        leader = new ArrayList<Integer>();
        this.alertManager = alertManager;
    }

    public void saveToFile()
    {
        TextField expPvP = getTextField("expPvP");
        TextField setPvP = getTextField("setPvP");
        TextField itemPvP = getTextField("itemPvP");
        String expPvpString = expPvP.getText();
        String setPvpString = setPvP.getText();
        String itemPvpString = itemPvP.getText();
//        System.out.println(expPvpString + " " + setPvpString + " " + itemPvpString);
        if((Integer.parseInt(expPvpString) < 100 && Integer.parseInt(expPvpString) > 0) &&
                (Integer.parseInt(setPvpString) < 100 && Integer.parseInt(setPvpString) > 0) &&
                (Integer.parseInt(itemPvpString) < 100 && Integer.parseInt(itemPvpString) > 0)) {
            weight.setExp(Integer.parseInt(expPvpString));
            weight.setSet(Integer.parseInt(setPvpString));
            weight.setItem(Integer.parseInt(itemPvpString));
            weight.setLeader(1000);
        } else {
            alertManager.setError("Waga musi być w przedziale 0-100!");
        }
    }

    public int getAllPlayerCount()
    {
        TextArea textArea = getTextArea("list");
        String text = textArea.getText().replaceAll("(?m)^\\s+$", "").replaceAll("\\s", "");
        String[] lines = text.split("\\;");
        return lines.length;
    }

    public void getData()
    {
        try {
            TextArea textArea = getTextArea("list");
            String text = textArea.getText().replaceAll("(?m)^\\s+$", "").replaceAll("\\s", "");
            String[] lines = text.split("\\;");
            for (int i = 0; i < lines.length; i++) {
                String[] args = lines[i].split("\\W+");
                nick.add(args[0]);
                exp.add(Integer.parseInt(args[1]));
                set.add(Integer.parseInt(args[2]));
                item.add(Integer.parseInt(args[3]));
                leader.add(Integer.parseInt(args[4]));
            }
        } catch(Exception ignored) {
            alertManager.setError("Nieprawidłowe argumenty!");
        }
    }

    public ArrayList<String> getNick()
    {
        return nick;
    }

    public ArrayList<Integer> getExp()
    {
        return exp;
    }

    public ArrayList<Integer> getSet()
    {
        return set;
    }

    public ArrayList<Integer> getItem()
    {
        return item;
    }

    public ArrayList<Integer> getLeader()
    {
        return leader;
    }

    private TextField getTextField(String id)
    {
        return (TextField) scene.lookup("#" + id);
    }

    public TextArea getTextArea(String id)
    {
        return (TextArea) scene.lookup("#" + id);
    }


}
