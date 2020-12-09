package com.xing.weight.server.http.api;

import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.PrinterInfo;
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

    @DELETE("/api/goods/delete/{id}")
    Observable<BasicResponse<Object>> deleteGoods(@Path("id") int goodsId);

    @POST("/api/goods/add")
    Observable<BasicResponse<Object>> addGoods(@Body GoodsDetail requestBody); //添加商品

    @POST("/api/goods/update")
    Observable<BasicResponse<Object>> updateGoods(@Body GoodsDetail requestBody); //更新商品

    @POST("/api/customer/getPageList")
    Observable<BasicResponse<PageList<CustomerInfo>>> getCustomer(@Body RequestList requestList); //获取货品列表

    @DELETE("/api/customer/delete/{id}")
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

    @DELETE("/api/printer/delete/{id}")
    Observable<BasicResponse<Object>> deletePrinter(@Path("id") int pId);

    @GET("/api/company/info/{id}")
    Observable<BasicResponse<CompanyInfo>> getCompanyInfo(@Path("id") int cid); //获取企业信息

    @POST("/api/company/add")
    Observable<BasicResponse<CompanyInfo>> addCompanyInfo(@Body CompanyInfo companyInfo);

    @GET("/api/company/update")
    Observable<BasicResponse<CompanyInfo>> updateCompanyInfo(@Body CompanyInfo companyInfo);


//
//    @PUT("/v3/companyinfo")
//    Observable<BasicResponse<Object>> setCompanyinfo(@Body UserInfo.CompanyInfo companyInfo);
//
//    @GET("/v3/invoices/styles")
//    Observable<BasicResponse<List<UserInfo.ValueInfo>>> getInvoicestyle(); //获取当前发票样式
//
//    @PUT("/v3/invoices/styles")
//    Observable<BasicResponse<Object>> setInvoicestyle(@Body RequestBody requestBody);
//
//    @POST("/v3/commodities")
//    Observable<BasicResponse<Object>> addCommodity(@Body RequestCommodity requestBody); //添加商品
//
//    @PUT("/v3/commodities/{id}")
//    Observable<BasicResponse<Object>> updateCommodity(@Path("id") int customerId, @Body RequestCommodity requestBody); //更新商品
//
//    @DELETE("/v3/commodities/{id}")
//    Observable<BasicResponse<Object>> deleteCommodity(@Path("id") int customerId); //删除商品
//
//    @GET("/v3/commodities/{id}")
//    Observable<BasicResponse<Goods.GoodsDetail>> getCommodity(@Path("id") int customerId); //获取商品
//
//    @GET("/v3/commodities/search")
//    Observable<BasicResponse<Goods>> searchCommodity(@QueryMap Map<String, String> map); //查询商品
//
//
//    @GET("/v3/taxcodes")
//    Observable<BasicResponse<GoodsCode>> getTaxCode(@QueryMap Map<String, String> map); //获取商品编码列表
//
//    @GET("/v3/taxcodes/default")
//    Observable<BasicResponse<GoodsCode.CodeDetail>> getDefaultTaxCode(); //获取商品编码列表
//
//    @POST("/v3/printers/add")
//    Observable<BasicResponse<Object>> addPrinter(@Body RequestBody requestBody); //添加打印机
//
//    @GET("/v3/printers")
//    Observable<BasicResponse<PrinterInfo>> getPrinter(); //获取打印机列表
//
//    @POST("/v3/printers/select")
//    Observable<BasicResponse<Object>> selectPrinter(@Body RequestBody requestBody); //选择打印机
//
//    @POST("/v3/printers/print")
//    Observable<BasicResponse<PrintResult>> print(@Body RequestBody requestBody); //网络打印
//
//
//    @GET("/v3/settings/server")
//    Observable<BasicResponse<ServerInfo>> getServer();
//
//    @PUT("/v3/settings/server")
//    Observable<BasicResponse<Object>> setServer(@Body RequestBody requestBody);
//
//    @POST("/v3/settings/server/init")
//    Observable<BasicResponse<Object>> setInitServer(@Body ServerInfo serverInfo);
//
//    @GET("/version/android")
//    Observable<BasicResponse<VerInfo>> checkVersion(@QueryMap Map<String, String> map);
//
//
//
//    @GET("/v3/values/taxationmethod")
//    Observable<BasicResponse<List<UserInfo.ValueInfo>>> getTaxationMethod();//获取征税方式列表
//
//    @GET("/v3/taxservers")
//    Observable<BasicResponse<List<UserInfo.ValueInfo>>> getTaxServers();//获取税控服务商列表
//
//    @GET("/v3/values/policy")
//    Observable<BasicResponse<List<UserInfo.ValueInfo>>> getPolicy();//获取优惠政策列表
//
//    @GET("/v3/values/zerotaxtype")
//    Observable<BasicResponse<List<UserInfo.ValueInfo>>> getZeroTaxType();//获取零税率标识列表
//
//    @GET("/v3/values/invoicecategory")
//    Observable<BasicResponse<List<UserInfo.ValueInfo>>> getInvoiceCategory();//获取发票类型（即发票样式的大类）列表

}
