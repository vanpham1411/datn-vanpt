import APIConfig from '../config/APIConfig'
import axios from 'axios';

var BaseAPIConfig = axios.create({
    baseURL: APIConfig,
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-type': 'application/json',
        'Access-Control-Allow-Credentials': 'true'
    }
});

export default BaseAPIConfig;