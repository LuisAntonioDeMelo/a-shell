package javaShell;

import java.util.Arrays;
import java.util.List;

public enum Type {
    CD("cd", ""),
    ECHO("echo", ""),
    PWD("pwd",""),
    GREP("grep", ""),
    TYPE("type", ""),;


    private String type;
    private String description;

    Type(String type, String description) {
        this.type = type;
        this.description = description;
    }

    private Type(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    // echo -n "banana blueberry." > "/tmp/bee/f   67"
    public List<String> getTypes() {
        return Arrays.stream(Type.values()).map(Type::getType).toList();
    }
}
