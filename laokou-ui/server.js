// server.js
const express = require('express')
const next = require('next')
const { createProxyMiddleware } = require("http-proxy-middleware")


const port = parseInt(process.env.PORT, 10) || 3000
const dev = process.env.NODE_ENV !== 'production'
const app = next({
    dev
})
const handle = app.getRequestHandler()

const { parseCookies, setCookie, destroyCookie } = require('nookies');

app.prepare()
    .then(() => {
        const server = express()

        if (dev) {
            server.use('/laokou',createProxyMiddleware({
                target: 'https://laokou.org.cn/laokou',
                changeOrigin: true,
                secure:false
            }))

        }
  
        server.all('*', (req, res) => {
            if(req.url.startsWith("https://laokou.org.cn/laokou")){
            }
             // Notice how the request object is passed
            //  const parsedCookies = parseCookies({ req });
  
             // Notice how the response object is passed
            //  setCookie({ res }, 'fromServer', 'value', {
            //    maxAge: 30 * 24 * 60 * 60,
            //    path: '/page',
            //  });
            //  console.log("这里")
            //  destroyCookie({ res }, 'fromServer');
            handle(req, res)
        })

        server.listen(port, err => {
            if (err) {
                throw err
            }
            console.log(`> Ready on http://localhost:${port}`)
        })
    })
    .catch(err => {
        console.log('An error occurred, unable to start the server')
        console.log(err)
    })
