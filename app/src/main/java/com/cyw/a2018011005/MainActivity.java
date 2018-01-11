package com.cyw.a2018011005;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
//記得打開manifest網路權限
public class MainActivity extends AppCompatActivity {
    //Intent it;
    ListView lv;
    //ArrayAdapter<String> adapter; 改成baseAdapter
    MyAdapter adapter;
    Intent it;
    MyHandler dataHandler; //這個移到外面宣告, intent才抓地到
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                it=new Intent(MainActivity.this,Main2Activity.class);
                it.putExtra("link",dataHandler.newsItems.get(i).link);
                startActivity(it);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_reload:
                //開一個副執行緒抓mobile01 RSS的資料, 用SAXParser(method寫在MyHandler.java)
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        String str_url = "https://www.mobile01.com/rss/news.xml";
                        URL url = null;
                        try {
                            url = new URL(str_url);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();
                            InputStream inputStream = conn.getInputStream();
                            InputStreamReader isr = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();
                            String str;

                            while ((str = br.readLine()) != null)
                            {
                                sb.append(str);
                            }
                            String str1 = sb.toString();
                            //Log.d("NET", str1);
                            //前面一樣通通讀到str1,然後放到SAXParser解析, 請參照MyHandler.java
                            dataHandler = new MyHandler();
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            xr.setContentHandler(dataHandler);
                            xr.parse(new InputSource(new StringReader(str1)));
                            //抓完並分析完資料把資料放進arrayAdapter,在主執行緒跑listView
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter=new MyAdapter(MainActivity.this,dataHandler.newsItems);
//                                    String data[] = new String[dataHandler.newsItems.size()];
//                                    for (int i=0;i<data.length;i++)
//                                    {
//                                        data[i] = dataHandler.newsItems.get(i).title;
//                                    }
//                                    adapter = new ArrayAdapter<String>(MainActivity.this,
//                                            android.R.layout.simple_list_item_1, data);data
                                    lv.setAdapter(adapter);

                                }
                            });

                            br.close();
                            isr.close();
                            inputStream.close();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

        }

        return super.onOptionsItemSelected(item);
    }


}
