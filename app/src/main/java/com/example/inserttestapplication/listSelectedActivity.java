package com.example.inserttestapplication;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class listSelectedActivity extends AppCompatActivity {

    private ArrayList<recycle_List_helper> itemArrayList;  //List items Array
    private MyAppAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    private DBOpenHelper connectionClass; //Connection Class Variable

    private String selected_item ;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);




        itemArrayList = new ArrayList<recycle_List_helper>(); // Arraylist Initialization

        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(listSelectedActivity.this, "Synchronising",
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

                    selected_item = MainActivity.a;

                    String get_name_query="SELECT brick_type from brick WHERE brick_id ='"+selected_item+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs1 = stmt.executeQuery(get_name_query);

                    while(rs1.next()) {
                        type = rs1.getString("brick_type");
                    }

                    // Change below query according to your own database.
                    String query = "SELECT Brick_type,batch,row , quantity FROM brick where brick_type = '"+type+"' order by row asc";
                    Statement stmt2 = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                itemArrayList.add(new recycle_List_helper(rs.getString("brick_type"), rs.getString("batch"), rs.getString("row"),rs.getString("quantity")));
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
            Toast.makeText(listSelectedActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, listSelectedActivity.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        }
    }

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<recycle_List_helper> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView textType;
            public TextView textBatch;
            public TextView textRow;
            public TextView textQuantity;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                textType = (TextView) v.findViewById(R.id.text_type);
                textBatch = (TextView) v.findViewById(R.id.text_batch);
                textRow = (TextView) v.findViewById(R.id.text_row);
                textQuantity = (TextView)v.findViewById(R.id.text_quantity);
            }
        }

        // Constructor
        public MyAppAdapter(List<recycle_List_helper> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.text_list_helper, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final recycle_List_helper classListItems = values.get(position);
            holder.textType.setText(classListItems.getType());
            holder.textBatch.setText(classListItems.getBatch());
            holder.textRow.setText(classListItems.getRow());
            holder.textQuantity.setText(classListItems.getQuantity());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }

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