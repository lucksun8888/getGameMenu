package getMenuBoot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.io.SAXReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFGenerator {
	static String water;
	static String defaultImg;
	static String outPut;
	public static void main(String[] args) throws IOException, org.dom4j.DocumentException {
		String directoryPath = args[0];
		outPut = args[1];
		defaultImg = args[2];
		water = args[3];
		File directory = new File(directoryPath);
		if (!directory.isDirectory()) {
			System.out.println("指定路径不是一个目录！");
			return;
		}
		List<Path> xmlFilePaths = getXmlFilePaths(directoryPath);
		List<Game> games = new ArrayList<>();

		for (Path xmlFilePath : xmlFilePaths) {
			List<Game> gamesInFile = parseXML(xmlFilePath);
			if(gamesInFile!=null)
			games.addAll(gamesInFile);
		}

		try {
			generatePDF(directory.getName(), games, outPut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Path> getXmlFilePaths(String dirPath) throws IOException {
		List<Path> xmlFilePaths = new ArrayList<>();
		Files.walkFileTree(Paths.get(dirPath), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().equals("gamelist.xml")) {
					xmlFilePaths.add(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return xmlFilePaths;
	}

	private static List<Game> parseXML(Path xmlFilePath) throws IOException, org.dom4j.DocumentException {
		List<Game> gl = new ArrayList<Game>();

		// TODO: 解析xml文件，获取所有标签为name、image的值，返回Game对象列表
		String folderName = xmlFilePath.getParent().getFileName().toString(); // 模拟器名称
		String folderDir = xmlFilePath.getParent().toAbsolutePath().toString();
		// 创建解析器
	    SAXReader saxReader = new SAXReader();
	    // 得到document
	    org.dom4j.Document document = null;
	    try {
	    	document = saxReader.read(xmlFilePath.toAbsolutePath().toString());
	    }catch(Exception e) {
	    	return null;
	    }
	    // 得到根节点
	    org.dom4j.Element root = document.getRootElement();

	    // 得到p1
	    @SuppressWarnings("unchecked")
		List<org.dom4j.Element> list = root.elements("game");
	    // 遍历list
	    for (org.dom4j.Element element : list) {
	        // element是每一个p1元素
	        // 得到p1下面的name元素
	    	String name = element.element("name").getStringValue();
	    	String image = element.element("image")==null?"":element.element("image").getStringValue();
	    	String path = element.element("path").getStringValue();
	    	if(image.startsWith(".")) {
	    		image = folderDir + image.substring(1);
	    		
	    	}
	    	if(path.startsWith(".")) {
	    		path = folderDir + path.substring(1);
	    	}
	        // 得到name里面的值
	    	if(new File(path).exists()) {
	        Game gm = new Game(path,folderName.toString(),name.toString(),image.toString());
	        gl.add(gm);}
	    }
		
		return gl;
	}

	private static void generatePDF(String directoryName, List<Game> games, String outputFilePath)
			throws DocumentException, IOException {

		Document document = new Document();

		// 使用微软雅黑字体显示中文
		String fontName = "C:\\Windows\\Fonts\\simsun.ttc";
		fontName += ",0";
		Font cFont = new Font(BaseFont.createFont(fontName, BaseFont.IDENTITY_H, true));// 中文简体

		
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFilePath));
		document.open();
		PdfPTable table = new PdfPTable(5);
		table.setTotalWidth(520);
		// 添加目录名称
		PdfPCell directoryCell = new PdfPCell(new Paragraph("batocera菜单-自动生成by烟魔",cFont));
		directoryCell.setColspan(5);
		directoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(directoryCell);

		// 添加表头
		PdfPCell ccell = new PdfPCell(new Paragraph("模拟器/机型", cFont));
		table.addCell(ccell);
		ccell = new PdfPCell(new Paragraph("序号", cFont));
		table.addCell(ccell);
		ccell = new PdfPCell(new Paragraph("游戏名称", cFont));
		table.addCell(ccell);
		ccell = new PdfPCell(new Paragraph("完全路径", cFont));
		table.addCell(ccell);
		ccell = new PdfPCell(new Paragraph("游戏缩略图", cFont));
		table.addCell(ccell);
		document.open();  //打开文档
		Image tImgCover = Image.getInstance(water);
		   /* 设置图片的位置 */
		   tImgCover.setAbsolutePosition(0, 0);
		   /* 设置图片的大小 */
		   tImgCover.scaleAbsolute(595, 842);
		   document.add(tImgCover);             //加载图片
		
		// 添加游戏信息 9个一页 分页
		int i = 0;
		long no =0;
		for (Game game : games) {
			no++;
			table.addCell(game.getFileName());
			table.addCell(String.format("%0"+6+"d",no)
					// 0 代表前面补充0
					// 4 代表长度为4
					// d 代表参数为正数型
);
			ccell = new PdfPCell(new Paragraph(game.getName(), cFont));
			table.addCell(ccell);
			ccell = new PdfPCell(new Paragraph(game.getSourcename(), cFont));
			table.addCell(ccell);
			Image thumbnail = Image.getInstance(game.getImagePath());
			thumbnail.scaleToFit(80, 60);
			PdfPCell thumbnailCell = new PdfPCell(thumbnail);
			thumbnailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			thumbnailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			thumbnailCell.setPadding(5);
			table.addCell(thumbnailCell);
			
			i++;
			if(i==10) {
				i=0;
				PdfContentByte canvas = writer.getDirectContent();
				table.writeSelectedRows(0, -1, document.leftMargin(), document.top() - 20, canvas);
				document.newPage();
				document.add(tImgCover);             //加载图片
				table.deleteBodyRows();
				// 添加目录名称
				directoryCell = new PdfPCell(new Paragraph("batocera菜单-自动生成by烟魔",cFont));
				directoryCell.setColspan(5);
				directoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(directoryCell);

				// 添加表头
				ccell = new PdfPCell(new Paragraph("模拟器/机型", cFont));
				table.addCell(ccell);
				ccell = new PdfPCell(new Paragraph("序号", cFont));
				table.addCell(ccell);
				ccell = new PdfPCell(new Paragraph("游戏名称", cFont));
				table.addCell(ccell);
				ccell = new PdfPCell(new Paragraph("完全路径", cFont));
				table.addCell(ccell);
				ccell = new PdfPCell(new Paragraph("游戏缩略图", cFont));
				table.addCell(ccell);
				
			}
		}
		document.close();
	}

}
