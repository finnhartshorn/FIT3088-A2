package utility.PLY;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Element {
    private String name;
    private HashMap<String, Property> propertyMap;
    private ArrayList<Property> propertyList;
    private int length;

    public Element(String name, int length) {
        this.name = name;
        this.length = length;
        propertyList = new ArrayList<>();
        propertyMap = new HashMap<>();
    }

    public boolean validate() {
        for(Property property : propertyList) {
            if (property.getData().size() != length) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public void addProperty(String name, Property property) {
        propertyMap.put(name, property);
        propertyList.add(property);
    }

    public HashMap<String, Property> getPropertyMap() {
        return propertyMap;
    }

    public ArrayList<Property> getPropertyList() {
        return propertyList;
    }

    public void addData(String data) throws TypeException{
        if(propertyList.size() == 1) {
            propertyList.get(0).addData(data);
        } else {
            String[] datemList = data.split(" ");
            assert datemList.length == propertyList.size();
            for (int i=0; i < datemList.length; i++) {
                propertyList.get(i).addData(datemList[i]);
            }
        }
    }


}
