package com.as.entity.files;

import java.math.BigDecimal;
import java.util.Date;

import com.as.util.PageData;

public class FileInfo {

	private String id;

    private String corpId;

    private String fileName;

    private Long fileSize;

    private String fileType;

    private String fileUrl;
    
    private String pdfUrl;

    private String fileTemplate;

    private String fileStatus;

    private String rsvStr1;

    private String rsvStr2;

    private BigDecimal rsvNum1;

    private BigDecimal rsvNum2;

    private String remark;

    private Date createDate;

    private Date modifyDate;

    private String operator;

    private Long gCatelogId;

    public String getId() {
        return id;
    }

	public void setId(String id) {
		this.id = id;
	}

    public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(String fileTemplate) {
        this.fileTemplate = fileTemplate == null ? null : fileTemplate.trim();
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus == null ? null : fileStatus.trim();
    }

    public String getRsvStr1() {
        return rsvStr1;
    }

    public void setRsvStr1(String rsvStr1) {
        this.rsvStr1 = rsvStr1 == null ? null : rsvStr1.trim();
    }

    public String getRsvStr2() {
        return rsvStr2;
    }

    public void setRsvStr2(String rsvStr2) {
        this.rsvStr2 = rsvStr2 == null ? null : rsvStr2.trim();
    }

    public BigDecimal getRsvNum1() {
        return rsvNum1;
    }

    public void setRsvNum1(BigDecimal rsvNum1) {
        this.rsvNum1 = rsvNum1;
    }

    public BigDecimal getRsvNum2() {
        return rsvNum2;
    }

    public void setRsvNum2(BigDecimal rsvNum2) {
        this.rsvNum2 = rsvNum2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

	public Long getgCatelogId() {
		return gCatelogId;
	}

	public void setgCatelogId(Long gCatelogId) {
		this.gCatelogId = gCatelogId;
	}

	public String toString() {
		// a.jpg|1024|jpg|/00/00/ZcmPE1cOA5KAe8F9AAALT1_qZKk155.jpg
		return fileName + "|" + fileSize + "|" + fileType + "|" + fileUrl + "|" + id + "|" + pdfUrl;
	}
	public PageData toPageData() {
		PageData pd = new PageData();
		pd.put("ID", id);
		pd.put("CORP_ID", corpId);
		pd.put("FILE_NAME", fileName);
		pd.put("FILE_SIZE", fileSize);
		pd.put("FILE_TYPE", fileType);
		pd.put("FILE_URL", fileUrl);
		pd.put("PDF_URL", pdfUrl);
		pd.put("FILE_STATUS", fileStatus);
		pd.put("CREATE_DATE", createDate);
		pd.put("MODIFY_DATE", modifyDate);
		pd.put("OPERATOR", operator);
		return pd;
	}
}