package Program.Compiler.LexerAndParser;

import Program.Compiler.Maps.MnemonicMap;
import Program.Compiler.Objects.Mnemonic;
import Program.Compiler.Objects.ParsedObject;
import Program.Compiler.Preprocessor.Preprocessor;
import Program.Compiler.Validator;
import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Managers.TabAndTreeManager;
import Program.IDE.Workers.FileWorker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michal on 2017-02-07.
 */
public class LexerAndParser {
    private FileWorker fileWorker;
    private Map<String, Integer> labelMap;
    private Validator validator;
    private MnemonicMap mnemonicMap;
    private ConsoleManager consoleManager;
    private TabAndTreeManager tabAndTreeManager;
    private Mnemonic mnemonic;
    private ArrayList<Mnemonic> mnemonicsList;


    public LexerAndParser(FileWorker fileWorker, Validator validator,
                          MnemonicMap mnemonicMap, ConsoleManager consoleManager, TabAndTreeManager tabAndTreeManager) {
        this.fileWorker = fileWorker;
        this.validator = validator;
        this.mnemonicMap = mnemonicMap;
        this.consoleManager = consoleManager;
        this.tabAndTreeManager = tabAndTreeManager;

        mnemonicsList = new ArrayList<>();
        labelMap = new HashMap<>();
    }

    public ParsedObject run(File in) {
        int lines = fileWorker.getLinesCount(in);
        int lineCounter = 0; //zlicza kolejne wiersze
        for (int i = 1; i <= lines; i++) {
            String line = fileWorker.getLine(i, in).replaceAll("^\\s+", ""); //usuniecie z wiersza spacji na lewo od poczatku pierwszego slowa
            if(line.startsWith("include")) continue; //jesli dyrektywa to olej
            if(line.isEmpty() || line.startsWith("//")) continue; //jesli linia jest pusta lub jest komentarzem
            if (line.split("[^\\w-]+").length == 1 && validator.isLabel(line.replaceAll("\\s", ""))) { //jeśli to etykieta, a nie zdefiniowanie jumpa do etykiety
                labelMap.put(line.replaceAll("\\W+", ""), lineCounter);
                //dodanie do listy etykiet i usuniecie dwukropkow itp itd |||||| NUMEROWANIE LINII OD 0 (jesli chcemy od 1 to lineCounter + 1)!!!!
            } else {
                lineCounter++; //zwiekszamy ilosc wierszy
                String[] splitString = line.split("[^\\w-]+"); //podzielenie stringa do tablicy
                splitString[0] = splitString[0].toLowerCase();
                if(splitString.length <= 0) {
                    consoleManager.appendText("Lexer Error! W linii: " + i);
                }

                if (!validator.isValidMnemonic(splitString[0]) && !validator.isLabel(splitString[0])) {//jesli niepoprawny mnemonik lub nie jest to etykieta
                    consoleManager.appendText("Parser Error! Nieprawidłowy mnemonik \"" + splitString[0] + "\" w linii: " + i + " pliku preprocesora .pre_wlasm");
                    File preprocessorFile = new File(in.getPath());
                    tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));
//                    fileWorker.deleteFile();
                    return null;
                }

                if (mnemonicMap.getArgCount(splitString[0]) == -1) { //jakis blad
                    consoleManager.appendText("Parser Error! Błąd w linii: " + i);
                    fileWorker.deleteFile(new File(in.getPath()));
                    File preprocessorFile = new File(in.getPath());
                    tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));
                    return null;
                } else if (mnemonicMap.getArgCount(splitString[0]) == 0) { //kiedy ilosc arg to 0 typu clear
                    if(!whenArgsNumberIs0(splitString)) return null; //jeśli mnemonik bez argumentowy
                } else if (mnemonicMap.getArgCount(splitString[0]) == 1) { //kiedy ilosc arg to 1
                    if(!whenArgsNumberIs1(splitString, i, in)) return null;
                } else if (mnemonicMap.getArgCount(splitString[0]) == 2) { //kiedy ilosc arg to 2
                    if(!whenArgsNumberIs2(splitString, line, i, in)) return null;
                } else {
                    consoleManager.appendText("Parser Error! Bład w linii: " + i + " pliku preprocesora .pre_wlasm");
                    File preprocessorFile = new File(in.getPath());
                    tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));
                    return null;
                }
            }
        }
        return new ParsedObject(labelMap, mnemonicsList);
    }

    private boolean whenArgsNumberIs0(String [] splitString)
    {
        if(splitString == null) return false;
        mnemonic = new Mnemonic(splitString[0], "a", "0");
        mnemonicsList.add(mnemonic);
        return true;
    }

    private boolean whenArgsNumberIs1(String [] splitString, int counter, File in)
    {
        try {
            splitString[0] = splitString[0].toLowerCase();
            splitString[1] = splitString[1].toLowerCase();
        } catch (Exception e) {
            consoleManager.appendText("Parser Error! Brak argumentu w linii: " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));
            return false;
        }

        if (validator.isValidArgWhenOnlyOne(splitString[0], splitString[1]) || validator.isJump(splitString[0], splitString[1])) {
            //jesli poprawny argument mnemonika lub jesli to jump
            mnemonic = new Mnemonic(splitString[0], "a", splitString[1]);
            mnemonicsList.add(mnemonic);
        } else {
            consoleManager.appendText("Parser Error! Nieprawidłowy argument 1 \"" + splitString[1] + "\" w linii: " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));
            return false;
        }
        return true;
    }

    private boolean whenArgsNumberIs2(String [] splitString, String line, int counter, File in)
    {
        try {
            splitString[0] = splitString[0].toLowerCase();
            splitString[1] = splitString[1].toLowerCase();
            splitString[2] = splitString[2].toLowerCase();
        } catch (Exception e) {
            consoleManager.appendText("Parser Error! Brak argumentów w linii: " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));
            return false;
        }

        if (!validator.isValidArg1(splitString[1])) { //poprawny arg1?
            consoleManager.appendText("Parser Error! Nieprawidłowy argument 1 \"" + splitString[1] + "\" w linii: " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));

        } else if (!validator.isValidArg2(splitString[0], splitString[2])) { //poprawny arg2?
            consoleManager.appendText("Parser Error! Nieprawidłowy argument 2 \"" + splitString[2] + "\" w linii: " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));

        } else if (validator.isTheSameArgsAsRegisters(splitString[0], splitString[1], splitString[2])) { // jesli nie jest ten sam rejestr
            consoleManager.appendText("Parser Error! Próbujesz wykonać operacje na tym samym rejestrze w linii: " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));

        } else if (!validator.isSyntaxCorrect(line)) { //niepoprawna skladnia (przecinek miedzy argumentami)
            consoleManager.appendText("Parser Error! Niepoprawna składnia! Sprawdź przecinek w linii " + counter + " pliku preprocesora .pre_wlasm");
            File preprocessorFile = new File(in.getPath());
            tabAndTreeManager.addTabAndTreeItem(preprocessorFile.getName(), preprocessorFile.getPath(), fileWorker.openFile(preprocessorFile.getPath()));

        } else { //jesli wszystko dobrze
            mnemonic = new Mnemonic(splitString[0], splitString[1], splitString[2]);
            mnemonicsList.add(mnemonic);
            return true;
        }
        return false;
    }
}
