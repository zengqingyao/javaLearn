package com.zengqy.design_pattern.struct;

/**
 * @包名: com.zengqy.design_pattern.struct
 * @author: zengqy
 * @DATE: 2023/8/29 17:37
 * @描述: 栈和队列的实现
 */
public class StackQueue<E> {
    transient int size = 0;
    Node<E> first;
    Node<E> last;


    private void linkFirst(E e) {
        final Node<E> f = first;
        Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    private E unlinkFirst() {
        if (first == null) {
            throw new RuntimeException("没有数据了");
        }
        final Node<E> f = first;
        final Node<E> next = f.next;
        E element = f.item;
        f.item = null;
        f.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return element;
    }

    private void linkLast(E e) {
        final Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    // 1 2 3 4 5  size 5  index 3
    private Node<E> node(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("下标范围过大");
        if (index < size >> 1) {
            Node<E> node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        } else {
            Node<E> node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
            return node;
        }
    }

    private E unlink(Node<E> node) {
        E element = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.next = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.prev = null;
        }

        size--;
        node.item = null;
        return element;
    }


    public E get(int index) {
        return node(index).item;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0 ? true : false;
    }

    public void push(E e) {
        linkFirst(e);
    }


    public E pop() {
        return unlinkFirst();
    }

    public E peek() {
        final Node<E> f = first;
        return f == null ? null : f.item;
    }

    public void add(E e) {
        linkLast(e);
    }

    public boolean offer(E e) {
        linkLast(e);
        return true;
    }

    public E remove() {
        return unlinkFirst();
    }

    public E remove(int index) {
        return unlink(node(index));
    }

    public E poll() {
        return unlinkFirst();
    }

    public boolean remove(Object o) {

        if (o == null) {
            Node<E> node = first;
            for (; node != null; node = node.next) {
                if (node.item == null) {
                    unlink(node);
                    return true;
                }
            }
        } else {
            Node<E> node = first;
            for (; node != null; node = node.next) {
                if (node.item.equals(o)) {
                    unlink(node);
                    return true;
                }
            }
        }
        return false;

    }

    private static class Node<E> {
        Node<E> prev;
        E item;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [ ");
        for (int i = 0; i < size(); i++) {
            sb.append(get(i) + " ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {

        StackQueue<String> stack = new StackQueue<>();
        stack.push("123");
        stack.push("456");
        stack.push("789");

        System.out.println(stack);
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());


        System.out.println("=======================队列");
        stack.offer("abc");
        stack.offer("def");
        stack.offer("hhc");
        stack.offer(null);
        stack.offer("zengqy");
        stack.offer("apple");
        System.out.println(stack);

        System.out.println(stack.get(1));
        System.out.println(stack.remove());
        System.out.println(stack);

        System.out.println(stack.remove(1));
        System.out.println(stack);

        System.out.println(stack.remove(null));
        System.out.println(stack.remove("apple"));
        System.out.println(stack);


    }
}
