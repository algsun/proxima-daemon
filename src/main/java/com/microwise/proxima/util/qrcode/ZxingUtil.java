package com.microwise.proxima.util.qrcode;

import com.google.common.io.Closeables;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Date: 12-9-26 Time: 下午3:25
 *
 * @author bastengao
 */
public class ZxingUtil {

    public static BinaryBitmap createBinaryBitmap(BufferedImage bufferedImage) {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        return new BinaryBitmap(new HybridBinarizer(source));
    }

    public static Result[] parse(File imageFile) throws IOException, NotFoundException {
        FileInputStream input = new FileInputStream(imageFile);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(input);
        } finally {
            Closeables.close(input, true);
        }

        if(bufferedImage == null){
            return null;
        }
        
        BinaryBitmap bitmap = createBinaryBitmap(bufferedImage);
        MultipleBarcodeReader reader = new QRCodeMultiReader();
        Result[] results = reader.decodeMultiple(bitmap);
        return results;
    }
}
