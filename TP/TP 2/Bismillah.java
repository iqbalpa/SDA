import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Bismillah {
    private static InputReader in;
    private static PrintWriter out;
    static Funzone funzone = new Funzone();
    static MesinPermainan[] myMesin;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak mesin
        int N = in.nextInt();
        myMesin = new MesinPermainan[N];
        for (int i=1; i<=N; i++){
            MesinPermainan newMesin = new MesinPermainan(i);
            funzone.addMesin(newMesin);
            myMesin[i-1] = newMesin;

            // banyak skor
            int Mi = in.nextInt();
            for (int j=0; j<Mi; j++){
                int Zj = in.nextInt();
            }
        }
        funzone.posisiBudi = funzone.first;

        // banyak query
        int Q = in.nextInt();
        for (int i=1; i<=Q; i++){
            String query = in.next();

            if (query.equals("MAIN")){
                int Y = in.nextInt();
                mainmain(Y);
            } else if (query.equals("GERAK")){
                String arahGerak = in.next();
            } else if (query.equals("HAPUS")){
                int X = in.nextInt();
            } else if (query.equals("LIHAT")){
                int L = in.nextInt();
                int H = in.nextInt();
            } else if (query.equals("EVALUASI")){
                
            }
        }
        out.close();
    }
    static void mainmain(int skorBudi) {
        MesinPermainan mesinBudi = funzone.posisiBudi;
        mesinBudi.root = mesinBudi.insertSkor(mesinBudi.root, skorBudi);
        int greater = mesinBudi.greaterThanX(mesinBudi.root, skorBudi);
        int urutanSkorBudi = greater + 1;
        out.println(urutanSkorBudi);
    }

    
    // referensi: template Lab SDA biasanya
    // untuk input output
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



// Class Skor sebagai Node AVL Tree
class Skor {
    int key;
    int height;
    int numOfSkor;
    int numOfChild;
    Skor left, right;
    Skor (int key) {
        this.key = key;
        this.height = 1;
        this.numOfSkor = 1;
        this.numOfChild = 0;
        this.left = null;
        this.right = null;
    }
}

// class MesinPermainan sebagai Node LinkedList
class MesinPermainan implements Comparable<MesinPermainan> {
    int id;
    long totalSkor;
    Skor root;
    MesinPermainan next, prev;
    MesinPermainan (int id){
        this.id = id;
        this.totalSkor = 0;
        this.root = null;
        this.next = null;
        this.prev = null;
    }
    // INSERT
    Skor insertSkor(Skor node, int newSkor){
        if (node == null){
            this.totalSkor += newSkor;
            Skor theSkor = new Skor(newSkor);
            return theSkor;
        }
        if (node.key == newSkor){
            node.numOfSkor++;
            this.totalSkor += newSkor;
            return node;
        } else if (newSkor < node.key){
            node.numOfChild++;
            node.left = insertSkor(node.left, newSkor);
        } else {
            node.numOfChild++;
            node.right = insertSkor(node.right, newSkor);
        }
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balance = getBalance(node);
        // LL
        if (balance > 1 && newSkor < node.left.key){
            return rightRotate(node);
        }
        // RR
        if (balance < -1 && newSkor > node.right.key){
            return leftRotate(node);
        }
        // LR
        if (balance > 1 && newSkor > node.left.key){
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RL
        if (balance < -1 && newSkor < node.right.key){
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    // GREATER THAN X
    int greaterThanX(Skor node, int X){
        if (node == null) return 0;
        if (node.key > X) return getSize(node) - getSize(node.left) + greaterThanX(node.left, X);
        else return greaterThanX(node.right, X);
    }
    // SMALLER THAN X
    int smallerThanX(Skor node, int X){
        if (node == null) return 0;
        if (node.key < X) return getSize(node) - getSize(node.right) + smallerThanX(node.right, X);
        else return smallerThanX(node.left, X);
    }

    // GETTER
    int getHeight(Skor node){
        if (node == null) return 0;
        return node.height;
    }
    int getBalance(Skor node){
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }
    int getNumOfChild(Skor node){
        if (node == null) return 0;
        return node.numOfChild;
    }
    int getNumOfSkor(Skor node){
        if (node == null) return 0;
        return node.numOfSkor;
    }
    int getSize(Skor node){
        if (node == null) return 0;
        return getNumOfChild(node) + getNumOfSkor(node);
    }

    // ROTATE
    Skor rightRotate(Skor y) {
        Skor x = y.left;
        Skor z = x.right;
        // before rotation
        x.numOfChild = getNumOfChild(z) + getNumOfSkor(z) + getNumOfChild(x.left) + getNumOfSkor(x.left);
        y.numOfChild = getNumOfChild(x) + getNumOfSkor(x) + getNumOfChild(y.right) + getNumOfSkor(y.right);
        // Perform rotation
        x.right = y;
        y.left = z;
        // after rotation
        y.numOfChild = getNumOfChild(y.right) + getNumOfSkor(y.right) + getNumOfChild(z) + getNumOfSkor(z);
        y.numOfChild = getNumOfChild(y) - (getNumOfChild(x) + getNumOfSkor(x)) + (getNumOfChild(z) + getNumOfSkor(z));
        x.numOfChild = getNumOfChild(x.left) + getNumOfSkor(x.left) + getNumOfChild(y) + getNumOfSkor(y);
        x.numOfChild = getNumOfChild(x) - (getNumOfChild(z) + getNumOfSkor(z)) + (getNumOfChild(y) + getNumOfSkor(y));
        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
        // Return new root
        return x;
    }
    Skor leftRotate(Skor x){
        Skor y = x.right;
        Skor z = y.left;
        // before rotation
        y.numOfChild = getNumOfChild(z) + getNumOfSkor(z) + getNumOfChild(y.right) + getNumOfSkor(y.right);
        x.numOfChild = getNumOfChild(x.left) + getNumOfSkor(x.left) + getNumOfChild(y) + getNumOfSkor(y);
        // Perform rotation
        y.left = x;
        x.right = z;
        // after rotation
        x.numOfChild = getNumOfChild(x.left) + getNumOfSkor(x.left) + getNumOfChild(z) + getNumOfSkor(z);
        x.numOfChild = getNumOfChild(x) - (getNumOfChild(y) + getNumOfSkor(y)) + (getNumOfChild(z) + getNumOfSkor(z));
        y.numOfChild = getNumOfChild(x) + getNumOfSkor(x) + getNumOfChild(y.right) + getNumOfSkor(y.right);
        y.numOfChild = getNumOfChild(y) - (getNumOfChild(z) + getNumOfSkor(z)) + (getNumOfChild(x) + getNumOfSkor(x));
        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
        // Return new root
        return y;
    }

    public int compareTo(MesinPermainan other) {
        if (this.totalSkor > other.totalSkor) return -1;
        else if (this.totalSkor < other.totalSkor) return 1;
        else (this.id - other.id);
    }
}

// class Funzone sebagai LinkedList of MesinPermainan
class Funzone {
    MesinPermainan first, last, posisiBudi;
    Funzone () {
        this.first = null;
        this.last = null;
        this.posisiBudi = null;
    }
    void addMesin(MesinPermainan newMesin) {
        if (first == null) {
            this.first = newMesin;
            this.last = this.first;
        } else {
            newMesin.prev = this.last;
            this.last.next = newMesin;
            this.last = newMesin;
            this.last.next = this.first;
            this.first.prev = this.last;
        }
    }
}