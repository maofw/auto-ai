package com.ai.generator.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: QIK
 * @CreateDate: 2019/4/30 18:37
 */
@Data
public class ExportDataDTO<T> implements Serializable {
    private String sheetName;
    private String fileName;
    private String title;
    private List<T> dataList;
    
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
}
