package com.microwise.proxima.util.ocr;

import com.google.common.io.CharStreams;
import com.microwise.proxima.util.FileUtil;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * Date: 12-8-23 Time: 下午3:14
 * 
 * @author bastengao
 */
public class TesseractOCR {

	public static final String TESSERACT_OCR_PATH ="tesseract-ocr/tesseract.exe";

	/**
	 * 解析图片中的文字
	 * 
	 * @param imagePath 图片路径
	 * @return
	 * @throws java.io.IOException
	 * @throws InterruptedException
	 * @throws IllegalArgumentException
	 *             if error when parsing
	 */
	public static String recognize(String imagePath) throws IOException,
			InterruptedException {
		String tesseractPath = FileUtil.filePathInClasspath(TESSERACT_OCR_PATH);
		String imagePath2 = new File(imagePath).getAbsolutePath();

		String outputFileName = UUID.randomUUID().toString(); // 随机一个输出文件名

		ProcessBuilder processBuilder = null;
		
		//支持两个系统，windows系统和linux系统
		if (OS.isWindows()) {
			processBuilder = new ProcessBuilder(tesseractPath, imagePath2,
					outputFileName);
		} else if (OS.isUnix()) {
			processBuilder = new ProcessBuilder("tesseract", imagePath2,
					outputFileName);
		}

		// 当前目录必须要是 "tesseract.exe" 的父目录，如果在 windows 平台上。
		// Linux 平台无所谓
		File currentDir = new File(new File(tesseractPath).getParent());
		processBuilder.directory(currentDir);

		Process process = processBuilder.start();
		if (process.waitFor() == 0) { // if success
			File outputFile = new File(currentDir, outputFileName + ".txt");
			FileReader reader = new FileReader(outputFile);
			String result = CharStreams.toString(reader);
			reader.close();

			outputFile.delete(); // 删除临时的结果文件

			return result.trim();
		} else { // if failed
			// 打印命令输出
			InputStream in = process.getInputStream();
			throw new IllegalArgumentException(
					CharStreams.toString(new InputStreamReader(in)));
		}
	}

	/**
     * 通过 tiff 格式图片识别
     *
     * @param bufferedImage
     * @return
     * @throws ImageWriteException
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    public static String recognize(BufferedImage bufferedImage) throws ImageWriteException, IOException, InterruptedException {
        return recognizeByTiff(bufferedImage);
    }

    /**
     * 通过 tiff 格式图片识别
     *
     * @param bufferedImage
     * @return
     * @throws java.io.IOException
     * @throws ImageWriteException
     * @throws InterruptedException
     */
    public static String recognizeByTiff(BufferedImage bufferedImage) throws IOException, ImageWriteException, InterruptedException {
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "tif");
        tempFile.deleteOnExit();
        toTiff(bufferedImage, tempFile);

        String topText = recognize(tempFile.getAbsolutePath());
        tempFile.delete();
        return topText;
    }


    public static void toTiff(BufferedImage image, File file) throws IOException, ImageWriteException {
        Sanselan.writeImage(image,file, ImageFormat.IMAGE_FORMAT_TIFF, null);
    }
	
}
