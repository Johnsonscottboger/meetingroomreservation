var app = new Vue({
    el: '#app',
    router: router,
    data: function () {
        return {
            isHidden: true,
            collapse: false,
            showDialog: false,
            reservationRecord: {
                meetingroom: null,
                start: null,
                end: null,
                comments: null
            },
            rules: {
                meetingroom: [
                    {required: true, message: '请选择会议室', trigger: 'blur'}
                ],
                start: [
                    {required: true, message: '请选择开始时间', trigger: 'blur'},
                    {min: new Date(), message: '请选择现在之后的时间', trigger: 'change'}
                ],
                end: [
                    {required: true, message: '请选择结束时间', trigger: 'blur'},
                    {min: new Date(), message: '请选择开始之后的时间', trigger: 'change'}
                ]
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
        },
        handleSubmit: function (formName) {
            this.$refs[formName].validate(function (valid) {
                if (valid) {
                    alert('submit!');
                    app.showDialog = false;
                } else {
                    console.log('valid error');
                    return false;
                }
            });
        },
        handleCancel: function (formName) {
            this.$refs[formName].resetFields();
            app.showDialog = false;
        }
    }
});