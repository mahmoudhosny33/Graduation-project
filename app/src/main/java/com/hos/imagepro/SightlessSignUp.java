package com.hos.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hos.imagepro.data.UserDao;
import com.hos.imagepro.data.UserDatabase;
import com.hos.imagepro.room.User;

import java.util.List;

public class SightlessSignUp extends AppCompatActivity {
EditText Name,Phone,Address;
private UserDao userDao;
    List<User> users;
    Boolean Found=true;
     @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sightless_sign_up);
            Button Signup ;
            Signup=findViewById(R.id.signup);
            Name=findViewById(R.id.Name);
            Phone=findViewById(R.id.Phone);
            Address=findViewById(R.id.address);
            userDao= Room.databaseBuilder(this, UserDatabase.class,"User").allowMainThreadQueries().build().getUserDao();

         users=userDao.getAll();
         for (int i=0;i<users.size();i++)
         {
             String inputname=Name.getText().toString();
             if(users.get(i).getName().equals(inputname))
             {
                 Found=false;
                 break;
             }
         }
         Signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputname=Name.getText().toString();
                    String inputphone=Phone.getText().toString();
                    String inputaddress=Address.getText().toString();
                    if(!(inputname.isEmpty()||inputphone.isEmpty()||inputaddress.isEmpty())){
                        User user=new User(inputname,inputphone,inputaddress);
                        User user2=userDao.getPerson(inputname,inputphone);
                    if(user2!=null){
                        Toast.makeText(getApplication(),"Invailed",Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                            userDao.insert(user);
                            Intent intent = new Intent(SightlessSignUp.this,DetectOrCall.class);
                            startActivity(intent);
                        }
                    }
                    else
                        Toast.makeText(getApplication(),"Please Enter Data",Toast.LENGTH_LONG).show();
                }
            });
        }
}