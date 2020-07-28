package net.eduard.api.lib.storage.storation;



public interface Storation<DataType> {


    boolean acceptClass(Class<?> clz);

    Object store(DataType data);

    DataType restore(Class<?> clz ,Object data);

}
