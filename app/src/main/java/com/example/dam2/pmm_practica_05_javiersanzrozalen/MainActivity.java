package com.example.dam2.pmm_practica_05_javiersanzrozalen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/***************************************************************/
/********************* -->MAIN ACTIVITY<-- *********************/
/***************************************************************/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inflamos nuestros widgets
        btnPlay = (ImageButton) findViewById(R.id.buttonPlay);
        findViewById(R.id.textViewInstrucciones);

        // Nos suscribimos a nuestro botón play
        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonPlay){
            // Botón que nos lleva al GameActivity
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
    }
}
