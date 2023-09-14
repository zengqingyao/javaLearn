package com.zengqy.design_pattern.struct;

import java.util.LinkedList;

/**
 * @包名: com.zengqy.design_pattern.struct
 * @author: zengqy
 * @DATE: 2023/8/29 14:49
 * @描述: 手写平衡二叉树
 */
public class AvlTree<E> {
    private Node<E> root;

    /**
     * 根据key 查找对应的value
     *
     * @param key
     * @return
     */
    public E find(int key) {
        Node<E> cur = root;
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.lchild;
            } else if (key > cur.key) {
                cur = cur.rchild;
            } else {
                return cur.item;
            }
        }
        return null;
    }


    public void insert(int key, E e) {
        Node<E> newNode = new Node<>(key, e);
        root = avl_insert(root, newNode);
    }


    private Node<E> avl_insert(Node<E> cur, Node<E> newNode) {
        if (cur == null) {
            return newNode;
        }

        if (newNode.key < cur.key) {
            cur.lchild = avl_insert(cur.lchild, newNode);
        } else if (newNode.key > cur.key) {
            cur.rchild = avl_insert(cur.rchild, newNode);
        } else {
            // 相同的key也可以插入，插入在左边
            cur.lchild = avl_insert(cur.lchild, newNode);
        }

        cur = avl_blance(cur);

        return cur;
    }


    public boolean remove(int key) {
        root = avl_remove(root, root, key);
        return true;
    }


    /**
     * 递归删除某个节点，需要传父节点
     *
     * @param cur
     * @param parent
     * @param key
     * @return
     */
    private Node<E> avl_remove(Node<E> cur, Node<E> parent, int key) {
        if (cur == null) {
            return null;
        }

        if (key < cur.key) {
            cur.lchild = avl_remove(cur.lchild, cur, key);
        } else if (key > cur.key) {
            cur.rchild = avl_remove(cur.rchild, cur, key);
        } else {
            Node<E> tmp;
            Node<E> tmpParent; // 记录节点的父节点
            if (cur.lchild != null) { //找到左孩子里面的最大的节点
                tmpParent = cur;
                for (tmp = cur.lchild; tmp.rchild != null; tmp = tmp.rchild) {
                    tmpParent = tmp;
                }
                cur.key = tmp.key;
                cur.item = tmp.item;
                cur.lchild = avl_remove(cur.lchild, tmpParent, tmp.key); //递归删除该节点
            } else if (cur.rchild != null) {
                tmpParent = cur;
                for (tmp = cur.rchild; tmp.lchild != null; tmp = tmp.lchild) {
                    tmpParent = tmp;
                }
                cur.key = tmp.key;
                cur.item = tmp.item;
                cur.rchild = avl_remove(cur.rchild, tmpParent, tmp.key);
            } else { // 没有孩子了，可以直接删除
                if (parent == null) // 没有父节点，可以直接返回了
                {
                    return null;
                }
                if (parent.lchild == cur) { // 父节点直接null，则会自动回收该子节点
                    parent.lchild = null;
                } else if (parent.rchild == cur) {
                    parent.rchild = null;
                }
                return null; // 子节点已经删除，返回给父节点
            }
        }

        cur = avl_blance(cur); // 判断cur是否平衡

        return cur;
    }


    /**
     * 返回某个节点的高度
     *
     * @param node
     * @return
     */
    private int avl_height(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    /**
     * 判断并使某个节点平衡，左右孩子高度差为<=1
     *
     * @param cur
     * @return
     */
    private Node<E> avl_blance(Node<E> cur) {
        Node<E> left = cur.lchild;
        Node<E> right = cur.rchild;

        if (avl_height(left) - avl_height(right) >= 2) { // 左孩子太高
            if (avl_height(left.lchild) > avl_height(left.rchild)) { //左左不平衡
                cur = avl_rotate_right(cur);
            } else { //左右不平衡
                cur = avl_rotate_leftright(cur);
            }
        } else if (avl_height(right) - avl_height(left) >= 2) { // 右孩子太高
            if (avl_height(right.rchild) > avl_height(right.lchild)) {
                cur = avl_rotate_left(cur);
            } else {
                cur = avl_rotate_rightleft(cur);
            }
        }

        cur.height = Math.max(avl_height(cur.lchild), avl_height(cur.rchild)) + 1;
        return cur;
    }


    /**
     * 左左不平衡的时候举例，先让4的左孩子指向3，再让2的右孩子指向4
     * //	左左不平衡的时候举例，先让4的左孩子指向3，再让2的右孩子指向4
     * //	    4							 2
     * //	  2           右旋后变为--->    1   4
     * //	1 	3                             3
     *
     * @param node
     * @return
     */
    private Node<E> avl_rotate_right(Node<E> node) {
        Node<E> tmp = node.lchild;
        node.lchild = tmp.rchild;
        tmp.rchild = node;


        node.height = Math.max(avl_height(node.lchild), avl_height(node.rchild)) + 1;
        tmp.height = Math.max(avl_height(tmp.lchild), avl_height(tmp.rchild)) + 1;
        return tmp;
    }


    /**
     * //	右右不平衡的时候举例，先让3的右孩子指向4，再让5的左孩子指向3
     * //	   3							   5
     * //	      5        左旋后变为--->    3    6
     * //	     4  6
     *
     * @param node
     * @return
     */
    private Node<E> avl_rotate_left(Node<E> node) {
        Node<E> tmp = node.rchild;
        node.rchild = tmp.lchild;
        tmp.lchild = node;

        node.height = Math.max(avl_height(node.lchild), avl_height(node.rchild)) + 1;
        tmp.height = Math.max(avl_height(tmp.lchild), avl_height(tmp.rchild)) + 1;
        return tmp;
    }


    /**
     * 右左转旋转，适用于右左不平衡，先对右孩子右旋，然后对该节点左旋
     *
     * @param node
     * @return
     */
    private Node<E> avl_rotate_rightleft(Node<E> node) {
        node.rchild = avl_rotate_right(node.rchild);
        return avl_rotate_left(node);
    }


    /**
     * 适用于左右不平衡，先对左孩子左旋，然后该节点右旋
     *
     * @param node
     * @return
     */
    private Node<E> avl_rotate_leftright(Node<E> node) {
        node.lchild = avl_rotate_left(node.lchild);
        return avl_rotate_right(node);
    }

    public void clear() {
        traverse(3, new AvlIter() {
            @Override
            void todo(Node e) {
                e.item = null;
                e.lchild = null;
                e.rchild = null;
            }
        });
        root = null;
    }

    public void traverse(int type, AvlIter fun) {
        if (type == 0) {
            System.out.println("前序遍历，先返回根节点，然后返回左，再到右");
            pre_avlIter(root, fun);
        } else if (type == 1) {
            System.out.println("中序遍历：先访问左子树，在访问根节点，最后访问右子树");
            mid_avlIter(root, fun);
        } else if (type == 2) {
            System.out.println("后序遍历：先访问左子树，再访问右子树，最后访问根节点");
            post_avlIter(root, fun);
        } else {
            System.out.println("按层遍历");
            layer_avlIter(root, fun);
        }
    }

    /**
     * 按层遍历
     *
     * @param
     * @param fun
     */
    private void layer_avlIter(Node<E> node, AvlIter fun) {

        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(node);
        while (true) {
            Node e = queue.poll();
            if (e == null) {
                break;
            }

            if (e.lchild != null) {
                queue.offer(e.lchild);
            }
            if (e.rchild != null) {
                queue.offer(e.rchild);
            }

            fun.todo(e);
        }
    }


    /**
     * 前序遍历，先返回根节点，然后返回左，再到右
     *
     * @param fun
     */
    private void pre_avlIter(Node<E> cur, AvlIter fun) {
        if (cur == null) {
            return;
        }
        fun.todo(cur);
        pre_avlIter(cur.lchild, fun);
        pre_avlIter(cur.rchild, fun);
    }

    /**
     * 中序遍历：先访问左子树，在访问根节点，最后访问右子树
     *
     * @param fun
     */
    private void mid_avlIter(Node<E> cur, AvlIter fun) {
        if (cur == null) {
            return;
        }
        pre_avlIter(cur.lchild, fun);
        fun.todo(cur);
        pre_avlIter(cur.rchild, fun);
    }

    /**
     * 后序遍历：先访问左子树，再访问右子树，最后访问根节点
     *
     * @param cur
     * @param fun
     */
    private void post_avlIter(Node<E> cur, AvlIter fun) {
        if (cur == null) {
            return;
        }
        pre_avlIter(cur.lchild, fun);
        pre_avlIter(cur.rchild, fun);
        fun.todo(cur);
    }


    static abstract class AvlIter {
        abstract void todo(Node e);
    }


    public static class Node<E> {
        int key;
        E item;
        Node<E> lchild;
        Node<E> rchild;
        int height;

        public Node(int key, E item) {
            this.key = key;
            this.item = item;
        }
    }


    @Override
    public String toString() {
        return printTree(root);
    }

    public static void main(String[] args) {
        AvlTree<String> avl = new AvlTree<>();
        avl.insert(50, "zengqy1");
        avl.insert(46, "zengqy");
        avl.insert(35, "zengqy");
        avl.insert(1, "zengqy");
        avl.insert(80, "zengqy");
        avl.insert(50, "zengqy2");
        avl.insert(50, "zengqy3");


        System.out.println(avl);

        avl.remove(50);
        System.out.println(avl);
        avl.remove(50);
        System.out.println(avl);
        avl.remove(46);
        System.out.println(avl);

        avl.clear();
        avl.insert(1, "sd1");
        avl.insert(2, "sd2");
        avl.insert(3, "sd3");
        avl.insert(4, "sd4");
        avl.insert(5, "sd5");
        avl.insert(6, "sd6");

        System.out.println("== 4的value: "+avl.find(4));
        System.out.println("== 4的value: "+avl.find(1));
        System.out.println("== 4的value: "+avl.find(7));


        AvlIter fun = new AvlIter() {

            @Override
            void todo(Node e) {
                System.out.println("key:" + e.key + " " + e.item);
            }
        };
        avl.traverse(3, fun);

        System.out.println(avl);
        avl.clear();

    }


    // ================================================树的打印
    private static int getTreeDepth(Node root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.lchild), getTreeDepth(root.rchild)));
    }

    private static void writeArray(Node currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) return;
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.valueOf(currNode.key);

        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) return;
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.lchild != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.lchild, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.rchild != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.rchild, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }


    private static String printTree(Node root) {
        if (root == null)
            return "注意：二叉树为空!";

        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i++) {
            for (int j = 0; j < arrayWidth; j++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        // res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
        writeArray(root, 0, arrayWidth / 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        StringBuilder sb = new StringBuilder();
        for (String[] line : res) {
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
//            System.out.println(sb.toString());
            sb.append("\n");
        }
        return sb.toString();
    }


}
