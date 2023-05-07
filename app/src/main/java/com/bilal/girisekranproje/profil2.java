package com.bilal.girisekranproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class profil2 extends AppCompatActivity {
    private Button buttonAna;
    private TextView TextAd;
    private TextView TextSoyad;
    private TextView Textemail;
    private TextView Tarih;
    private ImageView profilresim;
    private SharedPreferences sp;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil2);

        buttonAna = (Button) findViewById(R.id.buttonAna);
        TextAd = (TextView) findViewById(R.id.TextAd);
        TextSoyad = (TextView) findViewById(R.id.TextSoyad);
        Textemail = (TextView) findViewById(R.id.Textemail);
        profilresim = (ImageView) findViewById(R.id.profilresim);
        Tarih = (TextView) findViewById(R.id.tarih);


        sp = getSharedPreferences("GirisBilgi", MODE_PRIVATE);
        i = sp.getInt("id",-1);

        TextAd.setText("İsim: "+Kayit.kayitlar.get(i).ad);
        TextSoyad.setText("Soyisim: "+Kayit.kayitlar.get(i).soyad);
        Textemail.setText("İletişim: "+Kayit.kayitlar.get(i).mail);
        Tarih.setText("Giriş Tarihi: "+Kayit.kayitlar.get(i).girisTarihi + " Mezun Tarihi: " + Kayit.kayitlar.get(i).mezunTarihi);
        if(Kayit.kayitlar.get(i).imagePath!=null) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(Kayit.kayitlar.get(i).imagePath);
            profilresim.setImageBitmap(imageBitmap);
        }
        buttonAna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent( profil2.this,AnaEkranActivity.class));
            }
        });

    }
}