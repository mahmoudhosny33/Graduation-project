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
import com.hos.imagepro.data.VolunteerDao;
import com.hos.imagepro.room.Volunteer;

public class VolunteerLogin extends AppCompatActivity {
    Button login ;
    TextView signup;
    TextView forgot;
    EditText name , phone ;
    UserDatabase userDatabase;
    VolunteerDao volunteerDao;
    SharedPreferences mprefs;
    CheckBox remeber;
    private String prefs_Name="Prefs_Filee";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_login);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        remeber = findViewById(R.id.Remeber2);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.Phone);
        forgot = findViewById(R.id.forgotpass);
        userDatabase= Room.databaseBuilder(this, UserDatabase.class,"Volunteer").allowMainThreadQueries().build();
        volunteerDao =userDatabase.getVolunteerDao();
        mprefs=getSharedPreferences(prefs_Name,MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputname=name.getText().toString();
                String inputphone=phone.getText().toString();
                Volunteer volunteer=volunteerDao.getVolunteer(inputname,inputphone);
                if(volunteer!=null){
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
                    Toast.makeText(getApplicationContext(),"Thanks to your Effort",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(VolunteerLogin.this, Request.class);
                startActivity(intent);
                }
                else{
                    Toast.makeText(getApplication(),"Invailed",Toast.LENGTH_LONG).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerLogin.this, VolunteerSignUp.class);
                startActivity(intent);
            }
        });
        getPreferencesData();
    }
    private void getPreferencesData() {
        SharedPreferences sp=getSharedPreferences(prefs_Name,MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u=sp.getString("pref_name","not found");
            name.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p=sp.getString("pref_pass","not found");
            phone.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b=sp.getBoolean("pref_check",false);
            remeber.setChecked(b);
        }

    }
}