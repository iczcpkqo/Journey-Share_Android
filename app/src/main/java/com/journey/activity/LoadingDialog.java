package com.journey.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.journey.R;

public class LoadingDialog{
    private Activity activity;
    private AlertDialog alertDialog;
    private CountDownTimer countDownTimer;

    //count down timer
    private TextView timer;
    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.MyAlertDialogCustom);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.time_dialog,null));
        alertDialog = builder.create();
        //set round background for alert dialog
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.real_time_table_round);
        alertDialog.setMessage("10");
        alertDialog.show();

        startCounting();
    }

    private void startCounting(){
        new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage(" " + millisUntilFinished / 1000 + "s");
                TextView message = alertDialog.findViewById(android.R.id.message);
                message.setTextSize(40);
            }
            @Override
            public void onFinish() {
                if (((AlertDialog) alertDialog).isShowing()) {
                    alertDialog.dismiss();
                }
            }
        }.start();
    }
}
