package com.example.inserttestapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    public static String login_user;
    private Button login;
    private EditText username, password;
    private ProgressBar progressBarar;
    private DBOpenHelper connectionClass;

    private Button change_ip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login= (Button)findViewById(R.id.login_button);
        username = (EditText)findViewById(R.id.username_edit);
        password = (EditText)findViewById(R.id.userpassword_edit);
        progressBarar = (ProgressBar)findViewById(R.id.progressBar);
        progressBarar.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute("");

            }
        });


        change_ip=(Button)findViewById(R.id.ip_button);


        change_ip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                //    设置Title的内容
                builder.setTitle("Change ip as :");
                //    设置Content来显示一个信息


                final EditText aText = new EditText(LoginActivity.this);
                builder.setView(aText);

                //builder.setView( new EditText(LoginActivity.this));





                //    设置一个PositiveButton
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String a = aText.getText().toString();

                        Toast.makeText(LoginActivity.this, "successful: " + a, Toast.LENGTH_SHORT).show();

                          DBOpenHelper.ip = a;


                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(LoginActivity.this, "Canceled " , Toast.LENGTH_SHORT).show();
                    }
                });

                //    显示出该对话框
                builder.show();
            }


        });





    }//End of OnCreate



    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressBarar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            Statement stmt = null;
            String usernam = username.getText().toString();
            String passwords = password.getText().toString();
            if (usernam.trim().equals("") || passwords.trim().equals("")) {
                z = "Please enter Username and Password";
            } else {
                try {
                    Connection conn = connectionClass.getConn(); //Connection Object

                    if (conn == null) {
                        z = "check your internet access!";
                    } else {
                        String query = "SELECT * from worker where username = '" + usernam.toString() + "'and  password ='" + passwords.toString()+"'";
                        stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            z = "Login successful";
                            isSuccess = true;
                            conn.close();
                            login_user = usernam.toString();

                        } else {
                            z = "Invalid Credentials !";
                            isSuccess = false;
                        }
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    z = e.getMessage();
                }
            }


            return z;
        }


        @Override
        protected void onPostExecute(String r)
        {
            progressBarar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this,r,Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {

                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }





    }





}
