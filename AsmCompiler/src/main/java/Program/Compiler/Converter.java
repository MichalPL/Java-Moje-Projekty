package Program.Compiler;

/**
 * Created by Michal on 2016-03-18.
 */
public class Converter {

    private int[] convertToBinary(int num) {
        int[] binary = new int[8];
        int index = binary.length - 1;
        for (int i = 0; i < binary.length; i++) {
                binary[i] = 0;
        }
        while(num > 0){
            binary[index--] = num%2;
            num = num/2;
        }
        return binary;
    }

    private int[] negate(int[] binNumber) {
        int[] negateBinary = new int[binNumber.length];
        for (int i = 0; i < negateBinary.length; i++) {
            if(binNumber[i] == 0) negateBinary[i] = 1;
            else negateBinary[i] = 0;
        }
        return negateBinary;
    }

    public int[] convertToU2(String number) {
        int num = Integer.parseInt(number);
        if(num == 0) {
            return new int[] {0,0,0,0,0,0,0,0};
        } else if(num < 0){
            num = num*-1;
            return negate(convertToBinary(num - 1));
        } else {
            return convertToBinary(num);
        }
    }

    public String getString(int[] array) {
        String binaryString = "";
        for (int item : array) {
            binaryString += item;
        }
        return binaryString;
    }
}
