   **МУЛЬТИСПИСОК**
   
public class Node {

    int data;
    
    Node prev;
    
    Node next;
    

    public Node(int data)
    
    {
    
        this.data = data;
        
        this.prev = null;
        
        this.next = null;
        
    }
    
}

public class DoublyLinkedList {

    Node head;
    
    Node tail;
    

    public DoublyLinkedList()
    
    {
    
        this.head = null;
        
        this.tail = null;
        
    }
    
}


  **ОЧЕРЕДЬ**
  
Queue<String> queue = new LinkedList<>();

queue.add("Маша"); 

queue.add("Катя"); 

queue.add("Соня"); 



  **ДЕК**
  
Deque<Integer> stack = new ArrayDeque<>();

stack.push(10); 

stack.push(20); 

stack.push(30); 


  **ПРИОРИТЕТНАЯ ОЧЕРЕДЬ**
  
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

minHeap.offer(7); 

minHeap.offer(50); 

minHeap.offer(1); 

minHeap.offer(9); 

