import BaseAPI from '../../base/BaseAPI.js';
import BaseAPIConfig from '../../base/BaseAPIConfig.js';

class CategoryAPI extends BaseAPI {
    constructor() {
        super();
        this.controller = "Category";
    }

    /**
     * Phương thức lấy các danh mục thường đặt
     */
    async getPopularCategory() {
        const { data } = await BaseAPIConfig.get(`${this.controller}/GetPopularCategories`);
        return data;
    }

    async delete(body) {
        // const { data } = await BaseAPIConfig.push(`${this.controller}/delete`, body);'
        const data = await BaseAPIConfig.post(`${this.controller}/delete`, body)
        return data;
    }
}

export default new CategoryAPI();