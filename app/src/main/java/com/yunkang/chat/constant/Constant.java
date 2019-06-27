package com.yunkang.chat.constant;

/**
 * 作者：凌涛 on 2019/2/27 17:00
 * <p>
 * 邮箱：771548229@qq..com
 */
public class Constant {
    //public static final String root ="http://47.106.233.240:8090";//
    //public static final String root ="http://192.168.3.34:6334/originProject/H5";//本地
    //public static final String root="http://192.168.1.103:3300/pmplatform-app";
    //public static final String root ="http://app-doctor.daanlab.com/pmp-yk-app";//开发
    //public static final String root ="http://appdoctor-cg.yklis.com/jzyl-pmp-app";//测试
    //public static final String root = "http://app.yunkangdoctor.com/pmplatform-app";//生产
      public static final String root = "http://appdoctor-cg.yklis.com/pmplatform-app";//旧测试
    //public static final String root1 = "http://app-doctor.daanlab.com/pmp-app";//开发
//    public static final String root1 ="http://appdoctor-cg.yklis.com/pmp-app";//测试
    public static final String root1 = "http://app.yunkangdoctor.com/pmp-app";//生产

    public static class Commom {
        public static final String advince = root + "/View/advince/advince.html?token=";//反馈
        public static final String shopCar = root + "/View/toCar/index.html?token=";//购物车
        public static final String goodsDetail = root + "/View/mall/index.html?token=";//商品详情
        public static final String device = root + "/View/Index/index.html?token=";//认证/报备
        public static final String coupon = root + "/View/coupon/index.html?token=";//优惠劵
        public static final String address = root + "/View/address/addManage.html?token=";//地址管理
        public static final String cash = root + "/View/cash/remain.html?token=";//余额
        public static final String invoince = root + "/View/invoince/index.html?token=";//开票
        public static final String callLogister = root + "/View/callLogister/callLogister.html?token=";//呼叫物流
        public static final String orderDetail = root + "/View/orders/orderDetail.html?token=";//订单详情
        public static final String reportDetail = root + "/View/orders/reportDetail.html?token=";//报告详情
        public static final String orderDiscuss = root + "/View/orders/orderDiscuss.html?token=";//评价
        public static final String realName = root + "/View/personalInfo/reportName.html?token=";//实名认证
        public static final String bulu = root + "/View/orders/entering.html?token=";//补录
        public static final String integration = root + "/View/cash/integration.html?token=";//积分
        public static final String payOrder = root + "/View/orders/payOrder.html?secondPay=1&token=";//付款
        public static final String bandlist = root + "/View/personalInfo/bandlist.html?token=";//银行卡
        public static final String bandZFB = root + "/View/personalInfo/bandZFB.html?token=";//支付宝
        public static final String inviteCode = root + "/View/invite/inviteCode.html?token=";//邀请码
        public static final String invinteList = root + "/View/invite/invinteList.html?token=";//邀请详情
        public static final String disDetail = root + "/View/mall/disDetail.html?token=";//评论详情
        public static final String newDetail = root + "/View/news/index.html?";//认证详情
        public static final String backDetail = root + "/View/orders/backDetail.html?token=";//认证详情
        public static final String orderBack = root + "/View/orders/orderBack.html?token=";//认证详情
        public static final String about = root + "/View/advince/aboutUs.html?token=";//关于我们
        public static final String myQrcode = root1 + "/view/qrCode/buildCode.html?custId=";//二维码
    }

}
