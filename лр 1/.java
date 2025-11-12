     Java
СОЗДАНИЕ МАССИВА:
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[2];   
        arr[0] = 1;              
        arr[1] = 2;              

        System.out.println(Arrays.toString(arr)); 
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
