package idat.gomez.entregasegura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import idat.gomez.entregasegura.databinding.ActivityLoginBinding;

public class Login_Activity extends AppCompatActivity {

    ActivityLoginBinding binding;
    EditText etMail, etPass;
    Button btnIngresar;
    FirebaseAuth auth;
    FirebaseUser user;

    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_Theme_Entregasegura);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dbPrueba"
        ).allowMainThreadQueries().build();

        auth = FirebaseAuth.getInstance();
        etMail = binding.etMail;
        etPass = binding.etPass;
        btnIngresar = binding.btnIngresar;

        btnIngresar.setOnClickListener(v -> {

            String email, pass;
            email = String.valueOf(etMail.getText());
            pass = String.valueOf(etPass.getText());

            if(TextUtils.isEmpty(email)){
                Toast.makeText(Login_Activity.this,
                                "Ingrese Email",
                                Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(pass)){
                Toast.makeText(Login_Activity.this,
                        "Ingrese Contraseña",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Ingreso Exitoso",
                                        Toast.LENGTH_SHORT).show();

                                user = auth.getCurrentUser();
                                appDatabase.daoSesion().insertarSesion(new Sesion(user.getUid()));

                                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Login_Activity.this,
                                        "Autenticación fallida",
                                        Toast.LENGTH_SHORT).show();
                                etMail.setText("");
                                etPass.setText("");
                                etMail.requestFocus();
                            }
                        }
                    });


        });

        user = auth.getCurrentUser();
        if( user != null && appDatabase.daoSesion().buscarSesion(user.getUid()) != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(i);
            finish();
        }
    }
}