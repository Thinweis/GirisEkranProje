package com.bilal.girisekranproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonGiris, buttonKayit;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Kayit admin = new Kayit();
        admin.ad = "admin";
        admin.soyad = "amdin";
        admin.username = "admin";
        admin.password = "123";
        admin.mail = "akinalperen1998@gmail.com";
        admin.girisTarihi = "12/09/2018";
        admin.mezunTarihi = "12/06/2024";
        Kayit.kayitlar.add(admin);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonGiris = (Button) findViewById(R.id.buttonGiris);
        buttonKayit = (Button) findViewById(R.id.buttonKayit);

        sp = getSharedPreferences("GirisBilgi", MODE_PRIVATE);
        editor = sp.edit();

        username = sp.getString("username", "kullanıcı adı yok");
        password = sp.getString("password", "şifre yok");

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                String uusername = editTextUsername.getText().toString();
                String ppasword = editTextPassword.getText().toString();
                while(i > -1 && i < Kayit.kayitlar.size()){
                    Kayit kayit = Kayit.kayitlar.get(i);
                    if(uusername.equals(kayit.username) && ppasword.equals(kayit.password)){
                        editor.putInt("id",i);
                        editor.commit();
                        startActivity(new Intent(MainActivity.this,AnaEkranActivity.class));
                        finish();
                        i = -1;
                    }
                    else{
                        i++;
                    }
                }
                if(i!=-1) {
                    Toast.makeText(getApplicationContext(), "Kullanıcı Adı veya Şifre Hatalı", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
    }
}