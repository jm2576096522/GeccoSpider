package cn.succy.geccospider.pipeline.jd;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.scheduler.SchedulerContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;

import cn.succy.geccospider.bean.jd.ProductBrief;
import cn.succy.geccospider.bean.jd.ProductList;
import cn.succy.geccospider.util.MongoUtils;

@PipelineName("productListPipeline")
public class ProductListPipeline implements Pipeline<ProductList> {

	@Override
	public void process(ProductList productList) {
		HttpRequest currRequest = productList.getRequest();
		// 从productList里边获取url，目的是为了从之前存进数据库中找到对应url的小类目
		String url = currRequest.getUrl();// https://list.jd.com/list.html?cat=1319,1528,1563&delivery=1&page=1&JL=4_10_0&go=0
		// 把类目名对应的商品详情的列表获取，例如，手机对应到的页面的60条记录
		String[] strs = url.split("&");
		productList.getRequest().setUrl(strs[0]);
		//System.out.println("*****"+strs[0]);
		Document doc = Document.parse(JSON.toJSONString(productList));
		MongoUtils.getlistCollection().insertOne(doc);
		//updataUrl(productList,url);

		/*// 下一页继续抓取
		int currPage = productList.getCurrPage();
		int nextPage = currPage + 1;
		int totalPage = productList.getTotalPage();
		if (nextPage <= totalPage) {
			String nextUrl = "";
			String currUrl = currRequest.getUrl();
			if (currUrl.indexOf("page=") != -1) {
				nextUrl = StringUtils.replaceOnce(currUrl, "page=" + currPage, "page=" + nextPage);
			} else {
				nextUrl = currUrl + "&" + "page=" + nextPage;
			}
			//updataUrl(productList,url);
			SchedulerContext.into(currRequest.subRequest(nextUrl));
		}*/
	}

	/*private void updataUrl(ProductList productList, String url) {
		
		List<ProductBrief> details = productList.getDetails();
		// 转成json字符串
		String jsonString = JSON.toJSONString(details);
		//System.out.println("*****"+jsonString);
		String[] strs = url.split("&");
		//System.out.println("*****"+strs[0]);
		// 通过url找到数组里边对应url的类目，然后添加一个字段叫做details，并且把details的值
		// 给添加进去
		MongoUtils.getCollection().updateOne(new Document("categorys.url", strs[0]),
				Document.parse("{\"$set\":{\"categorys.$.productBrief\":" + jsonString + "}}"));
		
	}*/

}
