package com.cs410tutorgroup.findatutor;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class TutorRegistration extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_registration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutor_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerButtonClicked(View view)
    {
        //Check the text to make sure it's valid
        boolean nameGood = false;
        boolean emailGood = false;

        String nameString = ((EditText) findViewById(R.id.name_edit)).getText().toString();
        if(nameString.matches(getString(R.string.name_regex)))
        {
            nameGood = true;
            Log.d("NameRegEx", "It's good!");
        }
        else
        {
            showErrorDialog(R.string.name_error);
        }

        String emailString = ((EditText) findViewById(R.id.email_edit)).getText().toString();
        if(emailString.matches(getString(R.string.email_regex)))
        {
            emailGood = true;
            Log.d("EmailRegEx", "It's good!");
        }
        else
        {
            showErrorDialog(R.string.email_error);
        }

        if(nameGood && emailGood)
        {
            new AddTutorTask().execute(new ApiConnector());
            //Show progress dialog
        }
    }

    void showErrorDialog(int messageId)
    {
        DialogFragment newFragment = ErrorDialogFragment.newInstance(messageId);
        newFragment.show(getFragmentManager(), "dialog");
    }

    private class AddTutorTask extends AsyncTask<ApiConnector,Long,Boolean>
    {
        @Override
        protected Boolean doInBackground(ApiConnector... params)
        {
            try
            {
                EditText nameEdit = (EditText) findViewById(R.id.name_edit);
                EditText emailEdit = (EditText) findViewById(R.id.email_edit);
                EditText addressEdit = (EditText) findViewById(R.id.address_edit);

                return params[0].AddTutor(nameEdit.getText().toString(), emailEdit.getText().toString(), addressEdit.getText().toString());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            if(success)
            {
                showErrorDialog(R.string.registration_complete);
            }
            else
            {
                showErrorDialog(R.string.address_error);
            }
        }
    }

    public static class ErrorDialogFragment extends DialogFragment
    {
        public static ErrorDialogFragment newInstance(int message)
        {
            ErrorDialogFragment frag = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putInt("message", message);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            int message = getArguments().getInt("message");

            return new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setPositiveButton(R.string.alert_dialog_ok,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton){}
                            }
                    )
                    .create();
        }
    }


}
