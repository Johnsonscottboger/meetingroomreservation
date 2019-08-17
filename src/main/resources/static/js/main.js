var app = new Vue({
    el: '#app',
    router: router,
    data: function () {
        return {
            isHidden: true,
            collapse: false,
            showDialog: false,
            showLoginDialog: false,
            users: [],
            login: {
                id: ''
            },
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
            },
            loginRules: {
                name: [
                    {required: true, message: '请输入名称', trigger: 'blur'}
                ]
            }
        }
    },
    methods: {
        /**
         * 导航
         */
        handleSelect: function (key, path) {
            console.log(key, path)
            this.$router.push(key)
        },

        /**
         * 设置
         */
        handleSetting: function () {
            app.isHidden = !app.isHidden;
        },

        /**
         * 登录
         */
        handleLogin: function (form) {
            var target = this;
            if (form) {
                axios.post('/login', {
                    id: target.login.id
                }).then(function (response) {
                    if (!response.data.succ) {
                        target.$message.error(response.data.mesg);
                    }
                    target.showLoginDialog = false;
                }).catch(function (error) {
                    target.$notify.error({
                        title: '错误',
                        message: error.message
                    })
                })
            } else {
                axios.get('/login')
                    .then(function (response) {
                        if (!response.data.succ) {
                            target.showLoginDialog = true;
                        }
                    }).catch(function (error) {
                    target.$notify.error({
                        title: '错误',
                        message: error.message
                    })
                })
            }
        },

        /**
         * 预约
         */
        handleReserve: function () {


            app.showDialog = true
        },

        /**
         * 提交
         */
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

        /**
         * 取消
         */
        handleCancel: function (formName) {
            this.$refs[formName].resetFields();
            app.showDialog = false;
            app.showLoginDialog = false;
        },

        /**
         * 加载用户列表
         */
        loadUsers: function () {
            var target = this;
            userApi.getUserList()
                .then(function (response) {
                    if (!response.data.succ) {
                        target.$message.error(response.data.mesg);
                    }
                    target.users = response.data.data
                }).catch(function (error) {
                target.$notify.error({
                    title: '错误',
                    message: error.message
                })
            })
        },

        /**
         * 查询
         */
        querySearch: function (queryString, cb) {
            var results = queryString ? this.users.filter(function (value) {
                return value.name.indexOf(queryString) >= 0;
            }) : this.users;
            results = results.map(function (value) {
                return {
                    value: value.name
                }
            });
            cb(results);
        }
    },

    mounted: function () {
        this.loadUsers();
        this.handleLogin();
    }
});