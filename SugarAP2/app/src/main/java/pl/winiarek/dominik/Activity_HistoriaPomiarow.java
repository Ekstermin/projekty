package pl.winiarek.dominik;



import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;


import pl.winiarek.dominik.sugarap.R;




public class Activity_HistoriaPomiarow extends AppCompatActivity {

    static int permission;
    boolean zgoda=false;
    ArrayList<Pomiar> Lista;
    DBAdapter myDB;
LinearLayout brak,jest;
    Pomiar pomiar;
  //  ListView lv;
    final private String INSTRUKCJA="<font color=\"#000000\"><b> Historia pomiarów</b>" +
            "<br><br> Tutaj możesz zobaczyć historię pomiarów." +
          "<br><br>Długie kliknięcie na pomiarze spowoduje zapytanie o usunięcie pomiaru." +
          "<br><br>W opcjach na pasku u góry możesz wybrać zapis do pliku. " +
          "Spowoduje to zapisanie historii do pliku który znajdziesz w katalogu <b>SugarAP</b>." +
          "W pamięci twojego telefonu.</font>";
    long ID=0;
    AlertDialog.Builder dialogBuilder;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__historiapomiarow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        zgoda=verifyStoragePermissions(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        brak =  (LinearLayout) findViewById(R.id.brak_pomiaru);
        jest=  (LinearLayout) findViewById(R.id.Jest_pomiar);


        openDB();


        produktListView();
        if(Lista.size()<=0)
        {
            brak.setVisibility(View.VISIBLE);
            jest.setVisibility(View.GONE);
        }
        else {
            jest.setVisibility(View.VISIBLE);
            brak.setVisibility(View.GONE);
        }
        listViewItemLongClick();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historia,menu);
        return super.onCreateOptionsMenu(menu);

    }
    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.e("2:  ", String.valueOf(permission));
        Log.e("3:  ", String.valueOf(PackageManager.PERMISSION_GRANTED));
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            Log.e("3:  ", String.valueOf(PackageManager.PERMISSION_GRANTED));
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }








        if(permission==PackageManager.PERMISSION_GRANTED) {
            return true;
        }
            else {return false;}
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        produktListView();
        int id = item.getItemId();
    switch (id)
    {

        case R.id.action_Informacje :
         {

            pl.winiarek.dominik.Dialog dial = new pl.winiarek.dominik.Dialog(this, INSTRUKCJA);
            dial.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dial.setCancelable(true);
            dial.show();
            break;
         }
        case R.id.action_zapis :
        {
            Log.e("zapis:  ", String.valueOf(PackageManager.PERMISSION_GRANTED));
                if(verifyStoragePermissions(this))
                {
                    zapis_do_pliku();
                }
                else
                {
                   // zgoda=verifyStoragePermissions(this);
                    if(verifyStoragePermissions(this))
                    {
                        zapis_do_pliku();
                    }
                    else
                    {
                        Toast.makeText(this, "Zapis danych nie możliwy.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Udziel zgodę na dostęp do pamięci.", Toast.LENGTH_SHORT).show();
                    }
                }
            break;
        }
    }


        return super.onOptionsItemSelected(item);
    }


void zapis_do_pliku()
    {
        Document doc = new Document(PageSize.A4,50,50,30,65);

        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/SugarAP");
            Log.e("bbbbb:  ", String.valueOf(Environment.getExternalStorageDirectory().getAbsoluteFile()));

            if(!dir.exists()) {
                Log.e("jest:  ", String.valueOf(dir.exists()));
                dir.mkdirs();

            }
            File file = new File(dir, "Historia_Pomiarów.pdf");

            FileOutputStream fo = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fo);
            doc.open();


            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD);
            Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD);
            Paragraph p;
            if(0 >= Lista.size()){
                p = new Paragraph(" ");
                Log.e("dddddddddd:  ", "pusta lista");
                doc.add(p);
            }else {
                for (int i = 0; i < Lista.size(); i++) {
                    p = new Paragraph();
                    p.setLeading(0,4);
                    Paragraph p2 = new Paragraph();
                    p2.setLeading(0,3);
                    p.add(new Chunk("Pomiar " +String.valueOf(i + 1) + ": ", f));
                    p.add(new Chunk(Lista.get(i).getNazwa() + " ",f2));
                    Log.e("nazwa:  ", Lista.get(i).getNazwa());
                    p.add(new Chunk(Lista.get(i).getData() + " ",f2));
                    p.add(new Chunk(Lista.get(i).getCzas() + " ",f2));
                    p.add(new Chunk(String.valueOf(Lista.get(i).getPomiar()) + " ",f2));
                    p.add(new Chunk(Lista.get(i).getJednostka() + " ",f2));
                    doc.add(p);
                    p2.add(new Chunk("Notatka: " , f));
                    p2.add(new Chunk(Lista.get(i).getNota() + " \n" , f2));
                    Log.e("notatka:  ", Lista.get(i).getNota());
                    doc.add(p2);
                }
            }
            Log.e("bbbbb:  ", "dodano");

        } catch (FileNotFoundException e) {
            Log.e("1. :  ", e.toString());
        } catch (DocumentException e) {
            Log.e("2. :  ", e.toString());;
        } finally {
            doc.close();
        }

    }

    private void produktListView()
    {
        Lista = new ArrayList<Pomiar>();
    int i=0;
    Cursor cursor = myDB.getAllRows2();
    if(cursor.moveToFirst()) {
        cursor.moveToFirst();
        do {
            i++;
            pomiar = new Pomiar(i, cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getFloat(4), cursor.getString(5), cursor.getString(6));
            Log.e("TTTTTTTTT:  ", String.valueOf(cursor.getInt(0)) + " " + pomiar.getNazwa() + " " + pomiar.getData() + " " + pomiar.getCzas() + " " + String.format("%.2f", pomiar.getPomiar()) + " " + pomiar.getJednostka() + " " + pomiar.getNota());
            Lista.add(pomiar);
        } while (cursor.moveToNext());
    }
        cursor.moveToFirst();
        String[] fromFileName = new String[]{DBAdapter.KEY_POMIAR_NAZWA, DBAdapter.KEY_POMIAR_DATA, DBAdapter.KEY_POMIAR_CZAS, DBAdapter.KEY_POMIAR, DBAdapter.KEY_POMIAR_NOTE, DBAdapter.KEY_POMIAR_JEDNOSTKA};
        int[] toViewIds = new int[]{ R.id.NazwaPomiaru, R.id.DataPomiaru, R.id.CzasPomiaru, R.id.PomiarPomiaru, R.id.NotatkaPomiaru, R.id.JednostkaPomiaru};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.lista_pomiary, cursor, fromFileName, toViewIds, 0);
        ListView myList = (ListView) findViewById(R.id.listview_historia);
        myList.setAdapter(myCursorAdapter);


    }




    private void listViewItemLongClick()
    {

        final ListView myList = (ListView)findViewById(R.id.listview_historia);
        dialogBuilder = new AlertDialog.Builder(this);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ID=id;

                Log.e("usuwanie", String.valueOf(position));
                dialogBuilder.setMessage("Chcesz usunąć Pomiar?");
                dialogBuilder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("usuwanie", String.valueOf(ID));

                        myDB.deleteRoW2(ID);
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



    private void openDB()
    {
        myDB = new DBAdapter(this);
        myDB.open();
        Log.e("bbbbb:  ", "otworzyłem");
    }





    private void closeDB()
    {
        myDB.close();
        Log.e("bbbbb:  ", "zamknolem");
    }






}
