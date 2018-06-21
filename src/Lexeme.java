import java.util.regex.Pattern;

public enum Lexeme {
    TYPE(Pattern.compile("^int|set$")),
    ADD(Pattern.compile("^add$")),
    REMOVE(Pattern.compile("^remove$")),
    CLEAR(Pattern.compile("^clear$")),
    FOR(Pattern.compile("^for$")),
    VAR(Pattern.compile("^[a-zA-Z]+$")),
    ASSIGN_OP(Pattern.compile("^=$")),//+
    DIGIT(Pattern.compile("^0|[1-9][0-9]*")),
    ARITHMETIC_OP(Pattern.compile("^\\+|-|\\*|/$")),
    LOGIC_OP(Pattern.compile("^<|>|<=|>=|!=|==$")),
    WS(Pattern.compile("^\\s+$")),
    L_F_B(Pattern.compile("^\\{$")),
    R_F_B(Pattern.compile("^}$")),
    L_R_B(Pattern.compile("^\\($")),
    R_R_B(Pattern.compile("^\\)$")),
    SEMICOLON(Pattern.compile("^;$"));

    Pattern pattern;

    Lexeme(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
