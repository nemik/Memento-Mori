package net.nemik.mori.memento;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;

public class MementoMoriActivity extends Activity
{
	//TODO: use application params to allow a date to 
	
	public static final String PREFS_NAME = "MMPrefs";

	private static final int BDATE_DIALOG_ID = 3;
	private int m_year;
	private int m_month;
	private int m_day;
	private int expected_years;
	private String part1;
	private String part2;
	
	TextView bdaylabel;
	TextView eyearslabel;
	EditText e_years_edit;
	EditText part1_edit;
	EditText part2_edit;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        m_year = settings.getInt("year", 1970);
        m_month = settings.getInt("month", 1);
        m_day = settings.getInt("day", 1);
        expected_years = settings.getInt("expected_years",expected_years);
        part1 = settings.getString("part1", getString(R.string.wall_part1));
        part2 = settings.getString("part2", getString(R.string.wall_part2));
        
        setContentView(R.layout.main);
        
        bdaylabel = (TextView) findViewById(R.id.bdaylabel);
        bdaylabel.setText("Your birthday is: " +DateFormat.format("MMMM d, yyyy", new GregorianCalendar(m_year,m_month,m_day)));
        
        //eyearslabel = (TextView) findViewById(R.id.deathlabel);    
        //eyearslabel.setText(String.format(getString(R.string.deathlabel), expected_years));
        
        e_years_edit = (EditText) findViewById(R.id.e_years);
        e_years_edit.setText(new String(""+expected_years));
        
        part1_edit = (EditText) findViewById(R.id.part1);
        part1_edit.setText(part1);
        
        part2_edit = (EditText) findViewById(R.id.part2);
        part2_edit.setText(part2);
		
        expected_years = new Integer(e_years_edit.getText().toString());
        
    }
    
    protected void onPause()
    {
        super.onPause();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		String eyears_s = e_years_edit.getText().toString();
		Toast.makeText(this, "eyears is "+eyears_s, Toast.LENGTH_LONG);
		if(eyears_s.trim().equals("") || eyears_s == null)
		{
			eyears_s="0";
		}
        editor.putInt("expected_years",new Integer(eyears_s));
        editor.putString("part1", part1_edit.getText().toString());
        editor.putString("part2", part2_edit.getText().toString());
        editor.commit();
        //eyearslabel.setText(String.format(getString(R.string.deathlabel), expected_years));  
        updateAllWidgets();
    }

    private void updateAllWidgets()
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MMWidget.class));
        if (appWidgetIds.length > 0) {
            new MMWidget().onUpdate(this, appWidgetManager, appWidgetIds);
        }
    }
    
    public void showDatePickerDialog(View v) 
    {
        showDialog(BDATE_DIALOG_ID);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
       switch (id) {
       case BDATE_DIALOG_ID:
          return new DatePickerDialog(this,
                    mDateSetListener,
                    m_year, m_month, m_day);
       }
       return null;
    }

    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) 
            {
                m_year = year;
                m_month = month;
                m_day = day;
                
                //save prefs
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("year", year);
                editor.putInt("month", month);
                editor.putInt("day", day);
                editor.commit();
                	
                bdaylabel.setText("Your birthday is: " +DateFormat.format("MMMM dd, yyyy", new GregorianCalendar(year,month,day)));
                updateAllWidgets();
            }
        };
}

