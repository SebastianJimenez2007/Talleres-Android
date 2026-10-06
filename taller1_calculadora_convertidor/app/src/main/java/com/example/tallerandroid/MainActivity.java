package com.example.tallerandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirConvertidor(View view) {
        Intent intent = new Intent(this, ConvertidorActivity.class);
        startActivity(intent);
    }

    public void abrirCalculadora(View view) {
        Intent intent = new Intent(this, CalculadoraActivity.class);
        startActivity(intent);
    }
}
