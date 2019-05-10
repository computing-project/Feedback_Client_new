package com.example.feedback;

public class StudentInfo {

    private String number;
    private String firstName;
    private String middleName;
    private String surname;
    private String email;
    private double totalMark;
    private Mark mark;
    private int group = -999;

    public double getTotalMark() {

        return totalMark;

    }

    public void setTotalMark(double totalMark) {

        this.totalMark = totalMark;

    }

    public Mark getMark() {

        return mark;

    }

    public void setMark(Mark mark) {

        this.mark = mark;

    }

    public StudentInfo(){}
    public StudentInfo(String number, String firstName, String middleName,
                           String surname, String email){

        this.number = number;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.email = email;

    }

    public void setStudentInfo(String number, String firstName, String middleName,
                            String surname, String email){

        this.number = number;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.email = email;

    }

    public void setFirstName(String firstName){

        this.firstName = firstName;

    }

    public void setMiddleName(String middleName){

        this.middleName = middleName;

    }

    public void setEmail(String email){

        this.email = email;

    }

    public void setSurname(String surname){

        this.surname = surname;

    }

    public void setGroup(int group){

        this.group = group;

    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getNumber(){

        return number;

    }

    public String getFirstName(){

        return firstName;

    }

    public String getMiddleName(){

        return middleName;

    }

    public String getSurname(){

        return surname;

    }

    public String getEmail(){

        return email;

    }

    public int getGroup(){

        return group;

    }
}