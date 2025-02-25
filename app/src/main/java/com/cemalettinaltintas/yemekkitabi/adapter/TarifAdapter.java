package com.cemalettinaltintas.yemekkitabi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cemalettinaltintas.yemekkitabi.databinding.RecyclerRowBinding;
import com.cemalettinaltintas.yemekkitabi.model.Tarif;
import com.cemalettinaltintas.yemekkitabi.view.ListeFragmentDirections;

import java.util.List;

public class TarifAdapter extends RecyclerView.Adapter<TarifAdapter.TarifHolder>{

    List<Tarif> tarifList;
    public TarifAdapter(List<Tarif> tarifList) {
        this.tarifList = tarifList;
    }

    @NonNull
    @Override
    public TarifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding binding= RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new TarifHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TarifHolder holder, int position) {
        holder.binding.recyclerViewTextView.setText(tarifList.get(position).isim);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListeFragmentDirections.ActionListeFragmentToTarifFragment action=ListeFragmentDirections.actionListeFragmentToTarifFragment("eski",tarifList.get(holder.getAdapterPosition()).id);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tarifList.size();
    }

    class TarifHolder extends RecyclerView.ViewHolder {
        private RecyclerRowBinding binding;
        public TarifHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

    }
}
