package idat.gomez.entregasegura;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import idat.gomez.entregasegura.databinding.FragmentHistorialBinding;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentHistorialBinding binding;
    FirebaseAuth auth;
    static List<Entrega> entregas;
    TextView txtCodP, nombreC, fechaEnt;
    EntregadoAdapter entregadoAdapter;
    FirebaseUser user;

    Boolean recargar;

    public HistorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialFragment newInstance(String param1, String param2) {
        HistorialFragment fragment = new HistorialFragment();
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
        binding = FragmentHistorialBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null) {
            Intent i = new Intent(getActivity(), Login_Activity.class);
            startActivity(i);
        }

        String uid = user.getUid();
        try {
            recargar();
            if (entregas != null && !recargar){
                mostrarLista(entregas);
            } else {
                cargarDatos(uid);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void cargarDatos(String uid) {
        Thread thread = new Thread(
                () -> {
                    try{
                        Retrofit rf = getRetrofit();
                        ApiService api = rf.create(ApiService.class);
                        Call call = api.getByEntrega(uid +".json");
                        Response<List<Entrega>> res = call.execute();
                        entregas =  res.body();
                        getActivity().runOnUiThread(
                                () -> {
                                    if (entregas != null){
                                        mostrarLista(entregas);
                                    } else {
                                        Toast.makeText(getContext(), "No se encontraron entregas", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                    } catch (Exception e) {

                    }
                }
        );
        thread.start();


    }

    private void mostrarLista(List<Entrega> lista) {

        List<Entrega> listaEntregados = new ArrayList<>();

        for (Entrega item: lista) {
            if (!item.isEstaEntregado()) {
                continue;
            }

            listaEntregados.add(item);
        }

        entregadoAdapter = new EntregadoAdapter(listaEntregados);
        binding.listEntregados.setHasFixedSize(true);
        binding.listEntregados.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listEntregados.setAdapter(entregadoAdapter);

    }


    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://entrega-segura-78f02-default-rtdb.firebaseio.com/entregas/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClienteRetrofit())
                .build();
    }

    private OkHttpClient getClienteRetrofit() {
        return new OkHttpClient.Builder().build();
    }

    private void recargar() {
        Intent i = this.getActivity().getIntent();
        recargar = i.getBooleanExtra("recargar2", false);
        if(recargar){
            i.removeExtra("recargar2");
        }
    }
}