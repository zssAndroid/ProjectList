package com.zss.quickindex.domain;


import com.zss.quickindex.util.PinyinUtil;

/**
 * 类名:      Person
 * 创建者:    PoplarTang
 * 创建时间:  2016/11/2.
 * 描述：     TODO
 */
public class Person implements Comparable<Person>{

    String name;
    String pinyin;
    String letter;

    public Person(String name) {
        this.name = name;
        this.pinyin = PinyinUtil.getPinyin(name);
        this.letter = pinyin.charAt(0) + "";
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getLetter() {
        return letter;
    }

    @Override
    public int compareTo(Person another) {
        return this.pinyin.compareToIgnoreCase(another.pinyin);
    }
}
