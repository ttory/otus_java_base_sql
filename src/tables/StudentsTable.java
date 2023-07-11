/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

package tables;

import db.DBConnector;

import java.util.HashMap;
import java.util.Map;

public class StudentsTable extends AbsTable {
    private final static String TABLE_NAME = "Student";
    private final static String PRIMARY_KEY = "id";
    public StudentsTable() {
        super(TABLE_NAME, new HashMap<String, String>() {{
            put("id", "int");
            put("fio", "varchar(255)");
            put("sex", "varchar(10)");
            put("id_group", "int");
        }}, PRIMARY_KEY);

    }

    public boolean fill(Integer id, String fio, String sex, Integer id_group) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fio", fio);
        map.put("sex", sex);
        map.put("id_group", id_group);
        return super.fill(map);
    }

    public void selectStudents() {
        DBConnector.getInstance().printQuery("select s.id,s.fio,s.sex, g.name as `Group name`, " +
                "c.fio as `Curator name`" +
                " from Student as s left join `Group` as g on s.id_group = " +
                "g.id left join Curator as c on g.id_curator = c.id;");
    }

    public void printCountStudents() {
        DBConnector.getInstance().printQuery("select count(*) as `Students count` from Student;");
    }

    public void printFemaleStudents() {
        DBConnector.getInstance().printQuery("select s.fio,s.sex from Student as s where s.sex=\"female\";");
    }
}
