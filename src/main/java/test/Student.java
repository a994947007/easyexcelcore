package test;


import org.easyexcel.annotation.Entity;
import org.easyexcel.annotation.FieldName;

@Entity(sheet = 0,path="D:/Student.xls")
public class Student {
    @FieldName("s_id")
    private Integer id;

    @FieldName("s_age")
    private Integer age;

    @FieldName("s_name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
