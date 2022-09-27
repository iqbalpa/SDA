import java.util.*;

public class TP1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // banyak menu
        int M = input.nextInt();
        Menu[] arrMenu = new Menu[M];
        for (int i = 0; i < M; i++) {
            int harga = input.nextInt();
            String tipe = input.next();
            Menu newMenu = new Menu(harga, tipe);
            arrMenu[i] = newMenu;
        }
        
        // banyak koki
        int V = input.nextInt();
        Koki[] arrKoki = new Koki[V];
        for (int i = 0; i < V; i++) {
            String spesialisasi =  input.next();
            Koki newKoki = new Koki(spesialisasi);
            arrKoki[i] = newKoki;
        }

        // banyak pelanggan
        int P = input.nextInt();
        // banyak kursi
        int N = input.nextInt();
        // jumlah hari restoran beroperasi
        int Y = input.nextInt();

        // array pelanggan
        Pelanggan[] arrPelanggan = new Pelanggan[P];
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
                Pelanggan newPelanggan;
                if (K == '?'){
                    int R = input.nextInt();
                    newPelanggan = new Pelanggan(I, K, U, R);
                } else {
                    newPelanggan = new Pelanggan(I, K, U);
                }
                arrPelanggan[I] = newPelanggan;
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
                } else if (pelayanan == 'B'){
                    satu = input.nextInt();
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

    public void P(int idPelanggan, int indexMakanan){}
    public void L(){}
    public void B(int idPelanggan){}
    public void C(int Q){}
    public void D(int A, int G, int S){}
}

class Menu {
    int harga;
    String tipe;

    public Menu(int harga, String tipe) {
        this.harga = harga;
        this.tipe = tipe;
    }
}

class Pelanggan {
    int I; // id pelanggan
    char K; // status kesehatan
    int U; // jumlah uang
    int R; // range advance scanning

    public Pelanggan(int I, char K, int U, int R) {
        this.I = I;
        this.K = K;
        this.U = U;
        this.R = R;
    }
    public Pelanggan(int I, char K, int U) {
        this.I = I;
        this.K = K;
        this.U = U;
    }
}

class Koki {
    String spesialisasi;
    int jumlahPelayanan;

    public Koki(String spesialisasi){
        this.spesialisasi = spesialisasi;
        this.jumlahPelayanan = 0;
    }
}