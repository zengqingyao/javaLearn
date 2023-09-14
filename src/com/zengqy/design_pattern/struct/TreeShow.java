package com.zengqy.design_pattern.struct;

import static com.zengqy.design_pattern.struct.AvlLearn.Node;

/**
 * @包名: com.zengqy.design_pattern.struct
 * @author: zengqy
 * @DATE: 2023/8/29 11:23
 * @描述: 打印树的形状，Node需要导入对应的
 */
public class TreeShow {

//    public static class Node<E> {
//        int key;
//        AvlLearn.Node<E> lchild;
//        E item;
//        AvlLearn.Node<E> rchild;
//        int height = 1;
//
//        Node(int key, E item) {
//            this.key = key;
//            this.item = item;
//        }
//    }


    public static int getTreeDepth(Node root) {
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


    public static String printTree(Node root) {
        if (root == null)
            return "不能显示，因为二叉树为空!";

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


    public static void main(String[] args) {

    }
}
