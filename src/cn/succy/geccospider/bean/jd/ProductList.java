package cn.succy.geccospider.bean.jd;

import java.util.List;

import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import com.google.gson.Gson;

/**
 * 商品列表实体类，对应的是京东的类目连接到的页面的每页60条记录
 * @Title ProductList.java
 */
@Gecco(matchUrl = "https://list.jd.com/list.html?cat={cat}&delivery={delivery}&page={page}&JL={JL}&go=0", pipelines = {"consolePipeline", "productListPipeline" ,"filePipeline"})
public class ProductList implements HtmlBean {

	private static final long serialVersionUID = 1L;

	/**
	 * 获取请求对象，从该对象中可以获取抓取的是哪个url
	 */
	@Request
	private HttpRequest request;

	/**
	 * 抓取列表项的详细内容，包括titile，价格，详情页地址等
	 */
	@HtmlField(cssPath = "#plist>ul>li.gl-item")
	private List<ProductBrief> details;
	/**
	 * 获得商品列表的当前页
	 */
	@Text
	@HtmlField(cssPath = "#J_topPage > span > b")
	private int currPage;
	/**
	 * 获得商品列表的总页数
	 */
	@Text
	@HtmlField(cssPath = "#J_topPage > span > i")
	private int totalPage;

	public List<ProductBrief> getDetails() {
		return details;
	}

	public void setDetails(List<ProductBrief> details) {
		this.details = details;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public ProductList() {
	}

	public ProductList(HttpRequest request, List<ProductBrief> details, int currPage, int totalPage) {
		this.request = request;
		this.details = details;
		this.currPage = currPage;
		this.totalPage = totalPage;
	}

	public static ProductList getProductList(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, ProductList.class);
	}

}
