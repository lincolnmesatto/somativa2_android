package com.pucpr.somativaapp.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.pucpr.somativaapp.R;
import com.pucpr.somativaapp.model.Colecao;
import com.pucpr.somativaapp.model.DataModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormActivity extends AppCompatActivity {

    static final int CAMERA_PERMISSION_CODE = 2001;
    static final int CAMERA_INTENT_CODE = 3001;

    ImageView imageViewCamera;
    EditText editTextTitulo;
    EditText editTextUltimo;
    CheckBox checkBox;
    SeekBar seekBar;

    String picturePath;
    int position;
    int rate;
    Colecao colecao = new Colecao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        imageViewCamera = findViewById(R.id.imageViewCamera);
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextUltimo = findViewById(R.id.editTextUltimo);
        checkBox = findViewById(R.id.checkBox);
        seekBar = findViewById(R.id.seekBar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getInt("position");

            colecao = DataModel.getInstance().getColecoes().get(position);
            editTextTitulo.setText(colecao.getTitulo());
            editTextUltimo.setText(String.valueOf(colecao.getUltimoLido()));
            checkBox.setChecked(colecao.getCompleto() == 0 ? false : true);
            seekBar.setProgress(colecao.getRate());
        }else{
            position = -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(colecao.getCaminhoImg() != null) {
            File file = new File(colecao.getCaminhoImg());
            if (file.exists()) {
                imageViewCamera.setImageURI(Uri.fromFile(file));
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                rate = progressChangedValue;
                Toast.makeText(FormActivity.this, "O valor selecionado é:" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void buttonCameraClicked(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestCameraPermission();
        }else{
            sendCameraIntent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestCameraPermission(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{ Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }else{
                sendCameraIntent();
            }
        }else{
            Toast.makeText(FormActivity.this,"Nenhuma câmera disponível",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendCameraIntent();
            }else{
                Toast.makeText(FormActivity.this,"Permissão da câmera negada",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void sendCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION,true);

        if(intent.resolveActivity(getPackageManager()) != null){
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String picName = "pic_"+timeStamp;

            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File pictureFile = null;

            try {
                pictureFile = File.createTempFile(picName,".jpg",dir);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(pictureFile != null){
                picturePath = pictureFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(FormActivity.this,
                        "com.pucpr.somativaapp.fileprovider", pictureFile
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,CAMERA_INTENT_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT_CODE){
            if(resultCode == RESULT_OK){
                File file = new File(picturePath);
                if(file.exists()){
                    imageViewCamera.setImageURI(null);
                    imageViewCamera.setImageURI(Uri.fromFile(file));
                    colecao.setCaminhoImg(picturePath);
                }
            }else{
                Toast.makeText(FormActivity.this,"Problema ao pegar a foto da câmera", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void btnSaveClicked(View view){
        boolean checked = checkBox.isChecked();

        Colecao c = position >= 0 ?
                  new Colecao(colecao.getId(), editTextTitulo.getText().toString(), picturePath == null ? colecao.getCaminhoImg() : picturePath,
                          checked ? 1 : 0, Integer.valueOf(editTextUltimo.getText().toString()), rate)
                : new Colecao(editTextTitulo.getText().toString(), picturePath, checked ? 1 : 0, Integer.valueOf(editTextUltimo.getText().toString()), rate);

        if(position >= 0)
            DataModel.getInstance().updateColecao(c);
        else
            DataModel.getInstance().addColecao(c);

        Intent intent = new Intent(FormActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}