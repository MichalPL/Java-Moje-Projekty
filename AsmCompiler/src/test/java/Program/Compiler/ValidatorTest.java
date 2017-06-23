package Program.Compiler;

import Program.Compiler.Maps.MnemonicMap;
import Program.Compiler.Maps.RegistryMap;
import Program.Compiler.Objects.MappedMnemonic;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Michal on 2017-02-10.
 */
public class ValidatorTest {

    private MnemonicMap mnemonicMap = new MnemonicMap();
    private RegistryMap registryMap = new RegistryMap();

    private Validator validator = new Validator(registryMap, mnemonicMap);

    @Test
    public void shouldReturnTrueForAllMappedMnemonicInMnemonicMap() throws Exception {
        for (MappedMnemonic mm: mnemonicMap.getMnemonicsMap()) {
            assertTrue(validator.isValidMnemonic(mm.getName()));
        }
    }

    @Test
    public void shouldReturnFalseForWrongMappedMnemonicNullString() throws Exception {
        assertFalse(validator.isValidMnemonic("aaa"));
        assertFalse(validator.isValidMnemonic(null));
        assertFalse(validator.isValidMnemonic(""));
    }

    @Test
    public void shouldReturnTrueForAllGoodRegisterNameInRegistryMap() throws Exception {
        for (Map.Entry<String, String> entry: registryMap.getMap().entrySet()) {
            assertTrue(validator.isValidArg1(entry.getKey())); //arg1 jest zawsze rejestrem
        }
    }

    @Test
    public void shouldReturnFalseForWrongRegisterNameOrNullOrEmpty() throws Exception {
        assertFalse(validator.isValidArg1("aaa"));
        assertFalse(validator.isValidArg1(null));
        assertFalse(validator.isValidArg1(""));
    }

    @Test
    public void shouldReturnTrueForMnemonicEndsWithXAndRegisterOrNotEndsWithXAndNumber() throws Exception {
        for (Map.Entry<String, String> entry: registryMap.getMap().entrySet()) {
            //jesli konczy sie na "x" i drugim arg jest rejestr
            if(entry.getKey().endsWith("x")) {
                for (Map.Entry<String, String> entry2: registryMap.getMap().entrySet()) {
                    assertTrue(validator.isValidArg2(entry.getKey(), entry2.getKey()));
                }
            }
            //jesli nie konczy sie na "x", ale drugim argumentem jest liczba
            else {
                assertTrue(validator.isValidArg2(entry.getKey(), "100"));
            }
        }
    }

    @Test
    public void shouldReturnFalseForMnemonicsNotEndsWithXAndNotNumberOrNullArgsOrEmptyStrings() throws Exception {
        for (Map.Entry<String, String> entry : registryMap.getMap().entrySet()) {
            //jesli konczy sie na "x" i drugim argumentem jest liczba
            if (entry.getKey().endsWith("x")) {
                assertFalse(validator.isValidArg2(entry.getKey(), "100"));
            }
            //jesli nie konczy sie na "x" i drugim arg jest rejestr
            else {
                for (Map.Entry<String, String> entry2 : registryMap.getMap().entrySet()) {
                    assertFalse(validator.isValidArg2(entry.getKey(), entry2.getKey()));
                }
            }
        }

        assertFalse(validator.isValidArg2(null, null));
        assertFalse(validator.isValidArg2("", ""));
    }

    @Test
    public void shouldReturnTrueForNegxOrIncOrDecWithRegistryOrNegOrMovxWithNumber() throws Exception {
        for (Map.Entry<String, String> entry: registryMap.getMap().entrySet()) {
            assertTrue(validator.isValidArgWhenOnlyOne("negx", entry.getKey()));
            assertTrue(validator.isValidArgWhenOnlyOne("inc", entry.getKey()));
            assertTrue(validator.isValidArgWhenOnlyOne("dec", entry.getKey()));
        }
        assertTrue(validator.isValidArgWhenOnlyOne("neg", "100"));
        assertTrue(validator.isValidArgWhenOnlyOne("movzx", "100"));
    }

    @Test
    public void shouldReturnFalseNullOrWrongNumberForIsValidArgWhenOnlyOne() throws Exception {
        for (Map.Entry<String, String> entry: registryMap.getMap().entrySet()) {
            assertFalse(validator.isValidArgWhenOnlyOne("aaa", entry.getKey()));
            assertFalse(validator.isValidArgWhenOnlyOne("neg", entry.getKey()));
            assertFalse(validator.isValidArgWhenOnlyOne("movzx", entry.getKey()));
        }
        assertFalse(validator.isValidArgWhenOnlyOne(null, null));
        assertFalse(validator.isValidArgWhenOnlyOne("", ""));
    }

    @Test
    public void shouldReturnTrueForJumpMnemonicAndNotNumberArg() throws Exception {
        assertTrue(validator.isJump("jmp", "aaa"));
        assertTrue(validator.isJump("jw", "aaa"));
        assertTrue(validator.isJump("jm", "aaa"));
        assertTrue(validator.isJump("jr", "aaa"));
    }

    @Test
    public void shouldReturnFalseForJumpMnemonicAndNumberArgOrNullOrEmpty() throws Exception {
        assertFalse(validator.isJump("jmp", "100"));
        assertFalse(validator.isJump("jw", "100"));
        assertFalse(validator.isJump("jm", "100"));
        assertFalse(validator.isJump("jr", "100"));
        assertFalse(validator.isJump(null, null));
        assertFalse(validator.isJump("", ""));
    }

    @Test
    public void returnTrueForTheSameArgsAsRegisters() throws Exception {
        assertTrue(validator.isTheSameArgsAsRegisters("addx", "aaa", "aaa"));
    }

    @Test
    public void returnFalseForDifferentRegistersOrNullOrEmptyOrNotEndWithX() throws Exception {
        assertFalse(validator.isTheSameArgsAsRegisters("addx", "aaa", "bbb"));
        assertFalse(validator.isTheSameArgsAsRegisters(null, null, null));
        assertFalse(validator.isTheSameArgsAsRegisters("", "", ""));
        assertFalse(validator.isTheSameArgsAsRegisters("add", "aaa", "aaa"));
        assertFalse(validator.isTheSameArgsAsRegisters("add", "aaa", "bbb"));
    }

    @Test
    public void shouldReturnTrueForNumbersFromMinus128To127() throws Exception {
        for (int i = -128; i < 127; i++) {
            assertTrue(validator.isNumber("" + i));
        }
    }

    @Test
    public void shouldReturnFalseForLesserThanMinus128AndBiggerThan127AndForNonIntegerNumber() throws Exception {
        assertFalse(validator.isNumber("-129"));
        assertFalse(validator.isNumber("128"));
        assertFalse(validator.isNumber("12.21"));
        assertFalse(validator.isNumber(""));
        assertFalse(validator.isNumber("aaaa"));
        assertFalse(validator.isNumber(null));
    }

//    if(line == null) return false;
//    String[] splitString = line.split("\\W+");
//        return (map.getArgCount(splitString[0].toLowerCase()) == 2 && line.contains(","));
    @Test
    public void shouldReturnTrueForGoodMnemonicAndIfLineContainsSeparator() throws Exception {
        assertTrue(validator.isSyntaxCorrect("add 12,34"));
        assertTrue(validator.isSyntaxCorrect("add aaa,34"));
    }

    @Test
    public void shouldReturnFalseForNullOrWrongData() throws Exception {
        assertFalse(validator.isSyntaxCorrect("aaa 12,34"));
        assertFalse(validator.isSyntaxCorrect("add aaa 34"));
        assertFalse(validator.isSyntaxCorrect(null));
        assertFalse(validator.isSyntaxCorrect(""));
        assertFalse(validator.isSyntaxCorrect("add r0"));
    }

    @Test
    public void shouldReturnTrueForTextWithGoodEnd() throws Exception {
        assertTrue(validator.isLabel("aaa:"));
    }

    @Test
    public void shouldReturnFalseForNonLabelTekst() throws Exception {
        assertFalse(validator.isLabel("aaa"));
    }

}