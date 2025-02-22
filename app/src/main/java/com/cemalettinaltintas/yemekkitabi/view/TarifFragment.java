package com.cemalettinaltintas.yemekkitabi.view;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.room.Room;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cemalettinaltintas.yemekkitabi.databinding.FragmentTarifBinding;
import com.cemalettinaltintas.yemekkitabi.model.Tarif;
import com.cemalettinaltintas.yemekkitabi.roomdb.TarifDao;
import com.cemalettinaltintas.yemekkitabi.roomdb.TarifDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarifFragment extends Fragment {
    private FragmentTarifBinding binding;

    ActivityResultLauncher<String> permissionResultLauncher;
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri secilenGorsel;
    Bitmap secilenBitmap;
    String bilgi;
    TarifDao tarifDao;
    TarifDatabase db;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLaunher();
        db= Room.databaseBuilder(requireContext(),TarifDatabase.class,"Tarifler").build();
        tarifDao=db.tarifDao();
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
            bilgi= TarifFragmentArgs.fromBundle(getArguments()).getBilgi();
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
        String isim=binding.isimText.getText().toString();
        String malzeme=binding.malzemeText.getText().toString();
        if (secilenBitmap!=null){
            Bitmap kucukBitmap=kucukBitmapOlustur(secilenBitmap,300);
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            byte[] byteDizisi=outputStream.toByteArray();
            Tarif tarif=new Tarif(isim,malzeme,byteDizisi);

            //Threading


        }
    }

    private void handleResponseForInsert(){
        //Bir önceki fragmenta dön.
        NavDirections action= TarifFragmentDirections.actionTarifFragmentToListeFragment();
        Navigation.findNavController(requireView()).navigate(action);

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
    public Bitmap kucukBitmapOlustur(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}