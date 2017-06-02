package cn.succy.geccospider.engine.jd;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.bson.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HrefBean;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import cn.succy.geccospider.bean.jd.Category;
import cn.succy.geccospider.bean.jd.ProductBrief;
import cn.succy.geccospider.bean.jd.ProductDetail;
import cn.succy.geccospider.bean.jd.ProductList;
import cn.succy.geccospider.pipeline.jd.AllSortPipeline;
import cn.succy.geccospider.util.DBObjectToJavaBean;
import cn.succy.geccospider.util.MongoUtils;
import sun.util.logging.resources.logging;

import org.eclipse.swt.widgets.Composite;

public class jdView {

	protected Shell shlGecco;
	private Text txtSearchUrl;
	private Text PDetail;
	private Button btnSearch; // 开始爬取的按钮
	private Button button;// 查询按钮
	private Combo combo;
	private Combo combo_1;
	private Table productBrief;
	private Table categorys;
	private String select;
	private Text dcode;
	private Text dprice;
	private Text dsrcPrice;
	private Text dtitle;
	private Text adtitle;
	private Text adUrl;
	private DBCollection ProductCollection = MongoUtils.getProductCollection();// 连接Product数据库集合
	private DBCollection DetailsdbCollection = MongoUtils.getDetailsCollection();// 连接Details集合
	private MongoCollection<Document> listsCollection = MongoUtils.getlistCollection();// 连接list集合
	private MongoCollection<Document> detailCollection = MongoUtils.getdetailCollection();// 连接Details集合
	private DBCollection listdbCollection = MongoUtils.getlistdbCollection();// 连接list集合

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			jdView window = new jdView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * 
	 * @throws Exception
	 */
	public void open() throws Exception {
		Display display = Display.getDefault();
		createContents();
		shlGecco.open();
		shlGecco.layout();
		while (!shlGecco.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @throws Exception
	 */
	protected void createContents() throws Exception {
		shlGecco = new Shell();
		shlGecco.setImage(SWTResourceManager.getImage(jdView.class, "/images/yc.ico"));
		shlGecco.setSize(1366, 736);
		shlGecco.setText("gecco爬取京东信息");
		shlGecco.setLocation(0, 0);
		shlGecco.setLayout(new FillLayout(SWT.HORIZONTAL));
		SashForm sashForm = new SashForm(shlGecco, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);

		Group group = new Group(sashForm, SWT.NONE);
		group.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		group.setText("爬取查询条件");
		group.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm_2 = new SashForm(group, SWT.NONE);

		Group group_2 = new Group(sashForm_2, SWT.NONE);
		group_2.setText("爬取条件");

		Label lblip = new Label(group_2, SWT.NONE);
		lblip.setLocation(34, 27);
		lblip.setSize(113, 21);
		lblip.setText("输入开始爬取地址:");

		txtSearchUrl = new Text(group_2, SWT.BORDER);
		txtSearchUrl.setLocation(153, 23);
		txtSearchUrl.setSize(243, 23);
		txtSearchUrl.setText("https://www.jd.com/allSort.aspx");

		btnSearch = new Button(group_2, SWT.NONE);
		btnSearch.setLocation(408, 23);
		btnSearch.setSize(82, 25);

		btnSearch.setEnabled(false);
		btnSearch.setText("开始爬取");

		Group group_3 = new Group(sashForm_2, SWT.NONE);
		group_3.setText("查询条件");

		Label label_2 = new Label(group_3, SWT.NONE);
		label_2.setLocation(77, 25);
		label_2.setSize(86, 25);
		label_2.setText("选择查询条件:");

		combo = new Combo(group_3, SWT.NONE);
		combo.setLocation(169, 23);
		combo.setSize(140, 23);
		combo.setItems(new String[] { "全部", "商品总类别", "子类别名", "类别链接" });
		// combo.setText("全部");

		button = new Button(group_3, SWT.NONE);
		button.setLocation(524, 23);
		button.setSize(80, 25);
		button.setText("点击查询");
		button.setEnabled(false);

		combo_1 = new Combo(group_3, SWT.NONE);
		combo_1.setEnabled(false);
		combo_1.setLocation(332, 23);
		combo_1.setSize(170, 23);
		// combo_1.setItems(new String[] { "全部" });
		// combo_1.setText("全部");
		sashForm_2.setWeights(new int[] { 562, 779 });
		Group group_1 = new Group(sashForm, SWT.NONE);
		group_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		group_1.setText("爬取结果");
		group_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm_1 = new SashForm(group_1, SWT.NONE);

		Group group_Categorys = new Group(sashForm_1, SWT.NONE);
		group_Categorys.setText("商品分组");
		group_Categorys.setLayout(new FillLayout(SWT.HORIZONTAL));

		categorys = new Table(group_Categorys, SWT.BORDER | SWT.FULL_SELECTION);
		categorys.setHeaderVisible(true);
		categorys.setLinesVisible(true);

		TableColumn ParentName = new TableColumn(categorys, SWT.NONE);
		ParentName.setWidth(87);
		ParentName.setText("商品总类别");

		TableColumn Title = new TableColumn(categorys, SWT.NONE);
		Title.setWidth(87);
		Title.setText("子类别名");

		TableColumn Ip = new TableColumn(categorys, SWT.NONE);
		Ip.setWidth(152);
		Ip.setText("类别链接");

		Group group_ProductBrief = new Group(sashForm_1, SWT.NONE);
		group_ProductBrief.setText("商品简要信息列表");
		group_ProductBrief.setLayout(new FillLayout(SWT.HORIZONTAL));

		productBrief = new Table(group_ProductBrief, SWT.BORDER | SWT.FULL_SELECTION);
		productBrief.setLinesVisible(true);
		productBrief.setHeaderVisible(true);

		TableColumn Code = new TableColumn(productBrief, SWT.NONE);
		Code.setWidth(80);
		Code.setText("商品编号");

		TableColumn Detailurl = new TableColumn(productBrief, SWT.NONE);
		Detailurl.setWidth(162);
		Detailurl.setText("商品详情链接");

		TableColumn Preview = new TableColumn(productBrief, SWT.NONE);
		Preview.setWidth(170);
		Preview.setText("商品图片链接");

		TableColumn Dtitle = new TableColumn(productBrief, SWT.NONE);
		Dtitle.setWidth(150);
		Dtitle.setText("商品标题");

		Group group_ProductDetail = new Group(sashForm_1, SWT.NONE);
		group_ProductDetail.setText("商品详细信息");
		group_ProductDetail.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(group_ProductDetail, SWT.NONE);

		PDetail = new Text(composite,
				SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		PDetail.setLocation(55, 339);
		PDetail.setSize(360, 220);

		Label Id = new Label(composite, SWT.NONE);
		Id.setBounds(31, 28, 61, 15);
		Id.setText("商品编号：");

		Label detail = new Label(composite, SWT.NONE);
		detail.setText("商品详情：");
		detail.setBounds(31, 311, 61, 15);

		Label title = new Label(composite, SWT.NONE);
		title.setText("商品标题：");
		title.setBounds(31, 64, 61, 15);

		Label jdAd = new Label(composite, SWT.NONE);
		jdAd.setText("商品广告：");
		jdAd.setBounds(31, 201, 61, 15);

		Label price = new Label(composite, SWT.NONE);
		price.setBounds(31, 108, 61, 15);
		price.setText("价格：");

		dcode = new Text(composite, SWT.BORDER);
		dcode.setEditable(false);
		dcode.setBounds(98, 25, 217, 21);

		Label jdprice = new Label(composite, SWT.NONE);
		jdprice.setBounds(75, 127, 48, 15);
		jdprice.setText("京东价：");

		Label srcPrice = new Label(composite, SWT.NONE);
		srcPrice.setBounds(75, 166, 48, 15);
		srcPrice.setText("原售价：");

		dprice = new Text(composite, SWT.BORDER);
		dprice.setEditable(false);
		dprice.setBounds(128, 127, 187, 21);

		dsrcPrice = new Text(composite, SWT.BORDER);
		dsrcPrice.setEditable(false);
		dsrcPrice.setBounds(128, 166, 187, 21);

		dtitle = new Text(composite,
				SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		dtitle.setBounds(98, 62, 217, 42);

		Label label = new Label(composite, SWT.NONE);
		label.setText("广告标题：");
		label.setBounds(62, 233, 61, 15);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("广告链接：");
		label_1.setBounds(62, 272, 61, 15);

		adtitle = new Text(composite, SWT.BORDER);
		adtitle.setEditable(false);
		adtitle.setBounds(128, 231, 187, 21);

		adUrl = new Text(composite, SWT.BORDER);
		adUrl.setEditable(false);
		adUrl.setBounds(128, 270, 187, 21);
		sashForm_1.setWeights(new int[] { 335, 573, 430 });
		sashForm.setWeights(new int[] { 85, 586 });

		doEvent();// 组件的事件操作

	}

	// 进行爬取操作操作
	private void doEvent() throws Exception {

		// showProductBrief();// 商品简要情况

		productBrief.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				changeColumnWidth();
			}
		});

		// 商品主分类
		categorys.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				changeColumnWidth();
			}
		});

		// 鼠标在表格里面时，锁定某一行，并在下方控件中输出
		productBrief.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				TableItem[] tab = productBrief.getSelection();
				if (tab != null && tab.length > 0) {
					// String url = tab[0].getText(1);
					String code = tab[0].getText(0);
					try {
						showProductDetails(code);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		// 鼠标在表格里面时，锁定某一行，并在下方控件中输出
		categorys.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				TableItem[] tab = categorys.getSelection();
				if (tab != null && tab.length > 0) {
					String url = tab[0].getText(2);
					// String parentName=tab[0].getText(0);
					try {
						showProductBrief(url);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				select = combo.getText();
			}
		});
		txtSearchUrl.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				btnSearch.setEnabled(txtSearchUrl.getText().trim().intern() != "");
			}

			public void focusGained(FocusEvent e) {
				btnSearch.setEnabled(txtSearchUrl.getText().trim().intern() != "");
			}
		});

		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String url = txtSearchUrl.getText().trim().intern();
				String classpath = "cn.succy.geccospider";
				HttpRequest request = new HttpGetRequest(url);
				request.setCharset("gb2312");
				// 如果pipeline和htmlbean不在同一个包，classpath就要设置到他们的共同父包
				// 本引擎主要是把京东分类的页面手机板块给抓取过来封装成htmlbean
				GeccoEngine.create().classpath(classpath).start(request).interval(500).run();

				// 本引擎是负责抓取每一个细目对应的页面的第一页的所有商品列表的数据，开启5个线程同时抓取，提升效率
				GeccoEngine.create().classpath(classpath).start(AllSortPipeline.sortRequests).thread(1).interval(500)
						.run();
			}

		});
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				select = combo.getText();
				if ("全部".equals(select)) {
					combo_1.removeAll();
					combo_1.setEnabled(false);
					button.setEnabled(true);
				} else {
					button.setEnabled(false);
					combo_1.setEnabled(true);
					combo_1.removeAll();
					DBCursor cursor = ProductCollection.find();
					List<Category> list = null;
					try {
						list = DBObjectToJavaBean.cursorToList(cursor, Category.class);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// System.out.println(list.size());//57
					String[] items = new String[list.size()];
					Category category = new Category();
					for (int i = 0; i < items.length; i++) {
						category = list.get(i);
						if ("商品总类别".equals(select)) {
							items[i] = category.getParentName();
						} else {
							List<HrefBean> href = category.getCategorys();
							for (HrefBean hrefBean : href) {
								if ("子类别名".equals(select)) {
									items[i] = hrefBean.getTitle();
								}
								if ("类别链接".equals(select)) {
									items[i] = hrefBean.getUrl();
								}
							}
						}
					}
					combo_1.setItems(items);
				}
			}
		});
		combo_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//System.out.println(combo_1.getText());
				button.setEnabled(combo_1.getText().trim() != "");
			}
		});
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					if ("全部".equals(select)) {
						showCategorys();// 商品主分类
					}
					if ("商品总类别".equals(select)) {
						showCategoryByParentName(combo_1.getText());
					}
					if ("子类别名".equals(select)) {
						showCategoryByTitle(combo_1.getText());
					}
					if ("类别链接".equals(select)) {
						showCategoryByUrl(combo_1.getText());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	protected void showCategoryByUrl(String url) throws Exception {
		categorys.removeAll();
		productBrief.removeAll();
		removeDetail();
		// System.out.println(url);
		BasicDBObject obj = new BasicDBObject("categorys.url", url);
		DBCursor cursor = ProductCollection.find(obj);
		List<Category> list = DBObjectToJavaBean.cursorToList(cursor, Category.class);
		// System.out.println(list);
		TableItem ti;
		for (Category category : list) {
			List<HrefBean> href = category.getCategorys();
			for (HrefBean hrefBean : href) {
				ti = new TableItem(categorys, SWT.NONE);
				ti.setText(new String[] { category.getParentName(), hrefBean.getTitle(), hrefBean.getUrl() });
			}
		}
	}

	protected void showCategoryByTitle(String title) throws Exception {
		categorys.removeAll();
		productBrief.removeAll();
		removeDetail();
		// System.out.println(title);
		BasicDBObject obj = new BasicDBObject("categorys.title", title);
		DBCursor cursor = ProductCollection.find(obj);
		List<Category> list = DBObjectToJavaBean.cursorToList(cursor, Category.class);
		// System.out.println(list);
		TableItem ti;
		for (Category category : list) {
			List<HrefBean> href = category.getCategorys();
			for (HrefBean hrefBean : href) {
				ti = new TableItem(categorys, SWT.NONE);
				ti.setText(new String[] { category.getParentName(), hrefBean.getTitle(), hrefBean.getUrl() });
			}
		}
	}

	protected void showCategoryByParentName(String parentName) throws Exception {
		categorys.removeAll();
		productBrief.removeAll();
		removeDetail();
		// System.out.println(parentName);
		BasicDBObject obj = new BasicDBObject("parentName", parentName);
		DBCursor cursor = ProductCollection.find(obj);
		List<Category> list = DBObjectToJavaBean.cursorToList(cursor, Category.class);
		// System.out.println(list);
		TableItem ti;
		for (Category category : list) {
			List<HrefBean> href = category.getCategorys();
			for (HrefBean hrefBean : href) {
				ti = new TableItem(categorys, SWT.NONE);
				ti.setText(new String[] { category.getParentName(), hrefBean.getTitle(), hrefBean.getUrl() });
			}
		}
	}

	protected void showProductDetails(String code) {
		// System.out.println(code);
		BasicDBObject obj = new BasicDBObject("id", code);
		BasicDBObject obj1 = new BasicDBObject();
		obj1.put("_id", 0);
		// obj1.put("currPage", 0);
		// obj1.put("details", 1);
		DBCursor cursor = DetailsdbCollection.find(obj);
		while (cursor.hasNext()) {
			DBObject dbObj = cursor.next();
			// System.out.println(dbObj);
			try {
				ProductDetail detail = DBObjectToJavaBean.propertySetter(dbObj, ProductDetail.class);
				dcode.setText(detail.getId());
				dtitle.setText(detail.getTitle());
				dprice.setText(String.valueOf(detail.getPrice().getPrice()));
				dsrcPrice.setText(String.valueOf(detail.getPrice().getSrcPrice()));
				adtitle.setText(detail.getJdAd().getAd());
				adUrl.setText(detail.getJdAd().getAd());
				PDetail.setText(detail.getDetail());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void removeDetail() {
		dcode.setText("");
		dtitle.setText("");
		dprice.setText("");
		dsrcPrice.setText("");
		adtitle.setText("");
		adUrl.setText("");
		PDetail.setText("");
	}

	private void showProductBrief(String url) throws Exception {
		// System.out.println(url);
		BasicDBObject obj = new BasicDBObject("request.url", url);
		BasicDBObject obj1 = new BasicDBObject();
		obj1.put("_id", 0);
		// obj1.put("currPage", 0);
		obj1.put("details", 1);

		DBCursor cursor = listdbCollection.find(obj, obj1);
		List<ProductList> list = DBObjectToJavaBean.cursorToList(cursor, ProductList.class);
		TableItem ti;
		productBrief.removeAll();
		for (ProductList productList : list) {
			List<ProductBrief> productBriefs = productList.getDetails();
			for (ProductBrief brief : productBriefs) {
				ti = new TableItem(productBrief, SWT.NONE);
				ti.setText(
						new String[] { brief.getCode(), brief.getDetailUrl(), brief.getPreview(), brief.getTitle() });

			}
		}
	}

	private void showCategorys() throws Exception {
		categorys.removeAll();
		productBrief.removeAll();
		removeDetail();
		DBCursor cursor = ProductCollection.find();
		List<Category> list = DBObjectToJavaBean.cursorToList(cursor, Category.class);
		// System.out.println(list);
		TableItem ti;
		for (Category category : list) {
			List<HrefBean> href = category.getCategorys();
			for (HrefBean hrefBean : href) {
				ti = new TableItem(categorys, SWT.NONE);
				ti.setText(new String[] { category.getParentName(), hrefBean.getTitle(), hrefBean.getUrl() });
			}
		}
	}

	protected void changeColumnWidth() {
		TableColumn[] cols = categorys.getColumns();
		cols[0].setWidth(80);
		int width = (categorys.getSize().x - 80) / (cols.length - 1);
		for (int i = 1; i < cols.length; i++) {
			cols[i].setWidth(width);
		}
		TableColumn[] procols = productBrief.getColumns();
		procols[0].setWidth(80);
		int prowidth = (productBrief.getSize().x - 80) / (procols.length - 1);
		for (int i = 1; i < procols.length; i++) {
			procols[i].setWidth(prowidth);
		}
	}

}
