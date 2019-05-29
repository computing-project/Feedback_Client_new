package com.example.feedback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AllFunctions{

    private static AllFunctions allFunctions;
   //initiate the new object: AllFunctions all = AllFunctions.getObject();

    private CommunicationForClient communication;
    private ArrayList<ProjectInfo> projectList = new ArrayList<ProjectInfo>();
    private Handler handlerAllfunction;
    private String username;//for welcome message. this is the firstName.
    private String myEmail;
    private ArrayList<Mark> markListForMarkPage;


    private AllFunctions(){

        communication = new CommunicationForClient(this);
    }

    public void setHandler(Handler hander)
    { handlerAllfunction = hander;
    }

    public void login(final String username, final String password){


        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.login(username, password);

            }
        }).start();
    }

    public void setUsername(String username)
    {this.username = username;}

    public String getUsername()
    {return this.username;}

    public void setMyEmail(String email)
    {this.myEmail = email;}

    public String getMyEmail()
    {return this.myEmail;}

    public void setMarkListForMarkPage(ArrayList<Mark> markList)
    {this.markListForMarkPage = markList;
    handlerAllfunction.sendEmptyMessage(301);}

    public ArrayList<Mark> getMarkListForMarkPage()
    {return this.markListForMarkPage;}

    public void loginSucc(ArrayList<ProjectInfo> projectList){

        this.projectList = projectList;
        if(projectList.size()>0)
        System.out.println("在Allfunction中收到第一个project的criteriaList和commentList的数量分别为："+projectList.get(0).getCriteria().size()+"  "+projectList.get(0).getCommentList().size());
        sortStudent();
        handlerAllfunction.sendEmptyMessage(101);
    }

    public ArrayList<ProjectInfo> getProjectList(){

        return  projectList;

    }

    public void loginFail(){

        handlerAllfunction.sendEmptyMessage(100);

    }

    public void exceptionWithServer(){
        System.out.println("通讯错误，最后此处要改成弹出并报错！");



    }

    static public AllFunctions getObject(){
        if(allFunctions == null){

            allFunctions = new AllFunctions();

        }
        return allFunctions;
    }

    public void register(final String firstName, final String middleName,
                         final String lastName, final String email,
                         final String password){

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.register(firstName, middleName, lastName,
                        email, password);
                Log.d("register","success");

            }
        }).start();
    }

    public void registerACK(boolean ack){

        //for test
        System.out.println("receive register_ACK in AllFunc: "+ack);

        if(ack)
        {
            handlerAllfunction.sendEmptyMessage(111);
        }else
            {
            handlerAllfunction.sendEmptyMessage(110);
        }
    }


    public void createProject(String projectName, String subjectName,
                              String subjectCode, String description){

        ProjectInfo project = new ProjectInfo();
        projectList.add(project);
        project.setProjectName(projectName);
        project.setSubjectName(subjectName);
        project.setSubjectCode(subjectCode);
        project.setDescription(description);
        project.getAssistant().add(myEmail);

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.updateProject_About(projectName, subjectName,
                        subjectCode, description);
                Log.d("createProject","success");

            }
        }).start();

    }

    public void updateProject(ProjectInfo project, String projectName, String subjectName,
                              String subjectCode, String description){

        project.setProjectName(projectName);
        project.setSubjectName(subjectName);
        project.setSubjectCode(subjectCode);
        project.setDescription(description);

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.updateProject_About(projectName, subjectName,
                        subjectCode, description);
                Log.d("createProject","success");

            }
        }).start();

    }

    public void getMarks(String projectName, String studentID){

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.getMarks(projectName, studentID);

            }
        }).start();

    }



    public void deleteProject(int index)
    {
        String projectName = projectList.get(index).getProjectName();
        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.deleteProject(projectName);
                Log.d("deleteProject","success");

            }
        }).start();
        projectList.remove(index);
    }


    public void projectTimer(ProjectInfo project,int durationMin, int durationSec,
                             int warningMin, int warningSec){

        project.setDurationMin(durationMin);
        project.setDurationSec(durationSec);
        project.setWarningMin(warningMin);
        project.setWarningSec(warningSec);

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.updateProject_Time(project.getProjectName(), durationMin,
                        durationSec, warningMin, warningSec);
                Log.d("projectTimer","success");

            }
        }).start();

    }

    public void inviteAssessor(ProjectInfo project, String assessorEmail)
    {
        String projectName = project.getProjectName();
        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.inviteAssessor(projectName,assessorEmail);

            }
        }).start();
    }

    public void inviteAssessor_Success(String projectName, String assessorEmail)
    {
        for(ProjectInfo projectInfo:projectList)
        {
            if(projectInfo.getProjectName().equals(projectName))
            {
                projectInfo.getAssistant().add(assessorEmail);
                handlerAllfunction.sendEmptyMessage(207);
                break;
            }
        }
    }

    public void inviteAssessor_Fail()
    {
        handlerAllfunction.sendEmptyMessage(208);
    }

    public void deleteAssessor(ProjectInfo project, String assessorEmail)
    {
        String projectName = project.getProjectName();
        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.deleteAssessor(projectName,assessorEmail);

            }
        }).start();
    }


    public void projectCriteria(ProjectInfo project, ArrayList<Criteria> criteriaList,
                                ArrayList<Criteria> commentList){
        System.out.println("在Allfunction中发送的criteriaList和commentList的数量分别为："+criteriaList.size()+"  "+commentList.size());

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.criteriaListSend(project.getProjectName(), criteriaList, commentList);

                Log.d("readExcel","success");

            }
        }).start();


    }

    public void readExcel(ProjectInfo project, String path){

        System.out.println("project name in allfunction for readExcel: "+project.getProjectName());
        ReadExcel read = new ReadExcel();
        read.setInputFile(path);
        project.addStudentList(read.read());
        System.out.println("student list in allFunction: "+read.read().size());

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.importStudents(project.getProjectName(),
                        project.getStudentInfo());

                Log.d("readExcel","success");

            }
        }).start();

    }

    public void addStudent(ProjectInfo project, String number, String firstName,
                           String middleName, String surname, String email){

        StudentInfo studentInfo = new StudentInfo(number, firstName, middleName, surname, email);
        project.addSingleStudent(studentInfo);

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.addStudent(project.getProjectName(),number,
                        firstName, middleName, surname, email);

                Log.d("addStudent","success");

            }
        }).start();
    }

    public int searchStudent(ProjectInfo project, String number)
    {
        ArrayList<StudentInfo> list = project.getStudentInfo();

        //test
        System.out.println("list size in search student: "+list.size());
        for(int i = 0; i < list.size(); i++){
            //test
           // System.out.println("The "+i+" student number: "+list.get(i).getNumber());
            if(number.equals(list.get(i).getNumber())){

                return i;

            }
        }
        return -999;
    }

    public void editStudent(ProjectInfo project, String number, String firstName,
                            String middleName, String surname, String email){

        int i = searchStudent(project, number);
        project.getStudentInfo().get(i).setStudentInfo(number, firstName,
                middleName, surname, email);

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.editStudent(project.getProjectName(), number, firstName,
                        middleName, surname, email);

                Log.d("editStudent","success");

            }
        }).start();

    }

    public void deleteStudent(ProjectInfo project, String number){


        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.deleteStudent(project.getProjectName(), number);

                Log.d("deleteStudent","success");

            }
        }).start();
    }

    public void groupStudent(ProjectInfo project, String studentID, int groupNumber){


        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.groupStudent(project.getProjectName(),
                        studentID, groupNumber);

                Log.d("groupStudent","success");

            }
        }).start();

    }

    public int getMaxGroupNumber(int indexOfProject)
    {
        int max = 0;
        for(StudentInfo student : projectList.get(indexOfProject).getStudentInfo())
        {
            if(student.getGroup() > max)
                max = student.getGroup();
        }
        return max;
    }

    public void sendMark(ProjectInfo project, String studentID, Mark mark){

        new Thread(new Runnable(){
            @Override
            public void run(){

                communication.sendMark(project.getProjectName(),
                      studentID, mark);

                Log.d("sendMark","success");

            }
        }).start();

    }


    public void sortStudent(){

        for(int i = 0; i < projectList.size(); i++){

            Collections.sort(projectList.get(i).getStudentInfo(), new SortByGroup());

        }



    }

    public class SortByGroup implements Comparator{

        public int compare(Object o1, Object o2) {
            StudentInfo s1 = (StudentInfo) o1;
            StudentInfo s2 = (StudentInfo) o2;
            if (s1.getGroup() > s2.getGroup() && s2.getGroup() == -999) {

                return -1;

            }else if(s1.getGroup() < s2.getGroup() && s1.getGroup() == -999){
                return 1;
            }else if(s1.getGroup() > s2.getGroup()){
                return 1;
            }else if(s1.getGroup() == s2.getGroup()){
                return 1;
            }else return -1;
        }

    }


    public void testSortGroup()
    {


        ArrayList<StudentInfo> studentListForTest = new ArrayList<>();
        StudentInfo student1 = new StudentInfo();
        student1.setGroup(-999);
        studentListForTest.add(student1);
        StudentInfo student2 = new StudentInfo();
        student2.setGroup(2);
        studentListForTest.add(student2);
        StudentInfo student3 = new StudentInfo();
        student3.setGroup(-999);
        studentListForTest.add(student3);
        StudentInfo student4 = new StudentInfo();
        student4.setGroup(-999);
        studentListForTest.add(student4);
        StudentInfo student5 = new StudentInfo();
        student5.setGroup(1);
        studentListForTest.add(student5);
        StudentInfo student6 = new StudentInfo();
        student6.setGroup(2);
        studentListForTest.add(student6);
        StudentInfo student7 = new StudentInfo();
        student7.setGroup(77);
        studentListForTest.add(student7);

        //call sort
//        ArrayList<StudentInfo> studentTemp = new ArrayList<StudentInfo>();

//        for(StudentInfo ss: studentListForTest){
//            if(ss.getGroup() == -999){
//
//                studentTemp.add(ss);
//                studentListForTest.remove(ss);
//            }
//        }


        Collections.sort(studentListForTest, new SortByGroup());
//        studentListForTest.addAll(studentTemp);

        System.out.println("接下来是排序时间：");
        for(StudentInfo s: studentListForTest)
            System.out.println(s.getGroup());
        System.out.println("排序结束");
    }

}
