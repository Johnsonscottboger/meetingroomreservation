var initializer = {
    initializeAxios: function () {
        axios.defaults.headers.common["Authorization"] = "Bearer " + config.token;
    }
};


(function () {
    initializer.initializeAxios();
})();

