package cn.succy.geccospider.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import com.geccocrawler.gecco.spider.HrefBean;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.CursorType;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import cn.succy.geccospider.bean.jd.Category;

/**
 * mongodb的工具类，可以用作获取数据库的客户端连接对象，数据库对象，数据库的集合对象
 * 
 * @Title MongoUtils.java
 * @Description
 * @author Succy
 * @date 2016年11月17日 下午2:43:01
 * @version 1.0
 * @Company www.succy.cn
 */
public final class MongoUtils {
	private static String HOST; // mongo服务器地址
	private static String DBNAME; // 要连接的数据库名
	private static String COLLECTION;// 要操作的集合，这里假定只操作这一个集合

	private static MongoClient client;
	private static MongoDatabase db;
	private static MongoCollection<Document> colle;

	private MongoUtils() {
	}

	/**
	 * 通过配置文件把配置的参数取出
	 */
	static {
		Properties prop = new Properties();
		InputStream inStream = ClassLoader.getSystemResourceAsStream("mongo.properties");
		try {
			prop.load(inStream);
			HOST = prop.getProperty("mongo.host");
			DBNAME = prop.getProperty("mongo.dbName");
			COLLECTION = prop.getProperty("mongo.collection");

			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void init() {
		client = new MongoClient(HOST);
		db = client.getDatabase(DBNAME);
		colle = db.getCollection(COLLECTION);
	}

	/**
	 * 获取客户端对象
	 * 
	 * @return client
	 */
	public static MongoClient getClient() {
		return client;
	}

	/**
	 * 获取MongoDatabase对象
	 * 
	 * @return db
	 */
	public static MongoDatabase getDb() {
		return db;
	}

	/**
	 * 获取MongoCollection<Document>对象
	 * 
	 * @return collection
	 */
	public static MongoCollection<Document> getCollection() {
		return colle;
	}
	
	public static MongoCollection<Document> getdetailCollection() {
		return colle=db.getCollection("details");
	}

	public static MongoCollection<Document> getlistCollection() {
		return colle=db.getCollection("list");
	}

	//获取商品分类连接
	public static DBCollection getProductCollection(){
		DB psdoc = client.getDB("jd");
		DBCollection product = psdoc.getCollection("product");
		return product;
	}
	//获取商品简要信息连接
	public static DBCollection getlistdbCollection(){
		DB psdoc = client.getDB("jd");
		DBCollection list = psdoc.getCollection("list");
		return list;
	}
	//获取商品详情连接
	public static DBCollection getDetailsCollection(){
		DB psdoc = client.getDB("jd");
		DBCollection details = psdoc.getCollection("details");
		return details;
	}
	public static void main(String[] args) throws Exception {
		/*
		 * MongoClient mongoClient = new MongoClient();
		 * 
		 * MongoDatabase db = mongoClient.getDatabase("jd");
		 * MongoCollection<Document> doc = db.getCollection("product");
		 */

		/*
		 * FindIterable<Document> findIterable = colle.find();
		 * MongoCursor<Document> mongoCursor = findIterable.iterator(); while
		 * (mongoCursor.hasNext()) { System.out.println(mongoCursor.next()); }
		 */


		
		DBCursor cursor =getProductCollection().find();
		List<Category> list = DBObjectToJavaBean.cursorToList(cursor, Category.class);
		// System.out.println(list);
		for (Category category : list) {
			System.out.println(category.getParentName());
			List<HrefBean> href = category.getCategorys();
			for (HrefBean hrefBean : href) {
				System.out.println(hrefBean.getTitle() + hrefBean.getUrl());
			}
		}
		/*
		 * while (cursor.hasNext()) { DBObject obj = cursor.next(); // 反转
		 * Category category = DBObjectToJavaBean
		 * .propertySetter(obj,Category.class); //Category category =
		 * gson.fromJson(obj.toString(), Category.class); //Category category =
		 * BeanUtil.dbObject2Bean(obj, Category.class).newInstance();
		 * System.out.println(category.getParentName() +
		 * category.getCategorys()); }
		 */
	}

}
