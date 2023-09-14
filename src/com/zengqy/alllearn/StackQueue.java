package com.zengqy.alllearn;

/**
 * @包名: com.zengqy.alllearn
 * @author: zengqy
 * @DATE: 2023/9/12 16:24
 * @描述:
 */
public class StackQueue<E> {

    transient int size = 0;
    Node<E> first;
    Node<E> last;


    private void linkFirst(E e) {
        final Node<E> f = first;
        Node<E> newNode = new Node<>(null, e, first);
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
        final Node<E> next = first.next;
        E item = first.item;

        f.item = null;
        f.next = null;

        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }

        size--;
        return item;
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

    private E unlink(Node<E> e) {
        E element = e.item;

        Node<E> prev = e.prev;
        Node<E> next = e.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            e.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            e.next = null;
        }


        size--;
        return element;
    }

    // 1 2 3 4 5
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


    public void push(E e) {
        linkFirst(e);
    }

    public void add(E e) {
        linkLast(e);
    }


    public E pop() {
        return unlinkFirst();
    }

    public E peek() {
        final Node<E> f = first;
        return f == null ? null : f.item;
    }

    public boolean offer(E e) {
        linkLast(e);
        return true;
    }

    public E get(int index) {
        return node(index).item;
    }

    public E remove(int index) {
        return unlink(node(index));
    }

    public E remove() {
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

        public Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

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
