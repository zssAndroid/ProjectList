package com.zss.quickindex.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zss.quickindex.R;
import com.zss.quickindex.domain.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:      PersonAdapter
 * 创建者:    PoplarTang
 * 创建时间:  2016/11/2.
 * 描述：     TODO
 */

public class PersonAdapter extends BaseAdapter {

    List<Person> persons = new ArrayList<Person>();

    public void setDatas(List<Person> persons){
        this.persons = persons;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Person getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = View.inflate(parent.getContext(), R.layout.item_list_person, null);
        }

        ViewHolder holder = ViewHolder.getHolder(view);

        Person p = getItem(position);

        String currentLetter = p.getLetter();
        String letter = null; // 如果此变量被赋值了, 说明需要显示.
        if(position == 0){
//        1. position == 0
            letter = currentLetter;
        }else {
//        2. 当前首字母和上一个不同
            String preLetter = getItem(position - 1).getLetter();
            if(!TextUtils.equals(currentLetter, preLetter)){
                letter = currentLetter;
            }
        }

        holder.tv_index.setVisibility(letter == null? View.GONE : View.VISIBLE);
        holder.tv_index.setText(letter);
        holder.tv_name.setText(p.getName());

        return view;
    }
    
    static class ViewHolder {

        TextView tv_index;
        TextView tv_name;

        public static ViewHolder getHolder(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();

            if(holder == null){
                holder = new ViewHolder();
                holder.tv_index = (TextView) view.findViewById(R.id.tv_index);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(holder);
            }

            return holder;
        }
    }
}
