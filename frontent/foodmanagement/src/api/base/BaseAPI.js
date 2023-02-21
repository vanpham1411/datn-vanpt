import BaseAPIConfig from './BaseAPIConfig';

export default class BaseAPI {

    constructor() {
        this.controller = null;
    }

    /**
     * Phương thức lấy tất cả dữ liệu
     */
    async getAll() {
        const { data } = await BaseAPIConfig.get(`${this.controller}`);
        if (typeof(data) == 'string') {
            return [];
        } else return data;
    }

    /**
     * Phương thức lấy dữ liệu theo Id
     */
    async getObjById(id) {
        const { data } = await BaseAPIConfig.get(`${this.controller}/${id}`);
        return data;
    }

    /**
     * Phương thức lấy dữ liệu theo Id
     */
    async getObjByCode(code) {
        const { data } = await BaseAPIConfig.get(`${this.controller}/GetObjByCode/${code}`);
        return data.data;
    }

    /**
     * Phương thức tạo bản ghi mới
     */
    async postObj(body) {
        return await BaseAPIConfig.post(`${this.controller}`, body);
    }

    /**
     * Hàm cập nhật dữ liệu
     * @param {*} body 
     */
    async putObj(body) {
        return await BaseAPIConfig.put(`${this.controller}`, body);
    }

    /**
     * Hàm xóa bản ghi
     * @param {*} id 
     */
    async deleteObj(id) {
        return await BaseAPIConfig.delete(`${this.controller}/${id}`);
    }

    /**
     * Hàm xóa nhiều bản ghi
     * @param {*} body 
     */
    async deleteMultiObj(body) {
        return await BaseAPIConfig.delete(`${this.controller}/DeleteMulti`, { data: body });
    }

    /**
     * Phương thức phân trang
     */
    async getFilterPaging(body) {
        const { data } = await BaseAPIConfig.post(`${this.controller}/GetFilterPaging`, body);
        return data.data;
    }

    /**
     * Phương thức lấy layout
     */
    async getLayoutConfig() {
        const { data } = await BaseAPIConfig.get(`${this.controller}/GetLayoutConfig`);
        if (typeof(data) == 'string') {
            return [];
        } else return data.data;
    }

    /**
     * Hàm lấy mã code mới
     * @returns 
     */
    async getNewCode() {
        const { data } = await BaseAPIConfig.get(`${this.controller}/NewCode`);
        return data;
    }

    /**
     * Phương thức phân trang
     */
    async uploadFile(body) {
        const { data } = await BaseAPIConfig.post(`File`, body);
        return data.data
    }
}