package com.devinsight.lab4.databaseelab4solution.models;

import lombok.Data;

@Data
public class Doctor {
    private int id;
    private Hospital hospital;
    private String name;
    private String address;
    private String contactNo;
    private String email;
    private int hospitalId;
}

