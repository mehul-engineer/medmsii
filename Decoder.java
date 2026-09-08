public class Decoder extends memsii{
    private char getDecodedToCharacter(String string){
        int sum = 0;
        char[] str = string.toCharArray();
        char[] str2 = new char[8];
        for(int i = 0;i<= 7;i++){
            str2[i] = str[str.length-1-i];
        }
        str = str2;
        for(int i = 0 ; i < str.length ; i++){
            sum += ((int)(Math.pow(2,i)))*Character.getNumericValue(str[i]);
        }
        int i = 0;
        while(i<=((charArray.length)-1)){//linear search which searches for the required MEDMSII and uses the sime index to return the Character.
            if(sum == encodedArray[i]){
                break;
            }
            else i += 1;
        }
        return charArray[i];
    }

    private String breakEncryptFn(String string){
        return string.substring(0, string.length() - 2);
    }
    private String[] breakToSingleCharacter(String string){

        return new String[]{string.substring(0,10),string.substring(10)};
    }
    private String firstStage(String string){
        str = string.toCharArray();
        for(int i = 0; i < str.length; i++){
            str[i] = (Character.getNumericValue(str[i]) == 0)?('1'):('0');
        }
        string = new String(str);
        string = string.substring(5);
        return (string).substring(0, string.length() - 7);
    }

    public String getDecoded(String string){
        String finalString = "";
        string = firstStage(string);
        while(!string.isEmpty()){

            finalString += getDecodedToCharacter(breakEncryptFn((breakToSingleCharacter(string))[0]));
            string = (breakToSingleCharacter(string))[1];
        }
        return finalString;
    }

    public static void main(String[] args) {
        Decoder e = new Decoder();
        System.out.println(e.getDecoded("10011111010101110010110111000110011100111101110001000111010011011100010101110100000110101000"));
    }}