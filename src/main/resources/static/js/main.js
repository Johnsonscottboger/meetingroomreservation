var app = new Vue({
    el: '#app',
    router: router,
    data: function () {
        return {
            isHidden: true,
            collapse: false,
            showDialog: false,
            showLoginDialog: false,
            now: new Date().getHours() + ":" + new Date().getMinutes(),
            user: '',
            users: [],
            meetingrooms: [],
            handleRefresh: function () {
            },
            login: {
                id: ''
            },
            reservationRecord: {
                date: new Date(),
                meetingroom: null,
                start: null,
                end: null,
                comments: ''
            },
            rules: {
                date: [
                    {required: true, message: '请选择日期', trigger: 'blur'}
                ],
                meetingroom: [
                    {required: true, message: '请选择会议室', trigger: 'blur'}
                ],
                start: [
                    {required: true, message: '请选择开始时间', trigger: 'blur'}
                ],
                end: [
                    {required: true, message: '请选择结束时间', trigger: 'blur'}
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
                    target.user = response.data.data.id;
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
                        } else {
                            target.user = response.data.data.id;
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
            this.handleLogin();
            app.showDialog = true
        },

        /**
         * 提交
         */
        handleSubmit: function (formName) {
            var target = this;
            this.$refs[formName].validate(function (valid) {
                if (valid) {
                    var date = target.reservationRecord.date.toLocaleDateString() + " ";
                    var reservation = {
                        userId: target.user,
                        meetingRoomId: target.reservationRecord.meetingroom,
                        startTime: new Date(date + target.reservationRecord.start),
                        endTime: new Date(date + target.reservationRecord.end),
                        comments: target.reservationRecord.comments
                    };
                    reservationApi.reserve(reservation)
                        .then(function (response) {
                            if (!response.data.succ) {
                                target.$message.error(response.data.mesg);
                            } else {
                                target.showDialog = false;
                                if (target.handleRefresh)
                                    target.handleRefresh();
                            }
                        }).catch(function (error) {
                        target.$notify.error({
                            title: '错误',
                            message: error.message
                        })
                    });
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
         * 加载会议室列表
         */
        loadMeetingrooms: function () {
            var target = this;
            meetingroomApi.getMeetingRoomList()
                .then(function (response) {
                    if (!response.data.succ) {
                        target.$message.error(response.data.mesg);
                    }
                    target.meetingrooms = response.data.data;
                }).catch(function (error) {
                target.$notify.error({
                    title: '错误',
                    message: error.message
                });
            })
        }
    },

    mounted: function () {
        this.loadUsers();
        this.loadMeetingrooms();
        this.handleLogin();
        this.$router.push('/reserve-index');
    }
});