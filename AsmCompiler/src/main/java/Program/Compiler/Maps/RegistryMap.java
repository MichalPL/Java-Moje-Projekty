package Program.Compiler.Maps;

import java.util.HashMap;
import java.util.Map;

import static Program.IDE.StaticClasses.StaticVariable.REGISTERS;

/**
 * Created by Michal on 2016-03-18.
 */
public class RegistryMap {
    private Map<String, String> map;

    public RegistryMap() {
        map = new HashMap<String, String>();
        fill();
    }

    private void fill()
    {
        map.put(REGISTERS[0],   "000");
        map.put(REGISTERS[1],   "001");
        map.put(REGISTERS[2],   "010");
        map.put(REGISTERS[3],   "011");
        map.put(REGISTERS[4],   "100");
        map.put(REGISTERS[5],   "101");
        map.put(REGISTERS[6],   "110");
        map.put(REGISTERS[7],   "111");
    }

    public boolean isRegistry(String name) {
        return name != null && !name.isEmpty() && map.containsKey(name);
    }

    public String getBinary(String name)
    {
        if(name.isEmpty()) return "";
        return map.get(name);
    }

    public Map<String, String> getMap() {
        return map;
    }
}
