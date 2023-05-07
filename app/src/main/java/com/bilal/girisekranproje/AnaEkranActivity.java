package com.bilal.girisekranproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AnaEkranActivity extends AppCompatActivity {
    private Button buttonCikisYap;
    private Button ekle;
    private TextView textVievCikti;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String username;
    private String password;
    private ImageView profil;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);

        profil = (ImageView) findViewById(R.id.profil);
        ekle = (Button) findViewById(R.id.haberekle);

        buttonCikisYap = (Button) findViewById(R.id.buttonCikisYap);
        RecyclerView recyclerView = findViewById(R.id.haberler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(Haber.Haberler, getApplicationContext()));

        sp = getSharedPreferences("GirisBilgi", MODE_PRIVATE);
        editor = sp.edit();

        i = sp.getInt("id",-1);

        buttonCikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.remove("username");
                editor.remove("password");
                editor.commit();

                startActivity(new Intent( AnaEkranActivity.this,MainActivity.class));
                finish();
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( AnaEkranActivity.this,profil2.class));
                finish();
            }
        });

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( AnaEkranActivity.this,HaberEkle.class));
                finish();
            }
        });
    }
}