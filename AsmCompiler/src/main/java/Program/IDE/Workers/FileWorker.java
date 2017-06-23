package Program.IDE.Workers;

import Program.IDE.Managers.ConsoleManager;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import static org.apache.commons.io.FilenameUtils.equalsOnSystem;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * Created by Michal on 2017-02-06.
 */
public class FileWorker {
    private BufferedReader reader = null;
    private ConsoleManager consoleManager;

    public FileWorker(ConsoleManager consoleManager) {
        this.consoleManager = consoleManager;
    }

    public String openFile(String path) {
        File file = new File(path);
        StringBuilder sb = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (IOException e) {
            consoleManager.appendText("Błąd pliku: Nie udało się otworzyć pliku!");
            return null;
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                consoleManager.appendText("Błąd pliku: Nie udało się zamknąć pliku!");
            }
        }
        if(sb == null || sb.toString().isEmpty()) return null;
        return sb.toString();
    }

    public void saveFile(String path, String content) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(path);
            out.print(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (Exception e) {
                consoleManager.appendText("Błąd pliku: Nie udało się zapisać pliku!");
            }
        }
    }

    public String getLine(int lineNumber, File file)
    {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for (int i = 0; i < lineNumber - 1; i++)
                reader.readLine();
            String line = reader.readLine();
            reader.close();
            return line;
        } catch (IOException e) {
            consoleManager.appendText("Błąd pliku: Nie udało się przeczytać pliku!");
        }
        return "";
    }

    public int getLinesCount(File file)
    {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            consoleManager.appendText("Błąd pliku: Nie udało się przeczytać pliku!");
        }
        return -1;
    }

    public void addToFile(File file, String s)
    {
        try {
            Files.write(Paths.get(file.getPath()), s.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
            consoleManager.appendText("Błąd pliku: Nie udało się zapisać pliku!");
        }
    }

    public void createFile(File file)
    {
        try {
            deleteFile(file);
            File f = new File(file.getPath());
            f.createNewFile();
        } catch (IOException e) {
            consoleManager.appendText("Błąd pliku: Nie udało się utworzyć pliku!");
        }
    }

    public void copyFile(File in, File out)
    {
        try {
            Files.copy(in.toPath(), out.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            consoleManager.appendText("Błąd pliku: Nie udało się skopiować pliku!");
        }
    }

    public void createDir(File file) {
        if(!file.exists())
            file.mkdir();
    }

    public void moveFile(File in, File out)
    {
        try {
            Files.move(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            consoleManager.appendText("Błąd pliku: Nie udało się przenieść pliku!");
        }
    }

    public void deleteFile(File file)
    {
        try {
            if(Files.exists(Paths.get(file.getPath())))
                Files.delete(Paths.get(file.getPath()));
        } catch (IOException e) {
            consoleManager.appendText("Błąd pliku: Nie udało się usunąć pliku!");
        }
    }
}
