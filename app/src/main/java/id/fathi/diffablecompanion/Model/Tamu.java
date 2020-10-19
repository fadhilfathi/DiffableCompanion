package id.fathi.diffablecompanion.Model;

public class Tamu {

    private String id, email, password, nama, ttl, nik, jeniskelamin, alamat, pekerjaan, nohp, jenis;

    public Tamu(String id, String email, String password, String nama, String ttl, String nik, String jeniskelamin, String alamat, String pekerjaan, String nohp, String jenis) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.ttl = ttl;
        this.nik = nik;
        this.jeniskelamin = jeniskelamin;
        this.alamat = alamat;
        this.pekerjaan = pekerjaan;
        this.nohp = nohp;
        this.jenis = jenis;
    }

    public Tamu(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getJeniskelamin() {
        return jeniskelamin;
    }

    public void setJeniskelamin(String jeniskelamin) {
        this.jeniskelamin = jeniskelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
