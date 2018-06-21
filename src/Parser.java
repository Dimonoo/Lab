import java.util.*;

class Parser {
    Map<String, Object[]> tableOfVariables = new HashMap<>();// var, [type, value]
    List<String> token_polis = new ArrayList<>();
    private Stack<String> stack = new Stack<>();
    private List<Token> tokens = new ArrayList<>();
    private int position = 0;
    private int p1;
    private int p2;

    boolean lang(List<Token> tokens) {
        boolean lang = false;
        int majorTokens = 1;

        for (Token token : tokens) {
            if (token.getLexeme() != Lexeme.WS) {
                this.tokens.add(token);
            }
        }
        while (this.tokens.size() != position) {
            if (!expression()) {
                System.err.println("Error: ErrorSyntax in majorToken: " + majorTokens);
                System.exit(4);
            } else {
                majorTokens++;
                lang = true;
            }
        }
        return lang;
    }

    private boolean expression() {
        return init() || assign() || setAssign() || forModule();
    }

    private boolean assign() {
        boolean assign = false;
        int old_position = position;

        if (assignOperation()) {
            if (getCurrentTokenLexemeInc() == Lexeme.SEMICOLON) {
                assign = true;
            }
        }
        position = assign ? position : old_position;
        return assign;
    }

    private boolean assignOperation() {
        boolean assignOperation = false;
        int old_position = position;
        boolean add = false;
        String op, var;

        if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
            add = token_polis.add(getLastTokenValue());
            var = getLastTokenValue();

            if (getCurrentTokenLexemeInc() == Lexeme.ASSIGN_OP) {
                if (!tableOfVariables.get(var)[0].equals("set")) {
                    op = getLastTokenValue();
                    if (value()) {
                        while (!stack.empty()) {
                            token_polis.add(stack.pop());
                        }
                        token_polis.add(op);
                        assignOperation = true;
                    }
                } else {
                    System.err.println("Error: Try to assign set variable");
                    System.exit(321);
                }
            }
        }
        if (!assignOperation) {
            position = old_position;
            if (add) {
                token_polis.remove(token_polis.size() - 1);
            }
        }
        return assignOperation;
    }

    private boolean init() {
        boolean init = false;
        int old_position = position;
        String type, var;

        if (getCurrentTokenLexemeInc() == Lexeme.TYPE) {
            type = getLastTokenValue();
            if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
                var = getLastTokenValue();
                if (getCurrentTokenLexemeInc() == Lexeme.SEMICOLON) {
                    if (type.equals("set")) {
                        tableOfVariables.put(var, valueCreate(type, new MyHashSet()));
                    } else {
                        tableOfVariables.put(var, valueCreate(type, ""));
                    }
                    init = true;
                }
            }
        }
        position = init ? position : old_position;
        return init;
    }

    private boolean value() {
        if (val()) {
            while (valueOperation()) {
            }
            return true;
        }
        return false;
    }

    private boolean valueOperation() {
        boolean valueOperation = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Lexeme.ARITHMETIC_OP) {
            String arithmeticOP = getLastTokenValue();
            if (!stack.empty()) {
                while (getPriority(arithmeticOP) <= getPriority(stack.peek())) {
                    token_polis.add(stack.pop());
                    if (stack.empty()) {
                        break;
                    }
                }
            }
            stack.push(arithmeticOP);
            if (val()) {
                valueOperation = true;
            }
        }
        position = valueOperation ? position : old_position;
        return valueOperation;
    }

    private boolean val() {
        if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
            if (!tableOfVariables.containsKey(getLastTokenValue())) {
                System.err.println("Error: Variety " + getLastTokenValue() + " not initialize");
                System.exit(6);
            }
            token_polis.add(getLastTokenValue());
            return true;
        } else {
            position--;
        }
        if (getCurrentTokenLexemeInc() == Lexeme.DIGIT) {
            token_polis.add(getLastTokenValue());
            return true;
        } else {
            position--;
        }
        return breakValue();
    }

    private boolean breakValue() {
        boolean breakValue = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Lexeme.L_R_B) {
            stack.push(getLastTokenValue());
            if (value()) {
                if (getCurrentTokenLexemeInc() == Lexeme.R_R_B) {
                    while (!stack.peek().equals("(")) {
                        token_polis.add(stack.pop());
                    }
                    stack.pop();
                    breakValue = true;
                }
            }
        }
        position = breakValue ? position : old_position;
        return breakValue;
    }

    private boolean setAssign() {
        return setClear() || setAdd() || setRemove();
    }

    private boolean forModule() {
        boolean forModule = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Lexeme.FOR) {
            if (exprFor()) {
                if (body()) {
                    forModule = true;
                    token_polis.set(p1, String.valueOf(token_polis.size() + 2));
                    token_polis.add(String.valueOf(p2));
                    token_polis.add("!");
                }
            }
        }
        position = forModule ? position : old_position;
        return forModule;
    }

    private boolean exprFor() {
        boolean exprFor = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Lexeme.L_R_B) {
            if (assign()) {
                if (logExprFor()) {
                    if (assignOperation()) {
                        if (getCurrentTokenLexemeInc() == Lexeme.R_R_B) {
                            exprFor = true;
                        }
                    }
                }
            }
        }
        position = exprFor ? position : old_position;
        return exprFor;
    }

    private boolean logExprFor() {
        boolean logExprFor = false;
        int old_position = position;

        p2 = token_polis.size();
        if (logExpr()) {
            if (getCurrentTokenLexemeInc() == Lexeme.SEMICOLON) {
                logExprFor = true;
                p1 = token_polis.size();
                token_polis.add("p1");
                token_polis.add("!F");
            }
        }
        position = logExprFor ? position : old_position;
        return logExprFor;
    }

    private boolean body() {
        boolean body = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == Lexeme.L_F_B) {
            while (init() || assign() || setAssign()) {
            }
            if (getCurrentTokenLexemeInc() == Lexeme.R_F_B) {
                body = true;
            }
        }
        position = body ? position : old_position;
        return body;
    }

    private boolean logExpr() {
        boolean logExpr = false;
        int old_position = position;
        String op = "";
        ArrayList<String> stackZip = new ArrayList<>();

        if (assignOperation() || value()) {
            if (getCurrentTokenLexemeInc() == Lexeme.LOGIC_OP) {
                op = getLastTokenValue();
                while (!stack.empty()) {
                    String pop = stack.pop();
                    token_polis.add(pop);
                    stackZip.add(pop);
                }
                if (assignOperation() || value()) {
                    while (!stack.empty()) {
                        token_polis.add(stack.pop());
                    }
                    logExpr = true;
                    token_polis.add(op);
                    stackZip.clear();
                }
            }
        }
        if (!logExpr) {
            position = old_position;
            if (op.length() != 0) {
                for (int i = stackZip.size() - 1; i >= 0; i--) {
                    stack.push(stackZip.get(i));
                    token_polis.remove(token_polis.size() - 1);
                }
                stackZip.clear();
            }
        }
        return logExpr;
    }

    private boolean setAdd() {
        boolean setAdd = false;
        int old_position = position;
        boolean add = false;
        String op, var;

        if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
            add = token_polis.add(getLastTokenValue());
            var = getLastTokenValue();

            if (getCurrentTokenLexemeInc() == Lexeme.ADD) {
                if (tableOfVariables.get(var)[0].equals("set")) {
                    op = getLastTokenValue();
                    if (value()) {
                        if (getCurrentTokenLexemeInc() == Lexeme.SEMICOLON) {
                            while (!stack.empty()) {
                                token_polis.add(stack.pop());
                            }
                            token_polis.add(op);
                            setAdd = true;
                        }
                    }
                } else {
                    System.err.println("Error: Try to add to not Set variable");
                    System.exit(301);
                }
            }
        }
        if (!setAdd) {
            position = old_position;
            if (add) {
                token_polis.remove(token_polis.size() - 1);
            }
        }
        return setAdd;
    }

    private boolean setRemove() {
        boolean setRemove = false;
        int old_position = position;
        boolean add = false;
        String op, var;

        if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
            add = token_polis.add(getLastTokenValue());
            var = getLastTokenValue();

            if (getCurrentTokenLexemeInc() == Lexeme.REMOVE) {
                if (tableOfVariables.get(var)[0].equals("set")) {
                    op = getLastTokenValue();
                    if (value()) {
                        if (getCurrentTokenLexemeInc() == Lexeme.SEMICOLON) {
                            while (!stack.empty()) {
                                token_polis.add(stack.pop());
                            }
                            token_polis.add(op);
                            setRemove = true;
                        }
                    }
                } else {
                    System.err.println("Error: Try to remove from not Set variable");
                    System.exit(301);
                }
            }
        }
        if (!setRemove) {
            position = old_position;
            if (add) {
                token_polis.remove(token_polis.size() - 1);
            }
        }
        return setRemove;
    }

    private boolean setClear() {
        boolean setClear = false;
        int old_position = position;
        boolean add = false;
        String op, var;

        if (getCurrentTokenLexemeInc() == Lexeme.VAR) {
            add = token_polis.add(getLastTokenValue());
            var = getLastTokenValue();

            if (getCurrentTokenLexemeInc() == Lexeme.CLEAR) {
                if (tableOfVariables.get(var)[0].equals("set")) {
                    op = getLastTokenValue();
                    if (getCurrentTokenLexemeInc() == Lexeme.SEMICOLON) {
                        if (!stack.empty()) {
                            System.out.println("Stack is not empty");
                        }
                        while (!stack.empty()) {
                            token_polis.add(stack.pop());
                        }
                        token_polis.add(op);
                        setClear = true;
                    }
                } else {
                    System.err.println("Error: Try to clear not Set variable");
                    System.exit(300);
                }
            }
        }
        if (!setClear) {
            position = old_position;
            if (add) {
                token_polis.remove(token_polis.size() - 1);
            }
        }
        return setClear;
    }

    private Lexeme getCurrentTokenLexemeInc() {
        try {
            return tokens.get(position++).getLexeme();
        } catch (IndexOutOfBoundsException ex) {
            position--;
            System.err.println("Error: Lexeme \"" + tokens.get(--position).getLexeme() + "\" expected");
            System.exit(3);
        }
        return null;
    }

    private String getLastTokenValue() {
        return tokens.get(position - 1).getValue();
    }

    Object[] valueCreate(String type, Object value) {
        Object[] ret = new Object[2];

        ret[0] = type;
        ret[1] = value;
        return ret;
    }

    String printTOV(Map<String, Object[]> tov) {
        StringBuilder s = new StringBuilder();
        Set<String> keys = tov.keySet();
        String[] keyss = keys.toArray(new String[0]);

        s.append("[");
        for (int i = 0; i < keyss.length - 1; i++) {
            Object[] values = tov.get(keyss[i]);
            String value0 = values[0].toString();
            String value1 = values[1].toString();


            s.append(keyss[i]);
            s.append(", [");
            s.append(value0);
            s.append(", ");
            if (value1.equals("")) {
                s.append("null");
            } else {
                s.append(value1);
            }
            s.append("], ");
        }
        String value0 = tov.get(keyss[keyss.length - 1])[0].toString();
        String value1 = tov.get(keyss[keyss.length - 1])[1].toString();

        s.append(keyss[keyss.length - 1]);
        s.append(", [");
        s.append(value0);
        s.append(", ");
        if (value1.equals("")) {
            s.append("null");
        } else {
            s.append(value1);
        }
        s.append("]");
        s.append("]");
        return s.toString();
    }

    private int getPriority(String str) {
        switch (str) {
            case "+":
                return 1;
            case "*":
                return 2;
            case "-":
                return 1;
            case "/":
                return 2;
            case "(":
                return 0;
            default:
                System.err.println("Error: In symbol " + str);
                System.exit(5);
                return 0;
        }
    }
}