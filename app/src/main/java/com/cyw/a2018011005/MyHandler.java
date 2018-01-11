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
    public ArrayList<String> titles = new ArrayList<>();
    public ArrayList<String> links = new ArrayList<>();
    public ArrayList<String> imgs = new ArrayList<>();
    public ArrayList<String> descriptions = new ArrayList<>();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //Log.d("NET", qName);
        if (qName.equals("item") )
        {
            isItem = true;
        }
        if (qName.equals("title") )
        {
            isTitle = true;
        }
        if (qName.equals("link") )
        {
            isLink = true;
        }
        if (qName.equals("description") )
        {
            isDesc = true;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("item") )
        {
            isItem = false;
        }
        if (qName.equals("title"))
        {
            isTitle = false;
        }
        if (qName.equals("link") )
        {
            isLink = false;
            //因為抓下來分三段,所以在characters那邊先用StringBuilder接好
            if (isItem) {
                links.add(linkSB.toString());
                Log.d("NET_link", linkSB.toString());
                linkSB = new StringBuilder();
            }

        }
        if (qName.equals("description") )
        {
            isDesc = false;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle && isItem)
        {
            Log.d("NET", new String(ch, start, length));
            titles.add(new String(ch, start, length));
        }
        if (isLink && isItem)
        {
            linkSB.append(new String(ch, start, length));
            //Log.d("NET2", linkSB.toString());

        }
        if (isDesc)
        {
            Log.d("NET_desc", new String(ch, start, length));
            descriptions.add(new String(ch, start, length));
        }


    }

}
