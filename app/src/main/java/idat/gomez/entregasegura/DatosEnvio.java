package idat.gomez.entregasegura;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import idat.gomez.entregasegura.databinding.ActivityDatosEnvioBinding;

public class DatosEnvio extends AppCompatActivity {

    ActivityDatosEnvioBinding binding;
    DatabaseReference mDatabase;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatosEnvioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();

        if(user == null) {
            Intent i = new Intent(this,Login_Activity.class);
            startActivity(i);
        }

        Intent intent = this.getIntent();
        if (intent != null) {

            String codigoPaquete = intent.getStringExtra("codigoPaquete");
            String nombreTrabajador = intent.getStringExtra("nombreTrabajador");
            String cargoTrabajador = intent.getStringExtra("cargoTrabajador");
            String dniTrabajador = intent.getStringExtra("dniTrabajador");
            String direccionTrabajador = intent.getStringExtra("direccionTrabajador");
            String numeroCelTrabajador = intent.getStringExtra("numeroCelTrabajador");
            int id = intent.getIntExtra("idEnvio", -1);

            binding.codigoPaquete.setText(codigoPaquete);
            binding.nombreTrabajador.setText(nombreTrabajador);
            binding.cargoTrabajador.setText(cargoTrabajador);
            binding.dniTrabajador.setText(dniTrabajador);
            binding.direccionTrabajador.setText(direccionTrabajador);
            binding.numeroCelTrabajador.setText(numeroCelTrabajador);
            binding.btnRegresar.setClickable(true);
            binding.btnTerminar.setOnClickListener(v -> {

                Date fechaActual = new Date();
                SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMM yyyy - hh:mm a", new Locale("es", "PE"));
                String fechaFormateada = formato.format(fechaActual);
                String [] cadenas = fechaFormateada.split(" ");
                cadenas[2] = cadenas[2].replace(cadenas[2], String.valueOf(cadenas[2].charAt(0)).toUpperCase()+cadenas[2].substring(1));

                String fechaEntrega = String.join(" ", cadenas);

                Map<String, Object> entrega = new HashMap<>();
                entrega.put("cargoTrabajador", cargoTrabajador);
                entrega.put("codigoPaquete", codigoPaquete);
                entrega.put("direccionTrabajador", direccionTrabajador);
                entrega.put("dniTrabajador", dniTrabajador);
                entrega.put("estaEntregado", true);
                entrega.put("fechaEntrega", fechaEntrega);
                entrega.put("nombreTrabajador", nombreTrabajador);
                entrega.put("numeroCelular", numeroCelTrabajador);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/entregas/"+ user.getUid() + "/" + id, entrega);

                mDatabase.updateChildren(childUpdates);

                Intent valor = new Intent(v.getContext(), MainActivity2.class);
                valor.putExtra("recargar", true);
                startActivity(valor);
            });
            binding.btnRegresar.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), MainActivity2.class);
                startActivity(i);
            });
            binding.btnFirmar.setOnClickListener(view -> {
                camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            });
        }

    }

    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK){
                        assert o.getData() != null;
                        Bundle extras = o.getData().getExtras();
                        assert extras != null;
                        Bitmap imgBipmap = (Bitmap) extras.get("data");
                        binding.firmaTrabajador.setImageBitmap(imgBipmap);
                        binding.btnTerminar.setEnabled(true);
                    }
                }
            }
    );
}