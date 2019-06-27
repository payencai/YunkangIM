package com.yunkang.chat.home.model;

import java.util.List;

public class Report {

    /**
     * createDate : 2019-02-18T09:10:25.959Z
     * id : 0
     * patientName : string
     * products : [{"id":0,"laboratory":"string","laboratoryAddress":"string","merchantName":"string","productName":"string","supplierName":"string"}]
     */

    private String createDate;
    private String id;
    private String patientName;
    private List<ProductsBean> products;

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean {
        /**
         * id : 0
         * laboratory : string
         * laboratoryAddress : string
         * merchantName : string
         * productName : string
         * supplierName : string
         */

        private String id;
        private String laboratory;
        private String laboratoryAddress;
        private String merchantName;
        private String productName;
        private String supplierName;

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

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }
    }
}
