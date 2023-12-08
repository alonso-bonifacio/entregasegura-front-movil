package idat.gomez.entregasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import idat.gomez.entregasegura.databinding.ActivityLoginBinding;

public class Login_Activity extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_Theme_Entregasegura);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnIngresar = binding.btnIngresar;
        btnIngresar.setOnClickListener(v -> {
            Intent i = new Intent(Login_Activity.this, MainActivity2.class);
            startActivity(i);
        });
    }
}