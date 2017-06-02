package cn.succy.geccospider.bean.jd;

import java.util.List;

import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.spider.HtmlBean;

public class JdType implements HtmlBean {
	private static final long serialVersionUID = 1L;

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
	public JdType() {
	}

	public JdType(List<Category> mobile, List<Category> domestic, List<Category> baby) {
		this.mobile = mobile;
		this.domestic = domestic;
		this.baby = baby;
	}

}
