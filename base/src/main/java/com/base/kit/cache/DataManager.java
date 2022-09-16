package com.base.kit.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.kit.wapper.IResultBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单的数据共享管理类
 */
public class DataManager {
    private final Map<String, Object> _dataMap;
    private final Map<String, List<IResultBack>> _observerMap;

    private DataManager() {
        _dataMap = new HashMap<>(4);
        _observerMap = new HashMap<>(4);
    }

    private static volatile DataManager _singleton;

    public static DataManager get() {
        if (_singleton == null) {
            synchronized (DataManager.class) {
                if (_singleton == null) {
                    _singleton = new DataManager();
                }
            }
        }
        return _singleton;
    }


    public <T> T update(@NonNull String key, @Nullable T data) {
        Object old = _dataMap.remove(key);
        if (null != data) {
            _dataMap.put(key, data);
        }
        dispatchObserve(key, data);
        return null == old ? null : (T) old;
    }

    public <T> void addObserver(@NonNull String key, @Nullable IResultBack<T> observer) {
        // 存储
        List<IResultBack> observers = _observerMap.get(key);
        if (null == observers) {
            observers = new ArrayList<>(4);
        }
        observers.add(observer);
        // 首次尝试获取
        Object data = _dataMap.get(key);
        dispatchObserve(key, data);
    }

    public void remove(String key, @Nullable IResultBack observer) {
        List<IResultBack> observers = _observerMap.get(key);
        if (null != observers) {
            observers.remove(observer);
        }
    }

    public void clear() {
        _dataMap.clear();
        for (List<IResultBack> values : _observerMap.values()) {
            if (null != values) {
                values.clear();
            }
        }
    }

    <T> void dispatchObserve(String key, T data) {
        List<IResultBack> observers = _observerMap.get(key);
        int count = null == observers ? 0 : observers.size();
        for (int i = 0; i < count; i++) {
            IResultBack<T> observer = observers.get(i);
            observer.onResult(data);
        }
    }
}
