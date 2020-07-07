package com.example.inserttestapplication;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddActivity extends AppCompatActivity {


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date curDate =  new Date(System.currentTimeMillis());
    //获取当前时间
    String   str   =   formatter.format(curDate);

    private EditText row_edit, batch_edit;
    private Spinner type_spinner;
    private ArrayAdapter<String> type_adapter;
    private List<String> TypeList = null;
    private TextView textView;
    private String type;
    private DBOpenHelper connectionClass;

    private String loginUser =LoginActivity.login_user;
    private String  id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        row_edit = (EditText)findViewById(R.id.add_row_edit);
        batch_edit = (EditText)findViewById(R.id.add_batch_edit);

        textView =(TextView)findViewById(R.id.add_status);

        type_spinner = (Spinner)findViewById(R.id.add_type_spin);
        type_spinner.setPrompt("please select a type:");
        TypeList = Arrays.asList(getResources().getStringArray(R.array.typeList));

        type_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TypeList);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(type_adapter);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = getResources().getStringArray(R.array.typeList)[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type = "Please select a type";
            }
        });

        Button add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SendAdd objSend = new SendAdd();
                    objSend.execute("");
                } catch (Exception e) {
                    Toast.makeText(AddActivity.this, "add book failed!", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

    }//end of onCreate








    private class SendAdd extends AsyncTask<String,String,String>
    {
        String msg = "";
        String row_text = row_edit.getText().toString();
        String batch_text = batch_edit.getText().toString();
        String type_text = type;

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


                    // grab selections into a query
                    String sql ="INSERT INTO brick (brick_type,batch ,row, quantity) VALUES( ''"+ type_text + "'',''"+batch_text+"'',''"+row_text+"'', ''300'');";

                    String text ="Add type: "+type_text+",    batch :"+batch_text+",     to row: "+row_text+"  ";


                    //query of insert into add_brick_order
                    String query = "INSERT INTO add_brick_order(manager_id,worker_id,add_text,add_sql,datetime,status) VALUES('1','"+id+"','"+text+"','"+sql+"','"+str+"','Undone');";

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




