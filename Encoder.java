import java.util.Random;
public class Encoder extends memsii{
    private int getDecimalValue(char a){
        int i = 0; // Index Variable,used in loop for linear search to get the index of the required character
        while(i<=((charArray.length)-1)){//linear search which searches for the required character and uses the sime index to return the MEMSII Decoded code.If not found returns -1
            if(a == charArray[i]){
                return encodedArray[i];
            }
            else i += 1;
        }
        return -1;
    }

    private String complementBinary(String string){
        str = string.toCharArray();
        for(int i = 0; i < str.length; i++){
            str[i] = (Character.getNumericValue(str[i]) == 0)?('1'):('0');
        }
        return new String(str);
    }
    private static String generateRandomString(int length) {String characterSet = "01";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();
        int index;
        for (int i = 0; i < length; i++) {
            index = random.nextInt(characterSet.length());
            result.append(characterSet.charAt(index));
        }

        return result.toString();
    }
    private String getBinaryValue(int a){
        String binValue = "";
        while(a > 1){
            binValue = a%2 + binValue;
            a /= 2;
        }
        binValue = a + binValue;
        return binValue;
    }
    private String getEncodedBinaryValue(String a){
        return (a+"00");
    }

    private String getStringEncodedFinal(String a){
        return (generateRandomString(5)+a+generateRandomString(7));
    }

    private String getEncodedCharacter(char character){
        String encodedValue = getEncodedBinaryValue(getBinaryValue(getDecimalValue(character)));
        int zeros = 10-(encodedValue.length());
        for(int i = 0;i<=zeros-1;i++)
            encodedValue = "0" + encodedValue;

        return encodedValue.trim();
    }

    public String getEncoded(String string){//returns complete string as encoded binary value
        String finalString = "";
        for(int i = 0;i<=string.length() - 1;i++) {
            finalString = finalString + getEncodedCharacter(string.charAt(i));

        }
        finalString = complementBinary(getStringEncodedFinal(finalString));
        return finalString;

    }
    public static void main(String[] args){
        Encoder e = new Encoder();
        System.out.println(e.getEncoded("Amritesh"));//Input data to be encoded
    }
}



