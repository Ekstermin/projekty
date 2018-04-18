package pl.winiarek.dominik;

/**
 * Created by Dominik on 2017-11-10.
 */

public class Pomiar {


    private int ID;
   private int idlista;
   private String Nazwa;
   private String Data;
   private String Czas;
   private float Pomiar;
   private String Nota;
   private String Jednostka;

    public Pomiar( String nazwa, String data, String czas, float pomiar, String nota, String jednostka) {
     //   ID=id;
        Nazwa = nazwa;
        Data = data;
        Czas = czas;
        Pomiar = pomiar;
        Nota = nota;
        Jednostka = jednostka;
    }
    public Pomiar(int idl, int id, String nazwa, String data, String czas, float pomiar, String nota, String jednostka) {
        idlista=idl;
        ID=id;
        Nazwa = nazwa;
        Data = data;
        Czas = czas;
        Pomiar = pomiar;
        Nota = nota;
        Jednostka = jednostka;
    }
    public int getIdlista() {
        return idlista;
    }

    public void setIdlista(int idlista) {
        this.idlista = idlista;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String nazwa) {
        Nazwa = nazwa;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCzas() {
        return Czas;
    }

    public void setCzas(String czas) {
        Czas = czas;
    }

    public float getPomiar() {
        return Pomiar;
    }

    public void setPomiar(float pomiar) {
        Pomiar = pomiar;
    }

    public String getNota() {
        return Nota;
    }

    public void setNota(String nota) {
        Nota = nota;
    }

    public String getJednostka() {
        return Jednostka;
    }

    public void setJednostka(String jednostka) {
        Jednostka = jednostka;
    }
}
