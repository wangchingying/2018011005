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
    public ArrayList<String> titles = new ArrayList<>();
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
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle && isItem)
        {
            Log.d("NET", new String(ch, start, length));
            titles.add(new String(ch, start, length));
        }

    }

}
