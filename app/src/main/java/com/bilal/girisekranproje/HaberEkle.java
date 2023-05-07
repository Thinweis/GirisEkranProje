package com.bilal.girisekranproje;

import static com.bilal.girisekranproje.SignUpActivity.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class HaberEkle extends AppCompatActivity {

    private EditText haberekle;
    private ImageView haberresim;
    private Button ekle;
    private Haber yenihaber = new Haber();

    private SharedPreferences sp;

    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_ekle);

        haberekle = (EditText) findViewById(R.id.haber);
        haberresim = (ImageView) findViewById(R.id.haberinresmi);
        ekle = (Button) findViewById(R.id.ekle);

        haberresim.setOnClickListener(v -> addImage());

        sp = getSharedPreferences("GirisBilgi", MODE_PRIVATE);
        i = sp.getInt("id",-1);

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yenihaber.baslik = haberekle.getText().toString();
                yenihaber.yazar = Kayit.kayitlar.get(i).ad + " " + Kayit.kayitlar.get(i).soyad;
                Haber.Haberler.add(yenihaber);

                startActivity(new Intent(HaberEkle.this,AnaEkranActivity.class));
                finish();
            }
        });
    }
    private File imageFile() throws IOException {

        String imageFileName = "Entry_" + Kayit.kayitlar.size() + "";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        yenihaber.imagepath = image.getAbsolutePath();
        return image;

    }

    private void addImage(){
        final CharSequence[] options = {"Camera", "From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(options[i].equals("Camera")){

                    if(ActivityCompat.checkSelfPermission(HaberEkle.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(HaberEkle.this, new String[]{"android.permission.CAMERA"}, 2);
                    }

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent);

                    File photo = null;
                    try {
                        photo = imageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(photo != null){
                        Uri photoUri = FileProvider.getUriForFile(HaberEkle.this, "com.bilal.girisekranproje.fileprovider", photo);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                else if (options[i].equals("From Gallery")){
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if(options[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }

            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap imageBitmap = BitmapFactory.decodeFile(yenihaber.imagepath);

            haberresim.setImageBitmap(imageBitmap);
        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            Uri selected = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selected, path, null, null, null);

            c.moveToFirst();
            int index = c.getColumnIndex(path[0]);
            yenihaber.imagepath = c.getString(index);
            c.close();

            haberresim.setImageBitmap(BitmapFactory.decodeFile(yenihaber.imagepath));

        }
    }
}