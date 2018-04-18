package pl.winiarek.dominik;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.winiarek.dominik.sugarap.R;

public class Activity_Kalkulator extends AppCompatActivity {
    List<Produkt> ListaSpiner = new ArrayList<>();
    ArrayList<Produkt> ListaPosilek = new ArrayList<>();
    ArrayList<String> l = new ArrayList<>();
    DBAdapter myDB;
    ArrayAdapter<Produkt> adapter;
    Produkt p;
    Produkt p2  = new Produkt(0, "tmp", 0, 0, 0, 0, 0);
    Produkt Suma ;
    Spinner spiner;
    EditText waga;
    Button addddoposilku;
    ListView lv;
    final private String INSTRUKCJA="<font size=\"12sp\" color=\"#000000\"><b> Kalkulator</b>" +
            "<br><br>Tu obliczysz swój posiłek" +
            "<br><br>Kalkulator pobiera informacje z twojej listy produktów" +
            "<br><br>W rozwijanej liście wybierasz produkt następnie podajesz ile wagowo chcesz użyć tego produktu." +
            "<br> Klikając na przycisk dodaj, dodasz produkt do posiłku a poniżej ukażą ci się wartości " +
            "kaloryczne i odżywcze oraz WW i WBT całego posiłku." +
            "<br><br> Możesz dodać kika produktów a ich wartości zostaną za ciebie wyliczone</font>";

    TextView N;
    TextView Wa;
    TextView K;
    TextView Bi;
    TextView T;
    TextView We;
    TextView Bl;
    TextView WW;
    TextView WBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__kalkulator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spiner = (Spinner) findViewById(R.id.spinner);
        waga = (EditText)findViewById(R.id.et_waga);
        addddoposilku = (Button)findViewById(R.id.dodajProduktdoPosilku) ;
        lv = (ListView)findViewById(R.id.listview_posilek) ;


         N= (TextView)findViewById(R.id.TV_NZWA);
         Wa = (TextView)findViewById(R.id.TV_Waga_D);
         K = (TextView)findViewById(R.id.TV_Kalorie_D);
         Bi = (TextView)findViewById(R.id.TV_Bialko_D);
         T = (TextView)findViewById(R.id.TV_Tluszcz_D);
         We = (TextView)findViewById(R.id.TV_Weglowodany_D);
         Bl = (TextView)findViewById(R.id.TV_Blonnik_D);
         WW = (TextView)findViewById(R.id.TV_WW_D);
         WBT = (TextView)findViewById(R.id.TV_WBT_D);

        openDB();
        RobListe();
        lista_nazw();
        closeDB();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sugar_a,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Informacje) {

            pl.winiarek.dominik.Dialog dial = new pl.winiarek.dominik.Dialog(Activity_Kalkulator.this, INSTRUKCJA);
            dial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dial.setCancelable(true);
            dial.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void dodajDoPosilku(View view) {

        if(!TextUtils.isEmpty(waga.getText().toString()))
        {

           p2 = ListaSpiner.get(spiner.getSelectedItemPosition());
            p2.setWaga(Integer.valueOf(waga.getText().toString()));

            Log.e("mnoz1", String.valueOf(p2.getNazwa())+ "k "
                    +String.valueOf(p2.getKalorie())+ "b "+String.valueOf(p2.getBialko())+ "t "+String.valueOf(p2.getTluszcz())+ "w "
                    +String.valueOf(p2.getWeglowodany())+ "bl "+String.valueOf(p2.getBlonnik())+ "w "+String.valueOf(p2.getWaga()));
            p2=p2.mnoz();

            Log.e("mnoz2", String.valueOf(p2.getNazwa())+ "k "
                    +String.valueOf(p2.getKalorie())+ "b "+String.valueOf(p2.getBialko())+ "t "+String.valueOf(p2.getTluszcz())+ "w "
                    +String.valueOf(p2.getWeglowodany())+ "bl "+String.valueOf(p2.getBlonnik())+ "w "+String.valueOf(p2.getWaga())
                    + "ww "+String.valueOf(p2.getWW())+ "wbt "+String.valueOf(p2.getWBT()));
            Log.e("-----", "-----------------------------------------------");
            ListaPosilek.add(p2);
            Suma =new Produkt();
            for(int i=0;ListaPosilek.size() >i;i++ )
            {
                Log.e("lista", String.valueOf(ListaPosilek.get(i).getNazwa())+ "k "
                        +String.valueOf(ListaPosilek.get(i).getKalorie())+ "b "+String.valueOf(ListaPosilek.get(i).getBialko())
                        + "t "+String.valueOf(ListaPosilek.get(i).getTluszcz())+ "w "
                        +String.valueOf(ListaPosilek.get(i).getWeglowodany())+ "bl "+String.valueOf(ListaPosilek.get(i).getBlonnik())
                        + "w "+String.valueOf(ListaPosilek.get(i).getWaga()));
                Suma.dodaj(ListaPosilek.get(i));

            }
            lista();
            Log.e("-----", "-----------------------------------------------");

             Wa.setText(String.valueOf(Suma.getWaga()));
             K.setText(String.format("%.2f",Suma.getKalorie()));
             Bi.setText(String.format("%.2f",Suma.getBialko()));
             T.setText(String.format("%.2f",Suma.getTluszcz()));
             We.setText(String.format("%.2f",Suma.getWeglowodany()));
             Bl.setText(String.format("%.2f",Suma.getBlonnik()));
             WW.setText(String.format("%.2f",Suma.getWW()));
             WBT.setText(String.format("%.2f",Suma.getWBT()));
            waga.setText("");
            Log.e("suma","K "+  String.valueOf(Suma.getKalorie())+" Bi "+  String.valueOf(Suma.getKalorie())
                    +" T "+  String.valueOf(Suma.getTluszcz())+" We "+  String.valueOf(Suma.getWeglowodany())
                    +" Bl "+  String.valueOf(Suma.getBlonnik()));
        }
        else
        {
            Toast.makeText(this, " Uzupełnij wagę" , Toast.LENGTH_LONG ).show();
        }
        Suma=null;
    }

    public void lista()
    {
        adapter = new Lista_K_Adapter(this, ListaPosilek);
        lv.setAdapter(adapter);
    }

    private void lista_nazw()
    {
        for(int i = 0; ListaSpiner.size() > i; i++)
        {
            l.add(ListaSpiner.get(i).getNazwa());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
        ,android.R.layout.simple_spinner_dropdown_item,l);
        spiner.setAdapter(adapter);
    }
    private void openDB()
    {
        myDB = new DBAdapter(this);
        myDB.open();
    }
    private void RobListe()
    {


        Cursor cursor = myDB.getAllRows();
        cursor.moveToFirst();
        do
        {

            p = new Produkt(cursor.getInt(0),cursor.getString(1),
                    cursor.getFloat(2),cursor.getFloat(3),
                    cursor.getFloat(4),cursor.getFloat(5),cursor.getFloat(6));
            Log.e("TTTTTTTTT:  ", p.getNazwa()+" "+String.format("%.2f",p.getWeglowodany())+" "+
                    String.format("%.2f",p.getBlonnik())+" "+String.format("%.2f",p.getWW())+" "+String.format("%.2f",p.getWBT()));
            ListaSpiner.add(p);
        }while(cursor.moveToNext());
cursor.close();
closeDB();

    }
    private void closeDB()
    {
        myDB.close();
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
