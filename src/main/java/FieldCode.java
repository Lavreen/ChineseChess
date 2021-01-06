/**
 * Class that imitate pair. It's used in field numbering
 */

public class FieldCode {
    private final char key;
    private final int value;

    public FieldCode(char key, int  value){
        this.key =  key;
        this.value = value;
    }

    public char getKey() {
        return key;
    }

    public int getValue(){
        return value;
    }
}
