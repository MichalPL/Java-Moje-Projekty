package Program.Compiler.Objects;

/**
 * Created by Michal on 2017-02-07.
 */
public class MappedMnemonic {
    private String name;
    private String binaryCode;
    private int argCount;

    public MappedMnemonic(String name, String binaryCode, int argCount) {
        this.name = name;
        this.binaryCode = binaryCode;
        this.argCount = argCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBinaryCode() {
        return binaryCode;
    }

    public void setBinaryCode(String binaryCode) {
        this.binaryCode = binaryCode;
    }

    public int getArgCount() {
        return argCount;
    }

    public void setArgCount(int argCount) {
        this.argCount = argCount;
    }
}
