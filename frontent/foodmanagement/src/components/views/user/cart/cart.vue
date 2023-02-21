<template>
    <div class="cart m-t-20">
        <div class="flex" style="align-items: baseline;">
            <div class="w-1/2 order-info name">
                Người nhận:
                <textarea class="w-full" rows="1" ref="OrderAddress" v-model="userName" placeholder="">
                </textarea>  
            </div>
            <div class="w-1/2 order-info address">
                Địa chỉ giao hàng:
                <textarea class="w-full" rows="2" ref="OrderAddress" v-model="address" placeholder="">
                </textarea>  
            </div>
            <div class="w-1/2 order-info phone">
                Số điện thoại:
                <input  ref="OrderPhone" v-model="phone"
                        :class="{ 'is-invalid': !testPhone }"
                        type="text" maxlength="20" class="w-full form-control" @change="phoneChange()">
                <div v-if="!testPhone" class="invalid-feedback">
                    <p>Số điện thoại không hợp lệ!</p>
                </div>
            </div>
        </div>
        <div class="food-item w-full" v-for="(data, index) in listCart" :key="data.cartItem.id">
            <div class="flex food-init" style="align-items:center;">
                <router-link tag="div" :to="'/user/menu/'+data.productItem.product.productID" style="width:150px;" class="m-r-12 pointer food-img">
                    <div class="img-food"><img :src= "'../../../../../data/' + data.productItem.product.imageURL" alt=""></div>
                    <span class="sale" v-if="data.HasDiscount">Sale!</span>
                </router-link>
                <div class="w-1/4 m-l-12">
                    <router-link tag="div" :to="'/user/menu/'+data.productItem.product.productID" class="food-name pointer"><b>{{data.productItem.product.name}}</b></router-link>
                    <div class="food-size flex-center">
                        <div class="w-1/2 flex-center">
                            <div class="w-1/3 m-r-12">Type </div> 
                            <vue-combobox @getSelect="changeItem($event,data)" v-if="!data.productItem.product.hasDiscount"
                                        :combobox_valid="false" 
                                        :placeholder="''" 
                                        class="w-1/3 m-r-12"
                                        :item_text="['typeName', 'size']" 
                                        :items="data.productItem.items"
                                        :display_item="'typeName'" 
                                        :groupName="['Loại', 'Kích cỡ']"
                                        :vmodel="data.typeModel"
                                        :multiple="false">
                            </vue-combobox>
                            <!-- <vue-combobox @getSelect="changeSize($event, data, index)" v-else
                                        :combobox_valid="false" 
                                        :placeholder="''" 
                                        class="w-2/3 m-r-12 food-size"
                                        :item_text="['SizeName', 'NumberAmount', 'RealAmountNumber']" 
                                        :items="data.ListFoodDetailSame"
                                        :display_item="'SizeName'" 
                                        :groupName="['Kích thước', 'Đơn giá', 'Giá sau giảm']"
                                        :vmodel="data.sizeModel"
                                        :multiple="false">
                            </vue-combobox> -->
                        </div>
                        <div class="w-1/2">
                            <div class="unit-price">
                                Giá: <b :id="'numberAmount'+index" :class="{'line-through': data.hasDiscount}">{{data.productItem.product.cost}}</b> 
                                <span v-if="!data.hasDiscount">VND</span>
                            </div>
                            <!-- <div v-if="data.HasDiscount">
                                <b>{{formatNumber(data.RealAmount)}} VND</b>
                            </div> -->
                        </div>
                    </div>
                </div>
                <div class="w-1/6 m-l-12 flex-center food-to-cart">
                    <div class="food-quantity flex-center">
                        <button class="m-l-12" @click="updateQuantity('subtract', index, data)">-</button>
                        <div class="w-1/3 center"><input class="center w-full" type="number" v-model="data.cartItem.quantity" @change="caculateAmount(data, 'updateQuantity', index)" id=quantity></div>
                        <button class="m-r-12" @click="updateQuantity('add', index, data)">+</button>
                    </div>
                </div>
                <div class="m-l-12 w-1/3">
                    <!-- <div v-if="data.ListOrgTopping && data.ListOrgTopping.length > 0" class="toppings flex ">
                        <div class="">
                            <div class="topping-quantity">
                                <input type="checkbox" class="checkbox pointer flex-center" :ref="'ChooseAll'+index" :id="'choose-all' + index" @change="caculateTopping(data, 'all', index, $event)"/>
                            </div>
                            <div class="topping-name">Topping</div> 
                            <div class="topping-price">Giá (VND)</div> 
                        </div>
                        <div class="topping-item" v-for="(topping) in data.ListOrgTopping" :key="topping.ToppingCode">
                            <div class="topping-quantity">
                                <input type="checkbox" class="checkbox pointer flex-center" :ref="'CheckChoose'+index"
                                        @change="caculateTopping(data, topping, index, $event)"
                                        :value="topping.ToppingId" name="topping" v-model="data.checkedToppings"/>
                            </div>
                            <div class="topping-name">{{topping.ToppingName}}</div> 
                            <div class="topping-price">{{topping.ToppingAmount}}</div> 
                        </div>
                        <div class="topping-last">
                            <div class="topping-name flex-center" style="padding: 0;">Tổng giá</div> 
                            <div class="topping-price flex-center" style="height: 66.67%" :id="'amountToppingModel'+index">{{data.amountToppingModel}}</div> 
                        </div>
                    </div> -->
                </div>
                <div class="w-1/8 m-l-12">
                    Thành tiền: <b style="color:var(--primary-color);" :id="'amountFood'+index">{{data.realAmount}}</b>
                </div>
                <div class="w-1/8 m-l-12 cart-action center">
                    <div><button @click="crud='put';crudObject(data, index)" :id="'update'+index">Cập nhật</button></div>
                    <div><button @click="crud='delete';crudObject(data.cartItem)">Xóa</button></div>
                </div>
            </div>
        </div>
        <div class="flex-center m-t-20 m-b-20 to-order">
            <div class="w-1/2" style="font-size: 20px;">Tổng tiền: <b style="color:var(--primary-color);">{{allAmount}}</b></div>
            <div class="home">
                <button @click="order('post')">Đặt hàng</button>
            </div>
        </div>
        <div>
            <div v-for="(mgs, index) in listMgs" :key="index">
                <toast-message :titleMgs="mgs.titleMgs" :iconToast="mgs.iconToast" v-if="mgs.showToast" @deleteToast="deleteToast(index)"></toast-message>
            </div>
            <vue-popup v-if="popShow" :popShow="popShow" :message="message" @closePop="closePop" :action="actionPopup"></vue-popup>
        </div>
    </div>
    </template>

<script>
import OrderAPI from '../../../../api/component/order/OrderAPI';
import CartDetailAPI from '../../../../api/component/user/CartDetailAPI';
import Base from '../../../../base/Base';
import { CRUD, PopupState } from '../../../../base/Resources';
import ToastMessage from '../../../base/ToastMessage.vue';
import VueCombobox from '../../../base/VueCombobox.vue';
import VuePopup from '../../../base/VuePopup.vue';
export default {
    name: 'Cart',
    components: {
        VueCombobox,
        VuePopup,
        ToastMessage,
    },
    data() {
        return {
            imagepath : '../../../../../data/',
            listCart:[],
            crud: '',
            user: null,
            allAmount:0,
            userName:"",
            address:"",
            popShow:false,
            listMgs:[],
            message:"",
            actionPopup:"",
            phone: "",
            testPhone:true,
        }
    },
    async created() {
        this.user = JSON.parse(localStorage.getItem('user'));
        if(this.user) {
            this.userName = this.user.name;
            this.address = this.user.address;
            this.phone = this.user.phone;
            // this.listTopping = await ToppingAPI.getAll();
            await this.getListCart();
            this.listCart.forEach((ele, index) => {
                this.allAmount += ele.productItem.product.cost * ele.cartItem.quantity
                this.caculateAmount(ele, "load", index);
            });
        }
        else {
            this.$router.push({ path: '/login' });            
        }
    },
    methods: {
        formatNumber(val) {
            return Base.formatNumber(val);
        },
        deleteToast(index) {
            this.listMgs.splice(index, 1);
        },
        phoneChange(){
            let regexPhone = /^[+]?[(]?[0-9]{2,3}[)]?[-\s.]?[2-9]{1}\d{2}[-\s.]?[0-9]{4,6}$/;
            this.phone = this.phone.trim();
            if(regexPhone.test(this.phone.trim())){
                this.testPhone = true;
                return true;
            }else {
                this.testPhone = false;
                return false;
            }
        },
        changeItem(item, data) {
            data.typeModel = item;

        },
        async getListCart() {
            let response = await CartDetailAPI.getFilterPaging(
                {
                    userID: this.user.userID
                }
            );
            this.listCart = response.data;
            console.log("list cart: ", this.listCart)
            this.listCart.forEach(ele => {
                ele.productItem.items.forEach(item => {
                    item.typeName = item.type.name;
                    if(item.itemID === ele.cartItem.itemID) {
                        ele.typeModel = item;
                    }
                })
                
                ele.realAmount= ele.cartItem.quantity * ele.productItem.product.cost;
                
            })
            console.log("local storage: ", localStorage)
        },
        updateQuantity(operation, index, data){
            var cnt = this.listCart[index].cartItem.quantity;
            if(operation == 'add'){
                this.listCart[index].cartItem.quantity = parseInt(this.listCart[index].cartItem.quantity) + 1;
                
            }
            else{
                this.listCart[index].cartItem.quantity = parseInt(this.listCart[index].cartItem.quantity) > 1 ? parseInt(this.listCart[index].cartItem.quantity)-1 : 1;
            }
            this.caculateAmount(data, 'updateQuantity', index);
            if(cnt === this.listCart[index].cartItem.quantity -1) {
                this.allAmount +=   this.listCart[index].productItem.product.cost;
            } else if(cnt === this.listCart[index].cartItem.quantity + 1) {
                this.allAmount -=   this.listCart[index].productItem.product.cost;
            }
            
        },
        async crudObject(obj, index){
            try {
                switch (this.crud) {
                    case CRUD.Delete:
                        var { data } = await CartDetailAPI.deleteMultiObj([obj]);
                        console.log(data);
                        await this.getListCart();
                        this.listCart.forEach((ele, index) => {
                            this.caculateAmount(ele, "load", index);
                        });
                        break;
                    case CRUD.Put:
                        var res = await CartDetailAPI.putObj(obj);
                        if(res.status == 200){
                            //await this.getListCart();
                            document.getElementById("update"+index).disabled = true;
                        }
                        break;
                    default:
                        break;
                }
            } catch (error) {
                console.log(error)
            }
        },
        async order(action){
            try {
                switch (action) {
                    case CRUD.Post:
                    if(!this.userName){
                            this.popShow = true;
                            this.message = "Bạn vui lòng điền tên người nhận trước!";
                            this.actionPopup = PopupState.Duplicate;
                        }
                        else if(!this.address){
                            this.popShow = true;
                            this.message = "Bạn vui lòng điền địa chỉ nhận hàng trước!";
                            this.actionPopup = PopupState.Duplicate;
                        }
                        else if(!this.phone){
                            this.popShow = true;
                            this.message = "Bạn vui lòng điền số điện thoại nhận hàng trước!";
                            this.actionPopup = PopupState.Duplicate;
                        }
                        else if(this.listCart.length == 0){
                            this.popShow = true;
                            this.message = "Bạn vui lòng chọn sản phẩm để đặt hàng!";
                            this.actionPopup = PopupState.Duplicate;
                        }
                        else {
                            let orderDetails = [];
                            this.listCart.forEach(ele =>{
                                orderDetails.push({
                                    itemID: ele.cartItem.itemID,
                                    // ListTopping: ele.ListTopping,
                                    // DiscountId: ele.DiscountId,
                                    quantity: ele.cartItem.quantity,
                                    amount: ele.realAmount,
                                    cost: ele.productItem.product.cost,
                                    cartItemID: ele.cartItem.id,
                                })
                            })
                            let obj = {
                                order: {
                                    userID: this.user.userID,
                                    userName: this.userName, 
                                    cost: Base.formatToNumber(this.allAmount),
                                    phone: this.phone,
                                    address: this.address,
                                    status: 0, // 0 - Processing,
                                    name: this.user.userName,
                                    DiscountId: null,
                                },
                                orderDetails: orderDetails
                            }
                            console.log(obj);
                            var res = await OrderAPI.postOrder(obj);
                            console.log(res.status);
                            if(res.status == 200){
                                this.$router.push({path: '/user/account', name: 'Account'});
                            }
                        }
                        break;
                
                    default:
                        break;
                }
            } catch (error) {
                console.log(error)
            }
        },
        closePop(){
            this.popShow=false;
            if(!this.address){
                this.$refs.OrderAddress.focus();
            }
            else if(!this.phone){
                this.$refs.OrderPhone.focus();
            }
            else if(this.listCart.length == 0){
                this.$router.push({ path: '/user/shop' })
            }
        },
        caculateAmount(data, item, indexCart, e) {
            console.log(data.productItem.product.cost)
            console.log(data.cartItem.quantity)
            data.realAmount = Base.formatNumber(data.productItem.product.cost * data.cartItem.quantity);
            data.amount = Base.formatNumber(data.productItem.product.cost * data.cartItem.quantity);
            console.log(data,item, indexCart, e)
        }

    }
}
</script>

<style>
.cart .order-info{
    padding: 20px;
    position: sticky;
    top: 57px;
    background: #fff;
    z-index: 1;
}
.cart .order-info.phone input {
    height: 36px;
    padding: 10px;
    border-radius: 3px;
}
.cart .food-item .food-init:hover{
    background: #fff;
}
.cart .food-item .food-init:hover img{
    transform: scale(1);
}
.cart .food-item .food-init:hover .food-name{
    font-size: 18px;
}
.cart .food-item .food-init .food-img:hover img{
    transform: scale(1.1);
}
.cart .food-size .v-select__slot input {
    padding-left: 10px;
}
.cart .food-item .food-init .food-size{
    margin-top: 0;
}
button[disabled]{
    color: var(--gray-color);
}
.cart .cart-action button[disabled]:hover{
    text-decoration: none;
    color: var(--gray-color);
}
.cart .cart-action button:hover{
    text-decoration: underline;
    color: #f2765e;
}
.cart .to-order{
    position: sticky;
    padding: 10px 0;
    bottom: 0;
    background: #fff;
}
</style>