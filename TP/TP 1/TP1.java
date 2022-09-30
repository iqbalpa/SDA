import java.util.*;

public class TP1 {
    static Menu[] arrMenu;
    static Koki[] arrKoki;
    // static List<Koki> arrKoki = new ArrayList<>();
    static Pelanggan[] arrPelanggan;
    static LinkedList<Integer> sedangMakan = new LinkedList<>();
    static LinkedList<Integer> blackList = new LinkedList<>();
    static Queue<Pesanan> pesanan = new LinkedList<>();
    static Queue<Integer> ruangLapar = new LinkedList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // banyak menu
        int M = input.nextInt();
        arrMenu = new Menu[M+1];
        for (int i = 1; i <= M; i++) {
            int harga = input.nextInt();
            String tipe = input.next();
            Menu newMenu = new Menu(harga, tipe);
            arrMenu[i] = newMenu;
        }
        
        // banyak koki
        int V = input.nextInt();
        arrKoki = new Koki[V];
        for (int i = 0; i < V; i++) {
            String spesialisasi =  input.next();
            Koki newKoki = new Koki(spesialisasi, i);
            arrKoki[i] = newKoki;
            // arrKoki.add(newKoki);
        }

        // banyak pelanggan
        int P = input.nextInt();
        // banyak kursi
        int N = input.nextInt();
        // jumlah hari restoran beroperasi
        int Y = input.nextInt();

        // array pelanggan
        arrPelanggan = new Pelanggan[P+1];
        // jumlah pelanggan per hari
        int[] pelangganHariKe = new int[Y];
        // jumlah pelayanan per hari
        int[] pelayananHariKe = new int[Y];

        for (int i = 0; i < Y; i++) {
            pelangganHariKe[i] = input.nextInt();
            for (int j=0; j<pelangganHariKe[i]; j++){
                int I = input.nextInt();
                char K = input.next().charAt(0);
                int U = input.nextInt();

                if (blackList.contains(I)){
                    System.out.print("====" + 3 + " blacklist");
                } else {
                    Pelanggan newPelanggan;
                    if (K == '?'){
                        int R = input.nextInt();
                        // advance scanning
                        int negatif = 0;
                        int positif = 0;
                        for (int k=I-R; k>=I-1; k--){
                            if (arrPelanggan[k].K == '-'){
                                negatif++;
                            } else {
                                positif++;
                            }
                        }
                        K = negatif < positif ? '+' : '-';
                    } 
                    newPelanggan = new Pelanggan(I, K, U);
                    arrPelanggan[I] = newPelanggan;

                    if (j <= N){
                        sedangMakan.add(I);
                        if (K == '+'){
                            System.out.println("====" + 0 + " positif");
                        } else {
                            System.out.println("====" + 1 + " tidak masalah");
                        }
                    } else {
                        ruangLapar.add(I);
                        System.out.println("====" + 2 + " masuk ruang lapar");
                    }
                }
            }

            pelayananHariKe[i] = input.nextInt();
            // fungsi pelayanan
            for (int j=0; j<pelayananHariKe[i]; j++){
                int satu;
                int dua;
                int tiga;
                char pelayanan = input.next().charAt(0);
                if (pelayanan == 'P'){
                    satu = input.nextInt();
                    dua = input.nextInt();
                    P(satu, dua);
                } else if (pelayanan == 'B'){
                    satu = input.nextInt();
                    B(satu);
                } else if (pelayanan == 'C'){
                    satu = input.nextInt();
                } else if (pelayanan == 'D'){
                    satu = input.nextInt();
                    dua = input.nextInt();
                    tiga = input.nextInt();
                }
            }
        }

        input.close();
    }

    public static void P(int idPelanggan, int indexMakanan){
        Koki theKoki = arrKoki[0];
        // Koki theKoki = arrKoki.get(0);
        Menu theMenu = arrMenu[indexMakanan];
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        int indexKoki = 0;
        for (int i=0; i<arrKoki.length; i++){
            if (arrKoki[i].spesialisasi.equals(theMenu.tipe)){
                if (!theKoki.spesialisasi.equals(theMenu.tipe)){
                    theKoki = arrKoki[i];
                    indexKoki = i;
                } else if (theKoki.jumlahMelayani > arrKoki[i].jumlahMelayani){
                    theKoki = arrKoki[i];
                    indexKoki = i;
                } else if (theKoki.jumlahMelayani == arrKoki[i].jumlahMelayani && i > indexKoki) {
                    theKoki = arrKoki[indexKoki];
                }
            }
        }
        thePelanggan.tagihan += theMenu.harga;

        Pesanan newPesanan = new Pesanan(theMenu, theKoki, thePelanggan);
        pesanan.add(newPesanan);
        theKoki.jumlahMelayani++;
        System.out.println("===" + indexKoki + " index koki");
    }
    public static void L(){
        for (Pesanan p: pesanan){
            p.koki.jumlahMelayani--;
            p.koki.jumlahPelayanan++;
            System.out.println(p.pelanggan.I);
            pesanan.remove();
        }
    }
    public static void B(int idPelanggan){
        Pelanggan thePelanggan = arrPelanggan[idPelanggan];
        if (thePelanggan.U < thePelanggan.tagihan){
            System.out.println("===" + 0 + " uang tidak cukup");
            blackList.add(thePelanggan.I);
        } else {
            System.out.println("===" + 1 + " uang cukup");
            thePelanggan.U -= thePelanggan.tagihan;
        }
        sedangMakan.remove(idPelanggan);
        int idNewPelanggan = ruangLapar.remove();
        sedangMakan.add(idNewPelanggan);
    }
    public static void C(int Q){
        // Collections.sort(arrKoki);
        // for (int i=0; i<Q; i++){
        //     System.out.println(arrKoki.get(i).id);
        // }
    }
    public static void D(int A, int G, int S){}
}

class Menu {
    int harga;
    String tipe;

    public Menu(int harga, String tipe) {
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
    int I; // id pelanggan
    char K; // status kesehatan
    int U; // jumlah uang
    int tagihan;
  
    public Pelanggan(int I, char K, int U) {
        this.I = I;
        this.K = K;
        this.U = U;
        this.tagihan = 0;
    }
}

class Koki{
    String spesialisasi;
    int jumlahPelayanan;
    int jumlahMelayani;
    int id;

    public Koki(String spesialisasi, int id){
        this.spesialisasi = spesialisasi;
        this.jumlahPelayanan = 0;
        this.jumlahMelayani = 0;
        this.id = id;
    }

    // hasnt done yet
    // public int compareTo(Koki k){
    //     if (this.jumlahPelayanan > k.jumlahPelayanan){
    //         return 1;
    //     } else if (this.jumlahPelayanan < k.jumlahPelayanan){
    //         return -1;
    //     } else {
    //         if (this.spesialisasi.equals(k.spesialisasi)){
    //             return this.id > k.id ? 1 : -1;
    //         } else if (this.spesialisasi.equals("Airfood")){
    //             return 1;
    //         } else if (this.spesialisasi.equals("Groundfood") && k.spesialisasi.equals("Seafood")){
    //             return 1;
    //         } else if (this.spesialisasi.equals("Groundfood") && k.spesialisasi.equals("Airfood")){
    //             return -1;
    //         } else {
    //             return 1;
    //         }
    //     }
    // }
}