package utility.PLY;

import java.util.ArrayList;

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

    public <S> S convert(String string, Class<S> type) throws TypeException {
        if(type.equals(Integer.class)) {
            return (S) Integer.valueOf(string);
        } else if (type.equals(Float.class)) {
            return (S) Float.valueOf(string);
        } else if (type.equals(Double.class)) {
            return (S) Double.valueOf(string);
        } else if (type.equals(Short.class)) {
            return (S) Short.valueOf(string);
        } else if (type.equals(Integer[].class)){
            throw new TypeException("K");
        } else {
            throw new TypeException("Unknown Type" + type);
        }
    }

    public ArrayList<T> getData() {
        return data;
    }
}
