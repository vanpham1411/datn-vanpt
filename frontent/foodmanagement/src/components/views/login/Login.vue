<template>
<div class="login-signup">
    <div class="form-login">
        <div class="flex btn-login">
            <div class="text-title" :class="{'text-active': !isSignUp}" @click="showPass=false;isSignUp=false;$nextTick(() => { $v.$reset() })">Đăng nhập</div>
            <div class="text-title" :class="{'text-active': isSignUp}" @click="showPass=false;isSignUp=true;$nextTick(() => { $v.$reset() })">Đăng ký</div>
        </div>
        <div class="form-sign sign-in"  :class="{ 'w-0': isSignUp }"  v-if="!isSignUp">
            <div class="form-input">
                <input type="text" name="UserName" id="UserName" placeholder="Tên tài khoản" 
                        v-model="user.name" ref="UserName"
                        :class="{ 'is-invalid': $v.user.name.$error }"
                        @blur="$v.user.name.$touch()">
                <div v-if="$v.user.name.$error" class="invalid-feedback">
                    <p v-if="!$v.user.name.required">Tên tài khoản không được để trống!</p>
                </div>
            </div>
            <div class="form-input" style="position:relative;">
                <input type="password" name="Pass" id="Pass" placeholder="Mật khẩu"
                        v-model="user.password" ref="Pass" 
                        :class="{ 'is-invalid': $v.user.password.$error }"
                        @blur="$v.user.password.$touch()">
                <div v-if="$v.user.password.$error" class="invalid-feedback">
                    <p v-if="!$v.user.password.required">Mật khẩu không được để trống!</p>
                    <p v-if="$v.user.password.required && !$v.user.password.checkPassword">Mật khẩu chứa ít nhất 1 chữ cái thường, 1 chữ cái viết hoa, 1 chữ số và 1 ký tự đặc biệt!</p>
                </div>
                <i class="fa fa-eye-slash show-hide-pase pointer" v-if="showPass" @click="showHidePass(false)"></i>
                <i class="fa fa-eye show-hide-pase pointer" v-else @click="showHidePass(true)"></i>
            </div>
        </div>
        <div class="form-sign sign-up"  :class="{ 'w-0': !isSignUp }" v-if="isSignUp">
            <div class="form-input">
                <input type="text" name="UserName" id="UserName" placeholder="Tên tài khoản"
                        v-model="user.name" ref="UserName"
                        :class="{ 'is-invalid': $v.user.name.$error }"
                        @blur="$v.user.name.$touch()">
                <div v-if="$v.user.name.$error" class="invalid-feedback">
                    <p v-if="!$v.user.name.required">Tên tài khoản không được để trống!</p>
                </div>
            </div>
            <div class="form-input"> 
                <input name="Phone" id="Phone" placeholder="Số điện thoại" 
                        v-model="user.phone"  ref="Phone"
                        :class="{ 'is-invalid': $v.user.phone.$error }"
                        @blur="$v.user.phone.$touch()">
                <div v-if="$v.user.phone.$error" class="invalid-feedback">
                    <p v-if="!$v.user.phone.required">Số điện thoại không được để trống!</p>
                    <p v-if="$v.user.phone.required && !$v.user.phone.checkPhoneNumber">Số điện thoại không hợp lệ!</p>
                </div>
            </div>
            <div class="form-input">
                <input type="text" name="Address" ref="Address" id="Address" placeholder="Địa chỉ" v-model="user.address">
            </div>
            <!-- <div class="form-input"> 
                <input type="text" name="Address" id="Address" v-model="user.Address"  ref="Address" placeholder="Địa chỉ">
            </div> -->
            <div class="form-input" style="position:relative;">
                <input type="password" name="Pass" id="Pass" placeholder="Mật khẩu" 
                        v-model="user.password"  ref="Pass"
                        :class="{ 'is-invalid': $v.user.password.$error }"
                        @blur="$v.user.password.$touch()">
                <div v-if="$v.user.password.$error" class="invalid-feedback">
                    <p v-if="!$v.user.password.required">Mật khẩu không được để trống!</p>
                    <p v-if="$v.user.password.required && !$v.user.password.checkPassword">Mật khẩu chứa ít nhất 1 chữ cái thường, 1 chữ cái viết hoa, 1 chữ số và 1 ký tự đặc biệt!</p>
                </div>
                <i class="fa fa-eye-slash show-hide-pase pointer" v-if="showPass" @click="showHidePass(false)"></i>
                <i class="fa fa-eye show-hide-pase pointer" v-else @click="showHidePass(true)"></i>
            </div>
        </div>
        <div class="btn-login center">
            <button class="btn-submit" @click="login()">Tiếp</button>
        </div>
    </div>
    <div>
        <vue-popup v-if="popShow" :popShow="popShow" :action="action" :idInvalid="idInvalid" @closeForm="closeForm" :message="message" @closePop="closePop" @crudObject="login()"></vue-popup>
        <div v-for="(mgs, index) in listMgs" :key="index">
            <toast-message :titleMgs="mgs.titleMgs" :iconToast="mgs.iconToast" v-if="mgs.showToast" @deleteToast="deleteToast(index)"></toast-message>
        </div>
    </div>
</div>
</template>
<style>
@import url('../../../assets/css/login/login.css');
.show-hide-pase{
    position: absolute;
    top: 35%;
    right: 10px;
}
</style>
<script>
import {
    required,
} 
from "vuelidate/lib/validators";
import VuePopup from '../../base/VuePopup.vue';
import UserAPI from '../../../api/component/user/UserAPI';
import CartDetailAPI from '../../../api/component/user/CartDetailAPI';

import { PopupState } from '../../../base/Resources';
import Base from '../../../base/Base';
import ToastMessage from '../../base/ToastMessage.vue';
const regexPhone =/^[+]?[(]?[0-9]{2,3}[)]?[-\s.]?[2-9]{1}\d{2}[-\s.]?[0-9]{4,6}$/,
      regexPass = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?^()_#&])[A-Za-z\d@$!%*?^()_#]{6,}$/;
export default {
    components:{
        VuePopup,
        ToastMessage,
    },
    watch:{
        
    },
    name:'Login',
    data(){
        return{
            user: {
                name: "",
                password: "",
                phone: "",
                address: "",
                role:0
            },
            isSignUp: false,
            idInvalid: "",
            // action post, put, copy
            action: "",
            // hiển thị popup
            popShow: false,
            // message trong popup
            message:"",
            // toast
            mgs: {
                titleMgs: "",
                showToast: false,
                iconToast: "",
            },
            listMgs: [],
            prevRoute: null,
            showPass: false
        }
    },
    beforeRouteEnter(to, from, next) {
        next(vm => {
            vm.prevRoute = from
        })
    },
    // rule của các input
    validations: {
        user: {
            name: {
                required,
            },
            phone: {
                checkNull(){
                    if(this.isSignUp == false || this.user.phone){
                        return true;
                    }
                    else return false;
                },
                checkPhoneNumber(){
                    if(this.isSignUp == false || regexPhone.test(this.user.phone.trim()) == true){
                        return true;
                    }
                    else return false;
                }
            },
            password:{
                checkPassword(){
                    if(regexPass.test(this.user.password.trim()) == false){
                        return false;
                    }
                    else return true;
                }
            }
        }
    },
    methods:{
        showHidePass(val){
            this.showPass = val;
            document.getElementById("Pass").setAttribute("type", val?"text":"password");
        },
        /**
         * Mở popup
         */
        showPopup(action) {
            this.popShow = true;
            this.action = action;
        },

        /**
         * gọi đến hàm closeForm của ProviderList
         */
        closeForm() {
            this.$emit('closeForm');    
        },
        /**
         * delete toast message
         */
        deleteToast(index) {
            this.listMgs.splice(index, 1);
        },

        /**
         * Đóng popup
         */
        closePop() {
            // if(action == PopupState.Invalid){
            //     this.$refs[this.idInvalid].focus();
            // }
            // else if(action == PopupState.Duplicate){
            //     this.$refs[this.idInvalid].focus();
            //     this.$refs[this.idInvalid].classList.remove("is-invalid");
            // }
            this.popShow = false;
        },

        /**
         * validate khi nhấn click
         */
        validateForm() {
            this.idInvalid = "";
            this.$v.$touch();
            // nếu dữ liệu không hợp lệ
            if (this.$v.$invalid) {
                for(let key in this.user){
                    if(this.$v.user[key]){
                        if(this.$v.user[key].$error == true){
                            this.idInvalid = key;
                            this.showPopup("invalid");
                            return false;
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
        async login(){
            try {
                console.log(this.prevRoute ? this.prevRoute.path : '/');
                if(this.validateForm() == true){
                    if(this.isSignUp == false){
                        let res = await UserAPI.login(this.user);
                        console.log(res);
                        if(res.data.code == 200){
                            if(res.data.data.role === 1) {
                                res.data.data.isEmployee = true;
                            }
                            else res.data.data.isEmployee = false;
                            localStorage.setItem('user', JSON.stringify(res.data.data));
                            this.isEmployee = false;
                            setTimeout(function(){
                                Base.logout();
                            }, 86400000)
                            // console.log(time);
                            if(res.data.data.role === 1){
                                this.$emit('changeUser', res.data.data.isEmployee)
                                this.$router.push({path: '/app/home', name: 'HomeAdmin'});
                            }
                            else if(res.data.data.role === 0){
                                this.$emit('changeUser', res.data.data.isEmployee)
                                if(localStorage.getItem('addCart')){
                                    let addCart = JSON.parse(localStorage.getItem('addCart'));
                                    addCart.UserName = res.data.data.name;
                                    let result = CartDetailAPI.postObj(addCart);
                                    console.log(result);
                                    //this.$router.push({path: this.prevRoute.path});
                                }
                                console.log("route: ", this.prevRoute.name)

                                if(!this.prevRoute || this.prevRoute.name.includes('Admin')|| this.prevRoute.path.includes('product-detail-update')) {
                                    this.$router.push({path:  '/' })
                                }
                                else this.$router.push({path:  this.prevRoute.path })
                                // this.$router.push({path: this.prevRoute.name.includes() ? this.prevRoute.path : '/'});
                            }
                        }
                        else {
                            this.listMgs.push({
                                showToast: true,
                                titleMgs: "Tên tài khoản hoặc mật khẩu không đúng! Vui lòng kiểm tra lại!",
                                iconToast: 'error'
                            });
                        }
                        //console.log(res)
                    }
                    else{
                        this.user.role = 0;
                        this.user.isEmployee = false;
                        this.user.IsEmployee = false;
                        this.user.IsAdmin = false;
                        this.user.UserStatus = 0;
                        let res = await UserAPI.register(this.user);
                        console.log(res);
                        if(res.data.code == 200){
                            localStorage.setItem('user', JSON.stringify(res.data.data));
                            setTimeout(function(){
                                Base.logout();
                            }, 86400000);
                            this.$router.push({path: this.prevRoute ? this.prevRoute.path : '/'});
                        }
                        else if(res.data.code != 200 && res.data.resultCode != "FM-Exception"){
                            this.message = res.data.data[0];
                            this.showPopup(PopupState.Duplicate)
                        }
                        else {
                            this.listMgs.push({
                                showToast: true,
                                titleMgs: "Thao tác thất bại! Vui lòng kiểm tra lại kết nối mạng hoặc liên hệ để được trợ giúp",
                                iconToast: 'error'
                            });
                        }
                    }
                }
            } catch (error) {
                this.listMgs.push({
                    showToast: true,
                    titleMgs: "Thao tác thất bại! Vui lòng kiểm tra lại kết nối mạng hoặc liên hệ để được trợ giúp",
                    iconToast: 'error'
                });
            }
        },

    },
}
</script>