package com.progressive.minds;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<String> strings = Collections.singletonList("x,Y");
        strings.forEach(System.out::println);
    }
}