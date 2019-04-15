package cn.yxd.controller;

import cn.yxd.common.util.E3Result;
import cn.yxd.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchItemList {
    @Autowired
    private SearchItemService searchItemService;
    @RequestMapping(value ="/index/item/import" )
    @ResponseBody
    public E3Result getSearchItemList() throws  Exception{
        E3Result importItemSearch = searchItemService.getImportItemSearch();
        return  importItemSearch;

    }


}
