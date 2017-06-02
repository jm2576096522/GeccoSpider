package cn.succy.geccospider.pipeline.jd;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HrefBean;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;

import cn.succy.geccospider.bean.jd.AllSort;
import cn.succy.geccospider.bean.jd.Category;
import cn.succy.geccospider.util.MongoUtils;

@PipelineName("allSortPipeline")
public class AllSortPipeline implements Pipeline<AllSort> {

	public static List<HttpRequest> sortRequests = new ArrayList<HttpRequest>();
	public static MongoCollection<Document> collection = MongoUtils.getCollection();// 获取mongo的集合

	@Override
	public void process(AllSort allSort) {
		/*
		 * // 把json转成Document Document doc =
		 * Document.parse(JSON.toJSONString(allSort)); // 向集合里边插入一条文档
		 * collection.insertOne(doc);
		 */
		List<Category> mobiles = allSort.getMobile();
		process(allSort, mobiles);
		List<Category> domestics = allSort.getDomestic();
		process(allSort, domestics);
		List<Category> bodys = allSort.getBaby();
		process(allSort, bodys);
	}

	private void process(AllSort allSort, List<Category> categorys) {
		if (categorys == null) {
			return;
		}

		for (Category category : categorys) {
			  Document doc = Document.parse(JSON.toJSONString(category)); // 向集合里边插入一条文档 
			  collection.insertOne(doc);
			
			List<HrefBean> hrefs = category.getCategorys();
			for (HrefBean href : hrefs) {
				String url = href.getUrl() + "&delivery=1&page=1&JL=4_10_0&go=0";
				HttpRequest currRequest = allSort.getRequest();
				// 将分类的商品列表地址暂存起来
				sortRequests.add(currRequest.subRequest(url));
			}
		}
	}
}