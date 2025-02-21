package com.cemalettinaltintas.yemekkitabi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Visibility;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cemalettinaltintas.yemekkitabi.databinding.FragmentTarifBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class TarifFragment extends Fragment {
    private FragmentTarifBinding binding;

    ActivityResultLauncher<String> permissionResultLauncher;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri secilenGorsel;
    Bitmap secilenBitmap;
    String bilgi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLaunher();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTarifBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageView.setOnClickListener(this::gorselSec);
        binding.kaydetButton.setOnClickListener(this::kaydet);
        binding.silButton.setOnClickListener(this::sil);

        if (getArguments()!=null){
            bilgi=TarifFragmentArgs.fromBundle(getArguments()).getBilgi();
            binding.silButton.setEnabled(false);
            binding.kaydetButton.setEnabled(true);
            binding.isimText.setText("");
            binding.malzemeText.setText("");
        }else{
            binding.silButton.setEnabled(true);
            binding.kaydetButton.setEnabled(false);
        }
    }

    public void kaydet(View view) {

    }

    public void gorselSec(View view) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                //İzin verilmemiş, izin istememiz gerekiyor.
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)) {
                    //Snacbar gösterebiliriz. Kullanıcıdan neden izin istediğimizi bir kez daha söyleyerek izin istememiz lazım.
                    Snackbar.make(view, "Galeriye ulaşıp görsel seçmemiz lazım!", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //İzin isteyeceğiz.
                            permissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                } else {
                    //İzin isteyeceğiz
                    permissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else {
                //İzin verilmiş, galeriye gidebilirim.
                Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //İzin verilmemiş, izin istememiz gerekiyor.
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Snacbar gösterebiliriz. Kullanıcıdan neden izin istediğimizi bir kez daha söyleyerek izin istememiz lazım.
                    Snackbar.make(view, "Galeriye ulaşıp görsel seçmemiz lazım!", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //İzin isteyeceğiz.
                            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                } else {
                    //İzin isteyeceğiz
                    permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                //İzin verilmiş, galeriye gidebilirim.
                Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }
    }

    public void sil(View view) {
    }

    public void registerLaunher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == AppCompatActivity.RESULT_OK) {
                    Intent intentFromResult=o.getData();
                    if (intentFromResult!=null){
                        secilenGorsel=intentFromResult.getData();//Bunu bitmapa çevirip imageivew içerisine koyacağız.
                        try {
                            if (Build.VERSION.SDK_INT>=28){
                                //Yeni yöntem
                                ImageDecoder.Source source= ImageDecoder.createSource(requireActivity().getContentResolver(),secilenGorsel);
                                secilenBitmap=ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(secilenBitmap);
                            }else{
                                //Eski yöntem
                                secilenBitmap= MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),secilenGorsel);
                                binding.imageView.setImageBitmap(secilenBitmap);
                            }
                        } catch (IOException e) {
                            e.getLocalizedMessage();
                        }
                    }
                } else {

                }
            }
        });
        permissionResultLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                if (o){
                    //izin verildi.
                    //Galeriye gidebiliriz
                    Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    //İzin verilmedi.
                    Toast.makeText(requireContext(), "İzin verilmedi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}