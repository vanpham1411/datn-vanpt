<template>
<!-- table -->
<div class="content-table">
    <div>
        <div class="grid scroll-body" id="scroll-thumb">
            <table id="table">
                <thead>
                    <tr>
                        <th class="check" v-if="!hideAction&&!oneAction">
                            <input type="checkbox" class="checkbox pointer flex-center" ref="ChooseAll" id="choose-all" @click="chooseAll()"/>
                        </th>
                        <th v-for="(column) in layoutValue.filter(property => property.Type !== 'image')" 
                            :key="column.DataBind+'TH'" :class="[{'d-none':column.State!=true}, column.Position]"
                            v-tooltip.bottom-end="{content: column.Tooltip ? column.Title:null, classes:'tooltip'}">
                                {{column.NameColumn}}
                                <b v-if="column.Type=='currency'">(VND)</b>
                                <b v-if="column.Type=='percent'">(%)</b>
                                <b v-if="column.Type!='currency'&&column.Type!='date'"></b>
                        </th>
                        <th v-for="(column) in layoutValue.filter(property => property.Type == 'image')" 
                            :key="column.DataBind" :class="[{'d-none':column.State!=true}, column.Position]"
                            v-tooltip.bottom-end="{content: column.Tooltip ? column.Title:null, classes:'tooltip'}">
                                {{column.NameColumn}}
                        </th>
                        <th class="action" v-if="!hideAction">CHỨC NĂNG</th>
                    </tr>
                </thead>

                <tbody id="table-body" v-if="dataList.totalRecord>0">
                    <tr v-for="(data) in dataList.data" :key="data[objectName+'Id']"  :data="data" @dblclick="openForm(data, 'put')" >
                        <td class="check" v-if="!hideAction&&!oneAction">
                            <input type="checkbox" @click="getListChoose($event, data)" class="checkbox pointer flex-center" ref="CheckAction" :id="data[objectName+'Id']"/>
                        </td>
                        <td v-for="(column) in layoutValue.filter(property => property.Type !== 'image')"
                            :key="column.DataBind+'TD'" :class="[{'d-none':column.State!=true}, column.Position]"
                            v-tooltip.bottom-end="{content: column.Tooltip ? data[column.DataBind]:null, classes:'tooltip'}">
                                <div class="content-column" :name="column.DataBind" :type="column.Type" v-if="column.Type=='currency'">{{formatNumber(data[column.DataBind])}}</div>
                                <div class="content-column" :name="column.DataBind" :type="column.Type" v-if="column.Type=='date'">{{formatDate(data[column.DataBind])}}</div>
                                <div class="content-column" :name="column.DataBind" :data="data[column.DataBind]" :type="column.Type" v-if="column.Type!='currency'&&column.Type!='date'">{{data[column.DataBind]}}</div>
                        </td>
                        <td v-for="(column) in layoutValue.filter(property => property.Type == 'image')"
                            :key="column.DataBind+'TD'" :class="[{'d-none':column.State!=true}, column.Position]"
                            v-tooltip.bottom-end="{content: column.Tooltip ? data[column.DataBind]:null, classes:'tooltip'}">
                                <img style="width:140px;" alt="" :src="data[column.DataBind]">
                        </td>
                        <td class="action" v-if="!hideAction">
                            <div style="display: flex; align-items: center;justify-content: space-between;" v-if="!oneAction">
                                <div @click="openForm(data, 'put')" class="update pointer">Sửa</div>
                                <vue-select :item_text="'text'" :items="actions" :vmodel="action_model" @getSelect="getActionSelect($event, data)"></vue-select>
                            </div>
                            <div class="action-one" style="display: flex; align-items: center;justify-content: space-around;" v-if="oneAction" @click="actionOne">
                                <div v-for="ac in actions" :key="ac.value" class="pointer" @click="getActionSelect(ac.value, data)">{{ac.text}}</div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div v-if="dataList.totalRecord==0" class="no-data">
                <div class="w-full">
                    <img src="../../assets/lib/img/common/bg_report_nodata.76e50bd8.svg" alt=""><br>
                    <span class="no-data-text" v-if="objectName!='Order'">Không có dữ liệu</span>
                    <span class="no-data-text" v-else>Chưa có đơn hàng</span>
                </div>
            </div>
            <!-- </div> -->
        </div>
        <!-- pagination -->
        <div class="paging-bar" v-if="dataList.totalRecord>0">
            <p class="list-show">Tổng số: <b>{{dataList.data.length}} / {{dataList.totalRecord}}</b> bản ghi</p>
            <div class="left-footer" v-if="checkPaging">
                <div class="page-size">
                    <!-- @click="menuTop" -->
                    <vue-select :top="true" :item_text="'text'" :items="pages" :vmodel="page_model" @getSelect="getPageSelect($event)"></vue-select>
                </div>
                <v-pagination v-model="currentPage" :length="dataList.totalPage" :page-count="totalPages" :classes="bootstrapPaginationClasses" :labels="customLabels" @input="onchange($event)"></v-pagination>
            </div>
        </div>
    </div>
    <vue-popup v-if="popShow" :detail="detail" :displayName="displayName" :popShow="popShow" :action="actionPop" @closeForm="closeForm" @closePop="closePop" :message="message" @crudObject="crudObject" :idInvalid="idInvalid"></vue-popup>
    <setting-column v-if="setColumn" :layoutValue="layoutValue" @saveColumn="saveColumn"></setting-column>
</div>
</template>

<style scoped>
.content-table {
    background-color: #fff;
    padding-left: 12px;
}
.action .action-one div:hover{
    text-decoration: underline;
}
</style>

<script>
import { Pages } from '../../base/vi/Resources.js';
import { CRUD, PopupState } from '../../base/Resources.js';
import Base from '../../base/Base.js';
import vPagination from 'vue-plain-pagination';
import VueSelect from '@/components/base/VueSelect';
import VuePopup from '@/components/base/VuePopup';
import SettingColumn from '@/components/base/SettingColumn';
export default {

    name:'CommonTable',
    components:{
        vPagination,
        VueSelect,
        VuePopup,
        SettingColumn
    },
    props:{
        layoutConfig: [Object, Array],
        dataList: [Object, Array],
        objectName: String,
        actions:[Array, String],
        checkPaging: Boolean,
        displayName: String,
        crud: String,
        formShow: Boolean,
        detail:String,
        oneAction: Boolean,
        noAction: Boolean,
    },

    data(){
        return {
            hideAction: false,
            layoutValue: [],
            // trang hiện tại
            currentPage: 1,
            // tổng số trang
            totalPages: 1,
            // số bản ghi hiển thị
            totalRecord: 0,
            // mảng tất cả số bản ghi trong db
            allRecord:[],
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
            page_model: Pages[3],
            // action được chọn
            action_model:null,
            message:'',
            // tổng số bản ghi trên bảng
            pageSize: 50,
            popShow: false,
            // mảng bản ghi tích chọn
            listChoose: [],
            actionPop:"",
            idInvalid:"",
            setColumn: false,
        }
    },
    watch:{
        layoutConfig(){
            if(this.layoutConfig.LayoutValue){
                this.layoutValue = JSON.parse(this.layoutConfig.LayoutValue);
            }
        },
    },
    created(){
        // console.log(this.layoutConfig);
        this.totalRecord = this.dataList.totalRecord;
        if(this.noAction == true){
            this.hideAction = true;
        }
    },
    methods:{
        formatNumber(value){
            if(value)
                return Base.formatNumber(value);
            else 
                return "";
        },
        formatDate(value){
            if(value && Date.parse(value)){
                return Base.formatDateToString(value);
            }
            else {
                return "";
            }
        },
        /**
         * Lấy page size người dùng chọn
         */
        async getPageSelect(item){
            this.pageSize = item.value;
            this.currentPage = 1;
            await this.$emit("getFilterPage");
            // this.totalPages = this.dataList.totalPage;
        },

        /**
         * Chọn tất cả data trong một page
         */
        chooseAll() {
            // nếu đang chọn tất cả
            if(this.$refs.ChooseAll.checked == true){
                // tất cả input type = checkbox trong bảng gán bằng checked
                this.$refs.CheckAction.forEach(item =>{
                    item.checked = true;
                })
                this.listChoose = this.dataList.data;
                this.$emit("changeShowDeleteMulti", true);
            }
            // ngược lại
            else {
                this.$refs.CheckAction.forEach(item =>{
                    item.checked = false;
                })
                this.listChoose=[];
                this.$emit("changeShowDeleteMulti", false);
            }
        },

        /**
         * Mở form
         */
        openForm(entityDetail, action) {
            // console.log("openform ", entityDetail, action)
            this.$emit('openForm', entityDetail, action)
        },

        /**
         * Lấy hành động người dùng click là xóa hay nhân bản
         */
        getActionSelect(item, obj){

            console.log("click from common table ", item, obj, this.objectName, item == CRUD.Delete)
            // nếu là nhân bản
            if(item == CRUD.Copy || item.value == CRUD.Copy){
                this.listChoose=[];
                this.openForm(obj,CRUD.Copy);
            } 
            // nếu là xóa
            else if(item == CRUD.Delete || item.value == CRUD.Delete){
                console.log("Object commontable: ", obj);
                if(obj['order'+'ID']){
                    this.listChoose = [obj];
                    this.showPopup(CRUD.Delete);
                } else
                if(obj[this.objectName+'Id']){
                    this.listChoose = [obj];
                    this.showPopup(CRUD.Delete);
                }
            }
            else if(item == CRUD.Read || item.value == CRUD.Read){
                if(this.objectName == "Order" || this.objectName == "AccessLog"){
                    this.$emit("crudObject", CRUD.Read, obj);
                }
            }
            else if(item == CRUD.Rate || item.value == CRUD.Rate){
                if(this.objectName == "Order"){
                    this.$emit("crudObject", CRUD.Rate, obj);
                }
            }
        },

        /**
         * Đóng form bằng nút hủy hoặc x(khi có dữ liệu thay đổi)
         */
        btnXForm(obj, oldObj, crud, objectCode){
            // nếu có dữ liệu khác thì mở popup
            if(Base.checkChangeData(obj, oldObj, crud, objectCode) == false){
                this.showPopup(PopupState.Cancel);
                // console.log(this.popShow);
                return;
            }
            // nếu không thì đóng form
            else {
                this.$emit('closeForm'); 
                return;
            }
        },

        /**
         * Mở popup
         */
        showPopup(crud) {
            if(this.listChoose.length > 0) {
                this.popShow = true;
            this.actionPop = crud;
            //emit: goi den ham cha
            this.$emit('changeCrud', crud);
            if(crud == CRUD.Delete){
            
                this.message = '';
                this.listChoose.forEach(item => {
                    if(item['name']){
                        this.message += item['name'] + ", ";
                    }
                    else if(item['code']){
                        this.message += item['code'] + ", ";
                    }
                    else if(item['orderID']){
                        this.message += item['orderID'] + ", ";
                        this.$emit("crudObject", CRUD.Delete, item);

                    }
                })
                this.message = this.message.slice(0, this.message.length-2);
                console.log(this.message);
            }
            }
            
        },

        /**
         * Gửi yêu cầu thêm/sửa/xóa employee/customer
         */
        async crudObject() {
            console.log("crud : ", this.idInvalid)
            await this.$emit('crudObject');
        },

        actionOne(){
            this.$emit('actionOne');
        },

        // lấy mảng item được chọn
        getListChoose(e, data){
            // console.log(this.objectName)
            console.log("get list choose: ",data)
            // console.log(e)/
            if(e.target.checked==true){
                // if(this.objectName === "Category") {
                //     this.listChoose = this.listChoose.filter(obj => obj[this.objectName.toLowerCase()+"Id"] != e.target.getAttribute('d'));

                // } else
                // this.listChoose = this.listChoose.filter(obj => {
                //     console.log("obj: ", obj, obj[this.objectName.toLowerCase()+"ID"])

                //     console.log(obj[this.objectName.toLowerCase()+"ID"] != e.target.getAttribute('id'))
                //     return obj[this.objectName.toLowerCase()+"ID"] != e.target.getAttribute('id')
                // });
                this.listChoose.push(data);
                if(this.listChoose.length == this.dataList.data.length){
                    this.$refs.ChooseAll.checked = true;
                }
                else {
                    this.$refs.ChooseAll.checked = false;
                }
                // this.listChoose.push(data);
            }
            else{
                this.$refs.ChooseAll.checked = false;
                if(this.objectName === "Category") {
                    this.listChoose = this.listChoose.filter(obj => {
                        console.log(obj['categoryId'] , data)
                        return obj['category'+'Id'] != data['category'+'Id']
                    });
                } else
                this.listChoose = this.listChoose.filter(obj => obj[this.objectName.toLowerCase()+"ID"] != data[this.objectName.toLowerCase()+"ID"]);
                // this.listChoose.delete(data);
            }
            if(this.listChoose.length > 0){
                this.$emit("changeShowDeleteMulti", true);
            }
            else {
                this.$emit("changeShowDeleteMulti", false);
            }
        },
        closeForm(){
            this.$emit("closeForm");
            this.action_model = {text:'', value:null};
        },
        /**
         * Đóng Popup
         */
        closePop(){
            this.popShow = false;
            this.action_model = {text:'', value:null};
        },

        saveColumn(listStateTrue){
            //this.$cookies.set(this.objectName+"LayoutConfig", listStateTrue, "24");
            this.layoutValue.forEach( col => {
                if(listStateTrue.includes(col.DataBind)){
                    col.State = true;
                }
                else {
                    col.State = false;
                }
            })
            this.setColumn = false;
        },
        async onchange(value) {
            // console.log(value);
            this.currentPage = value;
            await this.$emit("getFilterPage");
        }
    },
}
</script>

<style>

</style>