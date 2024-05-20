import java.util.List;

class Parser {
    private List<TokenType> categoryMapping;
    private List<String> validTokens;

    public Parser(List<TokenType> categoryMapping, List<String> validTokens) {
        this.categoryMapping = categoryMapping;
        this.validTokens = validTokens;
    }

    public void parse() {
        Node root = new Node(categoryMapping.get(0).name());
        Node parent = root;
        for (int i = 0; i < validTokens.size(); i++) {
            String token = validTokens.get(i);
            TokenType category = categoryMapping.get(i + 1);
            Node node = new Node(token, parent);
            parent = new Node(category.name(), node);
        }
        printTree(root, "");
    }

    private void printTree(Node node, String prefix) {
        System.out.println(prefix + (prefix.isEmpty() ? "" : "|-- ") + node.name);
        for (Node child : node.children) {
            printTree(child, prefix + (prefix.isEmpty() ? "" : "|   "));
        }
    }
}