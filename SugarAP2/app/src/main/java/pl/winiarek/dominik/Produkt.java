package pl.winiarek.dominik;

/**
 * Created by Dominik on 2017-10-31.
 */

public class Produkt {
    private int Nr;
    private String Nazwa;
    private float Kalorie=0;
    private float Bialko=0;
    private float Tluszcz=0;
    private float Weglowodany=0;
    private float Blonnik=0;
    private float WW=0;
    private float WBT=0;
    private int Waga=0;


    public Produkt()
    {
        Nazwa="";
        Nr =0;
        Kalorie = 0;
        Bialko = 0;
        Tluszcz = 0;
        Weglowodany = 0;
        Blonnik = 0;
        Waga=0;
        WW = (Weglowodany - Blonnik) / 10;
        WBT = ((4*Bialko) + (9*Tluszcz)) / 100;}

    public Produkt(int nr, String nazwa, float kalorie, float bialko, float tluszcz, float weglowodany, float bonnik) {
        Nr = nr;
        Nazwa = nazwa;
        Kalorie = kalorie;
        Bialko = bialko;
        Tluszcz = tluszcz;
        Weglowodany = weglowodany;
        Blonnik = bonnik;
        Waga=100;
        WW = (Weglowodany - Blonnik) / 10;
        WBT = ((4*Bialko) + (9*Tluszcz)) / 100;
    }
    public void dodaj(Produkt a)
    {

        this.Waga=this.Waga +a.Waga;
        this.Nazwa = a.Nazwa;
        this.Kalorie = this.Kalorie+ a.Kalorie;
        this.Bialko = this.Bialko + a.Bialko;
        this.Tluszcz = this.Tluszcz + a.Tluszcz;
        this.Blonnik = this.Blonnik + a.Blonnik;
        this.Weglowodany = this.Weglowodany + a.Weglowodany;
        this.WW = (this.Weglowodany - this.Blonnik) / 10;
        this.WBT = ((4*this.Bialko) + (9*this.Tluszcz)) / 100;

    }
    public Produkt  mnoz()
    {
        Produkt d = new Produkt();
        d.Nazwa = this.getNazwa();
        d.Kalorie=(((float)Waga)*Kalorie)/100;
        d.Bialko = ((float)Waga*Bialko)/100;
        d.Tluszcz = ((float)Waga*Tluszcz)/100;
        d.Blonnik = ((float)Waga*Blonnik)/100;
        d.Weglowodany = ((float)Waga*Weglowodany)/100;
        d.Waga =Waga;
        d.WW =((float)Waga* WW)/100;
        d.WBT=((float)Waga* WBT)/100;
        return d;
    }
    public int getWaga() {
        return Waga;
    }

    public void setWaga(int waga) {
        Waga = waga;
    }

    public float getWW() {
        return WW;
    }

    public void setWW(float WW) {
        this.WW = WW;
    }

    public float getWBT() {
        return WBT;
    }

    public void setWBT(float WBT) {
        this.WBT = WBT;
    }

    public int getNr() {
        return Nr;
    }

    public void setNr(int nr) {
        Nr = nr;
    }

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String nazwa) {
        Nazwa = nazwa;
    }

    public float getKalorie() {
        return Kalorie;
    }

    public void setKalorie(float kalorie) {
        Kalorie = kalorie;
    }

    public float getBialko() {
        return Bialko;
    }

    public void setBialko(float bialko) {
        Bialko = bialko;
    }

    public float getTluszcz() {
        return Tluszcz;
    }

    public void setTluszcz(float tluszcz) {
        Tluszcz = tluszcz;
    }

    public float getWeglowodany() {
        return Weglowodany;
    }

    public void setWeglowodany(float weglowodany) {
        Weglowodany = weglowodany;
    }

    public float getBlonnik() {
        return Blonnik;
    }

    public void setBlonnik(float blonnik) {
        Blonnik = blonnik;
    }
}
