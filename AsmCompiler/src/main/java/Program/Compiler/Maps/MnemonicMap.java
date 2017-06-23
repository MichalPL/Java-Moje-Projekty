package Program.Compiler.Maps;

import Program.Compiler.Objects.MappedMnemonic;

import java.util.ArrayList;

import static Program.IDE.StaticClasses.StaticVariable.MNEMONICS;

/**
 * Created by Michal on 2016-03-17.
 */
public class MnemonicMap {
    private ArrayList<MappedMnemonic> mnemonicsMap;

    public MnemonicMap() {
        mnemonicsMap = new ArrayList<>();
        fill();
    }

    private void fill()
    {
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[0], "00000", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[1], "00001", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[2], "00010", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[3], "00011", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[4], "00100", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[5], "00101", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[6], "00110", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[7], "00111", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[8], "01000", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[9], "01001", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[10], "01010", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[11], "01011", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[12], "01100", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[13], "01101", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[14], "01110", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[15], "01111", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[16], "10000", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[17], "10001", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[18], "10010", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[19], "10011", 0));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[20], "10100", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[21], "10101", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[22], "10110", 2));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[23], "10111", 0));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[24], "11000", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[25], "11001", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[26], "11010", 1));
        mnemonicsMap.add(new MappedMnemonic(MNEMONICS[27], "11011", 2));
    }

    public int getArgCount(String name)
    {
        for (MappedMnemonic mm: mnemonicsMap) {
            if(mm.getName().equals(name))
                return mm.getArgCount();
        }
        return -1;
    }

    public boolean isMnemonic(String name) {
        if(name.isEmpty()) return false;
        for (MappedMnemonic mm: mnemonicsMap) {
            if(mm.getName().equals(name))
                return true;
        }
        return false;
    }

    public String getBinary(String name)
    {
        if(name.isEmpty()) return null;
        for (MappedMnemonic mm: mnemonicsMap) {
            if(mm.getName().equals(name))
                return mm.getBinaryCode();
        }
        return null;
    }

    public ArrayList<MappedMnemonic> getMnemonicsMap() {
        return mnemonicsMap;
    }
}
