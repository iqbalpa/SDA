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
    static FunZone funzone = new FunZone();
    static MesinPermainan[] myMesin;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // TODO: get the input from user

        // banyak mesin pada FunZone
        int N = in.nextInt();
        myMesin = new MesinPermainan[N];
        for (int i=1; i<=N; i++){
            MesinPermainan newMesin = new MesinPermainan(i);
            funzone.addMesinPermainan(newMesin);
            myMesin[i-1] = newMesin;

            // banyak skor awal pada mesin ke-i
            int Mi = in.nextInt();
            // System.out.println("======== mesin ke-" + i);
            for (int j=1; j<=Mi; j++){
                int Zj = in.nextInt();
                newMesin.root = newMesin.insertSkor(newMesin.root, Zj);
                // System.out.println("======= " + newMesin.root.skor);
            }
        }
        funzone.posisiBudi = funzone.first;

        // banyak query
        int Q = in.nextInt();
        for (int i=1; i<=Q; i++){
            String query = in.next();

            // cek jenis query
            if (query.equals("MAIN")){
                // skor Budi
                int Y = in.nextInt();
                mainmain(Y);
            }
            else if (query.equals("GERAK")){
                // arah gerak Budi
                String arahGerak = in.next();
                gerak(arahGerak);
            }
            else if (query.equals("HAPUS")) {
                // banyak skor yg mau dihapus
                int X = in.nextInt();
                hapus(X);
            }
            else if (query.equals("LIHAT")) {
                // batas bawah skor
                int L = in.nextInt();
                // batas atas skor
                int H = in.nextInt();
                lihat(L, H);
            }
            else if (query.equals("EVALUASI")){
                evaluasi();
            }
        }
        
        out.close();
    }

    // method untuk handle masing-masing query
    // query MAIN
    static void mainmain(int skorBudi){
        MesinPermainan mesinBudi = funzone.posisiBudi;
        mesinBudi.root = mesinBudi.insertSkor(mesinBudi.root, skorBudi);
        int greater = mesinBudi.greaterThanX(mesinBudi.root, skorBudi);
        int urutanSkorBudi = greater + 1;
        out.println(urutanSkorBudi);
    }

    // query GERAK
    static void gerak(String arahGerak){
        if (arahGerak.equals("KANAN")){
            funzone.posisiBudi = funzone.posisiBudi.next;
        }
        else if (arahGerak.equals("KIRI")){
            funzone.posisiBudi = funzone.posisiBudi.prev;
        }
        out.println(funzone.posisiBudi.id);
    }

    // query HAPUS
    static void hapus(int banyakSkor){
        // TODO create method getSum and create sum attribute to each Skor node
        long sumOfSkor = 0;
        Skor rootNode = funzone.posisiBudi.root;

        MesinPermainan mesinBudi = funzone.posisiBudi;
        int banyakSkorDiMesin = mesinBudi.getCount(mesinBudi.root);
        if (banyakSkorDiMesin <= banyakSkor) {
            mesinBudi.root = null;
        } else {
            for (int i=0; i<banyakSkor; i++){
                Skor maxSkor = mesinBudi.findMax(mesinBudi.root);
                if (maxSkor.jumlahSkor > 1){
                    sumOfSkor += maxSkor.skor;
                    maxSkor.jumlahSkor--;
                } else {
                    sumOfSkor += maxSkor.skor;
                    mesinBudi.root = mesinBudi.deleteSkor(mesinBudi.root, maxSkor.skor);
                }
            }

            if (mesinBudi == funzone.last){
                funzone.posisiBudi = funzone.first;
            } else if (funzone.first == funzone.last){
                // do nothing
            } else {
                funzone.posisiBudi = funzone.posisiBudi.next;
                // pindah mesinBudi ke paling kanan
                mesinBudi.prev.next = mesinBudi.next;
                mesinBudi.next.prev = mesinBudi.prev;
                funzone.last.next = mesinBudi;
                mesinBudi.prev = funzone.last;
                funzone.last.next = funzone.first;
                funzone.first.prev = funzone.last;
            }
        }
        out.println(sumOfSkor);
    }

    // query LIHAT
    static void lihat(int batasBawah, int batasAtas){
        int greaterThanL = funzone.posisiBudi.greaterThanX(funzone.posisiBudi.root, batasBawah);
        int greaterThanH = funzone.posisiBudi.greaterThanX(funzone.posisiBudi.root, batasAtas);
        int banyakSkor = greaterThanH - greaterThanL;
        out.println(banyakSkor);
    }

    // query EVALUASI
    static void evaluasi(){}


    // create method for merge sort the array myMesin using its compareTo method
    static void mergeSort(MesinPermainan[] arr, int l, int r){
        if (l < r){
            int m = (l+r)/2;
            mergeSort(arr, l, m);
            mergeSort(arr, m+1, r);
            merge(arr, l, m, r);
        }
    }
    static void merge(MesinPermainan[] arr, int l, int m, int r){
        int n1 = m - l + 1;
        int n2 = r - m;
        MesinPermainan[] L = new MesinPermainan[n1];
        MesinPermainan[] R = new MesinPermainan[n2];
        for (int i=0; i<n1; i++){
            L[i] = arr[l+i];
        }
        for (int j=0; j<n2; j++){
            R[j] = arr[m+1+j];
        }
        int i=0, j=0;
        int k = l;
        while (i < n1 && j < n2){
            if (L[i].compareTo(R[j]) <= 0){
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1){
            arr[k] = L[i];
            i++;
            k++;
        }
        while (j < n2){
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

// class untuk node skor di dalam mesin permainan
class Skor {
    int skor, jumlahSkor;
    int height, count;
    int sumOfSubtree;
    Skor left, right;
    Skor (int skor) {
        this.skor = skor;
        this.jumlahSkor = 1;
        this.left = null;
        this.right = null;
        this.height = 1;
        this.count = 1;
        this.sumOfSubtree = skor;
    }
}

// class untuk mesin permainan
// mesin permainan sebagai AVL tree
class MesinPermainan implements Comparable<MesinPermainan> {
    int id;
    Skor root;
    int currentCount;
    MesinPermainan next, prev;
    MesinPermainan(int id){
        this.id = id; 
    }

    // ============== GETTER ==============
    int getHeight(Skor node){
        if (node == null) return 0;
        return node.height;
    }
    int getBalance(Skor node){
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
    int getCount(Skor node){
        if (node == null) return 0;
        return node.count + (node.jumlahSkor - 1);
    }
    int getSumOfSubtree(Skor node){
        if (node == null) return 0;
        return node.sumOfSubtree * node.jumlahSkor;
    }

    // =============== ROTATE ===============
    Skor rightRotate(Skor y) {
        Skor x = y.left;
        Skor z = x.right;
        x.right = y;
        y.left = z;
        // update height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        // update size
        y.count = getCount(y) + getCount(z);
        y.count = getCount(y) - getCount(x) + getCount(z);
        x.count = getCount(x.left) + getCount(y);
        x.count = getCount(x) - getCount(z) + getCount(y);
        return x;
    }
    Skor leftRotate(Skor x) {
        // TODO: implement left rotate
        Skor y = x.right;
        Skor z = y.left;
        y.left = x;
        x.right = z;
        // update height
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        // update size
        x.count = getCount(x) + getCount(z);
        x.count = getCount(x) - getCount(y) + getCount(z);
        y.count = getCount(y.right) + getCount(x);
        y.count = getCount(y) - getCount(z) + getCount(x);
        return y;
    }

    // ================ INSERT =================
    Skor insertSkor(Skor node, int newSkor){
        if (node == null){
            return new Skor(newSkor);
        }
        if (node.skor == newSkor) {
            if (node.left != null) {
                currentCount += getCount(node.left);
            }
            node.jumlahSkor++;
            return node;
        }
        if (node.skor > newSkor) {
            node.count++;
            node.left = insertSkor(node.left, newSkor);
        }
        else {
            if (node.left != null){
                currentCount += getCount(node.left);
            }
            currentCount += node.jumlahSkor;
            node.count++;
            node.right = insertSkor(node.right, newSkor);
        }

        // balancing
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balanceFactor = getBalance(node);
        if (balanceFactor > 1) {
            if (newSkor < node.left.skor) {
                return rightRotate(node);
            } else if (newSkor > node.left.skor) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balanceFactor < -1) {
            if (newSkor > node.right.skor) {
                return leftRotate(node);
            } else if (newSkor < node.right.skor){
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }

    // ============== DELETE ==============
    Skor findMax(Skor node){
        if (node.right == null) return node;
        return findMax(node.right);
    }
    Skor minValueSkor(Skor node){
        Skor current = node;
        while (current.left != null){
            current = current.left;
        }
        return current;
    }
    Skor deleteSkor(Skor node, int skor){
        if (node == null) return node;
        if (skor < node.skor) {
            node.count--;
            node.left = deleteSkor(node.left, skor);
        }
        else if (skor > node.skor) {
            node.count--;
            node.right = deleteSkor(node.right, skor);
        }
        else {
            if (node.left == null || node.right == null) {
                Skor temp = null;
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
                }
            } else {
                Skor temp = minValueSkor(node.right);
                node.skor = temp.skor;
                node.right = deleteSkor(node.right, temp.skor);
            }
        }
        if (node == null) return node;
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
        return node;
    }

    // ============== LOWER UPPER ==============
    Skor lowerBound(Skor node, int batasBawah){
        if (node == null) return null;
        if (node.skor == batasBawah) return node;
        if (node.skor < batasBawah) return lowerBound(node.right, batasBawah);
        Skor temp = lowerBound(node.left, batasBawah);
        return (temp.skor >= batasBawah) ? temp : node;
    }
    Skor upperBound(Skor node, int batasAtas){
        if (node == null) return null;
        if (node.skor == batasAtas) return node;
        if (node.skor > batasAtas) return upperBound(node.left, batasAtas);
        Skor temp = upperBound(node.right, batasAtas);
        return (temp.skor <= batasAtas) ? temp : node;
    }
    int greaterThanX(Skor node, int x){
        if (node == null) return 0;
        if (node.skor == x) return getCount(node.right);
        if (node.skor > x) return getCount(node.right) + node.jumlahSkor + greaterThanX(node.left, x);
        return greaterThanX(node.right, x);
    }

    // override compareTo
    public int compareTo(MesinPermainan other){
        if (this.root.sumOfSubtree > other.root.sumOfSubtree) return 1;
        if (this.root.sumOfSubtree < other.root.sumOfSubtree) return -1;
        else return this.id - other.id;
    }
}

// FunZone sebagai kumpulan MesinPermainan as LinkedList class
class FunZone {
    MesinPermainan first, last, posisiBudi;
    FunZone(){
        this.first = null;
        this.last = null;
        this.posisiBudi = null; // buat posisi Budi
    }
    void addMesinPermainan(MesinPermainan newMesin){
        if (first == null){
            first = newMesin;
            last = first;
        } else if (first == last){
            newMesin.prev = last;
            last = newMesin;
            first.next = last;
            last.next = first;
            first.prev = last;
        } else {
            newMesin.prev = last;
            last = newMesin;
            last.next = first;
            first.prev = last;
        }
    }
}
