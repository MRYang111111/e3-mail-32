package cn.yxd.searchsolr.controller;

import cn.yxd.common.pojo.SearchItem;
import cn.yxd.common.pojo.SearchResult;
import cn.yxd.search.service.SearchItemService;
import cn.yxd.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Value(value = "${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping(value = "/search")
    public String search(String keyword, @RequestParam(required = true,defaultValue = "1") Integer page, Model model) throws UnsupportedEncodingException {
        //keyword = new String(keyword.getBytes("iis8859-1"), "utf-8");
        keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        SearchResult searchReault = searchService.getSearchReault(keyword, page, SEARCH_RESULT_ROWS);
        List<SearchItem> getItemList = searchReault.getGetItemList();
        for (SearchItem searchItem:getItemList){
            System.out.println(searchItem);
        }
        model.addAttribute("query",keyword);
        model.addAttribute("page",page);
        model.addAttribute("recourdCount",searchReault.getRecourdCount());
        model.addAttribute("itemList",searchReault.getGetItemList());
        model.addAttribute("totalPages",searchReault.getTotalPages());

        return "search";
    }






}
