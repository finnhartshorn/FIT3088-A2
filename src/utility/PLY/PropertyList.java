package utility.PLY;

// A subclass of property to support list types
public class PropertyList<T, S> extends Property<T> {
    private Class<S> innerType;


    public PropertyList(Class<T> type, Class<S> innerType) {
        super(type);
        this.innerType = innerType;
    }

    @Override
    public T convert(String string) throws TypeException {
        String[] dataString = string.split(" ");
        S[] innerList = (S[]) new Object[dataString.length-1];
        for (int i = 0; i < dataString.length-1; i++) {
            innerList[i] = convert(dataString[i+1], innerType);
        }

        return (T) innerList;
    }

    @Override
    public void addData(String string) throws TypeException {
        addData(convert(string));
    }
}
