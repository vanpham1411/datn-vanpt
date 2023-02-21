<script>
    import { Bar } from 'vue-chartjs/legacy'

    export default {
        extends: Bar,
        props: ['chartData', 'options'],
        mounted () {
            this.renderChart(this.chartData, this.options)
        }
    }
    
            // this.popularFoodAmount = {
            //     chartData:{
            //         labels: [],
            //         datasets: [{
            //             label: "Doanh thu",
            //             backgroundColor: "#f87979",
            //             data: [],
            //         }]
            //     },
            //     chartOptions: {responsive: true, maintainAspectRatio: false}
            // };
            // this.popularFoodQuantity = {
            //     chartData:{
            //         labels: [],
            //         datasets: [{
            //             label: "Số lượng bán",
            //             backgroundColor: "#f87979",
            //             data: [],
            //         }]
            //     },
            //     chartOptions: {responsive: true, maintainAspectRatio: false}
            // };
                        // this.popularFoodAmount.chartData.labels.push(ele.FoodName);
                        // this.popularFoodQuantity.chartData.labels.push(ele.FoodName);
                        // this.popularFoodAmount.chartData.datasets[0].data.push(ele.Amount);
                        // this.popularFoodQuantity.chartData.datasets[0].data.push(ele.NumOfOrder);
</script>

