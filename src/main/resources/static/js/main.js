var app = new Vue({
    el: '#app',
    router: router,
    data: function () {
        return {
            isHidden: true,
            collapse: false,
            showDialog:false,
            reservationRecord :{
                meetingroom:'',
                start:'',
                end:'',
                comments:''
            }
        }
    },
    methods: {
        handleSelect: function (key, path) {
            console.log(key, path)
            this.$router.push(key)
        },
        handleSetting: function () {
            app.isHidden = !app.isHidden;
        },
        handleReserve: function () {
            app.showDialog = true
        }
    }
});