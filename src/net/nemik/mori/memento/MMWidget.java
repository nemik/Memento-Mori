package net.nemik.mori.memento;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class MMWidget extends AppWidgetProvider
{	
	public static final String PREFS_NAME = "MMPrefs";

	private int m_year;
	private int m_month;
	private int m_day;
	private int expected_years;
	private String part1;
	private String part2;
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		// Restore preferences
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        m_year = settings.getInt("year", 1970);
        m_month = settings.getInt("month", 1);
        m_day = settings.getInt("day", 1);
        expected_years = settings.getInt("expected_years", 72);
        part1 = settings.getString("part1", context.getString(R.string.wall_part1));
        part2 = settings.getString("part2", context.getString(R.string.wall_part2));
        
		DateMidnight deathdate = new DateMidnight(m_year+expected_years, m_month, m_day);
		DateTime now = new DateTime();
		int remains = Days.daysBetween(now,deathdate).getDays();
		
		final int N = appWidgetIds.length;
		
		for (int i = 0; i < N; i++)
		{
			int appWidgetId = appWidgetIds[i];
			
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.mmwidget);
			
			views.setTextViewText(R.id.mmwidgetlabel, part1 + remains + part2);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
