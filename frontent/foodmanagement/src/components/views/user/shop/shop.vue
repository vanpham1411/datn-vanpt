<template>
    <div class="user-page">
        <!-- <banner-common :title="'Shop'" :displayTitle="'Shop'"></banner-common> -->
        <div class="content-action flex" style="padding: 15px 20px;">
            <div class="action-left flex" style="flex: 1;">
                <div class="content-filter flex">
                    <input placeholder="Tìm kiếm sản phẩm" 
                            type="text" 
                            ref="Filter" 
                            id="filter"
                            v-model="keyword"
                            v-tooltip.bottom-end="{offset: '5px', content:'Nhấp Enter để tìm kiếm', classes:'tooltip'}" @keyup.enter="getFilterPage()">
                    <div class="content-filter-search logo-icon other-icon pointer"></div>
                </div>
                <vue-combobox class="w-1/3 category-data m-l-12" 
                            @getSelect="category_model=$event;getFilterPage();"
                            :combobox_valid="false" 
                            :item_text="'name'" 
                            :items="categoryList" 
                            :vmodel="category_model"
                            :multiple="false"
                            :readonly="false">
                </vue-combobox>
            </div>
            <div class="action-right flex">
                <div class="filter-pop" style="position:relative;">
                    <button class="btn-filter-bar flex-center admin-btn-normal m-r-12" style="padding:5px" type="button" id="btnFilterToggle" @click="showFilter=!showFilter;">
                        <i class="fa fa-filter"></i>
                        <span>Lọc</span>
                    </button>
                    <div class="pop-filter" :class="{'show-filter':showFilter, 'h-0':!showFilter}">
                        <div class="other-icon logo-icon btn-close pointer" id="btn-close" style="position: absolute;right: 5px;top: 5px;"
                                @click="showFilter=false;">
                        </div>
                        <div class="pop-init">
                            <div class="pop-info1">
                                <div class="m-b-3">
                                </div>
                            </div>
                            <div class="pop-info3">
                            </div>
                            <div class="form-footer pop-footer flex">
                                <div class="admin-btn-normal btn-delete" @click="resetFilter()">Đặt lại</div>
                                <div class="admin-btn-normal admin-btn-primary" @click="filterMore()">Lọc</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="btn-filter-bar btn-action flex-center admin-btn-normal" style="padding:0 5px" v-tooltip.bottom="{content:'Lấy lại dữ liệu', classes:'tooltip', offset: '5px'}" @click="reload()">
                    <div class="refresh-icon other-icon logo-icon pointer"></div>
                </div>
            </div>
        </div>
        <div class="content-shop" style="text-align: center;">
            <router-link tag="div" :to="'/user/menu/'+data.productID" class="food-item w-1/3 pointer" v-for="(data) in dataList.data" :key="data.productID">
                <div class="flex-center food-init">
                    <div class="w-2/5 m-r-12">
                        <div class="img-food"><img :src= "data.imageURL" alt=""></div>
                        <span class="sale" v-if="data.HasDiscount">Sale!</span>
                    </div>
                    <div class="w-3/5 m-l-12">
                        <div class="food-name"><b>{{data.name}}</b></div>
                        <!-- <div class="food-title">{{data.Title}}</div> -->
                        <div class="food-size flex">
                            <div class="w-3/4">Giá: <b :class="{'line-through': data.HasDiscount}">{{data.cost}}</b> <b v-if="data.HasDiscount">{{formatNumber(data.cost)}}</b> VND</div>
                        </div>
                    </div>
                </div>
            </router-link>
        </div>
        <div class="paging-bar flex-center" v-if="dataList.totalRecord>0">
            <p class="list-show">Hiển thị: <b>{{dataList.data.length}} / {{dataList.totalRecord}}</b> sản phẩm</p>
            <div class="left-footer flex-center">
                <div class="page-size">
                    <!-- @click="menuTop" -->
                    <vue-select :top="true" :item_text="'text'" :items="pages" :vmodel="page_model" @getSelect="getPageSelect($event)"></vue-select>
                </div>
                <!-- <v-pagination v-model="currentPage" :page-count="totalPages" :classes="bootstrapPaginationClasses" :labels="customLabels" @change="$emit('getFilterPage')"></v-pagination> -->
            </div>
        </div>
    </div>
</template>

<script>
// import BannerCommon from '../common/BannerCommon.vue';
import VueCombobox from '../../../base/VueCombobox.vue';
import VueSelect from '../../../base/VueSelect.vue'
import CategoryAPI from '../../../../api/component/food/CategoryAPI';
import ProductAPI from '../../../../api/component/food/ProductAPI';
import { Pages } from '../../../../base/vi/Resources.js';

import Base from '../../../../base/Base';

export default {
    components: {
        VueCombobox,
        VueSelect,

    },
    name: "Shop",
    data() {
        return {
            imgPath:'../../../../../data/',
            category_model: {
                categoryId: '',
                name:'',
                code:''
            },
            keyword:null,
            currentPage:1,
            totalPages:1,
            totalRecord:0,
            // lớp của thư viện pagination
            bootstrapPaginationClasses: {
                ul: 'pagination',
                li: 'page-item',
                liActive: 'active',
                liDisable: 'disabled',
                button: 'page-link'
            },
            customLabels: {
                first: false,
                prev: 'Trước',
                next: 'Sau',
                last: false
            },
            // lấy dữ liệu số lượng bản ghi trên 1 trang
            pages: Pages,
            // số bản ghi trên trang
            page_model: Pages[1],
            // tổng số bản ghi trên bảng
            pageSize: 20,

            categoryList:[{
                categoryId:'',
                code:'',
                name: 'Tất cả danh mục'
            }],
            dataList:[],
            paramGet:{
                keyword:null,
                parentID:null,
                pageNumber:1,
                pageSize:20
            },
            showFilter:false,

        }
    },
    methods: {
        async getFilterPage() {
            this.paramGet = {
                keyword: this.keyword,
                parentID: this.category_model.categoryId?this.category_model.categoryId:null,
                pageNumber: this.currentPage,
                pageSize: this.pageSize
            };
            this.dataList = await ProductAPI.getFilterPaging(this.paramGet);
            this.dataList.data.forEach(ele => {
                ele.imageURL = this.imgPath + ele.imageURL;
            })
            this.totalPages = this.dataList.totalPage;
            if(this.dataList.data.length > 0){
                this.dataList.data.forEach(ele => {  
                    ele.cost = Base.formatNumber(ele.cost);
                });
            }
        },
        async reload(){
            this.keyword = null;
            await this.getFilterPage();
        },
        resetFilter(){},
        filterMore(){},
        formatNumber(val){
            return Base.formatNumber(val);
        }
    },

    async created() {
        try {
            if(document.querySelector(".search-food input")){
                this.keyword = document.querySelector(".search-food input").value;
                document.querySelector(".search-food input").value = "";
                this.paramGet.keyword = this.keyword;
            }
            this.paramGet.parentID = this.$route.params.categoryID?this.$route.params.categoryId:null;

            let categoryData = await CategoryAPI.getFilterPaging({CategoryFilter:null,CategoryStatus:null});
            this.categoryList = this.categoryList.concat(categoryData.data);
            this.dataList = await ProductAPI.getFilterPaging(this.paramGet);
            this.dataList.data.forEach(ele => {
                ele.imageURL = this.imgPath + ele.imageURL;
            })

            console.log("get list product: ", this.dataList)
            this.totalPages = this.dataList.totalPage;
            if(this.dataList.data.length > 0){
                this.dataList.data.forEach(ele => {
                    if(ele.DiscountId&&(new Date(ele.DiscountStartDate) <= new Date())&&(new Date(ele.DiscountEndDate) >= new Date())){
                        ele.HasDiscount = true;
                        if(ele.Amount*ele.DiscountAmount/100 > ele.DiscountMaxAmount){
                            ele.RealAmount = ele.Amount - ele.DiscountMaxAmount;
                        }
                        else {
                            ele.RealAmount = ele.Amount - ele.Amount*ele.DiscountAmount/100;
                        }
                    }
                    else {
                        ele.HasDiscount = false;
                        ele.RealAmount = ele.Amount;
                    }
                    ele.Amount = Base.formatNumber(ele.Amount);
                });
            }
            if(this.paramGet.parentID){
              this.category_model = this.categoryList.filter(x => x.categoryId == this.paramGet.parentID)[0];
            }
        } catch (error) {
            console.log(error);
        }
    }


}
</script>

<style>
.user-page input{
    border: 1px solid var(--gray-color);
    border-radius: 3px;
    padding: 0 7px;
    width: 100%;
}
.food-item{
    display: inline-block;
    min-width: 500px;
    text-align: left;
    font-family: 'GoogleSans-Regular';
}
.food-item .food-init{
    border: 1px solid var(--gray-color);
    margin: 10px 20px;
    border-radius: 20px;
    padding: 20px;
    transition: .2s;
    position: relative;
}
.food-item .food-init:hover{
    background: #fff9e9;
}
.food-item .food-init:hover img{
    transform: scale(1.1);
}
.food-item .food-init .food-title{
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    display: -webkit-box;
}
.food-item .food-init:hover .food-name{
    font-size: 20px;
}
.food-item .food-init .food-name{
    min-height: 60px;
    font-size: 18px;
    height: 35px;
    color: var(--primary-color);
    transition: .2s;
}
.food-item .food-init .food-size{
    font-size: 14px;
    margin-top: 7px;
}
.food-item .img-food {
    text-align: center;
    height: 150px;
}
.food-item .img-food img{
    width: 100%;
    border-radius: 20px;
    height: 150px;
    transition: .2s;
}
.user-page .paging-bar{
    justify-content: space-between;
    padding: 20px;
    position: sticky;
    bottom: 0;
    background-color: #fff;
}
.img-food .sale,
.food-init .sale{
    position: absolute;
    top: 10px;
    padding: 5px 7px;
    background: #000;
    border-radius: 45%;
    color: #fff;
    left: 10px;
}
.line-through{
    text-decoration: line-through;
}
</style>