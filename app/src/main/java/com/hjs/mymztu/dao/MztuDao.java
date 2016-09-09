package com.hjs.mymztu.dao;

import com.hjs.mymztu.constants.AppUrl;
import com.hjs.mymztu.entity.MzituBean;
import com.hjs.mymztu.utils.StringUtils;
import com.hjs.mymztu.utils.XLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MztuDao {

    public final static String TAG = MztuDao.class.getSimpleName();

    /**
     * 从网易Lofter获取数据
     * @param tag
     * @return
     */
    public List<MzituBean> getLofterDatas(String tag){
        XLog.v(MztuDao.TAG,"正在从网易Lofter服务器获取数据列表...");
        String baseUrl = AppUrl.lofter_baseUrl+(tag == null || tag.trim().length() <= 0 ? "胸":tag);
        List<MzituBean> imgsList = null;
        try {
            Document doc = Jsoup.connect(baseUrl).header("User-Agent",
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1").timeout(20000).get();
            Elements temps = doc.select("div.f-cb img");
            if (temps != null) {
                imgsList = new ArrayList<MzituBean>();
                for(Element ele : temps){
                    MzituBean bean = new MzituBean();
                    String firstImgUrl = ele.attr("src");
                    bean.setFirstImgUrl(firstImgUrl);
                    System.out.println("--"+firstImgUrl);
                    String[] urls = {firstImgUrl.substring(firstImgUrl.indexOf("imgurl=")+7,firstImgUrl.indexOf("?imageView")==-1?firstImgUrl.length():firstImgUrl.indexOf("?imageView"))};
                    System.out.println("--urls="+urls[0]);
                    bean.setUrls(urls);
                    imgsList.add(bean);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            XLog.v(MztuDao.TAG,"获取服务器列表数据失败!");
            return null;
        }
        return imgsList;
    }

    /**
     * 从妹子图网站上获取数据
     */
    public List<MzituBean> getMeiZiTuDatas(int pageNumber) {
        XLog.v(MztuDao.TAG,"正在妹子图(meizitu)服务器数据列表...");
        String baseUrl = AppUrl.meizitu_baseUrl;
        Document doc = null;
        int number = 0;
        try {
            doc = Jsoup.connect(baseUrl + "list_1_1.html").timeout(10000).get();
            Elements temps = doc.select("div#wp_page_numbers li a[href]");
            if (temps != null) {
                String content = temps.last().attr("href");
                if (content != null && content.trim().length() > 0) {
                    String[] strs = content.split("_");
                    String str = strs[strs.length - 1];
                    str = str.substring(0, str.indexOf("."));
                    number = Integer.parseInt(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            XLog.e(MztuDao.TAG,"获取妹子图服务器列表数据失败!");
            return null;
        }

        if (number == 0) {
            XLog.e(MztuDao.TAG,"获取妹子图服务器列表数据失败!");
            return null;
        }

        XLog.v(MztuDao.TAG,"一共有" + number + "页码，");
        int currentIndex = (pageNumber>number || pageNumber<0)?1:pageNumber;

        XLog.v(MztuDao.TAG,"正在连接服务器...");
        try {
            doc = Jsoup
                    .connect(
                            String.format(baseUrl + "list_1_%d.html",
                                    currentIndex)).timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
            XLog.v(MztuDao.TAG,"连接服务器失败!");
        }
        if (doc == null) {
            return null;
        }
        XLog.v(MztuDao.TAG,"正在读取服务器数据...");
        Elements elements = doc.select("div.pic a[href]");
        List<MzituBean> mList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            Element ele1 = elements.get(i);
            MzituBean bean = new MzituBean();
            Elements eles = ele1.select("img[src]");
            if (eles != null && eles.size() >= 1) {
                Element ele2 = eles.get(0);
                bean.setTitle(StringUtils.splitAndFilterString(ele2.attr("alt")));
                bean.setFirstImgUrl(ele2.attr("src"));
                try {
                    doc = Jsoup.connect(ele1.attr("href")).timeout(5000).get();
                    String tags = doc.select("div.metaRight p").first().text();
                    if (tags != null && tags.trim().length() > 0) {
                        bean.setTags(tags.replaceAll("Tags:|,", ""));
                    }
                    eles = doc.select("div#picture p img[src]");
                    List<String> picList = new ArrayList<String>();
                    for (Element ele3 : eles) {
                        picList.add(ele3.attr("src"));
                    }
                    bean.setUrls((String[]) picList.toArray(new String[picList .size()]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mList.add(bean);
            }
            XLog.v(MztuDao.TAG,"读取进度 " + (i + 1) + "/" + elements.size());
        }

        XLog.v(MztuDao.TAG,"获取服务器数据成功!");
        return mList;
    }

    /**
     * 从妹子图网站中获取数据
     */
    public List<MzituBean> getMzituDatas(int pageNumber) {
        XLog.v(MztuDao.TAG,"正在从妹子图(mzitu)服务器服务器数据列表...");
        String baseUrl = AppUrl.mzitu_baseUrl;
        Document doc = null;
        int number = 0;
        try {
            doc = Jsoup
                    .connect(baseUrl + "page/1")
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0")
                    .timeout(10000).get();
            Elements temps = doc.select("a.page-numbers a[href]");
            if (temps != null) {
                for (Element ele : temps) {
                    String url = ele.attr("href");
                    if (url != null && url.trim().length() > 0) {
                        String[] strs = url.split("/");
                        String str = strs[strs.length - 1];
                        try {
                            int currentPage = Integer.parseInt(str);
                            number = Math.max(currentPage, number);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            XLog.v(MztuDao.TAG,"-获取服务器列表数据失败!");
            return null;
        }

        if (number == 0) {
            XLog.v(MztuDao.TAG,"获取服务器列表数据失败!");
            return null;
        }

        int currentIndex = (pageNumber>number || pageNumber<0)?1:pageNumber;
        XLog.v(MztuDao.TAG,"正在连接服务器...");
        try {
            doc = Jsoup
                    .connect(String.format(baseUrl + "page/%d", currentIndex))
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0")
                    .timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
            XLog.v(MztuDao.TAG,"连接服务器失败!");
        }
        if (doc == null) {
            return null;
        }
        XLog.v(MztuDao.TAG,"正在读取服务器数据...");
        Elements elements = doc.select("ul#pins li");
        List<MzituBean> mList = new ArrayList<MzituBean>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            Element ele1 = elements.get(i);
            MzituBean bean = new MzituBean();
            Element eles = ele1.select("img[src]").first();
            bean.setTitle(eles.attr("alt"));
            bean.setFirstImgUrl(eles.attr("data-original"));
            try {
                doc = Jsoup
                        .connect(ele1.select("a[href]").first().attr("href"))
                        .header("User-Agent",
                                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0")
                        .timeout(5000).get();
                bean.setTags(doc.select("a[rel=category tag]").first().text());
                String imgUrl = doc.select("div.main-image img[src]").first()
                        .attr("src");

                Elements secondEles = doc.select("div.pagenavi span");
                int currentMax = 0;
                for (Element eless : secondEles) {
                    try {
                        int temp = Integer.parseInt(eless.text());
                        currentMax = Math.max(currentMax, temp);
                    } catch (NumberFormatException e) {

                    }
                }
                imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf("/")+4)
                        + "%s"
                        + imgUrl.substring(imgUrl.lastIndexOf("."),
                        imgUrl.length());
                List<String> picList = new ArrayList<String>();
                for (int j = 1; j <= currentMax; j++) {
                    DecimalFormat df1 = new DecimalFormat("00");
                    picList.add(String.format(imgUrl, df1.format(j)));
                }
                bean.setUrls((String[]) picList.toArray(new String[picList.size()]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mList.add(bean);
            sb.append(MzituBean.getJsonString(bean)
                    + (i >= elements.size() - 1 ? "" : "\n"));
            XLog.v(MztuDao.TAG,"读取进度 " + (i + 1) + "/" + elements.size());
        }

        XLog.v(MztuDao.TAG,"获取服务器数据成功!");
        return mList;
    }
}
