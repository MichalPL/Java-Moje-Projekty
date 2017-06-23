package Program.Compiler.Preprocessor;

import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Workers.FileWorker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Program.IDE.StaticClasses.Utils.*;

/**
 * Created by Michal on 2017-02-06.
 *
 * przetworzenie kodu źródłowego za pomocą dyrektyw preprocesora, na kod wyjściowy
 * dołączane są pliki z dyrektywy include
 */
public class Preprocessor {

    private FileWorker fileWorker;
    private ConsoleManager consoleManager;

    public Preprocessor(FileWorker fileWorker, ConsoleManager consoleManager) {
        this.fileWorker = fileWorker;
        this.consoleManager = consoleManager;
    }

    public boolean run(File in, String outPath) {
        List<String> listOfContentOfIncludedFiles = getListOfStringOfFiles(in);
        if(listOfContentOfIncludedFiles == null) {
            return false;
        }
        fileWorker.saveFile(outPath, merge(listOfContentOfIncludedFiles));
        return true;
    }

    private List<String> getListOfStringOfFiles(File in) {
        List<String> includedCodesList = new ArrayList<>();
        int lines = fileWorker.getLinesCount(in);
        String code = "";

        for (int i = 1; i <= lines; i++) {
            String line = fileWorker.getLine(i, in).replaceAll("^\\s+", "");
            String directive = getFistWord(line);
            switch (directive) {
                case "include":
                    //dodanie bloku kodu pomiedzy includami
                    if(!code.isEmpty()) {
                        includedCodesList.add(code);
                        code = "";
                    }
                    String openedFileContent = fileWorker.openFile(in.getParent() + "\\" + getIncludedFilenameFromLine(line));
                    if(openedFileContent == null) {
                        consoleManager.appendText("Błąd preprocessora! Dołączony plik "
                                + in.getParent() + "\\" + getIncludedFilenameFromLine(line) + "" +
                                "w linii " + i + " nie istnieje!");
                        return null;
                    }
                    includedCodesList.add(openedFileContent);
                    break;
                default:
                    code += line + "\n";
                    break;
            }
        }

        if(!code.isEmpty()) {
            includedCodesList.add(code);
        }

        return includedCodesList;
    }

    private String getIncludedFilenameFromLine(String line) {
        if(line.endsWith(".wlasm"))
            return line.replaceAll("^(include) ", "");
        else
            return line.replaceAll("^(include) ", "") + ".wlasm";
    }
}
