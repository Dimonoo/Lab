import java.util.Map;
import java.util.Stack;

class StackMachine {
    private Stack<String> stack = new Stack<>();

    Map<String, Object[]> stackMachine(Parser parser) {
        Map<String, Object[]> tableOfVar = parser.tableOfVariables;
        boolean reInitType = true;
        String type = "int";
        int a, b, c;
        boolean d;
        MyHashSet e = new MyHashSet();

        System.out.println(parser.printTOV(tableOfVar));
        System.out.println();
        for (int i = 0; i < parser.token_polis.size(); i++) {
            System.out.println(parser.printTOV(tableOfVar));
            switch (parser.token_polis.get(i)) {
                case "=":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            tableOfVar.put(stack.pop(), parser.valueCreate("int", String.valueOf(a)));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "+":
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            c = b + a;
                            stack.push(String.valueOf(c));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "-":
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            c = b - a;
                            stack.push(String.valueOf(c));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "*":
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            c = b * a;
                            stack.push(String.valueOf(c));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "/":
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            c = b / a;
                            stack.push(String.valueOf(c));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case ">":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            d = b > a;
                            stack.push(String.valueOf(d));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case ">=":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            d = b >= a;
                            stack.push(String.valueOf(d));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "<":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            d = b < a;
                            stack.push(String.valueOf(d));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "<=":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            d = b <= a;
                            stack.push(String.valueOf(d));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "==":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            d = b == a;
                            stack.push(String.valueOf(d));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "!=":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            b = getInt(tableOfVar);
                            d = b != a;
                            stack.push(String.valueOf(d));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(545);
                    }
                    break;
                case "add":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            String var = stack.pop();
                            e = (MyHashSet) tableOfVar.get(var)[1];
                            e.add(a);
                            tableOfVar.put(var, parser.valueCreate("set", e));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(546);
                    }
                    break;
                case "remove":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            a = getInt(tableOfVar);
                            String var = stack.pop();
                            e = (MyHashSet) tableOfVar.get(var)[1];
                            e.remove(a);
                            tableOfVar.put(var, parser.valueCreate("set", e));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(546);
                    }
                    break;
                case "clear":
                    reInitType = true;
                    switch (type) {
                        case "int":
                            String var = stack.pop();
                            e = (MyHashSet) tableOfVar.get(var)[1];
                            e.clear();
                            tableOfVar.put(var, parser.valueCreate("set", e));
                            break;
                        default:
                            System.err.println("Error: wrong type");
                            System.exit(546);
                    }
                    break;
                case "!F":
                    a = getInt(tableOfVar) - 1;
                    i = stack.pop().equals("true") ? i : a;
                    break;
                case "!":
                    i = getInt(tableOfVar) - 1;
                    break;
                default:
                    String token = parser.token_polis.get(i);
                    Lexer lexer = new Lexer();

                    if (reInitType && lexer.recognize(token).get(0).getLexeme() == Lexeme.VAR) {
                        reInitType = false;
                        type = "int";
                    }
                    stack.push(token);
                    break;
            }
        }
        System.out.println();
        return tableOfVar;
    }

    private int getInt(Map<String, Object[]> tov) {
        final String TYPE = "int";
        Lexer lexer = new Lexer();

        switch (lexer.recognize(stack.peek()).get(0).getLexeme()) {
            case VAR:
                Object[] var = tov.get(stack.pop());
                if (var[0].equals(TYPE)) {
                    if (!var[1].equals("")) {
                        return Integer.valueOf((String) var[1]);
                    } else {
                        System.err.println("Error: Var hasn't value");
                        System.exit(310);
                    }
                } else {
                    System.err.println("Error: Type expected is \"" + TYPE + "\"");
                    System.exit(333);
                }
            case DIGIT:
                return Integer.valueOf(stack.pop());
            default:
                System.err.println("Error: Error of type in getInt");
                System.exit(10);
        }
        return -1;
    }
}