package com.example.feedback;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteExcel {

    private ArrayList<StudentInfo> studentList = new ArrayList<StudentInfo>();

    public void write(ArrayList<StudentInfo> studentList) {

        this.studentList = studentList;

        try {

            File file = new File(Environment.getExternalStorageDirectory().getPath()
                    + "/StudentMarks" + ".csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write("number" + "," + "firstName" + "," + "middleName" + ","
                    + "surname" + "," + "email" + "," + "mark");
            bw.newLine();

            for (int i = 0; i < studentList.size(); i++) {
                bw.write(studentList.get(i).getNumber() + ","
                        + studentList.get(i).getFirstName() + ","
                        + studentList.get(i).getMiddleName() + ","
                        + studentList.get(i).getSurname() + ","
                        + studentList.get(i).getEmail() + ","
                        + studentList.get(i).getMark());
                bw.newLine();
            }

            bw.close();

            Log.d("WriteExcel", "successful");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}