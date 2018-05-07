package com.microwise.proxima.imagesync.ext;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.microwise.proxima.bean.*;
import com.microwise.proxima.beans2.IdentityCode;
import com.microwise.proxima.service.MarkSegmentPositionService;
import com.microwise.proxima.service.MarkSegmentService;
import com.microwise.proxima.service2.IdentityCodeService;
import com.microwise.proxima.util.Configs;
import com.microwise.proxima.util.IdUtil;
import com.microwise.proxima.util.PositionUtil;
import com.microwise.proxima.util.qrcode.ZxingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 光学 QR 图片
 * Date: 12-9-26 Time: 下午8:44
 *
 * @author bastengao
 */
@Component
@Scope("prototype")
public class OpticsQRCodeImageSyncListener implements ImageSyncListener {
    private static final Logger log = LoggerFactory.getLogger(OpticsQRCodeImageSyncListener.class);

    @Autowired
    private MarkSegmentService markSegmentService;
    @Autowired
    private MarkSegmentPositionService markSegmentPositionService;

    @Autowired
    private IdentityCodeService identityCodeService;

    @Override
    public void onImageSync(File newImage, DVType dvType, DVPlaceBean dvPlace, PictureBean picture) {
        // 如果不是光学图片，直接返回
        if (dvType != DVType.OPTICS) {
            return;
        }

        try {
            String temp = Configs.get("proxima.images.ignoreFileLength");
            if (Strings.isNullOrEmpty(temp)) {
                throw new IllegalArgumentException("proxima.images.ignoreFileLength 配置错误");
            }
            long ignoreFileLength = Long.parseLong(temp);
            // 文件小于限制大小则不解析二维码
            if (newImage.length() / 1024 < ignoreFileLength) {
                log.info(picture.getName() + "小于最小文件限制,bu");
                return;
            }

            log.debug("optics image: {}/{}", picture.getPath(), picture.getName());
            Result[] results = ZxingUtil.parse(newImage);
            if (results == null) {
                return;
            }
            log.info("qrcodes:{}", results.length);

            saveIdentityCodes(picture, results);

            saveOrUpdateLineGroup(dvPlace, picture, results);
        } catch (IOException e) {
            log.error("解析二维码", e);
        } catch (NotFoundException e) {
            //如果没有找到二维码, do nothing
        }
    }

    /**
     * 保存二维码信息
     *
     * @param picture
     * @param results
     */
    private void saveIdentityCodes(PictureBean picture, Result[] results) {
        for (Result result : results) {
            ResultPoint[] resultPoints = result.getResultPoints();
            //二维码 有且只有三个点
            if (resultPoints.length == 3) {
                saveIdentityCode(picture, result);
            }

            log.info("text:[{}]", result.getText());
        }
    }

    /**
     * 保存一个二维码
     *
     * @param picture
     * @param result
     */
    private void  saveIdentityCode(PictureBean picture, Result result) {
        IdentityCode identityCode = new IdentityCode();
        identityCode.setPicture(picture);
        identityCode.setText(result.getText());

        ResultPoint[] resultPoints = result.getResultPoints();
        identityCode.setFirstPoint(new Point((int) resultPoints[0].getX(), (int) resultPoints[0].getY()));
        identityCode.setSecondPoint(new Point((int) resultPoints[1].getX(), (int) resultPoints[1].getY()));
        identityCode.setThirdPoint(new Point((int) resultPoints[2].getX(), (int) resultPoints[2].getY()));
        identityCodeService.save(identityCode);
    }

    /**
     * 保存或者更新 线组
     *
     * @param picture
     * @param results
     */
    private void saveOrUpdateLineGroup(DVPlaceBean dvPlace, PictureBean picture, Result[] results) {
        //将相同组的二维码归类
        Map<String, Collection<Result>> groupMap = groupIdentityCodes(results);

        detectLineGroup(dvPlace, picture, groupMap);
    }


    /**
     * 将相同组的二维码归类
     * <p>
     * groupName:String(组名) => results:Collection(二维码结果)
     *
     * @param results
     * @return
     */
    private Map<String, Collection<Result>> groupIdentityCodes(Result[] results) {
        // groupName => identityCodes
        Multimap<String, Result> groupMap = ArrayListMultimap.create();

        for (Result result : results) {
            String text = result.getText();
            Iterable<String> segmentsIt = Splitter.on('-').omitEmptyStrings().trimResults().split(text);
            List<String> segments = Lists.newArrayList(segmentsIt);

            //文本必须是 "A-1" 的形式
            if (segments.size() == 2) {
                log.debug("found microwise qrcode:[{}]", text);

                String groupName = segments.get(0);
                groupMap.put(groupName, result);
            }
        }
        return groupMap.asMap();
    }

    /**
     * 检测 线
     *
     * @param dvPlace
     * @param picture
     * @param groupMap
     */
    private void detectLineGroup(DVPlaceBean dvPlace, PictureBean picture, Map<String, Collection<Result>> groupMap) {
        for (String groupName : groupMap.keySet()) {
            log.debug("\n");
            log.debug("line group:[{}]", groupName);

            Collection<Result> groupResults = groupMap.get(groupName);
            for (Result result : groupResults) {
                log.debug("line point:[{}]", result.getText());
            }

            /**
             *  如果是两个，才符合线的要求（两点一线）
             *  TODO 这里的判断有些严格.
             *  可能会出现以下情况:
             *  1.'B-1', 'B-2' 是新加的一组, 那么会被顺利的判断为一组, 并且保存到数据库中.
             *  但是如果再加一个 'B-3', 那么之后的每一张图片 'B' 组都有 3 个标识码,
             *  'B' 组不会被认为是线, 'B-1' 和 'B-2' 的变化也就不会被同步了
             *
             *  2.暂时还没想好
             */
            if (groupResults.size() == 2) {

                /**
                 * 根据组名，查找对应的组.
                 * 假定每一组只能是一个形状，不会变化。如果 'A' 组是 线，那么就只能是线.
                 */
                MarkSegmentBean markSegment = markSegmentService.findByName(dvPlace.getId(), groupName);

                QRMarkSegmentBean qrMarkSegment = null;
                //如果不存在，则创建
                if (markSegment == null) {
                    qrMarkSegment = createQrMarkSegment(dvPlace, groupName, groupResults);
                } else {
                    if (!(markSegment instanceof QRMarkSegmentBean)) {
                        log.debug("markSegment name conflict: {}", groupName);
                        continue;
                    }
                    log.debug("group exists");
                    qrMarkSegment = (QRMarkSegmentBean) markSegment;
                }

                //如果完全匹配，则更新标识码的位置
                if (isMatch(qrMarkSegment, groupResults)) {
                    saveMarkSegmentPosition(picture, qrMarkSegment, groupResults);
                }
            }

            log.debug("\n");
        }
    }

    /**
     * 创建并保存二维码标记段
     *
     * @param dvPlace
     * @param groupName
     * @param groupResults
     * @return
     */
    private QRMarkSegmentBean createQrMarkSegment(DVPlaceBean dvPlace, String groupName, Collection<Result> groupResults) {
        log.debug("qr markSegment not exists");

        QRMarkSegmentBean qrMarkSegment = new QRMarkSegmentBean();
        qrMarkSegment.setId(IdUtil.get64UUID());
        qrMarkSegment.setName(groupName);
        qrMarkSegment.setDvPlace(dvPlace);
        qrMarkSegment.setCreateTime(new Date());

        Result[] groupResultArray = groupResults.toArray(new Result[0]);
        qrMarkSegment.setTextA(groupResultArray[0].getText());
        qrMarkSegment.setTextB(groupResultArray[1].getText());

        markSegmentService.save(qrMarkSegment);

        log.debug("create qr markSegment");
        return qrMarkSegment;
    }


    /**
     * 是否完全匹配
     *
     * @param qrMarkSegment
     * @param groupResults
     * @return
     */
    private boolean isMatch(QRMarkSegmentBean qrMarkSegment, Collection<Result> groupResults) {
        Set<String> texts = Sets.newHashSet(qrMarkSegment.getTextA(), qrMarkSegment.getTextB());

        Result[] groupResultArray = groupResults.toArray(new Result[groupResults.size()]);
        Set<String> texts2 = Sets.newHashSet(groupResultArray[0].getText(), groupResultArray[1].getText());

        Sets.SetView<String> difference = Sets.symmetricDifference(texts, texts2);
        return difference.isEmpty();
    }

    /**
     * 保存二维码坐标信息
     *
     * @param picture
     * @param markSegment
     * @param groupResults
     */
    private void saveMarkSegmentPosition(PictureBean picture, QRMarkSegmentBean markSegment, Collection<Result> groupResults) {
        QRMarkSegmentPositionBean qrMarkSegmentPosition = new QRMarkSegmentPositionBean();

        qrMarkSegmentPosition.setId(IdUtil.get64UUID());
        qrMarkSegmentPosition.setPicture(picture);
        qrMarkSegmentPosition.setMarkSegment(markSegment);
        qrMarkSegmentPosition.setPicSaveTime(picture.getSaveTime());

        qrMarkSegmentPosition.setUpdateTime(new Date());

        for (Result result : groupResults) {
            Point position = positionOfQRCode(result);
            QRCodePosition qrCodePosition = createQRCodePosition(result);
            // A 点
            if (markSegment.getTextA().equals(result.getText())) {
                qrMarkSegmentPosition.setPositionX(position.getX());
                qrMarkSegmentPosition.setPositionY(position.getY());
                qrMarkSegmentPosition.setQrCodeA(qrCodePosition);

            }
            // B 点
            else if (markSegment.getTextB().equals(result.getText())) {
                qrMarkSegmentPosition.setPositionX2(position.getX());
                qrMarkSegmentPosition.setPositionY2(position.getY());
                qrMarkSegmentPosition.setQrCodeB(qrCodePosition);
            }
        }

        // 标记段长度单位选择的问题. 如果是 mm(毫米) 可以直接拿来显示图表，但图片机的实景宽度一旦变化需要重新计算,
        // 而且在计算的时候不一定提供了图片的实景宽度(用户还未录入); 如果是 px(像素) 这里就直接可以保存了，但显示图表时,
        // 需要根据图片的实景宽度实时计算, 如果图片的实景宽度变化不需要重新计算。 @gaohui 2013-06-13

        qrMarkSegmentPosition.setMarkLength(length(qrMarkSegmentPosition));
        MarkSegmentPositionBean msBefore = markSegmentPositionService.findLatestBefore(markSegment.getId(), picture.getId());
        if (msBefore == null) {
            qrMarkSegmentPosition.setLengthDelta(0);
        } else {
            float lengthDelta = Math.abs(qrMarkSegmentPosition.getMarkLength() - msBefore.getMarkLength());
            qrMarkSegmentPosition.setLengthDelta(PositionUtil.roundFloat(lengthDelta, 2));
        }
        markSegmentPositionService.save(qrMarkSegmentPosition);
    }

    /**
     * 返回二维码的中心. 暂时取第 0 点与第 2 点的中心
     *
     * @param result
     * @return
     */
    private static Point positionOfQRCode(Result result) {
        ResultPoint[] points = result.getResultPoints();
        int x = (int) ((points[0].getX() + points[2].getX()) / 2);
        int y = (int) ((points[0].getY() + points[2].getY()) / 2);
        return new Point(x, y);
    }

    /**
     * 创建二维码坐标对象
     *
     * @param result
     * @return
     */
    private static QRCodePosition createQRCodePosition(Result result) {
        ResultPoint[] points = result.getResultPoints();
        QRCodePosition qrCodePosition = new QRCodePosition();
        qrCodePosition.setPoint0(point(points[0]));
        qrCodePosition.setPoint1(point(points[1]));
        qrCodePosition.setPoint2(point(points[2]));

        return qrCodePosition;
    }

    /**
     * 将 ResultPoint 转换为 Point
     *
     * @param point
     * @return
     */
    private static Point point(ResultPoint point) {
        return new Point((int) point.getX(), (int) point.getY());
    }

    /**
     * 计算标记段的长度(像素px)
     *
     * @param msp
     * @return
     */
    @VisibleForTesting
    public static float length(MarkSegmentPositionBean msp) {
        double length = Math.hypot((msp.getPositionX() - msp.getPositionX2()), (msp.getPositionY() - msp.getPositionY2()));
        return PositionUtil.roundFloat(length, 2);
    }
}

