package ru.demetriuzz.tools.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import ru.demetriuzz.tools.JSONConverter;
import ru.demetriuzz.tools.TimeFormatter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JSONConverterTest {

    @Test
    void toJSONExceptionTest() {
        try {
            JSONConverter.toJSON(null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.toJSON(TestJSONSingle.class);
            Assertions.fail();
        } catch (Exception ignore) {}

        try {
            JSONConverter.toJSONArray(null);
            Assertions.fail();
        } catch (Exception ignore) {}
    }

    @Test
    void fromJSONExceptionTest() {
        try {
            JSONConverter.fromJSON(null, null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSON(TestJSONSingle.class, null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSON(TestJSONSingle.class, "");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(null, null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(TestJSONSingle.class, null);
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(TestJSONSingle.class, "");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(TestJSONSingle.class, "{}");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(TestJSONSingle.class, "{\"One\":[],\"Two\":[]}");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(TestJSONSingle.class, "{\"Other\":[]}");
            Assertions.fail();
        } catch (Exception ignore) {}
        try {
            JSONConverter.fromJSONArray(TestJSONSingle.class, "{\"BlaBla\":null}");
            Assertions.fail();
        } catch (Exception ignore) {}
    }

    @Test
    void toJSONTest() throws Exception {
        TestJSONSingle obj = new TestJSONSingle()
                .setIgnore(1) // поле помеченно как ignore
                .setUid("uid1") // в JSON без изменений
                .setText1("text 123")
                .setTime1(new Timestamp(1_000_000L))
                .setDate1(new Date(1_000_000L))
                .setBool1(false)
                .setIntNumber1(100)
                .setLongNumber1(2_000_000L);

        String json = JSONConverter.toJSON(obj);
        JSONAssert.assertEquals(
                "{" + // поля ignore не будет
                        "\"uid\":\"uid1\"" +
                        ",\"text1\":\"text 123\"" +
                        ",\"text2\":null" +
                        ",\"time1\":\"1970-01-01T00:16:40\"" +
                        ",\"time2\":null" +
                        ",\"date1\":\"1970-01-01\"" +
                        ",\"date2\":null" +
                        ",\"bool1\":false" +
                        ",\"bool2\":null" +
                        ",\"intNumber1\":100" +
                        ",\"intNumber2\":null" +
                        ",\"longNumber1\":2000000" +
                        ",\"longNumber2\":null" +
                        "}", json, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void fromJSONTest() throws Exception {
        String json = "{" +
                "\"intNumber\":123" +
                ",\"text1\":\"qwe\"" +
                ",\"xyz\":321" + // поля нет
                ",\"LONGNUMBER1\":30000000" + // приоритет будет дан имени longNumber1
                ",\"longNumber1\":20000000" +
                ",\"time1\":\"2020-01-01T00:10:20\"" +
                ",\"date1\":null" +
                ",\"time2\":null" +
                ",\"date2\":\"2020-01-01\"" +
                ",\"bool2\":true" +
                ",\"ignore\":112233" + // поле помеченно как ignore
                ",\"INTNUMBER2\":333" +
                ",\"uid\":\"uid111\"" + // поле будет заменено значеним генератора UID
                "}";

        TestJSONSingle obj = JSONConverter.fromJSON(TestJSONSingle.class, json);
        Assertions.assertNotEquals("uid111", obj.getUid());
        Assertions.assertNull(obj.getIgnore());
        Assertions.assertNull(obj.getText2());
        Assertions.assertNull(obj.getTime2());
        Assertions.assertNull(obj.getDate1());
        Assertions.assertNull(obj.getBool1());
        Assertions.assertNull(obj.getIntNumber1());
        Assertions.assertNull(obj.getLongNumber2());
        Assertions.assertEquals("qwe", obj.getText1());
        Assertions.assertEquals("2020-01-01T00:10:20", TimeFormatter.formatTimestamp(obj.getTime1()));
        Assertions.assertEquals("2020-01-01", TimeFormatter.formatDate(obj.getDate2()));
        Assertions.assertTrue(obj.getBool2());
        Assertions.assertEquals(333, obj.getIntNumber2());
        Assertions.assertEquals(20000000L, obj.getLongNumber1());

        // пустой JSON
        TestJSONSingle empty = JSONConverter.fromJSON(TestJSONSingle.class, "{}");
        Assertions.assertNotNull(empty.getUid());
        Assertions.assertNull(empty.getIgnore());
        Assertions.assertNull(empty.getText1());
        Assertions.assertNull(empty.getText2());
        Assertions.assertNull(empty.getTime1());
        Assertions.assertNull(empty.getTime2());
        Assertions.assertNull(empty.getDate1());
        Assertions.assertNull(empty.getDate2());
        Assertions.assertNull(empty.getBool1());
        Assertions.assertNull(empty.getBool2());
        Assertions.assertNull(empty.getIntNumber1());
        Assertions.assertNull(empty.getIntNumber2());
        Assertions.assertNull(empty.getLongNumber1());
        Assertions.assertNull(empty.getLongNumber2());
    }

    @Test
    void toJSONArrayTest() throws Exception {
        List<TestJSONSingle> list = new ArrayList<>();
        Assertions.assertEquals("[]", JSONConverter.toJSONArray(list));

        // поле ignore нет в JSON
        list.add(new TestJSONSingle().setUid("uid1").setIgnore(1).setText1("первый").setIntNumber1(12).setLongNumber2(11L));
        list.add(new TestJSONSingle().setUid("uid2").setIgnore(2).setText2("второй").setIntNumber2(21).setLongNumber1(22L));

        JSONAssert.assertEquals("[" +
                "{\"uid\":\"uid1\",\"text1\":\"первый\",\"text2\":null" +
                ",\"time1\":null,\"time2\":null,\"date1\":null,\"date2\":null" +
                ",\"bool1\":null,\"bool2\":null,\"intNumber1\":12,\"intNumber2\":null" +
                ",\"longNumber1\":null,\"longNumber2\":11}" +
                ",{\"uid\":\"uid2\",\"text1\":null,\"text2\":\"второй\"" +
                ",\"time1\":null,\"time2\":null,\"date1\":null,\"date2\":null" +
                ",\"bool1\":null,\"bool2\":null,\"intNumber1\":null,\"intNumber2\":21" +
                ",\"longNumber1\":22,\"longNumber2\":null}" +
                "]", JSONConverter.toJSONArray(list), JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void fromJSONArrayTest() throws Exception {
        List<TestJSONSingle> list = new ArrayList<>();
        list.add(new TestJSONSingle());
        Assertions.assertEquals(1, list.size());

        // измененный список - как новый объект!
        list = JSONConverter.fromJSONArray(TestJSONSingle.class,
                "[{}" + // пустой объект не создается
                        ",{\"uid\":\"uid1\",\"intNumber1\":111,\"longNumber2\":17,\"text1\":\"первый\"}" +
                        ",{\"uid\":\"uid2\",\"intNumber1\":222,\"longNumber2\":18,\"text1\":\"второй\"}" +
                        ",{\"uid\":\"uid3\",\"intNumber1\":333,\"longNumber2\":19,\"text1\":\"третий\"}" +
                        "]");
        Assertions.assertEquals(3, list.size());

        for (TestJSONSingle obj : list) {
            Assertions.assertNull(obj.getIgnore());
            Assertions.assertNull(obj.getBool1());
            Assertions.assertNull(obj.getBool2());
            Assertions.assertNull(obj.getDate1());
            Assertions.assertNull(obj.getDate2());
            Assertions.assertNull(obj.getIntNumber2());
            Assertions.assertNull(obj.getLongNumber1());
            Assertions.assertNull(obj.getText2());
            Assertions.assertNull(obj.getTime1());
            Assertions.assertNull(obj.getTime2());

            if (obj.getIntNumber1().equals(111)) {
                Assertions.assertNotEquals("uid1", obj.getUid());
                Assertions.assertEquals(17, obj.getLongNumber2());
                Assertions.assertEquals("первый", obj.getText1());
            } else if (obj.getIntNumber1().equals(222)) {
                Assertions.assertNotEquals("uid2", obj.getUid());
                Assertions.assertEquals(18, obj.getLongNumber2());
                Assertions.assertEquals("второй", obj.getText1());
            } else if (obj.getIntNumber1().equals(333)) {
                Assertions.assertNotEquals("uid3", obj.getUid());
                Assertions.assertEquals(19, obj.getLongNumber2());
                Assertions.assertEquals("третий", obj.getText1());
            } else Assertions.fail();
        }

        Assertions.assertTrue(JSONConverter.fromJSONArray(TestJSONSingle.class, "[]").isEmpty());
    }

    @Test
    void toMultiJSONTest() throws Exception {
        TestJSONMulti multi = new TestJSONMulti()
                .setText1("text1")
                .setTime1(new Timestamp(1_000_000L))
                .setDate1(new Date(1_000_000L))
                .setBool1(true)
                .setIntNumber1(100)
                .setLongNumber1(2_000_000L)
                .setIntNumberList(new ArrayList<>() {{
                    add(3);
                    add(2);
                    add(1);
                }})
                .setTextList(new ArrayList<>() {{
                    add("t6");
                    add("t5");
                    add("t4");
                }})
                .setElementList(new ArrayList<>() {{
                    add(new TestJSONMulti.Element()
                            .setTime1(new Timestamp(1_500_000L))
                            .setDate1(new Date(1_500_000L))
                            .setBool1(true)
                            .setIntNumber1(666)
                            .setIntNumberList(new ArrayList<>() {{
                                add(10);
                                add(20);
                            }}));
                    add(new TestJSONMulti.Element()
                            .setDate1(new Date(2_000_000L))
                            .setBool1(false)
                            .setLongNumber1(666666L)
                            .setTextList(new ArrayList<>() {{
                                add("t10");
                                add("t20");
                            }}));
                }});

        String json = JSONConverter.toJSON(multi);
        JSONAssert.assertEquals("{" +
                "\"elementList\":[" +
                "{\"text1\":null,\"time1\":\"1970-01-01T00:25:00\",\"date1\":\"1970-01-01\"" +
                ",\"bool1\":true,\"intNumber1\":666,\"longNumber1\":null" +
                ",\"intNumberList\":[10,20]" +
                ",\"textList\":[]}" +
                ",{\"text1\":null,\"time1\":null,\"date1\":\"1970-01-01\"" +
                ",\"bool1\":false,\"intNumber1\":null,\"longNumber1\":666666" +
                ",\"intNumberList\":[]" +
                ",\"textList\":[\"t10\",\"t20\"]}" +
                "]" +
                ",\"intNumberList\":[3,2,1],\"textList\":[\"t6\",\"t5\",\"t4\"]" +
                ",\"text1\":\"text1\"" +
                ",\"time1\":\"1970-01-01T00:16:40\"" +
                ",\"date1\":\"1970-01-01\"" +
                ",\"bool1\":true" +
                ",\"intNumber1\":100" +
                ",\"longNumber1\":2000000" +
                "}", json, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void fromMultiJSONTest() throws Exception {
        String json = "{" +
                "\"text1\":\"text\"" +
                ",\"time1\":\"2020-01-01T00:10:20\"" +
                ",\"date1\":\"2020-01-01\"" +
                ",\"bool1\":true" +
                ",\"intNumber1\":123" +
                ",\"longNumber1\":20000000" +
                ",\"elementList\":[{\"a\":11,\"b\":12},{\"a\":21,\"b\":22}]" + // fixme
                ",\"intNumberList\":[1,2,3]" +
                ",\"textList\":['t1',\"t2\"]" +
                "}";

        TestJSONMulti multi = JSONConverter.fromJSON(TestJSONMulti.class, json);
        Assertions.assertNotNull(multi);
        Assertions.assertEquals("text", multi.getText1());
        Assertions.assertEquals("2020-01-01T00:10:20", TimeFormatter.formatTimestamp(multi.getTime1()));
        Assertions.assertEquals("2020-01-01", TimeFormatter.formatDate(multi.getDate1()));
        Assertions.assertTrue(multi.getBool1());
        Assertions.assertEquals(123, multi.getIntNumber1());
        Assertions.assertEquals(20000000L, multi.getLongNumber1());
        //
        Assertions.assertEquals(0, multi.getElementList().size());
        Assertions.assertEquals(3, multi.getIntNumberList().size());
        Assertions.assertTrue(multi.getIntNumberList().contains(1));
        Assertions.assertTrue(multi.getIntNumberList().contains(2));
        Assertions.assertTrue(multi.getIntNumberList().contains(3));
        Assertions.assertEquals(2, multi.getTextList().size());
        Assertions.assertTrue(multi.getTextList().contains("t1"));
        Assertions.assertTrue(multi.getTextList().contains("t2"));
    }

}