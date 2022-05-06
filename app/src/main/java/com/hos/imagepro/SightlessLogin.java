package com.hos.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hos.imagepro.data.UserDao;
import com.hos.imagepro.data.UserDatabase;
import com.hos.imagepro.room.User;

public class SightlessLogin extends AppCompatActivity {
    EditText Name,Phone;
    private UserDao userDao;
    private UserDatabase userDatabase;
    SharedPreferences mprefs;
    CheckBox remeber;
    TextView forgetpassword;
    private String prefs_Name="Prefs_File";
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sightless_login);
            Button login ;
            TextView signup;
            login = findViewById(R.id.login);
            signup = findViewById(R.id.signup);
            Name=findViewById(R.id.namesightless);
            Phone=findViewById(R.id.Phone);
            remeber=findViewById(R.id.Remeber);
            forgetpassword=findViewById(R.id.forgotpass);

            forgetpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SightlessLogin.this,ForgetPassword.class);
                    startActivity(intent);
                }
            });

        mprefs=getSharedPreferences(prefs_Name,MODE_PRIVATE);
        userDatabase= Room.databaseBuilder(this, UserDatabase.class,"User").allowMainThreadQueries().build();
        userDao=userDatabase.getUserDao();

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputname=Name.getText().toString();
                    String inputphone=Phone.getText().toString();
                    if(!(inputname.isEmpty()&&inputphone.isEmpty())){
                    User user=userDao.getPerson(inputname,inputphone);
                    if(user!=null)
                    {
                        if(remeber.isChecked()){
                            boolean isCheck=remeber.isChecked();
                            SharedPreferences.Editor editor= mprefs.edit();
                            editor.putString("pref_name",inputname);
                            editor.putString("pref_pass",inputphone);
                            editor.putBoolean("pref_check",isCheck);
                            editor.apply();
                            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
                        }
                        else{
                            mprefs.edit().clear().apply();
                        }
                        Intent intent=new Intent(SightlessLogin.this,Recognition.class);
                        startActivity(intent);
                    }
                else{
                    Toast.makeText(getApplication(),"Invailed",Toast.LENGTH_LONG).show();
                    }
                    }
                    else
                        Toast.makeText(getApplication(),"Please Enter Data",Toast.LENGTH_LONG).show();

                }

            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SightlessLogin.this, SightlessSignUp.class);
                    startActivity(intent);
                }
            });
        getPreferencesData();
        }
    private void getPreferencesData() {
        SharedPreferences sp=getSharedPreferences(prefs_Name,MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u=sp.getString("pref_name","not found");
            Name.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p=sp.getString("pref_pass","not found");
            Phone.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b=sp.getBoolean("pref_check",false);
            remeber.setChecked(b);
        }

    }
}