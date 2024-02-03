package com.spm.viettel.msm.service;

import com.spm.viettel.msm.controller.MapObjectAttributesController;
import com.spm.viettel.msm.repository.sm.entity.MapObjectInfoImage;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapObjectInfoImageService {

    private final Logger loggerFactory = LoggerFactory.getLogger(MapObjectAttributesController.class);

    @Value("${FILE_IMAGE_PATH}")
    private String fileImagePath;


    public List<String> getImagesBase64(Session sessionSM, Long mapObjectInfoId) {
        List<MapObjectInfoImage> listImage = getImages(sessionSM, mapObjectInfoId);
        List<String> imagesBase64 = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(listImage)) {
            for (MapObjectInfoImage image : listImage) {
                File imageFile = new File(this.fileImagePath + File.separator + image.getPath());
                if (imageFile.exists() && imageFile.isFile()) {
                    if (imageFile.exists()) {
                        String encodedFile = null;
                        try {
                            FileInputStream fileInputStreamReader = new FileInputStream(imageFile);
                            byte[] bytes = new byte[(int) imageFile.length()];
                            fileInputStreamReader.read(bytes);
                            encodedFile = new String(Base64.encodeBase64(bytes), "UTF-8");
                            if (StringUtils.isNotEmpty(encodedFile) && StringUtils.isNotBlank(encodedFile)) {
                                imagesBase64.add(encodedFile);
                            }
                        } catch (FileNotFoundException e) {
                            loggerFactory.error(e.getMessage());
                        } catch (IOException e) {
                            loggerFactory.error(e.getMessage());
                        }
                    } else {
                        loggerFactory.error(String.format("File: %s not exist!", imageFile.getAbsolutePath()));
                    }
                }
            }
        }
        return imagesBase64;
    }

    public static List<MapObjectInfoImage> getImages(Session sessionSM, Long mapObjectInfoId) {
        String hql = "FROM MapObjectInfoImage a WHERE a.mapObjectInfoId =:mapObjectInfoId order by a.orderBy asc";
        Query query = sessionSM.createQuery(hql);
        query.setParameter("mapObjectInfoId", mapObjectInfoId);
        List<MapObjectInfoImage> listImage = query.list();
        return listImage;
    }
}
