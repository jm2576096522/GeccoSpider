package cn.succy.geccospider.bean.jd;

import com.geccocrawler.gecco.annotation.JSONPath;
import com.geccocrawler.gecco.spider.JsonBean;
import com.google.gson.Gson;

/**
 * 商品价格：json的数据元素的抽取
 * 
 * @author jiangmei
 */
public class JDPrice implements JsonBean {

	private static final long serialVersionUID = -5696033709028657709L;
	// 商品的编号
	@JSONPath("$.id[0]")
	private String code;

	// 京东价
	@JSONPath("$.p[0]")
	private float price;
	// 原价
	@JSONPath("$.m[0]")
	private float srcPrice;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getSrcPrice() {
		return srcPrice;
	}

	public void setSrcPrice(float srcPrice) {
		this.srcPrice = srcPrice;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public JDPrice() {
	}

	public JDPrice(String code, float price, float srcPrice) {
		this.code = code;
		this.price = price;
		this.srcPrice = srcPrice;
	}
	public static JDPrice  getJDPrice(String jsonStr) {
		Gson gson=new Gson();
		return gson.fromJson(jsonStr, JDPrice.class);
	}
	
}
