package com.example.janari.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Janari on 11/13/2017.
 */

//meie klass noteRecycleAdapter laieneb RecycleView'le ja kasutab meie klassi NoteRecyc...et hoida infot individuaalsete view'de jaoks

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{

    private  final Context mContext;
    private final List<CourseInfo> mCourses;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerAdapter(Context context, List<CourseInfo> courses) {
        this.mContext = context;
        this.mCourses = courses;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CourseInfo course = mCourses.get(position);
        holder.textCourse.setText(course.getTitle());
        //see on ka valitud kaardiga seotud
        holder.mCurrentPosition = position;

    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public final TextView textCourse;
        //see on kui kasutaja vajutab kaardile
        public int mCurrentPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            textCourse = (TextView) itemView.findViewById(R.id.text_course);

            //seostab vajutuse ära järgneva tegevusega
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();


                }
            });
        }
    }
}
