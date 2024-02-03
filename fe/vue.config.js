const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '^/api': {
        // target: 'http://10.121.14.196:9028',
        target: 'http://localhost:8090',
        ws: true,
        changeOrigin: true,
      },
    },
  }
})

