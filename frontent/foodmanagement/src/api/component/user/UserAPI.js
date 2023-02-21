import BaseAPI from '../../base/BaseAPI.js';
import BaseAPIConfig from '../../base/BaseAPIConfig.js';

class UserAPI extends BaseAPI {
    constructor() {
        super();
        this.controller = "User";
    }

    /**
     * Phương thức login
     */
    async login(body) {
        return await BaseAPIConfig.post(`${this.controller}/Login`, body);
    }

    async register(body) {
        return await this.postObj(body);
    }

    async updateUser(body) {
        // let param = {
        //     user: user,
        //     userName: userName
        // }
        return await BaseAPIConfig.put(`${this.controller}/UpdateUser`, body);
    }
}

export default new UserAPI();