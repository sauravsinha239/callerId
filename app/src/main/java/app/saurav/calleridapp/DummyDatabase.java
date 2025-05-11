package app.saurav.calleridapp;

import java.util.HashMap;

public class DummyDatabase {
    public static HashMap<String, String> getDummyData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("1234567890", "John Doe");
        map.put("9876543210", "Jane Smith");
        map.put("8340181676", "Priyanshu"); // ✅ Fixed
        return map;
    }
}
