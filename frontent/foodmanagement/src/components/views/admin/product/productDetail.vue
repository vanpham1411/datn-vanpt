<template>
<div class="detail food-detail">
    <!-- loading -->
    <div v-if="loader" :class="{'background-loader':loader}">
        <div :class="{loader:loader}">
            <object :data="loaderUrl"></object>
        </div>
    </div>
    <div class="add-item">
        <div class="flex content-head">
            <div class="content-title">Chi tiết sản phẩm</div>
        </div>
        <div class="add-entity">
            <div class="flex">
                <div class="w-3/5">
                    <div class="flex">
                        <div class="general-row w-1/2 m-r-12">
                            <p>Mã sản phẩm <span>*</span></p>
                            <input :readonly="readonly" v-model="product.code" ref="ProductCode" id="ProductCode"
                                type="text" maxlength="20" tabindex="2" 
                                class="w-full form-control" :class="{ 'is-invalid': $v.product.code.$error }"
                                @blur="$v.product.code.$touch()">
                            <div v-if="$v.product.code.$error" class="invalid-feedback">
                                <p v-if="!$v.product.code.required">Mã sản phẩm không được để trống.</p>
                                <p v-if="($v.product.code.required) && (!$v.product.code.startWithFC)">Mã sản phẩm phải bắt đầu bằng <i>'FC-'.</i></p>
                            </div>
                        </div>
                        <div class="general-row w-1/2">
                            <p>Tên sản phẩm <span>*</span></p>
                            <input :readonly="readonly" v-model="product.name" ref="ProductName" id="ProductName"
                                type="text" maxlength="50" tabindex="2" 
                                class="w-full form-control" :class="{ 'is-invalid': $v.product.name.$error }"
                                @blur="$v.product.name.$touch()">
                            <div v-if="$v.product.name.$error" class="invalid-feedback">
                                <p v-if="!$v.product.name.required">Tên sản phẩm không được để trống.</p>
                            </div>
                        </div>
                    </div>
                    <!-- <div class="general-row">
                        <p>Giới thiệu</p>
                        <input :readonly="readonly" tabindex="2" v-model="product.description">
                    </div> -->
                    <div class="general-row">
                        <p>Giá</p>
                        <input :readonly="readonly" tabindex="2" v-model="product.cost" @keyup="formatNumber">
                    </div>
                    <div class="general-row">
                        <p>Danh mục <span>*</span></p>
                        <vue-combobox @getSelect="categoryModel=$event;" 
                                    :combobox_valid="false" 
                                    :placeholder="''" 
                                    :readonly="readonly" 
                                    :item_text="['code','name']" 
                                    :display_item="'name'"
                                    :items="categoryList" 
                                    :groupName="['Mã danh mục','Tên danh mục']"
                                    :vmodel="categoryModel"
                                    :multiple="false">
                        </vue-combobox>
                    </div>
                    <div class="general-row">
                        <div class="row2-input" style="position: relative;">
                            
                        </div>
                    </div>
                    <div class="general-row">
                        <p>Mô tả</p>
                        <textarea :readonly="readonly" class="w-full" rows="2" v-model="product.description" placeholder="">
                        </textarea>                    
                    </div>
                </div>
                <div class="w-2/5 m-l-12">
                    <div class="flex">
                        <!-- <div class="general-row w-1/2 m-r-12">
                            <p>Lượt xem</p>
                            <input readonly="readonly" :value="product.FoodView">
                        </div> -->
                        <!-- <div class="general-row w-1/2" style="position: relative;">
                            <p>Danh sách mã gảm giá</p>
                            <vue-combobox @getSelect="getSelect('discountModel', $event)" 
                                        :combobox_valid="false" 
                                        :placeholder="''" 
                                        :item_text="['DiscountCode','DiscountAmount', 'DiscountMaxAmount', 'DiscountStartDate', 'DiscountEndDate']" 
                                        :items="discounts" 
                                        :vmodel="discountModel"
                                        :multiple="false"
                                        :groupName="['Mã giảm giá','% giảm giá', 'Giảm tối đa', 'Ngày bắt đầu', 'Ngày kết thúc']"
                                        :display_item="'DiscountCode'"
                                        :readonly="readonly">
                            </vue-combobox>
                        </div> -->
                        <!-- <div class="general-row w-1/2 center">
                            <p>Đánh giá</p>
                            <div class="rating">
                                <span class="stars-container" :class="'stars-'+star*20">★★★★★</span>
                            </div>
                        </div> -->
                    </div>
                    <div class="general-row">
                        <p>Hình ảnh</p>
                        <div class="center" style="">
                            <div class="flex-center m-r-12" style="min-width: fit-content;">
                                <input :readonly="readonly" id="upload-file" @change="getFile('#img-Product', $event)" ref="ImageURL" type="file">
                                <label class="pointer m-r-12" for="upload-file">Tải ảnh</label>
                                <div class="pointer" id="delete-image" @click="deleteFile('#img-Product')">Xóa ảnh</div>
                            </div>
                            <img alt=""  style="border-radius: 5px;width:220px;margin-top:10px;" id="img-Product" >
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="food-table">
            <table>
                <thead>
                    <tr>
                        <th class="center l-30" style="min-width:40px;">#</th>
                        <th style="min-width: 327px;">LOẠI</th>
                        <th style="min-width: 327px;">KÍCH THƯỚC</th>
                        <th style="min-width: 138px;" class="end">SỐ LƯỢNG</th>
                        <th style="min-width: 105px;">XÓA</th>
                        <th class="w-30" style="right:-1px; min-width: 31px;"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(itemDetail, index) in items" :key="index" :class="{'list-input':crudDetail!='read', 'read-input':crudDetail=='read'}">
                        <td class="center l-30">{{index+1}}</td>
                    
                        <td class=""><input :id="'typeName'+index" :readonly="readonly" type="text" style="text-align: end;" v-model="itemDetail.type.name" ></td>
                        <td class=""><input :readonly="readonly" type="text" style="text-align: end;" v-model="itemDetail.size"></td>

                        <td class=""><input :id="'quantity'+index" :readonly="readonly" type="text" style="text-align: end;" v-model="itemDetail.quantity"></td>
                        <td class="r-30">
                            <div class="delete-icon other-icon logo-icon pointer" @click="removeDetail(index)"></div>
                        </td>
                        <td class="w-30" style="right:-1px; min-width: 31px;"></td>
                    </tr>
                    <tr class="tr-input input-detail" v-if="showInputDetail">
                        <td class="center l-30">{{items.length+1}}</td>
                        <td class=""><input id="typeName" :readonly="readonly" type="text" style="text-align: end;" v-model="item.type.name" ></td>
                        <td class=""><input  :readonly="readonly" type="text" style="text-align: end;" v-model="item.size" ></td>
                        <td class=""><input id="quantity" :readonly="readonly" type="text" style="text-align: end;" v-model="item.quantity"></td>
                        
                        <td class="r-30">
                            <div class="delete-icon other-icon logo-icon pointer" @click="removeDetail('item_detail')"></div>
                        </td>
                        <td class="w-30" style="right:-1px; min-width: 31px;"></td>
                    </tr>
                </tbody>
            </table>
            <div class="table-actions flex">
                <div class="table-action pointer" @click="addDetail();showInputDetail=true">Thêm dòng</div>
                <div class="table-action pointer" @click="removeDetail('all')">Xóa hết dòng</div>
            </div>
        </div>
    </div>
    <div class="form-footer flex" :class="{'form-right':!fadeleft}">
        <div class="admin-btn-normal pointer btn-delete" tabindex="18" @click="btnXForm()" @keyup.enter="btnXForm()">Hủy</div>
        <div class="btn-save-all flex-center" v-if="crudDetail != 'read'">
            <div class="admin-btn-normal btn-save pointer" tabindex="16" @click="crudObject()" @keyup.enter="crudObject()">Cất</div>
            <!-- <div class="btn-footer btn-save-more" tabindex="17" @click="post_save = true;crudObject()" @keyup.enter="post_save = true,crudObject()">Cất và Thêm</div> -->
            <!-- <div class="admin-btn-normal admin-btn-primary pointer" v-if="!readonly" 
                    @click="post_save=true,crudObject()"
                    v-shortkey="['ctrl','shift','s']" @shortkey="post_save = true,crudObject()"
                    v-tooltip.top="{content:'Cất và thêm (Ctrl+ Shift + S)', classes:'tooltip', offset: '5px'}">Cất và thêm</div> -->
        </div>
        <div v-if="crudDetail == 'read'" class="btn-save-all">
            <div class="admin-btn-normal admin-btn-primary pointer" tabindex="16" @click="changeCrud('put')" @keyup.enter="changeCrud('put')">Sửa</div>
        </div>
    </div>
    <div>
        <div v-for="(mgs, index) in listMgs" :key="index">
            <toast-message :titleMgs="mgs.titleMgs" :iconToast="mgs.iconToast" v-if="mgs.showToast" @deleteToast="deleteToast(index)"></toast-message>
        </div>
        <vue-popup v-if="popShow" :popShow="popShow" :action="action" :idInvalid="idInvalid" @closeForm="closeForm" :message="message" :crud="crudDetail" @closePop="closePop" @crudObject="crudObject()"></vue-popup>
    </div>
</div>
</template>

<script>
import {
    required,
} from "vuelidate/lib/validators"
import ProductAPI from "../../../../api/component/food/ProductAPI";
import CategoryAPI from "../../../../api/component/food/CategoryAPI";
import { PopupState, CRUD, ToastMessageIcon } from '../../../../base/Resources.js';
import Base from "../../../../base/Base";
import VueCombobox from "../../../base/VueCombobox.vue";
import { ToastMgs } from '../../../../base/vi/Resources.js';
import VuePopup from '../../../../components/base/VuePopup.vue';
// import ProductDetailAPI from '../../../../api/component/food/ProductDetailAPI';
import ToastMessage from '../../../../components/base/ToastMessage';

export default {
    name: "ProductDetail",
    components: {
        VueCombobox,
        ToastMessage,
        VuePopup
    },
    props:{
        fadeleft: Boolean,
    },

    data() {
        return {
            readonly: false,
            popShow: false,
            showInputDetail:true,
            // action post, put, copy
            action: "",
            // message trong popup
            message:"",
            // hiển thị loading
            loader:false,
            loaderUrl: require('../../../../assets/lib/img/common/loading.svg'),
            // toast
            mgs: {
                titleMgs: "",
                showToast: false,
                iconToast: "",
            },
            listMgs: [],
            post_save: false,
            // id của ô input invalid đầu tiên
            idInvalid:"",
            categoryList:[],
            productItem: {
                product: {
                    productID: '',
                    code: '',
                    name: '',
                    imageURL: '',
                    categoryID:'',
                    cost:''
                },
                items: []
            },
            item: {
                itemID: '',
                size:'',
                quantity:'',
                type: {
                    typeID:'',
                    name:'',
                    describle:''
                }
            },
            dataDetailList: [],
            objectDetailName: 'ProductDetail',
            displayDetailName:'Chi tiết sản phẩm',
            formShow: false,
            crudDetail: '',
            categoryModel:{
                categoryId:'',
                name:'',
                code:''
            },
            discountModel:{
                discountId:'',
                code:'',
                title:'',
                amount:'',
                maxAmount:'',
                startDate:'',
                endDate:'',
            },
            discounts:[],
            sizes:[],
            // toppings:[],
            // topping_model:[],
            product: {
                productID:'',
                code:'',
                name:'',
                star:0,
                imageURL:'',
                cost:0,
            },
            items: [],
            star: 0,

        }
    },
    validations: {
        product: {
            code: {
                required,
                startWithFC(){
                    // if(this.Food.FoodCode.startsWith('FC-')) return true;
                    // else return false;
                    return true;
                }
            },
            name: {
                required,
            },
        }
    },

    async created() {
        try {
            // hiển thị loader
            this.changeLoader(true);
            this.ProductCode = this.$route.params.code;
            if(this.ProductCode){
                this.crudDetail = CRUD.Put;
                this.productItem = await ProductAPI.getObjByCode(this.ProductCode);
                console.log(this.productItem.product.categoryID)
                this.categoryModel = (await CategoryAPI.getObjById(this.productItem.product.categoryID)).data;

            }
            else {
                this.ProductCode = this.$route.params.copyCode;
                if(this.ProductCode){
                    this.crudDetail = CRUD.Copy;
                    this.productItem = await ProductAPI.getProductByCode(this.ProductCode);
                    // this.FoodMerge.Food.FoodCode = await ProductAPI.getNewCode();
                }
                this.crudDetail = CRUD.Post;
            }
            // for (let ind = 0; ind < this.FoodMerge.Comments.length; ind++) {
            //     this.star += parseInt(this.FoodMerge.Comments[ind].CommentStar);
                
            // }
            // this.star = this.FoodMerge.Comments.length > 0 ? Math.floor(this.star/this.FoodMerge.Comments.length) : 0;
            this.product = this.productItem.product;
            if(this.productItem.product.imageURL){
                this.$el.querySelector("#img-Product").setAttribute('src', "../../../../../data/" + this.product.imageURL);
            }
            // this.discountModel = {
            //     DiscountId: this.Food.DiscountId?this.Food.DiscountId:"",
            //     DiscountCode: this.Food.DiscountCode?this.Food.DiscountCode:"",
            //     DiscountMaxAmount: this.Food.DiscountMaxAmount?this.Food.DiscountMaxAmount:"",
            //     DiscountStartDate: this.Food.DiscountStartDate?this.Food.DiscountStartDate:"",
            //     DiscountEndDate: this.Food.DiscountEndDate?this.Food.DiscountEndDate:"",
            //     DiscountAmount: this.Food.DiscountAmount?this.Food.DiscountAmount:""
            // }
            if(this.product){
                this.product.cost = Base.formatNumber(this.product.cost);
            }
            this.items = this.productItem.items ? this.productItem.items : [];
            console.log("items", this.items)
            let categoryData = await CategoryAPI.getFilterPaging({keyword:null,status:null});
            this.categoryList = categoryData.data;
            // let sizeData = await SizeAPI.getFilterPaging({SizeFilter:null,SizeStatus:null});
            // this.sizes = sizeData.data;
            // let toppingData = await ToppingAPI.getFilterPaging({ToppingFilter:null,ToppingAmountMin:null,ToppingAmountMax:null,PageNumber:null,PageSize:null});
            // this.toppings = toppingData.data;
            // for (let index = 0; index < this.toppings.length; index++) {
            //     this.toppings[index].ToppingAmount = Base.formatNumber(this.toppings[index].ToppingAmount);
            // }
            // let discountData = await DiscountAPI.getFilterPaging({
            //     DiscountFilter: null,
            //     PageNumber: null,
            //     PageSize: null,
            //     DiscountFrom: new Date(),
            //     DiscountEnd: new Date(),
            //     DiscountAmount: null,
            //     DiscountOperation: null,
            //     DiscountMaxAmount: null,
            //     DiscountType: 0
            // })
            // this.discounts = discountData.data;
            // for (let index = 0; index < this.discounts.length; index++) {
            //     this.discounts[index].DiscountMaxAmount = Base.formatNumber(this.discounts[index].DiscountMaxAmount);
            //     this.discounts[index].DiscountStartDate = Base.formatDateToString(this.discounts[index].DiscountStartDate);
            //     this.discounts[index].DiscountEndDate = Base.formatDateToString(this.discounts[index].DiscountEndDate);
            // }
            this.changeLoader(false);
        } catch (error) {
            this.changeLoader(false);
            this.listMgs.push({
                showToast: true,
                titleMgs: "Tải dữ liệu thất bại! Vui lòng kiểm tra lại kết nối mạng hoặc liên hệ để được trợ giúp",
                iconToast: 'error'
            });
        }
    },
    
    methods: {
        /**
        * validate khi nhan click
        */
        validateForm() {
            this.idInvalid = "";
            this.$v.$touch();
            // nếu dữ liệu không hợp lệ
            if (this.$v.$invalid) {
                for(let key in this.product){
                    if(this.$v.product[key]){
                        if(this.$v.product[key].$error == true){
                            this.idInvalid = key;
                            this.showPopup(PopupState.Invalid);
                        }
                    }
                }
                return false;
            }
            // nếu dữ liệu hợp lệ 
            else {
                return true;
            }
        },

        /**
         * Mở popup
         */
         showPopup(crud) {
            this.popShow = true;
            this.action = crud;
        },

        /**
         * Đóng popup
         */
         closePop(action) {
            if(action == PopupState.Invalid){
                this.$refs[this.idInvalid].focus();
            }
            else if(action == PopupState.Duplicate){
                this.$refs.ProductCode.focus();
                this.$refs.ProductCode.classList.remove("is-invalid");
            }
            this.popShow = false;
        },

        /**
         * Đóng form bằng nút hủy hoặc x(khi có dữ liệu thay đổi)
         */
         btnXForm(){
            this.$router.go(-1);
        },
        /**
         * gọi đến hàm closeForm của FoodList
         */
        closeForm() {
            this.$router.go(-1);    
        },

        /**
         * Đổi trạng thái crud
         */
         changeCrud(action){
            this.crudDetail = action;
        },
        /**
         * delete toast message
         */
        deleteToast(index) {
            this.listMgs.splice(index, 1);
        },

        async crudObject(){
            try {
                console.log(this.crudDetail)
                let user = JSON.parse(localStorage.getItem("user"))
                // let user = {
                //     "userName": "vannnn"
                // }
                if(user){
                    // this.product.discount.discountId = this.discountModel.discountId;
                    // this.product.discount.maxAmount = Base.formatToNumber(this.discountModel.maxAmount);
                    // this.product.discount.amount = this.discountModel.DiscountAmount;
                    // this.product.discount.startDate = new Date(Base.formatDate(this.discountModel.startDate));
                    // this.product.discount.endDate = new Date(Base.formatDate(this.discountModel.endDate));
                    // this.product.discount.code = this.discountModel.code;
                    // this.product.discount.title = this.discountModel.title;
                    // this.product.userAction = user.userName;
                    if(this.validateForm() == true){
                        if(this.$refs.ImageURL.files && this.$refs.ImageURL.files[0]){
                            let formData = new FormData();
                            
                            formData.append('file', this.$refs.ImageURL.files[0])
                            this.product.imageURL = await ProductAPI.uploadFile(formData);
                            console.log("imageurl after upload file",this.product.imageURL);
                        }
                        else if(this.$el.querySelector("#img-Product").getAttribute('src')){
                            this.product.imageURL = this.$el.querySelector("#img-Product").getAttribute('src');
                        }
                        else {
                            this.product.imageURL = '';
                        }
                        this.product.imageURL = this.product.imageURL.replace("../../../../../data/", "")
                        this.product.categoryID = this.categoryModel.categoryId ? this.categoryModel.categoryId : null;
                        
                        this.productItem.product = this.product;
                        if(this.item.size.trim().length > 0 && this.item.quantity >0 && this.item.type.name.trim().length >0  ) {
                            // this.productItem.items.push(this.item);
                            this.items.push(this.item);
                        }
                        this.productItem.items = this.items;
                        this.productItem.product.cost = Base.formatToNumber(this.product.cost);
                        console.log("get body request product item : ", this.productItem)
                        
                        // thêm loader
                        this.changeLoader(true);
                        // nếu là thêm mới hoặc nhân bản
                        if((this.crudDetail == CRUD.Post)||(this.crudDetail == CRUD.Copy)){
                            // thực hiện thêm mới
                            //Base.formatObjToSave(this.Food);
                            console.log(this.productItem);
                            let res = await ProductAPI.postProduct(this.productItem);
                            console.log(res);
                            console.log(res.status)
                            // bỏ loader
                            this.changeLoader(false);
                            // nếu statusCode = 201
                            if(res.status == 200){
                                if(this.post_save == true){
                                    await this.postSave();
                                }
                                else this.$router.go(-1);
                            } 
                            // nếu không thêm mới thành công
                            else {
                                //Base.formatObj(this.Food);
                                this.message = res.data.data[0];
                                this.$refs.ProductCode.classList.add("is-invalid");
                                this.showPopup(PopupState.Duplicate);
                            }
                            // bỏ loader
                            this.changeLoader(false);
                            return;
                        }
                        // nếu là cập nhật
                        else if(this.crudDetail == CRUD.Put){
                            //  thực hiện cập nhật
                            //Base.formatObjToSave(this.Food);
                            console.log("get body request: ", this.productItem);
                            let res = await ProductAPI.putProduct(this.productItem);
                            // bỏ loader
                            this.changeLoader(false);
                            // nếu statusCode = 202
                            if(res.status == 200){
                                if(this.post_save == true){
                                    await this.postSave();
                                }
                                else {
                                    this.$router.go(-1);
                                }
                            } 
                            // nếu cập nhật thất bại
                            else{
                                console.log(res);
                                this.message = res.data.data[0];
                                this.$refs.ProductCode.classList.add("is-invalid");
                                this.showPopup(PopupState.Duplicate);
                            }
                            this.changeLoader(false);
                            return;
                        }
                    }
                }
                else {
                    this.$router.push({path: '/login', name: 'Login'});
                }
            } catch (error) {
                // bỏ loader
                this.changeLoader(false);
                if((this.crudDetail == CRUD.Post)||(this.crudDetail == CRUD.Copy)){
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: Base.stringFormat(ToastMgs.PostError,[this.product.name]),
                        iconToast: ToastMessageIcon.error
                    })
                }
                // nếu là cập nhật lỗi
                else if (this.crudDetail == CRUD.Put){
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: Base.stringFormat(ToastMgs.PutError,[this.product.name]),
                        iconToast: ToastMessageIcon.error
                    })
                }
                else {
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: ToastMgs.ExceptionError,
                        iconToast: ToastMessageIcon.error
                    })
                }
            }
        },

        getFile(imgSelector, event){
            Base.getFile(imgSelector, event);
        },
        deleteFile(imgSelector){
            this.product.imageURL = '';
            this.$el.querySelector(imgSelector).setAttribute('src', '');
        },
        changeLoader(value){
            this.loader = value;
        },

        formatNumber(){
            // this.product.cost = Base.formatToNumber(this.product.cost); 
        },
         removeDetail(index){
            if(typeof(index)=='number'){
                this.items.splice(index,1);
            }
            else {
                if(index == 'all'){
                    this.items = [];
                }
                else {
                    this.showInputDetail = false; 
                    this.item = {
                        size: '',
                        quantity:'',
                        type: {
                            typeID:'',
                            name:'',
                            describle:''
                        }
                    
                    }
                }
            }
            if(this.items.length == 0){
                this.showInputDetail = true; 
                    this.item = {
                        size: '',
                        quantity:'',
                        type: {
                            typeID:'',
                            name:'',
                            describle:''
                        }
                    }
            }
        },

        addDetail(){
            // console.log("ok");
            this.showInputDetail = false
            this.items = this.items.filter(e => e != this.item);
            console.log("item before add: ",this.item.size.trim().length);
            if(this.item.size.trim().length > 0 && this.item.quantity > 0 && this.item.type.name.trim().length >0  ) {
                this.items.push(this.item);

            // this.item = Object.assign({}, this.items[this.items.length-1]);
            this.item ={
                itemID: '',
                size:'',
                quantity:'',
                type: {
                    typeID:'',
                    name:'',
                    describle:''
                }
            }
            }
            console.log("this items",this.items);

        },

        /**
         * lấy ra phần tử được select
         */
         getSelect(model, item){
            this[model] = item;
            // switch (model){
            //     case 'topping_model':
            //         if(item.length)
            //             this.Food.ListTopping = item.map(topping => topping.ToppingId);
            //         break;
            //     case 'country_model':
            //         this.Food = item;
            //         break;
            //     default:
            //         break;
            // }
        },
        async postSave(){
            // reset lại validate
            this.$nextTick(() => { this.$v.$reset() })
            // đổi hành động về thêm mới
            this.changeCrud(CRUD.Post);
            // provider chuyển về giá trị ban đầu
            this.productItem = {};
            // lấy mã ncc mới
            // await this.getNewCode();
            // focus vào ô input object_code
            this.$refs.ProductCode.focus();
            // chuyển cất và thêm về false
            this.post_save = false;
        },

    }
 
    


    

}

</script>


<style>
    .food-detail{
        position: absolute;
        width: 100%;
        overflow: auto;
        height: calc(100% - 140px);
    }
    
    .food-detail .add-item{
        display: block;
        padding: 0 35px;
    }
    
    .food-detail .add-item .content-head{
        padding-left: 0;
    }
    
    table .w-30 {
        min-width: 30px;
        position: sticky !important;
        left: 0;
        border-style: none;
        background-color: #f4f5f7!important;
        z-index: 2 !important;
    }
    
    .food-detail .add-item .table-actions {
        margin-top: 10px;
        padding: 10px;
        height: 46px;
        position: sticky;
        bottom: 0;
        background-color: #f4f5f7!important;
    }
    
    .food-detail .add-item .table-action {
        background: #fff;
        border: 1px solid #8d9096;
        border-radius: 3px!important;
        padding: 2px 20px!important;
        margin-right: 10px;
        font-weight: 700;
    }
    
    table tr:hover > .w-30{
        background-color: #f4f5f7!important;
    }
    
    
    .stars-container {
        position: relative;
        display: inline-block;
        color: transparent;
        font-size: 20px;
    }
    
    .stars-container:before {
        position: absolute;
        top: 0;
        left: 0;
        content: '★★★★★';
        color: lightgray;
    }
    
    .stars-container:after {
        position: absolute;
        top: 0;
        left: 0;
        content: '★★★★★';
        color: gold;
        overflow: hidden;
    }
    
    .stars-0:after {
        width: 0%;
    }
    
    .stars-10:after {
        width: 10%;
    }
    
    .stars-20:after {
        width: 20%;
    }
    
    .stars-30:after {
        width: 30%;
    }
    
    .stars-40:after {
        width: 40%;
    }
    
    .stars-50:after {
        width: 50%;
    }
    
    .stars-60:after {
        width: 60%;
    }
    
    .stars-70:after {
        width: 70%;
    }
    
    .stars-80:after {
        width: 80%;
    }
    
    .stars-90:after {
        width: 90%;
    }
    
    .stars-100:after {
        width: 100;
    }
    
    .food-detail .form-footer,
    .order-detail .form-footer{
        position: fixed;
        padding: 18px 20px;
        z-index: 4;
        width: calc(100% - 57px);
        padding: 18px 35px;
        background-color: #f4f5f7;
    }
    .food-detail .form-footer.form-right,
    .order-detail .form-footer.form-right{
        width: calc(100% - 185px);
    }
    </style>