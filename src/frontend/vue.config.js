module.exports = {
    // https://cli.vuejs.org/config/#devserver-proxy
    devServer: {
        port: 3000,
        proxy: {
            '/dev': {
                target: 'https://t4wkoi27s0.execute-api.us-east-1.amazonaws.com',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '/dev':''
                }
            }
        }
    }
}