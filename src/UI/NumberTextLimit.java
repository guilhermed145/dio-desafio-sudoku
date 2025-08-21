package UI;

import java.util.List;
import static java.util.Objects.isNull;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumberTextLimit extends PlainDocument {

    private final List<String> NUMBER_LIST = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (isNull(str) || (!NUMBER_LIST.contains(str))) {
            return;
        }

        if (getLength() + str.length() <= 1) {
            super.insertString(offs, str, a);
        }
    }    

}
