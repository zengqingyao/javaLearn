package com.zengqy.design_pattern.struct;

import java.util.LinkedList;

/**
 * @包名: com.zengqy.design_pattern.struct
 * @author: zengqy
 * @DATE: 2023/8/28 22:17
 * @描述: 手写平衡二叉树
 */
public class AvlLearn<E> {

    // 树的根
    transient Node<E> root;


    /**
     * 需要递归调用，故需要返回node
     *
     * @param e
     * @return
     */
    public void insert(int key, E e) {
        Node<E> newNode = new Node<>(key, e);
        root = avl_insert(root, newNode);

    }

    public void reomve(int key) {
        root = avl_remove(root, root, key);
    }


    private int avl_height(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    /**
     * 插入数据
     *
     * @param root
     * @param e
     * @return
     */
    private Node<E> avl_insert(Node<E> root, Node<E> e) {

        if (root == null) {
            return e;
        }

        // 没有== 故不允许重复插入相同的key
        if (e.key < root.key) {
            root.lchild = avl_insert(root.lchild, e);
        } else if (e.key > root.key) {
            root.rchild = avl_insert(root.rchild, e);
        }

        root = avl_blance(root);

        return root;
    }

    private Node<E> avl_blance(Node<E> root) {
        Node<E> left = root.lchild;
        Node<E> right = root.rchild;
        if (avl_height(left) - avl_height(right) >= 2) { // 左孩子太高
            // 左左不平衡
            if (avl_height(left.lchild) > avl_height(left.rchild)) {
                root = avl_rotate_right(root);
            } else { // 左右不平衡
                root = avl_rotate_leftright(root);
            }

        } else if (avl_height(right) - avl_height((left)) >= 2) {
            // 右右不平衡
            if (avl_height(right.rchild) > avl_height(right.lchild)) {
                root = avl_rotate_left(root);
            } else {
                root = avl_rotate_rightleft(root);
            }
        }

        root.height = Math.max(avl_height(root.lchild), avl_height(root.rchild)) + 1;
        return root;
    }


    /**
     * 左右旋转
     * //	左右不平衡的时候举例，先让3的右孩子指向4，再让5的左孩子指向3
     * //	       6							   6                         4
     * //	    2         对2进行左旋后变为--->    4    然后对5进行右旋--->   2   6
     * //	      4
     *
     * @param root
     * @return
     */
    private Node<E> avl_rotate_leftright(Node<E> root) {
        root.lchild = avl_rotate_left(root.lchild);
        return avl_rotate_right(root);
    }

    /**
     * 右左旋转
     *
     * @param root
     * @return
     */
    private Node<E> avl_rotate_rightleft(Node<E> root) {
        root.rchild = avl_rotate_right(root.rchild);
        return avl_rotate_left(root);
    }

    /**
     * 右旋
     * 左左不平衡的时候举例，先让4的左孩子指向3，再让2的右孩子指向4
     * //	    4							 2
     * //	  2           右旋后变为--->    1      4
     * //	1 	3                              3
     *
     * @param root
     * @return
     */
    private Node<E> avl_rotate_right(Node<E> root) {
        Node<E> tmp = root.lchild;
        root.lchild = tmp.rchild;
        tmp.rchild = root;

        root.height = Math.max(avl_height(root.lchild), avl_height(root.rchild)) + 1;
        tmp.height = Math.max(avl_height(tmp.lchild), avl_height(tmp.rchild)) + 1;
        return tmp;
    }


    /**
     * 左旋
     * //	右右不平衡的时候举例，先让3的右孩子指向4，再让5的左孩子指向3
     * //	   3							   5
     * //	      5        左旋后变为--->    3     6
     * //	     4  6                      	  4
     *
     * @param root
     * @return
     */
    private Node<E> avl_rotate_left(Node<E> root) {
        Node<E> tmp = root.rchild;
        root.rchild = tmp.lchild;
        tmp.lchild = root;

        root.height = Math.max(avl_height(root.lchild), avl_height(root.rchild)) + 1;
        tmp.height = Math.max(avl_height(tmp.lchild), avl_height(tmp.rchild)) + 1;
        return tmp;
    }


    //       55
    //    50    70
    //  30    60
    //    40
    //  39  45
    private Node<E> avl_remove(Node<E> cur, Node<E> parent, int key) {
        if (cur == null) {
            return null;
        }

        if (key < cur.key) {
            cur.lchild = avl_remove(cur.lchild, cur, key);
        } else if (key > cur.key) {
            cur.rchild = avl_remove(cur.rchild, cur, key);
        } else { //找到对应的值
            Node<E> tmp;
            Node<E> tmpParent; // 保存tmp节点的父节点
            if (cur.lchild != null) {
                tmpParent = cur;
                for (tmp = cur.lchild; tmp.rchild != null; ) {
                    tmpParent = tmp;
                    tmp = tmp.rchild;
                }
                cur.key = tmp.key;
                cur.item = tmp.item;
                cur.lchild = avl_remove(cur.lchild, tmpParent, tmp.key);
            } else if (cur.rchild != null) {
                tmpParent = cur;
                for (tmp = cur.rchild; tmp.lchild != null; ) {
                    tmpParent = tmp;
                    tmp = tmp.lchild;
                }
                cur.item = tmp.item;
                cur.key = tmp.key;
                cur.rchild = avl_remove(cur.rchild, tmpParent, tmp.key);
            } else { // 没有左右孩子，所以直接删除就行
                if (parent == null) {
                    return null;
                }
                if (parent.lchild == cur) {
                    parent.lchild = null;
                } else if (parent.rchild == cur) {
                    parent.rchild = null;
                }
                return null;
            }
        }

        cur = avl_blance(cur);

        return cur;
    }


    //====================================遍历start=============================

    /**
     * 前序遍历：先访问根节点，再访问左子树，最后访问右子树
     *
     * @param fun
     */
    public void preIterate(AvlTraverse fun) {
        avl_preIterate(root, fun);
    }

    /**
     * 中序遍历：先访问左子树，在访问根节点，最后访问右子树
     *
     * @param fun
     */
    public void midIterate(AvlTraverse fun) {
        avl_midIterate(root, fun);
    }

    /**
     * 后序遍历：先访问左子树，再访问右子树，最后访问根节点
     *
     * @param fun
     */
    public void postIterate(AvlTraverse fun) {
        avl_postIterate(root, fun);
    }

    /**
     * 按层遍历需要用队列
     *
     * @param fun
     */
    public void layerIterate(AvlTraverse fun) {

        LinkedList<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        while (true) {
            Node<E> node = queue.poll();
            if (node == null) {
                break;
            }

            fun.todo(node);

            if (node.lchild != null) {
                queue.offer(node.lchild);
            }

            if (node.rchild != null) {
                queue.offer(node.rchild);
            }
        }
    }

    //前序遍历：先访问根节点，再访问左子树，最后访问右子树
    void avl_preIterate(Node<E> root, AvlTraverse fun) {
        if (root == null) {
            return;
        }
        fun.todo(root);
        avl_preIterate(root.lchild, fun);
        avl_preIterate(root.rchild, fun);
    }

    //中序遍历：先访问左子树，在访问根节点，最后访问右子树
    void avl_midIterate(Node<E> root, AvlTraverse fun) {
        if (root == null) {
            return;
        }
        avl_midIterate(root.lchild, fun);
        fun.todo(root);
        avl_midIterate(root.rchild, fun);
    }

    //后序遍历：先访问左子树，再访问右子树，最后访问根节点
    void avl_postIterate(Node<E> root, AvlTraverse fun) {
        if (root == null) {
            return;
        }
        avl_postIterate(root.lchild, fun);
        avl_postIterate(root.rchild, fun);
        fun.todo(root);
    }
    //====================================遍历end=============================


    //       55
    //    50    70
    //  30    60
    //    40
    //  39  45
    @Override
    public String toString() {
        return TreeShow.printTree(root);
    }

    // 排序后需要干啥
    public static abstract class AvlTraverse {
        abstract void todo(AvlLearn.Node e);
    }


    /**
     * 平衡二叉树的节点数据结构
     *
     * @param <E>
     */
    public static class Node<E> {
        int key;
        Node<E> lchild;
        E item;
        Node<E> rchild;
        int height = 1;

        Node(int key, E item) {
            this.key = key;
            this.item = item;
        }
    }

    public static void main(String[] args) {

        // 没有做平衡的时候
        //       55
        //    50    70
        //  30    60
        //    40
        //  39  45
        AvlLearn<String> avl = new AvlLearn<>();
        avl.insert(55, "zengqy");
        avl.insert(50, "zengqy");
        avl.insert(70, "zengqy");
        avl.insert(60, "zengqy");
        avl.insert(30, "zengqy");
        avl.insert(40, "zengqy");
        avl.insert(39, "zengqy");
        avl.insert(45, "zengqy");

        System.out.println(avl);
        avl.reomve(60);
        System.out.println(avl);
        avl.reomve(55);
//        avl.reomve(50);
//        avl.reomve(70);
//        avl.reomve(40);
//        avl.reomve(39);
//        avl.reomve(45);
//        avl.reomve(30);

        System.out.println(avl);


    }


}
