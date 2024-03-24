package org.example.entity.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.function.Consumer;

public interface BaseData {
    default <T> T asViewObject(Class<T> clazz, Consumer<T> consumer) {
        T t = this.asViewObject(clazz);
        consumer.accept(t);
        return t;
    }
    default <T> T asViewObject(Class<T> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            Field[] fields1 = this.getClass().getDeclaredFields();
            HashSet<String> hashSet = new HashSet<>();
            for (Field field : fields1) { hashSet.add(field.getName());}        //这是因为account中有token和expire属性， 二者不存在于AuthorizeVO中，所以要过滤
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            T t = constructor.newInstance();
            for (Field field : fields) {
                if (hashSet.contains(field.getName())) convert(field, t);
            }
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    default void convert(Field field, Object t){
        try {
            Field source = this.getClass().getDeclaredField(field.getName());
            field.setAccessible(true);
            source.setAccessible(true);
            field.set(t, source.get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
