/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

package tables;

import java.util.HashMap;
import java.util.Map;

public class CuratorsTable extends AbsTable {
    private final static String TABLE_NAME = "Curator";
    private final static String PRIMARY_KEY = "id";

    public CuratorsTable() {
        super(TABLE_NAME, new HashMap<String, String>() {{
            put("id", "int");
            put("fio", "varchar(255)");
        }}, PRIMARY_KEY);
    }

    public boolean fill(Integer id, String fio) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fio", fio);
        return super.fill(map);
    }

}
