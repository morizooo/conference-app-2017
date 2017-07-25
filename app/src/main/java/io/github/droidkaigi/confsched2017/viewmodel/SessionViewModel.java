package io.github.droidkaigi.confsched2017.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Date;

import io.github.droidkaigi.confsched2017.BR;
import io.github.droidkaigi.confsched2017.R;
import io.github.droidkaigi.confsched2017.model.Session;
import io.github.droidkaigi.confsched2017.repository.sessions.MySessionsRepository;
import io.github.droidkaigi.confsched2017.util.AlarmUtil;
import io.github.droidkaigi.confsched2017.util.DateUtil;
import io.github.droidkaigi.confsched2017.view.activity.MainActivity;
import io.github.droidkaigi.confsched2017.view.helper.Navigator;
import timber.log.Timber;

public class SessionViewModel extends BaseObservable implements ViewModel {

    private Session session;

    private String shortStime = "";

    private String formattedDate = "";

    private String title = "";

    private String speakerName = "";

    private String roomName = "";

    private String minutes = "";

    private int rowSpan = 1;

    private int colSpan = 1;

    private int titleMaxLines = 3;

    private int speakerNameMaxLines = 1;

    @DrawableRes
    private int backgroundResId;

    private boolean isClickable;

    private int normalSessionItemVisibility;

    private Navigator navigator;

    SessionViewModel(@NonNull Session session, Context context, Navigator navigator) {
        this.session = session;
        this.navigator = navigator;
        this.shortStime = DateUtil.getHourMinute(session.stime);
        this.formattedDate = DateUtil.getMonthDate(session.stime, context);
        this.title = session.title;

        if (session.speaker != null) {
            this.speakerName = session.speaker.name;
        }
        if (session.room != null) {
            this.roomName = session.room.name;
        }
        this.minutes = context.getString(R.string.session_minutes, session.durationMin);

        decideRowSpan(session);
        this.colSpan = 1;

        if (session.isBreak()) {
            this.isClickable = false;
            this.backgroundResId = R.drawable.bg_empty_session;
        } else {
            this.isClickable = true;
            this.backgroundResId = session.isLiveAt(new Date()) ? R.drawable.clickable_purple : R.drawable.clickable_white;
        }

        this.normalSessionItemVisibility = (!session.isBreak()) ? View.VISIBLE : View.GONE;
    }

    private SessionViewModel(int rowSpan, int colSpan) {
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
        this.isClickable = false;
        this.backgroundResId = R.drawable.bg_empty_session;
        this.normalSessionItemVisibility = View.GONE;
    }

    static SessionViewModel createEmpty(int rowSpan) {
        return createEmpty(rowSpan, 1);
    }

    static SessionViewModel createEmpty(int rowSpan, int colSpan) {
        return new SessionViewModel(rowSpan, colSpan);
    }

    private int decideColSpan(@NonNull Session session, int roomCount) {
        return 1;
    }

    private void decideRowSpan(@NonNull Session session) {
        // Break time is over 30 min, but one row is good
        if (session.durationMin > 30 && !session.isBreak()) {
            this.rowSpan = this.rowSpan * 2;
            this.titleMaxLines = this.titleMaxLines * 2;
            this.speakerNameMaxLines = this.speakerNameMaxLines * 3;
        }
    }

    Date getStime() {
        return session.stime;
    }

    public void showSessionDetail(@SuppressWarnings("unused") View view) {
        if (navigator != null && session != null) {
            navigator.navigateToSessionDetail(session, MainActivity.class);
        }
    }

    public boolean checkSession(@SuppressWarnings("UnusedParameters") View view) {
        return true;
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    public String getShortStime() {
        return shortStime;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    String getRoomName() {
        return roomName;
    }

    public String getMinutes() {
        return minutes;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public int getTitleMaxLines() {
        return titleMaxLines;
    }

    public int getSpeakerNameMaxLines() {
        return speakerNameMaxLines;
    }

    public int getNormalSessionItemVisibility() {
        return normalSessionItemVisibility;
    }

}
