package getMenuBoot;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Game {
	private String fileName;
	private String name;
	private String sourcename;
	private String imagePath;
	private String thumbnailPath;

	public Game(String sourcename,String fileName, String name, String imagePath) throws IOException {
		this.sourcename = sourcename;
		this.fileName = fileName;
		this.name = name;
		this.imagePath = imagePath;
//		this.thumbnailPath = generateThumbnail(imagePath);
	}

	public URL getThumbnailPath() throws MalformedURLException {
		// TODO Auto-generated method stub
		if (thumbnailPath == null || thumbnailPath == "") {
			thumbnailPath = PDFGenerator.defaultImg;
		}
		return new URL("file:///" + thumbnailPath);
	}

	public String generateThumbnail(String imagePath) throws IOException {
		// TODO: 生成缩略图，压缩分辨率到120x80像素，返回缩略图路径
		File f = new File(imagePath);
		return ThumbnailGenerator.generateThumbnail1(imagePath, 120, 80, "d:\\test1\\" + f.getName());
	}

	public String getFileName() {
		return fileName;
	}

	public String getName() {
		return name;
	}

	public String getImagePath() {
		if(!new File(imagePath).exists()) {
			imagePath = PDFGenerator.defaultImg;
		}
		return imagePath;
	}


	public String getSourcename() {
		return sourcename;
	}
}