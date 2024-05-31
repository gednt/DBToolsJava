package Models;

public class GenericObject {
    private String column;
    private Object value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    public void setValue(Object value){
        this.value = value;
    }
    public Object getValue(){
        return this.value;
    }
}
