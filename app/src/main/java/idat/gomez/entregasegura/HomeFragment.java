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

import idat.gomez.entregasegura.databinding.FragmentHomeBinding;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentHomeBinding binding;
    FirebaseAuth auth;
    static List<Entrega> entregas;
    TextView txtCodPaquete, txtNombreTrabajador, txtDniTrabajador;
    EntregaAdapter entregaAdapter;

    FirebaseUser user;
    Boolean recargar;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
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
                                   binding.listPendientes.setVisibility(View.INVISIBLE);
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

        List<Entrega> listaPendientes = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            Entrega entregaAdd = lista.get(i);
            if(entregaAdd.isEstaEntregado()){
                continue;
            }
            listaPendientes.add(entregaAdd);
            ids.add(i);
        }

        entregaAdapter = new EntregaAdapter(listaPendientes, ids);
        binding.listPendientes.setHasFixedSize(true);
        binding.listPendientes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listPendientes.setAdapter(entregaAdapter);

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
        recargar = i.getBooleanExtra("recargar", false);
        if(recargar){
            i.putExtra("recargar2", true);
            i.removeExtra("recargar");
        }


    }

}