import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

class Lexer {
    private String accumulator = "";
    private int position = 0;
    private Lexeme currentLexeme = null;
    private boolean waitForSuccess = true;

    List<Token> recognize(String str) {
        List<Token> tokens = new ArrayList<>();

        if (str.length() != 0) {
            while (position < str.length()) {
                accumulator += str.charAt(position++);
                boolean found = find();
                if (!found) {
                    if (!waitForSuccess) {
                        waitForSuccess = true;
                        Token token = new Token(currentLexeme, back(accumulator));
                        tokens.add(token);
                        accumulator = "";
                        position--;
                    } else {
                        waitForSuccess = true;
                        System.err.println('\n' + "Can't recognize input '" + accumulator + "' at position:" + position + "!");
                        System.exit(2);
                    }
                } else {
                    waitForSuccess = false;
                }
            }
            tokens.add(new Token(currentLexeme, accumulator));
        }else {
            System.err.println('\n' + "Error: Null input!");
            System.exit(1);
        }
        return tokens;
    }

    private boolean find() {
        for (Lexeme lexeme : Lexeme.values()) {
            Matcher matcher = lexeme.getPattern().matcher(accumulator);
            if (matcher.matches()) {
                currentLexeme = lexeme;
                return true;
            }
        }
        return false;
    }

    private String back(String str) {
        return str.substring(0, str.length() - 1);
    }
}