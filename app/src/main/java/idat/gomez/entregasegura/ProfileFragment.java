package idat.gomez.entregasegura;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import idat.gomez.entregasegura.databinding.FragmentProfileBinding;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    TextView profileNombre, profileDni, profileNumCelular, profileCorreo;
    ImageView profileFoto;
    Button btnCerrar;
    FirebaseUser user;
    static Usuario usuarioRf;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        profileNombre = binding.profileNombre;
        profileDni = binding.profileDni;
        profileNumCelular = binding.profileNumCelular;
        profileCorreo = binding.profileCorreo;
        profileFoto = binding.profileFoto;
        btnCerrar = binding.btnCerrar;
        user = auth.getCurrentUser();

        if(user == null) {
            Intent i = new Intent(getActivity(),Login_Activity.class);
            startActivity(i);
        }

        String uid = user.getUid(); // o0HPdNIjy2XUSWtueitMxPjpnyP2
        try {
            if(usuarioRf != null) {
                profileNombre.setText(usuarioRf.getNombre());
                profileDni.setText(usuarioRf.getDni());
                profileNumCelular.setText(usuarioRf.getNumeroCelular());
                profileCorreo.setText(user.getEmail());
                ImageViewExtension.fromUrl(profileFoto, usuarioRf.getFoto());
            } else {
                cargarDatos(uid);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnCerrar.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(),Login_Activity.class);
            startActivity(i);
        });

        return view;
    }

    private void cargarDatos(String uid) {
        Retrofit rf = getRetrofit();
        ApiService api = rf.create(ApiService.class);
        Call<Usuario> call = api.getByUser(uid +".json");
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                usuarioRf = response.body();
                profileNombre.setText(usuarioRf.getNombre());
                profileDni.setText(usuarioRf.getDni());
                profileNumCelular.setText(usuarioRf.getNumeroCelular());
                profileCorreo.setText(user.getEmail());
                ImageViewExtension.fromUrl(profileFoto, usuarioRf.getFoto());

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                            .baseUrl("https://entrega-segura-78f02-default-rtdb.firebaseio.com/usuarios/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(getClienteRetrofit())
                            .build();
    }

    private OkHttpClient getClienteRetrofit() {
        return new OkHttpClient.Builder().build();
    }
}