package com.androiddevelopment.carlosjesus.getapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TurnOffAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_off_alarm);

        final Intent csv = new Intent(this, QuestionActivity.class);

        Button off = findViewById(R.id.off);

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*arIntent.putExtra("notification", true);
                arIntent.putExtra("extra", false);

                sendBroadcast(arIntent);*/

                startActivity(csv);
            }
        });
    }


}
