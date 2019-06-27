package com.yunkang.chat.start.model;

/**
 * 作者：凌涛 on 2019/1/19 19:29
 * 邮箱：771548229@qq..com
 */
public class Medicine {

    /**
     * coverImages : string
     * createDate : 2019-01-23T02:06:35.638Z
     * id : 0
     * merchantId : 0
     * name : string
     * pCategory1Id : 0
     * pCategory2Id : 0
     * pCategory3Id : 0
     * pCategoryId : 0
     * pCategoryRank : 0
     * pLableId1 : 0
     * pLableId2 : 0
     * pLableId3 : 0
     * pLableName1 : string
     * pLableName2 : string
     * pLableName3 : string
     * promotionStatus : 0
     * ptPrice : 0
     * recommendStatus : 0
     * retailPrice : 0
     * salesVolume : 0
     * supplierId : 0
     */



    public String getCoverImages() {
        return coverImages;
    }

    public void setCoverImages(String coverImages) {
        this.coverImages = coverImages;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpCategory1Id() {
        return pCategory1Id;
    }

    public void setpCategory1Id(String pCategory1Id) {
        this.pCategory1Id = pCategory1Id;
    }

    public String getpCategory2Id() {
        return pCategory2Id;
    }

    public void setpCategory2Id(String pCategory2Id) {
        this.pCategory2Id = pCategory2Id;
    }

    public String getpCategory3Id() {
        return pCategory3Id;
    }

    public void setpCategory3Id(String pCategory3Id) {
        this.pCategory3Id = pCategory3Id;
    }

    public String getpCategoryId() {
        return pCategoryId;
    }

    public void setpCategoryId(String pCategoryId) {
        this.pCategoryId = pCategoryId;
    }

    public int getpCategoryRank() {
        return pCategoryRank;
    }

    public void setpCategoryRank(int pCategoryRank) {
        this.pCategoryRank = pCategoryRank;
    }

    public String getpLableId1() {
        return pLableId1;
    }

    public void setpLableId1(String pLableId1) {
        this.pLableId1 = pLableId1;
    }

    public String getpLableId2() {
        return pLableId2;
    }

    public void setpLableId2(String pLableId2) {
        this.pLableId2 = pLableId2;
    }

    public String getpLableId3() {
        return pLableId3;
    }

    public void setpLableId3(String pLableId3) {
        this.pLableId3 = pLableId3;
    }

    public String getpLableName1() {
        return pLableName1;
    }

    public void setpLableName1(String pLableName1) {
        this.pLableName1 = pLableName1;
    }

    public String getpLableName2() {
        return pLableName2;
    }

    public void setpLableName2(String pLableName2) {
        this.pLableName2 = pLableName2;
    }

    public String getpLableName3() {
        return pLableName3;
    }

    public void setpLableName3(String pLableName3) {
        this.pLableName3 = pLableName3;
    }

    public int getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(int promotionStatus) {
        this.promotionStatus = promotionStatus;
    }

    public double getPtPrice() {
        return ptPrice;
    }

    public void setPtPrice(double ptPrice) {
        this.ptPrice = ptPrice;
    }

    public int getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(int recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    private String coverImages;
    private String createDate;
    private String id;
    private String merchantId;
    private String name;
    private String pCategory1Id;
    private String pCategory2Id;
    private String pCategory3Id;
    private String pCategoryId;
    private int pCategoryRank;
    private String pLableId1;
    private String pLableId2;
    private String pLableId3;
    private String pLableName1;
    private String pLableName2;
    private String pLableName3;
    private int promotionStatus;
    private double ptPrice;
    private int recommendStatus;
    private double retailPrice;
    private int salesVolume;
    private String supplierId;
    private  boolean isHide;
    private boolean isShowPt;

    public boolean isShowPt() {
        return isShowPt;
    }

    public void setShowPt(boolean showPt) {
        isShowPt = showPt;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }
}

