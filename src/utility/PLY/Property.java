package utility.PLY;

import java.util.ArrayList;

// A generic class for storing and converting property data within an element
public class Property<T> {
    private Class<T> type;
    private ArrayList<T> data;

    public Property(Class<T> type) {
        this.type = type;
        data = new ArrayList<>();
    }

    public void addData(T newData) {
        data.add(newData);
    }

    public void addData(String string) throws TypeException {
        addData(convert(string));
    }

    public T convert(String string) throws TypeException {
        return convert(string, this.type);
    }

    // Converts a string containing this classes data to the correct type, used when adding data
    public <S> S convert(String string, Class<S> type) throws TypeException {
        if(type.equals(Integer.class)) {
            return (S) Integer.valueOf(string);
        } else if (type.equals(Float.class)) {
            return (S) Float.valueOf(string);
        } else if (type.equals(Double.class)) {
            return (S) Double.valueOf(string);
        } else if (type.equals(Short.class)) {
            return (S) Short.valueOf(string);
        } else {
            throw new TypeException("Unknown Type" + type);
        }
    }

    public ArrayList<T> getData() {
        return data;
    }
}
