package com.yunkang.chat.mine.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {


    /**
     * applyOrderPhoto : string
     * balance : 0
     * cashBack : 0
     * cashBackDate : 2019-01-23T08:31:53.698Z
     * cashBackStatus : string
     * cashInputErrorMsg : string
     * cashInputStatus : string
     * coupons : 0
     * couponsPrice : 0
     * createBy : 0
     * createDate : 2019-01-23T08:31:53.698Z
     * custId : string
     * id : 0
     * invoiceStatus : string
     * isMatch : string
     * openId : string
     * orderProducts : [{"balance":0,"couponsPrice":0,"createBy":0,"createDate":"2019-01-23T08:31:53.698Z","id":0,"laboratory":"string","laboratoryAddress":"string","merchantName":"string","orderId":0,"orderSonId":0,"productId":0,"productName":"string","productPrice":0,"ptPrice":0,"ptStatus":"string","realPrice":0,"refundType":"string","remarks":"string","supplierName":"string","updateBy":0,"updateDate":"2019-01-23T08:31:53.698Z"}]
     * orderRefundType : string
     * patientIdNumber : string
     * patientName : string
     * patientTelephone : string
     * payState : string
     * payTime : 2019-01-23T08:31:53.698Z
     * paymentInstanceCode : string
     * paymentMethod : string
     * productNum : 0
     * ptPrice : 0
     * ptStatus : string
     * realPrice : 0
     * refundPrice : 0
     * remarks : string
     * reportDepartment : string
     * reportHospital : string
     * sonOrderNum : 0
     * totalPrice : 0
     * tradeOrder : 0
     * updateBy : 0
     * updateDate : 2019-01-23T08:31:53.698Z
     * userAccount : string
     * userType : string
     */

    private String applyOrderPhoto;
    private double balance;
    private double cashBack;
    private String cashBackDate;
    private String cashBackStatus;
    private String cashInputErrorMsg;
    private String cashInputStatus;
    private String coupons;
    private double couponsPrice;
    private String createBy;
    private String createDate;
    private String custId;
    private String id;
    private String invoiceStatus;
    private String isMatch;
    private String openId;
    private String orderRefundType;
    private String patientIdNumber;
    private String patientName;
    private String patientTelephone;
    private String payState;
    private String payTime;
    private String paymentInstanceCode;
    private String paymentMethod;
    private int productNum;
    private double ptPrice;
    private String ptStatus;
    private double realPrice;
    private double refundPrice;
    private String remarks;
    private String reportDepartment;
    private String reportHospital;
    private int sonOrderNum;
    private double totalPrice;
    private int tradeOrder;
    private String updateBy;
    private String updateDate;
    private String userAccount;
    private String userType;
    @SerializedName("orderLogisticsApply")
    OrderLogisticsApply mOrderLogisticsApply;

    public OrderLogisticsApply getOrderLogisticsApply() {
        return mOrderLogisticsApply;
    }

    public void setOrderLogisticsApply(OrderLogisticsApply orderLogisticsApply) {
        mOrderLogisticsApply = orderLogisticsApply;
    }

    private List<OrderProductsBean> orderProducts;

    public String getApplyOrderPhoto() {
        return applyOrderPhoto;
    }

    public void setApplyOrderPhoto(String applyOrderPhoto) {
        this.applyOrderPhoto = applyOrderPhoto;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCashBack() {
        return cashBack;
    }

    public void setCashBack(double cashBack) {
        this.cashBack = cashBack;
    }

    public String getCashBackDate() {
        return cashBackDate;
    }

    public void setCashBackDate(String cashBackDate) {
        this.cashBackDate = cashBackDate;
    }

    public String getCashBackStatus() {
        return cashBackStatus;
    }

    public void setCashBackStatus(String cashBackStatus) {
        this.cashBackStatus = cashBackStatus;
    }

    public String getCashInputErrorMsg() {
        return cashInputErrorMsg;
    }

    public void setCashInputErrorMsg(String cashInputErrorMsg) {
        this.cashInputErrorMsg = cashInputErrorMsg;
    }

    public String getCashInputStatus() {
        return cashInputStatus;
    }

    public void setCashInputStatus(String cashInputStatus) {
        this.cashInputStatus = cashInputStatus;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public double getCouponsPrice() {
        return couponsPrice;
    }

    public void setCouponsPrice(double couponsPrice) {
        this.couponsPrice = couponsPrice;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getIsMatch() {
        return isMatch;
    }

    public void setIsMatch(String isMatch) {
        this.isMatch = isMatch;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderRefundType() {
        return orderRefundType;
    }

    public void setOrderRefundType(String orderRefundType) {
        this.orderRefundType = orderRefundType;
    }

    public String getPatientIdNumber() {
        return patientIdNumber;
    }

    public void setPatientIdNumber(String patientIdNumber) {
        this.patientIdNumber = patientIdNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientTelephone() {
        return patientTelephone;
    }

    public void setPatientTelephone(String patientTelephone) {
        this.patientTelephone = patientTelephone;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPaymentInstanceCode() {
        return paymentInstanceCode;
    }

    public void setPaymentInstanceCode(String paymentInstanceCode) {
        this.paymentInstanceCode = paymentInstanceCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public double getPtPrice() {
        return ptPrice;
    }

    public void setPtPrice(double ptPrice) {
        this.ptPrice = ptPrice;
    }

    public String getPtStatus() {
        return ptStatus;
    }

    public void setPtStatus(String ptStatus) {
        this.ptStatus = ptStatus;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public double getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(double refundPrice) {
        this.refundPrice = refundPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReportDepartment() {
        return reportDepartment;
    }

    public void setReportDepartment(String reportDepartment) {
        this.reportDepartment = reportDepartment;
    }

    public String getReportHospital() {
        return reportHospital;
    }

    public void setReportHospital(String reportHospital) {
        this.reportHospital = reportHospital;
    }

    public int getSonOrderNum() {
        return sonOrderNum;
    }

    public void setSonOrderNum(int sonOrderNum) {
        this.sonOrderNum = sonOrderNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTradeOrder() {
        return tradeOrder;
    }

    public void setTradeOrder(int tradeOrder) {
        this.tradeOrder = tradeOrder;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<OrderProductsBean> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductsBean> orderProducts) {
        this.orderProducts = orderProducts;
    }
    public static class OrderLogisticsApply{


        /**
         * id : 270449515417165824
         * custId : 263032045868175361
         * orderId : 270449515417165824
         * serviceDate : 2019-03-30 00:00:00
         * type : 1
         * province : 广东省
         * city : 广州市
         * district : 天河区
         * adminDiv : 440000,440100,440106
         * address : 发广告
         * contacts : 过后就忘
         * phone : 13531238160
         * isComplete : 0
         * logisticsOrderNo : R201903220001
         */

        private String id;
        private String custId;
        private String orderId;
        private String serviceDate;
        private String type;
        private String province;
        private String city;
        private String district;
        private String adminDiv;
        private String address;
        private String contacts;
        private String phone;
        private String isComplete;
        private String logisticsOrderNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getServiceDate() {
            return serviceDate;
        }

        public void setServiceDate(String serviceDate) {
            this.serviceDate = serviceDate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAdminDiv() {
            return adminDiv;
        }

        public void setAdminDiv(String adminDiv) {
            this.adminDiv = adminDiv;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIsComplete() {
            return isComplete;
        }

        public void setIsComplete(String isComplete) {
            this.isComplete = isComplete;
        }

        public String getLogisticsOrderNo() {
            return logisticsOrderNo;
        }

        public void setLogisticsOrderNo(String logisticsOrderNo) {
            this.logisticsOrderNo = logisticsOrderNo;
        }
    }
    public static class OrderProductsBean {
        public int getPayState() {
            return payState;
        }

        public void setPayState(int payState) {
            this.payState = payState;
        }

        /**
         * balance : 0
         * couponsPrice : 0
         * createBy : 0
         * createDate : 2019-01-23T08:31:53.698Z
         * id : 0
         * laboratory : string
         * laboratoryAddress : string
         * merchantName : string
         * orderId : 0
         * orderSonId : 0
         * productId : 0
         * productName : string
         * productPrice : 0
         * ptPrice : 0
         * ptStatus : string
         * realPrice : 0
         * refundType : string
         * remarks : string
         * supplierName : string
         * updateBy : 0
         * updateDate : 2019-01-23T08:31:53.698Z
         */
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        private int payState;
        private double balance;
        private double couponsPrice;
        private String createBy;
        private String createDate;
        private String id;
        private String image;
        private String laboratory;
        private String laboratoryAddress;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setPtPrice(double ptPrice) {
            this.ptPrice = ptPrice;
        }

        private String merchantName;
        private String orderId;
        private String orderSonId;
        private String productId;
        private String productName;
        private double productPrice;
        private double ptPrice;
        private String ptStatus;
        private double realPrice;
        private String refundType;
        private String remarks;
        private String supplierName;
        private String updateBy;
        private String updateDate;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getCouponsPrice() {
            return couponsPrice;
        }

        public void setCouponsPrice(double couponsPrice) {
            this.couponsPrice = couponsPrice;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
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

        public String getLaboratory() {
            return laboratory;
        }

        public void setLaboratory(String laboratory) {
            this.laboratory = laboratory;
        }

        public String getLaboratoryAddress() {
            return laboratoryAddress;
        }

        public void setLaboratoryAddress(String laboratoryAddress) {
            this.laboratoryAddress = laboratoryAddress;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderSonId() {
            return orderSonId;
        }

        public void setOrderSonId(String orderSonId) {
            this.orderSonId = orderSonId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }

        public double getPtPrice() {
            return ptPrice;
        }

        public void setPtPrice(int ptPrice) {
            this.ptPrice = ptPrice;
        }

        public String getPtStatus() {
            return ptStatus;
        }

        public void setPtStatus(String ptStatus) {
            this.ptStatus = ptStatus;
        }

        public double getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(double realPrice) {
            this.realPrice = realPrice;
        }

        public String getRefundType() {
            return refundType;
        }

        public void setRefundType(String refundType) {
            this.refundType = refundType;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }
}
