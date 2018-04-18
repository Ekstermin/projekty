package pl.winiarek.dominik;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import pl.winiarek.dominik.sugarap.R;

public class Activity_Pomiar extends AppCompatActivity {
   private int rok, miesiac,dzien, godzina, minuta;
   private TextView data,czas , jed;
   private DatePickerDialog datePicker;
   private EditText pom,nazwa;
   private Pomiar pomiar ;
   public static String KLUCZ_NAZWA="Jednostka";
   public static String KLUCZ="jednostka";
   public static String jednostka="mmol/l";

   private MultiAutoCompleteTextView note;
   String dzienPomiaru, godzinaPomiaru;
    TimePickerDialog timePicker;
    int r,m,d,g,mi;
    DBAdapter myDB;
    final private String INSTRUKCJA="<font size=\"12sp\" color=\"#000000\"><b> Pomiar</b>" +
            "<br><br> W tym oknie wprowadzasz swój pomiar glukozy." +
            "<br><br> W polu nazwa podajesz nazwę pomiaru np. <i>Śniadanie</i>." +
            "<br><br><b>Uwaga!!!</b>" +
            "<br>Nazwa może mieć maksymalnie 14 znaków.<br>" +
            "<br><br>Klikając w datę i godzinę masz możliwość ustawienia daty i godziny swojego pomiaru. " +
            "<br>Poniżej wpisujesz wartość swojego pomiaru, <b>to pole jest obowiązkowe</b>." +
            "<br><br>Klikając na jednostkę otworzy się okno ustawień pozwalające na jej zmianę." +
            "<br><br>W polu <b>Twoja notatka</b> Możesz zapisać swoje uwagi odnośnie pomiaru.</font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__pomiar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref= getSharedPreferences(KLUCZ_NAZWA, 0);
        jednostka = pref.getString(KLUCZ, "mmol/l");

        data = (TextView) findViewById(R.id.data) ;
        jed = (TextView) findViewById(R.id.tv_jednostkaGlukozy) ;
        czas = (TextView) findViewById(R.id.czas) ;
        pom = (EditText) findViewById(R.id.ET_glukoza);
        nazwa = (EditText) findViewById(R.id.ET_Nazwa);
        note = (MultiAutoCompleteTextView) findViewById(R.id.note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getkalendarz();
        ustaw_date();
        ustaw_godzine();


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

            pl.winiarek.dominik.Dialog dial = new pl.winiarek.dominik.Dialog(this, INSTRUKCJA);
            dial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dial.setCancelable(true);
            dial.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    public void getkalendarz()
    {
        Calendar c = Calendar.getInstance();
        godzina = c.get(Calendar.HOUR_OF_DAY);
        minuta = c.get(Calendar.MINUTE);
        czas.setText(String.format("%02d",godzina)+" : "+String.format("%02d",minuta));
        rok = c.get(Calendar.YEAR);
        miesiac = c.get(Calendar.MONTH);
        dzien = c.get(Calendar.DAY_OF_MONTH);
        data.setText(String.format("%02d",dzien)+" - "+String.format("%02d",(miesiac+1))+" - "+rok);
        r=rok;m=miesiac+1;d=dzien;
        g=godzina;mi=minuta;
        godzinaPomiaru= g+" : "+mi;
        dzienPomiaru= d+"-"+m+"-"+r+"r.";
    }

public void ustaw_date()
{
    datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            r=year;m=month+1;d=dayOfMonth;
            dzienPomiaru=String.format("%02d",d)+"-"+String.format("%02d",m)+"-"+r+"r.";
            data.setText(String.valueOf(dayOfMonth)+"-"+m+"-"+String.valueOf(year));
        }
    },rok,miesiac,dzien);

}
    public void ustaw_godzine()
    {
        timePicker = new TimePickerDialog(this,0, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                g=hourOfDay;mi=minute;
                godzinaPomiaru=String.format("%02d",g)+" : "+String.format("%02d",mi);
                czas.setText(String.format("%02d",hourOfDay)+" : "+String.format("%02d",minute));
            }
        },godzina,minuta,true);

    }
    public void UstawGodzine(View view) {
        timePicker.show();

    }

    public void UstawDate(View view) {
        datePicker.show();
        Log.e("data",d+ " "+m+" "+r);
    }

    public void OnClickDodajPomiar(View view) {

        if( !TextUtils.isEmpty(pom.getText().toString())) {
            openDB();
            pomiar = new Pomiar(nazwa.getText().toString(), dzienPomiaru, godzinaPomiaru, Float.valueOf(pom.getText().toString()), note.getText().toString(), jednostka);

            myDB.insertRow2(pomiar.getNazwa(),pomiar.getData(),pomiar.getCzas(),pomiar.getPomiar(),pomiar.getNota(),pomiar.getJednostka());
            Log.e("to jest baza", pomiar.getNazwa()+" "+ pomiar.getData()+" "+ pomiar.getCzas()+" "+ pomiar.getPomiar()+" "+ pomiar.getJednostka()+" "+ pomiar.getNota() );

            Toast.makeText(this, "Dodano pomiar do listy", Toast.LENGTH_LONG).show();

            closeDB();

            pom.setText("");
            nazwa.setText("");
            note.setText("");
        }
        else
        {
            Toast.makeText(this, "Uzupełnij wartość pomiaru", Toast.LENGTH_LONG).show();
            Log.e("to jest baza", "   nie zrobiło   ");
        }
    }

    private void openDB()
    {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void closeDB()
    {
        myDB.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref= getSharedPreferences(KLUCZ_NAZWA, 0);
        pref.edit().putString(KLUCZ,jednostka).apply();
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

    public void zmienjednostke(View view) {

        AlertDialog.Builder WyborJednostki = new AlertDialog.Builder(this);
        WyborJednostki.setMessage("Wybierz jednostkę dla pomiaru.");
        WyborJednostki.setPositiveButton("mmol/l", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jednostka="mmol/l";
                jed.setText(jednostka);
            }
        });
        WyborJednostki.setNegativeButton("mg/dl", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jednostka="mg/dl";
                jed.setText(jednostka);
            }
        });
        WyborJednostki.create();
        WyborJednostki.show();
    }


}
