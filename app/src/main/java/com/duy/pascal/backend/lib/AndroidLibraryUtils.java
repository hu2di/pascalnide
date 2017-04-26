/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duy.pascal.backend.lib;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.duy.pascal.backend.lib.android.AndroidBatteryLib;
import com.duy.pascal.backend.lib.android.AndroidClipboard;
import com.duy.pascal.backend.lib.android.AndroidNotifyLib;
import com.duy.pascal.backend.lib.android.AndroidTextToSpeechLib;
import com.duy.pascal.backend.lib.android.AndroidVibrateLib;
import com.duy.pascal.backend.lib.android.BaseAndroidLibrary;
import com.duy.pascal.backend.lib.android.temp.AndroidApplicationManagerLib;
import com.duy.pascal.backend.lib.android.temp.AndroidBluetoothLib;
import com.duy.pascal.backend.lib.android.temp.AndroidMediaPlayerLib;
import com.duy.pascal.backend.lib.android.temp.AndroidSensorLib;
import com.duy.pascal.backend.lib.android.temp.AndroidSettingLib;
import com.duy.pascal.backend.lib.android.temp.AndroidToneGeneratorLib;
import com.duy.pascal.backend.lib.android.temp.AndroidUtilsLib;
import com.duy.pascal.backend.lib.android.temp.AndroidWifiLib;
import com.google.common.collect.Maps;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.facade.ActivityResultFacade;
import com.googlecode.sl4a.facade.CameraFacade;
import com.googlecode.sl4a.facade.CommonIntentsFacade;
import com.googlecode.sl4a.facade.ContactsFacade;
import com.googlecode.sl4a.facade.EventFacade;
import com.googlecode.sl4a.facade.LocationFacade;
import com.googlecode.sl4a.facade.MediaRecorderFacade;
import com.googlecode.sl4a.facade.PhoneFacade;
import com.googlecode.sl4a.facade.PreferencesFacade;
import com.googlecode.sl4a.facade.SignalStrengthFacade;
import com.googlecode.sl4a.facade.SmsFacade;
import com.googlecode.sl4a.facade.SpeechRecognitionFacade;
import com.googlecode.sl4a.facade.WakeLockFacade;
import com.googlecode.sl4a.facade.WebCamFacade;
import com.googlecode.sl4a.facade.ui.UiFacade;
import com.googlecode.sl4a.rpc.MethodDescriptor;
import com.googlecode.sl4a.rpc.RpcDeprecated;
import com.googlecode.sl4a.rpc.RpcStartEvent;
import com.googlecode.sl4a.rpc.RpcStopEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Encapsulates the list of supported facades and their construction.
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 * @author Igor Karp (igor.v.karp@gmail.com)
 */
public class AndroidLibraryUtils {
    private final static Set<Class<? extends BaseAndroidLibrary>> sFacadeClassList;
    private final static SortedMap<String, MethodDescriptor> sRpcs =
            new TreeMap<>();

    static {
        sFacadeClassList = new HashSet<>();
        sFacadeClassList.add(AndroidUtilsLib.class);
        sFacadeClassList.add(AndroidApplicationManagerLib.class);
        sFacadeClassList.add(CameraFacade.class);
        sFacadeClassList.add(CommonIntentsFacade.class);
        sFacadeClassList.add(ContactsFacade.class);
        sFacadeClassList.add(EventFacade.class);
        sFacadeClassList.add(LocationFacade.class);
        sFacadeClassList.add(PhoneFacade.class);
        sFacadeClassList.add(MediaRecorderFacade.class);
        sFacadeClassList.add(AndroidSensorLib.class);
        sFacadeClassList.add(AndroidSettingLib.class);
        sFacadeClassList.add(SmsFacade.class);
        sFacadeClassList.add(SpeechRecognitionFacade.class);
        sFacadeClassList.add(AndroidToneGeneratorLib.class);
        sFacadeClassList.add(WakeLockFacade.class);
        sFacadeClassList.add(AndroidWifiLib.class);
        sFacadeClassList.add(UiFacade.class);
        sFacadeClassList.add(ActivityResultFacade.class);
        sFacadeClassList.add(AndroidMediaPlayerLib.class);
        sFacadeClassList.add(PreferencesFacade.class);
        sFacadeClassList.add(AndroidTextToSpeechLib.class);
        sFacadeClassList.add(AndroidBluetoothLib.class);
        sFacadeClassList.add(SignalStrengthFacade.class);
        sFacadeClassList.add(AndroidBatteryLib.class);
        sFacadeClassList.add(WebCamFacade.class);
        sFacadeClassList.add(AndroidVibrateLib.class);
        sFacadeClassList.add(AndroidClipboard.class);
        sFacadeClassList.add(AndroidNotifyLib.class);
        for (Class<? extends BaseAndroidLibrary> recieverClass : sFacadeClassList) {
            for (MethodDescriptor rpcMethod : MethodDescriptor.collectFrom(recieverClass)) {
                sRpcs.put(rpcMethod.getName(), rpcMethod);
            }
        }
    }

    private AndroidLibraryUtils() {
        // Utility class.
    }

    public static int getSdkLevel() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Returns a list of {@link MethodDescriptor} objects for all facades.
     */
    public static List<MethodDescriptor> collectMethodDescriptors() {
        return new ArrayList<>(sRpcs.values());
    }

    /**
     * Returns a list of not deprecated {@link MethodDescriptor} objects for facades supported by the
     * current SDK version.
     */
    public static List<MethodDescriptor> collectSupportedMethodDescriptors() {
        List<MethodDescriptor> list = new ArrayList<>();
        for (MethodDescriptor descriptor : sRpcs.values()) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(RpcDeprecated.class)) {
                continue;
            }
            list.add(descriptor);
        }
        return list;
    }

    public static Map<String, MethodDescriptor> collectStartEventMethodDescriptors() {
        Map<String, MethodDescriptor> map = Maps.newHashMap();
        for (MethodDescriptor descriptor : sRpcs.values()) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(RpcStartEvent.class)) {
                String eventName = method.getAnnotation(RpcStartEvent.class).value();
                if (map.containsKey(eventName)) {
                    throw new RuntimeException("Duplicate start event method descriptor found.");
                }
                map.put(eventName, descriptor);
            }
        }
        return map;
    }

    public static Map<String, MethodDescriptor> collectStopEventMethodDescriptors() {
        Map<String, MethodDescriptor> map = Maps.newHashMap();
        for (MethodDescriptor descriptor : sRpcs.values()) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(RpcStopEvent.class)) {
                String eventName = method.getAnnotation(RpcStopEvent.class).value();
                if (map.containsKey(eventName)) {
                    throw new RuntimeException("Duplicate stop event method descriptor found.");
                }
                map.put(eventName, descriptor);
            }
        }
        return map;
    }

    /**
     * Returns a method by name.
     */
    public static MethodDescriptor getMethodDescriptor(String name) {
        return sRpcs.get(name);
    }

    public static Collection<Class<? extends BaseAndroidLibrary>> getFacadeClasses() {
        return sFacadeClassList;
    }


    // TODO(damonkohler): Pull this out into proper argument deserialization and support
    // complex/nested types being passed in.
    public static void putExtrasFromJsonObject(JSONObject extras, Intent intent) throws JSONException {
        JSONArray names = extras.names();
        for (int i = 0; i < names.length(); i++) {
            String name = names.getString(i);
            Object data = extras.get(name);
            if (data == null) {
                continue;
            }
            if (data instanceof Integer) {
                intent.putExtra(name, (Integer) data);
            }
            if (data instanceof Float) {
                intent.putExtra(name, (Float) data);
            }
            if (data instanceof Double) {
                intent.putExtra(name, (Double) data);
            }
            if (data instanceof Long) {
                intent.putExtra(name, (Long) data);
            }
            if (data instanceof String) {
                intent.putExtra(name, (String) data);
            }
            if (data instanceof Boolean) {
                intent.putExtra(name, (Boolean) data);
            }
            // Nested JSONObject
            if (data instanceof JSONObject) {
                Bundle nestedBundle = new Bundle();
                intent.putExtra(name, nestedBundle);
                putNestedJSONObject((JSONObject) data, nestedBundle);
            }
            // Nested JSONArray. Doesn't support mixed types in single array
            if (data instanceof JSONArray) {
                // Empty array. No way to tell what type of data to pass on, so skipping
                if (((JSONArray) data).length() == 0) {
                    Log.e("Empty array not supported in JSONObject, skipping");
                    continue;
                }
                // Integer
                if (((JSONArray) data).get(0) instanceof Integer) {
                    Integer[] integerArrayData = new Integer[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        integerArrayData[j] = ((JSONArray) data).getInt(j);
                    }
                    intent.putExtra(name, integerArrayData);
                }
                // Double
                if (((JSONArray) data).get(0) instanceof Double) {
                    Double[] doubleArrayData = new Double[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        doubleArrayData[j] = ((JSONArray) data).getDouble(j);
                    }
                    intent.putExtra(name, doubleArrayData);
                }
                // Long
                if (((JSONArray) data).get(0) instanceof Long) {
                    Long[] longArrayData = new Long[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        longArrayData[j] = ((JSONArray) data).getLong(j);
                    }
                    intent.putExtra(name, longArrayData);
                }
                // String
                if (((JSONArray) data).get(0) instanceof String) {
                    String[] stringArrayData = new String[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        stringArrayData[j] = ((JSONArray) data).getString(j);
                    }
                    intent.putExtra(name, stringArrayData);
                }
                // Boolean
                if (((JSONArray) data).get(0) instanceof Boolean) {
                    Boolean[] booleanArrayData = new Boolean[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        booleanArrayData[j] = ((JSONArray) data).getBoolean(j);
                    }
                    intent.putExtra(name, booleanArrayData);
                }
            }
        }
    }

    // Contributed by Emmanuel T
    // Nested Array handling contributed by Sergey Zelenev
    public static void putNestedJSONObject(JSONObject jsonObject, Bundle bundle)
            throws JSONException {
        JSONArray names = jsonObject.names();
        for (int i = 0; i < names.length(); i++) {
            String name = names.getString(i);
            Object data = jsonObject.get(name);
            if (data == null) {
                continue;
            }
            if (data instanceof Integer) {
                bundle.putInt(name, (Integer) data);
            }
            if (data instanceof Float) {
                bundle.putFloat(name, (Float) data);
            }
            if (data instanceof Double) {
                bundle.putDouble(name, (Double) data);
            }
            if (data instanceof Long) {
                bundle.putLong(name, (Long) data);
            }
            if (data instanceof String) {
                bundle.putString(name, (String) data);
            }
            if (data instanceof Boolean) {
                bundle.putBoolean(name, (Boolean) data);
            }
            // Nested JSONObject
            if (data instanceof JSONObject) {
                Bundle nestedBundle = new Bundle();
                bundle.putBundle(name, nestedBundle);
                putNestedJSONObject((JSONObject) data, nestedBundle);
            }
            // Nested JSONArray. Doesn't support mixed types in single array
            if (data instanceof JSONArray) {
                // Empty array. No way to tell what type of data to pass on, so skipping
                if (((JSONArray) data).length() == 0) {
                    Log.e("Empty array not supported in nested JSONObject, skipping");
                    continue;
                }
                // Integer
                if (((JSONArray) data).get(0) instanceof Integer) {
                    int[] integerArrayData = new int[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        integerArrayData[j] = ((JSONArray) data).getInt(j);
                    }
                    bundle.putIntArray(name, integerArrayData);
                }
                // Double
                if (((JSONArray) data).get(0) instanceof Double) {
                    double[] doubleArrayData = new double[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        doubleArrayData[j] = ((JSONArray) data).getDouble(j);
                    }
                    bundle.putDoubleArray(name, doubleArrayData);
                }
                // Long
                if (((JSONArray) data).get(0) instanceof Long) {
                    long[] longArrayData = new long[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        longArrayData[j] = ((JSONArray) data).getLong(j);
                    }
                    bundle.putLongArray(name, longArrayData);
                }
                // String
                if (((JSONArray) data).get(0) instanceof String) {
                    String[] stringArrayData = new String[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        stringArrayData[j] = ((JSONArray) data).getString(j);
                    }
                    bundle.putStringArray(name, stringArrayData);
                }
                // Boolean
                if (((JSONArray) data).get(0) instanceof Boolean) {
                    boolean[] booleanArrayData = new boolean[((JSONArray) data).length()];
                    for (int j = 0; j < ((JSONArray) data).length(); ++j) {
                        booleanArrayData[j] = ((JSONArray) data).getBoolean(j);
                    }
                    bundle.putBooleanArray(name, booleanArrayData);
                }
            }
        }
    }

}