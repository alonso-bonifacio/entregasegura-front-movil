package idat.gomez.entregasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import idat.gomez.entregasegura.databinding.ActivityDatosEnvioBinding;

public class DatosEnvio extends AppCompatActivity {

    ActivityDatosEnvioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatosEnvioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null) {

            String codigoPaquete = intent.getStringExtra("codigoPaquete");
            String nombreTrabajador = intent.getStringExtra("nombreTrabajador");
            String cargoTrabajador = intent.getStringExtra("cargoTrabajador");
            String dniTrabajador = intent.getStringExtra("dniTrabajador");
            String direccionTrabajador = intent.getStringExtra("direccionTrabajador");
            String numeroCelTrabajador = intent.getStringExtra("numeroCelTrabajador");

            binding.codigoPaquete.setText(codigoPaquete);
            binding.nombreTrabajador.setText(nombreTrabajador);
            binding.cargoTrabajador.setText(cargoTrabajador);
            binding.dniTrabajador.setText(dniTrabajador);
            binding.direccionTrabajador.setText(direccionTrabajador);
            binding.numeroCelTrabajador.setText(numeroCelTrabajador);
            binding.btnRegresar.setClickable(true);
            binding.btnRegresar.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), MainActivity2.class);
                startActivity(i);
            });
        }

    }
}