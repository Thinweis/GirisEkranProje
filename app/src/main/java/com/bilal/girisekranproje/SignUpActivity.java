package com.bilal.girisekranproje;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextName, editTextSirname, editTextPasswordSign, editTextUserSign, editTextMail;
    private Button buttonKayitOl;
    private ImageView kayitresim;
    private EditText girisTarih;
    private EditText mezunTarih;
    private String tarih;
    private SimpleDateFormat format;
    private Calendar takvim;
    private Kayit yenikayit = new Kayit();
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }

        kayitresim = (ImageView) findViewById(R.id.kayitresim);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSirname = (EditText) findViewById(R.id.editTextSirname);
        editTextUserSign = (EditText) findViewById(R.id.editTextUserSign);
        editTextPasswordSign = (EditText) findViewById(R.id.editTextPasswordSign);
        buttonKayitOl = (Button) findViewById(R.id.buttonKayitOl);
        editTextMail = (EditText) findViewById(R.id.editTextMail);
        girisTarih = (EditText) findViewById(R.id.girisTarih);
        mezunTarih = (EditText) findViewById(R.id.mezunTarih);

        takvim = Calendar.getInstance();

        girisTarih.setInputType(InputType.TYPE_NULL);
        mezunTarih.setInputType(InputType.TYPE_NULL);

        format = new SimpleDateFormat("dd/MM/yyyy");
        tarih = format.format(takvim.getTime());
        girisTarih.setText(tarih);
        mezunTarih.setText(tarih);

        girisTarih.setOnClickListener(v -> showDateDialog(girisTarih));
        mezunTarih.setOnClickListener(v -> showDateDialog(mezunTarih));

        buttonKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yenikayit.ad = editTextName.getText().toString();
                yenikayit.soyad = editTextSirname.getText().toString();
                yenikayit.username = editTextUserSign.getText().toString();
                yenikayit.password = editTextPasswordSign.getText().toString();
                yenikayit.mail = editTextMail.getText().toString();
                yenikayit.girisTarihi = girisTarih.getText().toString();
                yenikayit.mezunTarihi = mezunTarih.getText().toString();

                String bos = "";

                if(yenikayit.ad.equals("")){
                    bos += "Ad ";
                }
                if(yenikayit.soyad.equals("")){
                    bos += "Soyad ";
                }
                if(yenikayit.username.equals("")){
                    bos += "Kullanıcı Adı ";
                }
                if(yenikayit.password.equals("")){
                    bos += "Şifre ";
                }
                if(yenikayit.mail.equals("")){
                    bos += "Email ";
                }
                if(bos.equals("")){
                    Kayit.kayitlar.add(yenikayit);
                    Toast.makeText(getApplicationContext(), "Kayıt Olundu", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), bos+"Kısımları doldurulmalıdır", Toast.LENGTH_LONG).show();
                }
            }
        });
        kayitresim.setOnClickListener(v -> addImage());
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
        yenikayit.imagePath = image.getAbsolutePath();
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

                    if(ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{"android.permission.CAMERA"}, 2);
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
                        Uri photoUri = FileProvider.getUriForFile(SignUpActivity.this, "com.bilal.girisekranproje.fileprovider", photo);
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
            Bitmap imageBitmap = BitmapFactory.decodeFile(yenikayit.imagePath);

            kayitresim.setImageBitmap(imageBitmap);
        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            Uri selected = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selected, path, null, null, null);

            c.moveToFirst();
            int index = c.getColumnIndex(path[0]);
            yenikayit.imagePath = c.getString(index);
            c.close();

            kayitresim.setImageBitmap(BitmapFactory.decodeFile(yenikayit.imagePath));

        }
    }
    private void showDateDialog(EditText tarih){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                takvim.set(Calendar.YEAR, i);
                takvim.set(Calendar.MONTH, i1);
                takvim.set(Calendar.DAY_OF_MONTH, i2);

                tarih.setText(format.format(takvim.getTime()));
            }
        };

        new DatePickerDialog(SignUpActivity.this, dateSetListener, takvim.get(Calendar.YEAR), takvim.get(Calendar.MONTH), takvim.get(Calendar.DAY_OF_MONTH)).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Cannot proceed without camera permissions.", Toast.LENGTH_SHORT).show();
        }
    }
}