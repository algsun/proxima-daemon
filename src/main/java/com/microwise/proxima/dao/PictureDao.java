package com.microwise.proxima.dao;

import com.microwise.proxima.bean.DVPlaceBean;
import com.microwise.proxima.bean.PictureBean;
import com.microwise.proxima.dao.base.BaseDao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图片数据库访问层
 *
 * @author zhang.licong
 * @date 2012-7-6
 */
public interface PictureDao extends BaseDao<PictureBean> {

    /**
     * 根据摄像机点位查询图片信息
     *
     * @param dvPlaceId 摄像机点位ID
     * @author zhang.licong
     * @date 2012-9-17
     */
    public List<PictureBean> findPictures(String dvPlaceId);

    /**
     * 根据摄像机点位查询最新图片信息
     *
     * @param dvPlaceId 摄像机点位ID
     * @author liu.zhu
     * @date 2014-8-18
     */
    public PictureBean findNewPictures(String dvPlaceId);


    /**
     * 根据摄像机查询图片(按照保存时间倒序)
     *
     * @param dvPlaceId
     * @param start
     * @param max
     * @return
     */
    public List<PictureBean> findByDVPlace(int dvPlaceId, int start, int max);

    /**
     * 根据摄像机查询某个时间段内的图片
     *
     * @param dvPlaceId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<PictureBean> findByDVPlaceWithDuration(int dvPlaceId,
                                                       Date startDate, Date endDate);

    /**
     * 根据图片查询对应的摄像机点位
     *
     * @param picId
     * @return
     */
    public DVPlaceBean findDVPlaceByPictureId(String picId);


    /**
     * 根据摄像机点位，查询某个时间之后的 n 张图片
     *
     * @param dvPlaceId 摄像机点位
     * @param dateAfter 时间
     * @param max       最大图片数量
     * @return
     */
    public List<PictureBean> findPicturesAfter(int dvPlaceId, Date dateAfter,
                                               int max);

    public Map<String, Object> findLastPhotoTime(List<? extends DVPlaceBean> dvPlaceBeanList);
}
