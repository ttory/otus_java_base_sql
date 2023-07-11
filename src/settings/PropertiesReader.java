/*
 * Copyright 2023.
 * Student's project.
 * Author: Polyanskaya Victory.
 */

package settings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader implements ISettings{

    @Override
    public Map<String, String> read(){
        String rootFolder = System.getProperty("user.dir");

        Map<String, String> props = new HashMap<>();

        try(InputStream input = Files.newInputStream(Paths.get(String.format("%s/src/resources/%s", rootFolder,
                "db.properties")))) {
            Properties properties = new Properties();
            properties.load(input);

            for(Map.Entry<Object, Object> entry: properties.entrySet()){
                props.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        return props;
    }
}
