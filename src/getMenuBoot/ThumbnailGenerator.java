package getMenuBoot;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public  class ThumbnailGenerator {

  /**
   * 生成指定分辨率的缩略图并返回缩略图的路径
   * 
   * @param imagePath  原图片路径
   * @param thumbnailWidth  缩略图宽度
   * @param thumbnailHeight  缩略图高度
   * @param thumbnailPath  缩略图保存路径
   * @return  生成的缩略图路径
   * @throws IOException
   */
  public static String generateThumbnail1(String imagePath, int thumbnailWidth, int thumbnailHeight, String thumbnailPath) throws IOException {
    File imageFile = new File(imagePath);
    if (!imageFile.exists()) {
    	imageFile = new File("d:\\default.png");
    }

    BufferedImage originalImage = ImageIO.read(imageFile);

    // 生成缩略图
    Image thumbnail = originalImage.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH);
    BufferedImage bufferedThumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
    bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);

    // 保存缩略图
    File thumbnailFile = new File(thumbnailPath);
    ImageIO.write(bufferedThumbnail, "jpg", thumbnailFile);

    return thumbnailFile.getAbsolutePath();
  }
}
