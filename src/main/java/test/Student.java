package test;


import org.easyexcel.annotation.Entity;
import org.easyexcel.annotation.FieldName;

@Entity
public class Student {
    @FieldName("s_name")
    private String name;

    @FieldName("s_age")
    private int age;

    @FieldName("s_score")
    private double score;
}
