package com.cemalettinaltintas.yemekkitabi.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cemalettinaltintas.yemekkitabi.adapter.TarifAdapter;
import com.cemalettinaltintas.yemekkitabi.databinding.FragmentListeBinding;
import com.cemalettinaltintas.yemekkitabi.model.Tarif;
import com.cemalettinaltintas.yemekkitabi.roomdb.TarifDao;
import com.cemalettinaltintas.yemekkitabi.roomdb.TarifDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ListeFragment extends Fragment {
    private FragmentListeBinding binding;
    TarifDao tarifDao;
    TarifDatabase db;
    TarifAdapter adapter;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public ListeFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(requireContext(), TarifDatabase.class, "Tarifler").build();
        tarifDao = db.tarifDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
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
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.tarifRecyclerView.setLayoutManager(layoutManager);
        verileriAl();
    }

    private void verileriAl() {
        compositeDisposable.add(
                tarifDao.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(ListeFragment.this::handleResponse)
        );
    }
    private void handleResponse(List<Tarif> tarifList){
        //binding.tarifRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter=new TarifAdapter(tarifList);
        binding.tarifRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.clear();
    }

    public void yeniEkle(View view) {
        Navigation.findNavController(view).navigate(ListeFragmentDirections.actionListeFragmentToTarifFragment("yeni", -1));
    }
}