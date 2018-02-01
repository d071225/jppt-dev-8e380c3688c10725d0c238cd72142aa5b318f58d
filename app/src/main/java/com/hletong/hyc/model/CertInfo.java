package com.hletong.hyc.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hletong.hyc.util.OtherInfoHelper;
import com.hletong.mob.validator.result.ResultBasic;
import com.hletong.mob.validator.result.ResultType;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengxin on 2017/7/3.
 */

public class CertInfo {
    private String paperFlag;
    private MemberDtoBean nMemberDto;
    private List<BankCard_> bankList;
    private List<Invoice_> invoiceList;
    private MmShipResponseDtoBean mmShipResponseDto;
    private MmTruckResponseDtoBean mmTruckResponseDto;
    private List<Paper_> paperList;

    public boolean hasProtocol() {
        return "1".equals(paperFlag);
    }

    public String getPaperFlag() {
        return paperFlag;
    }

    public void setPaperFlag(String paperFlag) {
        this.paperFlag = paperFlag;
    }

    public MemberDtoBean getNMemberDto() {
        return nMemberDto;
    }

    public void setMemberDto(MemberDtoBean nMemberDto) {
        this.nMemberDto = nMemberDto;
    }

    public List<Invoice_> getInvoiceList() {
        return invoiceList;
    }

    public MmShipResponseDtoBean getMmShipResponseDto() {
        return mmShipResponseDto;
    }

    public void setMmShipResponseDto(MmShipResponseDtoBean mmShipResponseDto) {
        this.mmShipResponseDto = mmShipResponseDto;
    }

    public MmTruckResponseDtoBean getMmTruckResponseDto() {
        return mmTruckResponseDto;
    }

    public void setMmTruckResponseDto(MmTruckResponseDtoBean mmTruckResponseDto) {
        this.mmTruckResponseDto = mmTruckResponseDto;
    }

    public List<Paper_> getPaperList() {
        return paperList;
    }

    public List<BankCard_> getBankList() {
        return bankList;
    }

    public static class MemberDtoBean {

        private String memberUuid;
        private String memberCode;
        private String unitName;
        private String unitCode;
        private String unitUuid;
        private String memberName;
        private String memberClassify;
        private String memberClassify_;
        private String companyName;
        private String simpleName;
        private String memberType;
        private String memberType_;
        private String memberStatus;
        private String reference;
        private String memeberLegal;
        private String memberIdentity;
        private String memberTel;
        private Object companyTel;
        private String fax;
        private String zipCode;
        private String email;
        private String memberAddressProvince;
        private String memberAddressCity;
        private String memberAddressArea;
        private String memberAddress;
        private String memberAddressDetail;
        private String recommend;
        private String registerDt;
        private String endDt;
        private String createDttm;
        private String updateDttm;
        private String bizContact;
        private String bizContactTel;
        private String remark;
        private Object firstTel;
        private Object secondTel;
        private Object commissionerUuid;
        private Object linkCompanyUuid;
        private Object agentUuid;
        private String agentName;
        private Object agentType;
        private String idType;
        private String organizCode;
        private String taxpayerCode;
        private String personalName;
        private String personalIdentity;
        private String personalTel;
        private String registerFrom;
        private String classify;
        private Object checkAdvice;
        private Object forbiddenAdvice;
        private Object activateAdvice;
        private Object refundAdvice;
        private Object unionAdvice;
        private Object assignTruck;
        private Object assignShip;
        private Object cargoType;
        private Object agentCargo;
        private Object isSend;
        private String version;
        private String isJp;
        private String isXh;
        private String dataSource;
        private Object quota;
        private Object registeredAddressCarrier;
        private Object registeredCapital;
        private Object uniformSocialCreateitCode;
        private Object businessScope;
        private String roadTransportPermitNo;
        private Object memberSname;

        public String getMemberUuid() {
            return memberUuid;
        }

        //获取公司性质证件类型
        public DictionaryItem getPaperByType(List<DictionaryItem> papers) {
            if (idType != null) {
                for (DictionaryItem item : papers) {
                    if (item.getId().equals(idType)) {
                        return item;
                    }
                }
            }
            return null;
        }

        public void setMemberUuid(String memberUuid) {
            this.memberUuid = memberUuid;
        }

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public String getUnitUuid() {
            return unitUuid;
        }

        public void setUnitUuid(String unitUuid) {
            this.unitUuid = unitUuid;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberClassify() {
            return memberClassify;
        }

        public void setMemberClassify(String memberClassify) {
            this.memberClassify = memberClassify;
        }

        public String getMemberClassify_() {
            return memberClassify_;
        }

        public void setMemberClassify_(String memberClassify_) {
            this.memberClassify_ = memberClassify_;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getMemberType_() {
            return memberType_;
        }

        public void setMemberType_(String memberType_) {
            this.memberType_ = memberType_;
        }

        public String getMemberStatus() {
            return memberStatus;
        }

        public void setMemberStatus(String memberStatus) {
            this.memberStatus = memberStatus;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getMemeberLegal() {
            return memeberLegal;
        }

        public void setMemeberLegal(String memeberLegal) {
            this.memeberLegal = memeberLegal;
        }

        public String getMemberIdentity() {
            return memberIdentity;
        }

        public void setMemberIdentity(String memberIdentity) {
            this.memberIdentity = memberIdentity;
        }

        public String getMemberTel() {
            return memberTel;
        }

        public void setMemberTel(String memberTel) {
            this.memberTel = memberTel;
        }

        public Object getCompanyTel() {
            return companyTel;
        }

        public void setCompanyTel(Object companyTel) {
            this.companyTel = companyTel;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMemberAddressProvince() {
            return memberAddressProvince;
        }

        public void setMemberAddressProvince(String memberAddressProvince) {
            this.memberAddressProvince = memberAddressProvince;
        }

        public String getMemberAddressCity() {
            return memberAddressCity;
        }

        public void setMemberAddressCity(String memberAddressCity) {
            this.memberAddressCity = memberAddressCity;
        }

        public String getMemberAddressArea() {
            return memberAddressArea;
        }

        public void setMemberAddressArea(String memberAddressArea) {
            this.memberAddressArea = memberAddressArea;
        }

        public String getMemberAddress() {
            return memberAddress;
        }

        public void setMemberAddress(String memberAddress) {
            this.memberAddress = memberAddress;
        }

        public String getMemberAddressDetail() {
            return memberAddressDetail;
        }

        public void setMemberAddressDetail(String memberAddressDetail) {
            this.memberAddressDetail = memberAddressDetail;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public String getRegisterDt() {
            return registerDt;
        }

        public void setRegisterDt(String registerDt) {
            this.registerDt = registerDt;
        }

        public String getEndDt() {
            return endDt;
        }

        public void setEndDt(String endDt) {
            this.endDt = endDt;
        }

        public String getCreateDttm() {
            return createDttm;
        }

        public void setCreateDttm(String createDttm) {
            this.createDttm = createDttm;
        }

        public String getUpdateDttm() {
            return updateDttm;
        }

        public void setUpdateDttm(String updateDttm) {
            this.updateDttm = updateDttm;
        }

        public String getBizContact() {
            return bizContact;
        }

        public void setBizContact(String bizContact) {
            this.bizContact = bizContact;
        }

        public String getBizContactTel() {
            return bizContactTel;
        }

        public void setBizContactTel(String bizContactTel) {
            this.bizContactTel = bizContactTel;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Object getFirstTel() {
            return firstTel;
        }

        public void setFirstTel(Object firstTel) {
            this.firstTel = firstTel;
        }

        public Object getSecondTel() {
            return secondTel;
        }

        public void setSecondTel(Object secondTel) {
            this.secondTel = secondTel;
        }

        public Object getCommissionerUuid() {
            return commissionerUuid;
        }

        public void setCommissionerUuid(Object commissionerUuid) {
            this.commissionerUuid = commissionerUuid;
        }

        public Object getLinkCompanyUuid() {
            return linkCompanyUuid;
        }

        public void setLinkCompanyUuid(Object linkCompanyUuid) {
            this.linkCompanyUuid = linkCompanyUuid;
        }

        public Object getAgentUuid() {
            return agentUuid;
        }

        public void setAgentUuid(Object agentUuid) {
            this.agentUuid = agentUuid;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public Object getAgentType() {
            return agentType;
        }

        public void setAgentType(Object agentType) {
            this.agentType = agentType;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getOrganizCode() {
            return organizCode;
        }

        public void setOrganizCode(String organizCode) {
            this.organizCode = organizCode;
        }

        public String getTaxpayerCode() {
            return taxpayerCode;
        }

        public void setTaxpayerCode(String taxpayerCode) {
            this.taxpayerCode = taxpayerCode;
        }

        public String getPersonalName() {
            return personalName;
        }

        public void setPersonalName(String personalName) {
            this.personalName = personalName;
        }

        public String getPersonalIdentity() {
            return personalIdentity;
        }

        public void setPersonalIdentity(String personalIdentity) {
            this.personalIdentity = personalIdentity;
        }

        public String getPersonalTel() {
            return personalTel;
        }

        public void setPersonalTel(String personalTel) {
            this.personalTel = personalTel;
        }

        public String getRegisterFrom() {
            return registerFrom;
        }

        public void setRegisterFrom(String registerFrom) {
            this.registerFrom = registerFrom;
        }

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public Object getCheckAdvice() {
            return checkAdvice;
        }

        public void setCheckAdvice(Object checkAdvice) {
            this.checkAdvice = checkAdvice;
        }

        public Object getForbiddenAdvice() {
            return forbiddenAdvice;
        }

        public void setForbiddenAdvice(Object forbiddenAdvice) {
            this.forbiddenAdvice = forbiddenAdvice;
        }

        public Object getActivateAdvice() {
            return activateAdvice;
        }

        public void setActivateAdvice(Object activateAdvice) {
            this.activateAdvice = activateAdvice;
        }

        public Object getRefundAdvice() {
            return refundAdvice;
        }

        public void setRefundAdvice(Object refundAdvice) {
            this.refundAdvice = refundAdvice;
        }

        public Object getUnionAdvice() {
            return unionAdvice;
        }

        public void setUnionAdvice(Object unionAdvice) {
            this.unionAdvice = unionAdvice;
        }

        public Object getAssignTruck() {
            return assignTruck;
        }

        public void setAssignTruck(Object assignTruck) {
            this.assignTruck = assignTruck;
        }

        public Object getAssignShip() {
            return assignShip;
        }

        public void setAssignShip(Object assignShip) {
            this.assignShip = assignShip;
        }

        public Object getCargoType() {
            return cargoType;
        }

        public void setCargoType(Object cargoType) {
            this.cargoType = cargoType;
        }

        public Object getAgentCargo() {
            return agentCargo;
        }

        public void setAgentCargo(Object agentCargo) {
            this.agentCargo = agentCargo;
        }

        public Object getIsSend() {
            return isSend;
        }

        public void setIsSend(Object isSend) {
            this.isSend = isSend;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getIsJp() {
            return isJp;
        }

        public void setIsJp(String isJp) {
            this.isJp = isJp;
        }

        public String getIsXh() {
            return isXh;
        }

        public void setIsXh(String isXh) {
            this.isXh = isXh;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public Object getQuota() {
            return quota;
        }

        public void setQuota(Object quota) {
            this.quota = quota;
        }

        public Object getRegisteredAddressCarrier() {
            return registeredAddressCarrier;
        }

        public void setRegisteredAddressCarrier(Object registeredAddressCarrier) {
            this.registeredAddressCarrier = registeredAddressCarrier;
        }

        public Object getRegisteredCapital() {
            return registeredCapital;
        }

        public void setRegisteredCapital(Object registeredCapital) {
            this.registeredCapital = registeredCapital;
        }

        public Object getUniformSocialCreateitCode() {
            return uniformSocialCreateitCode;
        }

        public void setUniformSocialCreateitCode(Object uniformSocialCreateitCode) {
            this.uniformSocialCreateitCode = uniformSocialCreateitCode;
        }

        public Object getBusinessScope() {
            return businessScope;
        }

        public void setBusinessScope(Object businessScope) {
            this.businessScope = businessScope;
        }

        public String getRoadTransportPermitNo() {
            return roadTransportPermitNo;
        }

        public void setRoadTransportPermitNo(String roadTransportPermitNo) {
            this.roadTransportPermitNo = roadTransportPermitNo;
        }

        public Object getMemberSname() {
            return memberSname;
        }

        public void setMemberSname(Object memberSname) {
            this.memberSname = memberSname;
        }
    }

    public static class MmShipResponseDtoBean {
        /**
         * mmShipDto : {"shipUuid":"d458996b07cf004abe0a27309c30ed6d","ship":"原木号","memberUuid":"0221b33b8bf818449e8be0c88a0818a7","memberName":"寿司船运","memberCode":"CBHY100000834","memberStatus":"1","memberStatus_":null,"memberType":"2","memberType_":null,"memberClassify":null,"memberClassify_":null,"personalMemberName":null,"personalMemberCode":"CBHY100000955","companyName":null,"memberTel":"13665493214","companyTel":null,"fax":null,"assignCargo":null,"shipCode":null,"contactCode":null,"shipName":"吉吉","dentityNo":"456345198901241132","phone":null,"unitUuid":"11bb29cab406cb4eeeb8075b15a91506","memberAddressProvince":"广东省","memberAddressCity":"潮州市","memberAddressArea":"潮安区","memberAddress":"1231","recommend":null,"shipType":"1","shipType_":null,"shipStatus":"1","shipStatus_":null,"nationality":"","nationalityCert":"23213342432","ton":"0.0000","netTon":"0.0000","loadTon":"1200.0000","shipLength":"0.0000","hatchNo":null,"deep":"0.0000","shipWidth":"0.0000","fullDraft":"8.0000","aboveHeight":"0.00","shipCrane":null,"createTime":null,"registerDt":"20170329","createDt":"20170329134757","updateDttm":"20170417135558","endDt":"20200328","registerFrom":null,"forbiddenAdvice":null,"activateAdvice":null,"refundAdvice":null,"unionAdvice":null,"checkAdvice":null,"longitude":null,"latitude":null,"remark":"","version":"8","subClassify":"1","subClassify_":null,"hasProtocol":"1","shipRegtime":null,"isSend":null,"longitudeLatitudeTime":null}
         * bankList : []
         * paperList : [{"paperUuid":"431c7352144311e7a12d0cda411db0dd","attachType":"1","attachUuid":"d458996b07cf004abe0a27309c30ed6d","paperType":"19","paperType_":"车辆会员入会协议书","paperFile":"DAF8C3B1AEC44750A8318D5A56D5AF8E","beginDt":null,"endDt":"20350301","version":"0"}]
         */

        private MmShipDtoBean mmShipDto;
        private List<BankCard_> bankList;
        private List<Paper_> paperList;

        public MmShipDtoBean getMmShipDto() {
            return mmShipDto;
        }


        public List<BankCard_> getBankList() {
            return bankList;
        }


        public List<Paper_> getPaperList() {
            return paperList;
        }

        public ShipInfo getShipInfo() {
            if (mmShipDto != null) {
                ShipInfo shipInfo = new ShipInfo();
                shipInfo.setNewTonnage(mmShipDto.netTon);
                shipInfo.setLoadTon(mmShipDto.loadTon);
                shipInfo.setGjzsNumber(mmShipDto.nationalityCert);
                if (mmShipDto.shipType != null) {
                    shipInfo.setShip_type(new DictionaryItem(mmShipDto.shipType, mmShipDto.shipType_));
                }
                shipInfo.setLoadedWater(mmShipDto.fullDraft);
                shipInfo.setShipLength(mmShipDto.shipLength);
                return shipInfo;
            }
            return null;
        }

        public static class MmShipDtoBean {
            private String shipUuid;
            private String ship;
            private String memberUuid;
            private String memberName;
            private String memberCode;
            private String memberStatus;
            private Object memberStatus_;
            private String memberType;
            private Object memberType_;
            private Object memberClassify;
            private Object memberClassify_;
            private String personalMemberName;
            private String personalMemberCode;
            private String companyName;
            private String memberTel;
            private Object companyTel;
            private Object fax;
            private Object assignCargo;
            private Object shipCode;
            private Object contactCode;
            private String shipName;
            private String dentityNo;
            private Object phone;
            private String unitUuid;
            private String memberAddressProvince;
            private String memberAddressCity;
            private String memberAddressArea;
            private String memberAddress;
            private Object recommend;
            private String shipType;
            private String shipType_;
            private String shipStatus;
            private Object shipStatus_;
            private String nationality;
            private String nationalityCert;
            private String ton;
            private String netTon;
            private String loadTon;
            private String shipLength;
            private Object hatchNo;
            private String deep;
            private String shipWidth;
            private String fullDraft;
            private String aboveHeight;
            private Object shipCrane;
            private Object createTime;
            private String registerDt;
            private String createDt;
            private String updateDttm;
            private String endDt;
            private Object registerFrom;
            private Object forbiddenAdvice;
            private Object activateAdvice;
            private Object refundAdvice;
            private Object unionAdvice;
            private Object checkAdvice;
            private Object longitude;
            private Object latitude;
            private String remark;
            private String version;
            private String subClassify;
            private Object subClassify_;
            private String hasProtocol;
            private Object shipRegtime;
            private Object isSend;
            private Object longitudeLatitudeTime;

            private String isSoldier;
            private String isOrganization;
            private String soldierKind;
            private String soldierArmy;
            private String soldierLevel;

            public String getShipUuid() {
                return shipUuid;
            }

            public void setShipUuid(String shipUuid) {
                this.shipUuid = shipUuid;
            }

            public String getShip() {
                return ship;
            }

            public void setShip(String ship) {
                this.ship = ship;
            }

            public String getMemberUuid() {
                return memberUuid;
            }

            public void setMemberUuid(String memberUuid) {
                this.memberUuid = memberUuid;
            }

            public String getMemberName() {
                return memberName;
            }

            public void setMemberName(String memberName) {
                this.memberName = memberName;
            }

            public String getMemberCode() {
                return memberCode;
            }

            public void setMemberCode(String memberCode) {
                this.memberCode = memberCode;
            }

            public String getMemberStatus() {
                return memberStatus;
            }

            public void setMemberStatus(String memberStatus) {
                this.memberStatus = memberStatus;
            }

            public Object getMemberStatus_() {
                return memberStatus_;
            }

            public void setMemberStatus_(Object memberStatus_) {
                this.memberStatus_ = memberStatus_;
            }

            public String getMemberType() {
                return memberType;
            }

            public void setMemberType(String memberType) {
                this.memberType = memberType;
            }

            public Object getMemberType_() {
                return memberType_;
            }

            public void setMemberType_(Object memberType_) {
                this.memberType_ = memberType_;
            }

            public Object getMemberClassify() {
                return memberClassify;
            }

            public void setMemberClassify(Object memberClassify) {
                this.memberClassify = memberClassify;
            }

            public Object getMemberClassify_() {
                return memberClassify_;
            }

            public void setMemberClassify_(Object memberClassify_) {
                this.memberClassify_ = memberClassify_;
            }

            public String getPersonalMemberName() {
                return personalMemberName;
            }

            public void setPersonalMemberName(String personalMemberName) {
                this.personalMemberName = personalMemberName;
            }

            public String getPersonalMemberCode() {
                return personalMemberCode;
            }

            public void setPersonalMemberCode(String personalMemberCode) {
                this.personalMemberCode = personalMemberCode;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getMemberTel() {
                return memberTel;
            }

            public void setMemberTel(String memberTel) {
                this.memberTel = memberTel;
            }

            public Object getCompanyTel() {
                return companyTel;
            }

            public void setCompanyTel(Object companyTel) {
                this.companyTel = companyTel;
            }

            public Object getFax() {
                return fax;
            }

            public void setFax(Object fax) {
                this.fax = fax;
            }

            public Object getAssignCargo() {
                return assignCargo;
            }

            public void setAssignCargo(Object assignCargo) {
                this.assignCargo = assignCargo;
            }

            public Object getShipCode() {
                return shipCode;
            }

            public void setShipCode(Object shipCode) {
                this.shipCode = shipCode;
            }

            public Object getContactCode() {
                return contactCode;
            }

            public void setContactCode(Object contactCode) {
                this.contactCode = contactCode;
            }

            public String getShipName() {
                return shipName;
            }

            public void setShipName(String shipName) {
                this.shipName = shipName;
            }

            public String getDentityNo() {
                return dentityNo;
            }

            public void setDentityNo(String dentityNo) {
                this.dentityNo = dentityNo;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public String getUnitUuid() {
                return unitUuid;
            }

            public void setUnitUuid(String unitUuid) {
                this.unitUuid = unitUuid;
            }

            public String getMemberAddressProvince() {
                return memberAddressProvince;
            }

            public void setMemberAddressProvince(String memberAddressProvince) {
                this.memberAddressProvince = memberAddressProvince;
            }

            public String getMemberAddressCity() {
                return memberAddressCity;
            }

            public void setMemberAddressCity(String memberAddressCity) {
                this.memberAddressCity = memberAddressCity;
            }

            public String getMemberAddressArea() {
                return memberAddressArea;
            }

            public void setMemberAddressArea(String memberAddressArea) {
                this.memberAddressArea = memberAddressArea;
            }

            public String getMemberAddress() {
                return memberAddress;
            }

            public void setMemberAddress(String memberAddress) {
                this.memberAddress = memberAddress;
            }

            public Object getRecommend() {
                return recommend;
            }

            public void setRecommend(Object recommend) {
                this.recommend = recommend;
            }

            public String getShipType() {
                return shipType;
            }

            public void setShipType(String shipType) {
                this.shipType = shipType;
            }

            public String getShipType_() {
                return shipType_;
            }

            public void setShipType_(String shipType_) {
                this.shipType_ = shipType_;
            }

            public String getShipStatus() {
                return shipStatus;
            }

            public void setShipStatus(String shipStatus) {
                this.shipStatus = shipStatus;
            }

            public Object getShipStatus_() {
                return shipStatus_;
            }

            public void setShipStatus_(Object shipStatus_) {
                this.shipStatus_ = shipStatus_;
            }

            public String getNationality() {
                return nationality;
            }

            public void setNationality(String nationality) {
                this.nationality = nationality;
            }

            public String getNationalityCert() {
                return nationalityCert;
            }

            public void setNationalityCert(String nationalityCert) {
                this.nationalityCert = nationalityCert;
            }

            public String getTon() {
                return ton;
            }

            public void setTon(String ton) {
                this.ton = ton;
            }

            public String getNetTon() {
                return netTon;
            }

            public void setNetTon(String netTon) {
                this.netTon = netTon;
            }

            public String getLoadTon() {
                return loadTon;
            }

            public void setLoadTon(String loadTon) {
                this.loadTon = loadTon;
            }

            public String getShipLength() {
                return shipLength;
            }

            public void setShipLength(String shipLength) {
                this.shipLength = shipLength;
            }

            public Object getHatchNo() {
                return hatchNo;
            }

            public void setHatchNo(Object hatchNo) {
                this.hatchNo = hatchNo;
            }

            public String getDeep() {
                return deep;
            }

            public void setDeep(String deep) {
                this.deep = deep;
            }

            public String getShipWidth() {
                return shipWidth;
            }

            public void setShipWidth(String shipWidth) {
                this.shipWidth = shipWidth;
            }

            public String getFullDraft() {
                return fullDraft;
            }

            public void setFullDraft(String fullDraft) {
                this.fullDraft = fullDraft;
            }

            public String getAboveHeight() {
                return aboveHeight;
            }

            public void setAboveHeight(String aboveHeight) {
                this.aboveHeight = aboveHeight;
            }

            public Object getShipCrane() {
                return shipCrane;
            }

            public void setShipCrane(Object shipCrane) {
                this.shipCrane = shipCrane;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public String getRegisterDt() {
                return registerDt;
            }

            public void setRegisterDt(String registerDt) {
                this.registerDt = registerDt;
            }

            public String getCreateDt() {
                return createDt;
            }

            public void setCreateDt(String createDt) {
                this.createDt = createDt;
            }

            public String getUpdateDttm() {
                return updateDttm;
            }

            public void setUpdateDttm(String updateDttm) {
                this.updateDttm = updateDttm;
            }

            public String getEndDt() {
                return endDt;
            }

            public void setEndDt(String endDt) {
                this.endDt = endDt;
            }

            public Object getRegisterFrom() {
                return registerFrom;
            }

            public void setRegisterFrom(Object registerFrom) {
                this.registerFrom = registerFrom;
            }

            public Object getForbiddenAdvice() {
                return forbiddenAdvice;
            }

            public void setForbiddenAdvice(Object forbiddenAdvice) {
                this.forbiddenAdvice = forbiddenAdvice;
            }

            public Object getActivateAdvice() {
                return activateAdvice;
            }

            public void setActivateAdvice(Object activateAdvice) {
                this.activateAdvice = activateAdvice;
            }

            public Object getRefundAdvice() {
                return refundAdvice;
            }

            public void setRefundAdvice(Object refundAdvice) {
                this.refundAdvice = refundAdvice;
            }

            public Object getUnionAdvice() {
                return unionAdvice;
            }

            public void setUnionAdvice(Object unionAdvice) {
                this.unionAdvice = unionAdvice;
            }

            public Object getCheckAdvice() {
                return checkAdvice;
            }

            public void setCheckAdvice(Object checkAdvice) {
                this.checkAdvice = checkAdvice;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getSubClassify() {
                return subClassify;
            }

            public void setSubClassify(String subClassify) {
                this.subClassify = subClassify;
            }

            public Object getSubClassify_() {
                return subClassify_;
            }

            public void setSubClassify_(Object subClassify_) {
                this.subClassify_ = subClassify_;
            }

            public String getHasProtocol() {
                return hasProtocol;
            }

            public void setHasProtocol(String hasProtocol) {
                this.hasProtocol = hasProtocol;
            }

            public Object getShipRegtime() {
                return shipRegtime;
            }

            public void setShipRegtime(Object shipRegtime) {
                this.shipRegtime = shipRegtime;
            }

            public Object getIsSend() {
                return isSend;
            }

            public void setIsSend(Object isSend) {
                this.isSend = isSend;
            }

            public Object getLongitudeLatitudeTime() {
                return longitudeLatitudeTime;
            }

            public void setLongitudeLatitudeTime(Object longitudeLatitudeTime) {
                this.longitudeLatitudeTime = longitudeLatitudeTime;
            }
        }
    }

    public static class MmTruckResponseDtoBean {
        /**
         * mmTruckDto : {"truckUuid":"dc0417fbf863af498af872ffc7513f23","plate":"帅B88888","memberUuid":"1fa5d067edff1e4aa6e9df5ea87005db","memberName":"我的","memberCode":"CLHY99984248","memberStatus":"1","memberStatus_":"正常","memberType":"1","memberType_":"货方会员","memberClassify":null,"memberClassify_":null,"personalMemberName":null,"personalMemberCode":"CLHY99984249","companyName":null,"memberTel":"13111111111","companyTel":null,"fax":null,"truckCode":null,"contactCode":null,"truckName":"傻逼","dentityNo":"342222199211111111","phone":null,"unitUuid":"5c5dea0975eb9747a378e1a7ac21e4ab","memberAddressProvince":"海南省","memberAddressCity":"儋州市","memberAddressArea":"","memberAddress":"1","recommend":null,"truckType":"11","truckType_":"平板挂车","truckStatus":"1","truckStatus_":null,"maxHeavy":"12.0000","maxLength":"12.0000","maxWidth":"0.0000","maxHeight":"0.0000","registerDt":"20170121","identify":"1","createDt":"20170121220803","updateDttm":"20170629210825","endDt":"20200120","registerFrom":null,"registerFrom_":null,"forbiddenAdvice":null,"activateAdvice":null,"refundAdvice":null,"unionAdvice":null,"checkAdvice":null,"longitude":null,"latitude":null,"remark":"","version":"9","subClassify":"2","subClassify_":"2","hasProtocol":null,"truckRegtime":null,"isSend":null,"longitudeLatitudeTime":null,"wheel":null,"licensePlateColor":"2","licensePlateColor_":"2","loadedVehicleQuality":"48.4050","vehicleMass":"10.0000","roadTransportCertificate":"130682313560","gender":"1","gender_":"1","qualificationCertificate":"12345566777","actualCarrier":"定州市金祥泰货物运输有限公司","licensePlateType":"2","licensePlateType_":"2","plateHang":null}
         * bankList : [{"memberCode":null,"bankCode":"2312","name":"发的","bankType":"6","certType":"1","certCode":"1412","isDefault":"1","bankAddress":"但是","refCode":"dc0417fbf863af498af872ffc7513f23","type":"2","assignCargo":null,"assignCode":"","bankUuid":"830e7cc21fa27144c01991c142683b32","version":"0"},{"memberCode":null,"bankCode":"123","name":"地方","bankType":"5","certType":"1","certCode":"123","isDefault":"2","bankAddress":"水电费","refCode":"dc0417fbf863af498af872ffc7513f23","type":"2","assignCargo":null,"assignCode":"","bankUuid":"e3b2b84317d5d14e7918baa1e3fa3b3f","version":"1"}]
         * paperList : [{"paperUuid":"0944918f5ccc11e79982000c29d4c705","attachType":"1","attachUuid":"dc0417fbf863af498af872ffc7513f23","paperType":"5","paperFile":"62AD0DF1C656463489804A0452CA9B4A","beginDt":null,"endDt":"20170614"},{"paperUuid":"094a36935ccc11e79982000c29d4c705","attachType":"1","attachUuid":"dc0417fbf863af498af872ffc7513f23","paperType":"14","paperFile":"6ABE7AB3B68D47379A2317695F7239A2","beginDt":null,"endDt":"20220616"}]
         */

        private MmTruckDtoBean mmTruckDto;
        private List<BankCard_> bankList;
        private List<Paper_> paperList;

        public MmTruckDtoBean getMmTruckDto() {
            return mmTruckDto;
        }

        public void setMmTruckDto(MmTruckDtoBean mmTruckDto) {
            this.mmTruckDto = mmTruckDto;
        }

        public List<Paper_> getPaperList() {
            return paperList;
        }

        public List<BankCard_> getBankList() {
            return bankList;
        }

        public TruckCompleteInfo getTruckInfo() {
            if (mmTruckDto != null) {
                TruckCompleteInfo truckInfo = new TruckCompleteInfo();
                truckInfo.setSbdhNumber(mmTruckDto.identify);
                truckInfo.setVehicleMass(mmTruckDto.vehicleMass);
                truckInfo.setDlysNumber(mmTruckDto.roadTransportCertificate);
                truckInfo.setTruckLength(mmTruckDto.maxLength);
                truckInfo.setLoadTon(mmTruckDto.maxHeavy);
                truckInfo.setLoadedVehicleQuality(mmTruckDto.loadedVehicleQuality);
                if (mmTruckDto.truckType != null) {
                    truckInfo.setTruck_type(new DictionaryItem(mmTruckDto.truckType, mmTruckDto.truckType_));
                }
                if (mmTruckDto.gender != null) {
                    truckInfo.setSexType(new DictionaryItem(mmTruckDto.gender, mmTruckDto.gender_));
                }
                if (mmTruckDto.licensePlateColor != null) {
                    truckInfo.setTruck_color(new DictionaryItem(mmTruckDto.licensePlateColor, mmTruckDto.licensePlateColor_));
                }
                truckInfo.setCyzgzNumber(mmTruckDto.qualificationCertificate);
                return truckInfo;
            }
            return null;
        }


        public static class MmTruckDtoBean {
            private String truckUuid;
            private String plate;
            private String memberUuid;
            private String memberName;
            private String memberCode;
            private String memberStatus;
            private String memberStatus_;
            private String memberType;
            private String memberType_;
            private Object memberClassify;
            private Object memberClassify_;
            private Object personalMemberName;
            private String personalMemberCode;
            private String companyName;
            private String memberTel;
            private String companyTel;
            private Object fax;
            private Object truckCode;
            private Object contactCode;
            private String truckName;
            private String dentityNo;
            private Object phone;
            private String unitUuid;
            private String memberAddressProvince;
            private String memberAddressCity;
            private String memberAddressArea;
            private String memberAddress;
            private Object recommend;
            private String truckType;
            private String truckType_;
            private String truckStatus;
            private Object truckStatus_;
            private String maxHeavy;
            private String maxLength;
            private String maxWidth;
            private String maxHeight;
            private String registerDt;
            private String identify;
            private String createDt;
            private String updateDttm;
            private String endDt;
            private Object registerFrom;
            private Object registerFrom_;
            private Object forbiddenAdvice;
            private Object activateAdvice;
            private Object refundAdvice;
            private Object unionAdvice;
            private Object checkAdvice;
            private Object longitude;
            private Object latitude;
            private String remark;
            private String version;
            private String subClassify;
            private String subClassify_;
            private Object hasProtocol;
            private Object truckRegtime;
            private Object isSend;
            private Object longitudeLatitudeTime;
            private Object wheel;
            private String licensePlateColor;
            private String licensePlateColor_;
            private String loadedVehicleQuality;
            private String vehicleMass;
            private String roadTransportCertificate;
            private String gender;
            private String gender_;
            private String qualificationCertificate;
            private String actualCarrier;
            private String licensePlateType;
            private String licensePlateType_;
            private Object plateHang;

            private String isSoldier;
            private String isOrganization;
            private String soldierKind;
            private String soldierArmy;
            private String soldierLevel;

            public String getTruckUuid() {
                return truckUuid;
            }

            public void setTruckUuid(String truckUuid) {
                this.truckUuid = truckUuid;
            }

            public String getPlate() {
                return plate;
            }

            public void setPlate(String plate) {
                this.plate = plate;
            }

            public String getMemberUuid() {
                return memberUuid;
            }

            public void setMemberUuid(String memberUuid) {
                this.memberUuid = memberUuid;
            }

            public String getMemberName() {
                return memberName;
            }

            public void setMemberName(String memberName) {
                this.memberName = memberName;
            }

            public String getMemberCode() {
                return memberCode;
            }

            public void setMemberCode(String memberCode) {
                this.memberCode = memberCode;
            }

            public String getMemberStatus() {
                return memberStatus;
            }

            public void setMemberStatus(String memberStatus) {
                this.memberStatus = memberStatus;
            }

            public String getMemberStatus_() {
                return memberStatus_;
            }

            public void setMemberStatus_(String memberStatus_) {
                this.memberStatus_ = memberStatus_;
            }

            public String getMemberType() {
                return memberType;
            }

            public void setMemberType(String memberType) {
                this.memberType = memberType;
            }

            public String getMemberType_() {
                return memberType_;
            }

            public void setMemberType_(String memberType_) {
                this.memberType_ = memberType_;
            }

            public Object getMemberClassify() {
                return memberClassify;
            }

            public void setMemberClassify(Object memberClassify) {
                this.memberClassify = memberClassify;
            }

            public Object getMemberClassify_() {
                return memberClassify_;
            }

            public void setMemberClassify_(Object memberClassify_) {
                this.memberClassify_ = memberClassify_;
            }

            public Object getPersonalMemberName() {
                return personalMemberName;
            }

            public void setPersonalMemberName(Object personalMemberName) {
                this.personalMemberName = personalMemberName;
            }

            public String getPersonalMemberCode() {
                return personalMemberCode;
            }

            public void setPersonalMemberCode(String personalMemberCode) {
                this.personalMemberCode = personalMemberCode;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getMemberTel() {
                return memberTel;
            }

            public void setMemberTel(String memberTel) {
                this.memberTel = memberTel;
            }

            public String getCompanyTel() {
                return companyTel;
            }

            public void setCompanyTel(String companyTel) {
                this.companyTel = companyTel;
            }

            public Object getFax() {
                return fax;
            }

            public void setFax(Object fax) {
                this.fax = fax;
            }

            public Object getTruckCode() {
                return truckCode;
            }

            public void setTruckCode(Object truckCode) {
                this.truckCode = truckCode;
            }

            public Object getContactCode() {
                return contactCode;
            }

            public void setContactCode(Object contactCode) {
                this.contactCode = contactCode;
            }

            public String getTruckName() {
                return truckName;
            }

            public void setTruckName(String truckName) {
                this.truckName = truckName;
            }

            public String getDentityNo() {
                return dentityNo;
            }

            public void setDentityNo(String dentityNo) {
                this.dentityNo = dentityNo;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public String getUnitUuid() {
                return unitUuid;
            }

            public void setUnitUuid(String unitUuid) {
                this.unitUuid = unitUuid;
            }

            public String getMemberAddressProvince() {
                return memberAddressProvince;
            }

            public void setMemberAddressProvince(String memberAddressProvince) {
                this.memberAddressProvince = memberAddressProvince;
            }

            public String getMemberAddressCity() {
                return memberAddressCity;
            }

            public void setMemberAddressCity(String memberAddressCity) {
                this.memberAddressCity = memberAddressCity;
            }

            public String getMemberAddressArea() {
                return memberAddressArea;
            }

            public void setMemberAddressArea(String memberAddressArea) {
                this.memberAddressArea = memberAddressArea;
            }

            public String getMemberAddress() {
                return memberAddress;
            }

            public void setMemberAddress(String memberAddress) {
                this.memberAddress = memberAddress;
            }

            public Object getRecommend() {
                return recommend;
            }

            public void setRecommend(Object recommend) {
                this.recommend = recommend;
            }

            public String getTruckType() {
                return truckType;
            }

            public void setTruckType(String truckType) {
                this.truckType = truckType;
            }

            public String getTruckType_() {
                return truckType_;
            }

            public void setTruckType_(String truckType_) {
                this.truckType_ = truckType_;
            }

            public String getTruckStatus() {
                return truckStatus;
            }

            public void setTruckStatus(String truckStatus) {
                this.truckStatus = truckStatus;
            }

            public Object getTruckStatus_() {
                return truckStatus_;
            }

            public void setTruckStatus_(Object truckStatus_) {
                this.truckStatus_ = truckStatus_;
            }

            public String getMaxHeavy() {
                return maxHeavy;
            }

            public void setMaxHeavy(String maxHeavy) {
                this.maxHeavy = maxHeavy;
            }

            public String getMaxLength() {
                return maxLength;
            }

            public void setMaxLength(String maxLength) {
                this.maxLength = maxLength;
            }

            public String getMaxWidth() {
                return maxWidth;
            }

            public void setMaxWidth(String maxWidth) {
                this.maxWidth = maxWidth;
            }

            public String getMaxHeight() {
                return maxHeight;
            }

            public void setMaxHeight(String maxHeight) {
                this.maxHeight = maxHeight;
            }

            public String getRegisterDt() {
                return registerDt;
            }

            public void setRegisterDt(String registerDt) {
                this.registerDt = registerDt;
            }

            public String getIdentify() {
                return identify;
            }

            public void setIdentify(String identify) {
                this.identify = identify;
            }

            public String getCreateDt() {
                return createDt;
            }

            public void setCreateDt(String createDt) {
                this.createDt = createDt;
            }

            public String getUpdateDttm() {
                return updateDttm;
            }

            public void setUpdateDttm(String updateDttm) {
                this.updateDttm = updateDttm;
            }

            public String getEndDt() {
                return endDt;
            }

            public void setEndDt(String endDt) {
                this.endDt = endDt;
            }

            public Object getRegisterFrom() {
                return registerFrom;
            }

            public void setRegisterFrom(Object registerFrom) {
                this.registerFrom = registerFrom;
            }

            public Object getRegisterFrom_() {
                return registerFrom_;
            }

            public void setRegisterFrom_(Object registerFrom_) {
                this.registerFrom_ = registerFrom_;
            }

            public Object getForbiddenAdvice() {
                return forbiddenAdvice;
            }

            public void setForbiddenAdvice(Object forbiddenAdvice) {
                this.forbiddenAdvice = forbiddenAdvice;
            }

            public Object getActivateAdvice() {
                return activateAdvice;
            }

            public void setActivateAdvice(Object activateAdvice) {
                this.activateAdvice = activateAdvice;
            }

            public Object getRefundAdvice() {
                return refundAdvice;
            }

            public void setRefundAdvice(Object refundAdvice) {
                this.refundAdvice = refundAdvice;
            }

            public Object getUnionAdvice() {
                return unionAdvice;
            }

            public void setUnionAdvice(Object unionAdvice) {
                this.unionAdvice = unionAdvice;
            }

            public Object getCheckAdvice() {
                return checkAdvice;
            }

            public void setCheckAdvice(Object checkAdvice) {
                this.checkAdvice = checkAdvice;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getSubClassify() {
                return subClassify;
            }

            public void setSubClassify(String subClassify) {
                this.subClassify = subClassify;
            }

            public String getSubClassify_() {
                return subClassify_;
            }

            public void setSubClassify_(String subClassify_) {
                this.subClassify_ = subClassify_;
            }

            public Object getHasProtocol() {
                return hasProtocol;
            }

            public void setHasProtocol(Object hasProtocol) {
                this.hasProtocol = hasProtocol;
            }

            public Object getTruckRegtime() {
                return truckRegtime;
            }

            public void setTruckRegtime(Object truckRegtime) {
                this.truckRegtime = truckRegtime;
            }

            public Object getIsSend() {
                return isSend;
            }

            public void setIsSend(Object isSend) {
                this.isSend = isSend;
            }

            public Object getLongitudeLatitudeTime() {
                return longitudeLatitudeTime;
            }

            public void setLongitudeLatitudeTime(Object longitudeLatitudeTime) {
                this.longitudeLatitudeTime = longitudeLatitudeTime;
            }

            public Object getWheel() {
                return wheel;
            }

            public void setWheel(Object wheel) {
                this.wheel = wheel;
            }

            public String getLicensePlateColor() {
                return licensePlateColor;
            }

            public void setLicensePlateColor(String licensePlateColor) {
                this.licensePlateColor = licensePlateColor;
            }

            public String getLicensePlateColor_() {
                return licensePlateColor_;
            }

            public void setLicensePlateColor_(String licensePlateColor_) {
                this.licensePlateColor_ = licensePlateColor_;
            }

            public String getLoadedVehicleQuality() {
                return loadedVehicleQuality;
            }

            public void setLoadedVehicleQuality(String loadedVehicleQuality) {
                this.loadedVehicleQuality = loadedVehicleQuality;
            }

            public String getVehicleMass() {
                return vehicleMass;
            }

            public void setVehicleMass(String vehicleMass) {
                this.vehicleMass = vehicleMass;
            }

            public String getRoadTransportCertificate() {
                return roadTransportCertificate;
            }

            public void setRoadTransportCertificate(String roadTransportCertificate) {
                this.roadTransportCertificate = roadTransportCertificate;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getGender_() {
                return gender_;
            }

            public void setGender_(String gender_) {
                this.gender_ = gender_;
            }

            public String getQualificationCertificate() {
                return qualificationCertificate;
            }

            public void setQualificationCertificate(String qualificationCertificate) {
                this.qualificationCertificate = qualificationCertificate;
            }

            public String getActualCarrier() {
                return actualCarrier;
            }

            public void setActualCarrier(String actualCarrier) {
                this.actualCarrier = actualCarrier;
            }

            public String getLicensePlateType() {
                return licensePlateType;
            }

            public void setLicensePlateType(String licensePlateType) {
                this.licensePlateType = licensePlateType;
            }

            public String getLicensePlateType_() {
                return licensePlateType_;
            }

            public void setLicensePlateType_(String licensePlateType_) {
                this.licensePlateType_ = licensePlateType_;
            }

            public Object getPlateHang() {
                return plateHang;
            }

            public void setPlateHang(Object plateHang) {
                this.plateHang = plateHang;
            }
        }
    }


    public static class Paper_ {
        public String paperType;
        public String paperType_;
        public String paperFile;
        public String endDt;
        public String paperUuid;
    }

    public static class Invoice_ {
        String invoiceName;
        String invoiceTaxpayer;
        String invoiceTel;
        String invoiceProvince;
        String invoiceCity;
        String invoiceCounty;
        String invoiceAddress;
        String invoiceFile;
        String isDefault;
        String invoiceBankCode;
        String invoiceBank;
        String invoiceUuid;

    }

    public static ArrayList<InvoiceCert> getInvoiceCert(List<Invoice_> invoice_s) {
        if (ParamUtil.isEmpty(invoice_s)) {
            return null;
        }
        ArrayList<InvoiceCert> invoiceCerts = new ArrayList<>();
        for (Invoice_ invoice_ : invoice_s) {
            InvoiceCert invoiceCert = new InvoiceCert();
            invoiceCert.setSprName(invoice_.invoiceName);
            invoiceCert.setNsrsbh(invoice_.invoiceTaxpayer);
            invoiceCert.setTel(invoice_.invoiceTel);
            invoiceCert.setProvince(invoice_.invoiceProvince);
            invoiceCert.setCity(invoice_.invoiceCity);
            invoiceCert.setCounty(invoice_.invoiceCounty);
            invoiceCert.setDefault("1".equals(invoice_.isDefault));
            invoiceCert.setCardNumber(invoice_.invoiceBankCode);
            invoiceCert.setOpenBankName(invoice_.invoiceBank);
            invoiceCert.setAddress(invoice_.invoiceAddress);
            if (invoice_.invoiceUuid != null) {
                invoiceCert.setInvoiceUuid(invoice_.invoiceUuid);
            }
            if (invoice_.invoiceFile != null) {
                invoiceCert.getInvoicePhoto().setFileGroupId(invoice_.invoiceFile);
            }
            invoiceCerts.add(invoiceCert);
        }
        return invoiceCerts;
    }

    public static class BankCard_ {
        String memberCode;
        String bankCode;
        String name;
        String bankType;
        String bankType_;
        String certType;
        String certType_;
        String certCode;
        String isDefault;
        String bankAddress;
        String refCode;
        String type;
        String assignCargo;
        String assignCode;
        String bankFile;
        String bankUuid;
    }

    public static ArrayList<BankCard> getBankList(List<BankCard_> bankCard_s) {
        if (ParamUtil.isEmpty(bankCard_s)) {
            return null;
        }
        ArrayList<BankCard> bankCards = new ArrayList<>();
        for (BankCard_ card_ : bankCard_s) {
            BankCard bankCard = new BankCard();
            if (card_.bankType != null) {
                bankCard.setBankEnum(new DictionaryItem(card_.bankType, card_.bankType_));
            }
            if (card_.certType != null) {
                bankCard.setPaper(new DictionaryItem(card_.certType, card_.certType_));
            }
            bankCard.setDefault("1".equals(card_.isDefault));
            bankCard.setAssignCode(card_.assignCode);
            bankCard.setBankAddress(card_.bankAddress);
            bankCard.setBankCode(card_.bankCode);
            bankCard.setName(card_.name);
            bankCard.setRefCode(card_.refCode);
            bankCard.setPaperNumber(card_.certCode);
            if (card_.bankUuid != null) {
                bankCard.setBankUuid(card_.bankUuid);
            }
            if (card_.bankFile != null) {
                bankCard.getCardPhoto().setFileGroupId(card_.bankFile);
            }
            bankCards.add(bankCard);
        }
        return bankCards;
    }

    public static void filterPaperPhoto(List<Paper_> httpPaper, List<PaperPhoto> localPaper) {
        if (ParamUtil.isEmpty(httpPaper)) {
            return;
        }
        for (PaperPhoto photo : localPaper) {
            for (Paper_ temp : httpPaper) {
                if (String.valueOf(photo.getPaperType()).equals(temp.paperType)) {
                    photo.setDate(temp.endDt);
                    photo.getRegisterPhoto().setFileGroupId(temp.paperFile);
                    photo.getRegisterPhoto().setHttp(true);
                    if (temp.paperUuid != null) {
                        photo.setPaperUuid(temp.paperUuid);
                    }
                    break;
                }
            }
        }
    }

    /**
     * @param isTruck 判断是船还是车
     * @return
     */
    @Nullable
    public ResultBasic getOtherInfo(boolean isTruck) {
        if (!hasOtherInfo(isTruck)) {
            return null;
        }
        ResultBasic resultBasic = ResultBasic.mapOf(null, "已选择");
        if (isTruck) {
            MmTruckResponseDtoBean.MmTruckDtoBean truckDtoBean = mmTruckResponseDto.mmTruckDto;
            if (TextUtils.isEmpty(truckDtoBean.isOrganization)) {
                truckDtoBean.isOrganization = "2";
            }
            boolean isOrganization = "1".equals(truckDtoBean.isOrganization);
            resultBasic.put(OtherInfoHelper.key_isOrganization, ResultBasic.textOf(ResultType.TEXT, truckDtoBean.isOrganization, isOrganization ? "是" : "否"));
            if (TextUtils.isEmpty(truckDtoBean.isSoldier)) {
                truckDtoBean.isSoldier = "2";
            }
            boolean isSoldier = "1".equals(truckDtoBean.isSoldier);
            ResultBasic solderInfo = ResultBasic.mapOf(null, isSoldier ? "是" : "否");
            resultBasic.put(OtherInfoHelper.key_soldierInfo, solderInfo);
            solderInfo.put(OtherInfoHelper.key_isSoldier, ResultBasic.textOf(ResultType.TEXT, truckDtoBean.isSoldier, isSoldier ? "是" : "否"));
            if (isSoldier) {
                solderInfo.put(OtherInfoHelper.key_soldierArmy, ResultBasic.textOf(truckDtoBean.soldierArmy));
                solderInfo.put(OtherInfoHelper.key_soldierKind, ResultBasic.textOf(truckDtoBean.soldierKind));
                solderInfo.put(OtherInfoHelper.key_soldierLevel, ResultBasic.textOf(truckDtoBean.soldierLevel));
            }
        } else {
            MmShipResponseDtoBean.MmShipDtoBean shipDtoBean = mmShipResponseDto.mmShipDto;
            if (TextUtils.isEmpty(shipDtoBean.isOrganization)) {
                shipDtoBean.isOrganization = "2";
            }
            boolean isOrganization = "1".equals(shipDtoBean.isOrganization);
            resultBasic.put(OtherInfoHelper.key_isOrganization, ResultBasic.textOf(ResultType.TEXT, shipDtoBean.isOrganization, isOrganization ? "是" : "否"));
            if (TextUtils.isEmpty(shipDtoBean.isSoldier)) {
                shipDtoBean.isSoldier = "2";
            }
            boolean isSoldier = "1".equals(shipDtoBean.isSoldier);
            ResultBasic solderInfo = ResultBasic.mapOf(null, isSoldier ? "是" : "否");
            resultBasic.put(OtherInfoHelper.key_soldierInfo, solderInfo);
            solderInfo.put(OtherInfoHelper.key_isSoldier, ResultBasic.textOf(ResultType.TEXT, shipDtoBean.isSoldier, isSoldier ? "是" : "否"));
            if (isSoldier) {
                solderInfo.put(OtherInfoHelper.key_soldierArmy, ResultBasic.textOf(shipDtoBean.soldierArmy));
                solderInfo.put(OtherInfoHelper.key_soldierKind, ResultBasic.textOf(shipDtoBean.soldierKind));
                solderInfo.put(OtherInfoHelper.key_soldierLevel, ResultBasic.textOf(shipDtoBean.soldierLevel));
            }
        }
        return resultBasic;
    }

    private boolean hasOtherInfo(boolean isTruck) {
        if (isTruck) {
            if (mmTruckResponseDto != null && mmTruckResponseDto.mmTruckDto != null) {
                MmTruckResponseDtoBean.MmTruckDtoBean dto = mmTruckResponseDto.mmTruckDto;
                return dto.isOrganization != null && dto.isSoldier != null;
            }
        } else {
            if (mmShipResponseDto != null && mmShipResponseDto.mmShipDto != null) {
                MmShipResponseDtoBean.MmShipDtoBean dto = mmShipResponseDto.mmShipDto;
                return dto.isOrganization != null && dto.isSoldier != null;
            }
        }
        return false;
    }

    @Nullable
    public static Paper_ getPaper(CertInfo certInfo, int type) {
        if (certInfo != null && certInfo.paperList != null) {
            for (Paper_ paper_ : certInfo.paperList) {
                if (String.valueOf(type).equals(paper_.paperType)) {
                    return paper_;
                }
            }
        }
        return null;
    }
}
