package com.indra.slyfox.logtime;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by slyfox on 06/03/17.
 */

public class handle_project_list {
    private String proj_file_dir_path = " ";
    private String proj_file_path = " ";
    private File proj_file_dir = null;
    private File proj_file = null;
    public ArrayList<String> projects = new ArrayList<String>();

    public handle_project_list(String path)
    {
        proj_file_dir_path = path + File.separator + "Log";
        proj_file_path = proj_file_dir_path + File.separator + "projects.txt";

        proj_file_dir = new File(proj_file_dir_path);
        proj_file = new File(proj_file_path);

        /*System.out.println("dir path " + proj_file_dir.getAbsolutePath().toString());
        System.out.println("file path " + proj_file.getAbsolutePath().toString());*/

        if(!proj_file_dir.exists())
        {
            proj_file_dir.mkdirs();
        }

        try{
            if(proj_file.createNewFile())
            {
                //System.out.println("Create successful");
                String list1[] = proj_file_dir.list();
               // System.out.println(list1.length);
                append_project_name(("[+] add Events\n"), true);
            }
            else
            {
                //System.out.println("Create unsuccessful");
                String list[] = proj_file_dir.list();
               /* System.out.println(list.length);
                System.out.println(proj_file.isFile());
                System.out.println(proj_file.length());*/
                /*if(proj_file.delete())
                {
                    System.out.println("Delete successful");

                }*/
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

     public boolean append_project_name(String name, boolean append)
    {
        boolean retval = false;

        try{
            FileWriter fw = new FileWriter(proj_file, append);
            BufferedWriter bw = new BufferedWriter(fw);
            //System.out.println("name to save " + name);
            bw.write(name);
            bw.close();
            retval = true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            retval = false;
        }

        return retval;
    }

   public int read_project_list()
    {
        int lines_read = 0;
        try{
            FileReader fir = new FileReader(proj_file);
            BufferedReader br = new BufferedReader(fir);

            String line = " ";

            while((line = br.readLine()) != null)
            {
                projects.add(line);
                lines_read = lines_read + 1;
                //System.out.println("name read " + line);
            }
            br.close();

        } catch (Exception e)
        {
            lines_read = 0;
        }

        return lines_read;
    }

    public boolean delete_selected(int position)
    {
        boolean success;
        int item_pos;

        projects.clear();
        int total_proj = read_project_list();

        /*for(int i = 0; i < total_proj; i = i + 1)
        {
            System.out.println("project " + i + " " + projects.get(i));
        }*/
        item_pos = ((projects.size() - 1) - position);
       // System.out.println("selected " + projects.get(item_pos));
        projects.remove(item_pos);

        /*for(int i = 0; i < projects.size(); i = i + 1)
        {
            System.out.println("project ad " + i + " " + projects.get(i));
        }*/
        success = append_project_name("", false);


        for(int i = 0; i < projects.size(); i = i + 1)
        {
            append_project_name((projects.get(i).toString() + "\n"), true);
        }

        return success;
    }
}
