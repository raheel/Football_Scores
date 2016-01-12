package barqsoft.footballscores.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresProvider;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.scoresAdapter;
import barqsoft.footballscores.service.myFetchService;


public class WidgetService extends RemoteViewsService {

	private Map<Integer, List<Match>> matchesMap = null;

	private static final String TAG = WidgetService.class
			.getSimpleName();


	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		int index = intent.getIntExtra("index", 0);

			Log.d(TAG, "onGetViewFactory()");

		if (matchesMap==null) {
			matchesMap = new HashMap<Integer, List<Match>>();
			myFetchService service = new myFetchService();
			service.getData("n2");
			service.getData("p2");
			for (int i = 0; i < 5; i++) {
				List<Match> matches = new ArrayList<Match>();
				matchesMap.put(i - 2, matches);
				Date date = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
				SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

				String fragmentdate = mformat.format(date);

				Cursor cursor = Utilities.getAllMatchesByDate(getApplicationContext(), fragmentdate);

				if (cursor != null) {
					if (cursor.moveToFirst()) {
						do {
							String teams = cursor.getString(scoresAdapter.COL_HOME) + " vs " + cursor.getString(scoresAdapter.COL_AWAY);
							String score = Utilities.getScores(cursor.getInt(scoresAdapter.COL_HOME_GOALS), cursor.getInt(scoresAdapter.COL_AWAY_GOALS));
							matches.add(new Match(teams, score));
						} while (cursor.moveToNext());
					}
					cursor.close();
				}

			}
		}
		return new MatchViewFactory(getApplicationContext(), matchesMap.get(index));
	}

	private static class ViewHolder {
		TextView teamsView;
		TextView scoresView;
	}

	private class MatchViewFactory implements RemoteViewsFactory{
		private Context context;
		private List<Match> matches;

		public MatchViewFactory(Context context, List<Match> matches) {
			this.context = context;
			this.matches = matches;
		}

		@Override
		public void onCreate() {

		}

		@Override
		public void onDataSetChanged() {

		}

		@Override
		public void onDestroy() {

		}

		@Override
		public int getCount() {
			return matches.size();
		}

		@Override
		public RemoteViews getViewAt(int position) {
			RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.scores);

			if (position < matches.size()) {
				try {
					row.setTextViewText(R.id.match_teams, matches.get(position).getTeams() + ": " + matches.get(position).getScore());
				} catch (Exception e) {
					System.out.println("e.getMessage() = " + e.getMessage());
				}
			}
			return row;
		}

		@Override
		public RemoteViews getLoadingView() {
			return null;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}
	}


		private class ViewFactory implements RemoteViewsFactory {
		
		private int mInstanceId = AppWidgetManager.INVALID_APPWIDGET_ID;
		private Date mUpdateDate = new Date();
		
		public ViewFactory(Intent intent) {
			mInstanceId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID );
		}

		@Override
		public void onCreate() {
			Log.i(TAG, "onCreate()");

		}

		@Override
		public void onDataSetChanged() {
			Log.i(TAG, "onDataSetChanged()");

			mUpdateDate = new Date();
		}

		@Override
		public void onDestroy() {
			Log.i(TAG, "onDestroy()");
		}

		@Override
		public int getCount() {
			Log.i(TAG, "getCount() " + 5);

			return 5;
		}

		@Override
		public RemoteViews getViewAt(int position) {
			Log.i(TAG, "getViewAt()" + position);

			RemoteViews page = new RemoteViews(getPackageName(), R.layout.widget_list_view);
			SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
					.getDateTimeInstance();
			page.setTextViewText(R.id.match_date, sdf.format(mUpdateDate));

			return page;
		}

		@Override
		public RemoteViews getLoadingView() {
			Log.i(TAG, "getLoadingView()");

			return null;
		}

		@Override
		public int getViewTypeCount() {
			Log.i(TAG, "getViewTypeCount()");

			return 1;
		}

		@Override
		public long getItemId(int position) {
			Log.i(TAG, "getItemId()");

			return position;
		}

		@Override
		public boolean hasStableIds() {
			Log.i(TAG, "hasStableIds()");

			return true;
		}

	}

	public static class Match {
		private String teams;
		private String score;

		public Match(String teams, String score) {
			this.teams = teams;
			this.score = score;
		}

		public String getTeams() {
			return teams;
		}

		public void setTeams(String teams) {
			this.teams = teams;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}
	}

}
