package pl.winiarek.dominik;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Dominik on 2017-10-31.
 */

public class DBAdapter  {

    public static final String Tag="DBAdapter";
//kolumny produkty
    public static final String KEY_ID ="_id";
    public static final String KEY_NZWA="Nazwa";
    public static final String KEY_KALORIE="Kalorie";
    public static final String KEY_BIALKO="Bialko";
    public static final String KEY_TLUSZCZ="Tluszcz";
    public static final String KEY_WEGLOWODANY="Weglowodany";
    public static final String KEY_BLONNIK="Blonnik";
    public static final String[] ALL_KEYS= new String[] {KEY_ID,KEY_NZWA,KEY_KALORIE,KEY_BIALKO, KEY_TLUSZCZ, KEY_WEGLOWODANY, KEY_BLONNIK};
//kolumny pomiar
    public static final String KEY_POMIAR_ID ="_id";
    public static final String KEY_POMIAR_NAZWA ="Nazwa";
    public static final String KEY_POMIAR_DATA="Data";
    public static final String KEY_POMIAR_CZAS="Czas";
    public static final String KEY_POMIAR="Pomiar";
    public static final String KEY_POMIAR_NOTE="Notatka";
    public static final String KEY_POMIAR_JEDNOSTKA="Jednostka";
    public static final String[] ALL_KEYS_POMIAR= new String[]
            {KEY_POMIAR_ID, KEY_POMIAR_NAZWA, KEY_POMIAR_DATA, KEY_POMIAR_CZAS, KEY_POMIAR, KEY_POMIAR_NOTE, KEY_POMIAR_JEDNOSTKA};



    public static final int COL_ID=0;
    public static final int COL_NAZWA=1;
    public static final int COL_KALORIE=2;
    public static final int COL_BIALKO=3;
    public static final int COL_TLUSZCZ=4;
    public static final int COL_WEGLOWODANY=5;
    public static final int COL_BLONNIK=6;

    public static final String DB_NAME="Spis";
    public static final String T_NAME="produkty";
    public static final String T_NAME2="pomiary";

    public static int VERSION=1;

    private static final String DB_CREATE_SQL = "CREATE TABLE " + T_NAME
            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NZWA + " TEXT NOT NULL, "
            + KEY_KALORIE + " REAL,"
            + KEY_BIALKO + " REAL,"
            + KEY_TLUSZCZ + " REAL,"
            + KEY_WEGLOWODANY + " REAL,"
            + KEY_BLONNIK + " REAL"
            + ");";
    private static final String DB_CREATE_SQL2 = "CREATE TABLE " + T_NAME2
            + " (" + KEY_POMIAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_POMIAR_NAZWA + " TEXT , "
            + KEY_POMIAR_DATA + " TEXT,"
            + KEY_POMIAR_CZAS + " TEXT,"
            + KEY_POMIAR + " REAL,"
            + KEY_POMIAR_NOTE + " TEXT,"
            + KEY_POMIAR_JEDNOSTKA + " TEXT" + ");";

    private final Context context;
    private DataBaseHelper myDBHelper;
    private SQLiteDatabase db;



    public DBAdapter(Context ctx)
    {
        this.context=ctx;
        myDBHelper = new DataBaseHelper(context);
    }
    public DBAdapter open()
    {
        db= myDBHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        myDBHelper.close();
    }
    public long insertRow(String nazwa, float k, float bi, float t, float w, float bl )
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NZWA,nazwa);
        initialValues.put(KEY_KALORIE, k);
        initialValues.put(KEY_BIALKO, bi);
        initialValues.put(KEY_TLUSZCZ, t);
        initialValues.put(KEY_WEGLOWODANY, w);
        initialValues.put(KEY_BLONNIK, bl);
        return db.insert(T_NAME, null, initialValues);
    }
    public long insertRow2( String nazwa, String data, String czas, float pomiar, String nota, String jednostka )
    {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_POMIAR_NAZWA, nazwa);
        initialValues.put(KEY_POMIAR_DATA, data);
        initialValues.put(KEY_POMIAR_CZAS, czas);
        initialValues.put(KEY_POMIAR, pomiar);
        initialValues.put(KEY_POMIAR_NOTE, nota);
        initialValues.put(KEY_POMIAR_JEDNOSTKA, jednostka);
        return db.insert(T_NAME2, null, initialValues);
    }

    public boolean deleteRoW(long rowID)
    {
        String where = KEY_ID + "=" +rowID;
        return db.delete(T_NAME, where,null) != 0;
    }
    public boolean deleteRoW2(long rowID)
    {
        String where = KEY_POMIAR_ID + "=" +rowID;
        return db.delete(T_NAME2, where,null) != 0;
    }

    public void deleteAll()
    {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ID);
        if(c.moveToFirst())
        {
            do {
                deleteRoW(c.getLong((int) rowId));
            }while (c.moveToNext());
        }
        c.close();
    }
    public void deleteAll2()
    {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_POMIAR_ID);
        if(c.moveToFirst())
        {
            do {
                deleteRoW(c.getLong((int) rowId));
            }while (c.moveToNext());
        }
        c.close();
    }

    public  Cursor getAllRows()
    {
        String where = null;
        Cursor c = db.query(true, T_NAME, ALL_KEYS, where,null,null,null,null,null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
    public  Cursor getAllRows2()
    {
        String where = null;

        Cursor c = db.query(true, T_NAME2, ALL_KEYS_POMIAR, where,null,null,null,null,null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
//========================================================================
    public Cursor getRow (long rowId)
    {
        String where = KEY_ID + "=" + rowId;
        Cursor c = db.query(true, T_NAME,ALL_KEYS,where,null,null,null,null,null);
        if(c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor getRow2 (long rowId)
    {
        String where = KEY_POMIAR_ID + "=" + rowId;
        Cursor c = db.query(true, T_NAME2,ALL_KEYS_POMIAR,where,null,null,null,null,null);
        if(c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    //==================================================================================
    public boolean updateRow (long rowId,String nazwa, float k, float bi, float t, float w, float bl )
    {
        String where = KEY_ID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NZWA,nazwa);
        newValues.put(KEY_KALORIE, k);
        newValues.put(KEY_BIALKO, bi);
        newValues.put(KEY_TLUSZCZ, t);
        newValues.put(KEY_WEGLOWODANY, w);
        newValues.put(KEY_BLONNIK, bl);
        return db.update(T_NAME, newValues, where,null) != 0;


    }
    public boolean updateRow2 (long rowId,String nazwa, String data, String czas, float pomiar, String nota, String jednostka )
    {
        String where = KEY_POMIAR_ID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_POMIAR_NAZWA,nazwa);
        newValues.put(KEY_POMIAR_DATA, data);
        newValues.put(KEY_POMIAR_CZAS, czas);
        newValues.put(KEY_POMIAR, pomiar);
        newValues.put(KEY_POMIAR_NOTE, nota);
        newValues.put(KEY_POMIAR_JEDNOSTKA, jednostka);
        return db.update(T_NAME2, newValues, where,null) != 0;


    }
    //==============================================

    private static class DataBaseHelper extends SQLiteOpenHelper
    {
        DataBaseHelper(Context context)
        {
            super(context, DB_NAME, null, VERSION);
        }

        public void onCreate(SQLiteDatabase _db)
        {

            _db.execSQL("DROP TABLE IF EXISTS " + T_NAME);
            _db.execSQL("DROP TABLE IF EXISTS " + T_NAME2);
            _db.execSQL(DB_CREATE_SQL);
            _db.execSQL(DB_CREATE_SQL2);
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (1,'Mleko 2%',52.0,3.4,2.0,4.9,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (2,'Mleko 0,5%',40.0,3.5,0.5,5.1,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (3,'Mleko 3,2%',62.0,3.3,3.2,4.8,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (4,'Jogurt naturalny 2%',61.0,4.3,2.0,6.2,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (5,'Jaja kurze całe',151.0,12.5,10.7,1.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (6,'Białko jaja kurzego',48.0,10.8,0.0,0.8,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (7,'Żółtko jaja kurzego',354.0,16.3,31.9,0.7,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (8,'Wieprzowina łopatka',259.0,16.0,21.7,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (9,'Wieprzowina schab',175.0,21.0,10.0,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (10,'Wieprzowina szynka',264.0,18.0,21.3,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (11,'Wieprzowina żeberka',324.0,15.2,29.3,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (12,'Wołowina pieczeń',118.0,20.9,3.6,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (13,'Śledź solony',219.0,19.8,15.4,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (14,'Makrela wędzona',223.0,20.7,15.5,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (15,'Śledź w oleju',343.0,19.7,29.4,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (16,'Olej słonecznikowy',892.0,0.0,100.0,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (17,'Olej rzepakowy',892.0,0.0,100.0,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (18,'Masło wyborowe',742.0,0.7,82.5,0.7,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (19,'Słonina',804.0,2.4,89.0,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (20,'Smalec',888.0,0.0,995.0,0.0,0.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (21,'Mąka pszenna 500',345.0,9.2,1.2,74.9,2.6);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (22,'Kasza gryczana',339.0,12.6,3.1,69.3,5.9);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (23,'Kasza jaglana',349.0,10.5,2.9,71.6,3.2);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (24,'Ryż biały',347.0,6.7,0.7,78.9,2.4);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (25,'Makaron bezjajeczny',366.0,10.0,1.6,78.5,2.7);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (26,'Makaron dwujajeczny',382.0,11.3,2.8,78.4,2.6);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (27,'Chleb żytni pełnoziarnisty',239.0,6.7,1.8,53.9,6.1);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (28,'Chleb żytni razowy',225.0,5.6,1.7,51.5,5.9);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (29,'Chleb zwykły',247.0,5.4,1.3,57.0,4.9);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (30,'Kajzerki',299.0,7.5,3.7,59.4,2.1);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (31,'Pieczywo tostowe',308.0,8.1,4.7,58.8,2.1);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (32,'Musli z owocami suszonymi',328.0,8.4,3.4,72.2,8.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (33,'Płatki kukurydziane',366.0,6.9,2.5,83.6,6.6);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (34,'Cebula',31.0,1.4,0.4,6.9,1.7);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (35,'Czosnek',147.0,6.4,0.5,32.6,4.1);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (36,'Kalafior',22.0,2.4,0.2,5.0,2.4);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (37,'Kapusta pekińska',12.0,1.2,0.2,3.2,1.9);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (38,'Marchew',27.0,1.0,0.2,8.7,3.6);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (39,'Ogórek',14.0,0.7,0.1,2.9,0.5);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (40,'Pomidor',15.0,0.9,0.2,3.6,1.2);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (41,'Brukselka',37.0,4.7,0.5,8.7,5.4);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (42,'Ziemniaki wczesne',70.0,1.8,0.1,16.3,1.3);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (43,'Ziemniaki późne',86.0,1.9,0.1,20.5,1.6);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (44,'Banan',96.0,1.0,0.3,23.5,1.7);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (45,'Cytryna',37.0,0.8,0.3,9.5,2.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (46,'Grejpfrut',37.0,0.6,0.2,9.8,1.9);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (47,'Gruszka',55.0,0.6,0.2,14.4,2.1);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (48,'Jabłko',47.0,0.4,0.4,12.1,2.0);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (49,'Pomarańcza',44.0,0.9,0.2,11.3,1.9);");
            _db.execSQL("INSERT INTO "+T_NAME+" VALUES (50,'Mandarynki',42.0,0.6,0.2,11.2,1.9);");


        }
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion)
        {
            Log.w(Tag, "Upgrading aplikacji db from version " + oldVersion + " to " + newVersion
            + " , which will destrroy all old date!");

            _db.execSQL("DROP TABLE IF EXISTS " + T_NAME);
            _db.execSQL("DROP TABLE IF EXISTS " + T_NAME2);

            onCreate(_db);
        }


    }









}

