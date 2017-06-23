package Program.IDE.StaticClasses;

import Program.IDE.Objects.TreeObject;

import java.util.Vector;

/**
 * Created by Michal on 2017-02-06.
 */
public class StaticVariable {
    public static Vector<TreeObject> editors = new Vector<>();
    public static int lineCounter = 0;

    public static String projectPath = "";

    public static final String[] MNEMONICS = new String[] {
            "add", "addx", "sub", "subx", "mul", "mulx",
            "mov", "movx", "inc", "dec", "cmp", "cmpx",
            "or", "orx", "and", "andx", "neg", "negx",
            "jmp", "nop", "movx", "xor", "xorx", "clr",
            "jm", "jw", "jr", "swap"
    };

    public static final String[] REGISTERS = new String[] {
            "a", "b", "x", "r0", "r1", "r2", "r3", "r4"
    };

    public static final String[] DIRECTIVE = new String[] {
            "include"
    };
}
