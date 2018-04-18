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
 * Created by Dominik on 2017-11-11.
 */

public class Lista_Pomiar_Adapter extends ArrayAdapter<Pomiar> {

    private List<Pomiar> historia;
    private Context context;
    public Lista_Pomiar_Adapter(@NonNull Context context, ArrayList<Pomiar> historia) {

        super(context, R.layout.lista_pomiary);
        this.historia = historia;
        this.context = context;
    }



    @Override
    public int getCount() {
        return historia.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row =convertView;
        HistoriaHolder holder =null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.lista_pomiary,null);

            holder = new Lista_Pomiar_Adapter.HistoriaHolder();

            holder.Nazwa = (TextView) row.findViewById(R.id.NazwaPomiaru);
            holder.data = (TextView) row.findViewById(R.id.DataPomiaru);
            holder.czas = (TextView) row.findViewById(R.id.CzasPomiaru);
            holder.pomiar = (TextView) row.findViewById(R.id.PomiarPomiaru);
            holder.jednostka = (TextView) row.findViewById(R.id.JednostkaPomiaru);
            holder.notatka = (TextView) row.findViewById(R.id.NotatkaPomiaru);


            row.setTag(holder);
        }else
        {
            holder = (Lista_Pomiar_Adapter.HistoriaHolder) row.getTag();
        }

        Pomiar pomiar = historia.get(position);
        holder.Nazwa.setText(pomiar.getNazwa());
        holder.data.setText(pomiar.getData());
        holder.czas.setText(pomiar.getCzas());
        holder.pomiar.setText(String.format("%.2f", pomiar.getPomiar()));
        holder.jednostka.setText(pomiar.getJednostka());
        holder.notatka.setText(pomiar.getNota());



        return row;
    }

    static class HistoriaHolder {

        TextView Nazwa;
        TextView data;
        TextView czas;
        TextView pomiar;
        TextView jednostka;
        TextView notatka;



    }
}
