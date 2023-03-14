import BaseAPI from '../../base/BaseAPI.js';
import BaseAPIConfig from '../../base/BaseAPIConfig';

class ProductAPI extends BaseAPI {
    constructor() {
        super();
        this.controller = "Product";
    }

    /**
     * Phương thức lấy dữ liệu theo Id
     */
    async getProductByCode(code) {
        const { data } = await BaseAPIConfig.get(`${this.controller}/GetByCode/${code}`);
        return data.data;
    }

    /**
     * Phương thức lấy dữ liệu theo Id
     */
    async getProductPopular(categoryId) {
        const { data } = await BaseAPIConfig.post(`${this.controller}/GetPopularProduct`, categoryId);
        return data;
    }

    /**
     * Phương thức tạo bản ghi mới
     */
    async postProduct(body) {
        return await BaseAPIConfig.post(`${this.controller}/AddProduct`, body);
    }

    /**
     * Hàm cập nhật dữ liệu
     * @param {*} body 
     */
    async putProduct(body) {
        return await BaseAPIConfig.put(`${this.controller}/UpdateProduct`, body);
    }

    async related(body) {
        return await BaseAPIConfig.post(`${this.controller}/Related`, body);
    }

    async suggestion(body) {
        return await BaseAPIConfig.post(`${this.controller}/Suggestion`, body);
    }

    async getProductSold(body) {
        const { data } = await BaseAPIConfig.post(`${this.controller}/ProductSold`, body);
        return data.data;
    }

    async viewProduct(body) {
        const { data } = await BaseAPIConfig.post(`${this.controller}/View`, body);
        return data.data;
    }

    async getAllType() {
        const  {data} = await BaseAPIConfig.get(`Type/Get`);
        return data.data
    }


}

export default new ProductAPI();