package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by raheel on 11/29/15.
 */
public class ScoresWidgetIntentService extends IntentService {

    public ScoresWidgetIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoresWidgetProvider.class));
        myFetchService service = new myFetchService();
        service.getData("n2");
        service.getData("p2");

       // RemoteViews views = new RemoteViews(getPackageName(), R.layout.abc);


    }
}
