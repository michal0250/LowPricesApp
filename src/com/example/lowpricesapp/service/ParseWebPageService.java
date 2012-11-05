package com.example.lowpricesapp.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ParseWebPageService {
	private String url;
	private static Logger logger = Logger.getLogger(ParseWebPageService.class);
	private Document doc;

	public ParseWebPageService() {
		logger.info("parse web pages constructor created.");

	}

	private Element getElement(String elementName) {
		Element element = doc.getElementById(elementName);
		logger.info("temp Element '" + elementName + "' contain: " + element);
		if (element != null) {
			logger.info("Element '" + elementName + "' contain: "
					+ element.ownText());
		}
		return element;
	}

	public String getActualPrice() {
		logger.info("function: getActualPrice.");

		Element element = getElement("BuySellingPrice2");
		String actualPriceString = element.ownText().substring(1);

		logger.info("actualPrice(double) contain: " + actualPriceString);
		return actualPriceString;
	}

	public String getPrice() {
		logger.info("function: getPrice.");
		String priceString = "";
		// Double price;
		Element element = getElement("BuyTicketPrice2");

		if (element == null) {
			logger.info("element do not exists ");
			return "";
		}
		priceString = element.ownText().substring(1);
		// price = Double.parseDouble(priceString);

		logger.info("price(String) contain: " + priceString);
		return priceString;
	}

	public String getProductDescribe() {
		logger.info("function: getProductDescribe.");

		Element element = getElement("rpvInfo");

		String productDescribe = element.text();

		logger.info("(String) productDescribe contain: " + productDescribe);
		return productDescribe;
	}

	public String getActualDate() {
		logger.info("function: getActualDate.");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String actualDate = simpleDateFormat.format(new Date());

		logger.info("(String) actualDate contain: " + actualDate);
		return actualDate;
	}

	public String getProductName() {
		logger.info("function: getProductName.");

		Element element = getElement("ProductName");
		String productName = element.ownText();

		logger.info("(String)productName contain: " + productName);
		return productName;
	}

	public BufferedImage getImage() {
		logger.info("imagesssssssssssssss start.");
		BufferedImage image;

		try {
			Element element = getElement("imgProduct");

			String imgSrc = element.attr("src");
			logger.info("imgSrc: " + imgSrc);

			URL imgUrl = new URL(imgSrc);
			image = ImageIO.read(imgUrl);

			if (image == null) {
				logger.info("image is null");
			}
			logger.info("imgHeight: " + image.getHeight() + ", imgWidth: "
					+ image.getWidth());
		} catch (IOException ex) {
			logger.warn("ex in getImage: " + ex);
			return null;
		}
		logger.info("imagesssssssssssssss end.");
		return image;
	}

	public void setURL(String url) throws IOException {
		this.url = url;
		this.doc = Jsoup.connect(url).get();
	}

	public String getUrl() {
		return this.url;
	}

	public String[] getAllProductData() {

		String allData[] = new String[6];

		allData[0] = getUrl();
		allData[1] = getPrice();
		allData[2] = getActualPrice();
		allData[3] = getActualDate();
		allData[4] = getProductName();
		allData[5] = getProductDescribe();

		//getImage();

		return allData;
	}

	public String[] parseDataFromUrl(String url) throws Exception {

		setURL(url);
		return getAllProductData();
	}

	public double didPriceChanged(String url) throws Exception {
		setURL(url);

		return parseActualPrice(getActualPrice());
	}

	private double parseActualPrice(String actualPrice) {

		return Double.parseDouble(actualPrice);
	}
}
