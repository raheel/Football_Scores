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
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresProvider;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.scoresAdapter;
import barqsoft.footballscores.service.myFetchService;


public class WidgetService extends RemoteViewsService {

	private static final String TAG = WidgetService.class
			.getSimpleName();


	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
			Log.d(TAG, "onGetViewFactory()");
			myFetchService service = new myFetchService();
			service.getData("n2");
			service.getData("p2");

			for (int i=0; i<5; i++) {
				Date date = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
				SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

				String fragmentdate = mformat.format(date);

				//fragmentdate = "2015-12-" + (10 + i);

				System.out.println("fragmentdate = " + fragmentdate);
				Cursor cursor = Utilities.getAllMatchesByDate(getApplicationContext(), fragmentdate);

				System.out.println("_cursor = " + cursor);

				if (cursor!=null){
					if (cursor.moveToFirst()) {
						do {
							String teams  = cursor.getString(scoresAdapter.COL_HOME) + " vs " + cursor.getString(scoresAdapter.COL_AWAY) ;
							System.out.println("\tteams	 = " + teams);
							String matchDate = cursor.getString(scoresAdapter.COL_DATE);
							System.out.println("matchDate = " + matchDate);
							String score = Utilities.getScores(cursor.getInt(scoresAdapter.COL_HOME_GOALS),cursor.getInt(scoresAdapter.COL_AWAY_GOALS));
							System.out.println("score = " + score);
							System.out.println("\n");
						} while (cursor.moveToNext());
					}
					cursor.close();
				}


			}

		List<Match> matches = new ArrayList<Match>();
		return new MatchViewFactory(getApplicationContext(), matches);
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
			return 5;
		}

		int i = 1;
		@Override
		public RemoteViews getViewAt(int position) {
			System.out.println("MatchViewFactory.getViewAt " + position );
//			RemoteViews parent = new RemoteViews(context.getPackageName(), R.layout.list_view);
//			parent.setTextViewText(R.id.match_date, "Today's Date " + position);
////			for (int i=0; i<getCount(); i++) {
////				System.out.println("\ti = " + i);
////				RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.scores);
////				row.setTextViewText(R.id.match_teams, matches.get(position).getTeams());
////				row.setTextViewText(R.id.match_score, matches.get(position).getScore());
////				parent.addView(R.layout.scores, row);
////			}
//			return parent;

			RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.scores);

			if (i < matches.size()) {
				row.setTextViewText(R.id.match_teams, matches.get(position).getTeams() + " " + i++);
				row.setTextViewText(R.id.match_score, matches.get(position).getScore() + " " + i++);
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
