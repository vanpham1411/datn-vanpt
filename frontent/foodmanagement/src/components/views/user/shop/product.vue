<template>
    <div class="shop-food">
        <div class="food">
            <div class="food-content flex">
                <div class="img-food w-1/3 m-r-12 pointer flex-center">
                    <img :src="this.imagePath + productMerge.product.imageURL" class="w-full" alt="">
                    <!-- <span class="sale" v-if="productMerge.product.hasDiscount">Sale!</span> -->
                </div>
                <div class="init-food w-2/3">
                    <div class="food-name m-b-20">{{productMerge.product.name}}</div>
                    <div class="category flex">
                        <span style="color:gray;" class="w-1/5">Danh mục: </span> 
                        <router-link tag="b" :to="'/user/shop/'+ productMerge.product.categoryID" class="w-4/5 pointer" style="color:var(--primary-color)">{{category.name}}</router-link>
                    </div>
                    <!-- <div class="food-title">{{productMerge.product.Title}}</div> -->
                    <div class="food-amount flex m-b-20">
                        <span style="color:gray;" class="w-1/5">Đơn giá: </span>
                        <div class="m-r-12" :class="{'line-through': productMerge.product.hasDiscount}">
                            <b>{{productMerge.product.cost}}</b>
                            <span v-if="!productMerge.product.hasDiscount">VND</span>
                        </div>
                        <div class="w-2/5" v-if="productMerge.product.hasDiscount">
                            <b>{{formatNumber(productMerge.product.cost)}}</b>
                            <b v-if="productMerge.items.length > 1"> - {{formatNumber(productMerge.items[productMerge.items.length-1].RealAmount)}}</b> VND
                        </div>
                    </div>
                    <div class="flex-center food-to-cart m-b-20">
                        <div class="w-1/5 food-quantity flex-center">
                            <button @click="updateQuantity('subtract')">-</button>
                            <div class="w-1/3 center"><input class="center w-full" type="number" v-model="quantity" id=quantity></div>
                            <button @click="updateQuantity('add')">+</button>
                        </div>
                        <div class="w-3/7 food-size flex-center">
                            <span class="w-1/4 m-r-12">Type: </span>
                            <vue-combobox @getSelect="changeItem($event)" v-if="!productMerge.product.hasDiscount"
                                        :combobox_valid="false" 
                                        :placeholder="''" 
                                        class="w-1/3 m-r-12"
                                        :item_text="['typeName', 'size']" 
                                        :items="productMerge.items"
                                        :display_item="'typeName'" 
                                        :groupName="['Loại', 'Kích cỡ']"
                                        :vmodel="typeModel"
                                        :multiple="false">
                            </vue-combobox>
                            <!-- <vue-combobox @getSelect="changeSize($event)" v-else
                                        :combobox_valid="false" 
                                        :placeholder="''" 
                                        class="w-1/3 m-r-12"
                                        :item_text="['SizeName', 'NumberAmount', 'RealAmount']" 
                                        :items="productMerge.items"
                                        :display_item="'SizeName'" 
                                        :groupName="['Kích thước', 'Đơn giá', 'Giá sau giảm']"
                                        :vmodel="sizeModel"
                                        :multiple="false"></vue-combobox>   -->
                            <!-- <div class="food-detail-price  w-1/3">Giá: {{amountFoodDetail}}</div> -->
                        </div>
                        <div class="w-1/3 add-to-cart">
                            <button class="flex-center pointer" @click="addToCart"><i class="fa fa-shopping-cart m-r-12"></i> Thêm vào giỏ hàng</button>
                        </div>
                        <div class="w-1/8" style="text-align: left;"><i class="fa fa-heart add-heart"></i></div>
                    </div>
                    <div class="toppings flex m-b-20" v-if="productMerge.Toppings && productMerge.Toppings.length > 0">
                        <div class="w-1/6">
                            <div class="topping-quantity">
                                <input type="checkbox" class="checkbox pointer flex-center" ref="ChooseAll" id="choose-all" />
                            </div>
                            <div class="topping-name">Topping</div> 
                            <div class="topping-price">Giá (VND)</div> 
                        </div>
                        <div class="topping-item w-1/6" v-for="(topping) in productMerge.Toppings" :key="topping.ToppingCode">
                            <div class="topping-quantity">
                                <input type="checkbox" class="checkbox pointer flex-center" ref="CheckChoose"
                                        
                                        :value="topping.ToppingId" name="topping" v-model="checkedToppings"/>
                            </div>
                            <div class="topping-name">{{topping.ToppingName}}</div> 
                            <div class="topping-price">{{topping.ToppingNumberAmount}}</div> 
                        </div>
                        <div class="w-1/6 topping-last">
                            <div class="topping-name flex-center" style="padding: 0;">Tổng giá</div> 
                            <div class="topping-price flex-center" style="height: 66.67%">{{amountToppingModel}}</div> 
                        </div>
                    </div> 
                    <div class="all-amount" style="font-size: 18px;">
                        Thành tiền: <b style="color: var(--primary-color);">{{this.amountProduct}}</b> VND
                    </div>
                     <div class="des-com-content">
                        <span style="color:gray;" class="w-1/5">Mô tả: </span> 
                        <div class="description" :class="{'d-none': !isDescription}">{{productMerge.product.description}}</div>
                     </div>

                </div>
            </div> 
            <div class="description-comment">
                <!-- <div class="btn-des-com">
                    <div class="flex-center" style="margin:auto;width:fit-content;">
                        <div class="btn-init btn-des m-r-12" :class="{'btn-active': isDescription}" @click="isDescription=true">Mô tả</div>
                        <div class="btn-init btn-com" :class="{'btn-active': !isDescription}" @click="isDescription=false">Bình luận ({{listComment.length}})</div>
                    </div>
                </div> -->
                <!-- <div class="des-com-content">
                    <div class="description" :class="{'d-none': !isDescription}">{{productMerge.product.description}}</div>
                    <div class="comments" :class="{'d-none': isDescription}">
                        <div class="comment flex">
                            <div class="w-1/5 center pointer m-r-12 comment-item" :class="{'active':star==null}"><div class="status" @click="loadComments(null)">Tất cả <div class="line-active"></div></div></div>
                            <div class="w-1/5 center pointer m-r-12 comment-item" :class="{'active':star==1}"><div class="status" @click="loadComments('1')">1 sao <div class="line-active"></div></div></div>
                            <div class="w-1/5 center pointer m-r-12 comment-item" :class="{'active':star==2}"><div class="status" @click="loadComments('2')">2 sao <div class="line-active"></div></div></div>
                            <div class="w-1/5 center pointer m-r-12 comment-item" :class="{'active':star==3}"><div class="status" @click="loadComments('3')">3 sao <div class="line-active"></div></div></div>
                            <div class="w-1/5 center pointer m-r-12 comment-item" :class="{'active':star==4}"><div class="status" @click="loadComments('4')">4 sao <div class="line-active"></div></div></div>
                            <div class="w-1/5 center pointer comment-item" :class="{'active':star==5}" @click="loadComments('5')"><div class="status">5 sao <div class="line-active"></div></div></div>
                        </div>
                        <div class="m-t-20 center">
                            <div v-if="listComment.length > 0">
                                <div v-for="(data) in listComment" :key="data.CommentId" class="m-b-12 comment-init">
                                    <div class="rate">
                                        <span class="stars-container" :class="'stars-'+data.CommentStar*20">★★★★★</span>
                                        <div class="flex" style="align-items:center">
                                            <span class="m-r-12"><b>{{data.UserName}}</b></span>
                                            <i class="fas fa-clock m-r-12" style="color:var(--gray-color)"></i>
                                            <span>{{formatDate(data.CreatedDate)}}</span>
                                        </div>
                                        <div class="content-comment">{{data.CommentContent}}</div>
                                    </div>
                                </div>
                            </div>
                            <div v-else class="w-full">
                                <img src="../../../../assets/lib/img/common/bg_report_nodata.76e50bd8.svg" alt=""><br>
                                <span class="no-data-text">Không có đánh giá</span>
                            </div>
                        </div>
                    </div>
                </div> -->
            </div>  
            <div class="related-food center" v-if="relatedProduct.length > 0">
                <div class="related-title">Sản phẩm liên quan</div>
                <div @click="reloadPage(data.productID)" class="food-item w-1/3 pointer" v-for="(data) in relatedProduct" :key="data.productID">
                    <div class="flex-center food-init">
                        <div class="w-2/5 m-r-12">
                            <div class="img-food"><img :src="'../../../../../data/' + data.imageURL" alt=""></div>
                            <span class="sale" v-if="data.hasDiscount">Sale!</span>
                        </div>
                        <div class="w-3/5 m-l-12">
                            <div class="food-name"><b>{{data.name}}</b></div>
                            <div class="food-title">{{data.description}}</div>
                            <div class="food-size flex">
                                <!-- <div class="w-1/4">Size: <b>{{data.SizeName}}</b></div> -->
                                <div class="w-3/4">Giá: <b :class="{'line-through': data.hasDiscount}">{{data.cost}}</b> <b v-if="data.hasDiscount">{{formatNumber(data.product)}}</b> VND</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>
    </template>


<script>
import ProductAPI from '../../../../api/component/food/ProductAPI';
import Base from '../../../../base/Base';
import VueCombobox from '../../../base/VueCombobox.vue';
import CartDetailAPI from '../../../../api/component/user/CartDetailAPI';
import CategoryAPI from '../../../../api/component/food/CategoryAPI'
export default {
    name: 'Product',
    components: {
        VueCombobox,
    },
    data() {
        return {
            productMerge: {
                product: {
                    hasDiscount: false,
                    productID:'',
                    code:'',
                    name:'',
                    description:'',
                    imageURL:'',
                    productView:0,
                    cost:0,
                    quantity:1
                },
                items: []
            },
            category: {
                categoryID:'',
                name:'',

            },
            quantity:1,
            isDescription: true,
            relatedProduct:[],
            imagePath : '../../../../../data/',
            amountProduct: 0,
            typeModel :{
                typeName:'',
                size:''
            }

        }
    },
    watch: {
        quantity() {
            if(!this.quantity || this.quantity < 0){
                this.quantity = 1;
            }
            if(this.quantity > this.productMerge.product.quantity) {
                this.quantity = this.productMerge.product.quantity;
            }

            this.amountProduct = this.quantity* this.productMerge.product.cost;
        }
    },
    async created() {
        this.productMerge = await ProductAPI.getProductByCode(this.$route.params.productID);
        this.category = (await CategoryAPI.getObjById(this.productMerge.product.categoryID)).data;
        let user = JSON.parse(localStorage.getItem('user'));

        if(user) {
            let view = (await ProductAPI.viewProduct({
            userID:user.userID,
            parentID: this.$route.params.productID
        }));
        console.log(view);
        }
        // this.productMerge.product.productView +=1;
        // console.log("get rproduct", this.productMerge)
        this.relatedProduct = (await ProductAPI.related(
            {
                parentID: this.$route.params.productID
            }
        )).data.data

        this.relatedProduct.forEach((el) => {
            el.hasDiscount = false;
        })


        this.productMerge.items.forEach((el) => {
            el.typeName = el.type.name;
        })
        this.typeModel = this.productMerge.items[0];
        console.log("type model: ", this.typeModel );
        
    },
    methods: {
        updateQuantity(operation){
            if(operation == 'add' ){
                if(this.quantity < this.productMerge.product.quantity) {
                    this.quantity = parseInt(this.quantity) + 1;
                }
                
            }
            else{
                this.quantity = parseInt(this.quantity) > 1 ? parseInt(this.quantity)-1 : 1;
            }
        },
        formatNumber(val){
            return Base.formatNumber(val);
        },

        reloadPage(code){
            this.$router.push({
                path: '/user/menu/'+code, 
                params: {
                    productID: code,
                }
            });
            location.reload();
        },
        formatDate(val){
            return Base.formatDateToString(val);
        },
        changeItem(item) {
            this.typeModel = item,
            this.amountProduct = Base.formatNumber(this.productMerge.product.cost* this.quantity)

        },
        async addToCart(){
            try {
                let user = JSON.parse(localStorage.getItem('user'));
                // console.log("user: ", user);
                // console.log("item: ", this.typeModel )
                // console.log("quantity: ", this.quantity )
                let bodyCartDetail = {
                    itemID: this.typeModel.itemID,
                    quantity: this.quantity,
                }
                console.log(bodyCartDetail);
                if(user){
                    bodyCartDetail.userID = user.userID; 
                    // localStorage.setItem('addCart', JSON.stringify(bodyCartDetail));
                    let result = await CartDetailAPI.postObj(bodyCartDetail);
                    if(result.status == 200){
                        this.$router.push({
                            path: '/user/cart'
                        })
                    }
                }
                else {
                    // localStorage.setItem('addCart', JSON.stringify(bodyCartDetail));
                    this.$router.push({
                        path: '/login'
                    })
                }
            } catch (error) {
                console.log(error);
            }
            
        },
        justNumber(event){
            return Base.justNumberInput(event);
        }

    }
}


</script>

<style>
.food .comment-init {
    display: inline-block;
    min-width: 400px;
    text-align: center;
}
.food .rate{
    margin: auto;
    text-align: left;
}
.food {
    padding: 20px;
    font-size: 14px;
}
.food .food-content .img-food{
    border: 1px solid var(--gray-color);
    border-radius: 15px;
    transition: .2s;
    padding: 15px;
    position: relative;
}
.food .food-content .img-food:hover{
    background: #fff9e9;
}
.food .food-size .v-select__slot input {
    padding-left: 10px;
}
.food .food-content .img-food:hover img{
    transform: scale(1.1);
}
.food .food-content .img-food img{
    transition: .2s;
    border-radius: 15px;
}
.food .init-food {
    padding: 10px 20px;
}
.food .init-food .food-name,
.food .related-title{
    font-size: 25px;
    font-weight: bold;
    height: 40px;
    color: var(--primary-color);
}
.food .init-food .food-title{
    margin: 10px 0;
    color: gray;
}
.food-quantity button{
    background-color: #e0e0e0;
    border-radius: 50%;
    height: 35px;
    width: 35px;
}
.food-quantity input:focus,
.food-quantity input:active{
    border: 0 !important;
}
.food .add-to-cart button{
    margin: auto;
    transition: .2s;
    padding: 6px 10px;
    font-weight: bold;
    border-radius: 3px;
    background-color: var(--primary-color);
}
.food .add-to-cart button:hover{
    color: ghostwhite;
}
.food-to-cart{
    justify-content: space-between;
    text-align: center;
}
.food .add-heart{
    padding: 10px;
    background-color: #e0e0e0;
    border-radius: 5px;
    cursor: pointer;
}
.description-comment{
    padding: 20px 50px;
}
.description-comment .des-com-content{
    margin: 25px;
}
.description-comment .des-com-content .description{
    white-space: pre-wrap;
}
.description-comment .btn-init {
    min-width: fit-content;
    padding: 8px 20px;
    border-radius: 5px;
    transition: .2s;
    cursor: pointer;
    font-size: 15px;
}
.description-comment .btn-init.btn-active{
    background-color: var(--primary-color);
    font-weight: bold;
}
.description-comment .btn-init.btn-active:hover{
    background-color: var(--primary-color);
    font-weight: bold;
}
.description-comment .btn-init:hover{
    background: #e0e0e0;
}
.toppings .topping-name,
.toppings .topping-price,
.toppings .topping-quantity{
    border-right: 1px dotted #c7c7c7;
    border-bottom: 1px solid #c7c7c7;
    padding: 0 10px;
    height: 25px;
    display: flex;
    align-items: center;
}
.toppings .topping-quantity{
    padding: 0;
}
.toppings .topping-quantity input {
    margin: auto;
}
.toppings .topping-last .topping-name,
.toppings .topping-last .topping-price{
    border-right: 0;
    border-bottom: 1px solid #c7c7c7;
}
.description-comment .line{
    padding: 30px 0;
    border-top: 1px solid var(--gray-color);
}
.description-comment .comment .comment-item > .status{
    width: fit-content;
    margin: auto;
    transition: .2s;
}
.description-comment .comment .line-active{
    width: 0;
    height: 3.5px;
    background-color: var(--primary-color);
    transition: .2s;
}
.description-comment .comment .comment-item > .status:hover .line-active,
.description-comment .comment .active .line-active{
    width: 100%;
}
</style>

