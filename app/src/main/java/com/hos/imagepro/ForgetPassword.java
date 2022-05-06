package com.hos.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hos.imagepro.data.UserDao;
import com.hos.imagepro.data.UserDatabase;
import com.hos.imagepro.room.User;

public class ForgetPassword extends AppCompatActivity {
  EditText username,phone;
  Button save;
  UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
       username=findViewById(R.id.nameforgrt);
       phone=findViewById(R.id.phoneforget);
       save=findViewById(R.id.save);
       userDao= Room.databaseBuilder(this, UserDatabase.class,"Usser").allowMainThreadQueries().build().getUserDao();

       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String inputphone=phone.getText().toString();
               String inputusername=username.getText().toString();
               if(!(inputphone.isEmpty()&&inputusername.isEmpty())){
               String user=userDao.getuser(inputphone);
               if(user!=null)
               {
                   User user1=userDao.getPerson(inputusername,inputphone);
                   userDao.update(user1);
               }
               else
                   {
                       Toast.makeText(getApplicationContext(),"This Name Not Found",Toast.LENGTH_LONG).show();
                   }
               }

           }
       });



    }
}