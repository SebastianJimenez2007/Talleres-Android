package com.example.tallerandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculadoraActivity extends AppCompatActivity {

    private EditText etValorCredito, etCuotas, etInteres;
    private TextView tvValorCuota, tvValorTotal, tvGananciaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        etValorCredito = findViewById(R.id.etValorCredito);
        etCuotas = findViewById(R.id.etCuotas);
        etInteres = findViewById(R.id.etInteres);
        tvValorCuota = findViewById(R.id.tvValorCuota);
        tvValorTotal = findViewById(R.id.tvValorTotal);
        tvGananciaTotal = findViewById(R.id.tvGananciaTotal);
        Button btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcular();
            }
        });
    }

    private void calcular() {
        String sValor = etValorCredito.getText().toString();
        String sCuotas = etCuotas.getText().toString();
        String sInteres = etInteres.getText().toString();

        if (sValor.isEmpty() || sCuotas.isEmpty() || sInteres.isEmpty()) {
            return;
        }

        double valorCredito = Double.parseDouble(sValor);
        int numCuotas = Integer.parseInt(sCuotas);
        double interesPct = Double.parseDouble(sInteres);

        if (numCuotas <= 0) return;

        double interesMensual = valorCredito * (interesPct / 100);
        double abonoCapital = valorCredito / numCuotas;
        double valorCuota = abonoCapital + interesMensual;
        double valorTotal = valorCuota * numCuotas;
        double ganancia = valorTotal - valorCredito;

        tvValorCuota.setText(String.format("Valor por cuota: %.2f", valorCuota));
        tvValorTotal.setText(String.format("Valor total del crédito: %.2f", valorTotal));
        tvGananciaTotal.setText(String.format("Ganancia total: %.2f", ganancia));
    }
}
