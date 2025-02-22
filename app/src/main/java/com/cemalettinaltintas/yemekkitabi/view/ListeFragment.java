package com.cemalettinaltintas.yemekkitabi.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.Room;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cemalettinaltintas.yemekkitabi.databinding.FragmentListeBinding;
import com.cemalettinaltintas.yemekkitabi.roomdb.TarifDao;
import com.cemalettinaltintas.yemekkitabi.roomdb.TarifDatabase;


public class ListeFragment extends Fragment {
    private FragmentListeBinding binding;
    TarifDao tarifDao;
    TarifDatabase db;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= Room.databaseBuilder(requireContext(),TarifDatabase.class,"Tarifler").build();
        tarifDao=db.tarifDao();
    }
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentListeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeniEkle(v);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void yeniEkle(View view) {
        Navigation.findNavController(view).navigate(ListeFragmentDirections.actionListeFragmentToTarifFragment("yeni",0));
    }
}