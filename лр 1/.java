     Java
СОЗДАНИЕ СПИСКА:
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(); 
        list.add("1");                     
        list.add("2");                     
        System.out.println(list);               
    }
}

СОЗДАНИЕ СТЕКА:
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>(); 
        stack.push("1");                     
        stack.push("2");                      
        System.out.println(stack);                
    }
}
