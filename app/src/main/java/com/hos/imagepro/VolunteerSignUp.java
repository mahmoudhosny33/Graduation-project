package com.hos.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hos.imagepro.data.UserDao;
import com.hos.imagepro.data.UserDatabase;
import com.hos.imagepro.data.VolunteerDao;
import com.hos.imagepro.room.Volunteer;

public class VolunteerSignUp extends AppCompatActivity {
    Button Signup ;
    EditText name , phone ,day ,time ;
    Spinner type;
    VolunteerDao volunteerDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_sign_up);
        Signup=findViewById(R.id.signup);
        name=findViewById(R.id.Name);
        phone=findViewById(R.id.Phone);
        type=findViewById(R.id.type);
        day=findViewById(R.id.day);
        time=findViewById(R.id.time);
        volunteerDao= Room.databaseBuilder(this, UserDatabase.class,"Volunteer").allowMainThreadQueries().build().getVolunteerDao();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputname=name.getText().toString();
                String inputphone=phone.getText().toString();
                String inputSpecialization=type.getSelectedItem().toString();
                String inputday=day.getText().toString();
                float inputtime= Float.parseFloat(time.getText().toString());
                if(!(inputname.isEmpty()||inputphone.isEmpty()||inputday.isEmpty()||inputSpecialization.isEmpty()||inputtime==0)){
                    Volunteer volunteer=volunteerDao.getVolunteer(inputname,inputphone);
                    if(volunteer!=null){
                        Toast.makeText(getApplication(),"Invailed",Toast.LENGTH_LONG).show();
                    }
                    else {
                Volunteer volunteer2=new Volunteer(inputname,inputphone,inputSpecialization,inputday,inputtime);
                volunteerDao.insertVolunteer(volunteer2);
                Intent intent = new Intent(VolunteerSignUp.this,VolunteerLogin.class);
                startActivity(intent);}
            }
                else
                    Toast.makeText(getApplication(),"Please Enter Data",Toast.LENGTH_LONG).show();
            }
        });
    }
}