const { PhASE_DEVELOPMENT_SERVER, PHASE_DEVELOPMENT_SERVER } = require('next/constants')

module.exports = {
    // if (phase === PHASE_DEVELOPMENT_SERVER) {
    //     return {
    //         serverRuntimeConfig: {
    //             mySecret: 'secret',
    //         }
    //     }
    // }

    publicRuntimeConfig: {
        secret: 'secreet',
    },

    serverRuntimeConfig: {
        REQ_HOST: 'http://localhost:8080',
    },

    env: {
        REQ_HOST: 'http://localhost:8080',
        STATIC_FRONT_RESOURCES: '/photo/logos'
    },
}