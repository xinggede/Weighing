package com.xing.weight.bean;

public class LoginResultInfo {

    /**
     * token :
     * bdUser : {"id":0,"username":"","nickname":"","password":"","phoneno":"","country":"","province":"","city":"","address":"","imgurl":"","gender":"","remark":"","comid":0,"comname":"","openid":"","delflag":"","createdate":"","modifydate":""}
     * clientInfo : {"ip":"","addree":"","browserName":"","browserversion":"","engineName":"","engineVersion":"","osName":"","platformName":"","mobile":true,"deviceName":"","deviceModel":""}
     */

    private String token;
    private BdUserBean bdUser;
    private ClientInfoBean clientInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BdUserBean getBdUser() {
        return bdUser;
    }

    public void setBdUser(BdUserBean bdUser) {
        this.bdUser = bdUser;
    }

    public ClientInfoBean getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfoBean clientInfo) {
        this.clientInfo = clientInfo;
    }

    public static class BdUserBean {
        /**
         * id : 0
         * username :
         * nickname :
         * password :
         * phoneno :
         * country :
         * province :
         * city :
         * address :
         * imgurl :
         * gender :
         * remark :
         * comid : 0
         * comname :
         * openid :
         * delflag :
         * createdate :
         * modifydate :
         */

        private int id;
        private String username;
        private String nickname;
        private String password;
        private String phoneno;
        private String country;
        private String province;
        private String city;
        private String address;
        private String imgurl;
        private String gender;
        private String remark;
        private int comid;
        private String comname;
        private String openid;
        private String delflag;
        private String createdate;
        private String modifydate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneno() {
            return phoneno;
        }

        public void setPhoneno(String phoneno) {
            this.phoneno = phoneno;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getComid() {
            return comid;
        }

        public void setComid(int comid) {
            this.comid = comid;
        }

        public String getComname() {
            return comname;
        }

        public void setComname(String comname) {
            this.comname = comname;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getDelflag() {
            return delflag;
        }

        public void setDelflag(String delflag) {
            this.delflag = delflag;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getModifydate() {
            return modifydate;
        }

        public void setModifydate(String modifydate) {
            this.modifydate = modifydate;
        }
    }

    public static class ClientInfoBean {
        /**
         * ip :
         * addree :
         * browserName :
         * browserversion :
         * engineName :
         * engineVersion :
         * osName :
         * platformName :
         * mobile : true
         * deviceName :
         * deviceModel :
         */

        private String ip;
        private String addree;
        private String browserName;
        private String browserversion;
        private String engineName;
        private String engineVersion;
        private String osName;
        private String platformName;
        private String mobile;
        private String deviceName;
        private String deviceModel;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getAddree() {
            return addree;
        }

        public void setAddree(String addree) {
            this.addree = addree;
        }

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserversion() {
            return browserversion;
        }

        public void setBrowserversion(String browserversion) {
            this.browserversion = browserversion;
        }

        public String getEngineName() {
            return engineName;
        }

        public void setEngineName(String engineName) {
            this.engineName = engineName;
        }

        public String getEngineVersion() {
            return engineVersion;
        }

        public void setEngineVersion(String engineVersion) {
            this.engineVersion = engineVersion;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }
    }
}
