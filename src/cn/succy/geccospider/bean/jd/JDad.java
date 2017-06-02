package cn.succy.geccospider.bean.jd;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.annotation.JSONPath;
import com.geccocrawler.gecco.spider.JsonBean;
import com.google.gson.Gson;

/**
 * 商品广告的抽取
 * 
 * @author jiangmei
 *
 */
public class JDad implements JsonBean {

	private static final long serialVersionUID = 2250225801616402995L;
	// 广告
	@JSONPath("$.ads[0].ad")
	private String ad;

	// 所有广告
	@JSONPath("$.ads")
	private List<JSONObject> ads;

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public List<JSONObject> getAds() {
		return ads;
	}

	public void setAds(List<JSONObject> ads) {
		this.ads = ads;
	}

	public JDad() {
	}

	public JDad(String ad, List<JSONObject> ads) {
		this.ad = ad;
		this.ads = ads;
	}

	public static JDad getJDad(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, JDad.class);
	}

}
