package com.sqmusicplus.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname MusicInfoNeteaseResult
 * @Description 单曲详情
 * @Version 1.0.0
 * @Date 2024/2/22 16:25
 * @Created by SQ
 */

public class MusicInfoNeteaseResult {


    @JSONField(name = "privileges")
    private List<PrivilegesDTO> privileges;
    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "songs")
    private List<SongsDTO> songs;

    public List<PrivilegesDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegesDTO> privileges) {
        this.privileges = privileges;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<SongsDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongsDTO> songs) {
        this.songs = songs;
    }

    public static class PrivilegesDTO {
        @JSONField(name = "flag")
        private Integer flag;
        @JSONField(name = "dlLevel")
        private String dlLevel;
        @JSONField(name = "subp")
        private Integer subp;
        @JSONField(name = "fl")
        private Integer fl;
        @JSONField(name = "fee")
        private Integer fee;
        @JSONField(name = "dl")
        private Integer dl;
        @JSONField(name = "plLevel")
        private String plLevel;
        @JSONField(name = "maxBrLevel")
        private String maxBrLevel;
        @JSONField(name = "rightSource")
        private Integer rightSource;
        @JSONField(name = "maxbr")
        private Integer maxbr;
        @JSONField(name = "id")
        private Integer id;
        @JSONField(name = "sp")
        private Integer sp;
        @JSONField(name = "payed")
        private Integer payed;
        @JSONField(name = "st")
        private Integer st;
        @JSONField(name = "chargeInfoList")
        private List<ChargeInfoListDTO> chargeInfoList;
        @JSONField(name = "freeTrialPrivilege")
        private FreeTrialPrivilegeDTO freeTrialPrivilege;
        @JSONField(name = "downloadMaxbr")
        private Integer downloadMaxbr;
        @JSONField(name = "downloadMaxBrLevel")
        private String downloadMaxBrLevel;
        @JSONField(name = "cp")
        private Integer cp;
        @JSONField(name = "preSell")
        private Boolean preSell;
        @JSONField(name = "playMaxBrLevel")
        private String playMaxBrLevel;
        @JSONField(name = "cs")
        private Boolean cs;
        @JSONField(name = "toast")
        private Boolean toast;
        @JSONField(name = "playMaxbr")
        private Integer playMaxbr;
        @JSONField(name = "flLevel")
        private String flLevel;
        @JSONField(name = "pl")
        private Integer pl;

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String getDlLevel() {
            return dlLevel;
        }

        public void setDlLevel(String dlLevel) {
            this.dlLevel = dlLevel;
        }

        public Integer getSubp() {
            return subp;
        }

        public void setSubp(Integer subp) {
            this.subp = subp;
        }

        public Integer getFl() {
            return fl;
        }

        public void setFl(Integer fl) {
            this.fl = fl;
        }

        public Integer getFee() {
            return fee;
        }

        public void setFee(Integer fee) {
            this.fee = fee;
        }

        public Integer getDl() {
            return dl;
        }

        public void setDl(Integer dl) {
            this.dl = dl;
        }

        public String getPlLevel() {
            return plLevel;
        }

        public void setPlLevel(String plLevel) {
            this.plLevel = plLevel;
        }

        public String getMaxBrLevel() {
            return maxBrLevel;
        }

        public void setMaxBrLevel(String maxBrLevel) {
            this.maxBrLevel = maxBrLevel;
        }

        public Integer getRightSource() {
            return rightSource;
        }

        public void setRightSource(Integer rightSource) {
            this.rightSource = rightSource;
        }

        public Integer getMaxbr() {
            return maxbr;
        }

        public void setMaxbr(Integer maxbr) {
            this.maxbr = maxbr;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSp() {
            return sp;
        }

        public void setSp(Integer sp) {
            this.sp = sp;
        }

        public Integer getPayed() {
            return payed;
        }

        public void setPayed(Integer payed) {
            this.payed = payed;
        }

        public Integer getSt() {
            return st;
        }

        public void setSt(Integer st) {
            this.st = st;
        }

        public List<ChargeInfoListDTO> getChargeInfoList() {
            return chargeInfoList;
        }

        public void setChargeInfoList(List<ChargeInfoListDTO> chargeInfoList) {
            this.chargeInfoList = chargeInfoList;
        }

        public FreeTrialPrivilegeDTO getFreeTrialPrivilege() {
            return freeTrialPrivilege;
        }

        public void setFreeTrialPrivilege(FreeTrialPrivilegeDTO freeTrialPrivilege) {
            this.freeTrialPrivilege = freeTrialPrivilege;
        }

        public Integer getDownloadMaxbr() {
            return downloadMaxbr;
        }

        public void setDownloadMaxbr(Integer downloadMaxbr) {
            this.downloadMaxbr = downloadMaxbr;
        }

        public String getDownloadMaxBrLevel() {
            return downloadMaxBrLevel;
        }

        public void setDownloadMaxBrLevel(String downloadMaxBrLevel) {
            this.downloadMaxBrLevel = downloadMaxBrLevel;
        }

        public Integer getCp() {
            return cp;
        }

        public void setCp(Integer cp) {
            this.cp = cp;
        }

        public Boolean getPreSell() {
            return preSell;
        }

        public void setPreSell(Boolean preSell) {
            this.preSell = preSell;
        }

        public String getPlayMaxBrLevel() {
            return playMaxBrLevel;
        }

        public void setPlayMaxBrLevel(String playMaxBrLevel) {
            this.playMaxBrLevel = playMaxBrLevel;
        }

        public Boolean getCs() {
            return cs;
        }

        public void setCs(Boolean cs) {
            this.cs = cs;
        }

        public Boolean getToast() {
            return toast;
        }

        public void setToast(Boolean toast) {
            this.toast = toast;
        }

        public Integer getPlayMaxbr() {
            return playMaxbr;
        }

        public void setPlayMaxbr(Integer playMaxbr) {
            this.playMaxbr = playMaxbr;
        }

        public String getFlLevel() {
            return flLevel;
        }

        public void setFlLevel(String flLevel) {
            this.flLevel = flLevel;
        }

        public Integer getPl() {
            return pl;
        }

        public void setPl(Integer pl) {
            this.pl = pl;
        }

        public static class FreeTrialPrivilegeDTO {
            @JSONField(name = "userConsumable")
            private Boolean userConsumable;
            @JSONField(name = "resConsumable")
            private Boolean resConsumable;

            public Boolean getUserConsumable() {
                return userConsumable;
            }

            public void setUserConsumable(Boolean userConsumable) {
                this.userConsumable = userConsumable;
            }

            public Boolean getResConsumable() {
                return resConsumable;
            }

            public void setResConsumable(Boolean resConsumable) {
                this.resConsumable = resConsumable;
            }
        }

        public static class ChargeInfoListDTO {
            @JSONField(name = "rate")
            private Integer rate;
            @JSONField(name = "chargeType")
            private Integer chargeType;

            public Integer getRate() {
                return rate;
            }

            public void setRate(Integer rate) {
                this.rate = rate;
            }

            public Integer getChargeType() {
                return chargeType;
            }

            public void setChargeType(Integer chargeType) {
                this.chargeType = chargeType;
            }
        }
    }

    public static class SongsDTO {
        @JSONField(name = "no")
        private Integer no;
        @JSONField(name = "rt")
        private String rt;
        @JSONField(name = "copyright")
        private Integer copyright;
        @JSONField(name = "fee")
        private Integer fee;
        @JSONField(name = "mst")
        private Integer mst;
        @JSONField(name = "pst")
        private Integer pst;
        @JSONField(name = "pop")
        private Integer pop;
        @JSONField(name = "dt")
        private Integer dt;
        @JSONField(name = "rtype")
        private Integer rtype;
        @JSONField(name = "s_id")
        private Integer sId;
        @JSONField(name = "rtUrls")
        private List<?> rtUrls;
        @JSONField(name = "resourceState")
        private Boolean resourceState;
        @JSONField(name = "id")
        private Integer id;
        @JSONField(name = "sq")
        private SqDTO sq;
        @JSONField(name = "st")
        private Integer st;
        @JSONField(name = "cd")
        private String cd;
        @JSONField(name = "publishTime")
        private Long publishTime;
        @JSONField(name = "cf")
        private String cf;
        @JSONField(name = "originCoverType")
        private Integer originCoverType;
        @JSONField(name = "h")
        private HDTO h;
        @JSONField(name = "mv")
        private Integer mv;
        @JSONField(name = "al")
        private AlDTO al;
        @JSONField(name = "l")
        private LDTO l;
        @JSONField(name = "m")
        private MDTO m;
        @JSONField(name = "version")
        private Integer version;
        @JSONField(name = "cp")
        private Integer cp;
        @JSONField(name = "alia")
        private List<?> alia;
        @JSONField(name = "djId")
        private Integer djId;
        @JSONField(name = "single")
        private Integer single;
        @JSONField(name = "ar")
        private List<ArDTO> ar;
        @JSONField(name = "ftype")
        private Integer ftype;
        @JSONField(name = "t")
        private Integer t;
        @JSONField(name = "v")
        private Integer v;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "mark")
        private Long mark;

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public String getRt() {
            return rt;
        }

        public void setRt(String rt) {
            this.rt = rt;
        }

        public Integer getCopyright() {
            return copyright;
        }

        public void setCopyright(Integer copyright) {
            this.copyright = copyright;
        }

        public Integer getFee() {
            return fee;
        }

        public void setFee(Integer fee) {
            this.fee = fee;
        }

        public Integer getMst() {
            return mst;
        }

        public void setMst(Integer mst) {
            this.mst = mst;
        }

        public Integer getPst() {
            return pst;
        }

        public void setPst(Integer pst) {
            this.pst = pst;
        }

        public Integer getPop() {
            return pop;
        }

        public void setPop(Integer pop) {
            this.pop = pop;
        }

        public Integer getDt() {
            return dt;
        }

        public void setDt(Integer dt) {
            this.dt = dt;
        }

        public Integer getRtype() {
            return rtype;
        }

        public void setRtype(Integer rtype) {
            this.rtype = rtype;
        }

        public Integer getSId() {
            return sId;
        }

        public void setSId(Integer sId) {
            this.sId = sId;
        }

        public List<?> getRtUrls() {
            return rtUrls;
        }

        public void setRtUrls(List<?> rtUrls) {
            this.rtUrls = rtUrls;
        }

        public Boolean getResourceState() {
            return resourceState;
        }

        public void setResourceState(Boolean resourceState) {
            this.resourceState = resourceState;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public SqDTO getSq() {
            return sq;
        }

        public void setSq(SqDTO sq) {
            this.sq = sq;
        }

        public Integer getSt() {
            return st;
        }

        public void setSt(Integer st) {
            this.st = st;
        }

        public String getCd() {
            return cd;
        }

        public void setCd(String cd) {
            this.cd = cd;
        }

        public Long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(Long publishTime) {
            this.publishTime = publishTime;
        }

        public String getCf() {
            return cf;
        }

        public void setCf(String cf) {
            this.cf = cf;
        }

        public Integer getOriginCoverType() {
            return originCoverType;
        }

        public void setOriginCoverType(Integer originCoverType) {
            this.originCoverType = originCoverType;
        }

        public HDTO getH() {
            return h;
        }

        public void setH(HDTO h) {
            this.h = h;
        }

        public Integer getMv() {
            return mv;
        }

        public void setMv(Integer mv) {
            this.mv = mv;
        }

        public AlDTO getAl() {
            return al;
        }

        public void setAl(AlDTO al) {
            this.al = al;
        }

        public LDTO getL() {
            return l;
        }

        public void setL(LDTO l) {
            this.l = l;
        }

        public MDTO getM() {
            return m;
        }

        public void setM(MDTO m) {
            this.m = m;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Integer getCp() {
            return cp;
        }

        public void setCp(Integer cp) {
            this.cp = cp;
        }

        public List<?> getAlia() {
            return alia;
        }

        public void setAlia(List<?> alia) {
            this.alia = alia;
        }

        public Integer getDjId() {
            return djId;
        }

        public void setDjId(Integer djId) {
            this.djId = djId;
        }

        public Integer getSingle() {
            return single;
        }

        public void setSingle(Integer single) {
            this.single = single;
        }

        public List<ArDTO> getAr() {
            return ar;
        }

        public void setAr(List<ArDTO> ar) {
            this.ar = ar;
        }

        public Integer getFtype() {
            return ftype;
        }

        public void setFtype(Integer ftype) {
            this.ftype = ftype;
        }

        public Integer getT() {
            return t;
        }

        public void setT(Integer t) {
            this.t = t;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getMark() {
            return mark;
        }

        public void setMark(Long mark) {
            this.mark = mark;
        }

        public static class SqDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class HDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class AlDTO {
            @JSONField(name = "picUrl")
            private String picUrl;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "tns")
            private List<?> tns;
            @JSONField(name = "pic_str")
            private String picStr;
            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "pic")
            private Long pic;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<?> getTns() {
                return tns;
            }

            public void setTns(List<?> tns) {
                this.tns = tns;
            }

            public String getPicStr() {
                return picStr;
            }

            public void setPicStr(String picStr) {
                this.picStr = picStr;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Long getPic() {
                return pic;
            }

            public void setPic(Long pic) {
                this.pic = pic;
            }
        }

        public static class LDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class MDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class ArDTO {
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "tns")
            private List<?> tns;
            @JSONField(name = "alias")
            private List<?> alias;
            @JSONField(name = "id")
            private Integer id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<?> getTns() {
                return tns;
            }

            public void setTns(List<?> tns) {
                this.tns = tns;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }
        }
    }
}
