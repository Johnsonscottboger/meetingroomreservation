var config = function () {
    var obj = {};
    var _token = '';
    Object.defineProperty(obj, 'token', {
        configurable: true,
        enumerable: true,
        get: function () {
            return _token;
        },
        set: function (value) {
            _token = value;
            Cookies.set('token', value, {
                expires: 30
            })
        }
    });
    return obj;
}();

(function () {
    var token = Cookies.get('token');
    if (token)
        config.token = token;
})();