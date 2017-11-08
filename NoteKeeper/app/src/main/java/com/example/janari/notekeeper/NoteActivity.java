package com.example.janari.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "com.example.janari.notekeeper.NOTE_POSITION";
    //kõigile kolmele tuleb teha mida ma tahan hoida saveOriginalValues
    //Sest nagu sa mäletad muidu ta hakkab XXXXX ka originaalideks võtma aga seda me ei taha
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.example.janari.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.jwhh.example.janari.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.jwhh.example.janari.notekeeper.ORIGINAL_NOTE_TEXT";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;
    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Et spinner ühendada oma koodi

        //ja peab olema field, sest ma tahan seda üle klassi kasutada ja muidu saab seda ainult siin meetodis kasutada
        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        // Adapter et courseInfo spinneri sisse saada. Esmalt teeme listi courseInfoga, mis asuvad DataManageris
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        //Adapter et ühendada see list spinneriga. Kontekst, resource, list mida me tahame sinna panna.This = current activity to the context
        // Android pakub ise android.R.layout.see käib siis spinneri kohta, lõppu oma list
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);

        //et seostada dropdown list of courses
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //See rida on selleks et seostada oma adapter spinneriga
        mSpinnerCourses.setAdapter(adapterCourses);


        readDisplayStateValues();
        //nüüd ta kontrollib kas on muudatusi tehtud...kui midagi on katki vahepal läinud. et kui ma emulaatori julmalt
        //kinni panin ja siis võtan uuesti emaili ette mis mul pooleli jäi siis seal ootab mind kõik nii nagu ma jätsin kui ma kadusin
        if(savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        //Nendega sama lugu, meil on neid vaja kasutada ka väljaspool seda meetodit seega peavad naad saama fieldiks

        mTextNoteTitle = (EditText) findViewById(R.id.text_note_title);
        mTextNoteText = (EditText) findViewById(R.id.text_note_text);

        //tähendab kui siis ei ole sisu edasi anda ja me ei kuva seda, kuvame ainult siis sisu kui EI ole uus note
        if(!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
    }

    //need mis me ülemuslikult salvestasime saame nüüd tagasi panna
    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }


    //et kui kasutada kritseldab midagi juurde ja siis otsustab canceldada et selleks puhuks on meil nüüd olemas originaalväärtused,
    //mis me saame sel puhul tagasi panna. See salvestab ja teine...
    private void saveOriginalNoteValues() {
        if(mIsNewNote)
            return;//kui see on uus note siis seal pole midagi sees ja ma lasen jalga siit
        //võtan välja course ID
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();
    }

    //et salvestada noutse
    @Override
    protected void onPause() {
        super.onPause();
        //seome canceldamise ka sisse
        if(mIsCancelling) {
            //juhul kui me uue note lisamisel canceldame siis eemaldatakse see note
            if(mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            } else {//et ta vahepeal uuesti ei salvestaks, tsükli värk
                //kui see munnmihkel nüüd canseldab siis meil on olemas originaalväärtused mis me saame tagasi panna
                storePreviousNoteValues();
            }
            //kui me ei cancelda siis salvestatakse note
        } else {
            saveNote();
        }
    }

    //See meetod paneb siis originaalväärtused tagasi
    private void storePreviousNoteValues() {
        //annab viite originaal coursele
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        //set' iga paneb nad tagasi
        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    @Override//me tahame neid väärtusi hoida
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    //Notside salvestamiseks

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    //kuvame notsid
    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        //kuna spinneris on list siis me tahame teada ju kindlat kohta kuhu notsid panna. võtame esmalt õige indeksi välja
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        //paneme sisu paika mida me tahame
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    //me tahame nüüd notsid intentist kätte saada ja kuvada nad ekraanile
//see saab notsid Extralt
    //siin me saame notsid tagasi kätte
    private void readDisplayStateValues() {
        Intent intent = getIntent();
        //sest tegemist on positsiooniga, sulgudes teine lilla on et mida sa tahad tagastada kui saadetises ei ole midagi(POSITION_NOT_SET=-1)
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET; // siis ei ole midagi sees
        if(mIsNewNote) {
            //juhul kui on tegemist uue notiga
            createNewNote();
            //juhul kui see ei ole uus note
        } else {
            //pannakse mNote muutujasse väärtus, kui ta ei ole uus note ja kui seal saadetud extras oli midagi sees
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    //peab tegeme viite datamanager
    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        //võtab dataManagerist selle meetodi, tegime fieldiks selle
        mNotePosition = dm.createNewNote();
        //nüüd saame võtta note ja määrata selle vajalikule positsioonile. väärtused, mida ma annan edasi
        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }


    //kui inimene valib menüüst midagi siis suunatakse ta siia
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //kõik oma menüü võimalused pean siin ka siduma
        //lilla on meie pandus nimi fieldile
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
            //oma lisatud canceli pean siia ka panema
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            //see finish lõpetab nüüd tegevuse
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        //saame tagasi valitud itemid spinnerilt ja paneme nad course muutujasse
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        //siin on nii et me saame midagi muud aga tahame et see oleks nüüd Stringiks teisendatud
        String subject = mTextNoteTitle.getText().toString();
        //anname oma emailile keha ja paneme valitud sisu ka sisse ja ette pealkirja
        String text = "Checkout what I learned in the Pluralsight course \"" +
                course.getTitle() +"\"\n" + mTextNoteText.getText().toString();
        //intent meili edasi saatmiseks
        Intent intent = new Intent(Intent.ACTION_SEND);
        //kuna ma tahan emaili saata siis ma pean emaili sisu määrama, standard, need erinevad on dokumeteeritud
        intent.setType("message/rfc2822");
        //paneb automaatselt pealkirjaks meie title ja sisuks meie notsi
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        //intent on loodud nüüd peab käivitama activity ja intenti edasi andma
        startActivity(intent);
    }
}
