package study;

import java.util.Stack;

public class graph_stack {
    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();

        s.push(5);
        s.push(2);
        s.push(3);
        s.push(7);
        s.pop();
        s.push(1);
        s.push(4);
        s.pop();

        // 최상단 원소부터 출력
        while (!s.empty()) {
            System.out.println(s.peek() + " ");
            s.pop();
        }
    }
    
}
