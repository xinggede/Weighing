package com.xing.weight.server.http.api;

import com.xing.weight.bean.AddPoundResultInfo;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.bean.StyleInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.bean.request.RequestList;
import com.xing.weight.server.http.response.BasicResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MainApi {

    @POST("/api/goods/getPageList")
    Observable<BasicResponse<PageList<GoodsDetail>>> getGoods(@Body RequestList requestList); //获取货品列表

    @POST("/api/goods/delete/{id}")
    Observable<BasicResponse<Object>> deleteGoods(@Path("id") int goodsId);

    @POST("/api/goods/add")
    Observable<BasicResponse<Object>> addGoods(@Body GoodsDetail requestBody); //添加商品

    @POST("/api/goods/update")
    Observable<BasicResponse<Object>> updateGoods(@Body GoodsDetail requestBody); //更新商品

    @POST("/api/customer/getPageList")
    Observable<BasicResponse<PageList<CustomerInfo>>> getCustomer(@Body RequestList requestList); //获取货品列表

    @POST("/api/customer/delete/{id}")
    Observable<BasicResponse<Object>> deleteCustomer(@Path("id") int cId);

    @POST("/api/customer/add")
    Observable<BasicResponse<Object>> addCustomer(@Body CustomerInfo requestBody); //添加商品

    @POST("/api/customer/update")
    Observable<BasicResponse<Object>> updateCustomer(@Body CustomerInfo requestBody); //更新商品

    @POST("/api/printer/getPageList")
    Observable<BasicResponse<PageList<PrinterInfo>>> getPrinter(@Body RequestList requestList); //添加商品

    @POST("/api/printer/add")
    Observable<BasicResponse<Object>> addPrinter(@Body PrinterInfo requestBody); //添加商品

    @POST("/api/printer/update")
    Observable<BasicResponse<Object>> updatePrinter(@Body PrinterInfo requestBody); //添加商品

    @POST("/api/printer/delete/{id}")
    Observable<BasicResponse<Object>> deletePrinter(@Path("id") int pId);

    @GET("/api/company/info/{id}")
    Observable<BasicResponse<CompanyInfo>> getCompanyInfo(@Path("id") int cid); //获取企业信息

    @POST("/api/company/add")
    Observable<BasicResponse<CompanyInfo>> addCompanyInfo(@Body CompanyInfo companyInfo);

    @POST("/api/company/update")
    Observable<BasicResponse<CompanyInfo>> updateCompanyInfo(@Body CompanyInfo companyInfo);

    @POST("/api/templetCfg/getPageList")
    Observable<BasicResponse<PageList<TemplateInfo>>> getTemplate(@Body RequestList requestList);

    @POST("/api/templetCfg/delete/{id}")
    Observable<BasicResponse<Object>> deleteTemplate(@Path("id") int tId);

    @POST("/api/templetCfg/add")
    Observable<BasicResponse<Object>> addTemplate(@Body TemplateInfo templateInfo);

    @POST("/api/templetCfg/update")
    Observable<BasicResponse<Object>> updateTemplate(@Body TemplateInfo templateInfo);

    @POST("/api/bills/add")
    Observable<BasicResponse<AddPoundResultInfo>> addPound(@Body PoundInfo poundInfo); //添加磅单

    @POST("/api/bills/update")
    Observable<BasicResponse<AddPoundResultInfo>> updatePound(@Body PoundInfo poundInfo); //修改磅单

    @POST("/api/bills/getPageList")
    Observable<BasicResponse<PageList<PoundInfo>>> getPounds(@Body RequestList requestList); //获取磅单

    @POST("/api/style/getPageList")
    Observable<BasicResponse<PageList<StyleInfo>>> getStyle(@Body RequestList requestList); //获取货品列表

}
