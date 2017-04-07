module.exports = {
    entry: {
      bundle: __dirname + "/src/main/js/app.js"
    },
    output: {
        path: __dirname + '/src/main/resources/static/js',
        filename: "bundle.js",
        // publicPath: "/assets/"
    },
    module: {
        loaders: [
          {
            test: /\.js[x]?$/,
            exclude: /node_modules/,
            loader: "babel",
            query:{
              presets: ['react', 'es2015']
            }
          }
        ]
    },
    resolve: {
      extensions: ["", ".webpack.js", ".web.js", ".js", "jsx"]
    }
};
