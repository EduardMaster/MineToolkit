package net.eduard.api.minecraft.reflection;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang.Validate;

public final class ArrayWrapper<E>
{
  private E[] _array;

  public ArrayWrapper(E[] elements)
  {
    setArray(elements);
  }

  public E[] getArray()
  {
    return this._array;
  }

  public void setArray(E[] array)
  {
    Validate.notNull(array, "The array must not be null.");
    this._array = array;
  }

  @Override
public boolean equals(Object other)
  {
    if (!(other instanceof ArrayWrapper)) {
      return false;
    }
    return Arrays.equals(this._array, ((ArrayWrapper<?>)other)._array);
  }

  @Override
public int hashCode()
  {
    return Arrays.hashCode(this._array);
  }

  @SuppressWarnings({"unchecked", "unused"})
public static <T> T[] toArray(Iterable<? extends T> list, Class<T> c)
  {
    int size = -1;
    if ((list instanceof Collection))
    {
      Collection<? extends T> coll = (Collection<? extends T>)list;
      size = coll.size();
    }

    if (size < 0) {
      size = 0;

      for (Object element : list) {
        size++;
      }
    }

    Object[] result = (Object[])Array.newInstance(c, size);
    int i = 0;
    for (Object element : list) {
      result[(i++)] = element;
    }
    return (T[]) result;
  }
}