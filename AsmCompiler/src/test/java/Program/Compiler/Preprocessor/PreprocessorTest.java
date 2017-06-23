package Program.Compiler.Preprocessor;

import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Workers.FileWorker;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Michal on 2017-02-19.
 */
public class PreprocessorTest {
//    public boolean run(File in, String outPath) {
//        List<String> listOfContentOfIncludedFiles = getListOfStringOfFiles(in);
//        if(listOfContentOfIncludedFiles == null) {
//            return false;
//        }
//        fileWorker.saveFile(outPath, merge(listOfContentOfIncludedFiles));
//        return true;
//    }

    private FileWorker fileWorker = mock(FileWorker.class);
    private ConsoleManager consoleManager = mock(ConsoleManager.class);

    private Preprocessor preprocessor = new Preprocessor(fileWorker, consoleManager);

    private String tempDir = System.getProperty("java.io.tmpdir");

    @Test
    public void run() throws Exception {

    }

}