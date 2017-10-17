package utility.PLY;

import java.util.ArrayList;
import java.util.List;

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
//        ArrayList<S> innerList = new ArrayList<>();
        for (int i = 0; i < dataString.length-1; i++) {
            innerList[i] = convert(dataString[i+1], innerType);
        }
//        for(String datem : dataString) {
//            innerList.add(convert(datem, innerType));
//        }

        return (T) innerList;
    }

    //    @Override
//    public <E> E convert(String string) throws Exception {
//        return super.convert(string, innerType);
//    }

    @Override
    public void addData(String string) throws TypeException {
        addData(convert(string));
    }

    public List<T[]> getListData() {
        return (List<T[]>) getData();
    }
}
