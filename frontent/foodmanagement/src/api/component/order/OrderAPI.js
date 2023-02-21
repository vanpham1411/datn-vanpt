import BaseAPI from '../../base/BaseAPI.js';
import BaseAPIConfig from '../../base/BaseAPIConfig';

class OrderAPI extends BaseAPI {
    constructor() {
        super();
        this.controller = "Order";
    }

    /**
     * Phương thức lấy dữ liệu theo Id
     */
    async getOrderByCode(code) {
        const { data } = await BaseAPIConfig.get(`${this.controller}/GetByCode/${code}`);
        return data.data;
    }

    /**
     * Phương thức tạo bản ghi mới
     */
    async postOrder(body) {
        return await BaseAPIConfig.post(`${this.controller}/AddOrder`, body);
    }

    /**
     * Phương thức cập nhật trạng thái hóa đơn
     */
    async putStatusOrder(order) {
        return await BaseAPIConfig.put(`${this.controller}/UpdateEmployeeOrder`, order);
    }
}

export default new OrderAPI();