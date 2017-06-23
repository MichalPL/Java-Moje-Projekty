package Program.Compiler.Objects;

import Program.Compiler.Maps.MnemonicMap;
import Program.Compiler.Maps.RegistryMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Michal on 2017-02-07.
 */
public class ParsedObject {

    private Map<String, Integer> labelMap;
    private ArrayList<Mnemonic> mnemonicsList;

    public ParsedObject(Map<String, Integer> labelMap, ArrayList<Mnemonic> mnemonicsList) {
        this.labelMap = labelMap;
        this.mnemonicsList = mnemonicsList;
    }

    public Map<String, Integer> getLabelMap() {
        return labelMap;
    }

    public void setLabelMap(Map<String, Integer> labelMap) {
        this.labelMap = labelMap;
    }
    public ArrayList<Mnemonic> getMnemonicsList() {
        return mnemonicsList;
    }

    public void setMnemonicsList(ArrayList<Mnemonic> mnemonicsList) {
        this.mnemonicsList = mnemonicsList;
    }
}
