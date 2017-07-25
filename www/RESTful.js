var exec = require('cordova/exec');

exports.get = function(arg0, arg1, arg2, success, error) {
    exec(success, error, "RESTful", "get", [arg0, arg1, arg2]);
};

exports.getSimple = function(arg0, success, error) {
    exec(success, error, "RESTful", "getSimple", [arg0]);
};

exports.post = function(arg0, arg1, arg2, arg3, arg4, success, error) {
    exec(success, error, "RESTful", "post", [arg0, arg1, arg2, arg3, arg4]);
};

exports.checkAdmin = function(arg0, arg1, arg2, success, error) {
    exec(success, error, "RESTful", "checkAdmin", [arg0, arg1, arg2]);
};

exports.isReachable = function(arg0, success, error) {
    exec(success, error, "RESTful", "isReachable", [arg0]);
};

exports.checkService = function(arg0, success, error) {
    exec(success, error, "RESTful", "checkService", [arg0]);
};


