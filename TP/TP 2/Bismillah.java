import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// Ideas by:
// M Ferry Husnil A
// Aushaaf F
// Collab with:
// Ibni Shaquille S
// Fresty Tania S

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
                // * menambahkan totalSkor di mesin
                newMesin.root = newMesin.insertSkor(newMesin.root, Zj);
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
                gerak(arahGerak);
            } else if (query.equals("HAPUS")){
                int X = in.nextInt();
                hapus(X);
            } else if (query.equals("LIHAT")){
                int L = in.nextInt();
                int H = in.nextInt();
                lihat(L,H);
            } else if (query.equals("EVALUASI")){
                evaluasi();
            }
        }
        out.close();
    }
    
    static void mainmain(int skorBudi) {
        MesinPermainan mesinBudi = funzone.posisiBudi;
        // * menambahkan totalSkor di mesin
        mesinBudi.root = mesinBudi.insertSkor(mesinBudi.root, skorBudi);
        int greater = mesinBudi.greaterThanX(mesinBudi.root, skorBudi);
        int urutanSkorBudi = greater + 1;
        out.println(urutanSkorBudi);
    }
    static void gerak(String arahGerak){
        if (arahGerak.equals("KANAN")){
            funzone.posisiBudi = funzone.posisiBudi.next;
        } else if (arahGerak.equals("KIRI")){
            funzone.posisiBudi = funzone.posisiBudi.prev;
        }
        out.println(funzone.posisiBudi.id);
    }
    static void lihat(int L, int H){
        MesinPermainan mesinBudi = funzone.posisiBudi;
        int smallerThanL = mesinBudi.smallerThanX(mesinBudi.root, L);
        int greaterThanH = mesinBudi.greaterThanX(mesinBudi.root, H);
        int banyakSkor = mesinBudi.getSize(mesinBudi.root) - smallerThanL - greaterThanH;
        out.println(banyakSkor);
    }
    static void evaluasi(){
        int idMesinBudi = funzone.posisiBudi.id;
        mergeSort(myMesin, 0, myMesin.length-1);
        funzone.first = null;
        funzone.last = null;

        int posisiBudi = idMesinBudi;
        for (int i=1; i<=myMesin.length; i++){
            funzone.addMesin(myMesin[i-1]);
            if (myMesin[i-1].id == idMesinBudi){
                posisiBudi = i;
            }
        }
        out.println(posisiBudi);
    }
    
    static void hapus(int X) {
        long sumOfSkor = 0;
        MesinPermainan mesinBudi = funzone.posisiBudi;
        // get banyak skor di mesin 
        int banyakSkorDiMesin = mesinBudi.getSize(mesinBudi.root);
        // mesin rusak
        if (banyakSkorDiMesin <= X) {
            for (int i=0; i<banyakSkorDiMesin; i++) {
                Skor maxSkor = mesinBudi.findMax(mesinBudi.root);
                // * mengurangi totalSkor di mesin
                sumOfSkor += maxSkor.key;
                mesinBudi.root = mesinBudi.deleteSkor(mesinBudi.root, maxSkor.key);
            }
            if (mesinBudi == funzone.last) {
                funzone.posisiBudi = funzone.first;
            } else if (funzone.first == funzone.last) {
                // do nothing
            } else {
                funzone.posisiBudi = mesinBudi.next;
                if (mesinBudi == funzone.first) funzone.first = mesinBudi.next;
                // move mesin into the last position
                mesinBudi.prev.next = mesinBudi.next;
                mesinBudi.next.prev = mesinBudi.prev;
                funzone.last.next = mesinBudi;
                mesinBudi.prev = funzone.last;
                funzone.last = mesinBudi;
                funzone.last.next = funzone.first;
                funzone.first.prev = funzone.last;
            }
        } 
        // mesin tidak rusak (hapus beberapa skor)
        else {
            for (int i=0; i<X; i++){
                Skor maxSkor = mesinBudi.findMax(mesinBudi.root);
                sumOfSkor += maxSkor.key;
                // * mengurangi totalSkor di mesin
                mesinBudi.root = mesinBudi.deleteSkor(mesinBudi.root, maxSkor.key);
            }
        }
        out.println(sumOfSkor);
    }


    // MERGE SORT: GEEKS FOR GEEKS
    static void mergeSort(MesinPermainan[] arr, int l, int r) { 
        if (l < r) { 
            int m = (l+r)/2; 
            mergeSort(arr, l, m); 
            mergeSort(arr, m+1, r); 
            merge(arr, l, m, r); 
        } 
    }
    static void merge(MesinPermainan[] arr, int l, int m, int r) { 
        int n1 = m - l + 1; 
        int n2 = r - m; 
        MesinPermainan L[] = new MesinPermainan [n1]; 
        MesinPermainan R[] = new MesinPermainan [n2]; 
        for (int i=0; i<n1; ++i) L[i] = arr[l + i]; 
        for (int j=0; j<n2; ++j) R[j] = arr[m + 1+ j]; 
        int i = 0, j = 0; 
        int k = l; 
        while (i < n1 && j < n2) { 
            if (L[i].compareTo(R[j]) <= 0) { 
                arr[k] = L[i]; 
                i++; 
            } else { 
                arr[k] = R[j]; 
                j++; 
            } 
            k++; 
        } 
        while (i < n1) { 
            arr[k] = L[i]; 
            i++; 
            k++; 
        } 
        while (j < n2) { 
            arr[k] = R[j]; 
            j++; 
            k++; 
        } 
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


class Skor {
    int key, height, size, numOfSkor;
    Skor left, right;
    Skor (int key) {
        this.key = key;
        this.height = 1;
        this.size = 1;
        this.numOfSkor = 1;
    }
}

// class MesinPermainan sebagai Node LinkedList
class MesinPermainan implements Comparable<MesinPermainan> {
    int id;
    Skor root;
    MesinPermainan next, prev;
    MesinPermainan (int id){
        this.id = id;
        this.root = null;
        this.next = null;
        this.prev = null;
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
    int getSize(Skor node){
        if (node == null) return 0;
        return node.size;
    }
    Skor rightRotate(Skor node){
        Skor left = node.left;
        Skor right = left.right;
        left.right = node;
        node.left = right;
        // update height & size
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node.size = node.numOfSkor + getSize(node.left) + getSize(node.right);
        left.size = left.numOfSkor + getSize(left.left) + getSize(left.right);
        left.height = Math.max(getHeight(left.left), getHeight(left.right)) + 1;

        return left;
    }
    Skor leftRotate(Skor node){
        Skor right = node.right;
        Skor left = right.left;
        right.left = node;
        node.right = left;
        // update height & size
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node.size = node.numOfSkor + getSize(node.left) + getSize(node.right);
        right.size = right.numOfSkor + getSize(right.left) + getSize(right.right);
        right.height = Math.max(getHeight(right.left), getHeight(right.right)) + 1;

        return right;
    }
    // insert new score
    Skor insertSkor(Skor node, int key) {
        if (node == null) {
            return new Skor(key);
        }
        if (key < node.key){
            node.left = insertSkor(node.left, key);
        } else if (key > node.key) {
            node.right = insertSkor(node.right, key);
        } else {
            // handle duplicate score
            node.numOfSkor++;
            node.size++;
            return node;
        }
        // update height & size
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        node.size = node.numOfSkor + getSize(node.left) + getSize(node.right);
        // balancing
        int balance = getBalance(node);
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }   
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    } 
    // get number of elements that greater than x (exclusive)
    int greaterThanX(Skor node, int x){
        if (node == null) return 0;
        if (node.key > x) return getSize(node) - getSize(node.left) + greaterThanX(node.left, x);
        else return greaterThanX(node.right, x);
    }
    // get number of elements that less than x (exclusive)
    int smallerThanX(Skor node, int x){
        if (node == null) return 0;
        if (node.key < x) return getSize(node) - getSize(node.right) + smallerThanX(node.right, x);
        else return smallerThanX(node.left, x);
    }
    // get the max score
    Skor findMax(Skor node){
        if (node.right == null) return node;
        return findMax(node.right);
    }
    // get the min score in the subtree (successor)
    Skor minValueNode(Skor node) {
        Skor current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    // delete the node with the given key
    Skor deleteSkor(Skor node, int key){
        if (node == null) return node;
        if (key < node.key){
            node.left = deleteSkor(node.left, key);
        } else if (key > node.key) {
            node.right = deleteSkor(node.right, key);
        } else {
            // handle duplicate score
            if (node.numOfSkor > 1) {
                node.numOfSkor--;
                node.size--;
            } else {
                if ((node.left == null) || (node.right == null)){
                    Skor temp = null;
                    if (temp == node.left) temp = node.left;
                    else temp = node.left;

                    if (temp == null) {
                        temp = node;
                        node = null;
                    } else {
                        node = temp;
                    }
                } else {
                    Skor temp = minValueNode(node);
                    node.key = temp.key;
                    node.right = deleteSkor(node.right, temp.key);
                }
            }
        }
        if (node == null) return node;
        // update height & size
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        node.size = node.numOfSkor + getSize(node.left) + getSize(node.right);
        // balancing
        int balance = getBalance(node);
        if (balance > 1){
            if (getBalance(node.left) >= 0){
                return rightRotate(node);
            } else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balance < -1){
            if (getBalance(node.right) <= 0){
                return leftRotate(node);
            } else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }
    // override compareTo
    public int compareTo(MesinPermainan other) {
        if (this.getSize(this.root) > other.getSize(other.root)) return -1;
        else if (this.getSize(this.root) < other.getSize(other.root)) return 1;
        else return (this.id - other.id);
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
    // add new mesin
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