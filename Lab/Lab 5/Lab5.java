import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.*;

public class Lab5 {
    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();
    static HashMap<Integer, Stack<Peserta>> map = new HashMap<>();
    

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            // TODO: process inputs
            String nama = in.next();
            int powerLevel = in.nextInt();
            if (map.containsKey(powerLevel)) {
                map.get(powerLevel).add(new Peserta(nama, powerLevel));
            } else {
                tree.root = tree.insertNode(tree.root, powerLevel, nama);
            }
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                handleQueryMasuk();
            } else {
                handleQueryDuo();
            }
        }

        out.close();
    }

    static void handleQueryMasuk() {
        // TODO
        String nama = in.next();
        int powerLevel = in.nextInt();
        // if stack existed, then just push to the stack
        if (map.containsKey(powerLevel)) {
            map.get(powerLevel).add(new Peserta(nama, powerLevel));
        } else {
            tree.root = tree.insertNode(tree.root, powerLevel, nama);
        }

        // count the element in the tree that smaller than powerlevel
        int count = tree.countNode(tree.root, powerLevel);
        out.println(count);
    }

    static void handleQueryDuo() {
        // TODO
        int K = in.nextInt();
        int B = in.nextInt();
        Node minimum = tree.lowerBound(tree.root, K);
        Node maximum = tree.upperBound(tree.root, B);

        // there is only 1 in a stack
        if (minimum == maximum && Lab5.map.get(maximum.powerLevel).size() == 1){
            out.println("-1 -1");
        }
        // if failed to get lowerbound or upperbound
        else if (minimum == null || maximum == null) {
            out.println("-1 -1");
        } else {
            String nama1 = "";
            String nama2 = "";
            Peserta peserta1 = null;
            Peserta peserta2 = null;

            // if maximum == minimum have the same power level
            if (minimum == maximum) {
                peserta1 = Lab5.map.get(minimum.powerLevel).pop();
                peserta2 = Lab5.map.get(maximum.powerLevel).peek();
                tree.root = tree.deleteNode(tree.root, minimum.powerLevel);
            } else {
                peserta1 = Lab5.map.get(minimum.powerLevel).peek();
                peserta2 = Lab5.map.get(maximum.powerLevel).peek();
                tree.root = tree.deleteNode(tree.root, minimum.powerLevel);
                tree.root = tree.deleteNode(tree.root, maximum.powerLevel);
            }

            // for lexicographical output
            if (peserta1.nama.compareTo(peserta2.nama) < 0) {
                nama1 = peserta1.nama;
                nama2 = peserta2.nama;
            } else {
                nama1 = peserta2.nama;
                nama2 = peserta1.nama;
            }
            out.println(nama1 + " " + nama2);
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}


// TODO: modify as needed
class Peserta {
    String nama;
    int powerLevel;
    Peserta (String nama, int powerLevel){
        this.nama = nama;
        this.powerLevel = powerLevel;
    }
}
class Node {
    int powerLevel;
    int height;
    Node left, right;
    Node(int powerLevel) {
        this.powerLevel = powerLevel;
        this.height = 1;
    }
}

// REFERENSI
// 1. Geeks for geeks: https://www.geeksforgeeks.org/insertion-in-an-avl-tree/
// 2. Geeks for geeks: https://www.geeksforgeeks.org/avl-tree-set-2-deletion/
// 3. Programiz: https://www.programiz.com/dsa/avl-tree
class AVLTree {
    Node root;
    // right rotate
    Node rightRotate(Node node) {
        // TODO: implement right rotate
        Node node1 = node.left;
        Node node2 = node1.right;
        node1.right = node;
        node.left = node2;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node1.height = Math.max(getHeight(node1.left), getHeight(node1.right)) + 1;
        return node1;
    }
    // left rotate
    Node leftRotate(Node node) {
        // TODO: implement left rotate
        Node node1 = node.right;
        Node node2 = node1.left;
        node1.left = node;
        node.right = node2;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node1.height = Math.max(getHeight(node1.left), getHeight(node1.right)) + 1;
        return node1;
    }
 
    Node insertNode(Node node, int powerLevel, String nama) {
        // TODO: implement insert node
        // create new node and new stack by powerlevel
        if (node == null) {
            Stack<Peserta> stack = new Stack<>();
            Peserta newPeserta = new Peserta(nama, powerLevel);
            stack.add(newPeserta);
            Lab5.map.put(powerLevel, stack);
            Node newNode = new Node(powerLevel);
            return newNode;
        }
        if (powerLevel < node.powerLevel) node.left = insertNode(node.left, powerLevel, nama);
        else if (powerLevel > node.powerLevel) node.right = insertNode(node.right, powerLevel, nama);
        else return node;

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (powerLevel < node.left.powerLevel) {
                return rightRotate(node);
            } else if (powerLevel > node.left.powerLevel) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balanceFactor < -1) {
            if (powerLevel > node.right.powerLevel) {
                return leftRotate(node);
            } else if (powerLevel < node.right.powerLevel) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }

    Node deleteNode(Node node, int powerLevel) {
        // TODO: implement delete node
        // if powerlevel stack size > 1 then just pop
        if (Lab5.map.get(powerLevel).size() > 1) {
            Lab5.map.get(powerLevel).pop();
        } 
        // else, then delete the stack
        else {
            if (node == null) return node;
            if (powerLevel < node.powerLevel) {
                node.left = deleteNode(node.left, powerLevel);
            } else if (powerLevel > node.powerLevel) {
                node.right = deleteNode(node.right, powerLevel);
            } else {
                // case kalo node punya 0/1 child
                if ((node.left == null) || (node.right == null)) {
                    Node temp = null;
                    if (temp == node.left) {
                        temp = node.right;
                    } else {
                        temp = node.left;
                    }
                    if (temp == null) {
                        temp = node;
                        node = null;
                    } else { 
                        node = temp;
                    };
                } 
                // case kalo node punya 2 child
                else {
                    Node current = node;
                    while (current.left != null) current = current.left;
                    node.powerLevel = current.powerLevel;
                    node.right = deleteNode(node.right, current.powerLevel);
                }
            }
            if (node == null) return node;

            // Update the balance factor of each node and balance the tree
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
            int balanceFactor = getBalance(node);
            if (balanceFactor > 1) {
                if (getBalance(node.left) >= 0) {
                    return rightRotate(node);
                } else {
                    node.left = leftRotate(node.left);
                    return rightRotate(node);
                }
            }
            if (balanceFactor < -1) {
                if (getBalance(node.right) <= 0) {
                    return leftRotate(node);
                } else {
                    node.right = rightRotate(node.right);
                    return leftRotate(node);
                }
            }
            Lab5.map.remove(powerLevel);
        }
        return node;
    }

    // get lowerbound
    Node lowerBound(Node node, int value){
        if (node.left == null && node.right == null && node.powerLevel < value) {
            return null;
        }
        if ((node.powerLevel >= value && node.left == null) || (node.powerLevel >= value && node.left.powerLevel < value)){
            return node;
        }
        if (node.powerLevel <= value) {
            return lowerBound(node.right, value);
        }
        else {
            return lowerBound(node.left, value);
        }
    }

    // get upperbound
    Node upperBound(Node node, int value){
        if (node.left == null && node.right == null && node.powerLevel > value) {
            return null;
        }
        if ((node.powerLevel <= value && node.right == null) || (node.powerLevel <= value && node.right.powerLevel > value)){
            return node;
        }
        if (node.powerLevel >= value) {
            return upperBound(node.left, value);
        }
        else {
            return upperBound(node.right, value);
        }   
    }

    // create countNode lower than x in tree
    int countNode(Node node, int x) {
        if (node == null) return 0;
        if (node.powerLevel < x) return Lab5.map.get(node.powerLevel).size() + countNode(node.left, x) + countNode(node.right, x);
        else return countNode(node.left, x);
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
}