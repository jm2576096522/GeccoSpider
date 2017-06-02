package cn.succy.geccospider.bean.jd;

import com.geccocrawler.gecco.annotation.Ajax;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Image;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.annotation.RequestParameter;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import com.google.gson.Gson;

/**
 * 商品详情页：详情页的抓取
 * 
 * @author jiangmei
 *
 */
@Gecco(matchUrl = "https://item.jd.com/{id}.html?dist=jd", pipelines = { "consolePipeline", "filePipeline","mongoPipeline" })
public class ProductDetail implements HtmlBean {

	private static final long serialVersionUID = -377053120283382723L;
	/**
	 * 获取请求对象，从该对象中可以获取抓取的是哪个url
	 */
	//@Request
	//private HttpRequest request;
	// 商品代码
	@RequestParameter
	private String id;

	// 标题
	@Text
	@HtmlField(cssPath = ".itemInfo-wrap > div.sku-name")
	private String title;

	// ajax获取商品价格
	@Ajax(url = "http://cd.jd.com/prices/get?type=1&pdtk=&pdbp=0&skuid=J_{id}")
	private JDPrice price;

	// 商品的推广语
	@Ajax(url = "http://cd.jd.com/promotion/v2?skuId={id}&area=1_2805_2855_0&cat=737%2C794%2C798")
	private JDad jdAd;

	// 商品规格参数
	@HtmlField(cssPath = "#detail > div.tab-con > div:nth-child(1) > div.p-parameter > ul.parameter2.p-parameter-list>li")
	private String detail;

	// 图片
	// @Image(download = "d:/gecco/jd/img")
	@Image({ "data-lazy-img", "src" })
	// @HtmlField(cssPath = "div.j-sku-item > div.p-img > a > img")
	@HtmlField(cssPath = ".preview-wrap >#spec-n1> img")
	private String image;

	public JDPrice getPrice() {
		return price;
	}

	public void setPrice(JDPrice price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public JDad getJdAd() {
		return jdAd;
	}

	public void setJdAd(JDad jdAd) {
		this.jdAd = jdAd;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/*public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}*/

	public ProductDetail() {
	}

	public ProductDetail(String id, String title, JDPrice price, JDad jdAd, String detail, String image) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.jdAd = jdAd;
		this.detail = detail;
		this.image = image;
	}

	public static ProductDetail getProductDetail(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, ProductDetail.class);
	}

}
