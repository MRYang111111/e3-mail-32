package cn.yxd.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
    private Long totalPages;
    private Long recourdCount;
    private List<SearchItem> getItemList;

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(Long recourdCount) {
        this.recourdCount = recourdCount;
    }

    public List<SearchItem> getGetItemList() {
        return getItemList;
    }

    public void setGetItemList(List<SearchItem> getItemList) {
        this.getItemList = getItemList;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "totalPages=" + totalPages +
                ", recourdCount=" + recourdCount +
                ", getItemList=" + getItemList +
                '}';
    }
}
