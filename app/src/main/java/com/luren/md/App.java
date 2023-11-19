package com.luren.md;

import android.app.Activity;
import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import java.util.Iterator;
import java.util.Stack;

@HiltAndroidApp
public class App extends Application {
  
  private final Stack<Activity> mActivityStacks = new Stack<>();
  private static App mApp;

  @Override
  public void onCreate() {
    super.onCreate();
    mApp = this;
  }

  public static App getApp() {
    return mApp;
  }

  /**
   * 获取 Activity 栈
   *
   * @return {@link Stack<Activity>}
   */
  public Stack<Activity> getActivityStacks() {
    return mActivityStacks;
  }

  /**
   * 添加 Activity
   *
   * @param activity {@link Activity}
   * @return {@link App}
   */
  public App addActivity(final Activity activity) {
    if (activity != null) {
      synchronized (mActivityStacks) {
        if (mActivityStacks.contains(activity)) {
          return this;
        }
        mActivityStacks.add(activity);
      }
    }
    return this;
  }

  /**
   * 移除 Activity
   *
   * @param activity {@link Activity}
   * @return {@link App}
   */
  public App removeActivity(final Activity activity) {
    if (activity != null) {
      synchronized (mActivityStacks) {
        int index = mActivityStacks.indexOf(activity);
        if (index == -1) {
          return this;
        }
        try {
          mActivityStacks.remove(index);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return this;
  }

  /**
   * 关闭指定 Activity
   *
   * @param activity {@link Activity}
   * @return {@link App}
   */
  public App finishActivity(final Activity activity) {
    // 先移除 Activity
    removeActivity(activity);
    // Activity 不为 null, 并且属于未销毁状态
    if (activity != null && !activity.isFinishing()) {
      activity.finish();
    }
    return this;
  }

  /** 结束所有 Activity */
  public void finishAllActivity() {
    synchronized (mActivityStacks) {
      // 保存新的堆栈, 防止出现同步问题
      Stack<Activity> stack = new Stack<>();
      stack.addAll(mActivityStacks);
      // 清空全部, 便于后续操作处理
      mActivityStacks.clear();
      // 进行遍历移除
      Iterator<Activity> iterator = stack.iterator();
      while (iterator.hasNext()) {
        Activity activity = iterator.next();
        if (activity != null && !activity.isFinishing()) {
          activity.finish();
          // 删除对应的 Item
          iterator.remove();
        }
      }
      // 移除数据, 并且清空内存
      stack.clear();
    }
  }

  // =

  /** 退出应用程序 */
  public void exitApplication() {
    try {
      finishAllActivity();
      // 退出 JVM (Java 虚拟机 ) 释放所占内存资源, 0 表示正常退出、非 0 的都为异常退出
      System.exit(0);
      // 从操作系统中结束掉当前程序的进程
      android.os.Process.killProcess(android.os.Process.myPid());
    } catch (Exception e) {
      // =
      System.exit(-1);
    }
  }
}
