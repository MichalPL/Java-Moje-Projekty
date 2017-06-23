package Program.Compiler;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michal on 2017-02-17.
 */
public class ConverterTest {
    private Converter converter = new Converter();

    @Test
    public void shouldConvertToTwosComplementBinary() throws Exception {
        assertArrayEquals(converter.convertToU2("100"), new int[] {0,1,1,0,0,1,0,0});
        assertArrayEquals(converter.convertToU2("-100"), new int[] {1,0,0,1,1,1,0,0});
        assertArrayEquals(converter.convertToU2("0"), new int[] {0,0,0,0,0,0,0,0});
    }

    @Test
    public void shouldReturnStringFromArray() throws Exception {
        assertEquals(converter.getString(new int[] {0,1,1,0,0,1,0,0}), "01100100");
    }

}