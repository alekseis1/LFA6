import java.util.ArrayList;
import java.util.List;

class Node {
    String name;
    List<Node> children = new ArrayList<>();

    Node(String name) {
        this.name = name;
    }

    Node(String name, Node parent) {
        this.name = name;
        parent.children.add(this);
    }
}