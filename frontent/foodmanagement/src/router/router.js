import Login from '@/components/views/login/Login'

import PageUser from '@/components/views/user/PageUser'
import Home from '@/components/views/user/home/home'
// import Faq from '@/components/views/user/faq/Faq'
import Shop from '@/components/views/user/shop/shop'
import Product from '@/components/views/user/shop/product'
// import About from '@/components/views/user/about/About'
import Cart from '@/components/views/user/cart/cart'
import OrderUser from '@/components/views/user/order/orderUser'
import Account from '@/components/views/user/account/account'
// import BlogList from '@/components/views/user/blog/BlogList'
// import BlogDetail from '@/components/views/user/blog/BlogDetail'

import PageAdmin from '@/components/views/admin/PageAdmin'
import HomeAdmin from '@/components/views/admin/home/HomeAdmin'
import CategoryAdmin from '@/components/views/admin/category/category'
import ProductAdmin from '@/components/views/admin/product/productAdmin'
import ProductDetail from '@/components/views/admin/product/productDetail'
import OrderAdmin from '@/components/views/admin/order/orderAdmin'
import OrderUserDetail from '@/components/views/user/order/orderUserDetail'
// import BlogAdmin from '@/components/views/admin/manage/blog/BlogAdmin'
// import AccessLog from '@/components/views/admin/manage/AccessLog'
// import SliderAdmin from '@/components/views/admin/manage/slider/SliderAdmin'
// import FaqAdmin from '@/components/views/admin/manage/faq/FaqAdmin'
// import DiscountAdmin from '@/components/views/admin/manage/discount/DiscountAdmin'
// import DiscountConditionAdmin from '@/components/views/admin/manage/discount/DiscountConditionAdmin'


export const routes = [
    { path: '/', redirect: '/user', name: 'PageUser', component: PageUser },
    { path: '/login', name: 'Login', component: Login },
    {
        path: '/user',
        redirect: 'user/home',
        name: 'PageUser',
        component: PageUser,
        children: [
            { path: 'home', name: 'Home', component: Home },
//             { path: 'faq', name: 'Faq', component: Faq },
//             { path: 'about', name: 'About', component: About },
            { path: 'cart', name: 'Cart', component: Cart },
            { path: 'order', name: 'OrderUser', component: OrderUser },
            { path: 'account', name: 'Account', component: Account },
            { path: 'shop/:categoryId?', name: 'Shop', component: Shop, props: true },
            { path: 'menu/:productID', name: 'Product', component: Product, props: true },
//             { path: 'blog', name: 'BlogList', component: BlogList },
//             { path: 'blog/:blogCode', name: 'BlogDetail', component: BlogDetail, props: true },
            { path: 'menu', redirect: 'shop' },
        ]
    },
    {
        path: '/app',
        redirect: 'app/home',
        name: 'PageAdmin',
        component: PageAdmin,
        children: [
            { path: 'home', name: 'HomeAdmin', component: HomeAdmin },
            { path: 'category', name: 'CategoryAdmin', component: CategoryAdmin },
            { path: 'product', name: 'ProductAdmin', component: ProductAdmin },
            { path: 'product-detail/:code?', name: 'ProductDetail', component: ProductDetail, props: true },
            { path: 'order', name: 'OrderAdmin', component: OrderAdmin },
            { path: 'order-detail/:code?', name: 'OrderDetail', component: OrderUserDetail, props: true },
//             { path: 'order-detail/copy/:copyCode', name: 'CopyOrderDetail', component: OrderUserDetail, props: true },
//             { path: 'blog', name: 'BlogAdmin', component: BlogAdmin },
//             { path: 'access-log', name: 'AccessLog', component: AccessLog },
//             { path: 'slider', name: 'SliderAdmin', component: SliderAdmin },
//             { path: 'faq', name: 'FaqAdmin', component: FaqAdmin },
//             { path: 'discount', name: 'DiscountAdmin', component: DiscountAdmin },
//             { path: 'discount-condition', name: 'DiscountConditionAdmin', component: DiscountConditionAdmin },
        ]
    },
]