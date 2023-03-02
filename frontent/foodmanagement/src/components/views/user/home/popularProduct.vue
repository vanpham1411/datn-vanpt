<template>
    <div class="popular-dish">
        <h1>Sản phẩm gợi ý</h1>
        <!-- <div class="menu-dishes">
            <div class="popular-category" v-for="(category, index) in lstPopularCategory" :key="category.CategoryId" :ref="category.CategoryCode">
                <button class="popular-category-btn" :class="{'active-dish': index==activeCategory}" @click="getFoodPopular(category.CategoryId, index)">{{category.CategoryName}}</button>
            </div>
        </div> -->
        <div class="content-shop" style="text-align: center;">
            <!-- <div class="food-item pointer w-1/3" v-for="(data) in lstProductPopular" :key="data.productID"> -->
                <router-link tag="div" :to="'/user/menu/'+data.productID" class="food-item w-1/3 pointer" v-for="(data) in lstProductPopular" :key="data.productID">

                <div class="flex-center food-init">
                    <div class="w-2/5 m-r-12">
                        <div class="img-food"><img :src=" data.imageURL" alt=""></div>
                        <!-- <span class="sale" v-if="data.HasDiscount">Sale!</span> -->
                    </div>
                    <div class="w-3/5 m-l-12">
                        <div class="food-name"><b>{{data.name}}</b></div>
                        <div class="food-title">{{data.description}}</div>
                        <div class="food-size flex">
                            <!-- <div class="w-1/4">Size: <b>{{data.SizeName}}</b></div> -->
                            <div class="w-3/4">Giá: <b >{{data.cost}}</b>  VND</div>
                            <!-- <div class="w-3/4">Giá: <b :class="{'line-through': data.HasDiscount}">{{data.Amount}}</b> <b v-if="data.HasDiscount">{{formatNumber(data.RealAmount)}}</b> VND</div> -->
                        </div>
                    </div>
                </div>
                </router-link>
            <!-- </div> -->
        </div>
    </div>
    </template>

<script>
    // import CategoryAPI from '../../../../api/component/food/CategoryAPI'
    import ProductAPI from '../../../../api/component/food/ProductAPI';
    import Base from '../../../../base/Base';
    export default {
        name: 'PopularProduct',
        data(){
            return {
                // lstPopularCategory: [],
                lstProductPopular:[],
                activeCategory: 0,
                imagePath : '../../../../../data/',
            }
        },
        async created() {
            let user = JSON.parse(localStorage.getItem('user'));
            let userID  = null;
            if(user) {
                userID = user.userID
            }
            try {
                // this.lstPopularCategory = await CategoryAPI.getPopularCategory();
                this.lstProductPopular =  (await ProductAPI.suggestion(
            {
                userID: userID
            }
        )).data.data
                this.lstProductPopular.forEach(ele => {
                ele.imageURL = this.imagePath + ele.imageURL;
            })
                console.log("imagepath " , this.imagePath);

            } catch (error) {
                console.log(error);
            }
        },
        mounted(){
            // this.$nextTick(() => { 
            //     this.$el.querySelector('.popular-category button.popular-category-btn').classList.add("active-dish");
            // })
        },
        methods: {
            // async getFoodPopular(categoryId, index){
            //     this.activeCategory = index;
            //     this.lstProductPopular = await FoodAPI.getFoodPopular(categoryId);
            //     if(this.lstProductPopular.length > 0){
            //         this.lstProductPopular.forEach(ele => {  
            //             console.log(ele);
            //             if(ele.DiscountId&&(new Date(ele.DiscountStartDate) <= new Date())&&(new Date(ele.DiscountEndDate) >= new Date())){
            //                 ele.HasDiscount = true;
            //                 if(ele.Amount*ele.DiscountAmount/100 > ele.DiscountMaxAmount){
            //                     ele.RealAmount = ele.Amount - ele.DiscountMaxAmount;
            //                 }
            //                 else {
            //                     ele.RealAmount = ele.Amount - ele.Amount*ele.DiscountAmount/100;
            //                 }
            //             }
            //             else {
            //                 ele.HasDiscount = false;
            //                 ele.RealAmount = ele.Amount;
            //             }
            //             ele.Amount = Base.formatNumber(ele.Amount);
            //         });
            //     }
            // },
            formatNumber(val){
                return Base.formatNumber(val);
            }
        }
    }
    </script>


<style scoped>
    
.popular-dish {
    font-family: 'GoogleSans-Bold';
    color: #1E1D23;
    text-align: center;
    margin-bottom: 50px;
}

.popular-dish h1{
    font-size: 42px;
    line-height: 45px;
    margin-bottom: 40px;
}

.popular-dish .menu-dishes .popular-category{
    display: inline-block;
    margin: 5px 10px;
    border-radius: 28px;
}

.popular-dish .menu-dishes .popular-category .popular-category-btn{
    font-size: 14px;
    font-weight: 700;
    text-transform: uppercase;
    padding: 15px 45px;
    border-radius: 28px;
    border: 1px solid var(--gray-color2);
    background-color: #fff;
    box-shadow: none;
}

.popular-dish .menu-dishes .popular-category .popular-category-btn:hover{
    border: 1px solid var(--primary-color);
    color: black;
}

.popular-dish .menu-dishes .popular-category .popular-category-btn.active-dish{
    background-color: var(--primary-color);
}
</style>