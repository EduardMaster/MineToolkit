package net.eduard.api.lib.old;

import net.eduard.api.lib.modules.Extra;

/**
 * API de conversão de tipos primitivos<br>
 * Versão nova {@link ObjectConverter} 2.0
 * @version 1.0
 * @since 0.7
 * @deprecated Adicionado na classe {@link Extra}
 * @author Eduard
 *
 */
public abstract interface EditSetup
{
  public  default int toInt(Object obj)
  {
    if (obj == null) return 0;
    if ((obj instanceof Integer)) {
      return ((Integer)obj).intValue();
    }
    if ((obj instanceof Number)) {
      Number number = (Number)obj;
      return number.intValue();
    }
    try {
      return Integer.valueOf(obj.toString()).intValue();
    } catch (Exception e) {}
    return 0;
  }
  
  public  default double toDouble(Object obj)
  {
    if (obj == null) return 0.0D;
    if ((obj instanceof Double)) {
      return ((Double)obj).doubleValue();
    }
    if ((obj instanceof Number)) {
      Number number = (Number)obj;
      return number.doubleValue();
    }
    try {
      return Double.valueOf(obj.toString()).doubleValue();
    } catch (Exception e) {}
    return 0.0D;
  }
  
  public  default float toFloat(Object obj)
  {
    if (obj == null) return 0.0F;
    if ((obj instanceof Float)) {
      return ((Float)obj).floatValue();
    }
    
    if ((obj instanceof Number)) {
      Number number = (Number)obj;
      return number.floatValue();
    }
    try {
      return Float.valueOf(obj.toString()).floatValue();
    } catch (Exception e) {}
    return 0.0F;
  }
  
  public  default short toShort(Object obj)
  {
    if (obj == null) return 0;
    if ((obj instanceof Short)) {
      return ((Short)obj).shortValue();
    }
    
    if ((obj instanceof Number)) {
      Number number = (Number)obj;
      return number.shortValue();
    }
    try {
      return Short.valueOf(obj.toString()).shortValue();
    } catch (Exception e) {}
    return 0;
  }
  
  public  default byte toByte(Object obj)
  {
    if (obj == null) return 0;
    if ((obj instanceof Byte)) {
      return ((Byte)obj).byteValue();
    }
    
    if ((obj instanceof Number)) {
      Number number = (Number)obj;
      return number.byteValue();
    }
    try {
      return Byte.valueOf(obj.toString()).byteValue();
    } catch (Exception e) {}
    return 0;
  }
  
  public  default long toLong(Object obj)
  {
    if (obj == null) return 0L;
    if ((obj instanceof Byte)) {
      return ((Long)obj).longValue();
    }
    
    if ((obj instanceof Number)) {
      Number number = (Number)obj;
      return number.longValue();
    }
    try {
      return Long.valueOf(obj.toString()).longValue();
    } catch (Exception e) {}
    return 0L;
  }
  
  public  default String toString(Object obj) {
    if (obj == null) return "";
    if ((obj instanceof String)) {
      return (String)obj;
    }
    return obj.toString();
  }
}
