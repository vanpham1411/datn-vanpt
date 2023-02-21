var APIConfig = {
    development: 'http://localhost:8081/api/v1/',
    production: 'http://localhost:8081/api/v1/',
    test: 'http://localhost:8081/api/v1/'
}

export default APIConfig[process.env.NODE_ENV];