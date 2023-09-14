package com.zengqy.alllearn;

/**
 * @包名: com.zengqy.alllearn
 * @author: zengqy
 * @DATE: 2023/9/12 18:06
 * @描述:
 */
public class AvlTree<E> {
    Node<E> root;


    public void insert(int key, E e) {
        Node<E> newNode = new Node<>(key, e);
        root = avl_insert(root, newNode);
    }


    private Node<E> avl_insert(Node<E> root, Node<E> newNode) {
        if (root == null) {
            return newNode;
        }

        if (newNode.key < root.key) {
            root.lchild = avl_insert(root.lchild, newNode);
        } else {
            root.rchild = avl_insert(root.rchild, newNode);
        }

        root = avl_blance(root);

        return root;
    }

    private int height(Node<E> node) {
        return node == null ? 0 : node.height;
    }


    private Node<E> avl_blance(Node<E> cur) {
        Node<E> left = cur.lchild;
        Node<E> right = cur.rchild;

        if (height(left) - height(right) >= 2) {

            if (height(left.lchild) > height(left.rchild)) {
                cur = avl_rotate_right(cur);
            } else {
                cur = avl_rotate_leftright(cur);
            }

        } else if (height(right) - height(left) >= 2) {
            if (height(right.rchild) > height(right.lchild)) {
                cur = avl_rotate_left(cur);
            } else {
                cur = avl_rotate_rightleft(cur);
            }
        }

        cur.height = Math.max(height(cur.lchild), height(cur.rchild)) + 1;
        return cur;
    }

    private Node<E> avl_rotate_rightleft(Node<E> cur) {
        cur.rchild = avl_rotate_right(cur.rchild);
        return avl_rotate_left(cur);
    }


    private Node<E> avl_rotate_leftright(Node<E> cur) {
        cur.lchild = avl_rotate_left(cur.lchild);
        return avl_rotate_right(cur);
    }

    private Node<E> avl_rotate_left(Node<E> cur) {
        Node<E> tmp = cur.rchild;
        cur.rchild = tmp.lchild;
        tmp.lchild = cur;
        cur.height = Math.max(height(cur.lchild), height(cur.rchild)) + 1;
        tmp.height = Math.max(height(tmp.lchild), height(tmp.rchild)) + 1;
        return tmp;
    }

    private Node<E> avl_rotate_right(Node<E> cur) {
        Node<E> tmp = cur.lchild;
        cur.lchild = tmp.rchild;
        tmp.rchild = cur;


        cur.height = Math.max(height(cur.lchild), height(cur.rchild)) + 1;
        tmp.height = Math.max(height(tmp.lchild), height(tmp.rchild)) + 1;
        return tmp;
    }


    public boolean remove(int key) {
        root = avl_remove(root, root, key);
        return true;
    }

    private Node<E> avl_remove(Node<E> cur, Node<E> parent, int key) {
        if (cur == null) {
            return null;
        }

        if (key < cur.key) {
            cur.lchild = avl_remove(cur.lchild, cur.lchild, key);
        } else if (key > cur.key) {
            cur.rchild = avl_remove(cur.rchild, cur.rchild, key);
        } else {
            Node<E> tmp;
            Node<E> tmpParent;
            if (cur.lchild != null) {
                tmp = cur.lchild;
                tmpParent = cur;
                for (; tmp.rchild != null; tmp = tmp.rchild) {
                    tmpParent = tmp;
                }
                cur.key = tmp.key;
                cur.item = tmp.item;
                cur.lchild = avl_remove(cur.lchild, tmpParent, tmp.key);
            } else if (cur.rchild != null) {
                tmp = cur.rchild;
                tmpParent = cur;
                for (; tmp.lchild != null; tmp = tmp.lchild) {
                    tmpParent = tmp;
                }

                cur.key = tmp.key;
                cur.item = tmp.item;
                cur.rchild = avl_remove(cur.rchild, tmpParent, tmp.key);
            } else {
                if (parent == null) {
                    return null;
                }
                if (parent.lchild == cur) {
                    parent.lchild = null;
                } else if (parent.rchild == cur) {
                    parent.rchild = null;
                }
                return null; // 子节点已经删除，返回给父节点
            }
        }

        return cur;
    }


    private static class Node<E> {
        int key;
        E item;
        int height;
        Node<E> lchild;
        Node<E> rchild;

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
        avl.insert(81, "zengqy3");
        avl.insert(82, "zengqy3");
        avl.insert(83, "zengqy3");
        avl.insert(84, "zengqy3");


        System.out.println(avl);
        avl.remove(1);
        System.out.println(avl);
        avl.remove(50);
        System.out.println(avl);
        avl.remove(50);
        System.out.println(avl);


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
