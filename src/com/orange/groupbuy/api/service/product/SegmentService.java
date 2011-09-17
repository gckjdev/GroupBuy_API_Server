package com.orange.groupbuy.api.service.product;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import com.orange.groupbuy.api.service.CommonGroupBuyService;
import com.orange.groupbuy.constant.ErrorCode;
import com.orange.groupbuy.constant.ServiceConstant;

public class SegmentService extends CommonGroupBuyService {
	
	String segment;
	Dictionary dic;
	List<String> stopwordsList;

	@Override
	public String toString() {
		return "SegmentService [segment=" + segment + "]";
	}

	@Override
	public boolean setDataFromRequest(HttpServletRequest request) {
		segment = request.getParameter(ServiceConstant.PARA_SEGMENT);
		if (!check(segment, ErrorCode.ERROR_PARAMETER_SEGMENT_EMPTY, ErrorCode.ERROR_PARAMETER_SEGMENT_NULL)) {
			return false;
		}
		dic = Dictionary.getInstance();
		stopwordsList = new ArrayList<String>();
		addStopwords();
		return true;
	}


	@Override
	public boolean needSecurityCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleData() {		
		try {
			String segmentWord = segWords(segment, " | ");
			if (segmentWord == null || segmentWord.isEmpty())
				return;
			String[] segmentWords = segmentWord.split("\\|");
			JSONArray array = new JSONArray();
			for (int i=0; i<segmentWords.length; i++){
				array.add(segmentWords[i]);
			}
			resultData = array;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

	private void addStopwords() {
		stopwordsList.add("仅售");
		stopwordsList.add("包邮");
		stopwordsList.add("原价");
		stopwordsList.add("元");
		stopwordsList.add("的");
		stopwordsList.add("全国");
		stopwordsList.add("抢购");
	}

	protected Seg getSeg() {
		return new ComplexSeg(dic);
	}
	
	public String segWords(Reader input, String wordSpilt) throws IOException {
		StringBuilder sb = new StringBuilder();
		Seg seg = getSeg();	//取得不同的分词具体算法
		MMSeg mmSeg = new MMSeg(input, seg);
		Word word = null;
		boolean first = true;
		while((word=mmSeg.next())!=null) {
			String w = word.getString();
			// if in the stopwordsList, then pass
			if (stopwordsList.contains(w)) {
				continue;
			}
			if(!first) {
				sb.append(wordSpilt);
			}
			sb.append(w);
			first = false;		
		}
		return sb.toString();
	}
	
	public String segWords(String txt, String wordSpilt) throws IOException {
		return segWords(new StringReader(txt), wordSpilt);
	}
	
}
