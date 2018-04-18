package pl.winiarek.dominik;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.winiarek.dominik.sugarap.R;


public class Activity_Produkty extends AppCompatActivity {

    long ID;
    EditText ETNazwa ;
    EditText ETKalorie ;
    EditText ETBialko ;
    EditText ETTluszcz ;
    EditText ETWeglowodany ;
    EditText ETBlonnik ;
    TextView weglo;
    Button BDodajProdukt  ;
    final private String INSTRUKCJA="<font size=\"12sp\" color=\"#000000\"><b>Produkty</b>" +
            "<br><br>Tutaj znajduje się lista twoich produktów wraz z ich wartościami odżywczymi." +
            "<br><br><b>Pamiętaj: wartości są podane dla 100 gram produktu. </b>" +
            "<br><br>W pola poniżej wprowadzasz wartości odżywcze produktu" +
            "<br>Wszystkie wartości mają być podane dla <b>100 gram </b>produktu" +
            "<br><br>Na przykłąd:" +
            "<br>100g Cytryny" +
            "<br>Zawiera:" +
            "<br>Kalorii[K]- 37" +
            "<br>Białka[Bi]- 0.8g" +
            "<br>Tłuszczu[T]- 0.3g" +
            "<br>Węglowodanów[W]- 9.5g" +
            "<br>Błonnika[Bł]- 2g" +
            "<br><br><b>Długie przytrzymanie</b> produktu umożliwia usunięcie go z listy" +
            "<br><br>Wypełniając wszystkie pola i <b>klikając</b> w produkt masz możliwość edytowania klikniętego produktu. </font>";
    DBAdapter myDB;
    List<Produkt> Lista = new ArrayList<>();
    Produkt p;
    boolean stat=false;

    AlertDialog.Builder dialogBuilder, dialogBuilder2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__produkty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ETNazwa = (EditText)findViewById(R.id.ed_Nazwa);
        ETKalorie = (EditText)findViewById(R.id.ed_Kalorie);
        ETBialko = (EditText)findViewById(R.id.ed_Bialko);
        ETTluszcz = (EditText)findViewById(R.id.ed_Tluszcz);
        ETWeglowodany = (EditText)findViewById(R.id.ed_Weglowodany);
        ETBlonnik = (EditText)findViewById(R.id.ed_Blonnik);
        BDodajProdukt = (Button)findViewById(R.id.BdodajProdukt) ;



        Log.e("Activity_Produkty", " uruchomiono");

        openDB();
        produktListView();
        listViewClick();
        listViewItemLongClick();




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

    private void openDB()
    {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public  void onClick_Add (View v)
    {
        if( !TextUtils.isEmpty(ETNazwa.getText().toString()) &&
                !TextUtils.isEmpty(ETKalorie.getText().toString()) &&
                !TextUtils.isEmpty(ETBialko.getText().toString()) &&
                !TextUtils.isEmpty(ETTluszcz.getText().toString()) &&
                !TextUtils.isEmpty(ETWeglowodany.getText().toString()) &&
                !TextUtils.isEmpty(ETBlonnik.getText().toString()))
        {
            if(    Float.valueOf(ETWeglowodany.getText().toString()) >= Float.valueOf(ETBlonnik.getText().toString())) {
                myDB.insertRow(ETNazwa.getText().toString(), Float.valueOf(ETKalorie.getText().toString()), Float.valueOf(ETBialko.getText().toString()), Float.valueOf(ETTluszcz.getText().toString()), Float.valueOf(ETWeglowodany.getText().toString()), Float.valueOf(ETBlonnik.getText().toString()));
                Toast.makeText(this, "Pomyślnie dodano", Toast.LENGTH_SHORT).show();
                ETNazwa.setText("");
                ETKalorie.setText("");
                ETBialko.setText("");
                ETTluszcz.setText("");
                ETWeglowodany.setText("");
                ETBlonnik.setText("");
            }
            else
            {
                Toast.makeText(this,"Niepoprawne dane!",Toast.LENGTH_LONG).show();
            }
        }else
        {
            Toast.makeText(this,"Uzupełnij wszystkie pola!",Toast.LENGTH_LONG).show();
        }
        produktListView();
    }

    private void produktListView()
    {


        Cursor cursor = myDB.getAllRows();
        cursor.moveToFirst();
        do
        {

            p = new Produkt(cursor.getInt(0),cursor.getString(1),
                    cursor.getFloat(2),cursor.getFloat(3),
                    cursor.getFloat(4),cursor.getFloat(5),cursor.getFloat(6));
            Log.e("TTTTTTTTT:  ", cursor.getInt(0)+" " +p.getNazwa()+" "+String.format("%.2f",p.getWeglowodany())+" "+
                   String.format("%.2f",p.getBlonnik())+" "+String.format("%.2f",p.getWW())+" "+String.format("%.2f",p.getWBT()));
            Lista.add(p);
        }while(cursor.moveToNext());
        cursor.moveToFirst();
        String[] fromFileName = new String[] { DBAdapter.KEY_NZWA,DBAdapter.KEY_KALORIE,DBAdapter.KEY_BIALKO
                ,DBAdapter.KEY_TLUSZCZ,DBAdapter.KEY_WEGLOWODANY,DBAdapter.KEY_BLONNIK};
        int[] toViewIds = new int[] {R.id.Na, R.id.K, R.id.Bi, R.id.T, R.id.W,  R.id.Bl };
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.list_items, cursor, fromFileName, toViewIds,0);
       NonScrollListView myList = (NonScrollListView)findViewById(R.id.listview_produkty);
       // ListView myList = (ListView)findViewById(R.id.listview_produkty);
        myList.setAdapter(myCursorAdapter);

    }

    private void updateProdut( long id)
    {
        Cursor cursor = myDB.getRow(id);
        if(cursor.moveToFirst())
        {
            String Nazwa = ETNazwa.getText().toString();
            float K = Float.valueOf(ETKalorie.getText().toString());
            float Bi = Float.valueOf(ETBialko.getText().toString());
            float T = Float.valueOf(ETTluszcz.getText().toString());
            float W = Float.valueOf(ETWeglowodany.getText().toString());
            float Bl = Float.valueOf(ETBlonnik.getText().toString());


            myDB.updateRow(id,Nazwa,K,Bi,T,W,Bl);
        }
        cursor.close();
    }

    private void listViewClick()
    {
        ListView myList = (ListView)findViewById(R.id.listview_produkty);
        dialogBuilder2 = new AlertDialog.Builder(this);
        dialogBuilder2.setMessage("Chcesz edytować produkt?");
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ID=id;
                if( !TextUtils.isEmpty(ETNazwa.getText().toString()) &&
                        !TextUtils.isEmpty(ETKalorie.getText().toString()) &&
                        !TextUtils.isEmpty(ETBialko.getText().toString()) &&
                        !TextUtils.isEmpty(ETTluszcz.getText().toString()) &&
                        !TextUtils.isEmpty(ETWeglowodany.getText().toString()) &&
                        !TextUtils.isEmpty(ETBlonnik.getText().toString()))
                {
                    dialogBuilder2.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateProdut(ID);
                            produktListView();
                            ETNazwa.setText("");
                            ETKalorie.setText("");
                            ETBialko.setText("");
                            ETTluszcz.setText("");
                            ETWeglowodany.setText("");
                            ETBlonnik.setText("");

                        }
                    });
                    dialogBuilder2.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialogBuilder2.create();
                    dialogBuilder2.show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Uzupełnij wszystki pola!",Toast.LENGTH_LONG).show();
                    return;
                }
                    }
                });
    }

    public void onClick_DeleteProdukt(View v)
    {
        myDB.deleteAll();
        produktListView();
    }

    private void listViewItemLongClick()
    {

        ListView myList = (ListView)findViewById(R.id.listview_produkty);
        dialogBuilder = new AlertDialog.Builder(this);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {




            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ID=id;

                dialogBuilder.setMessage("Chcesz usunąć produkt?");
                dialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB.deleteRoW(ID);
                        produktListView();

                    }
                });
                dialogBuilder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialogBuilder.create();
                dialogBuilder.show();


                return false;
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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


    public void przewijaj(View view) {

        weglo = (TextView) findViewById(R.id.weglo) ;
        weglo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        weglo.setSingleLine();
        weglo.setHorizontallyScrolling(true);


        weglo.requestFocus();
    }
}




