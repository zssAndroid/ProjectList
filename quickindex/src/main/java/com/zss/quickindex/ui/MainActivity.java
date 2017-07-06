package com.zss.quickindex.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;

import com.zss.quickindex.R;
import com.zss.quickindex.adapter.PersonAdapter;
import com.zss.quickindex.domain.Person;
import com.zss.quickindex.util.Cheeses;
import com.zss.quickindex.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuickIndexBar.OnLetterUpdateListener {

    private QuickIndexBar qib;
    private PersonAdapter personAdapter;
    private ListView lv;
    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qib = (QuickIndexBar) findViewById(R.id.qib);
        qib.setOnLetterUpdateListener(this);

        lv = (ListView) findViewById(R.id.lv);

        personAdapter = new PersonAdapter();
        lv.setAdapter(personAdapter);

        // 更新数据
        persons = new ArrayList<>();
        fillAndSortData(persons);
        personAdapter.setDatas(persons);
    }

    // 填充并排序
    private void fillAndSortData(List<Person> persons) {
        // 填充
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            String name = Cheeses.NAMES[i];
            persons.add(new Person(name));
        }
        // 排序
        Collections.sort(persons);

    }


    @Override
    public void onLetterUpdate(String letter){
        Utils.showToast(this, letter);

        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if(TextUtils.equals(letter, person.getLetter())){
                // 跳转到i位置即可
                lv.setSelection(i);
                break;
            }

        }
    }
}
