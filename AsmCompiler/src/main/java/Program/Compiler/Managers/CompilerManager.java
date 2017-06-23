package Program.Compiler.Managers;

import Program.Compiler.Generator.Generator;
import Program.Compiler.LexerAndParser.LexerAndParser;
import Program.Compiler.Objects.ParsedObject;
import Program.Compiler.Preprocessor.Preprocessor;
import Program.Compiler.Converter;
import Program.Compiler.Maps.MnemonicMap;
import Program.Compiler.Maps.RegistryMap;
import Program.Compiler.Validator;
import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Managers.TabAndTreeManager;
import Program.IDE.Workers.FileWorker;
import org.apache.commons.io.FilenameUtils;
import org.reactfx.util.LL;

import java.io.File;

/**
 * Created by Michal on 2016-03-17.
 */
public class CompilerManager {
    private Preprocessor preprocessor;
    private LexerAndParser lexerAndParser;
    private Generator generator;
    private ConsoleManager consoleManager;

    public CompilerManager(ConsoleManager consoleManager, FileWorker fileWorker, TabAndTreeManager tabAndTreeManager) {
        this.consoleManager = consoleManager;
        MnemonicMap mnemonicMap = new MnemonicMap();
        RegistryMap registryMap = new RegistryMap();
        Converter converter = new Converter();
        Validator validator = new Validator(registryMap, mnemonicMap);
        preprocessor = new Preprocessor(fileWorker, consoleManager);
        lexerAndParser = new LexerAndParser(fileWorker, validator, mnemonicMap, consoleManager, tabAndTreeManager);
        generator = new Generator(fileWorker, consoleManager, validator, converter, registryMap, mnemonicMap);
    }


    public boolean run(File startFile)
    {
        final String preprocessorOutput = startFile.getParent() + "\\" + FilenameUtils.getBaseName(startFile.getName()) + ".pre_wlasm";
        final String generetorOutput = startFile.getParent() + "\\" + FilenameUtils.getBaseName(startFile.getName()) + ".bin";
        consoleManager.appendText("Preprocessor pracuje...");
        if(!preprocessor.run(startFile, preprocessorOutput)) {
            return false;
        }
        consoleManager.appendText("Zakończono");
        consoleManager.appendText("Lexer i Parser pracuje...");
        ParsedObject parsedObject = lexerAndParser.run(new File(preprocessorOutput));
        if(parsedObject == null) {
            consoleManager.appendText("Przerwano! Błąd Lexera lub Parsera!");
            return false;
        }
        consoleManager.appendText("Zakończono");
        consoleManager.appendText("Generowanie kodu...");
        if(!generator.run(parsedObject, new File(generetorOutput))) {
            return false;
        }
        consoleManager.appendText("Zakończono");
        return true;
    }
}
