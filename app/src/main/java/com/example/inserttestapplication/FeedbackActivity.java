package com.example.inserttestapplication;

import android.app.Activity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;


class FeedbackActivity extends AppCompatActivity {

    //Definitions


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date curDate =  new Date(System.currentTimeMillis());
    //获取当前时间
    String   str   =   formatter.format(curDate);

    String id;
    String loginUser =  LoginActivity.login_user; //LoginActivity.login_user;
    TextView textView;
    EditText editText;
    RelativeLayout relativeLayout;
    Button hand_in_btn;
    private DBOpenHelper connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        hand_in_btn = (Button)findViewById(R.id.feed_back_btn);

        relativeLayout = (RelativeLayout)findViewById(R.id.feed_back_container);

        textView =(TextView)findViewById(R.id.feed_back_text_title);
        editText = (EditText)findViewById(R.id.fee_back_edit);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        hand_in_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "Sorry, I can't hear you",
                            Toast.LENGTH_SHORT).show();
                    //此处写处理逻辑
                } else {
                    Toast.makeText(getApplicationContext(), "your feedback Has been accepted！",
                            Toast.LENGTH_SHORT).show();
                    FeedbackActivity.this.finish();

                }


                Send objSend = new Send();
                objSend.execute("");
            }
        });



    }




    private class Send extends AsyncTask<String,String,String>
    {
        String msg = "";
        String text = editText.getText().toString();


        @Override
        protected void onPreExecute() { textView.setText("Please wait inserting Data");}


        @Override
        protected String doInBackground(String... strings) {

            try
            {
                Connection conn = connectionClass.getConn(); //Connection Object

                if(conn==null)
                {
                    msg = "Connection goes wrong";
                }
                else
                {
                    String get_name_query="SELECT worker_id from worker WHERE username ='"+loginUser+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(get_name_query);

                    while(rs.next()) {
                        id = rs.getString("worker_id");
                    }

                    String query ="INSERT INTO feedback (worker_id, manager_id , datetime, text) VALUES( '"+ id + "','1','"+str+"','"+text+"');";

                    Statement stmt2 = conn.createStatement();
                    int rs2 = stmt2.executeUpdate(query);
                    msg="Inserting Successful";
                }//end of else
                conn.close();
            }//end of try
            catch(Exception e)
            {
                msg = "Connection goes wrong";
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg){ textView.setText(msg);}
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
