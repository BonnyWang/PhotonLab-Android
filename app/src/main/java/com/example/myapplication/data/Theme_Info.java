package com.example.myapplication.data;

import com.example.myapplication.theme_Class;
import com.example.myapplication.theme_content_class;

import java.util.ArrayList;
import java.util.Arrays;

public class Theme_Info {
    private static String[] themeNames={"Spring","Fizzy Peach","Sky","Shit","Spring","Fizzy Peach","Sky","Shit"};
    private static int[][] colorCode= {{0xff009e00, 0xfffcee21},{0xfff24645, 0xffebc08d},{0xff00b7ff, 0xff00ffee},{0xff7c4d16,0x301c07},
            {0xff009e00, 0xfffcee21},{0xfff24645, 0xffebc08d},{0xff00b7ff, 0xff00ffee},{0xff7c4d16,0x301c07}};
    private static String[] subtitles={"Creator","Mood","Hover Enabled"};
    private static String[] contents={"PhotonLab","Home Happy Sunset","No"};
    // need to change logic - oh mamamia 一一对应 飞翔的荷兰人号


    //debug
    public ArrayList<theme_Class> classlist(){
        ArrayList<theme_Class> list = new ArrayList<>();
        if (themeNames.length==colorCode.length)
            for ( int i=0;i<themeNames.length;i++){
                theme_Class theme = new theme_Class(themeNames[i],colorCode[i]);
                list.add(theme);
            }
        return list;
    }

    public void addTheme(String name, int[] colorcode){
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(themeNames));
        myList.add(name);
        themeNames = myList.toArray(themeNames);

        int[][] myList2=new int[colorCode.length+1][];
        for(int i=0;i < colorCode.length;i++)
            myList2[i] = colorCode[i];
        myList2[colorCode.length+1]=colorcode;
        colorCode=myList2;

    }
    public ArrayList<theme_content_class> itemlist(){
        ArrayList<theme_content_class> items = new ArrayList<>();
        if (subtitles.length==contents.length){
            for (int i=0; i<subtitles.length;i++){
                theme_content_class item = new theme_content_class(subtitles[i],contents[i]);
                items.add(item);
            }
        }
        return items;
    }

}
