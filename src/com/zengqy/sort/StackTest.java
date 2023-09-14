package com.zengqy.sort;

import java.util.*;

/**
 * @包名: com.zengqy.sort
 * @author: zengqy
 * @DATE: 2023/8/21 21:15
 * @描述:
 */
public class StackTest {

    public static boolean isValid(String s) {
        int n = s.length();
        if (n % 2 == 1) {
            return false;
        }

        Map<Character, Character> pairs = new HashMap<Character, Character>() {{
            put(')', '(');
            put(']', '[');
            put('}', '{');
        }};


        Deque<Character> stack = new LinkedList<Character>();
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (pairs.containsKey(ch)) {
                if (stack.isEmpty() || stack.peek() != pairs.get(ch)) {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(ch);
            }
        }
        return stack.isEmpty();
    }


    public static void main(String[] args) {

        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE % 10);
        System.out.println(Integer.MAX_VALUE / 10);

        System.out.println(isValid("{[]"));

        LinkedList<String> stack = new LinkedList<>();

        // 入栈
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");

        System.out.println(stack);
        System.out.println(stack.peek()); // 单纯获取值，但并没有出栈
        System.out.println(stack);

        System.out.println(stack.poll()); // 真正的出栈
        System.out.println(stack);



        // ============== 队列
        System.out.println("\n===============队列=================");
        LinkedList<Integer> queue = new LinkedList<>();

        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);

        System.out.println(queue);
        System.out.println(queue.peek()); // 单纯获取值，但并没有出队
        System.out.println(queue);

        System.out.println(queue.poll()); // 出队

        System.out.println(queue);


    }
}
