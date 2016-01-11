package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;

public class WidgetProvider extends AppWidgetProvider {

	private static final String TAG = WidgetProvider.class.getSimpleName();

	public static final String PREVIOUS_ACTION = "me.sunphiz.apdapterviewflipperwidget.PREVIOUS";
	public static final String NEXT_ACTION = "me.sunphiz.apdapterviewflipperwidget.NEXT";

	private static int index = 0;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int id : appWidgetIds) {
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_list_view);
			rv.setTextViewText(R.id.match_date, "Today");

			// Specify the service to provide data for the collection widget.
			// Note that we need to
			// embed the appWidgetId via the data otherwise it will be ignored.
			final Intent intent = new Intent(context, WidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			rv.setRemoteAdapter(R.id.score_list, intent);

			// Bind the click intent for the next button on the widgetw
			final Intent nextIntent = new Intent(context,
					WidgetProvider.class);
			nextIntent.setAction(WidgetProvider.NEXT_ACTION);
			nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
			nextIntent.putExtra("day", 0);
			final PendingIntent nextPendingIntent = PendingIntent
					.getBroadcast(context, 0, nextIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.next, nextPendingIntent);


			// Bind the click intent for the refresh button on the widget
			final Intent previousIntent = new Intent(context,
					WidgetProvider.class);
			previousIntent.setAction(WidgetProvider.PREVIOUS_ACTION);
			final PendingIntent refreshPendingIntent = PendingIntent
					.getBroadcast(context, 0, previousIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.previous, refreshPendingIntent);

			appWidgetManager.updateAppWidget(id, rv);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();

		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

		if (action.equals(PREVIOUS_ACTION)) {
			index--;
			if (index <= -2) {
				rv.setViewVisibility(R.id.previous, View.INVISIBLE);
			}

			if (index < 2) {
				rv.setViewVisibility(R.id.next, View.VISIBLE);
			}
		}

		if (action.equals(NEXT_ACTION)) {
			index++;

			if (index >= 2) {
				rv.setViewVisibility(R.id.next, View.INVISIBLE);
			}

			if (index > -2) {
				rv.setViewVisibility(R.id.previous, View.VISIBLE);
			}
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, index);

		rv.setTextViewText(R.id.match_date, Utilities.getDayName(context, c.getTimeInMillis()));

		int appWidgetId = intent.getIntExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);

		final Intent scoreIntent = new Intent(context, WidgetService.class);
		scoreIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		scoreIntent.putExtra("index", index);
		scoreIntent.setData(Uri.parse(scoreIntent.toUri(Intent.URI_INTENT_SCHEME) + "/" + index));
		rv.setRemoteAdapter(R.id.score_list, scoreIntent);

		AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, WidgetProvider.class), rv);

		super.onReceive(context, intent);
	}
}
