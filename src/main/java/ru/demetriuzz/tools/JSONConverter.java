package ru.demetriuzz.tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import ru.demetriuzz.tools.annotation.JSONIgnore;
import ru.demetriuzz.tools.annotation.JSONReplaceUID;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * Преобразователь Классов в формат JSON и получение классов из JSON<br />
 * Для полей Date и Timestamp часовой пояс UTC!<br />
 * Минимальный формат одиночного JSON:<br />
 * <pre>
 * {
 *    "имя_класса":{
 *      "ключ_1":значение_1
 *     ,"ключ_2":"значение_2"
 *     ,"ключ_3":null
 *    }
 * }</pre>
 * Минимальный формат массива объектов JSON:<br />
 * <pre>
 * {
 *     "имя_класса":[
 *       {
 *         "ключ_1":значение_1
 *        ,"ключ_2":"значение_2"
 *        ,"ключ_3":null
 *       },
 *       {..},
 *       {..}
 *     ]
 * }</pre>
 */
public class JSONConverter {

    // to JSON

    /**
     * Преобразование объекта в строку формата JSON #1
     */
    public static <T1> String toJSON(T1 clazz) throws IllegalAccessException {
        return toJSON(clazz, false);
    }

    /**
     * Преобразование объекта в строку формата JSON #2
     */
    public static <T1> String toJSON(T1 clazz, boolean pretty) throws IllegalAccessException {
        if (clazz == null) throw new RuntimeException("Class Is Null");
        if (clazz.getClass() == Class.class) throw new RuntimeException("Need Class Instance!");

        StringWriter stringWriter = new StringWriter();
        JSONWriter writer = new JSONWriter(stringWriter);
        writer.object(); // ..{
        readValue(writer, clazz);
        writer.endObject(); // ..}

        String result = stringWriter.toString();
        if (pretty) return new JSONObject(result).toString(2);

        return result;
    }

    /**
     * Преобразование списка объектов в строку формата JSON #3
     */
    public static <T1> String toJSONArray(List<T1> clazz) throws IllegalAccessException {
        return toJSONArray(clazz, false);
    }

    /**
     * Преобразование списка объектов в строку формата JSON #4
     */
    public static <T1> String toJSONArray(List<T1> clazz, boolean pretty) throws IllegalAccessException {
        if (clazz == null) throw new RuntimeException("Class List Is Null");

        if (clazz.isEmpty()) return "[]";

        for (T1 t : clazz)
            if (t.getClass() == Class.class)
                throw new RuntimeException("Need Class Instance In List!");

        StringWriter stringWriter = new StringWriter();
        JSONWriter writer = new JSONWriter(stringWriter);

        writer.array(); // [
        for (T1 t : clazz) {
            writer.object(); // ..{
            readValue(writer, t);
            writer.endObject(); // ..}
        }
        writer.endArray(); // ]

        String result = stringWriter.toString();
        if (pretty) return new JSONObject(result).toString(2);

        return result;
    }

    // from JSON

    /**
     * Создание указанного класса согласно строки формата JSON #1
     */
    public static <T2> T2 fromJSON(Class<T2> clazz, String json) throws IllegalAccessException {
        if (clazz == null) throw new RuntimeException("Class Is Null");
        if (json == null) throw new RuntimeException("JSON Is Null");
        if (json.trim().isEmpty()) throw new RuntimeException("JSON Is Empty");

        return writeValue(clazz, new JSONObject(json));
    }

    /**
     * Создание списка с указанным классом согласно строки формата JSON #2
     */
    public static <T2> List<T2> fromJSONArray(Class<T2> clazz, String json) throws IllegalAccessException {
        if (clazz == null) throw new RuntimeException("Class List Is Null");
        if (json == null) throw new RuntimeException("JSON Is Null");
        if (json.trim().isEmpty()) throw new RuntimeException("JSON Is Empty");

        // новый список
        List<T2> t2List = new ArrayList<>(1);
        JSONArray array;

        try {
            array = new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException("Wrong JSON Array");
        }

        // массив пустой, список так же пустой
        if (array.isEmpty()) return t2List;

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.isEmpty()) continue;

            t2List.add(writeValue(clazz, object));
        }

        return t2List;
    }

    // private

    /**
     * Чтение полей объекта
     */
    private static <T1> void readValue(JSONWriter writer, T1 clazz) throws IllegalAccessException {
        final Field[] fields = clazz.getClass().getDeclaredFields();
        if (fields.length == 0) return;

        for (Field f : fields) {
            if (f.isAnnotationPresent(JSONIgnore.class)) continue;

            // имя элемента
            writer.key(f.getName());
            f.setAccessible(true);
            // форматирование даты и времени
            if (f.getType() == Timestamp.class)
                writer.value(TimeFormatter.formatTimestamp((Timestamp) f.get(clazz)));
            else if (f.getType() == Date.class)
                writer.value(TimeFormatter.formatDate((Date) f.get(clazz)));
                // списки значений
            else if (f.getType() == List.class) {
                List<?> list = (List<?>) f.get(clazz);
                if (list == null || list.isEmpty()) {
                    writer.array().endArray(); // []
                    continue;
                }

                writer.array(); // [

                // чем параметризирован List?
                if (list.get(0).getClass() == Long.class
                        || list.get(0).getClass() == Integer.class
                        || list.get(0).getClass() == String.class)
                    // базовые типы
                    for (Object one : list) writer.value(one);
                else
                    // объекты
                    for (Object one : list) {
                        writer.object(); // ..{
                        readValue(writer, one);
                        writer.endObject(); // ..}
                    }

                writer.endArray(); // ]
            } else writer.value(f.get(clazz));
        }
    }

    /**
     * Запись в поля объекта
     */
    private static <T2> T2 writeValue(Class<T2> clazz, JSONObject cObject) throws IllegalAccessException {

        // экземпляр класса
        T2 t2 = createInstance(clazz);

        // наполнение полей класса значениями из объекта JSON по имени поля
        final Field[] fields = t2.getClass().getDeclaredFields();
        if (fields.length == 0) return t2;

        for (Field f : fields) {
            if (f.isAnnotationPresent(JSONIgnore.class))
                continue;

            f.setAccessible(true);

            // задаем свой UID для строковых полей, всегда!
            if (f.isAnnotationPresent(JSONReplaceUID.class) && f.getType() == String.class) {
                f.set(t2, UUID.randomUUID().toString());
                continue;
            }

            String name = f.getName();
            // прямое написание имени поля
            if (cObject.has(name)) {
                writeField(f, t2, cObject, name);
                continue;
            }
            // как малые буквы
            if (cObject.has(name.toLowerCase())) {
                writeField(f, t2, cObject, name.toLowerCase());
                continue;
            }
            // как большие буквы
            if (cObject.has(name.toUpperCase())) {
                writeField(f, t2, cObject, name.toUpperCase());
            }
        }

        return t2;
    }

    private static <T2> void writeField(Field f,
                                        T2 clazz,
                                        JSONObject cObject,
                                        String name) throws IllegalAccessException {
        // значения NULL остаются
        if (cObject.isNull(name)) return;

        // тип объекта поля, который заполняется
        if (f.getType() == Long.class) {
            f.set(clazz, cObject.getLong(name));
            return;
        }
        if (f.getType() == Integer.class) {
            f.set(clazz, cObject.getInt(name));
            return;
        }

        if (f.getType() == Timestamp.class) {
            f.set(clazz, new Timestamp(TimeFormatter.parseTimestamp(cObject.getString(name))));
            return;
        }
        if (f.getType() == Date.class) {
            f.set(clazz, new Date(TimeFormatter.parseDate(cObject.getString(name))));
            return;
        }

        if (f.getType() == List.class) {
            writeFieldList(f, clazz, cObject.getJSONArray(name));
            return;
        }

        f.set(clazz, cObject.get(name));
    }

    private static <T2> void writeFieldList(Field f,
                                            T2 clazz,
                                            JSONArray array) throws IllegalAccessException {
        // тип внутри List неизвестен, создание пустого списка
        f.set(clazz, new ArrayList<>());
        if (array.isEmpty()) return;

        // тип внутри List, определение
        List<?> list = array.toList();

        // базовый тип по первому элементу
        if (list.get(0).getClass() == Long.class
                || list.get(0).getClass() == Integer.class
                || list.get(0).getClass() == String.class) {
            f.set(clazz, list);
            return;
        }

        List<?> temporary = (List<?>) f.get(clazz);
        // свои типы
        // чем параметризирован List?
        for (Object one : list) {
            // один элемент списка - карта одного объекта
            HashMap<?, ?> map = (HashMap<?, ?>) one;
            // ключи - имена элементов, значения - значения элементов
            Set<?> keys = map.keySet();
            // todo
        }

    }

    /**
     * Создание экземпляра класса
     */
    @SuppressWarnings("unchecked")
    private static <TEMP> TEMP createInstance(Class<TEMP> clazz) {
        try {
            // доступные конструкторы класса - минимум один
            // создание экземпляра класса - вариант пустой конструктор без аргументов
            return (TEMP) clazz.getDeclaredConstructors()[0].newInstance();
        } catch (Exception e) {
            throw new RuntimeException("createInstance error for Class " + clazz.getName(), e);
        }
    }

}