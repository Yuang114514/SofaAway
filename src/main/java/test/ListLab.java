package test;

import java.util.ArrayList;
import java.util.List;

public class ListLab {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(List.of(
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10"
        ));
        
        System.out.println(list.get(3));
        
        list.remove(3);
        
        System.out.println(list.get(3));
    }
}
