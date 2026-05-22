package org.example.paskolos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.example.paskolos.model.VienoMenDuomenys;
import java.util.List;

public class MokejimuAdapter extends RecyclerView.Adapter<MokejimuAdapter.ViewHolder> {

    private List<VienoMenDuomenys> duomenys;

    public MokejimuAdapter(List<VienoMenDuomenys> duomenys) {
        this.duomenys = duomenys;
    }

    public void atnaujinti(List<VienoMenDuomenys> naujiDuomenys) {
        this.duomenys = naujiDuomenys;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mokejimas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VienoMenDuomenys e = duomenys.get(position);
        holder.tvNr.setText(String.valueOf(position + 1));
        holder.tvData.setText(e.getData().toString());
        holder.tvImoka.setText(e.getImokaStr());
        holder.tvPagrindas.setText(e.getPagrindasStr());
        holder.tvPalukanos.setText(e.getPalukanosStr());
        holder.tvLikutis.setText(e.getLikutisStr());
    }

    @Override
    public int getItemCount() {
        return duomenys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNr, tvData, tvImoka, tvPagrindas, tvPalukanos, tvLikutis;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNr = itemView.findViewById(R.id.tvNr);
            tvData = itemView.findViewById(R.id.tvData);
            tvImoka = itemView.findViewById(R.id.tvImoka);
            tvPagrindas = itemView.findViewById(R.id.tvPagrindas);
            tvPalukanos = itemView.findViewById(R.id.tvPalukanos);
            tvLikutis = itemView.findViewById(R.id.tvLikutis);
        }
    }
}