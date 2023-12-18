package idat.gomez.entregasegura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import idat.gomez.entregasegura.databinding.ListHistorialBinding;

public class EntregadoAdapter extends RecyclerView.Adapter<EntregadoAdapter.ViewHolder> {

    private final List<Entrega> entregas;

    public EntregadoAdapter(List<Entrega> entregas) {this.entregas = entregas;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new EntregadoAdapter.ViewHolder(layoutInflater.inflate(R.layout.list_historial, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrega entrega = entregas.get(position);
        holder.bind(entrega);

    }

    @Override
    public int getItemCount() {
        return this.entregas.size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {

        private final ListHistorialBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ListHistorialBinding _binding = ListHistorialBinding.bind(itemView);
            this.binding = _binding;
        }

        public void bind(Entrega entrega) {
            this.binding.txtCodP.setText("Cod. Paquete: " + entrega.getCodigoPaquete());
            this.binding.nombreC.setText(entrega.getNombreTrabajador());
            this.binding.fechaEnt.setText(entrega.getFechaEntrega());
        }
    }
}