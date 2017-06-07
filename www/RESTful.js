var exec = require('cordova/exec');

exports.get = function(arg0, success, error) {
    exec(success, error, "RESTful", "get", [arg0]);
};

exports.post = function(arg0, success, error) {
    exec(success, error, "RESTful", "post", [arg0]);
};

