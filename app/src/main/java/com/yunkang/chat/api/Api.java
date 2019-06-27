package com.yunkang.chat.api;

/**
 * 作者：凌涛 on 2019/1/21 18:19
 * 邮箱：771548229@qq..com
 */
public class Api {
    public static final String testUser = "17628285613";

//    public static final String root = "http://api-health.daanlab.com/v999/pmplatform";//开发
//    public static final String root2 = "http://api-health.daanlab.com/v999/merchant";

//    public static final String root2 = "http://192.168.3.147:9122";
//    public static final String root = "http://192.168.3.147:9121";//本地

    public static final String root = "http://ykapi-cg.yklis.com/v999/pmplatform";//测试地址
    public static final String root2 = "http://ykapi-cg.yklis.com/v999/merchant";

//    public static final String root = "http://api.yunkanghealth.com/v999/pmplatform";//生产
//    public static final String root2 = "http://api.yunkanghealth.com/v999/merchant";

    public static class User {
        public static final String resetPassword=root+"/user/updateAppUserPwd";
        public static final String sendCode=root+"/user/sendBsVerifyCode";
        public static final String userRegister = root + "/user/userRegister";
        public static final String userLoginByCode = root + "/user/userLoginByCode";
        public static final String userLoginByApp = root + "/user/userLoginByApp";
        public static final String updateUserByUser = root + "/user/updateUserByUser";
        public static final String getUserInfoByUsername = root + "/user/getUserInfoByUsername";
        public static final String isHaveUser = root + "/user/isHaveUser";
        public static final String getUserDtoByToken = root + "/user/getUserDtoByToken";
        public static final String uploadImage = root + "/image/uploadImage";
        public static final String uploadOCRImage = root + "/image/uploadOCRImage";

    }
    public static class Polyv{
        public static final String getLiveChannel = root + "/polyv-plchannel/getChannel";
        public static final String getChannelRecordFiles=root+"/polyv-plchannel/getChannelRecordFiles";
        public static final String getChannelViewers=root+"/polyv-plchannel/getChannelViewers";
        public static final String getWhiteList=root+"/polyv-plchannel/getWhiteList";
    }
    public static class Video{
        public static final String getVideoByLabel=root2+"/video/getVideoByLabel";
        public static final String getVideoLabel = root2 + "/video_label/getVideoLabel";
        public static final String getParentVideoComment = root2 + "/video_comment/getParentVideoComment";
        public static final String getSonVideoComment = root2 + "/video_comment/getSonVideoComment";
        public static final String addSonVideoComment = root2 + "/video_comment/addSonVideoComment";
        public static final String addParentVideoComment = root2 + "/video_comment/addParentVideoComment";
        public static final String palyVideo =root2+"/video/palyVideo";

    }
    public static class Order {
        public static final String payment = root + "/pay/payment";
        public static final String delOrderById = root + "/order/delOrderById";
        public static final String getOrderByUser = root + "/order/getOrderByUser";
        public static final String getPDFNum = root + "/order/getPDFNum";
        public static final String getPDFUploadingList = root + "/order/getPDFUploadingList";
        public static final String updateOrderToComplete = root + "/order/updateOrderToComplete";
        public static final String getCouponDetailByUserId = root + "/coupon/getCouponDetailByUserId";
    }
    public static class ContentManage {
        public static final String getContentManageByApp = root + "/contentManage/getContentManageByApp";

    }
    public static class Veision{
        public static final String getVersion=root+"/sys/getSysPar";
    }
    public static class Integral{
        public static final String loginGetIntegral = root + "/integral/loginGetIntegral";
        public static final String registeredGetIntegral = root + "/integral/registeredGetIntegral";
    }
    public static class Message {
        public static final String getPushMassage = root + "/massage/getPushMassage";
        public static final String getNotReadNum = root + "/massage/getNotReadNum";
        public static final String delAllPushMassage = root + "/massage/delAllPushMassage";
    }
    public static class Protocol {
        public static final String getProtocolListByType = root + "/protocol/getProtocolListByType";
        public static final String getProtocolList = root + "/protocol/getProtocolList";
    }
    public static class Product {
        public static final String addToShopCar = root2 + "/shopping_cart/add";
        public static final String getHomeCategoryList = root2 + "/product_category/getList";//获取首页分类
        public static final String getHomeCategoryListWithPtOn = root2 + "/product_info/getListForAppCategoryWithPtOn";//获取首页分类
        public static final String getHomeGoodsList = root2 + "/product_info/getListForFirstPage";//获取首页商品列表，不带灰色价格
        public static final String getListForApp = root2 + "/product_label/getListForApp";
        public static final String getListForAppCategory = root2 + "/product_info/getListForAppCategory";
        public static final String getLabsByProductId = root2 + "/product_info/getLabsByProductId";
        public static final String getHomeGoodsListWithPt=root2+"/product_info/getListForFirstPageWithPtOn";
        public static final String getStatus= root2+"/operator_pt_on/getStatus";
        public static final String turnON= root2+"/operator_pt_on/turnON";
        public static final String turnOff= root2+"/operator_pt_on/turnOff";
        public static final String getMyUsualbuys=root2+"/usual_buy_products/getMyUsualbuys";
        public static final String getListForFirstSearch= root2+"/product_info/getListForFirstSearch";
        public static final String getListForFirstSearchWithPtOn= root2+"/product_info/getListForFirstSearchWithPtOn";
        public static final String getSearchHotWords= root2+"/product_info/getSearchHotWords";
        public static final String getMyCollections= root2+"/my_collections/getMyCollections";
        public static final String deleteCollect= root2+"/my_collections/repeal";
    }
}
