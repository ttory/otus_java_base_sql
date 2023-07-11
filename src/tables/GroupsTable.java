/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

package tables;

import db.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GroupsTable extends AbsTable {
    private final static String TABLE_NAME = "Group";
    private final static String PRIMARY_KEY = "id";

    public GroupsTable() {
        super(TABLE_NAME, new HashMap<String, String>() {{
            put("id", "int");
            put("name", "varchar(10)");
            put("id_curator", "int");
        }}, PRIMARY_KEY);
    }

    public boolean fill(Integer id, String name, Integer id_curator) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("id_curator", id_curator);
        return super.fill(map);
    }

    public void updateCurator(int groupId, int curatorId) {
        Statement all = DBConnector.getInstance().getStatementUpdatable();

        try {
            ResultSet set = all.executeQuery(String.format("select * from `Group` where id = %d;", groupId));
            set.beforeFirst();
            if (set.next()) {
                set.updateInt("id_curator", curatorId);
                set.updateRow();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                all.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void selectGroups() {
        DBConnector.getInstance().printQuery("select g.id as Id, g.name as `Group name`, " +
                "c.fio as `Curator name`" +
                "from `Group` as g left join Curator as c on g.id_curator = c.id;");
    }

    public void selectGroupByName(String name) {
        DBConnector.getInstance().printQuery(String.format("select s.id_group as `Group id`,s.id as `Student id`, " +
                "s.fio, s.sex from Student as s where s.id_group in (select id from `Group` as g where g.name = " +
                "\"%s\" );", name));
    }
}
