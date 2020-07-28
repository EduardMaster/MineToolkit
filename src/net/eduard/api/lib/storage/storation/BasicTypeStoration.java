package net.eduard.api.lib.storage.storation;

import net.eduard.api.lib.modules.Extra;

public final class BasicTypeStoration implements  Storation{

    BasicTypeStoration(){
        StorationAPI.getInstance().register(Integer.class,this);
    }
    @Override
    public boolean acceptClass(Class clz) {

        return clz.isPrimitive() || Extra.isWrapper(clz) || Extra.isString(clz);
    }

    @Override
    public Object store(Object data) {
        return data;
    }

    @Override
    public Object restore(Class clz, Object data) {
        if (clz == String.class)return data;
        try {
            return clz.getMethod("valueOf", String.class).invoke(clz, data.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


}
