package pl.winiarek.dominik.sugarap;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import pl.winiarek.dominik.Activity_HistoriaPomiarow;
import pl.winiarek.dominik.Activity_Kalkulator;
import pl.winiarek.dominik.Activity_Pomiar;
import pl.winiarek.dominik.Activity_Produkty;

public class SugarAP extends AppCompatActivity {

final private String INSTRUKCJA="<font color=\"#000000\"><b> Witaj w SugarAP</b>" +
        "<br> Na tym ekranie znajdują się cztery przyciski:" +
        "<br><br><b> Pomiar</b>" +
            "<br>Tu możesz zapisać wynik wykonanego pomiaru glukozy." +
        "<br><br><b> Kalkulator</b>" +
            "<br>Tu możesz obliczyć wartości kaloryczne i odżywcze swojego posiłku oraz wymiennik węglowodanowy, i wymiennik białkowo-tłuszczowy." +
        "<br><br><b> Produkty</b>" +
            "<br>Tu możesz przejrzeć, edytować oraz dodać produkt do listy spożywanych produktów." +
        "<br><br><b> Historia Pomiarów</b>" +
            "<br>Tu możesz zobaczyć historię zapisywanych pomiarów oraz zapisać ją do pliku w celu okazania lekarzowi." +
        "</font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_ap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Sugar);
        setSupportActionBar(toolbar);


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

            pl.winiarek.dominik.Dialog dial = new pl.winiarek.dominik.Dialog(SugarAP.this, INSTRUKCJA);
            dial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dial.setCancelable(true);
            dial.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Klik(View view) {
        Intent intent;
        switch(view.getId())
        {
            case R.id.Pomiar:
                intent= new Intent(SugarAP.this, Activity_Pomiar.class);
                startActivity(intent);
                break;
            case R.id.Kalkulator:
                intent= new Intent(SugarAP.this, Activity_Kalkulator.class);
                startActivity(intent);
                break;
            case R.id.Produkty:
                intent= new Intent(SugarAP.this, Activity_Produkty.class);
                startActivity(intent);
                break;
            case R.id.Kalendarz:
                intent= new Intent(SugarAP.this, Activity_HistoriaPomiarow.class);
                startActivity(intent);
                break;


        }


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
