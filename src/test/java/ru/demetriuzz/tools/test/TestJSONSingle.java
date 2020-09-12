package ru.demetriuzz.tools.test;

import ru.demetriuzz.tools.TimeFormatter;
import ru.demetriuzz.tools.annotation.JSONIgnore;
import ru.demetriuzz.tools.annotation.JSONReplaceUID;

import java.sql.Date;
import java.sql.Timestamp;

public class TestJSONSingle {

    // возможные типы данных
    @JSONIgnore
    private Integer ignore;
    @JSONReplaceUID
    private String uid;

    private String text1;
    private String text2;
    private Timestamp time1;
    private Timestamp time2;
    private Date date1;
    private Date date2;
    private Boolean bool1;
    private Boolean bool2;
    private Integer intNumber1;
    private Integer intNumber2;
    private Long longNumber1;
    private Long longNumber2;

    public Integer getIgnore() {
        return ignore;
    }

    public TestJSONSingle setIgnore(Integer ignore) {
        this.ignore = ignore;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public TestJSONSingle setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getText1() {
        return text1;
    }

    public TestJSONSingle setText1(String text1) {
        this.text1 = text1;
        return this;
    }

    public String getText2() {
        return text2;
    }

    public TestJSONSingle setText2(String text2) {
        this.text2 = text2;
        return this;
    }

    public Timestamp getTime1() {
        return time1;
    }

    public TestJSONSingle setTime1(Timestamp time1) {
        this.time1 = time1;
        return this;
    }

    public Timestamp getTime2() {
        return time2;
    }

    public TestJSONSingle setTime2(Timestamp time2) {
        this.time2 = time2;
        return this;
    }

    public Date getDate1() {
        return date1;
    }

    public TestJSONSingle setDate1(Date date1) {
        this.date1 = date1;
        return this;
    }

    public Date getDate2() {
        return date2;
    }

    public TestJSONSingle setDate2(Date date2) {
        this.date2 = date2;
        return this;
    }

    public Boolean getBool1() {
        return bool1;
    }

    public TestJSONSingle setBool1(Boolean bool1) {
        this.bool1 = bool1;
        return this;
    }

    public Boolean getBool2() {
        return bool2;
    }

    public TestJSONSingle setBool2(Boolean bool2) {
        this.bool2 = bool2;
        return this;
    }

    public Integer getIntNumber1() {
        return intNumber1;
    }

    public TestJSONSingle setIntNumber1(Integer intNumber1) {
        this.intNumber1 = intNumber1;
        return this;
    }

    public Integer getIntNumber2() {
        return intNumber2;
    }

    public TestJSONSingle setIntNumber2(Integer intNumber2) {
        this.intNumber2 = intNumber2;
        return this;
    }

    public Long getLongNumber1() {
        return longNumber1;
    }

    public TestJSONSingle setLongNumber1(Long longNumber1) {
        this.longNumber1 = longNumber1;
        return this;
    }

    public Long getLongNumber2() {
        return longNumber2;
    }

    public TestJSONSingle setLongNumber2(Long longNumber2) {
        this.longNumber2 = longNumber2;
        return this;
    }

    @Override
    public String toString() {
        return "TestJSONSingle{" +
                "ignore=" + ignore +
                ", uid='" + uid + '\'' +
                ", text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", time1=" + TimeFormatter.formatTimestamp(time1) +
                ", time2=" + TimeFormatter.formatTimestamp(time2) +
                ", date1=" + TimeFormatter.formatDate(date1) +
                ", date2=" + TimeFormatter.formatDate(date2) +
                ", bool1=" + bool1 +
                ", bool2=" + bool2 +
                ", intNumber1=" + intNumber1 +
                ", intNumber2=" + intNumber2 +
                ", longNumber1=" + longNumber1 +
                ", longNumber2=" + longNumber2 +
                '}';
    }

}