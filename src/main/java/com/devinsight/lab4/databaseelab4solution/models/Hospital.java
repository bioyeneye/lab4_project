package com.devinsight.lab4.databaseelab4solution.models;

import lombok.Data;

@Data
public class Hospital {
    private int id;
    private HospitalType hospitalType;
    private String name;
    private String description;
    private String place;
}
