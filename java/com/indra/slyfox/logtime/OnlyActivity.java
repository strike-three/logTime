package com.indra.slyfox.logtime;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;


import java.util.ArrayList;




public class OnlyActivity extends AppCompatActivity {

    private ArrayList<String> pop_projects = new ArrayList<String>();
    private handle_project_list proj_list_handler = null;
    private handle_log_file_operations handle_log_file = null;
    private ArrayAdapter list_adp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only);

        proj_list_handler = new handle_project_list(getFilesDir().getAbsolutePath().toString());
        handle_log_file = new handle_log_file_operations(getFilesDir().getAbsolutePath().toString());
        final ListView disp_list = (ListView) findViewById(R.id.project_names);

        int idx = proj_list_handler.read_project_list();

        //System.out.println("list length " + idx + " " + proj_list_handler.projects.get(0));
         //idx = the_lists.get_project_list_length();

        do{
            pop_projects.add(proj_list_handler.projects.get(--idx));
            //pop_projects.add(the_lists.get_activity_at_index(--idx));
        }while(idx != 0);

        list_adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pop_projects);
        disp_list.setAdapter(list_adp);

        disp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int listlen = pop_projects.size();
                if(position == (listlen - 1)) {
                    showDialog();
                }
                else
                {
                    handle_log_file.log_event(pop_projects.get(position).toString());
                    Toast.makeText(OnlyActivity.this, "event logged", Toast.LENGTH_SHORT).show();
                }

            }
        });

        disp_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            public boolean onItemLongClick(AdapterView parent, View v, final int position, long id)
            {
                int listlen = pop_projects.size() - 1;
                if(position == listlen)
                {
                    Toast.makeText(OnlyActivity.this, "Cannot delete system label", Toast.LENGTH_SHORT).show();
                }
                else {
                    final AlertDialog.Builder a = new AlertDialog.Builder(OnlyActivity.this);
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
                }

                //list_adp.remove(list_adp.getItem(position));
                return true;
            }

        }
        );
    }

    public void reload_list(int pos)
    {

        boolean retval = proj_list_handler.delete_selected(pos);
        if(retval) {
            pop_projects.clear();

            int idx = proj_list_handler.read_project_list();
            do {
                pop_projects.add(proj_list_handler.projects.get(--idx));
                //pop_projects.add(the_lists.get_activity_at_index(--idx));
            } while (idx != 0);

            list_adp.notifyDataSetChanged();
        }
    }

    public void populate_list(View v)
    {
        Intent intent = new Intent(OnlyActivity.this, Listactivity.class);
        startActivity(intent);
    }

    public void write_end_rec(View v)
    {
        handle_log_file.log_event("end_string");
        Toast.makeText(OnlyActivity.this, "end time logged", Toast.LENGTH_SHORT).show();
    }

    public void delete_log(View v)
    {
        if(handle_log_file.delete_log())
        {
            Toast.makeText(OnlyActivity.this, "Log deleted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(OnlyActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }

    protected void showDialog()
    {
        View viw = (LayoutInflater.from(OnlyActivity.this)).inflate(R.layout.user_input, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OnlyActivity.this);
        alertBuilder.setTitle("Enter Project Title");
        alertBuilder.setView(viw);

        final EditText userinp = (EditText) viw.findViewById(R.id.user_input);

        alertBuilder.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pop_projects.add(0, userinp.getText().toString());
                        proj_list_handler.append_project_name((userinp.getText().toString() + "\n"), true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                     @Override
                    public void onClick(DialogInterface dialog, int which){
                      Toast.makeText(OnlyActivity.this, "Nothing added", Toast.LENGTH_SHORT).show();
                    }
        });

        Dialog dialog = alertBuilder.create();
        dialog.show();
    }
}
