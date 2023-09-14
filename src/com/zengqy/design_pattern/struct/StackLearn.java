package com.zengqy.design_pattern.struct;

/**
 * @包名: com.zengqy.design_pattern.struct
 * @author: zengqy
 * @DATE: 2023/8/28 19:19
 * @描述: 手写栈 , 队列，双向链表就可以实现
 */
public class StackLearn<E> {

    transient int size = 0;
    transient Node<E> first;
    transient Node<E> last;

    /**
     * 从尾部插入数据
     *
     * @param e
     */
    private void addLast(E e) {
        final Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) { //如果没有任何节点，则把first也设置为最新的节点
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    /**
     * 删除第一个节点
     *
     * @param f
     * @return
     */
    private E unlinkFirst(Node<E> f) {
        E item = f.item;
        Node<E> next = f.next;
        f.next = null;
        f.item = null;
        first = next;
        if (next == null) {
            last = null;  // 没有节点了，则last重新设置为null;
        } else {
            next.prev = null;
        }
        size--;
        return item;
    }

    /**
     * 从头部插入节点
     *
     * @param e
     */
    private void linkFirst(E e) {
        final Node<E> f = first;
        Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) { // 说明没有任何节点，这是第一个，所以last也需要赋值
            last = newNode;
        } else {
            f.prev = newNode; // 前一个节点需要指向新的节点
        }
        size++;
    }

    /**
     * 栈，在头部插入数据
     *
     * @param e
     */
    public void push(E e) {
        linkFirst(e);
    }

    /**
     * 栈，取出头部的值
     *
     * @return
     */
    public E pop() {
        final Node<E> f = first;
        if (f == null) {
            throw new RuntimeException("栈是空的");
        }
        return unlinkFirst(f);
    }

    /**
     * 尾部插入
     *
     * @param e
     * @return
     */
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    /**
     * 尾部插入
     *
     * @param e
     * @return
     */
    public boolean offer(E e) {
        return add(e);
    }

    /**
     * 从头部取出节点
     *
     * @return
     */
    public E poll() {
        return unlinkFirst(first);
    }


    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : first.item;
    }

    /**
     * 返回对应的节点
     *
     * @param index
     * @return
     */
    Node<E> node(int index) {

        if (index < (size >> 1)) { // 从头搜索或者从尾部搜索
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

    /**
     * 根据index 获取对应的值
     *
     * @param index
     * @return
     */
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("数组下标越界");
        }
        return node(index).item;
    }

    /**
     * 删除第一个节点
     */
    public E remove() {
        final Node<E> f = first;
        if (f == null) {
            throw new RuntimeException("remove失败，没有数据了");
        }
        return unlinkFirst(f);
    }


    /**
     * 删除传入的节点
     *
     * @param e
     * @return
     */
    private E unlink(Node<E> e) {

        E element = e.item;
        Node<E> prev = e.prev;
        Node<E> next = e.next;

        if (prev == null) {  // 如果前一个节点为null，说明删除的是是头结点
            first = next;
        } else {
            prev.next = next;
            e.prev = null;
        }

        if (next == null) { // 下一个节点为null，则代表删除的是尾节点
            last = prev;
        } else {
            next.prev = prev;
            e.next = null;
        }

        size--;
        e.item = null;
        return element;
    }


    /**
     * 删除某个节点
     *
     * @param index
     * @return
     */
    public E remove(int index) {
        return unlink(node(index));
    }

    /**
     * 删除某个对象
     * @param o
     * @return
     */
    public boolean remove(Object o) {
        if (o == null) {
            Node<E> node = first;
            for (; node != null; node = node.next) {
                if (node.item == null) { // 删除 null
                    unlink(node);
                    return true;
                }
            }

        } else {
            Node<E> node = first;
            for (; node != null; node = node.next) {
                if (o.equals(node.item)) {
                    unlink(node);
                    return true;
                }
            }
        }
        return false;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }


    // 保存数据的node
    private static class Node<E> {
        E item;
        Node<E> prev;
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
        sb.append("[ ");
        for (int i = 0; i < size(); i++) {
            E e = this.get(i);
            sb.append(e + " ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {

        StackLearn<String> stack = new StackLearn<>();
        stack.push("123");
        stack.push("456");
        stack.push("789");
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());

        System.out.println("================队列");

        StackLearn<Integer> queue = new StackLearn<>();
        queue.offer(12);
        queue.offer(34);
        queue.offer(56);
        queue.offer(78);
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());

        queue.add(100);
        queue.add(200);
        queue.add(300);
        queue.add(500);
        System.out.println(queue.get(3));


        System.out.println(queue);
        queue.remove(1);
        System.out.println(queue);

        queue.remove(500);
        System.out.println(queue);


    }

}
