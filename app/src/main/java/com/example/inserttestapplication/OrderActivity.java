package com.example.inserttestapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class OrderActivity extends AppCompatActivity {


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date curDate =  new Date(System.currentTimeMillis());
    //获取当前时间
    String   str   =   formatter.format(curDate);


    private ArrayList<order_List_helper> itemArrayList;  //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    private DBOpenHelper connectionClass; //Connection Class Variable


    public static String selected_order;

    String id;
    String loginUser = LoginActivity.login_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        itemArrayList = new ArrayList<order_List_helper>(); // Arraylist Initialization

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");


    }//end of onCreate

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(OrderActivity.this, "Synchronising",
                    "RecyclerView Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Connection conn = connectionClass.getConn(); //Connection Object
                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.
                    String get_name_query = "SELECT worker_id from worker WHERE username ='" + loginUser + "'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(get_name_query);

                    while (rs.next()) {
                        id = rs.getString("worker_id");
                    }

                    String query = "SELECT text from order_brick where worker_id = '" + id + "' and status = 'Undone'";

                    Statement stmt2 = conn.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query);

                    if (rs2 != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs2.next()) {
                            try {
                                itemArrayList.add(new order_List_helper(rs2.getString("text")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            progress.dismiss();
            Toast.makeText(OrderActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, OrderActivity.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }


    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<order_List_helper> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView textName;

            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                textName = (TextView) v.findViewById(R.id.textName);

            }
        }

        // Constructor
        public MyAppAdapter(List<order_List_helper> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.order_list_content, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {


            final order_List_helper classListItems = values.get(position);

            holder.textName.setText(classListItems.getOrderText());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //   Toast.makeText(view.getContext(), "Clicked : "+classListItems.getText, Toast.LENGTH_SHORT).show();

                    selected_order = classListItems.getText;

                    Toast.makeText(view.getContext(), "Clicked : " + selected_order, Toast.LENGTH_SHORT).show();

                    //Start Create the alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alert：Did you finished it ?");
                    builder.setMessage(selected_order);
                    //点击对话框以外的区域是否让对话框消失
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "finished the work", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            ExecuteData executed = new ExecuteData();
                            executed.execute("");
                            restartActivity(OrderActivity.this);

                        }
                    });
                    //设置反面按钮
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });


                    builder.show();
                    //finished dialog


                }//finished onclick


            });//finished onClick Listeners


        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }

    }//finished the adapter create class


    private class ExecuteData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(OrderActivity.this, "Synchronising",
                    "RecyclerView Loading! Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Connection conn = connectionClass.getConn(); //Connection Object
                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.

                    String query = "UPDATE order_brick SET status = 'Did', \n" +
                            " datetime = '" +str+
                            "' WHERE text = '" + selected_order + "'";

                    Statement stmt2 = conn.createStatement();
                    int rs2 = stmt2.executeUpdate(query);
                    msg = "Inserting Successful";

                }
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            progress.dismiss();
            Toast.makeText(OrderActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, OrderActivity.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }


    public static void restartActivity(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        activity.startActivity(intent);
        activity.overridePendingTransition(0,0);
        activity.finish();
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





