package com.cyanogenmod.cmparts.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cyanogenmod.cmparts.R;

import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemProperties;
import android.content.ContentResolver;
import android.database.Cursor;
import android.preference.MultiSelectListPreference;
import android.provider.BaseColumns;
import android.provider.Calendar;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

public class InputActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String LOCKSCREEN_MUSIC_CONTROLS = "lockscreen_music_controls";
    private static final String LOCKSCREEN_ALWAYS_MUSIC_CONTROLS = "lockscreen_always_music_controls";
    private static final String TRACKBALL_WAKE_PREF = "pref_trackball_wake";
    private static final String VOLUME_WAKE_PREF = "pref_volume_wake";
    private static final String TRACKBALL_UNLOCK_PREF = "pref_trackball_unlock";
    private static final String MENU_UNLOCK_PREF = "pref_menu_unlock";
    private static final String BUTTON_CATEGORY = "pref_category_button_settings";
    private static final String LOCKSCREEN_STYLE_PREF = "pref_lockscreen_style";
    private static final String LOCKSCREEN_QUICK_UNLOCK_CONTROL = "lockscreen_quick_unlock_control";
    private static final String LOCKSCREEN_ALWAYS_BATTERY = "lockscreen_always_battery";
    private static final String LOCKSCREEN_CUSTOM_APP_TOGGLE = "pref_lockscreen_custom_app_toggle";
    private static final String LOCKSCREEN_CUSTOM_APP_ACTIVITY = "pref_lockscreen_custom_app_activity";
    private static final String LOCKSCREEN_DISABLE_UNLOCK_TAB = "lockscreen_disable_unlock_tab";
    private static final String LOCKSCREEN_CALENDARS = "lockscreen_calendars";
    private static final String LOCKSCREEN_CALENDAR_LOOKAHEAD = "lockscreen_calendar_lookahead";
    private static final String LOCKSCREEN_CALENDAR_REMINDERS_ONLY = "lockscreen_calendar_reminders_only";
    private static final String LOCKSCREEN_CALENDAR_ALARM = "lockscreen_calendar_alarm";
    private static final String LOCKSCREEN_CALENDAR_SHOW_LOCATION = "lockscreen_calendar_show_location";
    private static final String LOCKSCREEN_CALENDAR_SHOW_DESCRIPTION = "lockscreen_calendar_show_description";
    private static final String USER_DEFINED_KEY1 = "pref_user_defined_key1";
    private static final String USER_DEFINED_KEY2 = "pref_user_defined_key2";
    private static final String USER_DEFINED_KEY3 = "pref_user_defined_key3";
    private static final String MESSAGING_TAB_APP = "pref_messaging_tab_app";
    private static final String KEYPAD_TYPE_PREF = "pref_keypad_type";
    private static final String KEYPAD_TYPE_PERSIST_PROP = "persist.sys.keypad_type";
    private static final String KEYPAD_TYPE_DEFAULT = "euro-qwerty";
    private static final String DOCK_OBSERVER_OFF_PREF = "pref_dock_observer_off";
    private static final String DOCK_OBSERVER_OFF_PERSIST_PROP = "persist.sys.dock_observer_off";
    private static final String DOCK_OBSERVER_OFF_DEFAULT = "0";
    private static final String VOLBTN_ORIENT_PREF = "pref_volbtn_orientation";
    private static final String VOLBTN_ORIENT_PERSIST_PROP = "persist.sys.volbtn_orient_swap";
    private static final String VOLBTN_ORIENT_DEFAULT = "0";
    private static final String QTOUCH_NUM_PREF = "pref_qtouch_num";
    private static final String QTOUCH_NUM_PERSIST_PROP = "persist.sys.qtouch_num";
    private static final String QTOUCH_NUM_DEFAULT = "2";

    private CheckBoxPreference mMusicControlPref;
    private CheckBoxPreference mAlwaysMusicControlPref;
    private CheckBoxPreference mAlwaysBatteryPref;
    private CheckBoxPreference mCalendarAlarmPref;
    private CheckBoxPreference mCalendarRemindersOnlyPref;
    private CheckBoxPreference mTrackballWakePref;
    private CheckBoxPreference mVolumeWakePref;
    private CheckBoxPreference mTrackballUnlockPref;
    private CheckBoxPreference mMenuUnlockPref;
    private CheckBoxPreference mQuickUnlockScreenPref;
    private CheckBoxPreference mPhoneMessagingTabPref;
    private CheckBoxPreference mCustomAppTogglePref;
    private CheckBoxPreference mDisableUnlockTab;
    private CheckBoxPreference mDockObserverOffPref;
    private CheckBoxPreference mVolBtnOrientationPref;

    private ListPreference mLockscreenStylePref;
    private ListPreference mKeypadTypePref;
    private ListPreference mQtouchNumPref;
    private ListPreference mCalendarAlarmLookaheadPref;
    private ListPreference mCalendarShowLocationPref;
    private ListPreference mCalendarShowDescriptionPref;
    private ListPreference mLockscreenMusicHeadsetPref;

    private MultiSelectListPreference mCalendarsPref;

    private Preference mUserDefinedKey1Pref;
    private Preference mUserDefinedKey2Pref;
    private Preference mUserDefinedKey3Pref;
    private Preference mCustomAppActivityPref;
    private int mKeyNumber = 1;

    private static final int REQUEST_PICK_SHORTCUT = 1;
    private static final int REQUEST_PICK_APPLICATION = 2;
    private static final int REQUEST_CREATE_SHORTCUT = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.input_settings_title);
        addPreferencesFromResource(R.xml.input_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
                
        /* Music Controls */
        mMusicControlPref = (CheckBoxPreference) prefSet.findPreference(LOCKSCREEN_MUSIC_CONTROLS);
        mMusicControlPref.setChecked(Settings.System.getInt(getContentResolver(), 
                Settings.System.LOCKSCREEN_MUSIC_CONTROLS, 1) == 1);

        /* Always Display Music Controls */
        mAlwaysMusicControlPref = (CheckBoxPreference) prefSet.findPreference(LOCKSCREEN_ALWAYS_MUSIC_CONTROLS);
        mAlwaysMusicControlPref.setChecked(Settings.System.getInt(getContentResolver(), 
                Settings.System.LOCKSCREEN_ALWAYS_MUSIC_CONTROLS, 0) == 1);

        /* Always Display Battery Status */
        mAlwaysBatteryPref = (CheckBoxPreference) prefSet.findPreference(LOCKSCREEN_ALWAYS_BATTERY);
        mAlwaysBatteryPref.setChecked(Settings.System.getInt(getContentResolver(), 
                Settings.System.LOCKSCREEN_ALWAYS_BATTERY, 0) == 1);

        /* Calendars */
        mCalendarsPref = (MultiSelectListPreference) prefSet.findPreference(LOCKSCREEN_CALENDARS);
        mCalendarsPref.setValue(Settings.System.getString(getContentResolver(),
                Settings.System.LOCKSCREEN_CALENDARS));
        mCalendarsPref.setOnPreferenceChangeListener(this);
        CalendarEntries calEntries = CalendarEntries.findCalendars(getContentResolver());
        mCalendarsPref.setEntries(calEntries.getEntries());
        mCalendarsPref.setEntryValues(calEntries.getEntryValues());

        /* Calendar Reminders Only */
        mCalendarRemindersOnlyPref = (CheckBoxPreference) prefSet
                .findPreference(LOCKSCREEN_CALENDAR_REMINDERS_ONLY);
        mCalendarRemindersOnlyPref.setChecked(Settings.System.getInt(getContentResolver(),
                LOCKSCREEN_CALENDAR_REMINDERS_ONLY, 0) == 1);

        /* Calendar Alarm Lookahead */
        mCalendarAlarmLookaheadPref = (ListPreference) prefSet
                .findPreference(LOCKSCREEN_CALENDAR_LOOKAHEAD);
        long calendarAlarmLookaheadPref = Settings.System.getLong(getContentResolver(),
                Settings.System.LOCKSCREEN_CALENDAR_LOOKAHEAD, 10800000);
        mCalendarAlarmLookaheadPref.setValue(String.valueOf(calendarAlarmLookaheadPref));
        mCalendarAlarmLookaheadPref.setOnPreferenceChangeListener(this);

        /* Calendar Alarm Show Location */
        mCalendarShowLocationPref = (ListPreference) prefSet
                .findPreference(LOCKSCREEN_CALENDAR_SHOW_LOCATION);
        int calendarShowLocationPref = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CALENDAR_SHOW_LOCATION, 0);
        mCalendarShowLocationPref.setValue(String.valueOf(calendarShowLocationPref));
        mCalendarShowLocationPref.setOnPreferenceChangeListener(this);

        /* Calendar Alarm Show Description */
        mCalendarShowDescriptionPref = (ListPreference) prefSet
                .findPreference(LOCKSCREEN_CALENDAR_SHOW_DESCRIPTION);
        int calendarShowDescriptionPref = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CALENDAR_SHOW_DESCRIPTION, 0);
        mCalendarShowDescriptionPref.setValue(String.valueOf(calendarShowDescriptionPref));
        mCalendarShowDescriptionPref.setOnPreferenceChangeListener(this);

        /* Show next Calendar Alarm */
        mCalendarAlarmPref = (CheckBoxPreference) prefSet.findPreference(LOCKSCREEN_CALENDAR_ALARM);
        mCalendarAlarmPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CALENDAR_ALARM, 0) == 1);

        /* Quick Unlock Screen Control */
        mQuickUnlockScreenPref = (CheckBoxPreference)
                prefSet.findPreference(LOCKSCREEN_QUICK_UNLOCK_CONTROL);
        mQuickUnlockScreenPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_QUICK_UNLOCK_CONTROL, 0) == 1);

        /* Lockscreen Custom App Toggle */
        mCustomAppTogglePref = (CheckBoxPreference) prefSet.findPreference(LOCKSCREEN_CUSTOM_APP_TOGGLE);
        mCustomAppTogglePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CUSTOM_APP_TOGGLE, 0) == 1);

        /* Lockscreen Style */
        mLockscreenStylePref = (ListPreference) prefSet.findPreference(LOCKSCREEN_STYLE_PREF);
        int lockscreenStyle = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_STYLE_PREF, 1);
        mLockscreenStylePref.setValue(String.valueOf(lockscreenStyle));
        mLockscreenStylePref.setOnPreferenceChangeListener(this);

        /* Trackball Wake */
        mTrackballWakePref = (CheckBoxPreference) prefSet.findPreference(TRACKBALL_WAKE_PREF);
        mTrackballWakePref.setChecked(Settings.System.getInt(getContentResolver(), 
                Settings.System.TRACKBALL_WAKE_SCREEN, 0) == 1);
				
        /* Volume Wake */
        mVolumeWakePref = (CheckBoxPreference) prefSet.findPreference(VOLUME_WAKE_PREF);
        mVolumeWakePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.VOLUME_WAKE_SCREEN, 0) == 1);

        /* Trackball Unlock */
        mTrackballUnlockPref = (CheckBoxPreference) prefSet.findPreference(TRACKBALL_UNLOCK_PREF);
        mTrackballUnlockPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.TRACKBALL_UNLOCK_SCREEN, 0) == 1);
        /* Menu Unlock */
        mMenuUnlockPref = (CheckBoxPreference) prefSet.findPreference(MENU_UNLOCK_PREF);
        mMenuUnlockPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.MENU_UNLOCK_SCREEN, 0) == 1);

        /* Disabling of unlock tab on lockscreen */
        mDisableUnlockTab = (CheckBoxPreference)
        prefSet.findPreference(LOCKSCREEN_DISABLE_UNLOCK_TAB);
        if (!doesUnlockAbilityExist()) {
            mDisableUnlockTab.setEnabled(false);
            mDisableUnlockTab.setChecked(false);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.LOCKSCREEN_GESTURES_DISABLE_UNLOCK, 0);
        } else {
            mDisableUnlockTab.setEnabled(true);
        }

	/* hw keyboard settings */
        mKeypadTypePref = (ListPreference) prefSet.findPreference(KEYPAD_TYPE_PREF);
        String keypadType = SystemProperties.get(KEYPAD_TYPE_PERSIST_PROP, KEYPAD_TYPE_DEFAULT);
        mKeypadTypePref.setValue(keypadType);
        mKeypadTypePref.setOnPreferenceChangeListener(this);

	/* dock observer switch */
        mDockObserverOffPref = (CheckBoxPreference) prefSet.findPreference(DOCK_OBSERVER_OFF_PREF);
        String dockObserverOff = SystemProperties.get(DOCK_OBSERVER_OFF_PERSIST_PROP, DOCK_OBSERVER_OFF_DEFAULT);
        mDockObserverOffPref.setChecked("1".equals(dockObserverOff));

	/* volume buttons swap in landscape */
        mVolBtnOrientationPref = (CheckBoxPreference) prefSet.findPreference(VOLBTN_ORIENT_PREF);
        String volBtnOrientation = SystemProperties.get(VOLBTN_ORIENT_PERSIST_PROP, VOLBTN_ORIENT_DEFAULT);
        mVolBtnOrientationPref.setChecked("1".equals(volBtnOrientation));

        /* 5-point multitouch */
	mQtouchNumPref = (ListPreference) prefSet.findPreference(QTOUCH_NUM_PREF);
        String qtouchNum = SystemProperties.get(QTOUCH_NUM_PERSIST_PROP, QTOUCH_NUM_DEFAULT);
        mQtouchNumPref.setValue(qtouchNum);
        mQtouchNumPref.setOnPreferenceChangeListener(this);

        PreferenceCategory buttonCategory = (PreferenceCategory)prefSet.findPreference(BUTTON_CATEGORY);

        if (!getResources().getBoolean(R.bool.has_trackball)) {
            buttonCategory.removePreference(mTrackballWakePref);
            buttonCategory.removePreference(mTrackballUnlockPref);
        }
        mUserDefinedKey1Pref = (Preference) prefSet.findPreference(USER_DEFINED_KEY1);
        mUserDefinedKey2Pref = (Preference) prefSet.findPreference(USER_DEFINED_KEY2);
        mUserDefinedKey3Pref = (Preference) prefSet.findPreference(USER_DEFINED_KEY3);

        mCustomAppActivityPref = (Preference) prefSet.findPreference(LOCKSCREEN_CUSTOM_APP_ACTIVITY);

        if (!"vision".equals(Build.DEVICE)) {
            buttonCategory.removePreference(mUserDefinedKey1Pref);
            buttonCategory.removePreference(mUserDefinedKey2Pref);
            buttonCategory.removePreference(mUserDefinedKey3Pref);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserDefinedKey1Pref.setSummary(Settings.System.getString(getContentResolver(),
                Settings.System.USER_DEFINED_KEY1_APP));
        mUserDefinedKey2Pref.setSummary(Settings.System.getString(getContentResolver(),
                Settings.System.USER_DEFINED_KEY2_APP));
        mUserDefinedKey3Pref.setSummary(Settings.System.getString(getContentResolver(),
                Settings.System.USER_DEFINED_KEY3_APP));
        mCustomAppActivityPref.setSummary(Settings.System.getString(getContentResolver(),
                Settings.System.LOCKSCREEN_CUSTOM_APP_ACTIVITY));
        if (!doesUnlockAbilityExist()) {
            mDisableUnlockTab.setEnabled(false);
            mDisableUnlockTab.setChecked(false);
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.LOCKSCREEN_GESTURES_DISABLE_UNLOCK, 0);
        } else {
            mDisableUnlockTab.setEnabled(true);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mMusicControlPref) {
            value = mMusicControlPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_MUSIC_CONTROLS, value ? 1 : 0);
            return true;
        } else if (preference == mAlwaysMusicControlPref) {
            value = mAlwaysMusicControlPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_ALWAYS_MUSIC_CONTROLS, value ? 1 : 0);
            return true;
        } else if (preference == mAlwaysBatteryPref) {
            value = mAlwaysBatteryPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_ALWAYS_BATTERY, value ? 1 : 0);
            return true;
        } else if (preference == mCalendarAlarmPref) {
            value = mCalendarAlarmPref.isChecked();
            Settings.System.putInt(getContentResolver(), 
                    Settings.System.LOCKSCREEN_CALENDAR_ALARM, value ? 1 : 0);
            return true;
        } else if (preference == mCalendarRemindersOnlyPref) {
            value = mCalendarRemindersOnlyPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CALENDAR_REMINDERS_ONLY, value ? 1 : 0);
            return true;
        } else if (preference == mQuickUnlockScreenPref) {
            value = mQuickUnlockScreenPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_QUICK_UNLOCK_CONTROL, value ? 1 : 0);
            return true;
        } else if (preference == mCustomAppTogglePref) {
            value = mCustomAppTogglePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CUSTOM_APP_TOGGLE, value ? 1 : 0);
            return true;
        } else if (preference == mTrackballWakePref) {
            value = mTrackballWakePref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.TRACKBALL_WAKE_SCREEN, value ? 1 : 0);
            return true;
		} else if (preference == mVolumeWakePref) {
            value = mVolumeWakePref.isChecked();
            Settings.System.putInt(getContentResolver(),
			        Settings.System.VOLUME_WAKE_SCREEN, value ? 1 : 0);
            return true;
        } else if (preference == mTrackballUnlockPref) {
            value = mTrackballUnlockPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.TRACKBALL_UNLOCK_SCREEN, value ? 1 : 0);
            return true;
        } else if (preference == mMenuUnlockPref) {
            value = mMenuUnlockPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.MENU_UNLOCK_SCREEN, value ? 1 : 0);
            return true;
        } else if (preference == mDisableUnlockTab) {
            value = mDisableUnlockTab.isChecked();
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.LOCKSCREEN_GESTURES_DISABLE_UNLOCK, value ? 1 : 0);
        } else if (preference == mUserDefinedKey1Pref) {
            pickShortcut(1);
            return true;
	} else if (preference == mDockObserverOffPref) {
            SystemProperties.set(DOCK_OBSERVER_OFF_PERSIST_PROP,
                    mDockObserverOffPref.isChecked() ? "1" : "0");
            return true;
	} else if (preference == mVolBtnOrientationPref) {
            SystemProperties.set(VOLBTN_ORIENT_PERSIST_PROP,
                    mVolBtnOrientationPref.isChecked() ? "1" : "0");
            return true;
        } else if (preference == mUserDefinedKey2Pref) {
            pickShortcut(2);
            return true;
        } else if (preference == mUserDefinedKey3Pref) {
            pickShortcut(3);
            return true;
        } else if (preference == mCustomAppActivityPref) {
            pickShortcut(4);
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLockscreenStylePref) {
            int lockscreenStyle = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.LOCKSCREEN_STYLE_PREF,
                    lockscreenStyle);
            return true;
        }
	else if (preference == mKeypadTypePref) {
            String keypadType = (String) newValue;
            SystemProperties.set(KEYPAD_TYPE_PERSIST_PROP, keypadType);
            return true;
        }
        else if (preference == mQtouchNumPref) {
            String qtouchNum = (String) newValue;
            SystemProperties.set(QTOUCH_NUM_PERSIST_PROP, qtouchNum);
            return true;
        }
        else if (preference == mCalendarShowLocationPref) {
            int calendarShowLocationPref = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CALENDAR_SHOW_LOCATION, calendarShowLocationPref);
            return true;
        }
        else if (preference == mCalendarShowDescriptionPref) {
            int calendarShowDescriptionPref = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CALENDAR_SHOW_DESCRIPTION, calendarShowDescriptionPref);
            return true;
        }
        else if (preference == mCalendarAlarmLookaheadPref) {
            long calendarAlarmLookaheadPref = Long.valueOf((String) newValue);
            Settings.System.putLong(getContentResolver(),
                    Settings.System.LOCKSCREEN_CALENDAR_LOOKAHEAD, calendarAlarmLookaheadPref);
            return true;
       }
       else if (preference == mCalendarsPref) {
            String calendarsPref = (String) newValue;
            Settings.System.putString(getContentResolver(), Settings.System.LOCKSCREEN_CALENDARS,
                    calendarsPref);
            return true;
       }
       return false;
    }

    private void pickShortcut(int keyNumber) {
        mKeyNumber = keyNumber;
        Bundle bundle = new Bundle();
        ArrayList<String> shortcutNames = new ArrayList<String>();
        shortcutNames.add(getString(R.string.group_applications));
        bundle.putStringArrayList(Intent.EXTRA_SHORTCUT_NAME, shortcutNames);
        ArrayList<ShortcutIconResource> shortcutIcons = new ArrayList<ShortcutIconResource>();
        shortcutIcons.add(ShortcutIconResource.fromContext(this, R.drawable.ic_launcher_application));
        bundle.putParcelableArrayList(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIcons);
        Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
        pickIntent.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
        pickIntent.putExtra(Intent.EXTRA_TITLE, getText(R.string.select_custom_app_title));
        pickIntent.putExtras(bundle);
        startActivityForResult(pickIntent, REQUEST_PICK_SHORTCUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_APPLICATION:
                    completeSetCustomApp(data);
                    break;
                case REQUEST_CREATE_SHORTCUT:
                    completeSetCustomShortcut(data);
                    break;
                case REQUEST_PICK_SHORTCUT:
                    processShortcut(data, REQUEST_PICK_APPLICATION, REQUEST_CREATE_SHORTCUT);
                    break;
            }
        }
    }

    void processShortcut(Intent intent, int requestCodeApplication, int requestCodeShortcut) {
        // Handle case where user selected "Applications"
        String applicationName = getResources().getString(R.string.group_applications);
        String shortcutName = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        if (applicationName != null && applicationName.equals(shortcutName)) {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
            pickIntent.putExtra(Intent.EXTRA_INTENT, mainIntent);
            startActivityForResult(pickIntent, requestCodeApplication);
        } else {
            startActivityForResult(intent, requestCodeShortcut);
        }
    }

    void completeSetCustomShortcut(Intent data) {
        Intent intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        int keyNumber = mKeyNumber;
        if (keyNumber == 1){
            if (Settings.System.putString(getContentResolver(), Settings.System.USER_DEFINED_KEY1_APP, intent.toUri(0))) {
                mUserDefinedKey1Pref.setSummary(intent.toUri(0));
            }
        } else if (keyNumber == 2){
            if (Settings.System.putString(getContentResolver(), Settings.System.USER_DEFINED_KEY2_APP, intent.toUri(0))) {
                mUserDefinedKey2Pref.setSummary(intent.toUri(0));
            }
        } else if (keyNumber == 3){
            if (Settings.System.putString(getContentResolver(), Settings.System.USER_DEFINED_KEY3_APP, intent.toUri(0))) {
                mUserDefinedKey3Pref.setSummary(intent.toUri(0));
            }
        } else if (keyNumber == 4){
            if (Settings.System.putString(getContentResolver(), Settings.System.LOCKSCREEN_CUSTOM_APP_ACTIVITY, intent.toUri(0))) {
                mCustomAppActivityPref.setSummary(intent.toUri(0));
            }
        }
    }

    void completeSetCustomApp(Intent data) {
        int keyNumber = mKeyNumber;
        if (keyNumber == 1){
            if (Settings.System.putString(getContentResolver(), Settings.System.USER_DEFINED_KEY1_APP, data.toUri(0))) {
                mUserDefinedKey1Pref.setSummary(data.toUri(0));
            }
        } else if (keyNumber == 2){
            if (Settings.System.putString(getContentResolver(), Settings.System.USER_DEFINED_KEY2_APP, data.toUri(0))) {
                mUserDefinedKey2Pref.setSummary(data.toUri(0));
            }
        } else if (keyNumber == 3){
            if (Settings.System.putString(getContentResolver(), Settings.System.USER_DEFINED_KEY3_APP, data.toUri(0))) {
                mUserDefinedKey3Pref.setSummary(data.toUri(0));
            }
        } else if (keyNumber == 4){
            if (Settings.System.putString(getContentResolver(), Settings.System.LOCKSCREEN_CUSTOM_APP_ACTIVITY, data.toUri(0))) {
                mCustomAppActivityPref.setSummary(data.toUri(0));
            }
        }
    }

    private boolean doesUnlockAbilityExist() {
        final File mStoreFile = new File(Environment.getDataDirectory(), "/misc/lockscreen_gestures");
        boolean GestureCanUnlock = false;
        boolean trackCanUnlock = Settings.System.getInt(getContentResolver(),
                Settings.System.TRACKBALL_UNLOCK_SCREEN, 0) == 1;
        boolean menuCanUnlock = Settings.System.getInt(getContentResolver(),
                Settings.System.MENU_UNLOCK_SCREEN, 0) == 1;
        GestureLibrary gl = GestureLibraries.fromFile(mStoreFile);
        if (gl.load()) {
            for (String name : gl.getGestureEntries()) {
                if ("UNLOCK___UNLOCK".equals(name)) {
                    GestureCanUnlock = true;
                    break;
                }
            }
        }
        if (GestureCanUnlock || trackCanUnlock || menuCanUnlock) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDefaultLockscreenStyle() {
        int lockscreenStyle = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_STYLE_PREF, 1);
        if (lockscreenStyle == 1) {
            return true;
        } else {
            return false;
        }
    }
    private static class CalendarEntries {

        private final static String CALENDARS_WHERE = Calendar.CalendarsColumns.SELECTED + "=1 AND "
                + Calendar.CalendarsColumns.ACCESS_LEVEL + ">=200";

        private final CharSequence[] mEntries;

        private final CharSequence[] mEntryValues;

        static CalendarEntries findCalendars(ContentResolver contentResolver) {
            List<CharSequence> entries = new ArrayList<CharSequence>();
            List<CharSequence> entryValues = new ArrayList<CharSequence>();
            Cursor cursor = null;
            try {
                cursor = Calendar.Calendars.query(contentResolver, new String[] {
                    Calendar.Calendars.DISPLAY_NAME, BaseColumns._ID
                }, CALENDARS_WHERE, null);
                while (cursor.moveToNext()) {
                    String entry = cursor.getString(0);
                    entries.add(entry);
                    String entryValue = cursor.getString(1);
                    entryValues.add(entryValue);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return new CalendarEntries(entries, entryValues);
        }

        private CalendarEntries(List<CharSequence> mEntries, List<CharSequence> mEntryValues) {
            this.mEntries = mEntries.toArray(new CharSequence[mEntries.size()]);
            this.mEntryValues = mEntryValues.toArray(new CharSequence[mEntryValues.size()]);
        }

        CharSequence[] getEntries() {
            return mEntries;
        }

        CharSequence[] getEntryValues() {
            return mEntryValues;
        }
    }
}
