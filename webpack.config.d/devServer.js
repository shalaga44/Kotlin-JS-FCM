config.devServer = config.devServer || {}; // create devServer in case it is undefined
config.devServer.historyApiFallback = true;

config.output = config.output || {}; // create output in case it is undefined
config.output.publicPath = '/'
config.devServer.host = "localhost"


