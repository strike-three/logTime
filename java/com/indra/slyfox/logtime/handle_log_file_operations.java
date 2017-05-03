package com.indra.slyfox.logtime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by slyfox on 08/03/17.
 */

public class handle_log_file_operations {

    private String log_file_dir_path = " ";
    private String log_file_path = " ";
    private File log_file_dir = null;
    private File log_file = null;
    public ArrayList<String> log = new ArrayList<String>();
    private ArrayList<String> activity = new ArrayList<String>();
    private ArrayList<Long> time = new ArrayList<Long>();
    private ArrayList<String> report = new ArrayList<>();
    private static int lines_read = 0;
    private static int rec_number = 0;

    public handle_log_file_operations(String path)
    {
        log_file_dir_path = path + File.separator + "Log";
        log_file_path = log_file_dir_path + File.separator + "log.txt";

        log_file_dir = new File(log_file_dir_path);
        log_file = new File(log_file_path);

        if(log_file_dir.exists())
        {
            System.out.println("directory found");
        }
        else
        {
            log_file_dir.mkdirs();
            if(log_file_dir.exists())
            {
                System.out.println("directory created");
            }
        }

        try {
            if (log_file.createNewFile()) {
                System.out.println("file created");
            } else {
                System.out.println("file found");
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public boolean log_event(String name)
    {
        boolean retval = false;

        Long start = System.currentTimeMillis();

        String log_text = name + ";" + Long.toString(start) + ";"+ " \n";
        try{
            FileWriter fw = new FileWriter(log_file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            //System.out.println("name to save " + log_text);
            bw.write(log_text);
            bw.close();
            retval = true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            retval = false;
        }

        return retval;
    }
    public boolean append_event(String event)
    {
        boolean retval = false;

        try{
            FileWriter fw = new FileWriter(log_file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            //System.out.println("name to save " + log_text);
            bw.write(event);
            bw.close();
            retval = true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            retval = false;
        }

        return retval;
    }

    public void read_log_events()
    {
        log.clear();
        lines_read = 0;

        try{
            FileReader fir = new FileReader(log_file);
            BufferedReader br = new BufferedReader(fir);

            String line = "";

            while((line = br.readLine()) != null)
            {
                log.add(line);
                lines_read = lines_read + 1;

            }
            br.close();

        } catch (Exception e)
        {
            lines_read = 0;
        }

        if(lines_read > 0)
        {
            //System.out.println("lines read" + lines_read);
            split_log(lines_read);

        }

    }

    public String fetch_at_idx(int idx)
    {
        return (report.get(idx).toString());
    }

    public boolean delete_log()
    {
        boolean retval = false;
        String empty = "";
        try{
            FileWriter fw = new FileWriter(log_file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            //System.out.println("name to save " + name);
            bw.write(empty);
            bw.close();
            retval = true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            retval = false;
        }

        return retval;
    }

    public boolean delete_selected(int pos)
    {
        boolean success;
        int pos_to_go;
        read_log_events();
        pos_to_go = pos;
        /*for(int i = 0; i < log.size(); i = i + 1)
        {
            System.out.println("recor number " + i + " " + log.get(i).toString());
        }*/

        if((log.size() - 1) > pos)
        {
            //System.out.println("recor at pos " + log.get(pos_to_go).toString());
            String pos_parts[] = log.get(pos_to_go).split(";");
            String pos_1_parts[] = log.get(pos_to_go + 1).split(";");

            if((pos_parts[0].equals("end_string")))
            {
                pos_to_go = pos_to_go + 1;

                if((log.size() - 1) > pos_to_go)
                {
                    String pos_2_parts[] = log.get(pos_to_go + 1).split(";");
                    if(pos_2_parts[0].toString().equals("end_string"))
                    {
                        log.remove(pos_to_go +1 );
                        log.remove(pos_to_go);
                    }
                }
                else
                {
                    log.remove(pos_to_go);
                }
            }
            else if((pos_1_parts[0].equals("end_string")))
            {
               // System.out.println("pos 1 works ");
                log.remove(pos_to_go +1 );
                log.remove(pos_to_go);
            }
        }
        else {
           //System.out.println("item number " + log.get(item_pos).toString());
           log.remove(pos_to_go);
       }
        /*for(int i = 0; i < log.size(); i = i + 1)
        {
            System.out.println("recor number " + i + " " + log.get(i).toString());
        }*/

        success = delete_log();

        for(int i = 0; i < log.size(); i = i + 1)
        {
            append_event(log.get(i).toString() + " \n");
        }
        split_log(log.size());

        /*for(int i = 0; i < log.size(); i = i + 1)
        {
            System.out.println("split record " + i + " " + activity.get(i).toString());
            System.out.println("split record " + i + " " + time.get(i).toString());
        }*/
        lines_read = log.size();
        return success;
    }

    private String getDate(Long miliseconds)
    {
           Date nDate = new Date(miliseconds);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
           //System.out.println(format.format(nDate));
            return(format.format(nDate).toString());
    }

    private String getTime(Long miliseconds)
    {
        Date nDate = new Date(miliseconds);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        //System.out.println(format.format(nDate));
        return(format.format(nDate).toString());
    }

    private String calc_elapsed(int milisec)
    {
        String elapsed = "";
        int minutes = 0;
        int seconds = 0;
        int hours = 0;
        int remainder = 0;
        seconds = milisec / 1000;
        minutes = seconds / 60;
        seconds = seconds % 60;
        hours = minutes / 60;
        minutes = minutes % 60;
       /* System.out.println("minutes = " + minutes);
        System.out.println("remainder = " + remainder);*/
        elapsed = Integer.toString(hours) + " h " + Integer.toString(minutes) + " m " + Integer.toString(seconds) + " s";
        return elapsed;
    }
    private void split_log(int length)
    {
        activity.clear();
        time.clear();

        for(int idx = 0; idx < log.size(); idx = idx + 1)
        {
            String at_idx = log.get(idx).toString();
            String parts[] = at_idx.split(";");
            activity.add(parts[0]);
           // System.out.println("lines read 0" + activity.get(idx).toString());

            time.add(Long.parseLong(parts[1]));
            //System.out.println("lines read" + time.get(idx).toString());

        }
    }

    public int build_report()
    {
        //log.clear();
        report.clear();
        rec_number = 0;
       // System.out.println("lines read = " + lines_read);
        if(lines_read > 0)
        {
            if(lines_read == 1)
            {
                if (activity.get(0).equals("end_string")){
                    delete_log();
                }
                else{
                    append_sin_entree(0);
                }
               // System.out.println("Single entree = " );
            }
            else
            {
                for(rec_number = 0; rec_number < lines_read; rec_number++)
                {
                    /*System.out.println("record number = " + rec_number);
                    System.out.println("record number = " + activity.get(rec_number).toString());
                    System.out.println("record number = " + time.get(rec_number).toString());*/
                    if((lines_read - rec_number) > 1) {
                        append_mul_entree(rec_number);
                        //System.out.println("multiple ");
                    }
                    else
                    {
                        if(activity.get(rec_number).equals("end_string"))
                        {
                            //System.out.println("Single endstring");
                            /* do nothing */
                        }
                        else
                        {
                           // System.out.println("Single activity" + activity.get(rec_number));
                            append_sin_entree(rec_number);
                        }
                    }

                }

            }

        }
        rec_number = 0;
        return (report.size());
    }

    private void append_sin_entree(int idx)
    {
        String date = getDate(time.get(idx));
        String sTime = getTime(time.get(idx));
        String actv = activity.get(idx);
        report.add(actv + ";" + date + "            " + sTime + "                 " + ">> ongoing >>");
       // System.out.println("report line "+ report.get(idx));
    }

    private void append_mul_entree(int idx)
    {
        String actv = activity.get(idx);
        if(actv.equals("end_string"))
        {
            /* do nothing */
            System.out.println("multiple end string ");
        }
        else {
            Long stime = time.get(idx);


            Long etime = time.get(idx + 1);

            Long total = etime - stime;

            String date = getDate(time.get(idx));
            String sTime = getTime(time.get(idx));
            String eTime = getTime(time.get(idx + 1));
            String spend_time = getTime(total);
            //System.out.println("time diff " + " " + sTime + eTime);

            report.add(actv + ";" + date + "            " + sTime + "               " + eTime + "            " + calc_elapsed(total.intValue()));
           //System.out.println("report line "+ report.get(idx));
        }
    }
}
