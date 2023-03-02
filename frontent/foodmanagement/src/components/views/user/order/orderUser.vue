<template>
    <div class="content-init">
        <div class="flex content-head">
            <div class="content-title">Danh sách đơn hàng</div>
            <div class="btn flex-center">
                <!-- <button class="admin-btn-primary add-btn" @click="openForm(null, 'post')">
                    <i class="fa fa-plus"></i>
                    Thêm mới
                </button> -->
            </div>
        </div>
        <div class="content-action flex">
            <div class="action-left flex">
                <div class="content-filter flex">
                    <input placeholder="Nhập tên đơn hàng, mã đơn hàng để tìm kiếm" 
                            type="text" 
                            ref="Filter" 
                            id="filter"
                            v-model="keyword"
                            v-tooltip.bottom-end="{offset: '5px', content:'Nhấp Enter để tìm kiếm', classes:'tooltip'}" @keyup.enter="getFilterPage()">
                    <div class="content-filter-search logo-icon other-icon pointer"></div>
                </div>
                <div class="filter-pop" style="position:relative;">
                    <button class="btn-filter-bar flex-center" type="button" id="btnFilterToggle" @click="showFilter=!showFilter;">
                        <i class="fa fa-filter"></i>
                        <span>Lọc</span>
                    </button>
                    <div class="pop-filter" :class="{'show-filter':showFilter, 'h-0':!showFilter}">
                        <div class="other-icon logo-icon btn-close pointer" id="btn-close" style="position: absolute;right: 5px;top: 5px;"
                                @click="showFilter=false;">
                        </div>
                        <div class="pop-init">
                            <!-- <div class="pop-info1 m-b-3">
                                <p class="m-b-3">Khách hàng</p>
                                <v-combobox class="user-data" 
                                            @getSelect="getSelect('user_model', $event)" 
                                            :combobox_valid="false" 
                                            :placeholder="''" 
                                            :item_text="['UserName','FullName', 'Phone', 'Address']" 
                                            :items="users"
                                            :vmodel="user_model"
                                            :multiple="true"
                                            :groupName="['UserName','Họ tên', 'Số điện thoại', 'Địa chỉ']"
                                            :display_item="'UserName'"
                                            :readonly="false">
                                </v-combobox>
                            </div> -->
                            <div class="pop-info1">
                                <div class="m-b-3">
                                    <p class="m-b-3">Trạng thái hóa đơn</p>
                                    <vue-combobox @getSelect="orderStatusFilter=$event" 
                                                :combobox_valid="false" 
                                                :placeholder="''" 
                                                :item_text="'text'" 
                                                :items="listOrderStatus" 
                                                :vmodel="orderStatusFilter"
                                                :multiple="false">
                                    </vue-combobox>
                                </div>
                            </div>
                            <div class="pop-info3">
                                <p>Thời gian</p>
                                <div class="flex">
                                    <div class="w-2/5 m-r-12">
                                        <vue-combobox @getSelect="dateFilter=$event" 
                                                    :combobox_valid="false" 
                                                    :placeholder="''" 
                                                    :item_text="'text'" 
                                                    :items="listTimeToFilter" 
                                                    :vmodel="dateFilter"
                                                    :multiple="false">
                                        </vue-combobox>
                                    </div>
                                    <div class="w-3/5 flex">
                                        <div class="w-1/2 m-r-12">
                                            <vue-datepicker :dataDate="dateFilter.value.startDate.toString()" 
                                                            @changeDate="changeDate($event, 'startDate')" 
                                                            :checkDateValid="true">
                                            </vue-datepicker>
                                        </div>
                                        <div class="w-1/2">
                                            <vue-datepicker :dataDate="dateFilter.value.endDate.toString()" 
                                                        @changeDate="changeDate($event, 'endDate')" 
                                                        :checkDateValid="true">
                                            </vue-datepicker>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-footer pop-footer flex">
                                <div class="admin-btn-normal btn-delete" @click="resetFilter()">Đặt lại</div>
                                <div class="admin-btn-normal admin-btn-primary" @click="filterMore()">Lọc</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="admin-btn-normal m-l-12 pointer" style="padding: 5px 10px;" v-show="showDeleteMulti" @click="showPopup('delete')">Xóa</div>
            </div>
            
            <div class="action-right flex">
                
                <div class="btn-filter-bar btn-action flex-center" v-tooltip.bottom="{content:'Lấy lại dữ liệu', classes:'tooltip', offset: '5px'}" @click="reload()">
                    <div class="refresh-icon other-icon logo-icon pointer"></div>
                </div>
                <div class="btn-filter-bar btn-action flex-center" v-tooltip.bottom="{content:'Thiết lập cột', classes:'tooltip', offset: '5px'}" @click="settingColumn()">
                    <div class="setting-icon other-icon logo-icon pointer"></div>
                </div>
            </div>
        </div>
        <common-table :oneAction="false" :layoutConfig="layoutConfig" :dataList="dataList" :objectName="objectName" :formShow="formShow" @closeForm="formShow=false;"
                      :actions="actions" :checkPaging="false" :displayName="displayName" @changeCrud="changeCrud" @openForm="openForm" @getFilterPage="getFilterPage"
                      @crudObject="crudOrder" ref="CommonTable" :noAction="true">
        </common-table>
        <order-user-detail @crudObject="crudObject" v-if="formShow && orderRead && orderRead.orderID" @btnXForm="btnXForm" :orderRead="orderRead"></order-user-detail>
    </div>
</template>

<script>

import OrderAPI from '../../../../api/component/order/OrderAPI'
// import UserAPI from '../../../../api/component/user/UserAPI'
import CommonTable from '../../../base/CommonTable.vue';
import VueDatepicker from '../../../base/VueDatepicker.vue';
import VueCombobox from '../../../base/VueCombobox.vue';
import { Actions, ListOrderStatus, ListTimeToFilter } from '../../../../base/vi/Resources.js';
import { CRUD, PopupState, ToastMessageIcon } from '../../../../base/Resources.js';
import { ToastMgs } from '../../../../base/vi/Resources.js';
import Base from '../../../../base/Base.js';
import OrderUserDetail from '../../user/order/orderUserDetail.vue';
export default {
    components: {
        CommonTable,
        VueDatepicker,
        VueCombobox,
        OrderUserDetail
    },
    name: 'OrderUser',
    props: {
        loader : Boolean
    },
    data(){
        return {
            layoutConfig: [],
            dataList:[],
            objectName:'Order',
            displayName: 'Đơn hàng',
            paramGet: {
                status: null,
                keyword: null,
                pageNumber: 1,
                pageSize: 50,
                createDateMin: null,
                createDateMax:null,
                userID: null,
            },
            //cac chuc nang:
            actions: Actions,
            keyword: "",
            entityDetail: {
                orderID:'',
                name:'',
                description:'',
                imageURL:''
            },
            formShow:false,
            crud: 'post',
            listMgs:[],
            // danh sach category
            categoryList: [{
                code:'',
                name: 'Tất cả danh mục'
            }],
            //loc theo thoi gian
            listTimeToFilter: ListTimeToFilter,
            dateFilter: ListTimeToFilter[0],
            showFilter: false,
            listOrderStatus:ListOrderStatus,
            orderStatusFilter: ListOrderStatus[0],
            user_model:[],
            users: [{
                userName:'Tất cả khách hàng',
                userCode: '',
                fullName: '',
                address: '',
                phone: '',
            }],
            orderRead:null,
            showDeleteMulti: false
        }
    },

    methods: {
        settingColumn(){
            this.$refs.CommonTable.setColumn = !this.$refs.CommonTable.setColumn;
        },
        crudOrder(action, obj){
            if(action == CRUD.Read){
                this.orderRead = obj;
                this.formShow = true;
            }
            else {
                console.log("delete");
            }
        },
        /**
         * Đặt lại điều kiện lọc
         */
         resetFilter(){
            this.dateFilter = this.listTimeToFilter[0];
            this.orderStatusFilter = this.listOrderStatus[0];
            this.moreFilter = false;
            this.showFilter=false;
        },
        /**
         * Lay ngày của dateFilter
         */
         changeDate(date, model){
            if(date){
                this.dateFilter.value[model] = date;
            }
            else {
                this.dateFilter.value[model] = "";
            }
        },

        hideFilter(){
            this.showFilter=false;
        },
        /**
         * lọc nâng cao
         */
         async filterMore(){
            try{
                this.showFilter=false;
                this.moreFilter=true;
                await this.getFilterPage();
                if(this.dataList.data.length > 0){
                    // show toast thành công
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: ToastMgs.GetSuccess,
                        iconToast: ToastMessageIcon.success
                    })
                    this.$refs.Filter.focus();
                }
            }
            catch(e){
                console.log(e);
                this.listMgs.push({
                    showToast: true,
                    titleMgs: ToastMgs.GetError,
                    iconToast: ToastMessageIcon.error
                })
            }
        },

        /**
         * Xóa 1 điều kiện lọc
         */
         async removeACondition(){
           try{
               this.checkCondition();
                await this.getFilterPage();
                if(this.dataList.data.length > 0){
                    // focus vào ô filter
                    this.$refs.Filter.focus();
                }
            }
            catch(ex){
                console.log(ex);
                this.listMgs.push({
                    showToast: true,
                    titleMgs: ToastMgs.GetError,
                    iconToast: ToastMessageIcon.error
                })
            }
        },
        /**
         * Xóa điều kiện lọc
         */
         async removeFilterMore(){
            try{
                this.moreFilter = false;
                this.checkFilterHead = false;
                this.orderStatusFilter = this.listOrderStatus[0];
                this.dateFilter = this.listTimeToFilter[0];
                await this.getFilterPage();
                if(this.dataList.data.length > 0){
                    // focus vào ô filter
                    this.$refs.Filter.focus();
                }
            }
            catch(ex){
                this.listMgs.push({
                    showToast: true,
                    titleMgs: ToastMgs.GetError,
                    iconToast: ToastMessageIcon.error
                })
            }
        },

        changeLoader(value){
            this.$emit('changeLoader', value);
        },

        async getFilterPage(){
            this.paramGet = {
                status: this.orderStatusFilter.value == -1 ? null : this.orderStatusFilter.value,
                keyword: this.keyword,
                createDateMin: this.dateFilter.value.startDate,
                createDateMax: this.dateFilter.value.endDate,
                userID:null,
                pageNumber: this.$refs.CommonTable.currentPage,
                pageSize: this.$refs.CommonTable.pageSize
            };
            this.dataList = await OrderAPI.getFilterPaging(this.paramGet);
            if(this.dataList && this.dataList.data && this.dataList.data.length > 0){
                console.loader
                this.dataList.data.forEach(el => {
                    el.status = ListOrderStatus.filter(ot => ot.value == el.status)[0].text;
                })
            }
        },
        async reload(){
            this.keyword = null;
            await this.getFilterPage();
        },

        /**
         * Đổi trạng thái crud
         */
         changeCrud(action){
            this.crud = action;
        },

        changeForm(action){
            this.formShow = action;
        },

        openForm(data, action) {
            this.formShow = true;
            this.crud = action;
            if ((this.crud == CRUD.Read)||(this.crud == CRUD.Put)||this.crud == CRUD.Copy) {
                this.orderRead = Object.assign({}, data);
                console.log(this.orderRead)
            }
            else {
                this.orderRead = {
                    order: {
                        phone:"",
                        address: "",
                        status: 0,
                    },
                    OrderDetails: [],
                };
            }
        },

        showPopup(action){
            this.$refs.CommonTable.showPopup(action);
        },

        btnXForm(){
            this.formShow = false;
        },

        async crudObject(obj){
            try{
                this.keyword = "";
                // cập nhật trạng thái đơn hàng
                // thêm loader
                this.changeLoader(true);
                //Base.formatObjToSave(this.$refs.OrderDetail.order);
                let res = await OrderAPI.putStatusOrder(obj);
                // bỏ loader
                this.changeLoader(false);
                // nếu statusCode = 202
                if(res.status == 200){
                    // thông báo thành công
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: ToastMgs.PutSuccess,
                        iconToast: ToastMessageIcon.success
                    })
                    this.$store.dispatch('handleChangeOrder', obj);
                    console.log(this.$store.state.notiOrderStatus);
                    // tải lại dữ liệu
                    await this.getFilterPage();
                    if(this.post_save == true){
                        await this.postSave();
                    }
                    else {
                        this.formShow = false;
                    }
                } 
                // nếu cập nhật thất bại
                else{
                    console.log(res);
                    this.message = res.data.Data[0];
                    this.$refs.object_code.classList.add("is-invalid");
                    this.showPopup(PopupState.Duplicate);
                }
                this.changeLoader(false);
                return;
            }
            catch(ex){
                // console.log(ex);
                this.changeLoader(false);
                if (this.crud == CRUD.Delete){
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: Base.stringFormat(ToastMgs.DeleteError, [this.message]),
                        iconToast: ToastMessageIcon.error
                    })
                }
                // nếu là thêm mới hoặc nhân bản lỗi
                else if((this.crud == CRUD.Post)||(this.crud == CRUD.Copy)){
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: Base.stringFormat(ToastMgs.PostError,[obj.orderID]),
                        iconToast: ToastMessageIcon.error
                    })
                }
                // nếu là cập nhật lỗi
                else if (this.crud == CRUD.Put){
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: Base.stringFormat(ToastMgs.PutError,[obj.orderID]),
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
                this.changeLoader(false);
            }
        },

        /**
         * Nếu là hành động cất và thêm
         */
         async postSave(){
            // reset lại validate
            this.$nextTick(() => { this.$v.$reset() })
            // đổi hành động về thêm mới
            this.changeCrud(CRUD.Post);
            // provider chuyển về giá trị ban đầu
            this.$refs.OrderDetail.order = {};
            // lấy mã ncc mới
            await this.getNewCode();
            // focus vào ô input object_code
            this.$refs.OrderDetail.$refs.OrderCode.focus();
            // chuyển cất và thêm về false
            this.post_save = false;
        },

        /**
         * lấy ra phần tử được select
         */
         getSelect(model, item){
            this[model] = item;
            // switch (model){
            //     case 'user_model':
            //         this.paramGet.userName = item.map(user => user.UserName);
            //         break;
            //     case 'country_model':
            //         this.order.Food = item;
            //         break;
            //     default:
            //         break;
            // }
        },

    },
    async created() {
        try {
            // hiển thị loader
            this.changeLoader(true);
            this.layoutConfig = await OrderAPI.getLayoutConfig();
            // let userData = await UserAPI.getFilterPaging({UserFilter:null,IsEmployee:false, PageNumber:null, PageSize:null});
            // let userData=null;
            this.user_model = this.users;
            // this.users = this.users.concat(userData.data);
            let user = localStorage.getItem('user');
            if(user.isEmployee === false) {
                this.paramGet = {
                    userID: user.userID
                }
            }
            this.dataList = await OrderAPI.getFilterPaging(this.paramGet);
            console.log("data order: ", this.dataList)
            if(this.dataList && this.dataList.data && this.dataList.data.length > 0){
                this.dataList.data.forEach(el => {
                    el.status = ListOrderStatus.filter(ot => ot.value == el.status)[0].text;
                })
            }
            this.changeLoader(false);
        } catch (error) {
            this.changeLoader(false);
            this.listMgs.push({
                showToast: true,
                titleMgs: "Tải dữ liệu thất bại! Vui lòng kiểm tra lại kết nối mạng hoặc liên hệ để được trợ giúp",
                iconToast: 'error'
            });
        }
    }
        
}
</script>

<style>
.pop-filter p {
    font-family: GoogleSans-Bold;
}

.pop-filter {
    position: absolute;
    z-index: 5;
    background: #fff;
    /* border: 1px solid #babec5; */
    border-radius: 3px;
    transition: .2s;
    width: 538px;
    font-size: 12px;
    top: 35px;
    /* right: calc(-100% + 23px); */
    opacity: 0;
    height: 0;
}

.pop-filter .pop-init {
    overflow: hidden;
    transition: .2s;
    height: 0;
}

.pop-footer {
    font-size: inherit;
    position: relative;
    margin-top: 20px;
}

.show-filter {
    opacity: 1;
    /* height: 300px; */
}

.show-filter .pop-init {
    background-color: #fff;
    border: 1px solid var(--gray-color);
    padding: 20px 23px;
    height: fit-content;
}

</style>