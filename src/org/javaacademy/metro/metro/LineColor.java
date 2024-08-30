package org.javaacademy.metro.metro;

public enum LineColor {
    RED("Красная"),
    BLUE("Синяя");

    private final String name;

    LineColor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
