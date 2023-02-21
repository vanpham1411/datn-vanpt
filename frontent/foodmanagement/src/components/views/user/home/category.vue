<template>
    <div class="category">
        <div class="btn-menu">
        <button>MENUS</button>
        </div>
        <div class="home-menu">
            <div class="catalog" v-for="(category) in listCategory" :key="category.code">
                <router-link tag="div" :to="'/user/shop/'+category.categoryId" class="catalog-init pointer">
                    <div class="category-img">
                        <div class="hover-category"></div>
                        <!--   -->
                        <!-- <img class="img-project" alt="" :src="category.imageURL"> -->
                        <img class="img-project" alt="" src="https://bizweb.dktcdn.net/thumb/1024x1024/100/415/697/products/m0w2iuvv-1-1hxj-hinh-mat-truoc-01.jpg">

                    </div>
                    <div class="category-name">{{category.name}}</div>
                </router-link>
            </div>
        </div>
    </div>
    </template>
    <style scoped>
    @import url('../../../../assets/css/user/home/category.css');
    </style>
    <script>
    import CategoryAPI from '../../../../api/component/food/CategoryAPI'
    export default {
        name: 'Category',
        data(){
            return {
                listCategory: []
            }
        },
        async created() {
            try {
                this.listCategory = (await CategoryAPI.getFilterPaging({keyword:null,status:null})).data;
                console.log(this.listCategory)
            } catch (error) {
                console.log(error);
            }
        },
        methods: {
    
        }
    }
    </script>