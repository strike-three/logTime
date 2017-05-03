package com.indra.slyfox.logtime;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Listactivity extends AppCompatActivity {

    handle_log_file_operations read_log = null;
    ArrayList<String> items = new ArrayList<String>();
    MyCustomAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listactivity);


        ListView lv = (ListView) findViewById(R.id.list_data);

        read_log = new handle_log_file_operations(getFilesDir().getAbsolutePath().toString());
        /*ArrayAdapter adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adp);*/

        read_log.read_log_events();

        int logsize = read_log.build_report();

       // Toast.makeText(Listactivity.this, "log list " + logsize, Toast.LENGTH_SHORT).show();
        int rec_num = 0;

        if(logsize > 0) {
            for (rec_num = 0; rec_num < logsize; rec_num++) {
                items.add(read_log.fetch_at_idx(rec_num));
               // System.out.println("items appended " + rec_num + items.get(rec_num).toString());
            }
        }
        else
        {
            items.add("No Records; --");
        }

        update_list(items);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder a = new AlertDialog.Builder(Listactivity.this);
                a.setTitle("Attention");
                a.setMessage("Confirm Delete");
                a.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichbutton) {
                        reload_list(position);
                    }
                });
                a.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichbutton) {

                    }
                });
                a.show();
                return true;
            }
        });
    }

    private void reload_list(int position)
    {
        ArrayList<String> item_temp = new ArrayList<>();

        boolean retval = read_log.delete_selected(position);
        if(retval)
        {

            int logsize = read_log.build_report();
           // System.out.println("logsize "+ logsize);
            //System.out.println("logsize "+ read_log.fetch_at_idx(0));
            int rec_num = 0;

            if(logsize > 0) {
                for (rec_num = 0; rec_num < logsize; rec_num++) {

                    item_temp.add(read_log.fetch_at_idx(rec_num));

                }
            }
            else
            {
                item_temp.add("No Records; --");
            }
            update_list(item_temp);

        }
    }

    private void update_list(ArrayList<String> al)
    {
        ListView lv = (ListView) findViewById(R.id.list_data);

        adapter = null;
        adapter = new MyCustomAdapter(this, al, al.size());
        lv.setAdapter(adapter);
    }
}