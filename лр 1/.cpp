   C++
СОЗДАНИЕ МАССИВА:
#include <iostream>

int main() {
    int arr[2];           
    arr[0] = 10;         
    arr[1] = 20;          

    std::cout << arr[0] << " " << arr[1] << "\n"; 
    return 0;
}

СОЗДАНИЕ СТЕКА:
#include <iostream>
#include <stack>

int main() {
    std::stack<std::string> st;
    st.push("10");          
    st.push("20");          

    std::cout << st.top() << "\n"; 
    return 0;
}
