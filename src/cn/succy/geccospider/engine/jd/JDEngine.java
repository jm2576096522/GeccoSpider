package cn.succy.geccospider.engine.jd;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.dynamic.DynamicGecco;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;

import cn.succy.geccospider.pipeline.jd.AllSortPipeline;

/**
 * 程序的主入口，该程序主要用于抓取京东手机分类的所有的第一页的商品数据
 * @Title JDEngine.java
 * @Description
 * @author Succy
 * @date 2016年11月16日 下午9:25:33
 * @version 1.0
 * @Company www.succy.cn
 */
public class JDEngine {

	public static void main(String[] args) {
		String url = "https://www.jd.com/allSort.aspx";
		//String url = "https://list.jd.com/list.html?cat=9987,653,655&ev=3751_76035&JL=5_7_0#J_main";
		String classpath = "cn.succy.geccospider";
		HttpRequest request = new HttpGetRequest(url);
		request.setCharset("gb2312");
		// 如果pipeline和htmlbean不在同一个包，classpath就要设置到他们的共同父包
		// 本引擎主要是把京东分类的页面手机板块给抓取过来封装成htmlbean
		GeccoEngine
			.create()
			.classpath(classpath)
			.start(request)
			.interval(500)
			.run();

		// 本引擎是负责抓取每一个细目对应的页面的第一页的所有商品列表的数据，开启5个线程同时抓取，提升效率
		// url = "https://list.jd.com/list.html?cat=9987,653,655&ev=3751_76035&JL=5_7_0#J_main";
		GeccoEngine
			.create()
			.classpath(classpath)
			.start(AllSortPipeline.sortRequests)
			//.start(url)
			.thread(5)
			.interval(500)
			.run();
	
	
	}
}
