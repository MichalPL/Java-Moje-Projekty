package Program.Compiler.Generator;

import Program.Compiler.Maps.MnemonicMap;
import Program.Compiler.Maps.RegistryMap;
import Program.Compiler.Objects.Mnemonic;
import Program.Compiler.Objects.ParsedObject;
import Program.Compiler.Converter;
import Program.Compiler.Validator;
import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Workers.FileWorker;

import java.io.File;

/**
 * Created by Michal on 2017-02-07.
 */
public class Generator {
    private FileWorker fileWorker;
    private ConsoleManager consoleManager;
    private Validator validator;
    private Converter converter;
    private RegistryMap registryMap;
    private MnemonicMap mnemonicMap;

    public Generator(FileWorker fileWorker, ConsoleManager consoleManager, Validator validator,
                     Converter converter, RegistryMap registryMap, MnemonicMap mnemonicMap) {
        this.fileWorker = fileWorker;
        this.consoleManager = consoleManager;
        this.validator = validator;
        this.converter = converter;
        this.registryMap = registryMap;
        this.mnemonicMap = mnemonicMap;
    }

    public boolean run(ParsedObject parsedObject, File saveFile) {
        if(parsedObject.getMnemonicsList().isEmpty()) {
            consoleManager.appendText("Błąd generatora! Plik jest pusty!");
            return false;
        }
        fileWorker.createFile(saveFile);
        for (Mnemonic aList : parsedObject.getMnemonicsList()) {
            fileWorker.addToFile(saveFile, mnemonicMap.getBinary(aList.getName())); //zapis binarny mnemonika
            fileWorker.addToFile(saveFile, registryMap.getBinary(aList.getArg1())); //zapis binarny rejestru

            //zapis binarny rejestru lub liczby
            if (aList.getName().endsWith("x") || aList.getName().equals("inc") || aList.getName().equals("dec")) { //jesli mnemonik konczy sie na x lub jest to inc lub dec
                fileWorker.addToFile(saveFile, "00000" + registryMap.getBinary(aList.getArg2()) + "\n");

            } else if (!aList.getName().endsWith("x") && validator.isNumber(aList.getArg2())) { //jesli nie konczy sie na x to musi byc liczba
                fileWorker.addToFile(saveFile, converter.getString(converter.convertToU2(aList.getArg2())) + "\n");

            } else if(validator.isJump(aList.getName(), aList.getArg2())) { //jesli to jump
                if(parsedObject.getLabelMap().get(aList.getArg2()) == null) { //jesli etykiety
                    consoleManager.appendText("Blad! Sprawdz etykiety!");
//                    fileWorker.deleteFile(new File(saveFile.getPath()));
                    return false;
                }
                fileWorker.addToFile(saveFile, converter.getString(converter.convertToU2(parsedObject.getLabelMap().get(aList.getArg2()).toString())) + "\n" );

            } else {
                consoleManager.appendText("Blad podczas przetwarzania!");
//                fileWorker.deleteFile(new File(saveFile.getPath()));
                return false;
            }
        }
        return true;
    }

}
