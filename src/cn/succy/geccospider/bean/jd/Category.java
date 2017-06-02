package cn.succy.geccospider.bean.jd;

import java.util.List;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HrefBean;
import com.geccocrawler.gecco.spider.HtmlBean;
import com.google.gson.Gson;

/**
 * 抓取手机、家用电器和母婴三个大类的商品信息 Category表示子分类信息内容，HrefBean是共用的链接Bean。
 * 
 * @author jiangmei
 *
 */
public class Category implements HtmlBean {

	private static final long serialVersionUID = 1L;
	/**
	 * 对应的是大的分类名字，如手机通讯，运营商，手机配件等
	 */
	@Text
	@HtmlField(cssPath = "dt> a")
	private String parentName;
	/**
	 * 相对于上面的大的分类下的小类目名字
	 */
	@HtmlField(cssPath = "dd> a")
	private List<HrefBean> categorys;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<HrefBean> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<HrefBean> categorys) {
		this.categorys = categorys;
	}

	public Category() {
	}

	public Category(String parentName, List<HrefBean> categorys) {
		this.parentName = parentName;
		this.categorys = categorys;
	}

	public static Category getCategory(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, Category.class);
	}

}
