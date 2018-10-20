'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = {
  NODE_ENV: '"dev"',
  BASE_API: '"http://localhost:9038/bap"'
}
