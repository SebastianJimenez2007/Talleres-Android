package com.example.tallerandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ConvertidorActivity extends AppCompatActivity {

    private EditText etCantidad;
    private Spinner spinnerOrigen, spinnerDestino;
    private TextView tvResultado;

    // Tasas fijas respecto a USD
    private final double TASA_USD = 1.0;
    private final double TASA_COP = 4000.0;
    private final double TASA_EUR = 0.92;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertidor);

        etCantidad = findViewById(R.id.etCantidad);
        spinnerOrigen = findViewById(R.id.spinnerOrigen);
        spinnerDestino = findViewById(R.id.spinnerDestino);
        tvResultado = findViewById(R.id.tvResultado);
        Button btnConvertir = findViewById(R.id.btnConvertir);

        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertir();
            }
        });
    }

    private void convertir() {
        String input = etCantidad.getText().toString();
        if (input.isEmpty()) {
            tvResultado.setText("Por favor ingrese una cantidad");
            return;
        }

        double cantidad = Double.parseDouble(input);
        String origen = spinnerOrigen.getSelectedItem().toString();
        String destino = spinnerDestino.getSelectedItem().toString();

        // Convertir a USD primero
        double cantidadEnUSD = 0;
        if (origen.equals("USD")) cantidadEnUSD = cantidad / TASA_USD;
        else if (origen.equals("COP")) cantidadEnUSD = cantidad / TASA_COP;
        else if (origen.equals("EUR")) cantidadEnUSD = cantidad / TASA_EUR;

        // Convertir de USD a destino
        double resultado = 0;
        if (destino.equals("USD")) resultado = cantidadEnUSD * TASA_USD;
        else if (destino.equals("COP")) resultado = cantidadEnUSD * TASA_COP;
        else if (destino.equals("EUR")) resultado = cantidadEnUSD * TASA_EUR;

        tvResultado.setText(String.format("Resultado: %.2f %s", resultado, destino));
    }
}
