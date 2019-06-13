package lexer;

import java.io.*;

public class CharStream {
    private Reader reader;
    private Character cache;
    private int paren_pair;
    private int double_quote_pair;

    CharStream() {
        this.cache = null;
        this.paren_pair = 0;
        this.double_quote_pair = 0;
    }

    public void setReader(Serializable line) {
        if (line instanceof String)
            this.reader = new StringReader((String) line);
        else if (line instanceof File) {
            try {
                this.reader = new FileReader((File) line);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    Char nextChar() {
        if (cache != null) {
            char ch = cache;
            cache = null;

            return Char.of(ch);
        } else {
            try {
                int ch = reader.read();
                switch (ch) {
                    case -1:
                        if (paren_pair != 0 || double_quote_pair % 2 != 0) {
                            return Char.of((char) 10);
                        } else
                            return Char.end();
                    case '(':
                        if (double_quote_pair % 2 == 0)
                            paren_pair++;
                        return Char.of((char) ch);
                    case ')':
                        if (double_quote_pair % 2 == 0)
                            paren_pair--;
                        return Char.of((char) ch);
                    case '"':
                        double_quote_pair++;
                        return Char.of((char) ch);
                    case 10:
                        return Char.of((char) ch);
                    case 45:
                        return nextChar();
                    default:
                        return Char.of((char) ch);
                }
            } catch (IOException e) {
                throw new ScannerException("" + e);
            }
        }
    }

    void pushBack(char ch) {
        cache = ch;
    }
}
