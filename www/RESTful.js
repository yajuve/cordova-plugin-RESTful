var exec = require('cordova/exec');

exports.get = function(arg0, arg1, arg2, success, error) {
    exec(success, error, "RESTful", "get", [arg0, arg1, arg2]);
};

exports.post = function(arg0, arg1, arg2, arg3, success, error) {
    exec(success, error, "RESTful", "post", [arg0, arg1, arg2, arg3]);
};

