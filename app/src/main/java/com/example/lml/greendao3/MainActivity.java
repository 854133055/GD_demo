package com.example.lml.greendao3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lml.greendao3.Greendao.Article;
import com.example.lml.greendao3.Greendao.ArticleDao;
import com.example.lml.greendao3.Greendao.DaoMaster;
import com.example.lml.greendao3.Greendao.DaoSession;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase mdb;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private EditText titleText;
    private EditText contentText;
    private WebView mWebView;
    private byte[] icon_byte;
    private String data = "http://www.jianshu.com/p/c4e9288d2ce6";
    private String titleColumn;
    private String subTitleColumn;
    private String dateColumn;
    private String IconColumn;
    private Button btn_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabase();
        getArticleDao();
        initWebView();

        titleText = (EditText) findViewById(R.id.editTextNote);
        contentText = (EditText) findViewById(R.id.editTextContent);

        btn_icon = (Button) findViewById(R.id.buttonAdd);
        btn_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArticle();
            }
        });


        cursor = mdb.query(getArticleDao().getTablename(), null, null, null, null, null, null);
        int[] to = {R.id.tv_title,R.id.tv_content,R.id.tv_date,R.id.iv_1};

        ListView listView = (ListView) findViewById(R.id.listView);
        MyAdapter myAdapter = new MyAdapter(this, R.layout.layout_item, cursor, getFrom(), to);
        listView.setAdapter(myAdapter);

    }

    /**
     * 创建数据库
     */
    public void setupDatabase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ArticleTable.db", null);
        mdb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(mdb);
        daoSession = daoMaster.newSession();
    }

    /**
     * 获取 ArticleDao 对象
     * @return ArticleDao
     */
    public ArticleDao getArticleDao(){
        return daoSession.getArticleDao();
    }

    /**
     * WebView显示界面，同时获取网页title，显示在EditText上；获取网页icon，转换成byte[]格式，方便以后存到数据库中
     */
    public void initWebView(){
        mWebView = (WebView)this.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleText.setText(title);
            }

            //获取网页图标
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                //将bitmap转换程byte[]形式，存进数据库
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                icon_byte = byteArrayOutputStream.toByteArray();
            }
        });

//            //获取网页打开进度
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                progressBar.setProgress(newProgress);
//                if(newProgress == 100) {
//                    progressBar.setProgress(0);
//                    //获取当前网页地址
//                    url01.setText("url: " + mWebView.getUrl());
//
//                }
//            }
        mWebView.loadUrl(data);
    }

    /**
     * 获取插入的Artile对象的title、Content，date，byte[]，并插入数据库
     */
    public void addArticle() {

        titleText = (EditText) findViewById(R.id.editTextNote);
        contentText = (EditText) findViewById(R.id.editTextContent);

        String articleTitleText = titleText.getText().toString();
        String articleContentText = contentText.getText().toString();

        titleText.setText("");
        contentText.setText("");

        //规定时间格式，并获取系统时间,如：15:43
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = sdf.format(new Date());

        Article article = new Article(null, articleTitleText, articleContentText, date,icon_byte);
        getArticleDao().insert(article);

        Toast.makeText(this, "Inserted new note,ID:" + article.getId(), Toast.LENGTH_LONG).show();
        cursor.requery();

    }

    /**
     * 获得 Adapter的第四个from参数
     * @return String[]
     */
    public String[] getFrom(){
        String titleColumn = ArticleDao.Properties.Title.columnName;
        String subTitleColumn = ArticleDao.Properties.Content.columnName;
        String dateColumn = ArticleDao.Properties.Date.columnName;
        String IconColumn = ArticleDao.Properties.Icon.columnName;
        String[] from = {titleColumn,subTitleColumn,dateColumn,IconColumn};
        return from;
    }

}
