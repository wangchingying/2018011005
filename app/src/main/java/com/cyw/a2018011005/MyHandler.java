package com.cyw.a2018011005;


import android.util.Log;
import android.widget.ListView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Student on 2018/1/10.
 */

public class MyHandler extends DefaultHandler {

    boolean isTitle = false;
    boolean isItem = false;
    boolean isLink = false;
    boolean isDesc = false;
    boolean isImg = false;
    StringBuilder linkSB = new StringBuilder();
//    public ArrayList<String> titles = new ArrayList<>();
//    public ArrayList<String> links = new ArrayList<>();
//    public ArrayList<String> imgs = new ArrayList<>();
//    public ArrayList<String> descriptions = new ArrayList<>();
    public ArrayList<Mobile01NewsItem> newsItems = new ArrayList<>();
    Mobile01NewsItem item;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //Log.d("NET", qName);
        switch(qName)
        {
            case "title":
                isTitle = true;
                break;
            case "item":
                isItem = true;
                item = new Mobile01NewsItem();
                break;
            case "link":
                isLink = true;
                break;
            case "description":
                isDesc = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch(qName)
        {
            case "title":
                isTitle = false;
                break;
            case "item":
                isItem = false;
                newsItems.add(item);//xml中item開頭item結束,資料都在中間, 所以在start時New, 在end時加入資料'
                break;
            case "link":
                isLink = false;
                if (isItem)
                {
                    item.link = linkSB.toString();
                    linkSB = new StringBuilder();
                }
                break;
            case "description":
                isDesc = false;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle && isItem)
        {
            Log.d("NET", new String(ch, start, length));
            item.title=new String(ch, start, length);
        }
        if (isLink && isItem)
        {
            linkSB.append(new String(ch, start, length));
            //Log.d("NET2", linkSB.toString());

        }
        if (isDesc)
        {
            Log.d("NET_desc", new String(ch, start, length));

        }


    }

}
