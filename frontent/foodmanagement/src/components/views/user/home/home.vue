<template>
    <div class="home">
        <!-- <banner></banner> -->
        <category></category>
        <!-- <introduce></introduce> -->
        <popular-product></popular-product>
    </div>
</template>

<style>
    
</style>
<script>
// import Banner from './banner.vue'
import Category from './category.vue'
// import Introduce from './introduce.vue'
import PopularProduct from './popularProduct.vue'
// import TheSelect from '../base/TheSelect.vue';
export default {
  components: { 
    // Banner,
     Category, 
    //  Introduce, 
    PopularProduct 
},
//   components: { TheSelect },
    props:{
    },
    name:'Home',
    created(){
        let user = JSON.parse(localStorage.getItem('user'));
        if(user && user.isEmployee){
            this.$emit('changeUser', false);
            this.$router.push({ path: '/app/home' });
        }
    },
}
</script>