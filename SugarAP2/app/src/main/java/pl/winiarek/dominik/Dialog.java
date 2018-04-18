package pl.winiarek.dominik;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import pl.winiarek.dominik.sugarap.R;

/**
 * Created by Dominik on 2017-11-12.
 */

public class Dialog extends android.app.Dialog implements View.OnClickListener {

    Activity a;
    Dialog d;
    String msg;

    public Dialog (Activity c,String m)
    {
        super(c);
        this.a=c;
        msg=m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature((Window.FEATURE_NO_TITLE));
        setContentView(R.layout.layout_dialog1);
        TextView info = (TextView) findViewById(R.id.Dialog_info);

        info.setText(Html.fromHtml(msg));
    }


    @Override
    public void onClick(View v) {

    }
}
