package com.garkin.laban5;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextMiddleName, editTextLastName;
    private TextView tvIdPerson;
    private static PersonDbHelper personDbHelper;
    private static Person person;
    private Long idPerson;
    private static List<Person> personList;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personDbHelper = new PersonDbHelper(this);

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextMiddleName = (EditText) findViewById(R.id.editTextMiddleName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);

        tvIdPerson = (TextView) findViewById(R.id.tvIdPerson);

        personList = personDbHelper.getPersonList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }

    private void clearText() {
        tvIdPerson.setText("");
        editTextFirstName.getText().clear();
        editTextMiddleName.getText().clear();
        editTextLastName.getText().clear();
    }

    private void createPerson(){
        Person person = new Person(
                editTextFirstName.getText().toString(),
                editTextMiddleName.getText().toString(),
                editTextLastName.getText().toString()
        );
        idPerson = personDbHelper.createPerson(person);

        if (idPerson > 0) {
            tvIdPerson.setText(String.format("ID заявки - %d", idPerson));
            Toast.makeText(getApplicationContext(), "Человек успешно создан",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Не все поля заполнены",Toast.LENGTH_LONG).show();
        }
        personList = personDbHelper.getPersonList();
    }

    private void updatePerson(){
        Person person = new Person(
                MainActivity.person.getId(),
                editTextFirstName.getText().toString(),
                editTextMiddleName.getText().toString(),
                editTextLastName.getText().toString()
        );
        int result = personDbHelper.updatePerson(person);
        String message;
        if (result == 1) {
            message = "Заявка успешно обновлена";
        } else {
            message = "Заявка не обновлена!";
        }
        showToast(message);
        personList = personDbHelper.getPersonList();
    }

    private void deletePerson(){
        int result = personDbHelper.deletePerson(person.getId());

        if (result == 1) {
            Toast.makeText(getApplicationContext(), "Заявка успешно удалена!",Toast.LENGTH_LONG).show();
            clearText();
        } else {
            Toast.makeText(getApplicationContext(), "Заполните все поля, заявка не создана!",Toast.LENGTH_LONG).show();
        }
        personList = personDbHelper.getPersonList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new_person:
                clearText();
                break;
            case R.id.action_create:
                createPerson();
                break;
            case R.id.action_edit:
                updatePerson();
                break;
            case R.id.action_delete:
                deletePerson();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private EditText editTextFirstName, editTextMiddleName, editTextLastName;
        private TextView tvIdPerson;

        private static final String ARG_ID = "_ID";
        private static final String ARG_FIRST_NAME = "first_name";
        private static final String ARG_MIDDLE_NAME = "middle_name";
        private static final String ARG_LAST_NAME = "last_name";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            if (!personList.isEmpty() && sectionNumber < personList.size() - 1 ){
                Person person = personList.get(sectionNumber);
                args.putLong(ARG_ID, person.getId());
                args.putString(ARG_FIRST_NAME, person.getFirstName());
                args.putString(ARG_MIDDLE_NAME, person.getMiddleName());
                args.putString(ARG_LAST_NAME, person.getLastName());
                fragment.setArguments(args);
            }

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            editTextFirstName = (EditText) rootView.findViewById(R.id.editTextFirstName);
            editTextMiddleName = (EditText) rootView.findViewById(R.id.editTextMiddleName);
            editTextLastName = (EditText) rootView.findViewById(R.id.editTextLastName);

            tvIdPerson = (TextView) rootView.findViewById(R.id.tvIdPerson);

//            Long id = getArguments().getLong(ARG_ID);
//            String first_name = getArguments().getString(ARG_FIRST_NAME);
//            String middle_name = getArguments().getString(ARG_MIDDLE_NAME);
//            String last_name = getArguments().getString(ARG_LAST_NAME);
//
//            person = new Person(id, first_name, middle_name, last_name);
//
//
//            setPersonInView(person);
            return rootView;
        }

        private void setPersonInView(Person person){
            editTextFirstName.getText().clear();
            editTextMiddleName.getText().clear();
            editTextLastName.getText().clear();

            tvIdPerson.setText("Номер пользователя = " + person.getId());

            editTextFirstName.getText().append(person.getFirstName());
            editTextMiddleName.getText().append(person.getMiddleName());
            editTextLastName.getText().append(person.getLastName());
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return personList.size() > 0 ? personList.size():1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                default:
//                    return personList.get(position).getFirstName();
                    return "Всенм привет";
            }
        }
    }
}
