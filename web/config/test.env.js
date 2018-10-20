'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = {
  NODE_ENV: '"production"',
  EVN_CONFIG:'"test"',
  BASE_API: '"http://localhost:9038/bap"'
}
