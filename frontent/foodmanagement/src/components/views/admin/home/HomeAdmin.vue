<template>
    <div  class="home-admin" style="height:100%">
        <div class="filter-date flex-center">
            <div class="m-r-12">Thời gian lọc: </div>
            <vue-combobox @getSelect="getSelect($event)" 
                        :combobox_valid="false" 
                        :placeholder="''" 
                        :item_text="'text'" 
                        :items="listTimeToFilter" 
                        :vmodel="dateFilter"
                        :multiple="false">
            </vue-combobox>
        </div>
        <div class="flex" style="height:calc(100vh - 250px);">
            <GChart v-if="this.dataProductSold.length>0"
                class="chart w-1/2 m-r-12"
                type="BarChart"
                :data="popularProductQuantity.chartData"
                :options="popularProductQuantity.chartOptions"
                
            />
            <div v-else class="w-1/2 m-r-12 chart-no-data">
                <b class="m-b-20">Các món bán chạy nhất</b>
                <div class="no-data">
                    <div class="w-full">
                        <img src="../../../../assets/lib/img/common/bg_report_nodata.76e50bd8.svg" alt=""><br>
                        <span class="no-data-text">Không có dữ liệu</span>
                    </div>
                </div>
            </div>
            <GChart v-if="this.dataProductSold.length>0"
                class="chart w-1/2"
                type="BarChart"
                :data="popularProductAmount.chartData"
                :options="popularProductAmount.chartOptions"
            />
            <div v-else class="w-1/2 chart-no-data">
                <b class="m-b-20">Doanh thu các sản phẩm bán chạy nhất</b>
                <div class="no-data">
                    <div class="w-full">
                        <img src="../../../../assets/lib/img/common/bg_report_nodata.76e50bd8.svg" alt=""><br>
                        <span class="no-data-text">Không có dữ liệu</span>
                    </div>
                </div>
            </div>
            <!-- <bar-chart 
                :chartData="popularProductQuantity.chartData" 
                :options="popularProductQuantity.chartOptions"></bar-chart>
            <bar-chart 
                :chartData="popularProductAmount.chartData" 
                :options="popularProductAmount.chartOptions"></bar-chart> -->
        </div>
    </div>
</template>

<script>
import { GChart } from 'vue-google-charts/legacy';
import ProductAPI from '../../../../api/component/food/ProductAPI';
import { ListTimeToFilter } from '../../../../base/vi/Resources';
import VueCombobox from '../../../base/VueCombobox.vue';
// import BarChart from './BarChart.vue'
export default {
    components: {
        GChart,
        VueCombobox,
        // BarChart,
    },
    name: 'HomeAdmin',
    data () {
        return {
            // hiển thị loading
            loader:false,
            loaderUrl: require('../../../../assets/lib/img/common/loading.svg'),
            popularProductAmount:{
                chartData: [
                    ['Sản phẩm', 'Doanh thu']
                ],
                chartOptions: {
                    title: 'Doanh thu các sản phẩm bán chạy nhất',
                    height: 500,
                }
            },
            popularProductQuantity:{
                chartData: [
                    ['Sản phẩm', 'Số lượng']
                ],
                chartOptions: {
                    title: 'Các sản phẩm bán chạy nhất',
                    colors: ['#d73a49'],
                    height: 500,
                }
            },
            dataProductSold: [],
            dateFilter:ListTimeToFilter[1],
            listTimeToFilter: ListTimeToFilter,
            user: {}
        }
    },
    async created(){
        this.user = JSON.parse(localStorage.getItem('user'));
        // console.log(this.user.isEmployee)
        await this.getSelect(this.dateFilter);
    },
    methods: {
        changeLoader(value){
            this.$emit('changeLoader', value);
        },
        async getSelect(item){
            try{
                this.dateFilter = item;
                this.dataProductSold = [];
                if(this.dataProductSold.length < 1){
                    this.changeLoader(true);
                    // console.log("filter date: ", this.dateFilter.value)
                    this.dataProductSold = await ProductAPI.getProductSold(
                        {
                            createDateMin: this.dateFilter.value.startDate,
                            createDateMax: this.dateFilter.value.endDate
                        }
                    );
                    this.changeLoader(false);
                    // console.log(this.dataProductSold);
                    this.popularProductAmount.chartData = [
                        ['Sản phẩm', 'Doanh thu']
                    ];
                    this.popularProductQuantity.chartData = [
                        ['Sản phẩm', 'Số lượng']
                    ]
                    if(this.dataProductSold){
                        
                        if(this.dataProductSold.length > 5) {
                            let sum = this.dataProductSold[5];
                            sum.name="Các sản phẩm khác"

                            for(let i = 6; i< this.dataProductSold.length; i++) {
                                sum.amount += this.dataProductSold[i].amount;
                                sum.quantity += this.dataProductSold[i].quantity;
                                sum.name="Các sản phẩm khác"
                            }
                            let listProduct = [];
                            for(let i = 0; i<5;i++) {
                                listProduct.push(this.dataProductSold[i])
                            }
                            listProduct.push(sum);
                            listProduct.forEach(ele => {
                            // ele.amount = ele.amount * ele.quantity;
                            this.popularProductAmount.chartData.push([ele.name, ele.amount])
                            this.popularProductQuantity.chartData.push([ele.name, ele.quantity])
                        })
                        }
                        else {
                            this.dataProductSold.forEach(ele => {
                            // ele.amount = ele.amount * ele.quantity;
                            this.popularProductAmount.chartData.push([ele.name, ele.amount])
                            this.popularProductQuantity.chartData.push([ele.name, ele.quantity])
                        })
                        }
                        
                    }
                }
            }
            catch(e){
                this.changeLoader(false);
                console.log(e)
            }
        },
    },
}
</script>

<style>
.filter-date{
    width: 320px;
    margin: 20px auto;
}
.home-admin .no-data{
    justify-content: center
}
.home-admin .chart-no-data{
    background: #fff;
    text-align: center;
    padding: 24px;
    height: 100%;
}
</style>