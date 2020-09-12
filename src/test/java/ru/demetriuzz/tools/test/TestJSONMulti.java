package ru.demetriuzz.tools.test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class TestJSONMulti {

    public static class Element {

        private String text1;
        private Timestamp time1;
        private Date date1;
        private Boolean bool1;
        private Integer intNumber1;
        private Long longNumber1;

        private List<Integer> intNumberList;
        private List<String> textList;

        public String getText1() {
            return text1;
        }

        public Element setText1(String text1) {
            this.text1 = text1;
            return this;
        }

        public Timestamp getTime1() {
            return time1;
        }

        public Element setTime1(Timestamp time1) {
            this.time1 = time1;
            return this;
        }

        public Date getDate1() {
            return date1;
        }

        public Element setDate1(Date date1) {
            this.date1 = date1;
            return this;
        }

        public Boolean getBool1() {
            return bool1;
        }

        public Element setBool1(Boolean bool1) {
            this.bool1 = bool1;
            return this;
        }

        public Integer getIntNumber1() {
            return intNumber1;
        }

        public Element setIntNumber1(Integer intNumber1) {
            this.intNumber1 = intNumber1;
            return this;
        }

        public Long getLongNumber1() {
            return longNumber1;
        }

        public Element setLongNumber1(Long longNumber1) {
            this.longNumber1 = longNumber1;
            return this;
        }

        public List<Integer> getIntNumberList() {
            return intNumberList;
        }

        public Element setIntNumberList(List<Integer> intNumberList) {
            this.intNumberList = intNumberList;
            return this;
        }

        public List<String> getTextList() {
            return textList;
        }

        public Element setTextList(List<String> textList) {
            this.textList = textList;
            return this;
        }

    }

    private List<Element> elementList;

    private List<Integer> intNumberList;
    private List<String> textList;

    private String text1;
    private Timestamp time1;
    private Date date1;
    private Boolean bool1;
    private Integer intNumber1;
    private Long longNumber1;

    public List<Element> getElementList() {
        return elementList;
    }

    public TestJSONMulti setElementList(List<Element> elementList) {
        this.elementList = elementList;
        return this;
    }

    public List<Integer> getIntNumberList() {
        return intNumberList;
    }

    public TestJSONMulti setIntNumberList(List<Integer> intNumberList) {
        this.intNumberList = intNumberList;
        return this;
    }

    public List<String> getTextList() {
        return textList;
    }

    public TestJSONMulti setTextList(List<String> textList) {
        this.textList = textList;
        return this;
    }

    public String getText1() {
        return text1;
    }

    public TestJSONMulti setText1(String text1) {
        this.text1 = text1;
        return this;
    }

    public Timestamp getTime1() {
        return time1;
    }

    public TestJSONMulti setTime1(Timestamp time1) {
        this.time1 = time1;
        return this;
    }

    public Date getDate1() {
        return date1;
    }

    public TestJSONMulti setDate1(Date date1) {
        this.date1 = date1;
        return this;
    }

    public Boolean getBool1() {
        return bool1;
    }

    public TestJSONMulti setBool1(Boolean bool1) {
        this.bool1 = bool1;
        return this;
    }

    public Integer getIntNumber1() {
        return intNumber1;
    }

    public TestJSONMulti setIntNumber1(Integer intNumber1) {
        this.intNumber1 = intNumber1;
        return this;
    }

    public Long getLongNumber1() {
        return longNumber1;
    }

    public TestJSONMulti setLongNumber1(Long longNumber1) {
        this.longNumber1 = longNumber1;
        return this;
    }

}