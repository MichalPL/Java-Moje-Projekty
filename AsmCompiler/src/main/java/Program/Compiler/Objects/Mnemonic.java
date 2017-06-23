package Program.Compiler.Objects;

/**
 * Created by Michal on 2016-03-17.
 */
public class Mnemonic {
    private String name;
    private String arg1;
    private String arg2;

    public Mnemonic(String name, String arg1, String arg2) {
        this.name = name;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getName()
    {
        return name;
    }

    public String getArg1()
    {
        return arg1;
    }

    public String getArg2()
    {
        return arg2;
    }
}
