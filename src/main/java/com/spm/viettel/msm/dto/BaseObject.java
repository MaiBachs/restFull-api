package com.spm.viettel.msm.dto;

import java.io.Serializable;

public class BaseObject implements Serializable {
    protected Long id;
    protected String name;
    protected String code;
    protected Double x;
    protected Double y;
    protected Long numOfVisit;
    protected String address;
    protected String imgUrl;
    protected String iconUrl;
    protected String imgPath;
    private Double anypay = 0.0d;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Long getNumOfVisit() {
        return numOfVisit;
    }

    public void setNumOfVisit(Long numOfVisit) {
        this.numOfVisit = numOfVisit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getAnypay() {
        return anypay;
    }

    public void setAnypay(Double anypay) {
        this.anypay = anypay;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
