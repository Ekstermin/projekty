package pl.winiarek.dominik;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.winiarek.dominik.sugarap.R;

/**
 * Created by Dominik on 2017-11-04.
 */

public class Lista_K_Adapter extends ArrayAdapter<Produkt>{

    private List<Produkt> danie;
    private Context context;
    public Lista_K_Adapter(@NonNull Context context, ArrayList<Produkt> danie) {

        super(context, R.layout.posilek_items);
        this.danie = danie;
        this.context = context;
    }

    @Override
    public int getCount() {
        return danie.size();
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View row =convertView;
        PotrawaHolder holder =null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.posilek_items,null);

            holder = new PotrawaHolder();
            holder.Nazwa = (TextView) row.findViewById(R.id.Nazwa);
            holder.Kal = (TextView) row.findViewById(R.id.Kalorie);
            holder.Bia = (TextView) row.findViewById(R.id.Bialko);
            holder.Tlu = (TextView) row.findViewById(R.id.Tluszcz);
            holder.Blo = (TextView) row.findViewById(R.id.Blonnik);
            holder.Wag = (TextView) row.findViewById(R.id.Waga);
            holder.Weg = (TextView) row.findViewById(R.id.Weglowodany);
            holder.WW = (TextView) row.findViewById(R.id.WW);
            holder.WBT = (TextView) row.findViewById(R.id.WBT);

            row.setTag(holder);
        }else
        {
            holder = (PotrawaHolder) row.getTag();
        }

        Produkt produkt = danie.get(position);
        holder.Nazwa.setText(produkt.getNazwa());

        holder.Kal.setText(String.valueOf(String.format("%.01f",produkt.getKalorie())));
        holder.Bia.setText(String.valueOf(String.format("%.01f",produkt.getBialko())));
        holder.Tlu.setText(String.valueOf(String.format("%.01f",produkt.getTluszcz())));
        holder.Blo.setText(String.valueOf(String.format("%.01f",produkt.getBlonnik())));
        holder.Wag.setText(String.valueOf(produkt.getWaga()));
        holder.Weg.setText(String.valueOf(String.format("%.01f",produkt.getWeglowodany())));
        holder.WW.setText(String.valueOf(String.format("%.02f",produkt.getWW())));
        holder.WBT.setText(String.valueOf(String.format("%.02f",produkt.getWBT())));
        return row;
    }

    static class PotrawaHolder {
        TextView Nazwa;
        TextView Kal;
        TextView Bia;
        TextView Tlu;
        TextView Weg;
        TextView Blo;
        TextView Wag;
        TextView WW;
        TextView WBT;


    }
}
