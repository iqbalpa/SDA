import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.*;

// Ideas by:
// Fresty Tania S
// Diah Afia S
// M Ferry Husnil A
// Collab with:
// Ibni Shaquille S

public class Lab6 {
    private static InputReader in;
    static PrintWriter out;
    static Saham[] daftarSaham = new Saham[400012];
    static Saham[] sahamById = new Saham[400012];
    static MaxHeap maxheap;
    static MinHeap minheap;
    static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
 
        // banyak saham
        N = in.nextInt();
        for (int i=1; i<=N; i++) {
            int Ci = in.nextInt();
            Saham newSaham = new Saham(i, Ci);
            daftarSaham[i] = newSaham;
            sahamById[i] = newSaham;
        }

        // sort saham
        if (N > 1) {
            Arrays.sort(daftarSaham, 1, N+1);
        }
        // Arrays.sort(daftarSaham, 1, N+1);
        int batasAtasMaxHeap = N/2;
        int batasBawahMinHeap = N/2 + 1;
        // masukkan 1/2 termurah ke maxheap
        maxheap = new MaxHeap();
        for (int i=1; i<=batasAtasMaxHeap; i++) {
            maxheap.insert(daftarSaham[i]);
        }
        // masukkan 1/2 tertinggi ke minheap
        minheap = new MinHeap();
        for (int i=batasBawahMinHeap; i<=N; i++) {
            minheap.insert(daftarSaham[i]);
        }

        // printArray();
        // printAll();

        // banyak query
        int Q = in.nextInt();
        for (int i=0; i<Q; i++){
            String query = in.next();
            if (query.equals("TAMBAH")){
                int C = in.nextInt();
                TAMBAH(C);
            }
            else if (query.equals("UBAH")){
                int X = in.nextInt();
                int C = in.nextInt();
                UBAH(X, C);
            }
        }
        out.close();
    }
    static void TAMBAH(int C){
        N++;
        // out.println(N);
        Saham newSaham = new Saham(N, C);
        // out.println(newSaham);
        daftarSaham[N] = newSaham;
        sahamById[N] = newSaham;
        // O(1)
        // O(logN) 
        if (N == 1){
            minheap.insert(newSaham);
            out.println(newSaham.seri);
        } else {
            Saham minSaham = minheap.getMin();
            if (newSaham.harga < minSaham.harga){
                maxheap.insert(newSaham);
            } else if (newSaham.harga > minSaham.harga) {
                minheap.insert(newSaham);
            } else {
                if (newSaham.seri < minSaham.seri){
                    maxheap.insert(newSaham);
                } else {
                    minheap.insert(newSaham);
                }
            }
            // O(2logN)
            if (getDiffSize() > 1){
                Saham theSaham = minheap.removeMin();
                maxheap.insert(theSaham);
            } 
            if (getDiffSize() <= -1){
                Saham theSaham = maxheap.removeMax();
                minheap.insert(theSaham);
            }
            // printAll();
            // O(1)
            minSaham = minheap.getMin();
            out.println(minSaham.seri);
        }
    }
    static void UBAH(int X, int C){
        Saham theSaham = sahamById[X];
        if (theSaham.isInMaxheap){
            maxheap.remove(theSaham.posisiMaxheap);
        } else {
            minheap.remove(theSaham.posisiMinheap);
        }
        // System.out.println("============= sebelum insert ulang ==================");
        theSaham.harga = C;
        // printAll();
        // O(1)
        Saham minSaham = minheap.getMin();
        // O(logN)
        if (theSaham.harga < minSaham.harga){
            maxheap.insert(theSaham);
        } else if (theSaham.harga > minSaham.harga) {
            minheap.insert(theSaham);
        } else {
            if (theSaham.seri < minSaham.seri){
                maxheap.insert(theSaham);
            } else {
                minheap.insert(theSaham);
            }
        }
        // O(2logN)
        if (getDiffSize() > 1){
            Saham min = minheap.removeMin();
            maxheap.insert(min);
        }
        if (getDiffSize() <= -1){
            Saham max = maxheap.removeMax();
            minheap.insert(max);
        }
        // System.out.println("========== setelah insert ulang ==================");
        // printAll();
        // O(1)
        minSaham = minheap.getMin();
        out.println(minSaham.seri);
    }

    static int getDiffSize(){
        return minheap.tail - maxheap.tail;
    }

    static void printAll(){
        System.out.println("=============================");
        maxheap.printMaxheap();
        minheap.printMinheap();
        System.out.println("=============================");
    }
    static void printArray(){
        System.out.print(">>>>>>>>>>>>>>> order yg bener: ");
        for (int i=1; i<=N; i++){
            System.out.print(daftarSaham[i].harga + " ");
        }
        System.out.println();
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


class Saham implements Comparable<Saham> {
    int seri, harga;
    boolean isInMinheap, isInMaxheap;
    int posisiMinheap, posisiMaxheap;
    Saham (int seri, int harga){
        this.seri = seri;
        this.harga = harga;
        this.posisiMinheap = -999;
        this.posisiMaxheap = -999;
        this.isInMinheap = false;
        this.isInMaxheap = false;
    }
    public int compareTo(Saham o){
        if (this.harga < o.harga) return -1;
        else if (this.harga > o.harga) return 1;
        else return this.seri - o.seri;
    }
}

// * 1/2 TERMAHAL
class MinHeap {
    Saham[] heap;
    int tail;
    MinHeap(){
        this.tail = 0;
        heap = new Saham[200012];
    }
    int parent(int i){
        return (i-1)/2;
    }
    int leftChild(int i){
        return 2*i+1;
    }
    int rightChild(int i){
        return 2*i+2;
    }
    // get saham paling murah di 1/2 termahal
    Saham getMin(){
        return heap[0];
    }
    void swap(int i, int j){
        Saham temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        // update posisi
        heap[i].posisiMinheap = i;
        heap[j].posisiMinheap = j;
    }
    void percolateUp(int i){
        if (i==0) return;
        if (heap[parent(i)].compareTo(heap[i]) > 0){
            swap(i, parent(i));
            percolateUp(parent(i));
        }
    }
    void percolateDown(int i){
        int min = i;
        if (leftChild(i) < tail && heap[leftChild(i)].compareTo(heap[min]) < 0) min = leftChild(i);
        if (rightChild(i) < tail && heap[rightChild(i)].compareTo(heap[min]) < 0) min = rightChild(i);
        if (min != i){
            swap(i, min);
            percolateDown(min);
        }
    }
    void insert(Saham saham){
        heap[tail] = saham;
        saham.posisiMinheap = tail;
        saham.isInMinheap = true;
        saham.isInMaxheap = false;
        tail++;
        percolateUp(tail-1); //percolate up newest element
    }
    Saham removeMin(){
        Saham min = heap[0];
        if (tail > 0){
            heap[0] = heap[tail-1];
            tail--;
            percolateDown(0);
        }
        // min.posisiMinheap = -999;
        return min;
    }
    Saham remove(int i){
        Saham saham = heap[i];
        heap[i] = heap[tail-1];
        tail--;
        percolateDown(i);
        // saham.posisiMinheap = -999;
        return saham;
    }
    void printMinheap(){
        System.out.print("minheap: ");
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].harga + " ");
        } 
        System.out.println();
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].seri + " ");
        } 
        System.out.println();
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].isInMinheap + " ");
        }
        System.out.println();
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].posisiMinheap + " ");
        }
        System.out.println();
        System.out.println(">>> size: " + tail);
        // find min in minheap.heap
        int min = Integer.MAX_VALUE;
        for (int i=0; i<tail; i++){
            if (heap[i].harga < min) min = heap[i].harga;
        }
        System.out.println("<<< min: " + min + " T " + heap[0].harga);
    }

}
// * 1/2 TERMURAH
class MaxHeap {
    Saham[] heap;
    int tail;
    MaxHeap(){
        this.tail = 0;
        heap = new Saham[200012];
    }
    int parent(int i){
        return (i-1)/2;
    }
    int leftChild(int i){
        return 2*i+1;
    }
    int rightChild(int i){
        return 2*i+2;
    }
    // get saham paling mahal di 1/2 termurah
    Saham getMax(){
        return heap[0];
    }
    void swap(int i, int j){
        Saham temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        // update posisi
        heap[i].posisiMaxheap = i;
        heap[j].posisiMaxheap = j;
    }
    void percolateUp(int i){
        if (i==0) return;
        if (heap[parent(i)].compareTo(heap[i]) < 0){
            swap(i, parent(i));
            percolateUp(parent(i));
        }
    }
    void percolateDown(int i){
        int max = i;
        if (leftChild(i) < tail && heap[leftChild(i)].compareTo(heap[max]) > 0) max = leftChild(i);
        if (rightChild(i) < tail && heap[rightChild(i)].compareTo(heap[max]) > 0) max = rightChild(i);
        if (max != i){
            swap(i, max);
            percolateDown(max);
        }
    }
    void insert(Saham saham){
        heap[tail] = saham;
        saham.posisiMaxheap = tail;
        saham.isInMaxheap = true;
        saham.isInMinheap = false;
        tail++;
        percolateUp(tail-1); // percolate up newest element
    }
    Saham removeMax(){
        Saham max = heap[0];
        if (tail > 0){
            heap[0] = heap[tail-1];
            tail--;
            percolateDown(0);
        }
        // max.posisiMaxheap = -999;
        return max;
    }
    Saham remove(int i){
        Saham saham = heap[i];
        heap[i] = heap[tail-1];
        tail--;
        percolateDown(i);
        // saham.posisiMaxheap = -999;
        return saham;
    }

    
    void printMaxheap(){
        System.out.print("maxheap: ");
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].harga + " ");
        }
        System.out.println();
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].seri + " ");
        }
        System.out.println();
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].isInMaxheap + " ");
        }
        System.out.println();
        for (int i=0; i<tail; i++){
            System.out.print(heap[i].posisiMaxheap + " ");
        }
        System.out.println();
        System.out.println(">>> size: " + tail);
        // find max in maxheap.heap
        int max = -999;
        for (int i=0; i<tail; i++){
            if (heap[i].harga > max) max = heap[i].harga;
        }
        System.out.println("<<< max: " + max + " T " + heap[0].harga);
    }
}
