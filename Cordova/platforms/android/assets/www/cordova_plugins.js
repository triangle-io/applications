cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/io.triangle.cordova/www/triangle.js",
        "id": "io.triangle.cordova.Triangle",
        "clobbers": [
            "navigator.triangle"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "io.triangle.cordova": "1.0.0"
}
// BOTTOM OF METADATA
});