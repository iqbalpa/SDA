import java.util.*;
import java.io.*;

// Ideas by 
// 1. Aushaaf Fadhilah A
// 2. M ferry Husnil A
// Collaborators with
// 1. Laela Putri Salsa B
// 2. M Vicky Maulana

public class TP1 {
    private static Reader in;
    private static PrintWriter out;

    static int M;
    static int V;

    static Menu[] arrMenu;
    static Pelanggan[] arrPelanggan;
    static TreeSet<Koki> allKoki = new TreeSet<>();
    static TreeSet<Koki> kokiA = new TreeSet<>();
    static TreeSet<Koki> kokiG = new TreeSet<>();
    static TreeSet<Koki> kokiS = new TreeSet<>();
    static Queue<Pesanan> pesanan = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        in = new Reader();
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // banyak menu
        M = in.nextInt();
        // array of menu
        arrMenu = new Menu[M+1];
        for (int i = 1; i <= M; i++) {
            int harga = in.nextInt();
            char tipe = in.nextChar();
            Menu newMenu = new Menu(harga, tipe);
            arrMenu[i] = newMenu;
        }
        
        // banyak koki
        V = in.nextInt();
        for (int i = 1; i <= V; i++) {
            char spesialisasi =  in.nextChar();
            Koki newKoki = new Koki(spesialisasi, i);
            allKoki.add(newKoki);
            // menambahkan koki ke treeset yg sesuai
            if (spesialisasi == 'A'){
                kokiA.add(newKoki);
            } else if (spesialisasi == 'G'){
                kokiG.add(newKoki);
            } else {
                kokiS.add(newKoki);
            }
        }

        // banyak pelanggan
        int P = in.nextInt();
        // array pelanggan
        arrPelanggan = new Pelanggan[P+1];
        for (int i=1; i<=P; i++){
            arrPelanggan[i] = new Pelanggan(i);
        }
        // banyak kursi
        int N = in.nextInt();
        // jumlah hari restoran beroperasi
        int Y = in.nextInt();

        // pelayanan dari hari pertama sampai hari ke-Y
        for (int i = 0; i < Y; i++) {
            // banyak pelanggan pada hari ke-i
            int jmlPelanggan = in.nextInt();
            // array pelanggan perhari
            Pelanggan[] arrPelangganPerHari = new Pelanggan[jmlPelanggan];

            // jumlah yang sedang makan di kursi restoran
            int sedangMakan = 0;
            
            // memoisasi untuk menyimpan jumlah positif
            Integer[] positifMemo = new Integer[jmlPelanggan];
            int positif = 0;

            // pelanggan pada hari ke-i
            for (int j=0; j<jmlPelanggan; j++){
                int I = in.nextInt();   
                char K = in.nextChar();          
                int U = in.nextInt(); 

                // set uang pelanggan
                arrPelanggan[I].uang = U;
                arrPelangganPerHari[j] = arrPelanggan[I];

                // jika status ? maka dilakukan advance scanning
                if (K == '?'){
                    int R = in.nextInt();
                    int awal = j-R-1;
                    int akhir = j-1;
                    int pos = 0;
                    if (awal < 0){
                        pos = positifMemo[akhir];
                    } else {
                        pos = positifMemo[akhir] - positifMemo[awal];
                    }
                    int neg = R - pos;
                    K = (neg < pos) ? '+' : '-';
                }
                // status ? diubah sesuai hasil advance scanning
                arrPelanggan[I].status = K;

                // jika status +
                if (K == '+'){
                    positif++;
                }

                // jika pelanggan di blacklist
                if (arrPelanggan[I].isBlacklist){
                    out.print(3 + " ");
                }
                // jika pelanggan positif 
                else if (K == '+'){
                    out.print(0 + " ");
                } 
                // jika pelanggan negatif
                else {
                    // jika masih ada kursi kosong
                    if (sedangMakan < N){
                        out.print(1 + " ");
                        sedangMakan++;
                    } 
                    // pelanggan masuk ruang lapar
                    else {
                        out.print(2 + " ");
                    }
                }
                // simpan jml positif ke memo
                positifMemo[j] = positif;
            }
            out.println();

            // jumlah pelayanan pada hari ke-i
            int jmlPelayanan = in.nextInt();
            // fungsi pelayanan
            for (int j=0; j<jmlPelayanan; j++){
                int satu;
                int dua;
                int tiga;
                char pelayanan = in.nextChar();
                if (pelayanan == 'P'){
                    satu = in.nextInt();
                    dua = in.nextInt();
                    P(satu, dua);
                } else if (pelayanan == 'L'){
                    L();
                } else if (pelayanan == 'B'){
                    satu = in.nextInt();
                    B(satu);
                } else if (pelayanan == 'C'){
                    satu = in.nextInt();
                    C(satu);
                } else if (pelayanan == 'D'){
                    satu = in.nextInt();
                    dua = in.nextInt();
                    tiga = in.nextInt();
                }
            }
        }
        out.close();
    }

    // function untuk query P
    public static void P(int idPelanggan, int indexMakanan){
        Menu theMenu = arrMenu[indexMakanan];
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        Koki theKoki;
        // cek menu untuk mendapatkan koki dengan spesialisasi yang sesuai
        if (theMenu.tipe == 'A'){
            theKoki = kokiA.first();
        } else if (theMenu.tipe == 'G'){
            theKoki = kokiG.first();
        } else {
            theKoki = kokiS.first();
        }

        // tagihan pelanggan bertambah
        thePelanggan.tagihan += theMenu.harga;
        // bikin objek pesanan baru
        Pesanan newPesanan = new Pesanan(theMenu, theKoki, thePelanggan);
        // add newpesanan ke queue of pesanan
        pesanan.add(newPesanan);
        out.println(theKoki.id);
    }

    // function untuk query L
    public static void L(){
        Pesanan thePesanan = pesanan.poll();
        Koki theKoki = thePesanan.koki;
        allKoki.remove(theKoki);
        // cek spesialisasi koki dan update jml melayaninya
        if (theKoki.spesialisasi == 'A'){
            kokiA.remove(theKoki);
            theKoki.jumlahMelayani++;
            kokiA.add(theKoki);
        } else if (theKoki.spesialisasi == 'G'){
            kokiG.remove(theKoki);
            theKoki.jumlahMelayani++;
            kokiG.add(theKoki);
        } else {
            kokiS.remove(theKoki);
            theKoki.jumlahMelayani++;   
            kokiS.add(theKoki);
        }
        allKoki.add(theKoki);
        out.println(thePesanan.pelanggan.id); 
    }
    // function untuk query B
    public static void B(int idPelanggan){
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        if (thePelanggan.uang < thePelanggan.tagihan){
            out.println(0);
            thePelanggan.isBlacklist = true;
        } else {
            out.println(1);
            thePelanggan.tagihan = 0;
        }
    }
    // function untuk query C
    public static void C(int Q){
        Iterator<Koki> it = allKoki.iterator();
        while (it.hasNext() && Q > 0){
            out.print(it.next().id + " ");
            Q--;
        }
        out.println();
    }
    public static void D(int A, int G, int S){
        int total = 0;
        for (int i=1; i<=M; i++){
            total += arrMenu[i].harga;
        }
        out.println(total);
    }


    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(
                    new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readWord() throws IOException {
            byte[] buf = new byte[1005]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n' || c == ' ') {
                    if (cnt != 0) {
                        break;
                    } else {
                        continue;
                    }
                }
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public char nextChar() throws IOException {
            byte c = read();
            while (c <= ' ') {
                c = read();
            }

            return (char) c;
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') {
                c = read();
            }
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg)
                return -ret;
            return ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            if (neg)
                return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if (neg)
                c = read();

            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg)
                return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0,
                    BUFFER_SIZE);
            if (bytesRead == -1)
                buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead)
                fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null)
                return;
            din.close();
        }
    }
}

class Menu {
    int harga;
    char tipe;
    public Menu(int harga, char tipe) {
        this.harga = harga;
        this.tipe = tipe;
    }
}

class Pesanan {
    Menu menu;
    Koki koki;
    Pelanggan pelanggan;
    public Pesanan(Menu menu, Koki koki, Pelanggan pelanggan){
        this.menu = menu;
        this.koki = koki;
        this.pelanggan = pelanggan;
    }
}

class Pelanggan {
    int id; // id pelanggan
    char status; // status kesehatan
    int uang; // jumlah uang
    long tagihan;
    boolean isBlacklist;
    public Pelanggan(int id) {
        this.id = id;
        this.isBlacklist = false;
        this.tagihan = 0;
    }
}

class Koki implements Comparable<Koki> {
    char spesialisasi;
    int jumlahMelayani;
    int id;
    public Koki(char spesialisasi, int id){
        this.spesialisasi = spesialisasi;
        this.jumlahMelayani = 0;
        this.id = id;
    }
    @Override
    public int compareTo(Koki k){
        if (this.jumlahMelayani > k.jumlahMelayani){
            return 1;
        } else if (this.jumlahMelayani < k.jumlahMelayani){
            return -1;
        } else {
            if (this.spesialisasi > k.spesialisasi){
                return -1;
            } else if (this.spesialisasi < k.spesialisasi){
                return 1;
            } else {
                return this.id - k.id;
            }
        }
    }
}
