import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;





public class Main {
    public static void main(String[] args) {
        // String testEquation = "1+2+3";
        // String testEquation = "2*(3+4)";
        String testEquation = "2-(3+4)*5";

        Lexer lexer = new Lexer(testEquation);
        lexer.lexer();
    }
}