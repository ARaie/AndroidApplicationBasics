package com.example.janari.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jim.
 */
// et seda kasutatavaks teha peab lisama Parcelabel et indente ühelt activitylt teisele saata
public final class NoteInfo implements Parcelable {

    //Members of the course
    private CourseInfo mCourse;
    private String mTitle;
    private String mText;

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    //see oligi vajalik konstruktor souce jaoks
    private NoteInfo(Parcel source) {
        mCourse = source.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = source.readString();
        mText = source.readString();
    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    // paneme nad Parcelabelisse. kirjutab meie komponendid parselisse
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mCourse, 0);
        dest.writeString(mTitle);
        dest.writeString(mText);
    }

    // meie creator on nüüd võimeline genereerima instance noteInfost
    public final static Parcelable.Creator<NoteInfo> CREATOR =
            //siin on vaja teha implement methods, punane pirn pakub seda ise ja tehakse kaks publikut valmis
            new Parcelable.Creator<NoteInfo>() {

                @Override//siin tahtis konstruktorit teha
                public NoteInfo createFromParcel(Parcel source) {
                    return new NoteInfo(source);
                }

                @Override//siia lõppu panin 0 asemel size
                public NoteInfo[] newArray(int size) {
                    return new NoteInfo[size];
                }
            };
}
