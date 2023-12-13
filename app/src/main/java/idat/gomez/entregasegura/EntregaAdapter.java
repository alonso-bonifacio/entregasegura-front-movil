package idat.gomez.entregasegura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import idat.gomez.entregasegura.databinding.ListItemBinding;

public class EntregaAdapter extends RecyclerView.Adapter<EntregaAdapter.ViewHolder> {

    private final List<Entrega> entregas;

    public EntregaAdapter(List<Entrega> entregas) {this.entregas = entregas;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new EntregaAdapter.ViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
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

        private final ListItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ListItemBinding _binding = ListItemBinding.bind(itemView);
            this.binding = _binding;
        }

        public void bind(Entrega entrega) {
            this.binding.txtCodPaquete.setText("Cod. Paquete: " + entrega.getCodigoPaquete());
            this.binding.txtNombreTrabajador.setText(entrega.getNombreTrabajador());
            this.binding.txtDniTrabajador.setText("DNI: " + entrega.getDniTrabajador());
        }
    }
}
