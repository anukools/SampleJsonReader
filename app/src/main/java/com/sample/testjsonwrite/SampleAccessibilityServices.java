package com.sample.testjsonwrite;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;

/**
 * Created by Anukool Srivastav on 6/15/2017.
 */

public class SampleAccessibilityServices extends AccessibilityService {

    String FLIPKART_PACKAGE = "com.flipkart.android";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {

            // ignore notification events
            if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                    || event.getEventType() == AccessibilityEvent.TYPE_ANNOUNCEMENT) return;

            String packageName = (String) event.getPackageName();

            if (FLIPKART_PACKAGE.equalsIgnoreCase(packageName)) {
                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                    printLog(event);
                    if (event.getSource() != null && event.getSource().getContentDescription() != null) {
                        addEntryToJsonFile(getApplicationContext(), new Random().nextInt(100) + 1 + "",
                                event.getContentDescription().toString(), getEventType(event));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {

        try {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();

            info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            info.notificationTimeout = 0;
            info.flags |= AccessibilityServiceInfo.DEFAULT;
            info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
            info.flags |= AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

            setServiceInfo(info);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEntryToJsonFile(Context ctx, String id, String name, String eventType) {
        String fileName = "my_json_file.json";
        // parse existing/init new JSON
        File jsonFile = new File(ctx.getDir("my_data_dir", 0), fileName);
        String previousJson = null;
        if (jsonFile.exists()) {
            try {
                previousJson = Utils.readFromFile(jsonFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            previousJson = "{}";
        }

        // create new "complex" object
        JSONObject mO = null;
        JSONObject jO = new JSONObject();

        try {
            mO = new JSONObject(previousJson);
            jO.put("completed", true);
            jO.put("name", name);
            jO.put("eventType", eventType);
            mO.put(id, jO); //thanks "retired" answer
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // generate string from the object
        String jsonString = null;
        try {
            jsonString = mO.toString(4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // write back JSON file
        Utils.writeToFile(ctx, fileName, jsonString);

    }


    public static void printLog(AccessibilityEvent event) {
        if (event == null) return;

        try {

            String className = "NA";
            AccessibilityNodeInfo nodeInfo;
            String nodeClass = "NA";
            String classContent = "NA";
            String classText = "NA";
            String nodeContent = "NA";
            String nodeId = "NA";
            String nodeText = "NA";
            String nodeFocus = "NA";

            if (event.getClassName() != null) className = event.getClassName().toString();

            if (event.getContentDescription() != null)
                classContent = event.getContentDescription().toString();

            if (event.getText() != null) classText = event.getText().toString();

            if (event.getSource() != null) {
                nodeInfo = event.getSource();

                if (nodeInfo.getClassName() != null) nodeClass = nodeInfo.getClassName().toString();

                if (nodeInfo.getContentDescription() != null)
                    nodeContent = nodeInfo.getContentDescription().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (nodeInfo.getViewIdResourceName() != null)
                        nodeId = nodeInfo.getViewIdResourceName();
                }

                if (nodeInfo.getText() != null) nodeText = nodeInfo.getText().toString();

                nodeFocus = String.valueOf(nodeInfo.isFocused());
            }
            System.out.println("event_info: " + event.getPackageName() + className + "  " + classContent + "  " + classText + "  " +
                    nodeClass + "  " + nodeId + "  " + nodeContent + "  " + nodeText + " "
                    + getEventType(event) + " nodeFocus: " + nodeFocus);

        } catch (Exception e) {
        }
    }

    public static String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE_WINDOW_CONTENT_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                return "TYPE_VIEW_SCROLLED";
            case AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED:
                return "CONTENT_CHANGE_TYPE_UNDEFINED";
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                return "TYPE_WINDOWS_CHANGED";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                return "TYPE_TOUCH_INTERACTION_END";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                return "TYPE_TOUCH_INTERACTION_START";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                return "TYPE_GESTURE_DETECTION_END";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                return "TYPE_GESTURE_DETECTION_START";
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                return "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                return "TYPE_ANNOUNCEMENT";
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                return "TYPE_VIEW_HOVER_EXIT";
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                return "TYPE_VIEW_HOVER_ENTER";
        }
        return "default";
    }
}
