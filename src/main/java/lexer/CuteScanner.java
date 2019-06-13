package lexer;

import java.util.ArrayList;
import java.util.Optional;

public class CuteScanner {
    private ScanContext context = new ScanContext();

    public CuteScanner() {
    }

    public ArrayList<Token> scan(String line) {
        this.context.setInput(line);
        ArrayList<Token> token_list = new ArrayList<>();
        Optional<Token> next_token = generateToken(context);
        while(next_token.isPresent()) {
            token_list.add(next_token.get());
            next_token = generateToken(context);
        }
        return token_list;
    }

    private Optional<Token> generateToken(ScanContext context) {
        lexer.State current = lexer.State.START;
        while (true) {
            TransitionOutput output = current.transit(context);
            if (output.nextState() == lexer.State.MATCHED) {
                return output.token();
            } else if (output.nextState() == lexer.State.FAILED) {
                throw new ScannerException();
            } else if (output.nextState() == lexer.State.EOS) {
                return Optional.empty();
            }

            current = output.nextState();
        }
    }
}
