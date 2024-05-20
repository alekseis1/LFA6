import java.util.*;
import java.util.regex.Pattern;

class Lexer {
    private String equation;

    private static final Map<TokenType, List<String>> data = Map.of(
            TokenType.OPEN_PARENTHESIS, Arrays.asList("\\(", "\\["),
            TokenType.CLOSE_PARENTHESIS, Arrays.asList("\\)", "\\]"),
            TokenType.MATH_OPERATION, Arrays.asList("[+\\-*/%^]"),
            TokenType.NUMBERS, Arrays.asList("\\d+")
    );

    private static final Map<TokenType, List<TokenType>> transitions = Map.of(
            TokenType.OPEN_PARENTHESIS, Arrays.asList(TokenType.NUMBERS, TokenType.OPEN_PARENTHESIS),
            TokenType.MATH_OPERATION, Arrays.asList(TokenType.NUMBERS, TokenType.OPEN_PARENTHESIS),
            TokenType.CLOSE_PARENTHESIS, Arrays.asList(TokenType.MATH_OPERATION, TokenType.CLOSE_PARENTHESIS),
            TokenType.NUMBERS, Arrays.asList(TokenType.NUMBERS, TokenType.CLOSE_PARENTHESIS, TokenType.MATH_OPERATION),
            TokenType.START, Arrays.asList(TokenType.OPEN_PARENTHESIS, TokenType.NUMBERS)
    );

    public Lexer(String equation) {
        this.equation = equation.replace(" ", "");
    }

    public boolean lexer() {
        Stack<String> seqParenthesis = new Stack<>();
        List<TokenType> categoryMapping = new ArrayList<>(Collections.singletonList(TokenType.START));
        String failedOn = "";
        List<String> validTokens = new ArrayList<>();

        for (char symbol : equation.toCharArray()) {
            String symStr = String.valueOf(symbol);

            // Parenthesis handling
            if (data.get(TokenType.OPEN_PARENTHESIS).contains(symStr)) {
                seqParenthesis.push(symStr);
            } else if (data.get(TokenType.CLOSE_PARENTHESIS).contains(symStr)) {
                if (seqParenthesis.isEmpty()) {
                    System.out.println("ERROR: Extra closing parenthesis found.");
                    System.out.println("Failed on symbol " + failedOn);
                    return false;
                } else {
                    String lastOpen = seqParenthesis.pop();
                    if ((symStr.equals(")") && !lastOpen.equals("(")) || (symStr.equals("]") && !lastOpen.equals("["))) {
                        System.out.println("ERROR: Mismatched closing parenthesis found.");
                        System.out.println("Failed on symbol " + failedOn);
                        return false;
                    }
                }
            }

            // Token categorization using regular expressions
            TokenType currentCategory = null;
            boolean foundCategory = false;
            for (Map.Entry<TokenType, List<String>> entry : data.entrySet()) {
                TokenType category = entry.getKey();
                for (String pattern : entry.getValue()) {
                    if (Pattern.matches(pattern, symStr)) {
                        currentCategory = category;
                        foundCategory = true;
                        break;
                    }
                }
                if (foundCategory) break;
            }

            if (!foundCategory) {
                System.out.println("ERROR: Symbol '" + symbol + "' does not belong to any known category.");
                System.out.println("Failed on symbol " + failedOn);
                return false;
            }

            // Transition checking
            if (!transitions.get(categoryMapping.get(categoryMapping.size() - 1)).contains(currentCategory)) {
                System.out.println("ERROR: Transition not allowed from '" + categoryMapping.get(categoryMapping.size() - 1) + "' to '" + currentCategory + "'.");
                System.out.println("Failed on symbol " + failedOn);
                return false;
            }

            // Update mappings and tokens
            categoryMapping.add(currentCategory);
            validTokens.add(symStr);
            failedOn += symStr;
        }

        // Check for remaining open parentheses
        if (!seqParenthesis.isEmpty()) {
            System.out.println("ERROR: Not all parentheses were closed.");
            System.out.println("Failed on symbol " + failedOn);
            return false;
        }

        System.out.println(categoryMapping);
        System.out.println(validTokens);
        new Parser(categoryMapping, validTokens).parse();
        return true;
    }
}