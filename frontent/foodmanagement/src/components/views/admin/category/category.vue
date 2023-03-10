<template>
    <div v-if="this.user.role === 1" class="content-init">
    <div class="flex content-head">
        <div class="content-title">Danh sách danh mục</div>
        <div class="btn flex-center">
            <button class="admin-btn-primary add-btn" @click="openForm(null, 'post')">
                <i class="fa fa-plus"></i>
                Thêm mới
            </button>
        </div>
    </div>
    <div class="content-action flex">
        <div class="action-left flex">
            <div class="content-filter flex">
                <input placeholder="Nhập tên danh mục, mã danh mục để tìm kiếm" 
                        type="text" 
                        ref="Filter" 
                        id="filter"
                        v-model="keyword"
                        v-tooltip.bottom-end="{offset: '5px', content:'Nhấp Enter để tìm kiếm', classes:'tooltip'}"
                        @keyup.enter="getFilterPage()">
                <div class="content-filter-search logo-icon other-icon pointer"></div>
            </div>
            <div class="admin-btn-normal m-l-12 pointer" style="padding: 5px 10px;" @click="showPopup('delete');">Xóa</div>
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
    <common-table :oneAction="false" :objectName="objectName"  :layoutConfig="layoutConfig" :dataList="dataList" @closeForm="formShow=false;"
                  :actions="actions" :checkPaging="false" :displayName="displayName" @changeCrud="changeCrud" @openForm="openForm" @crudObject="crudObject"
                  ref="CommonTable">
    </common-table>
    <category-detail v-if="formShow" 
                    @closeForm="formShow=false;" 
                    @crudObject="crudObject" 
                    :crud="crud"
                    @showPopup="showPopup" 
                    @btnXForm="btnXForm"
                    :entityDetail="entityDetail" 
                    @changeCrud="changeCrud"
                    ref="CategoryDetail"></category-detail>
</div>
</template>

<script>
import CommonTable from '../../../base/CommonTable.vue';
import { CRUD, PopupState, ToastMessageIcon  } from '../../../../base/Resources.js'
import { ToastMgs } from '../../../../base/vi/Resources.js';
import Base from '../../../../base/Base.js';
import CategoryAPI from '../../../../api/component/food/CategoryAPI.js'
import CategoryDetail from './CategoryDetail.vue';
export default {
    components:{CommonTable, CategoryDetail},
    name: "CategoryAdmin",
    data(){
        return {
            listMgs:[],
            user:{},
            showDeleteMulti: false,

            objectName: 'Category',
            displayName:'Danh mục',
            actions: [CRUD.Put, CRUD.Delete],
            layoutConfig: [],
            keyword: '',
            dataList: {
                totalRecord: 0,

            },
            entityDetail: {
                code: '', 
                name:'', 
                descriptions: '', 
                imageURL: ''
            },
            formShow: false,
            crud:'post',
            paramGet:{
                keyword: this.keyword,
                status: null
            }
        }
    },
    props: {

    },
    methods: {
        settingColumn(){
            this.$refs.CommonTable.setColumn = !this.$refs.CommonTable.setColumn;
        },
        changeLoader(value){
            this.$emit('changeLoader', value);
        },
        async getFilterPage(){
            this.paramGet = {
                status: null,
                keyword: this.keyword,
            };
            this.showDeleteMulti = false;
            this.dataList = await CategoryAPI.getFilterPaging(this.paramGet)
        },
        async reload(){
            this.keyword = null;
            await this.getFilterPage();
        },

        /**
         * Đổi trạng thái crud
         * 
         */
         changeCrud(action){
            this.crud = action;
        },

        /**
         * Đổi trạng thái đóng mở form
         * 
         */
        changeForm(action){
            this.formShow = action;
        },


        /**
         * mở form
         * @param {*} data 
         * @param {*} action 
         */
        openForm(data, action) {
            this.formShow = true;
            this.crud = action;
            if ((this.crud == CRUD.Read)||(this.crud == CRUD.Put)||this.crud == CRUD.Copy) {
                this.entityDetail = Object.assign({}, data);
            }
            else {
                this.entityDetail = {
                    categoryId:0,
                    code: '', 
                    name:'', 
                    description: '', 
                    imageURL: ''
                };
            }
        },
        

        showPopup(action){
            console.log("put param for commontable: ", action)
            this.$refs.CommonTable.showPopup(action);
        },

        btnXForm(obj, oldObj, crud, objectCode){
            this.$refs.CommonTable.btnXForm(obj, oldObj, crud, objectCode);
        },

        async crudObject(obj){
            console.log(obj)
            try{
                 this.user = JSON.parse(localStorage.getItem("user"));
                
                console.log("name: "+ this.user.name)
                if(this.user){
                    this.keyword = "";
                    //  nếu là xóa
                    if (this.crud == CRUD.Delete) {
                        // trở về trang 1
                        this.currentPage = 1;
                        this.changeLoader(true);
                        // thành công
                        console.log(this.$refs.CommonTable.listChoose);
                        let listDelete = [];
                        this.$refs.CommonTable.listChoose.forEach(item => {
                            console.log("get list choose: ",item )
                            listDelete = listDelete.filter(id => id != item.categoryId);
                            listDelete.push(item.categoryId);
                        })
                        console.log(listDelete);
                        var {data}  = await CategoryAPI.delete(listDelete);
                        this.changeLoader(false);
                        console.log(data)
                        if(!data.data){
                            this.message = data.data;
                            this.$refs.CommonTable.showPopup(PopupState.Duplicate);
                        }
                        else {
                            this.listMgs.push({
                                showToast: true,
                                titleMgs: Base.stringFormat(ToastMgs.DeleteSuccess, [this.message]),
                                iconToast: ToastMessageIcon.success
                            })
                            this.$refs.CommonTable.listChoose = [];
                            // tải lại dữ liệu
                            await this.getFilterPage();
                        }
                    }
                    else {
                        console.log("CRUD")
                        console.log( this.$refs.CategoryDetail.category  )
                        this.$refs.CategoryDetail.category.UserAction = this.user.name
                        if(this.$refs.CategoryDetail.validateForm() == true){
                            // nếu là thêm mới hoặc nhân bản
                            if((this.crud == CRUD.Post)||(this.crud == CRUD.Copy)){
                                // thực hiện thêm mới
                                // thêm loader
                                this.changeLoader(true);
                                //Base.formatObjToSave(this.$refs.CategoryDetail.category);
                                console.log(this.$refs.CategoryDetail.category);
                                let res = await CategoryAPI.postObj(this.$refs.CategoryDetail.category);
                                console.log(res);
                                // bỏ loader
                                this.changeLoader(false);
                                // nếu statusCode = 201
                                if(res.status == 200){
                                    // thông báo thành công
                                    this.listMgs.push({
                                        showToast: true,
                                        titleMgs: ToastMgs.PostSuccess,
                                        iconToast: ToastMessageIcon.success
                                    })
                                    // tải lại dữ liệu
                                    // trở về trang 1
                                    this.currentPage = 1;
                                    await this.getFilterPage();
                                    if(this.post_save == true){
                                        await this.postSave();
                                    }
                                    else this.formShow = false;
                                } 
                                // nếu không thêm mới thành công
                                else {
                                    //Base.formatObj(this.$refs.CategoryDetail.category);
                                    this.message = res.data.Data[0];
                                    this.$refs.object_code.classList.add("is-invalid");
                                    this.showPopup(PopupState.Duplicate);
                                }
                                // bỏ loader
                                this.changeLoader(false);
                                return;
                            }
                            // nếu là cập nhật
                            else if(this.crud == CRUD.Put){
                                
                                //  thực hiện cập nhật
                                // nếu dữ liệu bị thay đổi
                                if(Base.checkChangeData(this.$refs.CategoryDetail.category, this.entityDetail, this.crud, 'code') == false){
                                    // thêm loader
                                    this.changeLoader(true);
                                    //Base.formatObjToSave(this.$refs.CategoryDetail.category);
                                    let res = await CategoryAPI.putObj(this.$refs.CategoryDetail.category);
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
                                // nếu dữ liệu không bị thay đổi 
                                else {
                                    // nếu ấn cất và thêm
                                    if(this.post_save == true){
                                        await this.postSave();
                                    }
                                    // nếu không
                                    else{
                                        this.formShow = false;
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    this.$router.push({path: '/login', name: 'Login'});
                }
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
                        titleMgs: Base.stringFormat(ToastMgs.PostError,[obj.name]),
                        iconToast: ToastMessageIcon.error
                    })
                }
                // nếu là cập nhật lỗi
                else if (this.crud == CRUD.Put){
                    this.listMgs.push({
                        showToast: true,
                        titleMgs: Base.stringFormat(ToastMgs.PutError,[obj.name]),
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


    },
    async created() {
        this.user = JSON.parse(localStorage.getItem("user"));

        try {

            // hiển thị loader
            this.changeLoader(true);
            this.layoutConfig = await CategoryAPI.getLayoutConfig();

            this.dataList = await CategoryAPI.getFilterPaging(this.paramGet);
            console.log(this.dataList)
            this.changeLoader(false);
        } catch (error) {
            this.changeLoader(false);
            console.log(error);
        }
    }
}
</script>


<style>

.content-action{
    padding: 15px 12px;
    background-color: #fff;
}
.content .content-action .action-left{
    flex: 1;
}
.content-filter{
    position: relative;
    width: 340px;
}
.content-filter input {
    padding-right: 30px !important;
}
.content-filter .content-filter-search {
    position: absolute;
    right: 0;
    width: 30px;
    font-size: 6px;
    cursor: pointer;
    height: 30px;
    background-position: -987px -354px;
}

.content-filter .content-filter-search:hover {
    background-position: -311px -141px;
}

.content .content-action .btn-filter-bar{
    height: 100%;
    font-size: 14px;
    justify-content: space-between;
    width: 60px;
    padding: 0 8px;
    border-radius: 3px;
    border: 1px solid var(--gray-color);
    margin-left: 10px;
    transition: .2s;
}

.content .content-action .btn-action{
    width: fit-content;
}

.content .content-action .btn-filter-bar:hover{
    font-weight: bold;
    background-color: #f0f2f5;
}
</style>