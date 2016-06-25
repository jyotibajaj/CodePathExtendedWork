package com.letsdecode.mytodo.utils;

import com.letsdecode.mytodo.models.ListViewItem;
import com.letsdecode.mytodo.models.TaskDetail;
import com.letsdecode.mytodo.models.TypeClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class ItemsBucketing {
    public static final String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    enum Keys {
        OVERDUE("Overdue"),
        TODAY("Today"),
        TOMORROW("Tomorrow"),
        NEXT_WEEK("Next Week"),
        IN_2_DAYS("In 2 days"),
        IN_3_DAYS("In 3 days"),
        IN_4_DAYS("In 4 days"),
        IN_5_DAYS("In 5 days"),
        IN_6_DAYS("In 6 days"),
        IN_7_DAYS("In 7 days"),
        THIS_MONTH("This month"),
        NEXT_MONTH("Next month"),
        JANUARY(months[0]),
        FEBRUARY(months[1]),
        MARCH(months[2]),
        APRIL(months[3]),
        MAY(months[4]),
        JUNE(months[5]),
        JULY(months[6]),
        AUGUST(months[7]),
        SEPTEMBER(months[8]),
        OCTOBER(months[9]),
        NOVEMBER(months[10]),
        DECEMBER(months[11]),
        THIS_YEAR("This year"),
        NEXT_YEAR("Next year"),
        SOME_DAY("Some day"),;
        String key;

        public String getDisplayValue() {
            return key;
        }

        Keys(String key) {
            this.key = key;
        }
    }

    public ArrayList<ListViewItem> createBuckets(ArrayList<TaskDetail> items) {
        ArrayList<ListViewItem> listViewItem = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        HashMap<Keys, ArrayList<TaskDetail>> map = new HashMap<>();
        Keys[] values = Keys.values();
        for (TaskDetail i : items) {
            Keys key = Keys.SOME_DAY;
            Calendar taskTime = Calendar.getInstance();
            Date itemDate = new Date(Long.valueOf(i.getTime()));
            taskTime.setTime(itemDate);
            if (Long.valueOf(i.getTime()) < currentTime) {
                //Task time is  less than current time;
                key = Keys.OVERDUE;
            } else if (taskTime.get(Calendar.YEAR) - now.get(Calendar.YEAR) > 1) {
                //Task time is more 1 year ahead of now
                key = Keys.SOME_DAY;
            } else if (taskTime.get(Calendar.YEAR) - now.get(Calendar.YEAR) == 1) {
                //Task time year is next year
                key = Keys.NEXT_YEAR;
            } else if (taskTime.get(Calendar.YEAR) - now.get(Calendar.YEAR) == 0) {
                //Same Year; Lets check which month, week, or day
                key = Keys.THIS_YEAR;
                if (taskTime.get(Calendar.MONTH) - now.get(Calendar.MONTH) > 1) {
                    //Any month after next month
                    key = values[Keys.JANUARY.ordinal() + taskTime.get(Calendar.MONTH)];
                } else if (taskTime.get(Calendar.MONTH)
                        - now.get(Calendar.MONTH) == 1) {
                    //Next Month
                    key = Keys.NEXT_MONTH;
                } else if (taskTime.get(Calendar.MONTH)
                        - now.get(Calendar.MONTH) == 0) {
                    //This month
                    if (taskTime.get(Calendar.WEEK_OF_MONTH)
                            - now.get(Calendar.WEEK_OF_MONTH) > 1) {
                        //Next to next week
                        key = Keys.THIS_MONTH;
                    } else if (taskTime.get(Calendar.WEEK_OF_MONTH)
                            - now.get(Calendar.WEEK_OF_MONTH) == 1) {
                        //Next week ;
                        key = Keys.NEXT_WEEK;
                    } else if (taskTime.get(Calendar.WEEK_OF_MONTH)
                            - now.get(Calendar.WEEK_OF_MONTH) == 0) {
                        if (taskTime.get(Calendar.DAY_OF_WEEK)
                                - now.get(Calendar.DAY_OF_WEEK) > 1) {
                            key = values[taskTime.get(Calendar.DAY_OF_WEEK) - now
                                    .get(Calendar.DAY_OF_WEEK)];
                        } else if (taskTime.get(Calendar.DAY_OF_WEEK)
                                - now.get(Calendar.DAY_OF_WEEK) == 1) {
                            key = Keys.TOMORROW;
                        } else if (taskTime.get(Calendar.DAY_OF_WEEK)
                                - now.get(Calendar.DAY_OF_WEEK) == 0) {
                            key = Keys.TODAY;
                            if (taskTime.getTime().getTime() < currentTime) {
                                key = Keys.OVERDUE;
                            }
                        }
                    }
                } else {
                    key = Keys.OVERDUE;
                }
            } else if (Long.valueOf(i.getTime()) < currentTime) {
                key = Keys.OVERDUE;
            }

            put(map, key, i);
        }

        Set<Keys> set = map.keySet();
        ArrayList<Keys> keyList = new ArrayList<>();
        keyList.addAll(set);
        Collections.sort(keyList);

        for (Keys k : values) {
            ArrayList<TaskDetail> list = map.get(k);
            if (list != null) {
                listViewItem.add(new ListViewItem(TypeClass.TIME_VIEW, k.getDisplayValue()));
                Collections.sort(list, comparator);
                for (TaskDetail item : list) {
                    listViewItem.add(new ListViewItem(TypeClass.ITEM_DETAIL_VIEW, item));
                }
            }
        }

        return listViewItem;
    }

    private static int getPriority(String priority) {
        if (priority.equalsIgnoreCase("urgent")) {
            return 0;
        } else if (priority.equalsIgnoreCase("high")) {
            return 1;
        } else if (priority.equalsIgnoreCase("medium")) {
            return 2;
        } else {
            return 3;
        }
    }

    private static Comparator<TaskDetail> comparator = new Comparator<TaskDetail>() {
        @Override
        public int compare(TaskDetail lhs, TaskDetail rhs) {
            return getPriority(lhs.getPriority()) - getPriority(rhs.getPriority());
        }
    };

    private void put(HashMap<Keys, ArrayList<TaskDetail>> map, Keys key, TaskDetail i) {
        ArrayList<TaskDetail> list = map.get(key);
        if (list == null) {
            list = new ArrayList<>();
            map.put(key, list);
        }
        list.add(i);
    }
}
