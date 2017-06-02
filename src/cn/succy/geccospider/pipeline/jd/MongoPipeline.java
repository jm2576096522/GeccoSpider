package cn.succy.geccospider.pipeline.jd;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import cn.succy.geccospider.bean.jd.AllSort;
import cn.succy.geccospider.bean.jd.ProductBrief;
import cn.succy.geccospider.bean.jd.ProductDetail;
import cn.succy.geccospider.bean.jd.ProductList;
import cn.succy.geccospider.util.MongoUtils;

/**
 * 对抽取出来的ProductList进行入库操作，使用mongodb
 * 
 * @Title MongoPipeline.java
 */
@PipelineName("mongoPipeline")
public class MongoPipeline implements Pipeline<ProductDetail> {
	public void process(ProductDetail productDetail) {
		Document doc = Document.parse(JSON.toJSONString(productDetail));
		MongoUtils.getdetailCollection().insertOne(doc);
	}
}
