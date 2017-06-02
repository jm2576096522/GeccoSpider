package cn.succy.geccospider.bean.jd;

import java.util.List;

import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import com.google.gson.Gson;

/**
 * 京东所有商品的页面抓取
 */
@Gecco(matchUrl = "https://www.jd.com/allSort.aspx", pipelines = { "allSortPipeline", "consolePipeline","filePipeline" })
public class AllSort implements HtmlBean {

	private static final long serialVersionUID = 1L;

	@Request
	private HttpRequest request;

	// 手机
	@HtmlField(cssPath = ".category-items > div:nth-child(1) > div:nth-child(2) > div.mc > div.items > dl")
	private List<Category> mobile;

	// 家用电器
	@HtmlField(cssPath = ".category-items > div:nth-child(1) > div:nth-child(3) > div.mc > div.items > dl")
	private List<Category> domestic;

	// 母婴
	@HtmlField(cssPath = ".category-items > div:nth-child(2) > div:nth-child(1) > div.mc > div.items > dl")
	private List<Category> baby;

	public List<Category> getMobile() {
		return mobile;
	}

	public void setMobile(List<Category> mobile) {
		this.mobile = mobile;
	}

	public List<Category> getDomestic() {
		return domestic;
	}

	public void setDomestic(List<Category> domestic) {
		this.domestic = domestic;
	}

	public List<Category> getBaby() {
		return baby;
	}

	public void setBaby(List<Category> baby) {
		this.baby = baby;
	}
	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public AllSort() {
	}


	public AllSort(HttpRequest request, List<Category> mobile, List<Category> domestic, List<Category> baby) {
		this.request = request;
		this.mobile = mobile;
		this.domestic = domestic;
		this.baby = baby;
	}

	public static AllSort getAllSort(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, AllSort.class);
	}
}
